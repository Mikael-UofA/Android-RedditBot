package com.example.redditbot;

import java.io.Serializable;

import masecla.reddit4j.client.Reddit4J;
import masecla.reddit4j.client.UserAgentBuilder;

public class CurrentUser implements Serializable {

    private static final CurrentUser instance = new CurrentUser();
    private AgentInfo agent;
    private SubredditList subreddits;
    private Reddit4J client;
    private Boolean connected;

    private CurrentUser() {
        this.connected = false;
    }

    public static CurrentUser getInstance() {
        return instance;
    }

    public AgentInfo getAgent() {
        return agent;
    }

    public void setAgent(AgentInfo agent) {
        this.agent = agent;
    }

    public SubredditList getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(SubredditList subreddits) {
        this.subreddits = subreddits;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
}
