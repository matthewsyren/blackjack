package a15008377.opsc7311_assign1_15008377;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Main Menu for the game
 * Allows the user to access the other activities for the game
 */

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hides progress bar, disables the buttons and displays the input message asking for a username
        toggleButtons(false);
        displayInputMessage("Please enter a username for yourself");
    }

    //Method either enables or disables buttons, depending on the value of the boolean that is passed in
    public void toggleButtons(boolean enabled){
        //Button assignments
        Button btnPlayGame = (Button) findViewById(R.id.button_play);
        Button btnHowToPlay = (Button) findViewById(R.id.button_how_to_play);
        Button btnStatistics = (Button) findViewById(R.id.button_statistics);

        //Toggles buttons' state
        btnPlayGame.setEnabled(enabled);
        btnHowToPlay.setEnabled(enabled);
        btnStatistics.setEnabled(enabled);
    }

    //Method displays an AlertDialog to get a username from the user
    public void displayInputMessage(String message){
        //Hides the ProgressBar while the user inputs their details
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_home);
        progressBar.setVisibility(View.INVISIBLE);

        //Displays AlertDialog asking for the user's username is the user hasn't already set a username (the username is stored in SharedPreferences with the 'username' key)
        SharedPreferences preferences = getSharedPreferences("", Context.MODE_PRIVATE);
        if(preferences.getString("username", null) == null){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(message);

            //Adds EditText to AlertDialog
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            alertDialog.setView(input);

            //Creates OnClickListener for the Dialog message
            DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int button) {
                    switch(button){
                        //Checks if the username is valid (length > 0 and every character is an alphabetic character)
                        case AlertDialog.BUTTON_POSITIVE:
                            String username = input.getText().toString();
                            boolean valid = true;
                            if(username.length() == 0){
                                valid = false;
                            }
                            for(int i = 0; i < username.length() && valid == true; i++){
                                if(!Character.isAlphabetic(username.charAt(i))){
                                    valid = false;
                                }
                            }
                            if(valid){
                                //Checks for duplicate usernames if the username is valid
                                checkUsername(username);
                            }
                            else{
                                //Asks the user to enter a valid username
                                displayInputMessage("Please enter a username that only contains letters (no spaces or symbols)");
                            }
                            break;
                    }
                }
            };

            //Assigns button and OnClickListener for the AlertDialog and displays the AlertDialog
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", dialogOnClickListener);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
        else{
            toggleButtons(true);
        }
    }

    //Method checks if the desired username is taken, if it is, the user is prompted for a new username; if the username is free, the user's information is written to the database
    public void checkUsername(final String username){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child(username);

        //Displays the ProgressBar while the data is being fetched from FireBase
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_home);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();


        //Checks if username is taken. If the username is taken, the user must choose a new one; if the username is not taken, the information is added to the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Displays the prompt for a username again, as the desired username is taken already
                    displayInputMessage(username + " is already taken, please choose another username.");
                }
                else{
                    //Writes the new username's information to the Firebase Database
                    User user = new User();
                    databaseReference.setValue(user);

                    //Signs the user in
                    setActiveUsername(username);

                    //Removes this ValueEventListener to prevent another prompt for a username once the data is written to FireBase
                    progressBar.setVisibility(View.INVISIBLE);
                    databaseReference.removeEventListener(this);
                    toggleButtons(true);
                    Toast.makeText(getApplicationContext(), "Welcome, " + username + "!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FRB", "Failed to read value.", error.toException());
            }
        });
    }

    //Method opens the GameActivity and starts the game
    public void startGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    //Method opens the StatisticsActivity
    public void startStatistics(View view){
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    //Method stores the user's username, which will keep them signed in every time they open the app
    public void setActiveUsername(String username){
        SharedPreferences preferences = getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.commit();
    }
}