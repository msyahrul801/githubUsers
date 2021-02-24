package com.example.consumerapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.consumerapp.Entity.UserEn;
import com.example.consumerapp.databinding.ActivityGithubDetailsBinding;
import com.example.consumerapp.helper.MappingHelper;
import com.example.consumerapp.model.RetroGetDetail;
import com.example.consumerapp.service.GetService;
import com.example.consumerapp.ui.FollowersFragment;
import com.example.consumerapp.ui.FollowingFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.consumerapp.db.DatabaseContract.UsersColumns.AVATAR;
import static com.example.consumerapp.db.DatabaseContract.UsersColumns.CONTENT_URI;
import static com.example.consumerapp.db.DatabaseContract.UsersColumns.USERNAME;

public class Github_detailsActivity extends AppCompatActivity implements LoadUserCallback {

    public static final String EXTRA_USERNAME = "username";
    private static int FOLLOWERS;
    private static int FOLLOWING;
    private ActivityGithubDetailsBinding binding;
    ArrayList<String> listUsername = new ArrayList<>();

    String username;
    String avatar;

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGithubDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setUsername(getIntent().getStringExtra(EXTRA_USERNAME));
        FollowersFragment.setUsername(username);
        FollowingFragment.setUsername(username);
        setSupportActionBar(binding.toolbarDetail);
        Objects.requireNonNull(getSupportActionBar()).setTitle(username);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetService service = retrofit.create(GetService.class);
        Call<RetroGetDetail> call = service.getDetail(username, getString(R.string.token));
        call.enqueue(new Callback<RetroGetDetail>() {
            @Override
            public void onResponse(Call<RetroGetDetail> call, Response<RetroGetDetail> response) {
                assert response.body() != null;
                setAvatar(response.body().getAvatar_url());
                Glide.with(Github_detailsActivity.this)
                        .load(response.body().getAvatar_url())
                        .transition(withCrossFade(300))
                        .circleCrop()
                        .into(binding.avatarDetail);
                setFOLLOWERS(response.body().getFollowers());
                setFOLLOWING(response.body().getFollowing());
                if (response.body().getName() == null) {
                    binding.nameDetail.setText(getString(R.string.private1));
                } else {
                    binding.nameDetail.setText(response.body().getName());
                }
                if (response.body().getLocation() == null) {
                    binding.locationDetail.setText(getString(R.string.private1));
                } else {
                    binding.locationDetail.setText(response.body().getLocation());
                }
                if (response.body().getCompany() == null) {
                    binding.companyDetail.setText(getString(R.string.private1));
                } else {
                    binding.companyDetail.setText(response.body().getCompany());
                }
                binding.avatarDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent start_fullscreen = new Intent(Github_detailsActivity.this, FullscreenActivity.class);
                        FullscreenActivity.setAvatar(response.body().getAvatar_url());
                        FullscreenActivity.setUsername(username);
                        startActivity(start_fullscreen);
                        Animatoo.animateSlideLeft(Github_detailsActivity.this);
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.shimmerViewContainerDetail.stopShimmer();
                        binding.shimmerViewContainerDetail.setVisibility(View.GONE);
                        binding.detailLinear.setVisibility(View.VISIBLE);
                    }
                }, 500);
                binding.tab.setupWithViewPager(binding.viewPager);
                SetupViewPager();
            }

            @Override
            public void onFailure(Call<RetroGetDetail> call, Throwable t) {
                Toast.makeText(Github_detailsActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();

            }
        });

        new LoadNoteAsync(this, this).execute();

    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<UserEn> notes) {
        listUsername.clear();
        for (int i = 0; i < notes.size(); i++) {
            listUsername.add(notes.get(i).getUsername());
        }
        Log.i("TAG", "postExecute: "+listUsername);
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
                ArrayList<UserEn> notes = MappingHelper.mapCursorToArrayList(dataCursor);

                handler.post(() -> weakCallback.get().postExecute(notes));
            });
        }
    }

    private void SetupViewPager() {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FollowersFragment(), String.format(getString(R.string.followers), getFOLLOWERS()));
        adapter.AddFragment(new FollowingFragment(), String.format(getString(R.string.following), getFOLLOWING()));
        binding.viewPager.setAdapter(adapter);
    }

    private static class MyViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fr = new ArrayList<>();
        private List<String> title = new ArrayList<>();

        MyViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        void AddFragment(Fragment fragment, String jd) {
            fr.add(fragment);
            this.title.add(jd);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fr.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(Github_detailsActivity.this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static int getFOLLOWERS() {
        return FOLLOWERS;
    }

    public static void setFOLLOWERS(int FOLLOWERS) {
        Github_detailsActivity.FOLLOWERS = FOLLOWERS;
    }

    public static int getFOLLOWING() {
        return FOLLOWING;
    }

    public static void setFOLLOWING(int FOLLOWING) {
        Github_detailsActivity.FOLLOWING = FOLLOWING;
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.shimmerViewContainerDetail.startShimmer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.shimmerViewContainerDetail.stopShimmer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        if (listUsername.contains(getUsername())) {
            favoriteItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_check));
        } else {
            favoriteItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_uncheck));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_favorite) {
            if (listUsername.contains(getUsername())) {
                getContentResolver().delete(Uri.parse(CONTENT_URI+"/"+getUsername()),USERNAME, new String[]{getUsername()});
                item.setIcon(R.drawable.ic_baseline_favorite_uncheck);
            } else {
                ContentValues values = new ContentValues();
                values.put(USERNAME, getUsername());
                values.put(AVATAR, getAvatar());
                getContentResolver().insert(CONTENT_URI, values);
                item.setIcon(R.drawable.ic_baseline_favorite_check);
            }
        }
        return true;
    }
}