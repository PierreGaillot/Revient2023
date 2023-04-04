package com.prgaillot.revient.ui.HomeFragment;

import androidx.lifecycle.ViewModel;

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

    void getUserFriends(String userUid, Callback<List<User>> callback) {
        getUserFriendsUseCase.getUserFriends(userUid, callback);
    }

    void getCurrentUserData(Callback<User> callback) {
        getCurrentUserDataUseCase.getCurrentUserData(callback);
    }

    void userIsRegistered(String userUid, Callback<Boolean> callback) {
        userIsRegisteredUseCase.userIsRegistered(userUid, callback);
    }

    void getUserStuffCollection(String userId, Callback<List<Stuff>> callback) {
        getUserStuffCollectionUseCase.getUserStuffCollection(userId, callback);
    }

    void getStuffItemUiModelsCollection(List<Stuff> stuffs, List<User> friends, Callback<List<StuffItemUiModel>> callback) {
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

    void getStuffItemUiModelsBorrowedCollection(List<Stuff> stuffs, List<User> owners, Callback<List<StuffItemUiModel>> callback) {
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

    public void getUserStuffBorrowedCollection(String userId, Callback<List<Stuff>> callback) {
        getUserStuffBorrowedCollectionUseCase.getUserStuffBorrowedCollection(userId, callback);
    }

    public void getUserLoanedStuffUiModelCollection(String userId, List<User> owners, Callback<List<StuffItemUiModel>> callback) {
        getUserLoanedStuffCollectionUseCase.getUserLoanedStuffCollection(userId, new Callback<List<Stuff>>() {
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
}
