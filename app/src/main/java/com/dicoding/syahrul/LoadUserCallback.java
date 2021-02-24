package com.dicoding.syahrul;

import com.dicoding.syahrul.Entity.UserEn;

import java.util.ArrayList;

public interface LoadUserCallback {
    void preExecute();
    void postExecute(ArrayList<UserEn> notes);
}
