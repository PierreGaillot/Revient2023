package com.prgaillot.revient.ui.NewStuffFragment;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.CreateStuffUseCase;
import com.prgaillot.revient.domain.usecases.GetUserFriendsUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.HashMap;
import java.util.List;

public class NewStuffFragmentViewModel extends ViewModel {

    private final CreateStuffUseCase createStuffUseCase = CreateStuffUseCase.instance;
    private final GetUserFriendsUseCase getUserFriendsUseCase = GetUserFriendsUseCase.instance;

    public void createStuff(Stuff stuff, Callback<Void> success){
        createStuffUseCase.createStuff(stuff, success);
    }

    public void getUserFriend(String userId, Callback<List<User>> callback){
        getUserFriendsUseCase.getUserFriends(userId, callback);
    }

    public void prepareFriendsSearchList(List<User> friends, Callback<HashMap<String, String>> callback){
        HashMap<String, String> outputFriends = new HashMap<>();
        for (User friend: friends) {
            outputFriends.put(friend.getDisplayName(), friend.getUid());
            if(friends.size() == outputFriends.size()) callback.onCallback(outputFriends);
        }
    }

}
