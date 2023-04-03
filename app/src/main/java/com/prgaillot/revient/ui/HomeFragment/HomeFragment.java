package com.prgaillot.revient.ui.HomeFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prgaillot.revient.R;
import com.prgaillot.revient.databinding.FragmentHomeBinding;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;

    private FriendsAdapter friendsAdapter;

    private CollectionAdapter collectionAdapter;
    private RecyclerView friendsRecyclerView, collectionRecyclerView;

    private HomeFragmentViewModel homeFragmentViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

            homeFragmentViewModel.getCurrentUserData(new Callback<User>() {
                @Override
                public void onCallback(User user) {
                    Log.d(TAG, user.toString());
                    homeFragmentViewModel.getUserFriends(user.getUid(), new Callback<List<User>>() {
                        @Override
                        public void onCallback(List<User> friendsList) {
                            friendsAdapter.update(friendsList);
                        }
                    });


                homeFragmentViewModel.getUserStuffCollection(user.getUid(), new Callback<List<Stuff>>() {
                    @Override
                    public void onCallback(List<Stuff> collection) {
                        Log.d(TAG, collection.toString());
                        collectionAdapter.update(collection);
                    }
                });
                }
            });



        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(FirebaseAuth.getInstance().getCurrentUser() != null ){

            initFriendsList();
            initCollectionList();
        }

    }

    private void initCollectionList() {
        collectionRecyclerView = binding.getRoot().findViewById(R.id.home_collection_recyclerView);
        collectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        collectionAdapter = new CollectionAdapter(new ArrayList<>());
        collectionRecyclerView.setAdapter(collectionAdapter);
    }

    private void initFriendsList() {
        friendsRecyclerView = binding.getRoot().findViewById(R.id.rv_friends);
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