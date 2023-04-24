package com.prgaillot.revient.ui.CollectionFragment.CollectionFragmentContent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.ui.HomeFragment.CollectionAdapter;
import com.prgaillot.revient.ui.uiModels.StuffItemUiModel;
import com.prgaillot.revient.utils.CircleTimer;

import java.util.List;

public class CollectionContentFragmentAdapter extends RecyclerView.Adapter<CollectionContentFragmentAdapter.CollectionContentFragmentViewHolder> {

    List<StuffItemUiModel> stuffItemUiModels;
    private Context context;

    public CollectionContentFragmentAdapter(List<StuffItemUiModel> stuffItemUiModels) {
        this.stuffItemUiModels = stuffItemUiModels;
    }

    public class CollectionContentFragmentViewHolder extends RecyclerView.ViewHolder {

        ImageView stuffImage, borrowerImage;
        TextView stuffDisplayName, borrowerDisplayName;
        ProgressBar circleTimer;

        public CollectionContentFragmentViewHolder(@NonNull View itemView) {
            super(itemView);

            stuffImage = itemView.findViewById(R.id.stuffListItemVertical_stuffImg_imageView);
            borrowerImage = itemView.findViewById(R.id.stuffListItemVertical_borrowerImg_imageView);
            stuffDisplayName = itemView.findViewById(R.id.stuffListItemVertical_diplayName_textView);
            borrowerDisplayName = itemView.findViewById(R.id.stuffListItemVertical_borrowerName_textView);
            circleTimer = itemView.findViewById(R.id.stuffListItemVertical_progressBar);
        }
    }

    @NonNull
    @Override
    public CollectionContentFragmentAdapter.CollectionContentFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.stuff_list_item_vertical, parent, false);
        context = parent.getContext();
        return new CollectionContentFragmentAdapter.CollectionContentFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionContentFragmentAdapter.CollectionContentFragmentViewHolder holder, int position) {
        StuffItemUiModel stuffItem = stuffItemUiModels.get(position);
        Stuff stuff = stuffItem.getStuff();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference(stuff.getImgUrl());
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                holder.stuffDisplayName.setText(stuff.getDisplayName());

                Glide.with(context)
                        .load(bytes)
                        .centerCrop()
                        .circleCrop()
                        .into(holder.stuffImage);

                Glide.with(context)
                        .load(stuffItem.getActionUrl())
                        .centerCrop()
                        .circleCrop()
                        .into(holder.borrowerImage);

                CircleTimer circleTimer = new CircleTimer(holder.circleTimer, context, stuffItem.getStuff());
                circleTimer.initTimer();
            }
        });


    }

    @Override
    public int getItemCount() {
        return stuffItemUiModels.size();
    }

    public void update(List<StuffItemUiModel> newList){
        this.stuffItemUiModels.clear();
        this.stuffItemUiModels = newList;
        notifyDataSetChanged();
    }
}
