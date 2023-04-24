package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.FriendRequestRepository;
import com.prgaillot.revient.domain.usecases.DeleteFriendRequestUseCase;
import com.prgaillot.revient.utils.Callback;

public class DeleteFriendRequestUseCaseImpl implements DeleteFriendRequestUseCase {

    FriendRequestRepository friendRequestRepository = FriendRequestRepository.getInstance();

    @Override
    public void deleteFriendRequest(String friendRequestId, Callback<Void> callback) {
        friendRequestRepository.deleteRequest(friendRequestId, callback);
    }
}
