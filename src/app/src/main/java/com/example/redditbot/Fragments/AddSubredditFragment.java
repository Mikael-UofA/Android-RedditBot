package com.example.redditbot.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.redditbot.CurrentUser;
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
    TextView termsText;
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

        subredditName = view.findViewById(R.id.name_edit);
        subredditTerm = view.findViewById(R.id.terms_edit);
        termsText = view.findViewById(R.id.terms_displayed);

        ImageButton confirmButton = view.findViewById(R.id.complete_button);
        ImageButton cancelButton = view.findViewById(R.id.cancel_button);
        TextView maxValue = view.findViewById(R.id.max_posts);
        SeekBar seekBar = view.findViewById(R.id.seekBar);

        String maxValueString = "Max. Posts: " + seekBar.getProgress();
        maxValue.setText(maxValueString);

        CurrentUser user = CurrentUser.getInstance();

        subredditTerm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (terms.size() < 10) {
                        terms.add(Objects.requireNonNull(subredditTerm.getText()).toString().trim());
                        termsText.setText(displayTerm());
                        subredditTerm.setText("");
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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validCondition()) {
                    Subreddit subreddit = new Subreddit(Objects.requireNonNull(subredditName.getText()).toString().trim(), seekBar.getProgress(), terms);
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
        if (subredditName == null || terms.isEmpty() || Objects.requireNonNull(subredditName.getText()).toString().length() < 3) {
            return false;
        }
        return terms.size() <= 7;
    }

    public String displayTerm() {
        StringBuilder text = new StringBuilder("Terms: ");

        if (!terms.isEmpty()) {
            for (String term : terms) {
                text.append(term).append(", ");
            }
            text = new StringBuilder(text.substring(0, text.length() - 2));
        }

        return text.toString();
    }
}