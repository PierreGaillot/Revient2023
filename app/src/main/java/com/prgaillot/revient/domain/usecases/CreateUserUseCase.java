package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.CreateUserUseCaseImpl;
import com.prgaillot.revient.domain.models.User;

public interface CreateUserUseCase {

    CreateUserUseCase instance = new CreateUserUseCaseImpl();

    void createUser(User user);
}
