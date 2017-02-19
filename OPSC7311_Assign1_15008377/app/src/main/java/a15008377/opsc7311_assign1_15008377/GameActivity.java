package a15008377.opsc7311_assign1_15008377;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import java.util.ArrayList;

/**
 * The actual game play happens on this activity
 * The user will see their hand, as well as their current score
 * The user will be given the option to Hit (add another card) or Stay (keep their current hand) until the game ends
 */

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ArrayList lstImages = new ArrayList();
        lstImages.add(R.drawable.ace_of_clubs);
        lstImages.add(R.drawable.ace_of_diamonds);

        final CardAdapter adapter = new CardAdapter(this, lstImages);
        GridView gridView = (GridView) findViewById(R.id.hand_of_cards);
        gridView.setAdapter(adapter);
    }
}