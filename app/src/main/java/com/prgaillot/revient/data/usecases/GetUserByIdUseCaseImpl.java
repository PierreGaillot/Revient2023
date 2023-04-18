package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.domain.usecases.repository.UserRepository;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.GetUserByIdUserCase;
import com.prgaillot.revient.utils.Callback;

public class GetUserByIdUseCaseImpl implements GetUserByIdUserCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void getUserById(String userId, Callback<User> callback) {
        userRepository.getUser(userId, callback);
    }
}
