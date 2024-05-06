package com.example.redditbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import masecla.reddit4j.objects.RedditPost;

public class PostAdapter extends ArrayAdapter<RedditPost> {

    private final ArrayList<RedditPost> posts;
    private final Context context;

    TextView subredditName;
    TextView userName;
    TextView postTime;
    TextView title;
    TextView selfText;

    MaterialButton karmaCount;
    MaterialButton commentsCount;

    public PostAdapter(Context context, ArrayList<RedditPost> posts) {
        super(context, 0, posts);
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.posts_list_content, parent,false);
        }

        RedditPost post = posts.get(position);

        subredditName = view.findViewById(R.id.post_subreddit);
        userName = view.findViewById(R.id.post_author);
        postTime = view.findViewById(R.id.post_time);
        title = view.findViewById(R.id.post_title);
        selfText = view.findViewById(R.id.post_self_text);

        karmaCount = view.findViewById(R.id.post_karma);
        commentsCount = view.findViewById(R.id.post_comments);

        String subreddit = "r/" + post.getSubreddit();
        String user = "u/" + post.getAuthor();

        subredditName.setText(subreddit);
        userName.setText(user);

        return view;

    }

    public void cutStrings(RedditPost post) {
        String title = post.getTitle();
        if (title.length() > 30) {
            title = title.substring(0, 30) + "...";
        }

        String selfText = post.getSelftext();
        if (selfText.length() > 35) {
            selfText = selfText.substring(0, 35) + "...";
        }

        this.title.setText(title);
        this.selfText.setText(selfText);

    }

    public void getNumbers(RedditPost post) {
        int comments = post.getNumComments();
        int karma = post.getScore();

        String commentText = comments + " Comments";
        if (comments >= 1000) {
            comments = comments / 1000;
            commentText = comments + "k Comments";

        }
        String karmaText = String.valueOf(karma);
        if (karma >= 1000) {
            karma = karma / 1000;
            karmaText = String.valueOf(karma);

        }

        commentsCount.setText(commentText);
        karmaCount.setText(karmaText);
    }

    public void getTime(RedditPost post) {
        long created = post.getCreated();

        Instant now = Instant.now();
        Instant createStamp = Instant.ofEpochSecond(created);
        Duration difference = Duration.between(createStamp, now);

        long minutes = difference.toMinutes();
        long hours = difference.toHours();
        long days = difference.toDays();

        String createdText;
        if (difference.getSeconds() < 60) {
            createdText = "now";
        } else if (minutes < 60) {
            createdText = minutes + "m";
        } else if (hours < 24) {
            createdText = hours + "h";
        } else {
            createdText = days + "d";
        }

        postTime.setText(createdText);
    }
}
