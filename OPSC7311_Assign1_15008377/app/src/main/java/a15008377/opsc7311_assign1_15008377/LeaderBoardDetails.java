/**
 * Author: Matthew Syrén
 *
 * Date:   27 March 2017
 *
 * Description: This class defines the data needed to display and sort the leader board
 */

package a15008377.opsc7311_assign1_15008377;

@SuppressWarnings("WeakerAccess")
public class LeaderBoardDetails {
    private String username;
    private double winRate;

    //Constructor
    public LeaderBoardDetails(String username, double winRate) {
        this.username = username;
        this.winRate = winRate;
    }

    public String getUsername() {
        return username;
    }

    public double getWinRate() {
        return winRate;
    }
}