package a15008377.opsc7311_assign1_15008377;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

/**
 * Created by matthewsyren on 18/02/2017.
 * Class displays the cards in the GridView on GameActivity
 */

public class CardAdapter extends ArrayAdapter {
    //Declarations
    Context context;
    ImageView image;
    ArrayList lstImages;

    //Constructor
    public CardAdapter(Context context, ArrayList lstImages){
        super(context, R.layout.card,lstImages);
        this.context = context;
        this.lstImages = lstImages;
    }

    //Method
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Component assignments
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        image = (ImageView) convertView.findViewById(R.id.individual_card);

        //Assigns the next item in the lstImages ArrayList to a card
        image.setImageResource((int) lstImages.get(position));
        return convertView;
    }
}