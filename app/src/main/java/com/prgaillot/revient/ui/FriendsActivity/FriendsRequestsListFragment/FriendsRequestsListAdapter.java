package com.prgaillot.revient.ui.FriendsActivity.FriendsRequestsListFragment;

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
import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.uiModels.FriendRequestItemUiModel;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class FriendsRequestsListAdapter extends RecyclerView.Adapter<FriendsRequestsListAdapter.FRLViewHolder> {

    private final OnValidateClickListener onValidateClickListener;
    private Context context;

    String currentUserId;

    OnDeleteClickListener onDeleteClickListener;

    public FriendsRequestsListAdapter(String userId, List<FriendRequestItemUiModel> friendsRequestsUiModels, OnDeleteClickListener onDeleteClickListener, OnValidateClickListener onValidateClickListener) {
        this.currentUserId = userId;
        this.friendsRequestsUiModels = friendsRequestsUiModels;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onValidateClickListener = onValidateClickListener;
    }

    List<FriendRequestItemUiModel> friendsRequestsUiModels;
    public class FRLViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImg;
        TextView displayName;
        Button yesBtn, noBtn;

        public FRLViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.friendRequestItem_profilImg_imageView);
            displayName = itemView.findViewById(R.id.friendRequestItem_diplayName_textView);
            yesBtn = itemView.findViewById(R.id.friendRequestItem_yes_btn);
            noBtn = itemView.findViewById(R.id.friendRequestItem_no_btn);
        }
    }


    @NonNull
    @Override
    public FRLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friend_request_item_list, parent, false);
        context = parent.getContext();
        return new FriendsRequestsListAdapter.FRLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FRLViewHolder holder, int position) {
        FriendRequestItemUiModel friendRequestItemUiModel = friendsRequestsUiModels.get(position);
        FriendRequest friendRequest = new FriendRequest(friendRequestItemUiModel.getUserSendId(), friendRequestItemUiModel.getUserReceivedId(), friendRequestItemUiModel.getRequestTimestamp());
        friendRequest.setId(friendRequestItemUiModel.getuId());


        Glide.with(context)
                .load(friendRequestItemUiModel.getImgUrl())
                .centerInside()
                .circleCrop()
                .into(holder.profileImg);

        holder.displayName.setText(friendRequestItemUiModel.getUserDisplayName());

        holder.noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(friendRequestItemUiModel.getId());
            }
        });

        holder.yesBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                onValidateClickListener.onValidateClick(friendRequest);
            }
        });

        if (friendRequestItemUiModel.getUserSendId().equals(currentUserId)) {
            holder.yesBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return friendsRequestsUiModels.size();
    }

    public void update(List<FriendRequestItemUiModel> newFriendRequestItemUiModels){
        friendsRequestsUiModels.clear();
        friendsRequestsUiModels.addAll(newFriendRequestItemUiModels);
        notifyDataSetChanged();
    }

    public void refreshUser(String currentUser) {
        currentUserId = currentUser;
        notifyDataSetChanged();
    }

}
