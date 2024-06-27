package com.example.redditbot.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditbot.DataHolders.AgentInfo;
import com.example.redditbot.DataHolders.Client;
import com.example.redditbot.CurrentUser;
import com.example.redditbot.R;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements Client.BoolCallBack {

    CurrentUser user;
    TextView viewAppName;
    TextView viewAgentId;
    TextView viewDevName;
    TextView viewReceiver;
    TextView viewAuthor;
    ImageButton deleteButton;
    MaterialButton authButton;
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
        authButton = view.findViewById(R.id.button);

        user = CurrentUser.getInstance();
        agent = user.getAgent();
        setViews();
        setButtonOff();

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
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authButton.isClickable()) {
                    //user.authClient(SettingsFragment.this);
                    user.authClient2();
                }

            }
        });
        return view;
    }

    public void setButtonOff() {
        if (user.getConnected()) {
            authButton.setClickable(false);
            authButton.setBackgroundColor(Color.DKGRAY);
        } else {
            authButton.setBackgroundColor(Color.BLUE);
        }
    }
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
            avatar.setImageResource(R.drawable.reddit_avatar);
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

    @Override
    public void onResult(Boolean success) {
        if (success) {
            user.setConnected(true);
            Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
            setButtonOff();
        } else {
            Toast.makeText(getContext(), "Authentication Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}