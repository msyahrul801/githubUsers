package com.example.consumerapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.consumerapp.Entity.UserEn;
import com.example.consumerapp.adapter.FavoriteAdapter;
import com.example.consumerapp.databinding.ActivityFavoriteBinding;
import com.example.consumerapp.helper.MappingHelper;
import com.example.consumerapp.model.Users;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.consumerapp.db.DatabaseContract.UsersColumns.CONTENT_URI;

public class FavoriteActivity extends AppCompatActivity implements LoadUserCallback {
    private ActivityFavoriteBinding binding;
    private FavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.favorite));
        adapter = new FavoriteAdapter(this);
        new LoadNoteAsync(this, this).execute();
        adapter.setOnItemClickCallback(new FavoriteAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(String username) {
                Intent start_detail = new Intent(FavoriteActivity.this, Github_detailsActivity.class);
                start_detail.putExtra(Github_detailsActivity.EXTRA_USERNAME, username);
                startActivity(start_detail);
                Animatoo.animateSlideLeft(FavoriteActivity.this);
            }
        });
    }

    private static class LoadNoteAsync {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadUserCallback> weakCallback;

        private LoadNoteAsync(Context context, LoadUserCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        void execute() {

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            weakCallback.get().preExecute();
            executor.execute(() -> {
                Context context = weakContext.get();
                Cursor dataCursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
                assert dataCursor != null;
                ArrayList<UserEn> notes = MappingHelper.mapCursorToArrayList(dataCursor);

                handler.post(() -> weakCallback.get().postExecute(notes));
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(FavoriteActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadNoteAsync(this, this).execute();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList(EXTRA_STATE, adapter.getListUsers());
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<UserEn> users) {
        Log.i("TAG", "postExecute: "+users);
        if (users.size() > 0) {
            adapter.setListUsers(users);
        } else {
            adapter.setListUsers(new ArrayList<>());
        }
        binding.rvFavorite.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvFavorite.setLayoutManager(layoutManager);
        binding.rvFavorite.setAdapter(adapter);
    }
}