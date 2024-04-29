package com.example.redditbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
public class SubredditAdapter extends ArrayAdapter<Subreddit> {
    private final ArrayList<Subreddit> subreddits;
    private final Context context;

    public SubredditAdapter(Context context, SubredditList subreddits) {
        super(context, 0, subreddits.getSubreddits());
        this.subreddits = subreddits.getSubreddits();
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.subreddits_content, parent,false);
        }

        Subreddit subreddit = subreddits.get(position);

        TextView subredditName = view.findViewById(R.id.subreddit_text);

        String displayName = "r/" + subreddit.getName();
        subredditName.setText(displayName);

        return view;

    }


}
