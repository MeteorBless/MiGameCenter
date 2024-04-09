package com.example.migamecenter;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.migamecenter.bean.GameInfo;

public class DetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView gameIconImageView = findViewById(R.id.game_icon_image_view);

        TextView gameNameTextView = findViewById(R.id.game_name_text_view);

        TextView briefTextView = findViewById(R.id.brief_text_view);

        TextView tagsTextView = findViewById(R.id.tags_text_view);

        GameInfo gameInfo = getIntent().getParcelableExtra("gameInfo");
        // 使用游戏信息更新界面上的内容

        assert gameInfo != null;
        Glide.with(this).load(gameInfo.icon).transform(new RoundedCorners(50))
                .into(gameIconImageView);

        gameNameTextView.setText(gameInfo.gameName);

        briefTextView.setText(gameInfo.brief);

        tagsTextView.setText(gameInfo.tags);

    }
}
