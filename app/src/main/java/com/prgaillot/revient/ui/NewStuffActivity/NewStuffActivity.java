package com.prgaillot.revient.ui.NewStuffActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.prgaillot.revient.R;
import com.prgaillot.revient.databinding.ActivityNewStuffBinding;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.NewStuffActivity.NewSuffFragment.NewStuffFragment;
import com.prgaillot.revient.utils.Callback;

public class NewStuffActivity extends AppCompatActivity {
    private static final String TAG = "NewStuffActivity";
    Button loanByEmailBtn, loanToFriend;
    private NewStuffActivityViewModel viewModel;

    BottomSheetDialog loanToBottomSheetDial;
    private View loanToBSDView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private User borrower;

    Bundle userBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.prgaillot.revient.databinding.ActivityNewStuffBinding binding = ActivityNewStuffBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(NewStuffActivityViewModel.class);

        userBundle = new Bundle();
        userBundle = getIntent().getExtras();



        View view = binding.getRoot();
        setContentView(view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_newStuff);

        if (userBundle.getSerializable("userKey") != null) {
            borrower = (User) userBundle.getSerializable("userKey");
            Log.d(TAG, borrower.getDisplayName());
            navController.setGraph(R.navigation.new_stuff_nav_graph, userBundle);
        }

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        loanToBottomSheetDial = new BottomSheetDialog(NewStuffActivity.this);
        loanToBSDView = getLayoutInflater().inflate(R.layout.loan_to_bt_dialg, null, false);
        loanByEmailBtn = loanToBSDView.findViewById(R.id.dialBtSheetLoanTo_loanToEmail_btn);
        loanToFriend = loanToBSDView.findViewById(R.id.dialBtSheetLoanTo_loanToFriend_btn);
        loanByEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanToBottomSheetDial.dismiss();
                Bundle isEmailBorrowerBundle = new Bundle();
                isEmailBorrowerBundle.putBoolean("isEmailBorrowerKey", true);
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                int id  = navController.getCurrentDestination().getId();
                navController.navigate(id, isEmailBorrowerBundle);
                ft.commit();
            }
        });

        loanToFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanToBottomSheetDial.dismiss();
                navController.navigate(R.id.action_newStuffFragment_to_friendsListFragment);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_newStuff);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void openLoanToBottomSheetDial() {
        loanToBottomSheetDial.setContentView(loanToBSDView);
        loanToBottomSheetDial.show();
    }

    public void onFriendClick(User user) {
//        NewStuffFragment newStuffFragment = new NewStuffFragment(user);
        Bundle userBundle = new Bundle();
        userBundle.putSerializable("userKey", user);
        navController.navigate(R.id.action_friendsListFragment_to_newStuffFragment, userBundle);
    }
}