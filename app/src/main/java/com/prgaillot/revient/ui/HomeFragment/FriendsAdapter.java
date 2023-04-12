package com.prgaillot.revient.ui.HomeFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.User;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    List<User> friends;
    FriendAdapterClickListener friendAdapterClickListener;
    AddFriendsAdapterClickListener addFriendsAdapterClickListener;
    private Context context;

    public FriendsAdapter(List<User> friends, FriendAdapterClickListener friendAdapterClickListener, AddFriendsAdapterClickListener addFriendsAdapterClickListener) {
        this.friends = friends;
        this.friendAdapterClickListener = friendAdapterClickListener;
        this.addFriendsAdapterClickListener = addFriendsAdapterClickListener;
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        ImageView friendImageView;
        Button addFriendsBtn;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            friendImageView = itemView.findViewById(R.id.friendItemList_friend_imageView);
            addFriendsBtn = itemView.findViewById(R.id.friendItemList_addFriends_button);
        }
    }

    @NonNull
    @Override
    public FriendsAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friend_item_list, parent, false);
        context = parent.getContext();
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendsViewHolder holder, int position) {


        if (position < friends.size() && friends.size() > 0) {
            User friend = friends.get(position);
            holder.friendImageView.setVisibility(View.VISIBLE);
            holder.addFriendsBtn.setVisibility(View.INVISIBLE);

            Glide.with(holder.itemView.getContext())
                    .load(friends.get(position).getImgUrl())
                    .centerCrop()
                    .circleCrop()
                    .into(holder.friendImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendAdapterClickListener.onFriendClick(friend);
                }
            });

        } else if (position == friends.size() && friends.size() != 0) {
            holder.addFriendsBtn.setVisibility(View.VISIBLE);
            holder.addFriendsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFriendsAdapterClickListener.navToFriendsActivity();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return friends.size() + 1;
    }

    public void update(List<User> friends) {
        this.friends.clear();
        this.friends = friends;
        notifyDataSetChanged();
    }
}
