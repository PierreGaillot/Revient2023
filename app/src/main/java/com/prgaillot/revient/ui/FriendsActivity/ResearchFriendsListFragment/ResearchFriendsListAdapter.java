package com.prgaillot.revient.ui.FriendsActivity.ResearchFriendsListFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.uiModels.UserWithStatus;

import java.util.List;

public class ResearchFriendsListAdapter extends RecyclerView.Adapter<ResearchFriendsListAdapter.RFLViewHolder> {

    List<UserWithStatus> usersWithStatus;
    private Context context;

    UserAdapterClickListener userClickListener;
    AddFriendAdapterClickListener addFriendClickListener;

    public ResearchFriendsListAdapter( List<UserWithStatus> usersWithStatus, UserAdapterClickListener userClickListener, AddFriendAdapterClickListener addFriendClickListener) {
        this.usersWithStatus = usersWithStatus;
        this.userClickListener = userClickListener;
        this.addFriendClickListener = addFriendClickListener;
    }

    @NonNull
    @Override
    public RFLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friend_search_list_item, parent, false);
        context = parent.getContext();
        return new ResearchFriendsListAdapter.RFLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResearchFriendsListAdapter.RFLViewHolder holder, int position) {

        UserWithStatus userWithStatus  = usersWithStatus.get(position);
        User user = userWithStatus.getUser();
        int status = userWithStatus.getStatus();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userClickListener.onUserClick(user);
            }
        });

        switch (status){
            case 0:
                holder.addIconBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFriendClickListener.onAddFriendClick(user);
                    }
                });
                break;
            case 2:
                holder.userDisplayName.setTextColor(context.getColor(R.color.rv_primary_primary));
                holder.addIconBtn.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        Glide.with(holder.itemView)
                .load(user.getImgUrl())
                .centerCrop()
                .circleCrop()
                .into(holder.userImage);

        holder.userDisplayName.setText(user.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return usersWithStatus.size();
    }

    public class RFLViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userDisplayName;

        Button addIconBtn;
        public RFLViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.RFIL_userImage_imageView);
            userDisplayName = itemView.findViewById(R.id.RFIL_userDisplayName_textView);
            addIconBtn = itemView.findViewById(R.id.RFIL_iconButton_btn);
        }
    }

    public void update(List<UserWithStatus> usersList){
        usersWithStatus.clear();
        usersWithStatus.addAll(usersList);
        notifyDataSetChanged();
    }
}
