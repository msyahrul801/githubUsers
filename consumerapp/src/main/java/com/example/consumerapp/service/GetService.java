package com.example.consumerapp.service;

import com.example.consumerapp.model.RetroGetDetail;
import com.example.consumerapp.model.RetroGetFollowers;
import com.example.consumerapp.model.RetroGetFollowing;
import com.example.consumerapp.model.RetroGetUsers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetService {
    @GET("/search/users")
    Call<RetroGetUsers> getAllUsers(@Query("q") String query, @Header("Authorization") String token);

    @GET("/users/{user}")
    Call<RetroGetDetail> getDetail(@Path("user") String user, @Header("Authorization") String token);

    @GET("/users/{user}/followers")
    Call<List<RetroGetFollowers>> getFollowers(@Path("user") String user, @Header("Authorization") String token);

    @GET("/users/{user}/following")
    Call<List<RetroGetFollowing>> getFollowing(@Path("user") String user, @Header("Authorization") String token);

}
