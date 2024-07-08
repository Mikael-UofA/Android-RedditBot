package com.example.redditbot.Containers;

import com.example.redditbot.DataHolders.Subreddit;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a wrapper for {@link ArrayList<Subreddit>}
 */
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
     * This returns a list of subreddits
     * @return the list
     */
    public ArrayList<Subreddit> getSubreddits() {
        return subreddits;
    }

    /**
     * Removes subreddit from list if it exists
     * Otherwise will throw an error if the item to be deleted doesn't exist
     * @param subreddit The subreddit we want to remove from the list
     */
    public void replace(Subreddit subreddit, Integer position) {
        subreddits.remove((int) position);
        subreddits.add(position, subreddit);
    }

    /**
     * This returns how many subreddit are in the list.
     * @return an int
     */
    public int count() {
        return subreddits.size();
    }

    /**
     * This removes a subreddit from the list at a specific position
     * @param pos The position of the subreddit that needs to be removed
     */
    public void remove(int pos) {
        subreddits.remove(pos);
    }

    /**
     * This returns the Subreddit object at a specific position
     * @param pos The position at which a Subreddit object needs to be retrieved
     * @return a Subreddit object
     */
    public Subreddit getSubreddit(int pos) {
        return subreddits.get(pos);
    }
}
