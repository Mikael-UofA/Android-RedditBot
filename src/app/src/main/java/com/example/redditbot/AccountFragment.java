package com.example.redditbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    public AccountFragment() {
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Button signOut = view.findViewById(R.id.sign_out_button);
        Button confirm = view.findViewById(R.id.btnConfirm);
        Button cancel = view.findViewById(R.id.btnCancel);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(signOut, confirm, cancel);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(signOut, confirm, cancel);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(signOut, confirm, cancel);
            }
        });
        return view;
    }

    public void changeState(Button signOut, Button confirm, Button cancel) {
        signOut.setVisibility(signOut.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirm.setVisibility(confirm.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        cancel.setVisibility(cancel.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}