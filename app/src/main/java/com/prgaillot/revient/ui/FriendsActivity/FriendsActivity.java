package com.prgaillot.revient.ui.FriendsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.prgaillot.revient.R;
import com.prgaillot.revient.databinding.ActivityFriendsBinding;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    FriendsActivityViewPagerAdapter viewPagerAdapter;

    Toolbar toolbar;
    FriendsActivityViewModel viewModel;
    private AppBarConfiguration appBarConfiguration;

    List<User> users = new ArrayList<>();
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FriendsActivityViewModel.class);
        com.prgaillot.revient.databinding.ActivityFriendsBinding binding = ActivityFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.friendsActivityToolbar);

        tabLayout = binding.FriendsActivityTabLayout;
        viewPager = binding.FriendsActivityViewPager;
        viewPagerAdapter = new FriendsActivityViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_friends_research);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        viewModel.getCurrentUser(new Callback<User>() {
            @Override
            public void onCallback(User result) {
                users.add(result);
                initFriendsRList();
            }
        });




    }

    private void initFriendsRList() {
    }

    public void openProfileFragment(User user) {
        Bundle userBundle = new Bundle();
        userBundle.putSerializable("user", user);
        navController.navigate(R.id.action_researchFriendsListFragment_to_profileFragment2, userBundle);
    }
}