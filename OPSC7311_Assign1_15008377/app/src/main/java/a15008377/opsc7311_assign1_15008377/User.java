/**
 * Author: Matthew Syrén
 *
 * Date:   27 March 2017
 *
 * Description: This class defines the data needed to create and read user data
 */

package a15008377.opsc7311_assign1_15008377;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class User {
    private String emailAddress;
    private int gamesPlayed;
    private int gamesWon;

    //Constructor
    public User(String emailAddress){
        this.emailAddress = emailAddress;
        gamesPlayed = 0;
        gamesWon = 0;
    }

    //Default constructor (needed for Firebase when a DataSnapshot is retrieved)
    public User(){

    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    //Method stores the user's username in the SharedPreferences of the device, which will keep them signed in every time they open the app
    public void setActiveUsername(String username, Context context){
        try{
            SharedPreferences preferences = context.getSharedPreferences("", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", username);
            editor.apply();
        }
        catch(Exception exc){
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method fetches the username of the user currently signed in
    public String getActiveUsername(Context context){
        String username = null;
        try{
            SharedPreferences preferences = context.getSharedPreferences("", Context.MODE_PRIVATE);
            username = preferences.getString("username", null);
        }
        catch(Exception exc){
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_LONG).show();
        }
        return username;
    }
}