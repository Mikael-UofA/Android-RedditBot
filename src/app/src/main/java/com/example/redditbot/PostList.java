package com.example.redditbot;

import java.util.ArrayList;

import masecla.reddit4j.objects.RedditPost;

public class PostList {
    private final ArrayList<RedditPost> posts = new ArrayList<>();

    public PostList() {
    }

    /**
     * This adds a post to the list if the post does not exist
     * @param post This is a candidate RedditPost to add
     * @throws IllegalArgumentException If the post that is being added is already in the list
     */
    public void add(RedditPost post) {
        if (posts.contains(post)) {
            throw new IllegalArgumentException();
        }
        posts.add(post);
    }
    /**
     * This returns a sorted list of posts
     * @return
     * Return the sorted list
     */
    public ArrayList<RedditPost> getRedditPosts() {
        return posts;
    }
    /**
     * This returns a boolean that represents whether or not
     * this list contains a specific post
     * @param post This is the post we're trying to find in the list
     */
    public boolean has(RedditPost post) {
        return posts.contains(post);
    }
    /**
     * Removes post from list if it exists
     * Otherwise will throw an error if the item to be deleted doesn't exist
     * @param post The post we want to remove from the list
     */
    public void replace(RedditPost post, int position) {
        posts.remove(position);
        posts.add(position, post);
    }
    /**
     * This returns how many posts are in the list
     */
    public int count() {
        return posts.size();
    }

    public void remove(int pos) {
        posts.remove(pos);
    }

    public RedditPost getRedditPost(int pos) {
        return posts.get(pos);
    }
}

