package com.prgaillot.revient.ui.FriendsActivity.ResearchFriendsListFragment;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.CheckIfIsFriendUseCase;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.ResearchUsersUseCase;
import com.prgaillot.revient.ui.uiModels.UserWithStatus;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class ResearchFriendsListViewModel extends ViewModel {
    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;
    private final ResearchUsersUseCase researchUsersUseCase = ResearchUsersUseCase.instance;

    private final CheckIfIsFriendUseCase checkIfIsFriendUseCase = CheckIfIsFriendUseCase.instance;

    public void getCurrentUser(Callback<User> callback) {
        getCurrentUserDataUseCase.getCurrentUserData(callback);
    }

    public void researchUsers(String newText, Callback<List<User>> callback) {
        researchUsersUseCase.researchUsersById(newText, callback);
    }

    public void checkIfIsFriend(String userId, Callback<Boolean> callback) {
        checkIfIsFriendUseCase.checkIfUserIsFriend(userId, callback);
    }

    public void getResearchUserWithStatus(List<User> userList, Callback<List<UserWithStatus>> callback) {
        List<UserWithStatus> usersWithStatus = new ArrayList<>();

        getCurrentUser(new Callback<User>() {
            @Override
            public void onCallback(User result) {
                for (User user : userList) {
                    int status = 0;
                    UserWithStatus userFriendWithStatus = new UserWithStatus(user, status);
                    if ( result.getFriendsUid() != null && result.getFriendsUid().contains(user.getUid())) {
                        userFriendWithStatus.setStatus(2);
                    }
                    usersWithStatus.add(userFriendWithStatus);
                }
                callback.onCallback(usersWithStatus);
            }
        });

    }
}