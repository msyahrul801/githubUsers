package com.example.consumerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.consumerapp.Github_detailsActivity;
import com.example.consumerapp.R;
import com.example.consumerapp.adapter.ListFollowersAdapter;
import com.example.consumerapp.databinding.FragmentFollowersBinding;
import com.example.consumerapp.model.RetroGetFollowers;
import com.example.consumerapp.service.GetService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FollowersFragment extends Fragment {
    ArrayList<RetroGetFollowers> list = new ArrayList<>();
    private FragmentFollowersBinding binding;
    private ListFollowersAdapter adapter;
    public static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        FollowersFragment.username = username;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFollowersBinding.inflate(inflater, container, false);
        list.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetService service = retrofit.create(GetService.class);
        Call<List<RetroGetFollowers>> call = service.getFollowers(getUsername(), getString(R.string.token));
        call.enqueue(new Callback<List<RetroGetFollowers>>() {
            @Override
            public void onResponse(Call<List<RetroGetFollowers>> call, Response<List<RetroGetFollowers>> response) {
                assert response.body() != null;
                binding.rvFollowers.setLayoutManager(new LinearLayoutManager(getActivity()));
                list.addAll(response.body());
                adapter = new ListFollowersAdapter(list);
                binding.rvFollowers.setAdapter(adapter);
                adapter.setOnItemClickCallback(new ListFollowersAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(String username) {
                        Intent start_detail = new Intent(getActivity(), Github_detailsActivity.class);
                        start_detail.putExtra(Github_detailsActivity.EXTRA_USERNAME, username);
                        startActivity(start_detail);
                        Animatoo.animateSlideLeft(getActivity());
                    }
                });
                binding.shimmerViewContainerFollowers.stopShimmer();
                binding.shimmerViewContainerFollowers.setVisibility(View.GONE);
                binding.rvFollowers.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<RetroGetFollowers>> call, Throwable t) {
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
        binding.shimmerViewContainerFollowers.startShimmer();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.shimmerViewContainerFollowers.stopShimmer();
    }
}