package a15008377.opsc7311_assign1_15008377;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        toggleLabelVisibility(View.INVISIBLE);
        displayUserStatistics();
        populateListView();
    }

    //Method fetches the statistics for the current user and displays the statistics
    public void displayUserStatistics(){
        //Displays the ProgressBar (which stays displayed until the data from FireBase is fetched
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_statistics);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        //Fetches the data from FireBase for the current user
        SharedPreferences preferences = getSharedPreferences("", Context.MODE_PRIVATE);
        final String username = preferences.getString("username", "");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child(username);
        final TextView txtUserStatistics = (TextView) findViewById(R.id.text_user_statistics);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                txtUserStatistics.setText("Username: " + username + "\nGames Played: " + user.getGamesPlayed() + "\nGames Won: " + user.getGamesWon() + "\nWin Rate: " + Math.round(determineWinRate(user.getGamesPlayed(), user.getGamesWon())) + "%");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FRB", "Failed to read value.", error.toException());
            }
        });
    }

    //Method fetches all data from FireBase and sorts it, before displaying the data in the ListView
    public void populateListView(){
        //Fetches all users' data from FireBase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final ListView listView = (ListView) findViewById(R.id.list_best_win_rates);
        final Context context = this;

        //Adds Listeners for when the data is changed
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<UserRankingDetails> lstUsers = new ArrayList<>();

                //Loops through all users and adds each user to the lstUsers ArrayList
                Iterable<DataSnapshot> lstSnapshots = dataSnapshot.getChildren();
                for(DataSnapshot snapshot : lstSnapshots){
                    String key = snapshot.getKey();
                    User user = snapshot.getValue(User.class);
                    UserRankingDetails userRankingDetails = new UserRankingDetails(key, determineWinRate(user.getGamesPlayed(), user.getGamesWon()));
                    Log.i("key", key + "  " + user.getGamesPlayed() + "  " + user.getGamesWon());
                    lstUsers.add(userRankingDetails);
                }

                //Sorts the users in order of win rate, and then displays the users in the ListView
                sortUsers(lstUsers);
                ListViewAdapter adapter = new ListViewAdapter(context, lstUsers);
                listView.setAdapter(adapter);


                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                LinearLayout linearLayout = (LinearLayout)inflater.inflate( R.layout.list_view_header, null, false );
                listView.addHeaderView(linearLayout);

                //Hides the ProgressBar once the data is displayed
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_statistics);
                progressBar.setVisibility(View.INVISIBLE);
                toggleLabelVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FRB", "Failed to read value.", error.toException());
            }
        });
    }

    //Method sorts the lstUsers ArrayList by the user's win percentage in ascending order
    public void sortUsers(ArrayList<UserRankingDetails> lstUsers){
        boolean swapped = true;
        int j = 0;
        UserRankingDetails temp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < lstUsers.size() - j; i++) {
                if (lstUsers.get(i).getWinRate() < lstUsers.get(i + 1).getWinRate()) {
                    temp = lstUsers.get(i);
                    lstUsers.set(i, lstUsers.get(i + 1));
                    lstUsers.set((i + 1), temp);
                    swapped = true;
                }
            }
        }
    }

    //Method calculates the win rate of the user and returns the win rate
    public static double determineWinRate(int gamesPlayed, int gamesWon){
        double winRate;
        if(gamesWon == 0){
            winRate = 0;
        }
        else{
            winRate =  (double) gamesWon / gamesPlayed * 100;
        }
        return winRate;
    }

    //Method hides or displays the labels on the Activity, depending on the value passed in as a parameter
    public void toggleLabelVisibility(int visible){
        TextView txtYourStatistics = (TextView) findViewById(R.id.label_user_statistics);
        TextView txtLeaderBoard = (TextView) findViewById(R.id.label_leader_board);

        txtYourStatistics.setVisibility(visible);
        txtLeaderBoard.setVisibility(visible);
    }
}