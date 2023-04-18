package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.domain.usecases.repository.UserRepository;
import com.prgaillot.revient.domain.usecases.UserIsRegisteredUseCase;
import com.prgaillot.revient.utils.Callback;

public class UserIsRegisteredUseCaseImpl implements UserIsRegisteredUseCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void userIsRegistered(String userUid, Callback<Boolean> callback) {
        userRepository.userIsRegistered(userUid, callback);
    }
}
