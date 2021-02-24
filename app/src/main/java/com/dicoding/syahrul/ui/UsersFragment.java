package com.dicoding.syahrul.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.dicoding.syahrul.adapter.ListUserAdapter;
import com.dicoding.syahrul.Github_detailsActivity;
import com.dicoding.syahrul.R;
import com.dicoding.syahrul.databinding.FragmentUsersBinding;
import com.dicoding.syahrul.model.RetroGetUsers;
import com.dicoding.syahrul.model.Users;
import com.dicoding.syahrul.service.GetService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersFragment extends Fragment {
    ArrayList<Users> list = new ArrayList<>();
    private ListUserAdapter adapter;
    private FragmentUsersBinding binding;
    private static String query = "a";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        binding.rvUsers.setHasFixedSize(true);
        return binding.getRoot();
    }

    public void searchUsers(String query) {
        closeKeyboard();
        binding.rvUsers.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        list.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetService service = retrofit.create(GetService.class);
        Call<RetroGetUsers> call = service.getAllUsers(query, getString(R.string.token));
        call.enqueue(new Callback<RetroGetUsers>() {
            @Override
            public void onResponse(Call<RetroGetUsers> call, Response<RetroGetUsers> response) {
                assert response.body() != null;
                binding.rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
                list.addAll(response.body().getUsers());
                adapter = new ListUserAdapter(list);
                binding.rvUsers.setAdapter(adapter);
                adapter.setOnItemClickCallback(new ListUserAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(String username) {
                        Intent start_detail = new Intent(getActivity(), Github_detailsActivity.class);
                        start_detail.putExtra(Github_detailsActivity.EXTRA_USERNAME, username);
                        startActivity(start_detail);
                        Animatoo.animateSlideLeft(requireActivity());
                    }
                });
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.rvUsers.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<RetroGetUsers> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed_load), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        searchUsers(getQuery());
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.shimmerViewContainer.stopShimmer();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_item, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setQuery(query);
                searchUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("TAG", "onQueryTextChange: " + newText);
                return true;
            }
        });
    }

    public static String getQuery() {
        return query;
    }

    public static void setQuery(String query) {
        UsersFragment.query = query;
    }
}