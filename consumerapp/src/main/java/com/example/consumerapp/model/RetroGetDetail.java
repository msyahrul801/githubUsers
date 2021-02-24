package com.example.consumerapp.model;

import com.google.gson.annotations.SerializedName;

public class RetroGetDetail {
    @SerializedName("name")
    private String name;
    @SerializedName("company")
    private String company;
    @SerializedName("location")
    private String location;
    @SerializedName("followers")
    private int followers;
    @SerializedName("following")
    private int following;
    @SerializedName("avatar_url")
    private String avatar_url;


    public RetroGetDetail(String avatar_url, String name, String company, String location, int followers, int following) {
        this.name = name;
        this.company = company;
        this.location = location;
        this.followers = followers;
        this.following = following;
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
