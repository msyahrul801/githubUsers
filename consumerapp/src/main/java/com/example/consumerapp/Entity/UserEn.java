package com.example.consumerapp.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class UserEn implements Parcelable {
    private int id;
    private String username;
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.avatar);
    }

    public UserEn() {

    }

    public UserEn(int id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
    }

    private UserEn(Parcel in) {
        this.id = in.readInt();
        this.username = in.readString();
        this.avatar = in.readString();
    }

    public static final Creator<UserEn> CREATOR = new Creator<UserEn>() {
        @Override
        public UserEn createFromParcel(Parcel source) {
            return new UserEn(source);
        }

        @Override
        public UserEn[] newArray(int size) {
            return new UserEn[size];
        }
    };
}
