package com.example.migamecenter.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import com.example.migamecenter.R;
import com.example.migamecenter.bean.GameInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GameInfo> gameInfoList = new ArrayList<>();

    // 在适配器中设置数据
    @SuppressLint("NotifyDataSetChanged")
    public void setGameInfoList(List<GameInfo> gameInfoList) {
        this.gameInfoList = gameInfoList;
        notifyDataSetChanged();
    }

    public List<GameInfo> getCurrentGameInfoList() {
        return new ArrayList<>(gameInfoList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_game, parent, false);

        return new TypeOneViewHolder(itemView);

    }
    public interface OnItemClickListener {
        void onItemClick(GameInfo gameInfo);
    }
    private final OnItemClickListener listener;

    // 在构造函数中传入点击事件监听器
    public GameAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GameInfo gameInfo = gameInfoList.get(position);
        ((TypeOneViewHolder) holder).bindTypeOne(gameInfo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用接口方法处理点击事件
                listener.onItemClick(gameInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameInfoList.size();
    }

    public static class TypeOneViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder for type one
        // 定义类型一的视图持有者
        private final ImageView gameIconImageView;
        private final TextView gameNameTextView;
        private final TextView briefTextView;

        private final TextView tagsTextView;

        public TypeOneViewHolder(@NonNull View itemView) {
            super(itemView);
            gameIconImageView = itemView.findViewById(R.id.game_icon_image_view);
            gameNameTextView = itemView.findViewById(R.id.game_name_text_view);
            briefTextView = itemView.findViewById(R.id.brief_text_view);
            tagsTextView = itemView.findViewById(R.id.tags_text_view);
        }

        public void bindTypeOne(GameInfo gameInfo) {
            Glide.with(itemView.getContext()).load(gameInfo.icon)
                    .transform(new RoundedCorners(50))
                    .into(gameIconImageView);
            gameNameTextView.setText(gameInfo.gameName);
            briefTextView.setText(gameInfo.brief);
            tagsTextView.setText(gameInfo.tags);
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    public void addGameInfoList(List<GameInfo> gameInfoList) {
        this.gameInfoList.addAll(gameInfoList);
        notifyDataSetChanged();
    }
}
