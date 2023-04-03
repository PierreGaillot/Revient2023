package com.prgaillot.revient.ui.HomeFragment;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.GetUserFriendsUseCase;
import com.prgaillot.revient.domain.usecases.GetUserStuffCollectionUseCase;
import com.prgaillot.revient.domain.usecases.UserIsRegisteredUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private final GetUserFriendsUseCase getUserFriendsUseCase = GetUserFriendsUseCase.instance;

    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;

    private final UserIsRegisteredUseCase userIsRegisteredUseCase = UserIsRegisteredUseCase.instance;

    private final GetUserStuffCollectionUseCase getUserStuffCollectionUseCase = GetUserStuffCollectionUseCase.instance;



    void getUserFriends(String userUid, Callback<List<User>> callback) {
        getUserFriendsUseCase.getUserFriends(userUid, callback);
    }

    void getCurrentUserData(Callback<User> callback){
        getCurrentUserDataUseCase.getCurrentUserData(callback);
    }

    void userIsRegistered(String userUid, Callback<Boolean> callback){
        userIsRegisteredUseCase.userIsRegistered(userUid, callback);
    }

    void getUserStuffCollection(String userId, Callback<List<Stuff>> callback){
        getUserStuffCollectionUseCase.getUserStuffCollection(userId, callback);
    }

}
