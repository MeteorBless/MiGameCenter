package com.example.migamecenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.UserInfo;
import com.example.migamecenter.httpUtils.HttpManager;
import com.example.migamecenter.httpUtils.NetCallBack;

import retrofit2.Call;

public class MyHomePageFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private ImageView ivPersonalAvatar;
    private TextView appLoginTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        // 初始化 SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        ivPersonalAvatar = view.findViewById(R.id.personal_avatar);
        appLoginTextView = view.findViewById(R.id.app_login);

        // 加载保存的状态
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            ivPersonalAvatar.setImageResource(R.drawable.icon_personal_avatar);
            appLoginTextView.setText("怕上火的王老菊");
        } else {
            ivPersonalAvatar.setImageResource(R.drawable.icon_avatar);
            appLoginTextView.setText("点击登录");
        }

        appLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新状态并保存
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                ivPersonalAvatar.setImageResource(R.drawable.icon_personal_avatar);
                appLoginTextView.setText("怕上火的王老菊");
            }
        });

        Button btnLogOut = view.findViewById(R.id.btn_log_out);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新状态并保存
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                ivPersonalAvatar.setImageResource(R.drawable.icon_avatar);
                appLoginTextView.setText("点击登录");
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences = null; // 解除对 SharedPreferences 的引用
    }

//    private void getUserInfo(){
//        HttpManager.getUserInfo(new NetCallBack<BaseGameBean<UserInfo>>() {
//            @Override
//            public void onSuccess(BaseGameBean<UserInfo> data) {
//                // 处理成功的响应数据
//                Log.i("MyHomePageFragment","data:"+data.data.id);
//                Log.i("MyHomePageFragment","data:"+data.data.username);
//                Log.i("MyHomePageFragment","data:"+data.data.phone);
//                Log.i("MyHomePageFragment","data:"+data.data.avatar);
//                Log.i("MyHomePageFragment","data:"+data.data.loginStatus);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                // 处理请求失败的情况
//                Log.e("MyHomePageFragment", "Network request failed: " + msg);
//            }
//        });
//    }


}

