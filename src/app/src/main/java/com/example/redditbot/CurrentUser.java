package com.example.redditbot;

public class CurrentUser {

    private static final CurrentUser instance = new CurrentUser();
    private String username;
    private String pass;
    private String agentId;
    private UserAgent agent;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        return instance;
    }

    public void Initialization(String deviceId) {
        // Create file
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public UserAgent getAgent() {
        return agent;
    }

    public void setAgent(UserAgent agent) {
        this.agent = agent;
    }
}
