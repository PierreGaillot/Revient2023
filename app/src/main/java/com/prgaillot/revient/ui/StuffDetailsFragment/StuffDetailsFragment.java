package com.prgaillot.revient.ui.StuffDetailsFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.MainActivity.MainActivityViewModel;
import com.prgaillot.revient.ui.uiModels.StuffItemUiModel;
import com.prgaillot.revient.utils.Callback;

public class StuffDetailsFragment extends Fragment {

    StuffItemUiModel stuffItem;
    private String TAG = "StuffDetailsFragment";

    public StuffDetailsFragment() {
    }

    ImageView stuffImageView;
    TextView displayNameTextView, actionTextView;

    StuffDetailsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stuff_details_fragment, container, false);
        viewModel = new ViewModelProvider(this).get(StuffDetailsViewModel.class);



        Bundle data = getArguments();
        if(data != null) {
            stuffItem = (StuffItemUiModel) data.getSerializable("stuffItem");
            stuffImageView = view.findViewById(R.id.stuffDetails_imageView);
            displayNameTextView = view.findViewById(R.id.stuffDetails_name_textView);
            actionTextView = view.findViewById(R.id.stuffDetails_action_textView);

            if(stuffItem.getStuff().getBorrowerId() != null){

                viewModel.getUser(stuffItem.getStuff().getBorrowerId(), new Callback<User>() {
                    @Override
                    public void onCallback(User user) {
                        actionTextView.setVisibility(View.VISIBLE);
                        actionTextView.setText(String.format("Borrowed to %s", user.getDisplayName()));
                    }
                });

            }


            displayNameTextView.setText(stuffItem.getStuff().getDisplayName());


            String stuffUrl = stuffItem.getStuff().getImgUrl();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference(stuffUrl);
            final long ONE_MEGABYTE = 1024 * 1024;
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Glide.with(getContext())
                            .load(bytes)
                            .centerCrop()
                            .circleCrop()
                            .into(stuffImageView);
                }
            });
        }




        return view;
    }

}