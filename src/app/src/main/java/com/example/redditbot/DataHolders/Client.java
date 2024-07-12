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
    public Client() {
    }

    /**
     * Initializes the Reddit4J client with the provided agent information.
     * This method configures the Reddit4J client using the provided AgentInfo object,
     * which contains the client ID, client secret, app name, and author name. The client
     * is then authenticated using a userless connection.
     *
     * @param agent The AgentInfo object containing the necessary authentication details.
     * @throws AuthenticationException If there is an error during authentication.
     * @throws IOException If there is an input/output error during the connection setup.
     * @throws InterruptedException If the connection setup is interrupted.
     */
    public void setInfo(AgentInfo agent) throws AuthenticationException, IOException, InterruptedException {
        client = Reddit4J.rateLimited()
                .setClientId(agent.getAgentClientId()).setClientSecret(agent.getAgentClientSecret())
                .setUserAgent(new UserAgentBuilder().appname(agent.getAgentAppName()).author(agent.getAgentAuthorName()).version("1.0"));
        client.userlessConnect();
    }

    /**
     * This method sends a request to Reddit to get the newest posts from the specified
     * subreddit. It filters the posts based on the terms defined in the Subreddit
     * object. The filtered posts are then returned through the provided callback.
     *
     * @param subreddit The Subreddit object containing the subreddit name, max posts, and terms for filtering.
     * @param callBack The callback to handle the results.
     */
    public void getTopPosts(Subreddit subreddit, PostCallBack callBack) {
        Thread thread = new Thread(() -> {
            SubredditPostListingEndpointRequest request = client.getSubredditPosts(subreddit.getName(), subreddit.getSorting());
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

    /**
     * This method iterates through a list of subreddits, retrieves the posts for each,
     * and updates a progress bar to indicate the progress. The results from all subreddits
     * are returned through the provided callback.
     *
     * @param list The SubredditList containing the subreddits to request posts from.
     * @param progressBar The ProgressBar to update during the process.
     * @param callBack The callback to handle the results.
     */
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
