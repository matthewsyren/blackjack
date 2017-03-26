package a15008377.opsc7311_assign1_15008377;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by matthewsyren on 11/03/2017.
 */

public class ListViewAdapter extends ArrayAdapter {
    //Declarations
    Context context;
    ArrayList<LeaderBoardDetails> lstUsers;

    //Constructor
    public ListViewAdapter(Context context, ArrayList<LeaderBoardDetails> lstUsers){
        super(context, R.layout.list_item_high_score, lstUsers);
        this.context = context;
        this.lstUsers = lstUsers;
    }

    //Method
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Component assignments
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_item_high_score, null);
        TextView txtRanking = (TextView) convertView.findViewById(R.id.text_ranking);
        TextView txtUsername = (TextView) convertView.findViewById(R.id.text_username);
        TextView txtWinRate = (TextView) convertView.findViewById(R.id.text_win_rate);

        txtRanking.setText((position + 1) + "");
        txtUsername.setText(lstUsers.get(position).getUsername() + "");
        txtWinRate.setText(Math.round(lstUsers.get(position).getWinRate()) + "%");
        return convertView;
    }
}