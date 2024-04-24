package com.example.redditbot;

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

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    TextView viewAppName;
    TextView viewAgentId;
    TextView viewDevName;
    TextView viewReceiver;
    TextView viewAuthor;
    ImageButton deleteButton;
    ImageView avatar;
    UserAgent agent;
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
        viewDevName = view.findViewById(R.id.agent_username);
        viewReceiver = view.findViewById(R.id.agent_receiver);
        viewAuthor = view.findViewById(R.id.agent_author);
        deleteButton = view.findViewById(R.id.deleteButton);
        avatar = view.findViewById(R.id.avatar);

        agent = CurrentUser.getInstance().getAgent();
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

    public void setViews() {
        agent = CurrentUser.getInstance().getAgent();
        String appNameString;
        String agentIdString;
        String devNameString;
        String receiverString;
        String authorString;
        if (agent == null) {
            appNameString = "NO AGENT";
            agentIdString = "ID: N/A";
            devNameString = "Dev: N/A";
            receiverString = "Receiver: N/A";
            authorString = "Author: N/A";
            deleteButton.setBackground(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.blue_add_button, null));
            avatar.setImageResource(R.drawable.reddit_avatar);
        } else {
            appNameString = agent.getAgentAppName();
            agentIdString = "ID: " + agent.getAgentClientId();
            devNameString = "Dev: " + agent.getAgentUsername();
            receiverString = "Receiver: " + agent.getAgentReceiver();
            authorString = "Author: " + agent.getAgentAuthorName();
            deleteButton.setBackground(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.red_delete_button, null));
            avatar.setImageResource(R.drawable.happy_reddit_avatar);
        }
        viewAppName.setText(appNameString);
        viewAgentId.setText(agentIdString);
        viewDevName.setText(devNameString);
        viewReceiver.setText(receiverString);
        viewAuthor.setText(authorString);
    }
}