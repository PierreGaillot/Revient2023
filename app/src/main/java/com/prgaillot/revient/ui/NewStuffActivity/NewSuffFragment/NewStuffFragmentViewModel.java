package com.prgaillot.revient.ui.NewStuffActivity.NewSuffFragment;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.CreateStuffUseCase;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.GetUserFriendsUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.HashMap;
import java.util.List;

public class NewStuffFragmentViewModel extends ViewModel {
    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;
    private final GetUserFriendsUseCase getUserFriendsUseCase = GetUserFriendsUseCase.instance;

    private final CreateStuffUseCase createStuffUseCase = CreateStuffUseCase.instance;


    public void getCurrentUser(Callback<User> callback){
        getCurrentUserDataUseCase.getCurrentUserData(callback);
    }

    public void getUserFriend(String userId, Callback<List<User>> callback){
        getUserFriendsUseCase.getUserFriends(userId, callback);
    }

    public void prepareFriendsSearchList(List<User> friends, Callback<HashMap<String, String>> callback){
        HashMap<String, String> outputFriends = new HashMap<>();
        for (User friend: friends) {
            outputFriends.put( friend.getUid(), friend.getDisplayName());
            if(friends.size() == outputFriends.size()) callback.onCallback(outputFriends);
        }
    }

    public void createStuff(Stuff stuff, Callback<String> success){
        createStuffUseCase.createStuff(stuff, success);
    }

}