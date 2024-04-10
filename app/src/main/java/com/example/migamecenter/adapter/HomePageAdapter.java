package com.example.migamecenter.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.migamecenter.DetailActivity;
import com.example.migamecenter.R;
import com.example.migamecenter.bean.GameInfo;
import com.example.migamecenter.bean.HomePageInfo;

import java.util.ArrayList;
import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<HomePageInfo> homePageInfoList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setHomePageInfoList(List<HomePageInfo> homePageInfoList) {
        this.homePageInfoList = homePageInfoList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addHomePageInfoList(List<HomePageInfo> homePageInfoList) {
        this.homePageInfoList.addAll(homePageInfoList);
        notifyDataSetChanged();
    }

    public List<HomePageInfo> getCurrentHomePageList()
    {return new ArrayList<>(homePageInfoList);}

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if (viewType == 1) {
            // 样式 1：1*3
            itemView = inflater.inflate(R.layout.item_game_style_1, parent, false);
            return new StyleOneViewHolder(itemView);
        } else if (viewType == 2) {
            // 样式 2：1*4
            itemView = inflater.inflate(R.layout.item_game_style_2, parent, false);
            return new StyleTwoViewHolder(itemView);
        } else {
            // 样式 3：单行
            itemView = inflater.inflate(R.layout.item_game_style_3, parent, false);
            return new StyleThreeViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomePageInfo homePageInfo = homePageInfoList.get(position);
        int viewType = getItemViewType(position);
        if (viewType == 1) {
            ((StyleOneViewHolder) holder).bindStyleOne(homePageInfo);

        } else if (viewType == 2) {
            ((StyleTwoViewHolder) holder).bindStyleTwo(homePageInfo);
        } else {
            ((StyleThreeViewHolder) holder).bindStyleThree(homePageInfo);
        }
    }

    @Override
    public int getItemCount() {
        return homePageInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // 根据样式字段决定布局类型
        return homePageInfoList.get(position).style;
    }

    public static class StyleOneViewHolder extends RecyclerView.ViewHolder {
        private final List<ImageView> gameIconImageViewList;
        private final List<TextView> gameNameTextViewList;
        private final List<TextView> gamePlayNumTextViewList;

        public StyleOneViewHolder(@NonNull View itemView) {
            super(itemView);
            gameIconImageViewList = new ArrayList<>();
            gameNameTextViewList = new ArrayList<>();
            gamePlayNumTextViewList = new ArrayList<>();

            for (int i = 0; i < 6; i++) {
                ImageView gameIconImageView = itemView.findViewById(
                        itemView.getResources().getIdentifier("game_icon_image_view_" + i,
                                "id", itemView.getContext().getPackageName()));
                TextView gameNameTextView = itemView.findViewById(
                        itemView.getResources().getIdentifier("game_name_text_view_" + i,
                                "id", itemView.getContext().getPackageName()));
                TextView gamePlayNumTextView = itemView.findViewById(
                        itemView.getResources().getIdentifier("play_num_text_view_" + i,
                                "id", itemView.getContext().getPackageName()));

                final int finalI = i;
                gameIconImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 处理游戏图标点击事件的逻辑
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            GameInfo gameInfo = homePageInfoList.get(position).gameInfoList.get(finalI);
                            Intent intent = new Intent(v.getContext(), DetailActivity.class);
                            intent.putExtra("gameInfo", gameInfo);
                            v.getContext().startActivity(intent);
                        }
                    }
                });
                gameIconImageViewList.add(gameIconImageView);
                gameNameTextViewList.add(gameNameTextView);
                gamePlayNumTextViewList.add(gamePlayNumTextView);
            }
        }

        @SuppressLint("SetTextI18n")
        public void bindStyleOne(HomePageInfo homePageInfo) {
            if (homePageInfo.gameInfoList != null && !homePageInfo.gameInfoList.isEmpty()) {
                int size = 6;
                for (int i = 0; i < size; i++) {
                    GameInfo gameInfo = homePageInfo.gameInfoList.get(i);
                    Glide.with(itemView.getContext()).load(gameInfo.icon)
                            .transform(new RoundedCorners(50))
                            .into(gameIconImageViewList.get(i));
                    String gameName = gameInfo.gameName;
                    if (gameName.length() > 5) {
                        gameName = gameName.substring(0, 4) + "...";
                    }
                    gameNameTextViewList.get(i).setText(gameName);
                    if(gameInfo.playNumFormat!=null){
                        gamePlayNumTextViewList.get(i).setText(gameInfo.playNumFormat+"人正在玩");
                    }
                }
            } else {
                Log.i("HomePageAdapter", "gameInfoList为空");
            }
        }

    }

    public static class StyleTwoViewHolder extends RecyclerView.ViewHolder {
        private final List<ImageView> gameIconImageViewList;
        private final List<TextView> gameNameTextViewList;
        private final List<TextView> gamePlayNumTextViewList;

        public StyleTwoViewHolder(@NonNull View itemView) {
            super(itemView);
            gameIconImageViewList = new ArrayList<>();
            gameNameTextViewList = new ArrayList<>();
            gamePlayNumTextViewList = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                ImageView gameIconImageView = itemView.findViewById(
                        itemView.getResources().getIdentifier("game_icon_image_view_" + i,
                                "id", itemView.getContext().getPackageName()));
                TextView gameNameTextView = itemView.findViewById(
                        itemView.getResources().getIdentifier("game_name_text_view_" + i,
                                "id", itemView.getContext().getPackageName()));
                TextView gamePlayNumTextView = itemView.findViewById(
                        itemView.getResources().getIdentifier("play_num_text_view_" + i,
                                "id", itemView.getContext().getPackageName()));

                final int finalI = i;
                gameIconImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 处理游戏图标点击事件的逻辑
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            GameInfo gameInfo = homePageInfoList.get(position).gameInfoList.get(finalI);
                            Intent intent = new Intent(v.getContext(), DetailActivity.class);
                            intent.putExtra("gameInfo", gameInfo);
                            v.getContext().startActivity(intent);
                        }
                    }
                });

                gameIconImageViewList.add(gameIconImageView);
                gameNameTextViewList.add(gameNameTextView);
                gamePlayNumTextViewList.add(gamePlayNumTextView);
            }
        }

        @SuppressLint("SetTextI18n")
        public void bindStyleTwo(HomePageInfo homePageInfo) {
            if (homePageInfo.gameInfoList != null && !homePageInfo.gameInfoList.isEmpty()) {
                int size = 8;
                for (int i = 0; i < size; i++) {
                    GameInfo gameInfo = homePageInfo.gameInfoList.get(i);

                    Glide.with(itemView.getContext()).load(gameInfo.icon)
                            .transform(new RoundedCorners(50))
                            .into(gameIconImageViewList.get(i));
                    String gameName = gameInfo.gameName;
                    if (gameName.length() > 5) {
                        gameName = gameName.substring(0, 4) + "...";
                    }
                    gameNameTextViewList.get(i).setText(gameName);
                    if(gameInfo.playNumFormat!=null){
                        gamePlayNumTextViewList.get(i).setText(gameInfo.tags);
                    }

                }
            } else {
                Log.i("HomePageAdapter", "gameInfoList为空");
            }
        }
    }

    public static class StyleThreeViewHolder extends RecyclerView.ViewHolder {
        private final List<ImageView> gameIconImageViewList;
        private final List<TextView> gameNameTextViewList;
        private final List<TextView> briefTextViewList;

        private final List<TextView> tagsTextViewList;

        public StyleThreeViewHolder(@NonNull View itemView) {
            super(itemView);
            gameIconImageViewList = new ArrayList<>();
            gameNameTextViewList = new ArrayList<>();
            briefTextViewList = new ArrayList<>();
            tagsTextViewList = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                ImageView gameIconImageView = itemView.findViewById(
                        itemView.getResources().getIdentifier("game_icon_image_view_" + i,
                                "id", itemView.getContext().getPackageName()));
                TextView gameNameTextView = itemView.findViewById(
                        itemView.getResources().getIdentifier("game_name_text_view_" + i,
                                "id", itemView.getContext().getPackageName()));
                TextView briefTextView = itemView.findViewById(
                        itemView.getResources().getIdentifier("brief_text_view_" + i,
                                "id", itemView.getContext().getPackageName()));
                TextView tagsTextView = itemView.findViewById(
                        itemView.getResources().getIdentifier("tags_text_view_" + i,
                                "id", itemView.getContext().getPackageName()));

                final int finalI = i;
                gameIconImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 处理游戏图标点击事件的逻辑
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            GameInfo gameInfo = homePageInfoList.get(position).gameInfoList.get(finalI);
                            Intent intent = new Intent(v.getContext(), DetailActivity.class);
                            intent.putExtra("gameInfo", gameInfo);
                            v.getContext().startActivity(intent);
                        }
                    }
                });

                gameIconImageViewList.add(gameIconImageView);
                gameNameTextViewList.add(gameNameTextView);
                briefTextViewList.add(briefTextView);
                tagsTextViewList.add(tagsTextView);
            }

        }

        public void bindStyleThree(HomePageInfo homePageInfo) {
            if (homePageInfo.gameInfoList != null && !homePageInfo.gameInfoList.isEmpty()) {
                int size = 4;
                for (int i = 0; i < size; i++) {
                    GameInfo gameInfo = homePageInfo.gameInfoList.get(i);
                    Glide.with(itemView.getContext()).load(gameInfo.icon)
                            .transform(new RoundedCorners(50))
                            .into(gameIconImageViewList.get(i));
                    gameNameTextViewList.get(i).setText(gameInfo.gameName);
                    briefTextViewList.get(i).setText(gameInfo.brief);
                    tagsTextViewList.get(i).setText(gameInfo.tags);
                }
            } else {
                Log.i("HomePageAdapter", "gameInfoList为空");
            }
        }
    }

}