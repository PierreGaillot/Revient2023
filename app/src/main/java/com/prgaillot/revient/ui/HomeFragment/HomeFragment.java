package com.prgaillot.revient.ui.HomeFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    List<User> friendsList;
    FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        fragmentManager = getParentFragmentManager();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            homeFragmentViewModel.getCurrentUserData(new Callback<User>() {
                @Override
                public void onCallback(User user) {
                    initHelloUser(user.getDisplayName());
                    initFriendsList();
                    homeFragmentViewModel.friendsList.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
                        @Override
                        public void onChanged(List<User> userList) {
                            initStuffCollection();
                            initUserStuffBorrowedCollection();
                            initUserLoanedStuffCollection();
                        }
                    });

                }
            });
        }


        return binding.getRoot();
    }

    private void initHelloUser(String userDisplayName) {
        TextView helloTextView = binding.homeFragHelloTextView;
        helloTextView.setText("Hello " +  userDisplayName + " !");
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void initFriendsList() {
        RecyclerView friendsRecyclerView = binding.getRoot().findViewById(R.id.rv_friends);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        friendsAdapter = new FriendsAdapter(new ArrayList<>(), new FriendAdapterClickListener() {
            @Override
            public void onFriendClick(User user) {
                ((MainActivity) getActivity()).openProfileFragment(user);
            }
        }, new AddFriendsAdapterClickListener() {
            @Override
            public void navToFriendsActivity() {
                ((MainActivity) getActivity()).navToFriendsActivity();
            }
        });
        friendsRecyclerView.setAdapter(friendsAdapter);
        refreshFriendsList();
    }

    private void refreshFriendsList() {
        if (friendsList == null) {
            homeFragmentViewModel.refreshFriendsList();
            homeFragmentViewModel.friendsList.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
                @Override
                public void onChanged(List<User> userList) {
                    friendsList = userList;
                    friendsAdapter.update(userList);
                }
            });
        } else {
            friendsAdapter.update(friendsList);
        }
    }

    public void initStuffCollection() {
        HorStuffListFragment horStuffListFragment = new HorStuffListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_userCollection_container_view, horStuffListFragment, null)
                .commit();
        refreshStuffCollection();
    }

    public void refreshStuffCollection() {
        homeFragmentViewModel.refreshUserStuffCollection();
        homeFragmentViewModel.userStuffCollection.observe(getViewLifecycleOwner(), new Observer<List<Stuff>>() {
            @Override
            public void onChanged(List<Stuff> stuffs) {
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
                                    .replace(R.id.fragment_userCollection_container_view, horStuffListFragment, null)
                                    .commit();
                        }
                    }
                });
            }
        });

    }

    public void initUserStuffBorrowedCollection() {
        HorStuffListFragment horStuffListFragment = new HorStuffListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_stuffBorrowed_container_view, horStuffListFragment, null)
                .commit();
        refreshUserStuffBorrowedCollection();
    }

    public void refreshUserStuffBorrowedCollection() {
        homeFragmentViewModel.refreshUserStuffBorrowedCollection();
        homeFragmentViewModel.userBorrowedStuffCollection.observe(getViewLifecycleOwner(), new Observer<List<Stuff>>() {
            @Override
            public void onChanged(List<Stuff> stuffs) {
                if (stuffs.isEmpty()) return;
                homeFragmentViewModel.getStuffItemUiModelsBorrowedCollection(stuffs, friendsList, new Callback<List<StuffItemUiModel>>() {
                    @Override
                    public void onCallback(List<StuffItemUiModel> stuffItemUiModels) {
                        if (stuffItemUiModels != null) {
                            Bundle collectionBundle = new Bundle();
                            collectionBundle.putSerializable("collection", (Serializable) stuffItemUiModels);
                            collectionBundle.putString("listName", "Borrowed Stuff");
                            HorStuffListFragment horStuffListFragment = new HorStuffListFragment();
                            horStuffListFragment.setArguments(collectionBundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_stuffBorrowed_container_view, horStuffListFragment, null)
                                    .commit();
                        }
                    }
                });
            }
        });
    }

    public void initUserLoanedStuffCollection() {
        HorStuffListFragment horStuffListFragment = new HorStuffListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentProfile_stuffBorrowed_container_view, horStuffListFragment, null)
                .commit();
        refreshUserLoanedStuffCollection();
    }

    public void refreshUserLoanedStuffCollection() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        homeFragmentViewModel.refreshUserLoanedStuffCollection();

        homeFragmentViewModel.userUserLoanedStuffCollection.observe(getViewLifecycleOwner(), new Observer<List<Stuff>>() {
            @Override
            public void onChanged(List<Stuff> stuffs) {
                homeFragmentViewModel.getUserLoanedStuffUiModelCollection(firebaseUser.getUid(), friendsList, new Callback<List<StuffItemUiModel>>() {
                    @Override
                    public void onCallback(List<StuffItemUiModel> stuffItemUiModels) {
                        if (stuffItemUiModels != null) {
                            Bundle collectionBundle = new Bundle();
                            collectionBundle.putSerializable("collection", (Serializable) stuffItemUiModels);
                            collectionBundle.putString("listName", "Loaned Stuff");
                            HorStuffListFragment horStuffListFragment = new HorStuffListFragment();
                            horStuffListFragment.setArguments(collectionBundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragmentProfile_stuffBorrowed_container_view, horStuffListFragment, null)
                                    .commit();
                        }
                    }
                });
            }
        });

    }


}