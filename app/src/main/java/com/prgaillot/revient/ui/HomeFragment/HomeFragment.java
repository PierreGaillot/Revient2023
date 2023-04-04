package com.prgaillot.revient.ui.HomeFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.prgaillot.revient.R;
import com.prgaillot.revient.databinding.FragmentHomeBinding;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.HomeFragment.HorStuffListFragment.HorStuffListFragment;
import com.prgaillot.revient.ui.MainActivity.MainActivity;
import com.prgaillot.revient.ui.uiModels.StuffItemUiModel;
import com.prgaillot.revient.utils.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;

    private FriendsAdapter friendsAdapter;

    private HomeFragmentViewModel homeFragmentViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        FragmentManager fragmentManager = getParentFragmentManager();

        homeFragmentViewModel.getCurrentUserData(new Callback<User>() {
            @Override
            public void onCallback(User user) {
                Log.d(TAG, user.toString());
                homeFragmentViewModel.getUserFriends(user.getUid(), new Callback<List<User>>() {
                    @Override
                    public void onCallback(List<User> friendsList) {
                        friendsAdapter.update(friendsList);
                        homeFragmentViewModel.getUserStuffCollection(user.getUid(), new Callback<List<Stuff>>() {
                            @Override
                            public void onCallback(List<Stuff> stuffs) {
                                homeFragmentViewModel.getStuffItemUiModelsCollection(stuffs, friendsList, new Callback<List<StuffItemUiModel>>() {
                                    @Override
                                    public void onCallback(List<StuffItemUiModel> stuffItemUiModels) {
                                        if (stuffItemUiModels != null) {
                                            Bundle collectionBundle = new Bundle();
                                            collectionBundle.putSerializable("collection", (Serializable) stuffItemUiModels);
                                            collectionBundle.putString("listName", "My Collection");
                                            HorStuffListFragment horStuffListFragment = new HorStuffListFragment();
                                            horStuffListFragment.setArguments(collectionBundle);
                                            fragmentManager.beginTransaction()
                                                    .add(R.id.fragment_userCollection_container_view, horStuffListFragment, null)
                                                    .commit();
                                        }
                                    }
                                });

                            }
                        });

                        homeFragmentViewModel.getUserStuffBorrowedCollection(user.getUid(), new Callback<List<Stuff>>() {
                            @Override
                            public void onCallback(List<Stuff> result) {
                                homeFragmentViewModel.getStuffItemUiModelsBorrowedCollection(result, friendsList, new Callback<List<StuffItemUiModel>>() {
                                    @Override
                                    public void onCallback(List<StuffItemUiModel> stuffItemUiModels) {
                                        if (stuffItemUiModels != null) {
                                            Bundle collectionBundle = new Bundle();
                                            collectionBundle.putSerializable("collection", (Serializable) stuffItemUiModels);
                                            collectionBundle.putString("listName", "Borrowed Stuff");
                                            HorStuffListFragment horStuffListFragment = new HorStuffListFragment();
                                            horStuffListFragment.setArguments(collectionBundle);
                                            fragmentManager.beginTransaction()
                                                    .add(R.id.fragment_stuffBorrowed_container_view, horStuffListFragment, null)
                                                    .commit();
                                        }
                                    }
                                });
                            }
                        });

                        homeFragmentViewModel.getUserLoanedStuffUiModelCollection(user.getUid(), friendsList, new Callback<List<StuffItemUiModel>>() {
                            @Override
                            public void onCallback(List<StuffItemUiModel> stuffItemUiModels) {
                                if (stuffItemUiModels != null) {
                                    Bundle collectionBundle = new Bundle();
                                    collectionBundle.putSerializable("collection", (Serializable) stuffItemUiModels);
                                    collectionBundle.putString("listName", "Loaned Stuff");
                                    HorStuffListFragment horStuffListFragment = new HorStuffListFragment();
                                    horStuffListFragment.setArguments(collectionBundle);
                                    fragmentManager.beginTransaction()
                                            .add(R.id.fragment_stuffLoaned_container_view, horStuffListFragment, null)
                                            .commit();
                                }
                            }
                        });
                    }
                });
            }
        });

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            initFriendsList();
        }

    }


    private void initFriendsList() {
        RecyclerView friendsRecyclerView = binding.getRoot().findViewById(R.id.rv_friends);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        friendsAdapter = new FriendsAdapter(new ArrayList<>());
        friendsRecyclerView.setAdapter(friendsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}