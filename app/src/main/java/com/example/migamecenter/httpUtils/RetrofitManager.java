package com.example.migamecenter.httpUtils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.GameInfoPage;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final String TAG = "RetrofitManager";
    private static final String PRE_HTTP_URL = "https://hotfix-service-prod.g.mi.com";
//    private static final String BASE_URL = "https://hotfix-service-prod.g.mi.com/";
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    private static volatile RetrofitManager instance;

    public static RetrofitManager getInstance(){
        if(instance==null){
            synchronized (RetrofitManager.class){
                if(instance==null){
                    instance = new RetrofitManager();
                }

            }
        }
        return instance;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(PRE_HTTP_URL)
            .client(HttpManager.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final ApiService apiService = retrofit.create(ApiService.class);

//    private final GameService gameService = gameRetrofit.create(GameService.class);


    private final Handler handler = new Handler(Looper.getMainLooper());




    public void searchGame(String search, int current, int size, NetCallBack<BaseGameBean<GameInfoPage>> netCallBack){
        Call<BaseGameBean<GameInfoPage>> gameByModule = apiService.searchGame(search,current,size);
        enqueue_game(gameByModule,netCallBack);
    }



    private <T> void enqueue_game(Call<BaseGameBean<T>> gameByModule, NetCallBack<BaseGameBean<T>> netCallBack) {
        gameByModule.enqueue(new Callback<BaseGameBean<T>>() {
            @Override
            public void onResponse(@NonNull Call<BaseGameBean<T>> call, @NonNull Response<BaseGameBean<T>> response) {
                int resPoseCode = response.code();
                if (response.isSuccessful()) {
                    BaseGameBean<T> body = response.body();
                    if (body != null && body.code == 200) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                netCallBack.onSuccess(body);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int code = -1;
                                String msg = "网络请求错误";
                                if (body != null) {
                                    code = body.code;
                                    msg = body.msg;
                                }
                                netCallBack.onFailure(code, msg);
                            }
                        });
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallBack.onFailure(resPoseCode, "网络请求失败");
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseGameBean<T>> call, @NonNull Throwable t) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallBack.onFailure(-1, Log.getStackTraceString(t));
                    }
                });
            }
        });
    }

//    private boolean isLoading = false; // 标记是否正在加载数据
//    private boolean isLastPage = false; // 标记是否已经到达最后一页
//    // 获取isLoading的方法
//    public boolean isLoading() {
//        return isLoading;
//    }
//
//    // 获取isLastPage的方法
//    public boolean isLastPage() {
//        return isLastPage;
//    }

//    private int pageSize = 10; // 默认每页大小为10
//
//    public int getPageSize() {
//        return pageSize;
//    }



}
