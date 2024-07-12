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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditbot.Misc.CurrentUser;
import com.example.redditbot.Misc.SpaceItemDecoration;
import com.example.redditbot.R;
import com.example.redditbot.Adapters.StringAdapter;
import com.example.redditbot.DataHolders.Subreddit;

import java.util.ArrayList;
import java.util.MissingFormatArgumentException;
import java.util.Objects;

import masecla.reddit4j.objects.Sorting;

/**
 * A simple {@link Fragment} subclass.
 * This fragment serves to edit the information of a subreddit already present in the subreddit list
 */
public class EditSubredditFragment extends Fragment {

    public EditSubredditFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_subreddit, container, false);

        if (getArguments() == null) {
            throw new MissingFormatArgumentException("Failed to pass a bundle");
        } else if (getArguments().get("subreddit") == null) {
            throw new MissingFormatArgumentException("Failed to pass subreddit in the bundle");
        }
        CurrentUser user = CurrentUser.getInstance();
        TextView title = view.findViewById(R.id.add_subreddit_textview);
        TextView maxValue = view.findViewById(R.id.max_posts);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        EditText addTerm = view.findViewById(R.id.edit_term);
        ImageButton confirmButton = view.findViewById(R.id.done_button);
        ImageButton cancelButton = view.findViewById(R.id.cancel_button);

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), R.layout.item_spinner, user.getSortingList());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        Subreddit subreddit = (Subreddit) getArguments().get("subreddit");
        assert subreddit != null;
        String sorting = subreddit.getSorting().toString();
        int position2 = user.getSortingList().indexOf(sorting.toLowerCase());
        spinner.setSelection(position2);

        Integer position = (Integer) getArguments().get("position");
        String titleText = "r/" + subreddit.getName();
        title.setText(titleText);
        ArrayList<String> terms = subreddit.getTerms();

        seekBar.setProgress(subreddit.getMaxPosts());
        String maxValueString = "Max. Posts: " + seekBar.getProgress();
        maxValue.setText(maxValueString);
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

        StringAdapter adapter = new StringAdapter(terms);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addTerm.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                if (terms.size() <= 10 && addTerm.getError() == null && addTerm.length() != 0) {
                    terms.add(Objects.requireNonNull(addTerm.getText()).toString().trim());
                    adapter.notifyItemInserted(terms.size() - 1);
                    addTerm.getText().clear();
                }
                return true;
            }
            return false;
        });
        addTerm.addTextChangedListener(new TextWatcher() {
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
                    addTerm.setError("Input must be at least 2 characters");
                } else if (input.length() > 21) {
                    addTerm.setError("Input must be no more than 21 characters");
                } else {
                    addTerm.setError(null);
                }
            }
        });

        confirmButton.setOnClickListener(v -> {
            subreddit.setMaxPosts(seekBar.getProgress());
            subreddit.setTerms(adapter.getStringList());
            subreddit.setSorting(getSorting(spinner.getSelectedItem().toString()));
            user.editSubreddit(subreddit, position);
            user.saveSubreddits(requireContext());
            Navigation.findNavController(view).popBackStack();
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

}