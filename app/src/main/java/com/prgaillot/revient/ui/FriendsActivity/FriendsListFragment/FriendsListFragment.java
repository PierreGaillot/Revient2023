package com.prgaillot.revient.ui.FriendsActivity.FriendsListFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.FriendsActivity.ResearchFriendsListFragment.AddFriendAdapterClickListener;
import com.prgaillot.revient.ui.FriendsActivity.ResearchFriendsListFragment.ResearchFriendsListAdapter;
import com.prgaillot.revient.ui.FriendsActivity.ResearchFriendsListFragment.UserAdapterClickListener;
import com.prgaillot.revient.ui.uiModels.UserWithStatus;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class FriendsListFragment extends Fragment {

    private static final String TAG = "FriendsListFragment";
    List<UserWithStatus> usersWithStatus;
    RecyclerView recyclerView;
    ResearchFriendsListAdapter adapter;
    FriendsListFragmentViewModel viewModel;

    public FriendsListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FriendsListFragmentViewModel.class);
    }

    private void initFriendsList() {
        usersWithStatus = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ResearchFriendsListAdapter(usersWithStatus, new UserAdapterClickListener() {
            @Override
            public void onUserClick(User user) {

            }
        }, new AddFriendAdapterClickListener() {
            @Override
            public void onAddFriendClick(User user) {

            }
        });
        recyclerView.setAdapter(adapter);
        refreshFriendsList(usersWithStatus);
    }

    private void refreshFriendsList(List<UserWithStatus> newUserWithStatus) {
//        adapter.update(newUserWithStatus);


        usersWithStatus = new ArrayList<>();
        usersWithStatus.addAll(newUserWithStatus);
        adapter.update(usersWithStatus);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        recyclerView = view.findViewById(R.id.friendListFragment_recyclerView);
        initFriendsList();

        viewModel.getCurrentUser(new Callback<User>() {
            @Override
            public void onCallback(User result) {
                viewModel.getFriendsWithStatus(result.getUid(), new Callback<List<UserWithStatus>>() {
                    @Override
                    public void onCallback(List<UserWithStatus> result) {
                        for (UserWithStatus userWithStatus : result) {
                            Log.d(TAG, userWithStatus.getUser().getDisplayName());

                        }
                        usersWithStatus =result;
                        refreshFriendsList(result);
                    }
                });
            }
        });
        return view;
    }
}