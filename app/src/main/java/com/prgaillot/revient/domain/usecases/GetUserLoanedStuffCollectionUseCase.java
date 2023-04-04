package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.GetUserLoanedStuffCollectionUseCaseImpl;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public interface GetUserLoanedStuffCollectionUseCase {

    GetUserLoanedStuffCollectionUseCase instance = new GetUserLoanedStuffCollectionUseCaseImpl();

    void getUserLoanedStuffCollection(String userId, Callback<List<Stuff>> callback);
}
