package com.example.redditbot.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditbot.Containers.PostList;
import com.example.redditbot.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import masecla.reddit4j.objects.RedditPost;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    public interface onItemClickListener {
        void onItemClick(RedditPost post);
    }
    private final PostList postList;
    private final onItemClickListener listener;

    public PostAdapter(PostList postList, onItemClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_list_content, parent, false);
        return new PostAdapter.ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        RedditPost post = postList.getRedditPost(holder.getAdapterPosition());
        ArrayList<String> list = getStrings(post);
        ArrayList<String> list2 = getNumbers(post);

        holder.subredditName.setText(list.get(0));
        holder.userName.setText(list.get(1));
        holder.title.setText(list.get(2));
        holder.selfText.setText(list.get(3));

        holder.commentsCount.setText(list2.get(0));
        holder.karmaCount.setText(list2.get(1));
        holder.postTime.setText(getTime(post));

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(postList.getRedditPost(holder.getAdapterPosition()));
            }
        });
        
    }
    
    @Override
    public int getItemCount() {
        return postList.count();
    }
    public ArrayList<String> getStrings(RedditPost post) {
        String subreddit = "r/" + post.getSubreddit();
        String user = "u/" + post.getAuthor();
        String title = post.getTitle();
        if (title.length() > 30) {
            title = title.substring(0, 30) + "...";
        }
        String selfText = post.getSelftext();
        if (selfText.length() > 35) {
            selfText = selfText.substring(0, 35) + "...";
        }

        ArrayList<String> list = new ArrayList<>();
        list.add(subreddit);
        list.add(user);
        list.add(title);
        list.add(selfText);

        return list;

    }
    public ArrayList<String> getNumbers(RedditPost post) {
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
            karmaText = String.valueOf(karma) + "k";

        }

        ArrayList<String> list = new ArrayList<>();
        list.add(commentText);
        list.add(karmaText);

        return list;
    }
    public String getTime(RedditPost post) {
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

        return createdText;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView subredditName;
        TextView userName;
        TextView postTime;
        TextView title;
        TextView selfText;

        MaterialButton karmaCount;
        MaterialButton commentsCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.mat_card);
            subredditName = itemView.findViewById(R.id.post_subreddit);
            userName = itemView.findViewById(R.id.post_author);
            postTime = itemView.findViewById(R.id.post_time);
            title = itemView.findViewById(R.id.post_title);
            selfText = itemView.findViewById(R.id.post_self_text);

            karmaCount = itemView.findViewById(R.id.post_karma);
            commentsCount = itemView.findViewById(R.id.post_comments);
        }
    }
}

