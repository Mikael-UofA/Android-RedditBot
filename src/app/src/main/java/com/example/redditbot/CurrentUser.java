package com.example.redditbot;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    public void saveAgentInfo(Context context) {
        try (FileOutputStream fos = context.openFileOutput("agent-info.ser", Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.agent);
        } catch (IOException e) {
            Log.w("FileSaving", "Error: " + e);
        }
    }
    public void saveSubreddits(Context context) {
        try (FileOutputStream fos = context.openFileOutput("subreddits.ser", Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.subreddits);
        } catch (IOException e) {
            Log.w("FileSaving", "Error: " + e);
        }
    }
}
