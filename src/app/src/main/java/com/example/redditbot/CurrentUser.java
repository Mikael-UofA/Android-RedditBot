package com.example.redditbot;

import java.util.ArrayList;

//import masecla.reddit4j.client.Reddit4J;
//import masecla.reddit4j.client.UserAgentBuilder;

public class CurrentUser {

    private static final CurrentUser instance = new CurrentUser();
    private final FirebaseDB firebaseDBInstance = FirebaseDB.getInstance();
    private String username;
    private String pass;
    private String agentId;
    private UserAgent agent;
    private SubredditList subreddits;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        return instance;
    }

    public void startInitialization(String username, String pass) {
        this.username = username;
        this.pass = pass;
        firebaseDBInstance.createUser();
    }
    public Boolean startLogin(String username, String pass) {
        this.username = username;
        this.pass = pass;
        final Boolean[] successful = {false};
        firebaseDBInstance.loginUser(username, pass, new FirebaseDB.GetBooleanCallBack() {
            @Override
            public void onResult(Boolean bool) {
                successful[0] = bool;
            }
        });
        return successful[0];
    }

//    public void beginAuthentication() {
//        if (agent == null) {
//            throw new NullPointerException("User does not have a agent");
//        }
//        try {
//            Reddit4J client = Reddit4J.rateLimited().setUsername(agent.getAgentUsername())
//                    .setPassword(agent.getAgentPass())
//                    .setClientId(agent.getAgentClientId()).setClientSecret(agent.getAgentClientSecret())
//                    .setUserAgent(new UserAgentBuilder().appname(agent.getAgentAppName()).author(agent.getAgentAuthorName()).version("1.0"));
//            client.connect();
//        } catch (Exception e) {
//            throw new RuntimeException("Something went wrong when connecting client: " + e);
//        }
//    }

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

    public SubredditList getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(SubredditList subreddits) {
        this.subreddits = subreddits;
    }
}
