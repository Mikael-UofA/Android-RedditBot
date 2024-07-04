package com.example.redditbot.Containers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import masecla.reddit4j.objects.RedditPost;

public class PostList implements Serializable {
    private final ArrayList<RedditPost> posts = new ArrayList<>();

    public PostList() {
    }


    public void addAll(List<RedditPost> posts1) {
        posts.addAll(posts1);
    }
    public int size() {
        return posts.size();
    }

    /**
     * This returns how many posts are in the list
     */
    public int count() {
        return posts.size();
    }

    public RedditPost getRedditPost(int pos) {
        return posts.get(pos);
    }
}

