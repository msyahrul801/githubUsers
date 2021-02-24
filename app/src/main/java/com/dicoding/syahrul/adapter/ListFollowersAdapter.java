package com.dicoding.syahrul.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dicoding.syahrul.R;
import com.dicoding.syahrul.model.RetroGetFollowers;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ListFollowersAdapter extends RecyclerView.Adapter<ListFollowersAdapter.ListViewHolder> {
    private ArrayList<RetroGetFollowers> listUsers = new ArrayList<>();

    public ListFollowersAdapter(ArrayList<RetroGetFollowers> list) {
        listUsers.addAll(list);
    }

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(String username);
    }

    @NonNull
    @Override
    public ListFollowersAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_user, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFollowersAdapter.ListViewHolder holder, int position) {
        RetroGetFollowers user = listUsers.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar_url())
                .transition(withCrossFade(300))
                .circleCrop()
                .into(holder.imgPhoto);
        holder.Name.setText(user.getLogin());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetroGetFollowers user = listUsers.get(holder.getAdapterPosition());
                onItemClickCallback.onItemClicked(user.getLogin());
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
