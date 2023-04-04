package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.UserIsRegisteredUseCaseImpl;
import com.prgaillot.revient.utils.Callback;

public interface UserIsRegisteredUseCase {

    UserIsRegisteredUseCase instance = new UserIsRegisteredUseCaseImpl();
    void userIsRegistered(String userUid, Callback<Boolean> callback);

}
