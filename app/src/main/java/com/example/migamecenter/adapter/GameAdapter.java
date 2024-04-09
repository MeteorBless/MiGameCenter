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

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private static final int TYPE_ONE = 0;
//    private static final int TYPE_TWO = 1;
//    private static final int TYPE_THREE = 2;

//    private Set<GameInfo> gameInfoSet = new HashSet<>(); // 使用 Set 来去重

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

//    @Override
//    public int getItemViewType(int position) {
//        // 根据位置或数据来确定每个项目的类型
////        GameInfo gameInfo = gameInfoList.get(position);
//        // 假设类型一是偶数位置，类型二是奇数位置，类型三是其他位置
//        if (position % 3 == 1) {
//            return TYPE_ONE;
//        } else if (position % 3 == 2) {
//            return TYPE_ONE;
//        } else {
//            return TYPE_THREE;
//        }
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_game, parent, false);

        return new TypeOneViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GameInfo gameInfo = gameInfoList.get(position);
        ((TypeOneViewHolder) holder).bindTypeOne(gameInfo);
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

        public TypeOneViewHolder(@NonNull View itemView) {
            super(itemView);
            gameIconImageView = itemView.findViewById(R.id.game_icon_image_view);
            gameNameTextView = itemView.findViewById(R.id.game_name_text_view);
            briefTextView = itemView.findViewById(R.id.brief_text_view);
        }

        public void bindTypeOne(GameInfo gameInfo) {
            Glide.with(itemView.getContext()).load(gameInfo.icon)
                    .transform(new RoundedCorners(50))
                    .into(gameIconImageView);
            gameNameTextView.setText(gameInfo.gameName);
            briefTextView.setText(gameInfo.brief);
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    public void addGameInfoList(List<GameInfo> gameInfoList) {
        this.gameInfoList.addAll(gameInfoList);
        notifyDataSetChanged();
    }
}
