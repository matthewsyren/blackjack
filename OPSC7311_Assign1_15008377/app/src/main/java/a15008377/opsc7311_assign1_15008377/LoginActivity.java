package a15008377.opsc7311_assign1_15008377;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.DisplayContext;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    //Declarations
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            if(new User().getActiveUsername(this) != null){
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            firebaseAuth = FirebaseAuth.getInstance();
            toggleProgressBarVisibility(View.INVISIBLE);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method initiates the password recovery feature of this app
    public void forgotPasswordOnClick(View view){
        try{
            displayInputMessage("Please enter your email address. An email with a link to reset your password will be sent to you.");
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method displays an AlertDialog to get a username from the user
    public void displayInputMessage(String message){
        try{
            //Creates AlertDialog content
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            TextView textView = new TextView(this);
            textView.setText(message);
            textView.setTypeface(null, Typeface.BOLD);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

            //Adds content to AlertDialog
            LinearLayout relativeLayout = new LinearLayout(this);
            relativeLayout.setOrientation(LinearLayout.VERTICAL);
            relativeLayout.addView(textView);
            relativeLayout.addView(input);
            alertDialog.setView(relativeLayout);

            //Creates OnClickListener for the Dialog message
            DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int button) {
                    switch(button){
                        case AlertDialog.BUTTON_POSITIVE:
                            String emailAddress = input.getText().toString();
                            if(emailAddress.length() > 0){
                                toggleProgressBarVisibility(View.VISIBLE);

                                //Sends the email to the user if the email address is valid
                                firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Displays message telling the user the email has been sent successfully
                                            Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            String exceptionMessage = task.getException().getMessage();

                                            //Displays appropriate error messages based on the exception message details
                                            if(exceptionMessage.contains("There is no user record corresponding to this identifier. The user may have been deleted.")){
                                                Toast.makeText(getApplicationContext(), "There is no account associated with that email address, please re-enter your email address", Toast.LENGTH_LONG).show();
                                            }
                                            else if(exceptionMessage.contains("INVALID_EMAIL")){
                                                Toast.makeText(getApplicationContext(), "The email you entered is invalid, please ensure that the email address you enter exists", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        toggleProgressBarVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else{
                                //Asks the user to enter a valid email address
                                displayInputMessage("Please enter your email address");
                            }
                            break;
                    }
                }
            };

            //Assigns button and OnClickListener for the AlertDialog and displays the AlertDialog
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", dialogOnClickListener);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Fetches the user's input and sends the input for authentication
    public void loginOnClick(View view){
        try{
            EditText txtEmailAddress = (EditText) findViewById(R.id.text_email_address);
            EditText txtPassword = (EditText) findViewById(R.id.text_password);

            String emailAddress = txtEmailAddress.getText().toString();
            String password = txtPassword.getText().toString();

            if(emailAddress.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_LONG).show();
            }
            else if(password.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_LONG).show();
            }
            else{
                signUserIn(emailAddress, password);
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method toggles the ProgressBar's visibility
    public void toggleProgressBarVisibility(int visibility){
        try{
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_login);
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

    //Method authenticates the login details using Firebase, and sets the active username for the app if the user signs in successfully
    public void signUserIn(final String emailAddress, String password){
        try{
            toggleProgressBarVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("FBA", "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w("FBA", "signInWithEmail", task.getException());
                        toggleProgressBarVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Incorrect email address or password, please try again", Toast.LENGTH_LONG).show();
                    }
                    else{
                        //Takes the user to the HomeActivity once their account has been created
                        getUsername(emailAddress);
                    }
                }
            });
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method fetches the username of the user based on their email address
    public void getUsername(final String emailAddress){
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if(user.getEmailAddress().equalsIgnoreCase(emailAddress)){
                            Toast.makeText(getApplicationContext(), "Welcome, " + snapshot.getKey(), Toast.LENGTH_LONG).show();
                            new User().setActiveUsername(snapshot.getKey(), getApplicationContext());
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method takes the user to the CreateAccountActivity
    public void createAccountOnClick(View view){
        try{
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
