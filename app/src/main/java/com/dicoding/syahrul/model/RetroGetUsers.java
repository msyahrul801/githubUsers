package com.dicoding.syahrul.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroGetUsers {
    @SerializedName("items")
    List<Users> users;

    public RetroGetUsers (List<Users> users) {
        this.users = users;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
}
