package com.prgaillot.revient.ui.HomeFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.User;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    List<User> friends;

    public FriendsAdapter(List<User> friends) {
        this.friends = friends;
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        ImageView friendImageView;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            friendImageView = itemView.findViewById(R.id.friend_imageView);
        }
    }

    @NonNull
    @Override
    public FriendsAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friend_item_list, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendsViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .load(friends.get(position).getImgUrl())
                .centerCrop()
                .circleCrop()
                .into(holder.friendImageView);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void update(List<User> friends) {
        this.friends.clear();
        this.friends = friends;
        notifyDataSetChanged();
    }
}
