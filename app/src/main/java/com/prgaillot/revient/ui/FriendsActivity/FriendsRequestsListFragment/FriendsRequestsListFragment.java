package com.prgaillot.revient.ui.FriendsActivity.FriendsRequestsListFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.uiModels.FriendRequestItemUiModel;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;


public class FriendsRequestsListFragment extends Fragment {

    FriendsRequestsListViewModel viewModel;
    RecyclerView recyclerView;
    FriendsRequestsListAdapter adapter;

    List<FriendRequestItemUiModel> friendRequestItemUiModelList;
    private final User currentUser = new User();

    public FriendsRequestsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FriendsRequestsListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_requests_list, container, false);
        recyclerView = view.findViewById(R.id.fragFriendsRequestList_recyclerView);
        initList();
        return view;
    }


    private void initList() {
        friendRequestItemUiModelList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new FriendsRequestsListAdapter(currentUser.getUid(), friendRequestItemUiModelList, new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String friendRequestId) {
                viewModel.deleteFriendRequest(friendRequestId, new Callback<Void>() {
                    @Override
                    public void onCallback(Void result) {
                        viewModel.refreshFriendsRequest(new Callback<Void>() {
                            @Override
                            public void onCallback(Void result) {
                                refreshList();
                                Toast.makeText(getContext(), "Friend request delete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }, new OnValidateClickListener() {
            @Override
            public void onValidateClick(FriendRequest friendRequest) {
                viewModel.validateFriendRequest(friendRequest, new Callback<Void>() {
                    @Override
                    public void onCallback(Void result) {
                        refreshList();
                        Toast.makeText(getContext(), "Friend request Validate", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        refreshList();
    }


    private void refreshList() {
        viewModel.refreshFriendsRequest(new Callback<Void>() {
            @Override
            public void onCallback(Void result) {
                viewModel.friendsRequests.observe(getViewLifecycleOwner(), new Observer<List<FriendRequest>>() {
                    @Override
                    public void onChanged(List<FriendRequest> friendRequestList) {
                        viewModel.friendsRequestToUi(friendRequestList, new Callback<List<FriendRequestItemUiModel>>() {
                            @Override
                            public void onCallback(List<FriendRequestItemUiModel> result) {
                                adapter.update(result);

                            }
                        });

                    }
                });
            }
        });

    }
}