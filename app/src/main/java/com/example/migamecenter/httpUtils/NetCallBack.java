package com.example.migamecenter.httpUtils;

public interface NetCallBack<T> {

    void onSuccess(T data);
    void onFailure(int code,String errMsg);
}
