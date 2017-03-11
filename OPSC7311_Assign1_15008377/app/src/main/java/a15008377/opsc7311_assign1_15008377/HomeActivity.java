package a15008377.opsc7311_assign1_15008377;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

       // displayInputMessage("Please enter a username for yourself");
    }

    //Method displays an AlertDialog to get a username from the user
    public void displayInputMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);

        //Adds EditText to AlertDialog
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        //Adds confirmation button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                    checkUsername(username);
                }
                else{
                    displayInputMessage("Please enter a username that only contains letters (no spaces or symbols)");
                }
            }
        });

        builder.show();
    }

    //Method checks if the desired username is taken, if it is, the user is prompted for a new username; if the username is free, the user's information is written to the database
    public void checkUsername(final String username){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child(username);

        //Checks if username is taken. If the username is taken, the user must choose a new one; if the username is not taken, the information is added to the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Displays the prompt for a username again, as the desired username is taken already
                    displayInputMessage(username + " is already taken, please choose another username.");
                }
                else{
                    //Writes the new username's information to the Firebase Database
                    User user = new User(0, 0);
                    myRef.setValue(user);

                    //Removes this ValueEventListener to prevent another prompt for a username once the data is written to Firebase
                    myRef.removeEventListener(this);
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
}