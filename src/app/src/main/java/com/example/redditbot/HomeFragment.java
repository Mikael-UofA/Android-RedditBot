package com.example.redditbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private final FirebaseDB firebaseDB = FirebaseDB.getInstance();
    private CurrentUser user;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ListView subreddits = view.findViewById(R.id.subreddit_list);
        FloatingActionButton addButton = view.findViewById(R.id.add_button);

        user = CurrentUser.getInstance();
        SubredditList subredditList = new SubredditList();
        SubredditAdapter adapter = new SubredditAdapter(getContext(), subredditList);
        subreddits.setAdapter(adapter);

        firebaseDB.getSubreddits(new FirebaseDB.GetSubredditsCallBack() {
            @Override
            public void onResult(SubredditList list) {
                if (list.count() != 0) {
                    for (Subreddit subreddit : list.getSubreddits()) {
                        subredditList.add(subreddit);
                    }
                    adapter.notifyDataSetChanged();
                    user.setSubreddits(subredditList);

                }
            }
        });
        subreddits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Subreddit subreddit = (Subreddit) parent.getItemAtPosition(position);
                Bundle args = new Bundle();
                args.putSerializable("subreddit", subreddit);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_editSubredditFragment, args);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addSubredditFragment);
            }
        });

        return view;
    }
}