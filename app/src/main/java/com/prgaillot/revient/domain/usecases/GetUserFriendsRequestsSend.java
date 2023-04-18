package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.GetUserFriendsRequestsImpl;
import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public interface GetUserFriendsRequestsSend {

    GetUserFriendsRequestsSend instance = new GetUserFriendsRequestsImpl();

    void getUserFriendsRequestsSend(String userId, Callback<List<FriendRequest>> callback);
}
