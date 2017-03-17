package a15008377.opsc7311_assign1_15008377;

/**
 * Created by matthewsyren on 11/03/2017.
 */

public class UserRankingDetails {
    private String username;
    private double winRate;

    public UserRankingDetails(String username, double winRate) {
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
