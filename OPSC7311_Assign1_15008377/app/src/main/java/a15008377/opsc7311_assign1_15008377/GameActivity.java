package a15008377.opsc7311_assign1_15008377;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    int userScore = 0;
    int dealerScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
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

            //Calculates the user and dealers' scores
            userScore = calculateScore(userHand);
            dealerScore = calculateScore(dealerHand);

            //Displays the user's hand
            displayUserHand();
            displayHiddenDealerHand();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method displays the user's hand
    public void displayUserHand(){
        try{
            displayHand(userHand, R.id.hand_of_cards);

            //Displays the user's score
            userScore = calculateScore(userHand);
            TextView txtUserScore = (TextView) findViewById(R.id.label_user_score);

            if (userScore > 21){
                txtUserScore.setText("Your score is " + userScore + " points. You have gone bust...");
                disableButtons();
                determineWinner();
            }
            else{
                txtUserScore.setText("Your score is " + userScore + " points...");
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method displays the dealer's hand
    public void displayHiddenDealerHand(){
        try{
            ArrayList<Integer> dealerHiddenHand = new ArrayList<>();
            dealerHiddenHand.add(dealerHand.get(0));
            dealerHiddenHand.add(R.drawable.back_of_card);

            displayHand(dealerHiddenHand, R.id.dealer_hand_of_cards);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method displays the hand of the appropriate player by populating the view that is passed in with the data in the ArrayList that is passed in
    public void displayHand(ArrayList<Integer> lstCards, int view){
        try{
            final CardAdapter adapter = new CardAdapter(this, lstCards);
            GridView gridView = (GridView) findViewById(view);
            gridView.setAdapter(adapter);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method removes a card from the deckOfCards ArrayList and adds it to the ArrayList that is passed in
    public void dealCard(ArrayList<Integer> lstCards){
        try{
            //Plays card_deal sound each time a card is dealt
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.card_deal);
            mediaPlayer.start();

            //Pauses program execution until the card_deal sound has played fully
            while (mediaPlayer.isPlaying()) {
            }
            int cardNumber = (int) (Math.random() * deckOfCards.size());
            lstCards.add(deckOfCards.get(cardNumber));
            deckOfCards.remove(cardNumber);
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
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method calculates the scores for each user
    public int calculateScore(ArrayList<Integer> lstCards) {
        int score = 0;
        int aceCount = 0;
        try{
            for (int i = 0; i < lstCards.size(); i++) {
                int cardNumber = lstCards.get(i);

                //Method assigns value to score based on the hexadecimal value of the images stored in the player's hand
                if (cardNumber >= R.drawable.two_of_clubs && cardNumber <= R.drawable.two_of_spades) {
                    score += 2;
                } else if (cardNumber >= R.drawable.three_of_clubs && cardNumber <= R.drawable.three_of_spades) {
                    score += 3;
                } else if (cardNumber >= R.drawable.four_of_clubs && cardNumber <= R.drawable.four_of_spades) {
                    score += 4;
                } else if (cardNumber >= R.drawable.five_of_clubs && cardNumber <= R.drawable.five_of_spades) {
                    score += 5;
                } else if (cardNumber >= R.drawable.six_of_clubs && cardNumber <= R.drawable.six_of_spades) {
                    score += 6;
                } else if (cardNumber >= R.drawable.seven_of_clubs && cardNumber <= R.drawable.seven_of_spades) {
                    score += 7;
                } else if (cardNumber >= R.drawable.eight_of_clubs && cardNumber <= R.drawable.eight_of_spades) {
                    score += 8;
                } else if (cardNumber >= R.drawable.nine_of_clubs && cardNumber <= R.drawable.nine_of_spades) {
                    score += 9;
                } else if (cardNumber >= R.drawable.ace_of_clubs && cardNumber <= R.drawable.ace_of_spades) {
                    score += 11;
                    aceCount += 1;
                } else {
                    score += 10;
                }
            }

            //Sets the appropriate number of aces to 1 if the user goes bust
            if(score > 21 && aceCount > 0){
                while(aceCount > 0 && score > 21){
                    score -= 10;
                    aceCount--;
                }
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
        return score;
    }

    //Method deals the rest of the dealer's hand (once the player chooses to stay)
    public void finishDealingDealerHand(View view){
        try{
            disableButtons();
            displayHand(dealerHand, R.id.dealer_hand_of_cards);
            dealerScore = calculateScore(dealerHand);
            boolean dealAgain = dealerScore < 17;
            while(dealAgain){
                dealCard(dealerHand);
                dealerScore = calculateScore(dealerHand);
                dealAgain = dealerScore < 17;
                displayHand(dealerHand, R.id.dealer_hand_of_cards);
            }
            determineWinner();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method outputs the winner of the game, and offers the user the chance to play again
    public void determineWinner(){
        try{
            AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();

            //Determines the winner based on the user and dealers' scores
            if(userScore > 21){
                updateUserStatistics(false);
                alertDialog.setTitle("You lose...");
                alertDialog.setMessage("You went bust, with a score of " + userScore + ".\n\nWould you like to play again?");
            }
            else if(dealerScore > 21){
                updateUserStatistics(true);
                alertDialog.setTitle("YOU WIN!");
                alertDialog.setMessage("The dealer went bust, with a score of " + dealerScore + ". \n\nWould you like to play again?");
            }
            else{
                if(dealerScore > userScore){
                    updateUserStatistics(false);
                    alertDialog.setTitle("You lose...");
                }
                else if(userScore > dealerScore){
                    updateUserStatistics(true);
                    alertDialog.setTitle("YOU WIN!");
                }
                else{
                    updateUserStatistics(false);
                    alertDialog.setTitle("You draw!");
                }
                alertDialog.setMessage("The dealer scored " + dealerScore + ", while you scored " + userScore + ". \n\nWould you like to play again?");
            }

            //Creates OnClickListener for the Dialog message
            DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int button) {
                    Intent intent;
                    switch(button){
                        case AlertDialog.BUTTON_POSITIVE:
                            intent = getIntent();
                            finish();
                            startActivity(intent);
                            break;
                        case AlertDialog.BUTTON_NEGATIVE:
                            intent = new Intent(GameActivity.this, HomeActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            };

            //Assigns buttons and OnClickListener for the AlertDialog and displays the AlertDialog
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", dialogOnClickListener);
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", dialogOnClickListener);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method disables the buttons on the screen to prevent the user from clicking them when they are not supposed to
    public void disableButtons(){
        try{
            Button btnHit = (Button) findViewById(R.id.button_hit);
            Button btnStay = (Button) findViewById(R.id.button_stay);
            btnHit.setEnabled(false);
            btnStay.setEnabled(false);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method updates the user's statistics after each game
    public void updateUserStatistics(final boolean win){
        SharedPreferences preferences = getSharedPreferences("", Context.MODE_PRIVATE);
        String username = preferences.getString("username", null);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference().child(username);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                int gamesPlayed = user.getGamesPlayed();
                user.setGamesPlayed(++gamesPlayed);
                if(win){
                    int gamesWon = user.getGamesWon();
                    user.setGamesWon(++gamesWon);
                }
                databaseReference.setValue(user);
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FRB", "Failed to read value.", error.toException());
            }
        });
    }
}