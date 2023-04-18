package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.SendFriendRequestUseCaseImpl;
import com.prgaillot.revient.utils.Callback;

public interface SendFriendRequestUseCase {

    SendFriendRequestUseCase instance = new SendFriendRequestUseCaseImpl();

    void sendFriendRequest(String userSendRequest, String userReceivedRequest, Callback<Void> callback);
}
