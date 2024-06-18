package com.example.redditbot;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import masecla.reddit4j.client.Reddit4J;
import masecla.reddit4j.client.UserAgentBuilder;
import masecla.reddit4j.exceptions.AuthenticationException;
import masecla.reddit4j.objects.RedditPost;
import masecla.reddit4j.objects.Sorting;
import masecla.reddit4j.requests.SubredditPostListingEndpointRequest;

public class Client {
    private Reddit4J client;
    private final Sorting sorting = Sorting.NEW;

    public Client() {
    }


    public Boolean beginAuthentication(AgentInfo agent) {
        if (agent == null) {
            throw new NullPointerException("User does not have a agent");
        }
        try {

            client = Reddit4J.rateLimited().setUsername(agent.getAgentUsername())
                    .setPassword(agent.getAgentPass())
                    .setClientId(agent.getAgentClientId()).setClientSecret(agent.getAgentClientSecret())
                    .setUserAgent(new UserAgentBuilder().appname(agent.getAgentAppName()).author(agent.getAgentAuthorName()).version("1.0"));
            client.connect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ArrayList<RedditPost> getTopPosts(Subreddit subreddit){
        SubredditPostListingEndpointRequest request = client.getSubredditPosts(subreddit.getName(), sorting);
        List<RedditPost> posts;
        try {
            posts =  request.submit();
        } catch (Exception e) {
            return new ArrayList<>();
        }
        int maxPosts = subreddit.getMaxPosts();
        ArrayList<RedditPost> returnedPosts = new ArrayList<>();

        for (int i = 0; i < maxPosts; i++) {
            String title = posts.get(i).getTitle();
            String text = posts.get(i).getSelftext();
            for (String term : subreddit.getTerms()) {
                if (title.toLowerCase().contains(term.toLowerCase()) || text.toLowerCase().contains(term.toLowerCase())) {
                    returnedPosts.add(posts.get(i));
                    break;
                }
            }
        }
        return returnedPosts;
    }
    public Reddit4J getClient() {
        return client;
    }

}
