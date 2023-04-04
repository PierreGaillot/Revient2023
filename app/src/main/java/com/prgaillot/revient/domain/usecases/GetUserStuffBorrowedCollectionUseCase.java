package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.GetUserStuffBorrowedCollectionUseCaseImpl;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public interface GetUserStuffBorrowedCollectionUseCase {

    GetUserStuffBorrowedCollectionUseCase instance = new GetUserStuffBorrowedCollectionUseCaseImpl();

    void getUserStuffBorrowedCollection(String userId, Callback<List<Stuff>> callback);

}
