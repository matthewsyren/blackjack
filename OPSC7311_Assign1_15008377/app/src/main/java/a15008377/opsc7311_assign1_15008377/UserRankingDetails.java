package a15008377.opsc7311_assign1_15008377;

/**
 * Created by matthewsyren on 11/03/2017.
 */

public class UserRankingDetails {
    private String username;
    private int winRate;

    public UserRankingDetails(String username, int winRate) {
        this.username = username;
        this.winRate = winRate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWinRate(int winRate) {
        this.winRate = winRate;
    }

    public String getUsername() {
        return username;

    }

    public int getWinRate() {
        return winRate;
    }
}
