package com.example.redditbot.Fragments;

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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.redditbot.Adapters.StringAdapter;
import com.example.redditbot.CurrentUser;
import com.example.redditbot.Misc.SpaceItemDecoration;
import com.example.redditbot.R;
import com.example.redditbot.DataHolders.Subreddit;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddSubredditFragment extends Fragment {
    TextInputEditText subredditName;
    TextInputEditText subredditTerm;
    RecyclerView recyclerView;
    ImageButton confirmButton;
    ImageButton cancelButton;
    TextView maxValue;
    SeekBar seekBar;
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

        CurrentUser user = CurrentUser.getInstance();

        subredditName = view.findViewById(R.id.name_edit);
        subredditTerm = view.findViewById(R.id.terms_edit);
        recyclerView = view.findViewById(R.id.recyclerView);
        confirmButton = view.findViewById(R.id.complete_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        maxValue = view.findViewById(R.id.max_posts);
        seekBar = view.findViewById(R.id.seekBar);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        StringAdapter adapter = new StringAdapter(terms);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        String maxValueString = "Max. Posts: " + seekBar.getProgress();
        maxValue.setText(maxValueString);

        subredditTerm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (terms.size() < 10 && subredditTerm.getError() == null && subredditTerm.length() != 0) {
                        terms.add(Objects.requireNonNull(subredditTerm.getText()).toString().trim());
                        adapter.notifyItemInserted(terms.size() - 1);
                        subredditTerm.getText().clear();
                    }
                    return true;
                }
                return false;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String maxValueString = "Max. Posts: " + seekBar.getProgress();
                maxValue.setText(maxValueString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        subredditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.contains("r/")) {
                    subredditName.setError("Do not include r/");
                } else if (!input.matches("[a-zA-Z0-9_]*")) {
                    subredditName.setError("Input must only contain letters, numbers, and/or underscores");
                } else if (input.length() < 3) {
                    subredditName.setError("Input must be at least 3 characters");
                } else if (input.length() > 21) {
                    subredditName.setError("Input must be no more than 21 characters");
                } else {
                    subredditTerm.setError(null);
                }
            }
        });
        subredditTerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.length() == 1) {
                    subredditTerm.setError("Input must be at least 2 characters");
                } else if (input.length() > 21) {
                    subredditTerm.setError("Input must be no more than 21 characters");
                } else {
                    subredditTerm.setError(null);
                }
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validCondition()) {
                    Subreddit subreddit = new Subreddit(Objects.requireNonNull(subredditName.getText()).toString().trim(), seekBar.getProgress(), adapter.getStringList());
                    user.addSubreddit(subreddit);
                    user.saveSubreddits(requireContext());
                    Navigation.findNavController(view).popBackStack();
                }
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
    public Boolean validCondition() {
        if (subredditName == null || terms.isEmpty() || subredditName.getError() != null) {
            return false;
        }
        return terms.size() <= 10;
    }

}