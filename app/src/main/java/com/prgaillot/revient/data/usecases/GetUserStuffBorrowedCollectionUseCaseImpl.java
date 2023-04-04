package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.usecases.GetUserStuffBorrowedCollectionUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class GetUserStuffBorrowedCollectionUseCaseImpl implements GetUserStuffBorrowedCollectionUseCase {

    UserRepository userRepository = UserRepository.getInstance();


    @Override
    public void getUserStuffBorrowedCollection(String userId, Callback<List<Stuff>> callback) {
        userRepository.getUserStuffBorrowedCollection(userId, callback);
    }
}
