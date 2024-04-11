package com.example.migamecenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.migamecenter.bean.GameInfo;

public class DetailActivity extends FragmentActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView iv_back_to_home_page = findViewById(R.id.iv_back_to_home_page);

        LinearLayout app_introduction = findViewById(R.id.app_introduction);

        LinearLayout app_update = findViewById(R.id.app_update);

        ImageView gameIconImageView = findViewById(R.id.game_icon_image_view);

        TextView gameNameTextView = findViewById(R.id.game_name_text_view);

        TextView gameVersionTextView = findViewById(R.id.game_version_text_view);

        TextView tv_app_introduction = findViewById(R.id.tv_app_introduction);

        TextView tv_app_update = findViewById(R.id.tv_app_update);

        GameInfo gameInfo = getIntent().getParcelableExtra("gameInfo");

        // 使用游戏信息更新界面上的内容

        assert gameInfo != null;
        Glide.with(this).load(gameInfo.icon).transform(new RoundedCorners(50))
                .into(gameIconImageView);

        gameNameTextView.setText(gameInfo.gameName);

        String version_name = (String) gameVersionTextView.getText();

        gameVersionTextView.setText(version_name+gameInfo.versionName);

        if(gameInfo.introduction!=null){
            app_introduction.setVisibility(View.VISIBLE);
            tv_app_introduction.setText(gameInfo.introduction);
        }

        if(gameInfo.introduction!=null){
            app_update.setVisibility(View.VISIBLE);
            tv_app_update.setText(gameInfo.introduction);
        }

        iv_back_to_home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
