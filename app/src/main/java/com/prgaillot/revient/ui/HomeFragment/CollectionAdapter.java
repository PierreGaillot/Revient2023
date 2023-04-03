package com.prgaillot.revient.ui.HomeFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    private static final String TAG = "CollectionAdapter";
    List<Stuff> collection;

    public CollectionAdapter(List<Stuff> collection){this.collection = collection;}


    public static class CollectionViewHolder extends RecyclerView.ViewHolder {

        ImageView stuffImageView;
        TextView stuffTextView;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            stuffImageView = itemView.findViewById(R.id.stuffListItem_imageView);
            stuffTextView = itemView.findViewById(R.id.stuffListItem_textView);
        }
    }

    @NonNull
    @Override
    public CollectionAdapter.CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.stuff_list_item, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.CollectionViewHolder holder, int position) {
        holder.stuffTextView.setText(collection.get(position).getDisplayName());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference(collection.get(position).getImgUrl());
        final long ONE_MEGABYTE = 1024*1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Glide.with(holder.itemView.getContext())
                        .load(bytes)
                        .into(holder.stuffImageView);
            }
        });


    }

    @Override
    public int getItemCount() {
        return collection.size();
    }

    public void update(List<Stuff> collection) {
        this.collection.clear();
        this.collection = collection;
        notifyDataSetChanged();
    }
}
