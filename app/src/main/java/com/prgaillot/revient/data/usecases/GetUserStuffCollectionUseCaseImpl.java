package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.domain.usecases.repository.UserRepository;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.usecases.GetUserStuffCollectionUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class GetUserStuffCollectionUseCaseImpl implements GetUserStuffCollectionUseCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void getUserStuffCollection(String userId, Callback<List<Stuff>> callback) {
        userRepository.getUserStuffCollection(userId, callback);
    }
}
