package com.example.migamecenter.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GameInfo implements Parcelable{
    public String gameName;
    public String brief;

    public String icon;

    public String introduction;

    public String tags;

    public String appId; // appID

    public String packageName; // 游戏包名

    public Long id; // 游戏id

    public String versionName; // 游戏版本

    public String apkUrl; // 游戏apk地址

    public float score; // 游戏评分

    public String playNumFormat; // 多少人在玩，格式化数据

    public String createTime; // 创建时间

    protected GameInfo(Parcel in) {
        gameName = in.readString();
        brief = in.readString();
        icon = in.readString();
        introduction = in.readString();
        tags = in.readString();
        appId = in.readString();
        packageName = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        versionName = in.readString();
        apkUrl = in.readString();
        score = in.readFloat();
        playNumFormat = in.readString();
        createTime = in.readString();
    }

    public static final Parcelable.Creator<GameInfo> CREATOR = new Parcelable.Creator<GameInfo>() {
        @Override
        public GameInfo createFromParcel(Parcel in) {
            return new GameInfo(in);
        }

        @Override
        public GameInfo[] newArray(int size) {
            return new GameInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gameName);
        dest.writeString(brief);
        dest.writeString(icon);
        dest.writeString(introduction);
        dest.writeString(tags);
        dest.writeString(appId);
        dest.writeString(packageName);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(versionName);
        dest.writeString(apkUrl);
        dest.writeFloat(score);
        dest.writeString(playNumFormat);
        dest.writeString(createTime);
    }
}
