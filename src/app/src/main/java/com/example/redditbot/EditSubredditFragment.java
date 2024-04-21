package com.example.redditbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
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

        FirebaseDB firebaseDB = FirebaseDB.getInstance();
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        EditText addTerm = view.findViewById(R.id.edit_term);
        Button confirmButton = view.findViewById(R.id.done_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        Subreddit subreddit = (Subreddit) getArguments().get("subreddit");
        ArrayList<String> terms = subreddit.getTerms();

        seekBar.setProgress(subreddit.getMaxPosts());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeState(confirmButton, cancelButton);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        StringAdapter adapter = new StringAdapter(terms);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addTerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();

                if (!inputText.isEmpty() && terms.size() < 7) {
                    terms.add(inputText);
                    adapter.notifyItemInserted(terms.size() - 1);
                    addTerm.getText().clear();

                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subreddit.setMaxPosts(seekBar.getProgress());
                subreddit.setTerms(adapter.getStringList());
                firebaseDB.updateSubreddit(subreddit);
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

    public void changeState(Button confirm, Button cancel) {
        confirm.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
    }
}