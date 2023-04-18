package com.prgaillot.revient.ui.FriendsActivity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.prgaillot.revient.R;

public class FriendsContentFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FriendsActivityViewPagerAdapter viewPagerAdapter;

    public FriendsContentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_content, container, false);
        tabLayout = view.findViewById(R.id.FriendsActivity_tabLayout);
        viewPager = view.findViewById(R.id.FriendsActivity_viewPager);

        viewPagerAdapter = new FriendsActivityViewPagerAdapter(getActivity());
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

        return view;
    }
}