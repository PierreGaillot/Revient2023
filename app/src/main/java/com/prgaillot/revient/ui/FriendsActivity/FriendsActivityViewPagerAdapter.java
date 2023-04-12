package com.prgaillot.revient.ui.FriendsActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.prgaillot.revient.ui.FriendsActivity.FriendsListFragment.FriendsListFragment;
import com.prgaillot.revient.ui.FriendsActivity.FriendsRequestsListFragment.FriendsRequestsListFragment;
import com.prgaillot.revient.ui.FriendsActivity.ResearchFriendsListFragment.ResearchFriendsListFragment;

public class FriendsActivityViewPagerAdapter extends FragmentStateAdapter {

    public FriendsActivityViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
                return new FriendsRequestsListFragment();
            case 2:
                return new FriendsListFragment();
            default:
                return new ResearchFriendsListFragment();
        }
    };

    @Override
    public int getItemCount() {
        return 3;
    }
}
