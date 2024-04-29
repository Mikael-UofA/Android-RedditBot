package com.example.redditbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddSubredditFragment extends Fragment {
    TextInputEditText subredditName;
    TextInputEditText subredditMaxPosts;
    TextInputEditText subredditTerm;
    ImageButton addTerm;
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

        FirebaseDB firebaseDB = FirebaseDB.getInstance();
        subredditName = view.findViewById(R.id.name_edit);
        subredditMaxPosts = view.findViewById(R.id.max_posts_edit);
        subredditTerm = view.findViewById(R.id.terms_edit);
        addTerm = view.findViewById(R.id.add_term);
        termsText = view.findViewById(R.id.terms_displayed);

        Button confirmButton = view.findViewById(R.id.complete_button);

        addTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (terms.size() < 7 && validTerm()) {
                    terms.add(subredditTerm.getText().toString().trim());
                    termsText.setText(displayTerm());
                    subredditTerm.setText("");
                }
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validCondition()) {
                    String name = subredditName.getText().toString();
                    int maxPosts = Integer.parseInt(subredditMaxPosts.getText().toString());
                    Subreddit newSubreddit = new Subreddit(CurrentUser.getInstance().getUsername(),
                            name, maxPosts, terms);
                    firebaseDB.userSubredditAlreadyExists(name, new FirebaseDB.GetBooleanCallBack() {
                        @Override
                        public void onResult(Boolean bool) {
                            if (!bool) {
                                firebaseDB.addSubreddit(newSubreddit);
                                Navigation.findNavController(view).popBackStack();
                            } else {
                                // Didn't work
                            }
                        }
                    });
                }
            }
        });


        return view;
    }

    public Boolean validTerm() {
        return !terms.contains(subredditTerm.getText().toString().trim());
    }
    public Boolean validCondition() {
        if (subredditName == null || subredditMaxPosts == null || terms.isEmpty() || subredditName.getText().toString().length() < 3) {
            return false;
        }
        int maxPosts = Integer.parseInt(subredditMaxPosts.getText().toString());
        if (maxPosts > 20 || maxPosts < 1) {
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