package com.example.redditbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        RecyclerView subreddits = view.findViewById(R.id.subreddit_list);
        FloatingActionButton addButton = view.findViewById(R.id.add_button);

        CurrentUser user = CurrentUser.getInstance();
        SubredditAdapter adapter = new SubredditAdapter(user.getSubreddits(), this);
        subreddits.setAdapter(adapter);
        subreddits.setLayoutManager(new LinearLayoutManager(requireContext()));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addSubredditFragment);
            }
        });

        return view;
    }

    @Override
    public void onItemClick(Bundle args) {
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_editSubredditFragment, args);
    }
}