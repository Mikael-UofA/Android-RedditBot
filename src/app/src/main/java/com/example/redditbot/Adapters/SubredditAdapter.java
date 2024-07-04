package com.example.redditbot.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditbot.Containers.SubredditList;
import com.example.redditbot.Misc.CurrentUser;
import com.example.redditbot.R;


public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.ViewHolder>{
    public interface onItemClickListener {
        void onItemClick(Bundle args);
    }
    private final CurrentUser user = CurrentUser.getInstance();
    private final SubredditList subList;
    private final onItemClickListener listener;

    public SubredditAdapter(SubredditList subList, onItemClickListener listener) {
        this.subList = subList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubredditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_string, parent, false);
        return new SubredditAdapter.ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String str = subList.getSubreddits().get(position).getName();
        holder.textView.setText(str);

        // Set click listener on the end drawable
        holder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_delete, 0);
        holder.textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Testing", String.valueOf(event.getAction()));
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Drawable drawable = holder.textView.getCompoundDrawablesRelative()[2];
                    if (drawable != null) {
                        Rect bounds = drawable.getBounds();

                        int drawableRightEdge = holder.textView.getRight() - holder.textView.getPaddingRight();
                        int drawableLeftEdge = drawableRightEdge - bounds.width();
                        int clickX = (int) event.getX();

                        if (clickX >= drawableLeftEdge && clickX <= drawableRightEdge) {
                            removeItem(holder.getAdapterPosition());
                            user.saveSubreddits(v.getContext());
                        } else {
                            listener.onItemClick(bundleToTransfer(holder.getAdapterPosition()));
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return subList.count();
    }

    public void removeItem(int position) {
        subList.remove(position);
        notifyItemRemoved(position);
    }
    public Bundle bundleToTransfer(int position) {
        Bundle args = new Bundle();
        args.putSerializable("subreddit", subList.getSubreddit(position));
        args.putSerializable("position", position);
        return args;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
