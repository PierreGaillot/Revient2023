package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.ValidateFriendRequestUseCaseImpl;
import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.utils.Callback;

public interface ValidateFriendRequestUseCase {

    ValidateFriendRequestUseCase instance = new ValidateFriendRequestUseCaseImpl();

    void validateFriendRequest(FriendRequest friendRequest, Callback<Void> callback);

}
