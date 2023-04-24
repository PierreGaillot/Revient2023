package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.UserRepository;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.GetUserFriendsUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public class GetUserFriendsUseCaseImpl implements GetUserFriendsUseCase {

    UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void getUserFriends(String userUid, Callback<List<User>> callback) {
        userRepository.getUserFriendById(userUid, callback);
    }
}
