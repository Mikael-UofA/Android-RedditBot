package com.example.redditbot.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditbot.R;

import java.util.ArrayList;

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.ViewHolder> {
    private final ArrayList<String> stringList;

    public StringAdapter(ArrayList<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_string, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String str = stringList.get(position);
        holder.textView.setText(str);

        // Set click listener on the end drawable
        holder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.green_trash_can, 0);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            final int i = holder.getAdapterPosition();
            @Override
            public void onClick(View v) {
                removeItem(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public void removeItem(int position) {
        stringList.remove(position);
        notifyItemRemoved(position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public ArrayList<String> getStringList() {
        return stringList;
    }
}
