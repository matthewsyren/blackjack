/**
 * Author: Matthew Syrén
 *
 * Date:   27 March 2017
 *
 * Description: This class defines the data needed to create and read user data
 */

package a15008377.opsc7311_assign1_15008377;

public class User {
    private int gamesPlayed;
    private int gamesWon;

    //Default constructor
    public User(){
        gamesPlayed = 0;
        gamesWon = 0;
    }

    //Constructor
    public User(int gamesPlayed, int gamesWon){
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
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
}