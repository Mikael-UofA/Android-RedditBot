package com.example.redditbot.Containers;

import com.example.redditbot.DataHolders.Subreddit;

import java.io.Serializable;
import java.util.ArrayList;

public class SubredditList implements Serializable {
    private final ArrayList<Subreddit> subreddits = new ArrayList<>();

    public SubredditList() {
    }

    /**
     * This adds a subreddit to the list if the subreddit does not exist
     * @param subreddit This is a candidate Subreddit to add
     * @throws IllegalArgumentException If the subreddit that is being added is already in the list
     */
    public void add(Subreddit subreddit) {
        if (subreddits.contains(subreddit)) {
            throw new IllegalArgumentException();
        }
        subreddits.add(subreddit);
    }
    /**
     * This returns a sorted list of subreddits
     * @return
     * Return the sorted list
     */
    public ArrayList<Subreddit> getSubreddits() {
        return subreddits;
    }

    /**
     * Removes subreddit from list if it exists
     * Otherwise will throw an error if the item to be deleted doesn't exist
     * @param subreddit The subreddit we want to remove from the list
     */
    public void replace(Subreddit subreddit, int position) {
        subreddits.remove(position);
        subreddits.add(position, subreddit);
    }
    /**
     * This returns how many subreddits are in the list
     */
    public int count() {
        return subreddits.size();
    }

    public void remove(int pos) {
        subreddits.remove(pos);
    }

    public Subreddit getSubreddit(int pos) {
        return subreddits.get(pos);
    }
}
