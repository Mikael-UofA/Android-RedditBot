package com.example.redditbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.MissingFormatArgumentException;

/**
 * create an instance of this fragment.
 */
public class EditSubredditFragment extends Fragment {

    public EditSubredditFragment() {
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
        View view = inflater.inflate(R.layout.fragment_edit_subreddit, container, false);

        if (getArguments() == null) {
            throw new MissingFormatArgumentException("Failed to pass a bundle");
        } else if (getArguments().get("subreddit") == null) {
            throw new MissingFormatArgumentException("Failed to pass subreddit in the bundle");
        }

        SeekBar seekBar = view.findViewById(R.id.seekBar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Button confirmButton = view.findViewById(R.id.done_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        Subreddit subreddit = (Subreddit) getArguments().get("subreddit");

        StringAdapter adapter = new StringAdapter(subreddit.getTerms());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        seekBar.setProgress(subreddit.getMaxPosts());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        return view;
    }
}