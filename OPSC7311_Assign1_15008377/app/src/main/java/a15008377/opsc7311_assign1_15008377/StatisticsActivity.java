package a15008377.opsc7311_assign1_15008377;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

        displayUserStatistics();
        populateListView();
    }

    public void displayUserStatistics(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("matthewsyren");
        final TextView txtUserStatistics = (TextView) findViewById(R.id.text_user_statistics);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                txtUserStatistics.setText("Games Played: " + user.gamesPlayed + "\nGames Won: " + user.gamesWon + "\nWin Rate: " + user.gamesPlayed / user.gamesWon * 100 + "%");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FRB", "Failed to read value.", error.toException());
            }
        });
    }


    //Method fetches all data from Firebase and sorts it, before displaying the data in the ListView
    public void populateListView(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final ListView listView = (ListView) findViewById(R.id.list_best_win_rates);
        final Context context = this;
        final ArrayList<UserRankingDetails> lstUsers = new ArrayList<>();

        //Adds Listeners for when the data is changed
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Loops through all users and adds each user to the lstUsers ArrayList
                Iterable<DataSnapshot> lstSnapshots = dataSnapshot.getChildren();
                for(DataSnapshot snapshot : lstSnapshots){
                    String key = snapshot.getKey();
                    User user = snapshot.getValue(User.class);
                    UserRankingDetails userRankingDetails = new UserRankingDetails(key, determineWinRate(user.gamesPlayed, user.gamesWon));
                    lstUsers.add(userRankingDetails);
                }

                sortUsers(lstUsers);

                ListViewAdapter adapter = new ListViewAdapter(context, lstUsers);
                listView.setAdapter(adapter);
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
        for(int i = 0; i < lstUsers.size(); i++){
            double winRate1 = lstUsers.get(i).getWinRate();
            for(int j = i + 1; j < lstUsers.size(); j++){
                double winRate2 = lstUsers.get(j).getWinRate();
                if(winRate1 < winRate2){
                    UserRankingDetails tempUser = lstUsers.get(i);
                    lstUsers.set(i,  lstUsers.get(j));
                    lstUsers.set(j, tempUser);
                }
            }
        }
    }

    //Method calculates the win rate of the user and returns the win rate
    public static int determineWinRate(int gamesPlayed, int gamesWon){
        int winRate;
        if(gamesWon == 0){
            winRate = 0;
        }
        else{
            winRate =  gamesWon / gamesPlayed * 100;
        }
        return winRate;
    }
}