package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.domain.usecases.CheckIfIsFriendUseCase;
import com.prgaillot.revient.utils.Callback;

public class CheckIfIsFriendUseCaseImpl implements CheckIfIsFriendUseCase {
    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void checkIfUserIsFriend(String userId, Callback<Boolean> callback) {
        userRepository.checkIfUserIsFriend(userId, callback);
    }
}
