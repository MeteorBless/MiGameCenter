package com.example.migamecenter.httpUtils;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.GameInfoPage;
import com.example.migamecenter.bean.HomePageInfo;
import com.example.migamecenter.bean.Page;
import com.example.migamecenter.bean.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpManager {
    private static final Gson gson = new Gson();
    private static volatile HttpManager instance;
    private static final String TAG = "HttpManager";
    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    public void postByFormData(String phone){
        FormBody requestBody = new FormBody.Builder()
                .add("phone",phone)
                .build();
        Request request = new Request.Builder().post(requestBody)
                .addHeader("content-type","application/json")
                .url("https://hotfix-service-prod.g.mi.com/quick-game/api/auth/sendCodeByFormData")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG,"请求失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i(TAG,"网络请求成功,code:"+response.code());
                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body!=null){
                        String content = body.string();
                        Log.i(TAG,"网络请求结果,content:"+content);
                    }
                }
            }
        });
    }
    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    public static void getRequestWithCallBack(NetCallBack<String> netCallBack){
        Request request = new Request.Builder()
                .get()
                .url("https://hotfix-service-prod.g.mi.com/quick-game/game/109")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG,"请求失败");
                netCallBack.onFailure(-1,"请求失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i(TAG,"请求成功, code="+response.code());
                if (response.isSuccessful()){
                    ResponseBody body = response.body();
                    if(body!=null){
                        String content = body.string();
                        Log.i(TAG,"网络请求结果："+content);
                        netCallBack.onSuccess(content);
                    }
                }
            }
        });

    }

    public void searchGame(String search, int current, int size, NetCallBack<BaseGameBean<GameInfoPage>> netCallBack) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://hotfix-service-prod.g.mi.com/quick-game/game/search")).newBuilder();
        urlBuilder.addQueryParameter("search", search);
        urlBuilder.addQueryParameter("current", String.valueOf(current));
        urlBuilder.addQueryParameter("size", String.valueOf(size));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 解析响应体
                    String responseBody = response.body().string();
                    // 使用 Gson 解析 JSON 响应
                    Gson gson = new Gson();
                    Type type = new TypeToken<BaseGameBean<GameInfoPage>>(){}.getType();
                    BaseGameBean<GameInfoPage> data = gson.fromJson(responseBody, type);

                    netCallBack.onSuccess(data);
                } else {
                    netCallBack.onFailure(response.code(), "网络请求失败");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                netCallBack.onFailure(-1, Log.getStackTraceString(e));
            }
        });
    }

    public void searchHomePage(int current, int size, NetCallBack<BaseGameBean<Page<HomePageInfo>>> netCallBack) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://hotfix-service-prod.g.mi.com/quick-game/game/homePage")).newBuilder();
        urlBuilder.addQueryParameter("current", String.valueOf(current));
        urlBuilder.addQueryParameter("size", String.valueOf(size));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 解析响应体
                    String responseBody = response.body().string();
                    // 使用 Gson 解析 JSON 响应
                    Gson gson = new Gson();
                    Type type = new TypeToken<BaseGameBean<Page<HomePageInfo>>>(){}.getType();
                    BaseGameBean<Page<HomePageInfo>> data = gson.fromJson(responseBody, type);

                    netCallBack.onSuccess(data);
                } else {
                    netCallBack.onFailure(response.code(), "网络请求失败");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                netCallBack.onFailure(-1, Log.getStackTraceString(e));
            }
        });
    }

    public static void getUserInfo(final NetCallBack<BaseGameBean<UserInfo>> netCallBack) {
        Request request = new Request.Builder()
                .url("https://hotfix-service-prod.g.mi.com/quick-game/api/user/info")
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        // 解析响应体
                        String responseBody = response.body().string();
                        // 使用 Gson 解析 JSON 响应
                        Gson gson = new Gson();
                        Type type = new TypeToken<BaseGameBean<UserInfo>>(){}.getType();
                        BaseGameBean<UserInfo> data = gson.fromJson(responseBody, type);

                        netCallBack.onSuccess(data);
                    } else {
                        netCallBack.onFailure(response.code(), "网络请求失败");
                    }
                } catch (IOException e) {
                    netCallBack.onFailure(-1, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                netCallBack.onFailure(-1, Log.getStackTraceString(e));
            }
        });
    }


}
