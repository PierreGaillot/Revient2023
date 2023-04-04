
package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.CreateUserUseCase;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void createUser(User user) {
        userRepository.createUser(user);
    }
}
