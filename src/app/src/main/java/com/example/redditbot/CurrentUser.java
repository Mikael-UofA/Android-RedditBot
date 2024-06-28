package com.example.redditbot;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.redditbot.Containers.SubredditList;
import com.example.redditbot.DataHolders.AgentInfo;
import com.example.redditbot.DataHolders.Client;
import com.example.redditbot.DataHolders.Subreddit;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CurrentUser implements Serializable {
    private static final CurrentUser instance = new CurrentUser();
    private AgentInfo agent;
    private SubredditList subreddits;
    private final Client client;

    private CurrentUser() {
        this.subreddits = new SubredditList();
        this.client = new Client();
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
    public void addSubreddit(Subreddit subreddit) {
        this.subreddits.add(subreddit);
    }
    public void editSubreddit(Subreddit subreddit, int position) {
        this.subreddits.replace(subreddit, position);
    }
    public void setClientInfo(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.setInfo(agent);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Authentication Successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Authentication Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        thread.start();
    }
    public Client getClient() {
        return client;
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
