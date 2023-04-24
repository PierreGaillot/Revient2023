package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.FriendRequestRepository;
import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.domain.usecases.GetUserFriendsRequest;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class GetUserFriendsRequestsImpl implements GetUserFriendsRequest {

    FriendRequestRepository friendRequestRepository = FriendRequestRepository.getInstance();

    @Override
    public void getUserFriendsRequest(String userId, Callback<List<FriendRequest>> callback) {
        friendRequestRepository.getFriendsRequests(userId, callback);
    }
}
