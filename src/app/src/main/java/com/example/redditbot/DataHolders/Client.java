package com.example.redditbot.DataHolders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import masecla.reddit4j.client.Reddit4J;
import masecla.reddit4j.client.UserAgentBuilder;
import masecla.reddit4j.exceptions.AuthenticationException;
import masecla.reddit4j.objects.RedditPost;
import masecla.reddit4j.objects.Sorting;
import masecla.reddit4j.requests.SubredditPostListingEndpointRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public interface BoolCallBack {
        void onResult(Boolean success);
    }
    public interface PostCallBack {
        void onResult(ArrayList<RedditPost> posts);
    }
    private Reddit4J client;
    private final Sorting sorting = Sorting.NEW;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private BoolCallBack callBack;
    public Client() {
    }

    public void setInfo(AgentInfo agent) {
        client = Reddit4J.rateLimited()
                .setClientId(agent.getAgentClientId()).setClientSecret(agent.getAgentClientSecret())
                .setUserAgent(new UserAgentBuilder().appname(agent.getAgentAppName()).author(agent.getAgentAuthorName()).version("1.0"));
    }
    public void getTopPosts(Subreddit subreddit, PostCallBack callBack){
        SubredditPostListingEndpointRequest request = client.getSubredditPosts(subreddit.getName(), sorting);
        List<RedditPost> posts;
        try {
            posts =  request.submit();
        } catch (Exception e) {
           callBack.onResult(new ArrayList<>());
           return;
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
        callBack.onResult(returnedPosts);
    }
    public Reddit4J getClient() {
        return client;
    }

    public void startConnection2() {
        try {
            client.userlessConnect();
        } catch (IOException | AuthenticationException | InterruptedException e) {
            System.out.println("Connection Error in Client");
        }
    }
}
