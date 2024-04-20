package com.example.redditbot;

import java.util.ArrayList;

public class Subreddit {
    private String name;
    private Integer maxPosts;
    private ArrayList<String> terms;

    public Subreddit() {
    }
    public Subreddit(String name, Integer maxPosts, ArrayList<String> terms) {
        this.name = name;
        this.maxPosts = maxPosts;
        this.terms = terms;
    }

    public String getName() { return name; }
    public Integer getMaxPosts() { return maxPosts; }
    public ArrayList<String> getTerms() { return terms; }

    public void setName(String name) { this.name = name; }
    public void setMaxPosts(Integer maxPosts) { this.maxPosts = maxPosts; }
    public void setTerms(ArrayList<String> terms) { this.terms = terms; }
}
