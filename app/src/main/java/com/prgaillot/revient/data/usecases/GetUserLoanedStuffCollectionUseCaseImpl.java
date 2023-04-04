package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.usecases.GetUserLoanedStuffCollectionUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class GetUserLoanedStuffCollectionUseCaseImpl implements GetUserLoanedStuffCollectionUseCase {

    UserRepository userRepository = UserRepository.getInstance();


    @Override
    public void getUserLoanedStuffCollection(String userId, Callback<List<Stuff>> callback) {
        userRepository.getUserLoanedCollection(userId, callback);
    }
}
