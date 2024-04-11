package com.example.migamecenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private View privacyLayout;

    private View overlay;

    private ImageView iv_app_logo;

    private SharedPreferences sharedPreferences;
    private static final String AGREED_TO_PRIVACY_POLICY = "agreed_to_privacy_policy";

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        privacyLayout = findViewById(R.id.privacy_layout);

        overlay = findViewById(R.id.overlay);

        iv_app_logo = findViewById(R.id.app_logo);

        relativeLayout = findViewById(R.id.relative_splash);

        sharedPreferences = getSharedPreferences("Privacy_policy", Context.MODE_PRIVATE);

        boolean hasAgreed = sharedPreferences.getBoolean(AGREED_TO_PRIVACY_POLICY, false);

        if (hasAgreed) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideUp = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_up);
                    relativeLayout.startAnimation(slideUp);
                    navigateToMainActivity();
                }
            }, 1000); // 延迟2秒执行跳转操作
            return;
        }


        // 延迟执行动画，模拟闪屏页动画结束后显示隐私内容
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showPrivacyContentWithAnimation();
            }
        },1000);


    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showPrivacyContentWithAnimation() {
        overlay.setVisibility(View.VISIBLE);
        privacyLayout.setVisibility(View.VISIBLE);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        privacyLayout.startAnimation(slideUp);

        Drawable img = iv_app_logo.getDrawable();

        img.setColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_IN);

        iv_app_logo.setImageDrawable(img);

        TextView tvContent = findViewById(R.id.tv_content);
        String text = getResources().getString(R.string.string_tv_terms_and_conditions);
        SpannableString spannableString = new SpannableString(text);

        // 找到 "用户协议" 和 "隐私协议" 在文本中的起始和结束位置
        int startUserAgreement = text.indexOf("用户协议");
        int endUserAgreement = startUserAgreement + "用户协议".length();
        int startPrivacyPolicy = text.indexOf("隐私协议");
        int endPrivacyPolicy = startPrivacyPolicy + "隐私协议".length();

        // 创建 ClickableSpan 对象
        ClickableSpan clickableUserAgreement = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // 用户协议点击事件
                Toast.makeText(SplashActivity.this, "查看用户协议", Toast.LENGTH_SHORT).show();
            }
        };

        ClickableSpan clickablePrivacyPolicy = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // 隐私协议点击事件
                Toast.makeText(SplashActivity.this, "查看隐私协议", Toast.LENGTH_SHORT).show();
            }
        };

        // 将 ClickableSpan 应用到相应的文本区域
        spannableString.setSpan(clickableUserAgreement, startUserAgreement, endUserAgreement, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickablePrivacyPolicy, startPrivacyPolicy, endPrivacyPolicy, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将 SpannableString 设置到 TextView 中
        tvContent.setText(spannableString);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance()); // 设置文本可点击


        Button btn_disagree = findViewById(R.id.btn_disagree);

        Button btn_agree_and_use = findViewById(R.id.btn_agree_and_use);

        btn_agree_and_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(AGREED_TO_PRIVACY_POLICY, true);
                editor.apply();
                navigateToMainActivity();
            }
        });

        btn_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // 退出应用
            }
        });
    }


}
