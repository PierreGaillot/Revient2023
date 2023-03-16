package com.prgaillot.revient.ui.MainActivity;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.CreateUserUseCase;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.GetUserFriendsUseCase;
import com.prgaillot.revient.domain.usecases.UserIsRegisteredUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final CreateUserUseCase createUserUseCase = CreateUserUseCase.instance;
    private final GetUserFriendsUseCase getUserFriendsUseCase = GetUserFriendsUseCase.instance;

    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;

    private final UserIsRegisteredUseCase userIsRegisteredUseCase = UserIsRegisteredUseCase.instance;

    void createUser(User user){
        createUserUseCase.createUser(user);
    }

    void getUserFriends(List<String> friendsUid, Callback<List<User>> callback) {
        getUserFriendsUseCase.getUserFriends(friendsUid, callback);
    }

    void getCurrentUserData(Callback<User> callback){
        getCurrentUserDataUseCase.getCurrentUserData(callback);
    }

    void userIsRegistered(String userUid, Callback<Boolean> callback){
        userIsRegisteredUseCase.userIsRegistered(userUid, callback);
    }

}
