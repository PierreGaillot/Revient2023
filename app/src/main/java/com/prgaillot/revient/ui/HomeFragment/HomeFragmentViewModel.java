package com.prgaillot.revient.ui.HomeFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prgaillot.revient.data.usecases.GetUserStuffBorrowedCollectionUseCaseImpl;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.GetUserFriendsUseCase;
import com.prgaillot.revient.domain.usecases.GetUserLoanedStuffCollectionUseCase;
import com.prgaillot.revient.domain.usecases.GetUserStuffBorrowedCollectionUseCase;
import com.prgaillot.revient.domain.usecases.GetUserStuffCollectionUseCase;
import com.prgaillot.revient.domain.usecases.UserIsRegisteredUseCase;
import com.prgaillot.revient.ui.uiModels.StuffItemUiModel;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private final GetUserFriendsUseCase getUserFriendsUseCase = GetUserFriendsUseCase.instance;

    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;

    private final UserIsRegisteredUseCase userIsRegisteredUseCase = UserIsRegisteredUseCase.instance;

    private final GetUserStuffCollectionUseCase getUserStuffCollectionUseCase = GetUserStuffCollectionUseCase.instance;

    private final GetUserStuffBorrowedCollectionUseCase getUserStuffBorrowedCollectionUseCase = GetUserStuffBorrowedCollectionUseCase.instance;

    private final GetUserLoanedStuffCollectionUseCase getUserLoanedStuffCollectionUseCase = GetUserLoanedStuffCollectionUseCase.instance;

    // LIVEDATA
    private final MutableLiveData<List<Stuff>> _userStuffCollection = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Stuff>> _userBorrowedStuffCollection = new MutableLiveData<>(new ArrayList<>());
    private  final MutableLiveData<List<Stuff>> _userUserLoanedStuffCollection = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<User>> _friendsList = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Stuff>> userStuffCollection = _userStuffCollection;
    public LiveData<List<Stuff>> userBorrowedStuffCollection = _userBorrowedStuffCollection;
    public LiveData<List<Stuff>> userUserLoanedStuffCollection = _userUserLoanedStuffCollection;
    public LiveData<List<User>> friendsList = _friendsList;

    void getCurrentUserData(Callback<User> callback) {
        getCurrentUserDataUseCase.getCurrentUserData(callback);
    }


    void getUserFriends(String userUid, Callback<List<User>> callback) {
        getUserFriendsUseCase.getUserFriends(userUid, callback);
    }
    public void refreshFriendsList(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) return;

        getUserFriends(firebaseUser.getUid(), new Callback<List<User>>() {
            @Override
            public void onCallback(List<User> friends) {
                _friendsList.postValue(friends);
            }
        });
    }


    void getUserStuffCollection(String userId, Callback<List<Stuff>> callback) {
        getUserStuffCollectionUseCase.getUserStuffCollection(userId, callback);
    }
    public void refreshUserStuffCollection() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) return;

        getUserStuffCollection(firebaseUser.getUid(), new Callback<List<Stuff>>() {
            @Override
            public void onCallback(List<Stuff> result) {
                _userStuffCollection.postValue(result);
            }
        });
    }
    void getStuffItemUiModelsCollection(List<Stuff> stuffs, List<User> friends, Callback<List<StuffItemUiModel>> callback) {
        if (friends.isEmpty()) return;
        List<StuffItemUiModel> stuffItemUiModels = new ArrayList<>();
        for (Stuff stuff : stuffs) {
            StuffItemUiModel stuffItemUiModel = new StuffItemUiModel(stuff);
            if (stuff.getBorrowerId() != null) {
                stuffItemUiModel.setActionUrl(String.valueOf(friends.stream().filter(user -> user.getUid().equals(stuff.getBorrowerId())).findFirst().get().getImgUrl()));
            }
            stuffItemUiModels.add(stuffItemUiModel);
        }
        callback.onCallback(stuffItemUiModels);
    }

    public void getUserStuffBorrowedCollection(String userId, Callback<List<Stuff>> callback) {
        getUserStuffBorrowedCollectionUseCase.getUserStuffBorrowedCollection(userId, callback);
    }
    public void refreshUserStuffBorrowedCollection(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getUserStuffBorrowedCollection(firebaseUser.getUid(), new Callback<List<Stuff>>() {
            @Override
            public void onCallback(List<Stuff> result) {
                _userBorrowedStuffCollection.postValue(result);
            }
        });
    }
    void getStuffItemUiModelsBorrowedCollection(List<Stuff> stuffs, List<User> owners, Callback<List<StuffItemUiModel>> callback) {
        if (owners.isEmpty()) return;
        List<StuffItemUiModel> stuffItemUiModels = new ArrayList<>();
        for (Stuff stuff : stuffs) {
            StuffItemUiModel stuffItemUiModel = new StuffItemUiModel(stuff);
            if (stuff.getBorrowerId() != null) {
                stuffItemUiModel.setActionUrl(String.valueOf(owners.stream().filter(user -> user.getUid().equals(stuff.getOwnerId())).findFirst().get().getImgUrl()));
            } else {
                stuffItemUiModel.setActionUrl("https://eitrawmaterials.eu/wp-content/uploads/2016/09/person-icon.png");
            }
            stuffItemUiModels.add(stuffItemUiModel);
        }
        callback.onCallback(stuffItemUiModels);
    }



    public void getUserLoanedStuffCollection(String userId, Callback<List<Stuff>> callback){
        getUserLoanedStuffCollectionUseCase.getUserLoanedStuffCollection(userId, callback);
    }
    public void getUserLoanedStuffUiModelCollection(String userId, List<User> owners, Callback<List<StuffItemUiModel>> callback) {
        if(owners.isEmpty()) return;
        getUserLoanedStuffCollection(userId, new Callback<List<Stuff>>() {
            List<StuffItemUiModel> stuffItemUiModels = new ArrayList<>();

            @Override
            public void onCallback(List<Stuff> stuffs) {
                for (Stuff stuff : stuffs) {
                    StuffItemUiModel stuffItemUiModel = new StuffItemUiModel(stuff);
                    if (stuff.getOwnerId() != null) {
                        stuffItemUiModel.setActionUrl(String.valueOf(owners.stream().filter(user -> user.getUid().equals(stuff.getBorrowerId())).findFirst().get().getImgUrl()));
                    } else {
                        stuffItemUiModel.setActionUrl("https://eitrawmaterials.eu/wp-content/uploads/2016/09/person-icon.png");
                    }
                    stuffItemUiModels.add(stuffItemUiModel);
                }
                callback.onCallback(stuffItemUiModels);
            }
        });
    }

    public void refreshUserLoanedStuffCollection(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getUserLoanedStuffCollection(firebaseUser.getUid(), new Callback<List<Stuff>>() {
            @Override
            public void onCallback(List<Stuff> result) {
                _userUserLoanedStuffCollection.postValue(result);
            }
        });
    }


}
