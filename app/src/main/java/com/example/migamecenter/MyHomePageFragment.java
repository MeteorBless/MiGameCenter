package com.example.migamecenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.UserInfo;
import com.example.migamecenter.httpUtils.HttpManager;
import com.example.migamecenter.httpUtils.NetCallBack;

import retrofit2.Call;

public class MyHomePageFragment extends Fragment {

    private ImageView ivPersonalAvatar;
    private TextView appLoginTextView;
    private SharedPreferences sharedPreferences;
    private Button btnLogOut;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_LOGIN_TEXT = "loginText";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        ivPersonalAvatar = view.findViewById(R.id.personal_avatar);
        appLoginTextView = view.findViewById(R.id.app_login);

        btnLogOut = view.findViewById(R.id.btn_log_out);

        String savedAvatar = sharedPreferences.getString(KEY_AVATAR, "");
        if (!savedAvatar.isEmpty()) {
            Glide.with(requireActivity()).load(savedAvatar).into(ivPersonalAvatar);
        }

        String savedLoginText = sharedPreferences.getString(KEY_LOGIN_TEXT, "");
        if (!savedLoginText.isEmpty()) {
            appLoginTextView.setText(savedLoginText);
        }


        bindListener();



        return view;
    }

    private void bindListener(){
        appLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireActivity(), LoginActivity.class);

                requireActivity().startActivity(intent);

                getUserInfo();

            }
        });


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivPersonalAvatar.setImageResource(R.drawable.icon_avatar);
                appLoginTextView.setText("点击登录");

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(KEY_AVATAR);
                editor.remove(KEY_LOGIN_TEXT);
                editor.apply();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appLoginTextView = null;
        ivPersonalAvatar = null;
    }

    private void getUserInfo(){
        HttpManager.getUserInfo(new NetCallBack<BaseGameBean<UserInfo>>() {
            @Override
            public void onSuccess(BaseGameBean<UserInfo> data) {
                final String avatar = data.data.avatar;
                final String username = data.data.username;
                // 处理成功的响应数据
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (data.data != null) {
//                            Log.i("MyHomePageFragment", "data:" + data.data.id);
//                            Log.i("MyHomePageFragment", "data:" + data.data.username);
//                            Log.i("MyHomePageFragment", "data:" + data.data.phone);
//                            Log.i("MyHomePageFragment", "data:" + data.data.avatar);
//                            Log.i("MyHomePageFragment", "data:" + data.data.loginStatus);
                            Glide.with(requireActivity()).load(avatar).into(ivPersonalAvatar);
                            appLoginTextView.setText(username);

//                            appLoginTextView.setText(data.data.username);
//                            Glide.with(requireActivity()).load(data.data.avatar).into(ivPersonalAvatar);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_AVATAR, avatar);
                            editor.putString(KEY_LOGIN_TEXT, username);
                            editor.apply();

                        } else {
                            Log.e("MyHomePageFragment", "User data is null");
                        }

                    }
                });


            }

            @Override
            public void onFailure(int code, String msg) {
                // 处理请求失败的情况
                Log.e("MyHomePageFragment", "Network request failed: " + msg);
            }
        });
    }


}

