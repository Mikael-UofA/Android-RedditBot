package com.example.redditbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddSubredditFragment extends Fragment {
    EditText subredditName;
    EditText subredditMaxPosts;
    EditText subredditTerm;

    ArrayList<String> terms = new ArrayList<>();
    public AddSubredditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_subreddit, container, false);

        FirebaseDB firebaseDB = FirebaseDB.getInstance();
        subredditName = view.findViewById(R.id.name_edit);
        subredditMaxPosts = view.findViewById(R.id.max_posts_edit);
        subredditTerm = view.findViewById(R.id.terms_edit);

        Button confirmButton = view.findViewById(R.id.complete_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validCondition()) {
                    Subreddit newSubreddit = new Subreddit(CurrentUser.getInstance().getUsername(), subredditName.getText().toString(),
                            Integer.getInteger(subredditMaxPosts.getText().toString()), terms);
                    firebaseDB.addSubreddit(newSubreddit);
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });


        return view;
    }

    public Boolean validCondition() {
        SubredditList subreddits = CurrentUser.getInstance().getSubreddits();
        if (subredditName == null || subredditMaxPosts == null || terms.isEmpty()) {
            return false;
        }
        for (Subreddit subreddit : subreddits.getSubreddits()) {
            if (Objects.equals(subreddit.getName(), subredditName.getText().toString())) {
                return false;
            }
        }
        Integer maxPosts = Integer.getInteger(subredditMaxPosts.getText().toString().trim());
        if (maxPosts == null || maxPosts > 20 || maxPosts < 1) {
            return false;
        }
        return terms.size() <= 7;
    }
}