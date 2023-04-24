package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.domain.usecases.SendFriendRequestUseCase;
import com.prgaillot.revient.utils.Callback;

public class SendFriendRequestUseCaseImpl implements SendFriendRequestUseCase {
    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void sendFriendRequest(String userSendRequest, String userReceivedRequest, Callback<Void> callback) {
        userRepository.sendFriendRequest(userSendRequest, userReceivedRequest, callback);
    }
}
