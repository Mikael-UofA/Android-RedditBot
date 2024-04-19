package com.example.redditbot;

public class UserAgent {
    private String agentUsername;
    private String agentPass;
    private String agentClientId;
    private String agentClientSecret;
    private String agentAppName;
    private String agentAuthorName;

    public UserAgent(){
    }
    public UserAgent(String agentUsername, String agentPass, String agentClientId, String agentClientSecret, String agentAppName, String agentAuthorName) {
        this.agentUsername = agentUsername;
        this.agentPass = agentPass;
        this.agentClientId = agentClientId;
        this.agentClientSecret = agentClientSecret;
        this.agentAppName = agentAppName;
        this.agentAuthorName = agentAuthorName;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public String getAgentPass() {
        return agentPass;
    }

    public String getAgentClientId() {
        return agentClientId;
    }

    public String getAgentClientSecret() {
        return agentClientSecret;
    }

    public String getAgentAppName() {
        return agentAppName;
    }

    public String getAgentAuthorName() {
        return agentAuthorName;
    }
}
