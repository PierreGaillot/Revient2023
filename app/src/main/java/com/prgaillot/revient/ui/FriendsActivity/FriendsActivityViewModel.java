package com.prgaillot.revient.ui.FriendsActivity;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.SendFriendRequestUseCase;
import com.prgaillot.revient.utils.Callback;

public class FriendsActivityViewModel extends ViewModel {

    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;
 private final SendFriendRequestUseCase sendFriendRequestUseCase = SendFriendRequestUseCase.instance;

    public void getCurrentUser(Callback<User> callback) {
        getCurrentUserDataUseCase.getCurrentUserData(callback);
    }

    public void sendFriendRequest(String userSendRequest, String userReceivedRequest,Callback<Void> callback) {
        sendFriendRequestUseCase.sendFriendRequest(userSendRequest, userReceivedRequest, callback);
    }
}
