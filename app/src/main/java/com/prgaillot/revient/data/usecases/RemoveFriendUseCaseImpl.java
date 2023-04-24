package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.domain.usecases.RemoveFriendUseCase;
import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.utils.Callback;

public class RemoveFriendUseCaseImpl implements RemoveFriendUseCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void removeFriend(String friendId, Callback<Void> callback) {
        userRepository.removeFriend(friendId, callback);
    }
}
