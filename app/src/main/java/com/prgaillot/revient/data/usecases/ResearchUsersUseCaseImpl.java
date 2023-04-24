package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.ResearchUsersUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class ResearchUsersUseCaseImpl implements ResearchUsersUseCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void researchUsersById(String newText, Callback<List<User>> callback) {
        userRepository.researchUsersByString(newText, callback);
    }
}
