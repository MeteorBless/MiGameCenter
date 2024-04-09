package com.example.migamecenter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.migamecenter.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<String> searchHistoryList;
    private OnHistoryItemClickListener listener;

    public HistoryAdapter(List<String> searchHistoryList) {
        this.searchHistoryList = searchHistoryList;
    }

    public void setOnHistoryItemClickListener(OnHistoryItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        String keyword = searchHistoryList.get(position);
        holder.keywordTextView.setText(keyword);

        // If a listener is set, enable the remove icon and attach a click listener
        if (listener != null) {
            holder.removeIcon.setVisibility(View.VISIBLE);
            holder.removeIcon.setOnClickListener(v -> {
                searchHistoryList.remove(position);
                notifyItemRemoved(position);
                listener.onHistoryItemRemove(position, keyword);
            });
        } else {
            holder.removeIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return searchHistoryList.size();
    }

    public interface OnHistoryItemClickListener {
        void onHistoryItemRemove(int position, String keyword);
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView keywordTextView;
        ImageView removeIcon;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            keywordTextView = itemView.findViewById(R.id.history_text_view);
        }
    }
}
