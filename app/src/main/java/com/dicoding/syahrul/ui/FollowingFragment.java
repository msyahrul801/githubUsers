package com.dicoding.syahrul.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.dicoding.syahrul.adapter.ListFollowingAdapter;
import com.dicoding.syahrul.Github_detailsActivity;
import com.dicoding.syahrul.R;
import com.dicoding.syahrul.databinding.FragmentFollowingBinding;
import com.dicoding.syahrul.model.RetroGetFollowing;
import com.dicoding.syahrul.service.GetService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FollowingFragment extends Fragment {
    ArrayList<RetroGetFollowing> list = new ArrayList<>();
    private FragmentFollowingBinding binding;
    private ListFollowingAdapter adapter;
    public static String username2;


    public static String getUsername() {
        return username2;
    }

    public static void setUsername(String username) {
        username2 = username;
    }

    public FollowingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFollowingBinding.inflate(inflater, container, false);
        binding.rvFollowing.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetService service = retrofit.create(GetService.class);
        Call<List<RetroGetFollowing>> call = service.getFollowing(getUsername(), getString(R.string.token));
        call.enqueue(new Callback<List<RetroGetFollowing>>() {
            @Override
            public void onResponse(Call<List<RetroGetFollowing>> call, Response<List<RetroGetFollowing>> response) {
                assert response.body() != null;
                list.addAll(response.body());
                adapter = new ListFollowingAdapter(list);
                binding.rvFollowing.setAdapter(adapter);
                adapter.setOnItemClickCallback(new ListFollowingAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(String username) {
                        Intent start_detail = new Intent(getActivity(), Github_detailsActivity.class);
                        start_detail.putExtra(Github_detailsActivity.EXTRA_USERNAME, username);
                        startActivity(start_detail);
                        Animatoo.animateSlideLeft(getActivity());
                    }
                });
                binding.shimmerViewContainerFollowing.stopShimmer();
                binding.shimmerViewContainerFollowing.setVisibility(View.GONE);
                binding.rvFollowing.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<RetroGetFollowing>> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
                Log.i("TAG", "onFailure: " + t);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.shimmerViewContainerFollowing.startShimmer();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.shimmerViewContainerFollowing.stopShimmer();
    }
}