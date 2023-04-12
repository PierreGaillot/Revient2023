package com.prgaillot.revient.ui.FriendsActivity.FriendsListFragment;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.GetUserFriendsUseCase;
import com.prgaillot.revient.ui.uiModels.UserWithStatus;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class FriendsListFragmentViewModel extends ViewModel {
    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;
    private final GetUserFriendsUseCase getUserFriendsUseCase = GetUserFriendsUseCase.instance;

    public void getCurrentUser(Callback<User> callback) {
        getCurrentUserDataUseCase.getCurrentUserData(callback);
    }

    public void getFriends(String userId, Callback<List<User>> callback) {
        getUserFriendsUseCase.getUserFriends(userId,callback);
    }

    public void getFriendsWithStatus(String userId, Callback<List<UserWithStatus>> callback) {

        getFriends(userId, new Callback<List<User>>() {
            @Override
            public void onCallback(List<User> userList) {
                List<UserWithStatus> usersWithStatus = new ArrayList<>();
                for (User user : userList) {
                    int status = 2;
                    UserWithStatus userWithStatus = new UserWithStatus(user, status);
                    usersWithStatus.add(userWithStatus);
                }
                callback.onCallback(usersWithStatus);
            }
        });
    }
}
