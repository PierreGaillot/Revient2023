package com.prgaillot.revient.ui.NewStuffActivity.NewSuffFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.User;

public class BorrowerFragment extends Fragment {
    String displayName;
    String imgUrl;
    ImageView imageImageView;
    TextView displayNameTextView;
    Button addButton;


    public BorrowerFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle userBundle = this.getArguments();

        if (userBundle != null){

            User user = (User) userBundle.getSerializable("userKey");
            displayName = user.getDisplayName();
            imgUrl = user.getImgUrl();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.friend_search_list_item, container, false);
        imageImageView = view.findViewById(R.id.RFIL_userImage_imageView);
        displayNameTextView = view.findViewById(R.id.RFIL_userDisplayName_textView);
        addButton = view.findViewById(R.id.RFIL_iconButton_btn);

        addButton.setVisibility(View.GONE);

        Glide.with(getContext())
                .load(imgUrl)
                .centerInside()
                .circleCrop()
                .into(imageImageView);

        displayNameTextView.setText(displayName);


        return view;
    }
}