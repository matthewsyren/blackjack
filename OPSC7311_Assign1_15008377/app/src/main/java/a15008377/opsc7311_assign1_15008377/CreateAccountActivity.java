package a15008377.opsc7311_assign1_15008377;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateAccountActivity extends AppCompatActivity {
    //Declarations
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_account);

            firebaseAuth = FirebaseAuth.getInstance();

            toggleProgressBarVisibility(View.INVISIBLE);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method toggles the ProgressBar's visibility
    public void toggleProgressBarVisibility(int visibility){
        try{
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_create_account);
            progressBar.setVisibility(visibility);
            if(visibility == View.VISIBLE){
                //Prevents user from clicking other buttons when the ProgressBar is visible. Learnt from https://stackoverflow.com/questions/36918219/how-to-disable-user-interaction-while-progressbar-is-visible-in-android
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            else{
                //Allows the user to click other buttons when the ProgressBar is invisible
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method creates a new account for the user using the entered email address, username and password
    public void createAccount(final String emailAddress, final String username, final String password){
        try{
            //Creates a new account for the user
            firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        String exception = task.getException().getMessage();
                        if(exception.contains("WEAK_PASSWORD")){
                            //Displays message telling the user to choose a stronger password
                            Toast.makeText(getApplicationContext(), "The password you have entered is too weak, please enter a password with at least 8 characters", Toast.LENGTH_LONG).show();
                        }
                        else if(exception.contains("The email address is already in use by another account.")){
                            //Displays a message telling the user to choose another email address, as their desired email has already been used
                            Toast.makeText(getApplicationContext(), "The email address you have entered has already been used for this app, please enter another email address", Toast.LENGTH_LONG).show();
                        }
                        else if(exception.contains("The email address is badly formatted.")){
                            //Displays a message telling the user to enter a valid email address
                            Toast.makeText(getApplicationContext(), "The email address you have entered is invalid, please enter a valid email address e.g. yourname@example.com", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        saveDetailsToDatabase(emailAddress, username);
                        Toast.makeText(getApplicationContext(), "Account successfully created!", Toast.LENGTH_LONG).show();

                        //Takes the user to the HomeActivity once their account has been created
                        Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    toggleProgressBarVisibility(View.INVISIBLE);
                }
            });
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method validates the input before attempting to create an account once the input has been validated
    public void createAccountOnClick(View view){
        try{
            EditText txtEmailAddress = (EditText) findViewById(R.id.text_email_address);
            EditText txtUsername = (EditText) findViewById(R.id.text_username);
            EditText txtPassword = (EditText) findViewById(R.id.text_password);
            EditText txtConfirmPassword = (EditText) findViewById(R.id.text_confirm_password);

            String emailAddress = txtEmailAddress.getText().toString();
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();

            //Converts the username to lower case, as the app requires the username to be lower case
            username = username.toLowerCase();

            if(emailAddress.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_LONG).show();
            }
            else if(username.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please enter your desired username", Toast.LENGTH_LONG).show();
            }
            else if(password.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_LONG).show();
            }
            else if(!password.equals(txtConfirmPassword.getText().toString())){
                Toast.makeText(getApplicationContext(), "Your passwords don't match, please ensure that they match", Toast.LENGTH_LONG).show();
            }
            else{
                //Ensures that the user's username only contains letters
                boolean valid = true;
                for(int i = 0; i < username.length() && valid; i++){
                    if(!Character.isAlphabetic(username.charAt(i))){
                        valid = false;
                    }
                }

                //Attempts to create the account if the username only contains letters
                if(valid){
                    checkUsername(emailAddress, username, password);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter a username that only contains letters", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method checks if the desired username is taken. If it is, the user is prompted for a new username; if the username is free, the user's information is written to the database
    public void checkUsername(final String emailAddress, final String username, final String password){
        try{
            if(checkInternetConnection()){
                toggleProgressBarVisibility(View.VISIBLE);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReference().child(username);

                //Checks if username is taken. If the username is taken, the user must choose a new one; if the username is not taken, the information is added to the database
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //Displays the prompt for a username again, as the desired username is taken already
                            Toast.makeText(getApplicationContext(), username + " is already taken, please choose another username.", Toast.LENGTH_LONG).show();
                            toggleProgressBarVisibility(View.INVISIBLE);
                        }
                        else{
                            //Attempts to create the user's account if the username is free
                            createAccount(emailAddress, username, password);
                            databaseReference.removeEventListener(this);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        //Failed to read value
                        Toast.makeText(getApplicationContext(), "Failed to read data, please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method saves the user's username and game statistics (gamesPlayed = 0, gamesWon = 0) to the database
    public void saveDetailsToDatabase(String emailAddress, String username){
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference().child(username);

            //Writes the new username's information to the FireBase Database
            User user = new User(emailAddress);
            databaseReference.setValue(user);

            //Signs the user in once their account is created
            user.setActiveUsername(username, this);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method checks the Internet connection, and returns true if there is an internet connection, and false if there is no internet connection
    public boolean checkInternetConnection(){
        boolean connected = true;
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            //Displays a message if there is no internet connection
            if (!(networkInfo != null && networkInfo.isConnected())) {
                Toast.makeText(getApplicationContext(), "Please check your internet connection...", Toast.LENGTH_LONG).show();
                connected = false;
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
        return connected;
    }
}