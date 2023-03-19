package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.GetUserStuffCollectionUseCaseImpl;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public interface GetUserStuffCollectionUseCase {

    GetUserStuffCollectionUseCase instance = new GetUserStuffCollectionUseCaseImpl();
    void getUserStuffCollection(String userId, Callback<List<Stuff>> stuffCollection);
}
