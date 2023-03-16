package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.GetCurrentUserDataUseCaseImpl;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

public interface GetCurrentUserDataUseCase {

    GetCurrentUserDataUseCase instance = new GetCurrentUserDataUseCaseImpl();
    void getCurrentUserData(Callback<User> callback);
}
