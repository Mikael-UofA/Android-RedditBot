package com.example.redditbot.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.redditbot.Adapters.StringAdapter;
import com.example.redditbot.Misc.CurrentUser;
import com.example.redditbot.Misc.SpaceItemDecoration;
import com.example.redditbot.R;
import com.example.redditbot.DataHolders.Subreddit;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import masecla.reddit4j.objects.Sorting;

/**
 * A simple {@link Fragment} subclass.
 * This fragment serves to add a new subreddit to the subreddit list
 */
public class AddSubredditFragment extends Fragment {
    TextInputEditText subredditName;
    TextInputEditText subredditTerm;
    RecyclerView recyclerView;
    ImageButton confirmButton;
    ImageButton cancelButton;
    TextView maxValue;
    SeekBar seekBar;
    final ArrayList<String> terms = new ArrayList<>();
    public AddSubredditFragment() {
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

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), R.layout.item_spinner, user.getSortingList());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        StringAdapter adapter = new StringAdapter(terms);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        String maxValueString = "Max. Posts: " + seekBar.getProgress();
        maxValue.setText(maxValueString);

        subredditTerm.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                if (terms.size() <= 10 && subredditTerm.getError() == null && subredditTerm.length() != 0) {
                    terms.add(Objects.requireNonNull(subredditTerm.getText()).toString().trim());
                    adapter.notifyItemInserted(terms.size() - 1);
                    subredditTerm.getText().clear();
                }
                return true;
            }
            return false;
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
        confirmButton.setOnClickListener(v -> {
            if (validCondition()) {
                String thing = (String) spinner.getSelectedItem();
                Sorting sorting = getSorting(thing);
                Subreddit subreddit = new Subreddit(Objects.requireNonNull(subredditName.getText()).toString().trim(), seekBar.getProgress(), adapter.getStringList(), sorting);
                user.addSubreddit(subreddit);
                user.saveSubreddits(requireContext());
                Navigation.findNavController(view).popBackStack();
            }
        });
        cancelButton.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        return view;
    }

    @NonNull
    private static Sorting getSorting(String thing) {
        Sorting sorting;
        if (Objects.equals(thing, "hot")) {
            sorting = Sorting.HOT;
        } else if (Objects.equals(thing, "new")) {
            sorting = Sorting.NEW;
        }
        else if (Objects.equals(thing, "controversial")) {
            sorting = Sorting.CONTROVERSIAL;
        }
        else if (Objects.equals(thing, "rising")) {
            sorting = Sorting.RISING;
        }
        else {
            sorting = Sorting.TOP;
        }
        return sorting;
    }

    /**
     * Use this method to check whether or not the user entered the correct subreddit information
     * @return Boolean
     * */
    public Boolean validCondition() {
        if (subredditName == null || terms.isEmpty() || subredditName.getError() != null) {
            return false;
        }
        return terms.size() <= 10;
    }

}