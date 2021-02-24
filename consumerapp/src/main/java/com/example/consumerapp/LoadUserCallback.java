package com.example.consumerapp;

import com.example.consumerapp.Entity.UserEn;

import java.util.ArrayList;

public interface LoadUserCallback {
    void preExecute();
    void postExecute(ArrayList<UserEn> notes);
}
