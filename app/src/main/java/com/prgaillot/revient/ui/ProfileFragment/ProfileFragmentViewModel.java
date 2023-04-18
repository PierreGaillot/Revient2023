package com.prgaillot.revient.ui.ProfileFragment;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.CheckIfIsFriendUseCase;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.RemoveFriendUseCase;
import com.prgaillot.revient.domain.usecases.SendFriendRequestUseCase;
import com.prgaillot.revient.utils.Callback;

public class ProfileFragmentViewModel extends ViewModel {

    private final CheckIfIsFriendUseCase checkIfIsFriendUseCase = CheckIfIsFriendUseCase.instance;
    private final RemoveFriendUseCase removeFriendUseCase = RemoveFriendUseCase.instance;
    private final SendFriendRequestUseCase sendFriendRequestUseCase = SendFriendRequestUseCase.instance;
    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;

    public void checkIsFriend(String userId, Callback<Boolean> callback) {
        checkIfIsFriendUseCase.checkIfUserIsFriend(userId, callback);
    }

    public void removeFriend(String friendId, Callback<Void> callback) {
        removeFriendUseCase.removeFriend(friendId, callback);
    }

    public void sendFriendRequest(String uid, Callback<Void> callback) {

        getCurrentUserDataUseCase.getCurrentUserData(new Callback<User>() {
            @Override
            public void onCallback(User result) {

                sendFriendRequestUseCase.sendFriendRequest(result.getUid(), uid, callback);
            }
        });


    }
}
