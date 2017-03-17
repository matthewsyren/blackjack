package a15008377.opsc7311_assign1_15008377;

/**
 * Created by matthewsyren on 06/03/2017.
 */

public class User {
    private int gamesPlayed;
    private int gamesWon;

    public User(){
        gamesPlayed = 0;
        gamesWon = 0;
    }

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