package com.example.migamecenter.httpUtils;

import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.GameInfoPage;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("quick-game/game/search")
    Call<BaseGameBean<GameInfoPage>> searchGame(
            @Query("search") String search,
            @Query("current") int current,
            @Query("size") int size
    );

}
