package a15008377.opsc7311_assign1_15008377;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The actual game play happens on this activity
 * The user will see their hand, as well as their current score
 * The user will be given the option to Hit (add another card) or Stay (keep their current hand) until the game ends
 */

public class GameActivity extends AppCompatActivity {
    //Declarations
    ArrayList<Integer> deckOfCards;
    ArrayList<Integer> userHand;
    ArrayList<Integer> dealerHand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        userHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        populateDeckOfCards();

        //Deals 2 cards each to the player and the dealer
        for(int i = 0; i < 2; i++){
            dealCard(userHand);
            dealCard(dealerHand);
        }

        //Displays the user's hand
        displayUserHand();
    }

    //Method displays the user's hand
    public void displayUserHand(){
        final CardAdapter adapter = new CardAdapter(this, userHand);
        GridView gridView = (GridView) findViewById(R.id.hand_of_cards);
        gridView.setAdapter(adapter);
    }

    //Method adds all cards to the deckOfCards ArrayList
    public void populateDeckOfCards() {
        try{
            deckOfCards = new ArrayList<>();
            deckOfCards.add(R.drawable.two_of_clubs);
            deckOfCards.add(R.drawable.two_of_diamonds);
            deckOfCards.add(R.drawable.two_of_hearts);
            deckOfCards.add(R.drawable.two_of_spades);
            deckOfCards.add(R.drawable.three_of_clubs);
            deckOfCards.add(R.drawable.three_of_diamonds);
            deckOfCards.add(R.drawable.three_of_hearts);
            deckOfCards.add(R.drawable.three_of_spades);
            deckOfCards.add(R.drawable.four_of_clubs);
            deckOfCards.add(R.drawable.four_of_diamonds);
            deckOfCards.add(R.drawable.four_of_hearts);
            deckOfCards.add(R.drawable.four_of_spades);
            deckOfCards.add(R.drawable.five_of_clubs);
            deckOfCards.add(R.drawable.five_of_diamonds);
            deckOfCards.add(R.drawable.five_of_hearts);
            deckOfCards.add(R.drawable.five_of_spades);
            deckOfCards.add(R.drawable.six_of_clubs);
            deckOfCards.add(R.drawable.six_of_diamonds);
            deckOfCards.add(R.drawable.six_of_hearts);
            deckOfCards.add(R.drawable.six_of_spades);
            deckOfCards.add(R.drawable.seven_of_clubs);
            deckOfCards.add(R.drawable.seven_of_diamonds);
            deckOfCards.add(R.drawable.seven_of_hearts);
            deckOfCards.add(R.drawable.seven_of_spades);
            deckOfCards.add(R.drawable.eight_of_clubs);
            deckOfCards.add(R.drawable.eight_of_diamonds);
            deckOfCards.add(R.drawable.eight_of_hearts);
            deckOfCards.add(R.drawable.eight_of_spades);
            deckOfCards.add(R.drawable.nine_of_clubs);
            deckOfCards.add(R.drawable.nine_of_diamonds);
            deckOfCards.add(R.drawable.nine_of_hearts);
            deckOfCards.add(R.drawable.nine_of_spades);
            deckOfCards.add(R.drawable.ten_of_clubs);
            deckOfCards.add(R.drawable.ten_of_diamonds);
            deckOfCards.add(R.drawable.ten_of_hearts);
            deckOfCards.add(R.drawable.ten_of_spades);
            deckOfCards.add(R.drawable.jack_of_clubs);
            deckOfCards.add(R.drawable.jack_of_diamonds);
            deckOfCards.add(R.drawable.jack_of_hearts);
            deckOfCards.add(R.drawable.jack_of_spades);
            deckOfCards.add(R.drawable.queen_of_clubs);
            deckOfCards.add(R.drawable.queen_of_diamonds);
            deckOfCards.add(R.drawable.queen_of_hearts);
            deckOfCards.add(R.drawable.queen_of_spades);
            deckOfCards.add(R.drawable.king_of_clubs);
            deckOfCards.add(R.drawable.king_of_diamonds);
            deckOfCards.add(R.drawable.king_of_hearts);
            deckOfCards.add(R.drawable.king_of_spades);
            deckOfCards.add(R.drawable.ace_of_clubs);
            deckOfCards.add(R.drawable.ace_of_diamonds);
            deckOfCards.add(R.drawable.ace_of_hearts);
            deckOfCards.add(R.drawable.ace_of_spades);
        }
        catch(NullPointerException nfp){
            Toast.makeText(getApplicationContext(), nfp.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method removes a card from the deckOfCards ArrayList and adds it to the ArrayList that is passed in
    public void dealCard(ArrayList<Integer> lstCards){
        try{
            int cardNumber = (int) (Math.random() * deckOfCards.size());
            lstCards.add(deckOfCards.get(cardNumber));
            deckOfCards.remove(cardNumber);
        }
        catch(NullPointerException nfp){
            Toast.makeText(getApplicationContext(), nfp.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method gives a card to the player that decides to hit
    public void hitPlayer(View view){
        try{
            dealCard(userHand);
            displayUserHand();
        }
        catch(NullPointerException nfp){
            Toast.makeText(getApplicationContext(), nfp.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}