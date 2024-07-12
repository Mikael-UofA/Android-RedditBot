package com.example.redditbot.Fragments;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.redditbot.DataHolders.AgentInfo;
import com.example.redditbot.Misc.CurrentUser;
import com.example.redditbot.R;

/**
 * A simple {@link Fragment} subclass.
 * This fragment serves to add or remove the agent information for authentication
 */
public class SettingsFragment extends Fragment{

    CurrentUser user;
    TextView viewAppName;
    TextView viewAgentId;
    TextView viewAuthor;
    ImageButton deleteButton;
    ImageView avatar;
    AgentInfo agent;

    public SettingsFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        viewAppName = view.findViewById(R.id.agent_app_name);
        viewAgentId = view.findViewById(R.id.agent_clientId);
        viewAuthor = view.findViewById(R.id.agent_author);
        deleteButton = view.findViewById(R.id.deleteButton);
        avatar = view.findViewById(R.id.avatar);

        user = CurrentUser.getInstance();
        agent = user.getAgent();
        setViews();

        deleteButton.setOnClickListener(v -> {
            AddAgentFragment fragment = new AddAgentFragment();
            if (agent == null) {
                fragment.setTargetFragment(SettingsFragment.this, 123);
                fragment.show(requireActivity().getSupportFragmentManager(), "Add");
            } else {
                fragment.setTargetFragment(SettingsFragment.this, 123);
                fragment.show(requireActivity().getSupportFragmentManager(), "Delete");
            }
            setViews();
        });
        return view;
    }


    /**
     * Use this method to set the views for this fragment
     */
    public void setViews() {
        agent = CurrentUser.getInstance().getAgent();
        String appNameString;
        String agentIdString;
        String authorString;
        if (agent == null) {
            appNameString = "NO AGENT";
            agentIdString = "ID: N/A";
            authorString = "Author: N/A";
            deleteButton.setBackground(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.blue_add_button, null));
            avatar.setImageResource(R.drawable.sad_reddit_avatar);
        } else {
            appNameString = agent.getAgentAppName();
            agentIdString = "ID: " + agent.getAgentClientId();
            authorString = "Author: " + agent.getAgentAuthorName();
            deleteButton.setBackground(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.red_delete_button, null));
            avatar.setImageResource(R.drawable.happy_reddit_avatar);
        }
        viewAppName.setText(appNameString);
        viewAgentId.setText(agentIdString);
        viewAuthor.setText(authorString);

    }
}