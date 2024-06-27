package com.example.redditbot.DataHolders;

import java.io.Serializable;
import java.util.ArrayList;

public class Subreddit implements Serializable {
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
    public String getName() { return name == null ? "N/A" : name; }
    public Integer getMaxPosts() { return maxPosts == null ? 1 : maxPosts; }
    public ArrayList<String> getTerms() { return terms == null ? new ArrayList<>() : terms; }

    public void setName(String name) { this.name = name; }
    public void setMaxPosts(Integer maxPosts) { this.maxPosts = maxPosts; }
    public void setTerms(ArrayList<String> terms) { this.terms = terms; }
}
