package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.DeleteFriendRequestUseCaseImpl;
import com.prgaillot.revient.utils.Callback;

public interface DeleteFriendRequestUseCase {

    DeleteFriendRequestUseCase instance = new DeleteFriendRequestUseCaseImpl();

    void deleteFriendRequest(String friendRequest, Callback<Void> callback);

}
