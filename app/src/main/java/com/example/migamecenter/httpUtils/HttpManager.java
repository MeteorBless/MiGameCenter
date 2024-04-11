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

//    public static void getUserInfo(final NetCallBack<BaseGameBean<UserInfo>> netCallBack) {
//        Request request = new Request.Builder()
//                .url("https://hotfix-service-prod.g.mi.com/quick-game/api/user/info")
//                .get()
//                .build();
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) {
//                try {
//                    if (response.isSuccessful()) {
//                        // 解析响应体
//                        String responseBody = response.body().string();
//                        // 使用 Gson 解析 JSON 响应
//                        Gson gson = new Gson();
//                        Type type = new TypeToken<BaseGameBean<UserInfo>>(){}.getType();
//                        BaseGameBean<UserInfo> data = gson.fromJson(responseBody, type);
//
//                        netCallBack.onSuccess(data);
//                    } else {
//                        netCallBack.onFailure(response.code(), "网络请求失败");
//                    }
//                } catch (IOException e) {
//                    netCallBack.onFailure(-1, Log.getStackTraceString(e));
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, IOException e) {
//                netCallBack.onFailure(-1, Log.getStackTraceString(e));
//            }
//        });
//    }


    public static void getUserInfo(final NetCallBack<BaseGameBean<UserInfo>> netCallBack) {

        Request request = new Request.Builder()
                .url("https://hotfix-service-prod.g.mi.com/quick-game/api/user/info")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MTI4NTUzNzMsInN1YiI6IjE4MDYxODQzMTIwIiwiY3JlYXRlZCI6MTcxMjgxMjE3Mzk4NH0.CqKY_U3OvPuo-_xADnscSE_0CgezZY1bPI28zgscvXvQY-5LH6KctGoVd0x3er4s0QthtCOdTuxtf4AZ5xcaQA")
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
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
            public void onFailure(@NonNull Call call, IOException e) {
                netCallBack.onFailure(-1, Log.getStackTraceString(e));
            }
        });
    }

    public static final MediaType jsonMedia = MediaType.parse("application/json;charset=utf-8");


    // 发送手机验证码
    public void sendCode(String phone){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("phone",phone);
        String json = gson.toJson(hashMap);
        RequestBody requestBody = RequestBody.create(jsonMedia,json);

        Request request = new Request.Builder().post(requestBody)
                .addHeader("content-type","application/json")
                .url("https://hotfix-service-prod.g.mi.com/quick-game/api/auth/sendCode")
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

    // 登录
    public void login(String phone, String smsCode,final LoginCallBack callback) {
        // 构造请求体参数
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", phone);
        hashMap.put("smsCode", smsCode);
        String json = gson.toJson(hashMap);
        RequestBody requestBody = RequestBody.create(jsonMedia, json);

        // 构造请求对象
        Request request = new Request.Builder()
                .post(requestBody)
                .addHeader("content-type", "application/json")
                .url("https://hotfix-service-prod.g.mi.com/quick-game/api/auth/login")
                .build();

        // 发送请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure("请求失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 请求成功回调
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String content = body.string();
                        Log.i(TAG,"网络请求结果,content:"+content);
                        callback.onSuccess(content);
                    }
                } else {
                    callback.onFailure("网络请求失败");
                }
            }
        });

//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.i(TAG, "请求失败");
//            }
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                // 请求成功回调
//                Log.i(TAG, "网络请求成功, code:" + response.code());
//                if (response.isSuccessful()) {
//                    ResponseBody body = response.body();
//                    if (body != null) {
//                        String content = body.string();
//                        Log.i(TAG, "网络请求结果, content:" + content);
//                    }
//                }
//            }
//        });
    }

}
