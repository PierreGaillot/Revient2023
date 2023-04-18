package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.domain.usecases.repository.UserRepository;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.utils.Callback;

public class GetCurrentUserDataUseCaseImpl implements GetCurrentUserDataUseCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void getCurrentUserData(Callback<User> callback) {
        userRepository.getCurrentUserData(callback);
    }
}
