package com.example.redditbot.DataHolders;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.redditbot.Containers.SubredditList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import masecla.reddit4j.client.Reddit4J;
import masecla.reddit4j.client.UserAgentBuilder;
import masecla.reddit4j.exceptions.AuthenticationException;
import masecla.reddit4j.objects.RedditPost;
import masecla.reddit4j.objects.Sorting;
import masecla.reddit4j.requests.SubredditPostListingEndpointRequest;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is a wrapper for the Reddit4J class.
 */
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
        Thread thread = new Thread(() -> {
            SubredditPostListingEndpointRequest request = client.getSubredditPosts(subreddit.getName(), sorting);
            List<RedditPost> posts;
            try {
                posts = request.submit();
            } catch (Exception e) {
                Log.e("ErrorDetection", "Subreddit request unsuccessful: " + e);
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(new ArrayList<>()));
                return;
            }
            Log.d("ErrorDetection", "Subreddit request successful");
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
            Log.d("ErrorDetection", "Subreddit posts filtering complete");
            new Handler(Looper.getMainLooper()).post(() -> callBack.onResult((List<RedditPost>) returnedPosts));
        });
        thread.start();

    }

    public void handleRequests(SubredditList list, ProgressBar progressBar, PostCallBack callBack) {
        List<RedditPost> returningPosts = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger count = new AtomicInteger(list.getSubreddits().size());
        Handler handler = new Handler(Looper.getMainLooper());
        Iterator<Subreddit> iterator = list.getSubreddits().iterator();

        progressBar.setMax(list.getSubreddits().size());
        progressBar.setProgress(0);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (iterator.hasNext()) {
                    Subreddit subreddit = iterator.next();
                    getTopPosts(subreddit, new PostCallBack() {
                        @Override
                        public void onResult(ArrayList<RedditPost> posts) {
                            Log.d("ErrorDetection", "Error: getTopPosts for ArrayList");
                        }

                        @Override
                        public void onResult(List<RedditPost> posts) {
                            returningPosts.addAll(posts);
                            if (count.decrementAndGet() == 0) {
                                Log.d("ErrorDetection", "handleRequests: Finished going through each subreddit");
                                progressBar.setProgress(progressBar.getProgress() + 1);
                                callBack.onResult(returningPosts);
                            }
                            handler.post(() -> progressBar.setProgress(progressBar.getProgress() + 1));
                        }
                    });

                    handler.postDelayed(this, 2000);
                }
            }
        };

        // Start the first iteration
        handler.post(task);
    }
}
