package com.example.redditbot.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.redditbot.CurrentUser;
import com.example.redditbot.Containers.PostList;
import com.example.redditbot.DataHolders.Client;
import com.example.redditbot.Misc.SpaceItemDecoration;
import com.example.redditbot.R;
import com.example.redditbot.Adapters.SubredditAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import masecla.reddit4j.objects.RedditPost;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SubredditAdapter.onItemClickListener {
    private View view;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        CurrentUser user = CurrentUser.getInstance();

        RecyclerView subreddits = view.findViewById(R.id.subreddit_list);
        ImageButton addButton = view.findViewById(R.id.add_button);
        ImageButton lookupButton = view.findViewById(R.id.lookup_button);


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        subreddits.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        SubredditAdapter adapter = new SubredditAdapter(user.getSubreddits(), this);
        subreddits.setAdapter(adapter);
        subreddits.setLayoutManager(new LinearLayoutManager(requireContext()));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addSubredditFragment);
            }
        });
        lookupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostList list = new PostList();
                user.getClient().handleRequests(user.getSubreddits(), new Client.PostCallBack() {
                    @Override
                    public void onResult(ArrayList<RedditPost> posts) {

                    }

                    @Override
                    public void onResult(List<RedditPost> posts) {
                        list.addAll(posts);
                        Log.w("ErrorDetection", "Length of returned lists: " + list.size());
                        if (list.size() == 0) {
                            Toast.makeText(requireContext(), "No posts found", Toast.LENGTH_SHORT).show();
                        } else {
                            Bundle args = new Bundle();
                            args.putSerializable("RedditPosts", list);
                            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_postsFragment, args);
                        }
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onItemClick(Bundle args) {
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_editSubredditFragment, args);
    }
}