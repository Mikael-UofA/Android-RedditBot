package com.example.redditbot;

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

import java.util.Objects;

/**
 * create an instance of this fragment.
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
        EditText editAgentName = view.findViewById(R.id.edit_text_username);
        EditText editAgentPass = view.findViewById(R.id.edit_text_password);
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
                        agent.setAgentUsername(editAgentName.getText().toString());
                        agent.setAgentPass(editAgentPass.getText().toString());
                        agent.setAgentClientId(editClientId.getText().toString());
                        agent.setAgentClientSecret(editClientSecret.getText().toString());
                        agent.setAgentAppName(editAppName.getText().toString());
                        agent.setAgentAuthorName(authorName.getText().toString());
                        user.setAgent(agent);
                        user.saveAgentInfo(requireContext());
                        Fragment targetFragment = getTargetFragment();
                        if (targetFragment instanceof SettingsFragment) {
                            ((SettingsFragment) targetFragment).setViews();
                        }
                    })
                    .create();
        } else {
            editAgentName.setVisibility(View.GONE);
            editAgentPass.setVisibility(View.GONE);
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