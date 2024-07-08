package com.example.redditbot.Containers;

import com.example.redditbot.DataHolders.Subreddit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import masecla.reddit4j.objects.RedditPost;

/**
 * This class is a wrapper for {@link ArrayList<RedditPost>}
 */
public class PostList implements Serializable {
    private final ArrayList<RedditPost> posts = new ArrayList<>();

    public PostList() {
    }


    /**
     * Adds all RedditPosts from a list to the list in this class.
     * @param posts1 The list to retrieve all posts from
     */
    public void addAll(List<RedditPost> posts1) {
        posts.addAll(posts1);
    }

    /**
     * This returns how many posts are in the list.
     * @return an int
     */
    public int size() {
        return posts.size();
    }

    /**
     * This returns the RedditPost object at a specific position
     * @param pos The position at which a RedditPost object needs to be retrieved
     * @return a RedditPost object
     */
    public RedditPost getRedditPost(int pos) {
        return posts.get(pos);
    }
}

