package com.prgaillot.revient.ui.ProfileFragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

public class ProfileFragment extends Fragment {

    private static final String USER_ARG = "user";

    ProfileFragmentViewModel viewModel;
    private User user;
    TextView userDisplayNameTextView;
    ImageView userImageView;
    Button friendActionBtn;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileFragmentViewModel.class);


        Bundle data = getArguments();
        if (data != null) {
            user = (User) getArguments().getSerializable(USER_ARG);
            userDisplayNameTextView = view.findViewById(R.id.profilFrag_userDisplayName_textView);
            userImageView = view.findViewById(R.id.profilFrag_userprofilImg_imageView);
            friendActionBtn = view.findViewById(R.id.profilFrag_friendAction_btn);

            userDisplayNameTextView.setText(user.getDisplayName());
            Glide.with(getContext()).load(user.getImgUrl()).centerCrop().circleCrop().into(userImageView);

            viewModel.checkIsFriend(user.getUid(), new Callback<Boolean>() {
                @Override
                public void onCallback(Boolean result) {
                    if(result){
                        Drawable deleteFriendIcon = getContext().getDrawable(R.drawable.ic_baseline_checkroom_24);
                        friendActionBtn.setBackgroundColor(getContext().getColor(R.color.rv_warn));
                        friendActionBtn.setCompoundDrawables(deleteFriendIcon, null, null,null);
                        friendActionBtn.setText("remove friends");
                    } else {
                        friendActionBtn.setText("add to friends");
                    }
                }
            });
        }

        // Inflate the layout for this fragment
        return view;
    }
}