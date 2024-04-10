package com.example.migamecenter.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.migamecenter.R;
import com.example.migamecenter.bean.GameInfo;
import com.example.migamecenter.bean.HomePageInfo;

import java.util.ArrayList;
import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HomePageInfo> homePageInfoList = new ArrayList<>();

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

//    public static class StyleOneViewHolder extends RecyclerView.ViewHolder {
//        private final ImageView gameIconImageView;
//        private final TextView gameNameTextView;
//
//        private final TextView gamePlayNumTextView;
//
//        public StyleOneViewHolder(@NonNull View itemView) {
//            super(itemView);
//            gameIconImageView = itemView.findViewById(R.id.game_icon_image_view);
//            gameNameTextView = itemView.findViewById(R.id.game_name_text_view);
//            gamePlayNumTextView = itemView.findViewById(R.id.play_num_text_view);
//        }
//
//
//        public void bindStyleOne(HomePageInfo homePageInfo) {
//            if (homePageInfo.gameInfoList != null && !homePageInfo.gameInfoList.isEmpty()) {
//                Glide.with(itemView.getContext()).load(homePageInfo.gameInfoList.get(0).icon)
//                        .transform(new RoundedCorners(50))
//                        .into(gameIconImageView);
//                gameNameTextView.setText(homePageInfo.gameInfoList.get(0).gameName);
//
//                gamePlayNumTextView.setText(homePageInfo.gameInfoList.get(0).playNumFormat);
//            } else {
//                Log.i("HomePageAdapter","gameInfoList为空");
//            }
//        }
//
//
//    }

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

                gameIconImageViewList.add(gameIconImageView);
                gameNameTextViewList.add(gameNameTextView);
                gamePlayNumTextViewList.add(gamePlayNumTextView);
            }
        }

        public void bindStyleOne(HomePageInfo homePageInfo) {
            if (homePageInfo.gameInfoList != null && !homePageInfo.gameInfoList.isEmpty()) {
                int size = 6;
                for (int i = 0; i < size; i++) {
                    GameInfo gameInfo = homePageInfo.gameInfoList.get(i);
                    Glide.with(itemView.getContext()).load(gameInfo.icon)
                            .transform(new RoundedCorners(50))
                            .into(gameIconImageViewList.get(i));
                    gameNameTextViewList.get(i).setText(gameInfo.gameName);
                    gamePlayNumTextViewList.get(i).setText(gameInfo.playNumFormat);
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

                gameIconImageViewList.add(gameIconImageView);
                gameNameTextViewList.add(gameNameTextView);
                gamePlayNumTextViewList.add(gamePlayNumTextView);
            }
        }

        public void bindStyleTwo(HomePageInfo homePageInfo) {
            if (homePageInfo.gameInfoList != null && !homePageInfo.gameInfoList.isEmpty()) {
                int size = 8;
                for (int i = 0; i < size; i++) {
                    GameInfo gameInfo = homePageInfo.gameInfoList.get(i);
                    Glide.with(itemView.getContext()).load(gameInfo.icon)
                            .transform(new RoundedCorners(50))
                            .into(gameIconImageViewList.get(i));
                    gameNameTextViewList.get(i).setText(gameInfo.gameName);
                    gamePlayNumTextViewList.get(i).setText(gameInfo.playNumFormat);
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