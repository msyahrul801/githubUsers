package com.dicoding.syahrul;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.dicoding.syahrul.databinding.ActivityFullscreenBinding;

import java.util.Objects;

public class FullscreenActivity extends AppCompatActivity {
    ActivityFullscreenBinding binding;
    public static String avatar;
    public static String username;

    public static String getAvatar() {
        return avatar;
    }

    public static void setAvatar(String avatar) {
        FullscreenActivity.avatar = avatar;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        FullscreenActivity.username = username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getUsername());
        Glide.with(this)
                .load(getAvatar())
                .into(binding.fullAvatar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(FullscreenActivity.this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}