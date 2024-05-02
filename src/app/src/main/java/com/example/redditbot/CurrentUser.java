package com.example.redditbot;

import masecla.reddit4j.client.Reddit4J;
import masecla.reddit4j.client.UserAgentBuilder;

public class CurrentUser {

    private static final CurrentUser instance = new CurrentUser();
    private final FirebaseDB firebaseDBInstance = FirebaseDB.getInstance();
    private String username;
    private String deviceId;
    private String agentId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
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
