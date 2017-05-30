/**
 * Author: Matthew Syrén
 *
 * Date:   27 March 2017
 *
 * Description: Main Menu for the game
 *              Allows the user to access the other activities for the game
 */

package a15008377.opsc7311_assign1_15008377;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
        }
        catch(Exception exc){
            displayToast(exc.getMessage());
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
                displayToast("Please check your internet connection...");
                connected = false;
            }
        }
        catch(Exception exc){
            displayToast(exc.getMessage());
        }
        return connected;
    }

    //Method opens the GameActivity and starts the game
    public void startGame(View view){
        try{
            //Opens the GameActivity if there is an internet connection
            if(checkInternetConnection()){
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
            }
        }
        catch(Exception exc){
            displayToast(exc.getMessage());
        }
    }

    //Method opens the StatisticsActivity
    public void startStatistics(View view){
        try{
            //Opens the Statistics Activity if there is an internet connection
            if(checkInternetConnection()){
                Intent intent = new Intent(this, StatisticsActivity.class);
                startActivity(intent);
            }
        }
        catch(Exception exc){
            displayToast(exc.getMessage());
        }
    }

    //Method opens the How To Play activity
    public void startHowToPlay(View view){
        try{
            Intent intent = new Intent(HomeActivity.this, HowToPlayActivity.class);
            startActivity(intent);
        }
        catch(Exception exc){
            displayToast(exc.getMessage());
        }
    }

    //Method displays a Toast message with the String parameter as its value
    public void displayToast(String message){
        try{
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
        catch(Exception exc){
            displayToast(exc.getMessage());
        }
    }

    //Method signs the user out of their account
    public void signOutOnClick(View view){
        try{
            //Signs the user out of the app and Firebase
            new User().setActiveUsername(null, this);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        catch(Exception exc){
            displayToast(exc.getMessage());
        }
    }
}