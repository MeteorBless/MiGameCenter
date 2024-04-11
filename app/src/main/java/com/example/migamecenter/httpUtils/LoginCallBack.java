package com.example.migamecenter.httpUtils;

public interface LoginCallBack {
    void onSuccess(String content);
    void onFailure(String errorMessage);
}
