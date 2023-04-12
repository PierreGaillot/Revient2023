package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.CheckIfIsFriendUseCaseImpl;
import com.prgaillot.revient.utils.Callback;

public interface CheckIfIsFriendUseCase {

    CheckIfIsFriendUseCase instance = new CheckIfIsFriendUseCaseImpl();

    void checkIfUserIsFriend(String userId, Callback<Boolean> callback);

}
