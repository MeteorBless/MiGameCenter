package com.example.migamecenter.httpUtils;

import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.GameInfoPage;
import com.example.migamecenter.bean.HomePageInfo;
import com.example.migamecenter.bean.Page;
import com.example.migamecenter.bean.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("quick-game/game/search")
    Call<BaseGameBean<GameInfoPage>> searchGame(
            @Query("search") String search,
            @Query("current") int current,
            @Query("size") int size
    );

    @GET("quick-game/game/homePage")
    Call<BaseGameBean<Page<HomePageInfo>>> searchHomePage(
            @Query("current") int current,
            @Query("size") int size
            );

//    Call<BaseGameBean<GameInfoPage>> searchGame(
//            @Query("search") String search,
//            @Query("current") int current,
//            @Query("size") int size
//    );
    @GET("/quick-game/api/user/info")
    Call<BaseGameBean<UserInfo>> getUserInfo();


}
