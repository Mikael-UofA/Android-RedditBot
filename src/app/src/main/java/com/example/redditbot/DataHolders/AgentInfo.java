package com.example.redditbot.DataHolders;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class AgentInfo implements Serializable {
    private String agentClientId;
    private String agentClientSecret;
    private String agentAppName;
    private String agentAuthorName;

    public AgentInfo(){
    }
    public AgentInfo(String agentClientId, String agentClientSecret, String agentAppName, String agentAuthorName) {
        this.agentClientId = agentClientId;
        this.agentClientSecret = agentClientSecret;
        this.agentAppName = agentAppName;
        this.agentAuthorName = agentAuthorName;
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
