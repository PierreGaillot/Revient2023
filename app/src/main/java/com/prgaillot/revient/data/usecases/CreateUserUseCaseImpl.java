
package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.CreateUserUseCase;
import com.prgaillot.revient.utils.Callback;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void createUser(User user, Callback<Void> callback) {
        userRepository.createUser(user, callback);
    }
}
