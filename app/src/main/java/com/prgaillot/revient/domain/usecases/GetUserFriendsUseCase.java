package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.GetUserFriendsUseCaseImpl;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public interface GetUserFriendsUseCase {

    GetUserFriendsUseCase instance = new GetUserFriendsUseCaseImpl();

    void getUserFriends(List<String> friendsUid, Callback<List<User>> callback);

}
