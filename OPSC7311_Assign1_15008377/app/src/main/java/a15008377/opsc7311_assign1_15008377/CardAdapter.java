/**
 * Author: Matthew Syrén
 *
 * Date: 27 March 2017
 *
 * Description: This class populates the appropriate GridView on GameActivity with the appropriate cards
 *              There are 2 GridViews, 1 representing the user's hand and 1 representing the dealer's hand
 *              lstImages contains the images that need to be displayed in the GridView
 */

package a15008377.opsc7311_assign1_15008377;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

public class CardAdapter extends ArrayAdapter {
    //Declarations
    private Context context;
    private ArrayList<Integer> lstImages;

    //Constructor
    public CardAdapter(Context context, ArrayList<Integer> lstImages){
        super(context, R.layout.card, lstImages);
        this.context = context;
        this.lstImages = lstImages;
    }

    //Method inflates a card and assigns it the appropriate image, and adds it to the appropriate GridView
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Component assignments
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.card, null);
        ImageView image = (ImageView) convertView.findViewById(R.id.individual_card);

        //Assigns the next item in the lstImages ArrayList to a card and adds it to the appropriate GridView
        image.setImageResource(lstImages.get(position));
        return convertView;
    }
}