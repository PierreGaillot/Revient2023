package com.prgaillot.revient.ui.FriendsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.Toast;

import com.prgaillot.revient.R;
import com.prgaillot.revient.databinding.ActivityFriendsBinding;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {


    Toolbar toolbar;
    FriendsActivityViewModel viewModel;
    private AppBarConfiguration appBarConfiguration;

    List<User> users = new ArrayList<>();
    private NavController navController;

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FriendsActivityViewModel.class);
        com.prgaillot.revient.databinding.ActivityFriendsBinding binding = ActivityFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.friendsActivityToolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_friend);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        viewModel.getCurrentUser(new Callback<User>() {
            @Override
            public void onCallback(User result) {
                currentUser = result;
                initFriendsRList();
            }
        });
    }

    private void initFriendsRList() {
    }

    public void openProfileFragment(User user) {
        Bundle userBundle = new Bundle();
        userBundle.putSerializable("user", user);
        navController.navigate(R.id.action_friendsContentFragment_to_profileFragment2, userBundle);
    }

    public void sendFriendRequest(User user) {
        viewModel.sendFriendRequest(currentUser.getUid(),user.getUid(),  new Callback<Void>() {
            @Override
            public void onCallback(Void result) {
                Toast.makeText(getBaseContext(), "Request has be send", Toast.LENGTH_SHORT).show();
            }
        });
    }
}