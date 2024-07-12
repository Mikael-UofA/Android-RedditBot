package com.example.redditbot.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.EditText;

import com.example.redditbot.DataHolders.AgentInfo;
import com.example.redditbot.Misc.CurrentUser;
import com.example.redditbot.R;

import java.util.Objects;

/**
 * A simple {@link DialogFragment} subclass.
 * This dialog fragment serves to add/save or delete the information of a bot for authentication.
 */
public class AddAgentFragment extends DialogFragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_add_agent, null);
        EditText editClientId = view.findViewById(R.id.edit_text_client_id);
        EditText editClientSecret = view.findViewById(R.id.edit_text_secret);
        EditText editAppName = view.findViewById(R.id.edit_text_app_name);
        EditText authorName = view.findViewById(R.id.edit_text_author_name);

        String tag = getTag();
        CurrentUser user = CurrentUser.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AgentInfo agent = new AgentInfo();

        if (Objects.equals(tag, "Add")) {
            return builder.setView(view)
                    .setTitle("Add an Agent")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", (dialog, which) -> {
                        agent.setAgentClientId(editClientId.getText().toString());
                        agent.setAgentClientSecret(editClientSecret.getText().toString());
                        agent.setAgentAppName(editAppName.getText().toString());
                        agent.setAgentAuthorName(authorName.getText().toString());
                        user.setAgent(agent);
                        user.setClientInfo(requireContext());
                        user.saveAgentInfo(requireContext());
                        Fragment targetFragment = getTargetFragment();
                        if (targetFragment instanceof SettingsFragment) {
                            ((SettingsFragment) targetFragment).setViews();
                        }
                    })
                    .create();
        } else {
            editClientId.setVisibility(View.GONE);
            editClientSecret.setVisibility(View.GONE);
            editAppName.setVisibility(View.GONE);
            authorName.setVisibility(View.GONE);
            if (Objects.equals(tag, "Delete")) {
                return builder.setView(view)
                        .setTitle("Delete Agent")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Confirm", (dialog, which) -> {
                            user.setAgent(null);
                            user.saveAgentInfo(requireContext());
                            Fragment targetFragment = getTargetFragment();
                            if (targetFragment instanceof SettingsFragment) {
                                ((SettingsFragment) targetFragment).setViews();
                            }
                        })
                        .create();
            } else {
                return builder.setView(view)
                        .setTitle("Error")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Ok", null)
                        .create();
            }
        }

    }
}