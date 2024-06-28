package com.example.redditbot.DataHolders;

import android.util.Log;

import com.example.redditbot.Containers.SubredditList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import masecla.reddit4j.client.Reddit4J;
import masecla.reddit4j.client.UserAgentBuilder;
import masecla.reddit4j.exceptions.AuthenticationException;
import masecla.reddit4j.objects.RedditPost;
import masecla.reddit4j.objects.Sorting;
import masecla.reddit4j.requests.SubredditPostListingEndpointRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

    public interface PostCallBack {
        void onResult(ArrayList<RedditPost> posts);
        void onResult(List<RedditPost> posts);
    }
    private Reddit4J client;
    private final Sorting sorting = Sorting.NEW;
    public Client() {
    }

    public void setInfo(AgentInfo agent) throws AuthenticationException, IOException, InterruptedException {
        client = Reddit4J.rateLimited()
                .setClientId(agent.getAgentClientId()).setClientSecret(agent.getAgentClientSecret())
                .setUserAgent(new UserAgentBuilder().appname(agent.getAgentAppName()).author(agent.getAgentAuthorName()).version("1.0"));
        client.userlessConnect();
    }
    public void getTopPosts(Subreddit subreddit, PostCallBack callBack) {
        SubredditPostListingEndpointRequest request = client.getSubredditPosts(subreddit.getName(), sorting);
        List<RedditPost> posts;
        try {
            posts = request.submit();
        } catch (Exception e) {
            callBack.onResult(new ArrayList<>());
            return;
        }
        int maxPosts = Math.min(subreddit.getMaxPosts(), posts.size());
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

    public void handleRequests(SubredditList list, PostCallBack callBack) {
        List<RedditPost> returningPosts = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger count = new AtomicInteger(list.getSubreddits().size());
        for (Subreddit subreddit : list.getSubreddits()) {
            getTopPosts(subreddit, new PostCallBack() {
                @Override
                public void onResult(ArrayList<RedditPost> posts) {
                }

                @Override
                public void onResult(List<RedditPost> posts) {
                    returningPosts.addAll(posts);
                    if (count.decrementAndGet() == 0) {
                        callBack.onResult(returningPosts);
                    }
                }
            });
        }
    }
    public Reddit4J getClient() {
        return client;
    }

    public void startConnection() {
        try {
            client.userlessConnect();
        } catch (IOException | AuthenticationException | InterruptedException e) {
            System.out.println("Connection Error in Client");
        }
    }
}
