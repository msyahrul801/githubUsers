package com.example.consumerapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.consumerapp.Entity.UserEn;
import com.example.consumerapp.R;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ListViewHolder> {
    private ArrayList<UserEn> listUsers = new ArrayList<>();
    private final Activity activity;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(String username);
    }

    public FavoriteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<UserEn> getListUsers() {
        return listUsers;
    }

    public void setListUsers(ArrayList<UserEn> list) {
        this.listUsers.clear();
        this.listUsers.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_user, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(getListUsers().get(position).getAvatar())
                .transition(withCrossFade(300))
                .circleCrop()
                .into(holder.imgPhoto);
        holder.Name.setText(getListUsers().get(position).getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(getListUsers().get(position).getUsername());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView Name;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            Name = itemView.findViewById(R.id.item_name);
        }
    }
}
