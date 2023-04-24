package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.domain.usecases.ValidateFriendRequestUseCase;
import com.prgaillot.revient.utils.Callback;

public class ValidateFriendRequestUseCaseImpl implements ValidateFriendRequestUseCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void validateFriendRequest(FriendRequest friendRequest, Callback<Void> callback) {
        userRepository.validateFriendRequest(friendRequest, callback);
    }
}
