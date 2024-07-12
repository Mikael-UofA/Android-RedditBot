package com.example.redditbot.DataHolders;

import java.io.Serializable;

/**
 * This class holds the information of an agent for the purposes of this app.
 * This includes the client id, client secret, the app name, and the author.
 */
public class AgentInfo implements Serializable {
    private String agentClientId;
    private String agentClientSecret;
    private String agentAppName;
    private String agentAuthorName;

    public AgentInfo(){
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
    public void setAgentClientId(String agentClientId) {
        this.agentClientId = agentClientId;
    }
    public void setAgentClientSecret(String agentClientSecret) {
        this.agentClientSecret = agentClientSecret;
    }
    public void setAgentAppName(String agentAppName) {
        this.agentAppName = agentAppName;
    }
    public void setAgentAuthorName(String agentAuthorName) {
        this.agentAuthorName = agentAuthorName;
    }
}
