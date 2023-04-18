package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.domain.usecases.repository.FriendRequestRepository;
import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.domain.usecases.GetUserFriendsRequestsSend;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class GetUserFriendsRequestsImpl implements GetUserFriendsRequestsSend {

    FriendRequestRepository friendRequestRepository = FriendRequestRepository.getInstance();

    @Override
    public void getUserFriendsRequestsSend(String userId, Callback<List<FriendRequest>> callback) {
        friendRequestRepository.getFriendsRequests(userId, callback);
    }
}
