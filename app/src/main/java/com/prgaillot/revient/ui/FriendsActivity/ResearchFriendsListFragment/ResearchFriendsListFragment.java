package com.prgaillot.revient.ui.FriendsActivity.ResearchFriendsListFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.FriendsActivity.FriendsActivity;
import com.prgaillot.revient.ui.uiModels.UserWithStatus;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class ResearchFriendsListFragment extends Fragment {

    private static final String TAG = "ResearchFriendsListFrag";
    List<UserWithStatus> usersWithStatus;
    SearchView searchView;
    RecyclerView RFLRecyclerView;

    ResearchFriendsListAdapter RFLadapter;


    private ResearchFriendsListViewModel viewModel;

    public static ResearchFriendsListFragment newInstance() {
        return new ResearchFriendsListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ResearchFriendsListViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_research_friends_list, container, false);

        RFLRecyclerView = view.findViewById(R.id.researchFriendsList_recyclerview);
        searchView = view.findViewById(R.id.researchFriendsList_searchView);
        initResearchList();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 3){
                viewModel.researchUsers(newText, new Callback<List<User>>() {
                    @Override
                    public void onCallback(List<User> result) {

                        viewModel.getResearchUserWithStatus(result, new Callback<List<UserWithStatus>>() {
                            @Override
                            public void onCallback(List<UserWithStatus> result) {
                                usersWithStatus = result;
                                refreshResearchList(result);
                            }
                        });
                    }
                });
                }
                return false;
            }
        });
        return view;
    }

    private void initResearchList() {
        usersWithStatus = new ArrayList<>();
        RFLadapter = new ResearchFriendsListAdapter(usersWithStatus, new UserAdapterClickListener() {
            @Override
            public void onUserClick(User user) {
                ((FriendsActivity) getActivity()).openProfileFragment(user);
                Toast.makeText(getContext(), user.getDisplayName() + " click !", Toast.LENGTH_SHORT).show();
            }
        }, new AddFriendAdapterClickListener() {
            @Override
            public void onAddFriendClick(User user) {
                ((FriendsActivity)getActivity()).sendFriendRequest(user);
            }
        });

        RFLRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RFLRecyclerView.setAdapter(RFLadapter);

        refreshResearchList(usersWithStatus);
    }

    private void refreshResearchList(List<UserWithStatus> usersWithStatusList) {
        usersWithStatus = new ArrayList<>();
        usersWithStatus.addAll(usersWithStatusList);
        RFLadapter.update(usersWithStatus);
    }

}