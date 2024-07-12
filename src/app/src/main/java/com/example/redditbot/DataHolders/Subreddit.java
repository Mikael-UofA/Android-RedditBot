package com.example.redditbot.DataHolders;

import java.io.Serializable;
import java.util.ArrayList;

import masecla.reddit4j.objects.Sorting;

/**
 * This class holds the information of subreddit for the purposes of this app.
 * This includes the name of the subreddit, the maximum number of posts to look through
 * and the search terms to use.*/
public class Subreddit implements Serializable {
    private final String name;
    private Integer maxPosts;
    private ArrayList<String> terms;
    private Sorting sorting;

    public Subreddit(String name, Integer maxPosts, ArrayList<String> terms, Sorting sorting) {
        this.name = name;
        this.maxPosts = maxPosts;
        this.terms = terms;
        this.sorting = sorting;
    }
    public String getName() { return name == null ? "N/A" : name; }
    public Integer getMaxPosts() { return maxPosts == null ? 1 : maxPosts; }
    public ArrayList<String> getTerms() { return terms == null ? new ArrayList<>() : terms; }
    public Sorting getSorting() {
        return sorting;
    }
    public void setMaxPosts(Integer maxPosts) { this.maxPosts = maxPosts; }
    public void setTerms(ArrayList<String> terms) { this.terms = terms; }
    public void setSorting(Sorting sorting) {
        this.sorting = sorting;
    }
}
