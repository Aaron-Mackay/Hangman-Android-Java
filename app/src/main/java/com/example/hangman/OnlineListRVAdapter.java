package com.example.hangman;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hangman.ui.main.OnlineFragment;

import java.io.IOException;
import java.util.List;

public class OnlineListRVAdapter extends RecyclerView.Adapter<OnlineListRVAdapter.ViewHolder> {
    Handler handler;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView listnameTextView;
        public Button downloadListButton;
        public Button scoreboardButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            listnameTextView = (TextView) itemView.findViewById(R.id.listNameRow);
            downloadListButton = (Button) itemView.findViewById(R.id.downloadListButton);
            scoreboardButton = (Button) itemView.findViewById(R.id.scoreboardButton);


        }
    }

    // Store a member variable for the contacts
    private List<String> mGoalStringNameList;
    private Context mContext;

    // Pass in the list array into the constructor
    public OnlineListRVAdapter(List<String> goalStringNameList, Context context) {
        mGoalStringNameList = goalStringNameList;
        mContext = context;
    }

    //Store list of name selected
    String goalStringListName;

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public OnlineListRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.online_rv_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(OnlineListRVAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        goalStringListName = mGoalStringNameList.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.listnameTextView;
        Button downloadListButton = viewHolder.downloadListButton;
        Button scoreboardButton = viewHolder.scoreboardButton;

        //Set properties of those views
        textView.setText(goalStringListName);

        //Creates onclicklistener for each list row, taking the list name and using to fetch and download data
        View.OnClickListener downloadListOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String selectedList = mGoalStringNameList.get(position);
                handler = new Handler();
                new Thread() {
                    public void run() {
                        try {
                            APIFetch.apiFetch(selectedList, mContext);
                            handler.post(new Runnable() {
                                public void run() {
                                    mGoalStringNameList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mGoalStringNameList.size());
                                    Toast.makeText(mContext,
                                            String.format("%s downloaded", selectedList),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(mContext,
                                            String.format("Failed to download %s", selectedList),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }.start();
            }
        };

        View.OnClickListener scoreboardOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo intent to scoreboard page
            }
        };

        downloadListButton.setOnClickListener(downloadListOnClickListener);
        scoreboardButton.setOnClickListener(scoreboardOnClickListener);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mGoalStringNameList.size();
    }


}
