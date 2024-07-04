package com.example.redditbot.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.redditbot.Adapters.PostAdapter;
import com.example.redditbot.Containers.PostList;
import com.example.redditbot.R;

import masecla.reddit4j.objects.RedditPost;

/**
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment implements PostAdapter.onItemClickListener {

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        assert getArguments() != null;
        PostList posts = (PostList) getArguments().get("RedditPosts");
        RecyclerView recyclerView = view.findViewById(R.id.posts_listview);
        ImageButton backButton = view.findViewById(R.id.back_button);

        PostAdapter adapter = new PostAdapter(posts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        backButton.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        return view;
    }

    @Override
    public void onItemClick(RedditPost post) {
        Uri uri = Uri.parse(post.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}