package com.prgaillot.revient.ui.ProfileFragment;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prgaillot.revient.domain.usecases.CheckIfIsFriendUseCase;
import com.prgaillot.revient.utils.Callback;

public class ProfileFragmentViewModel extends ViewModel {

    CheckIfIsFriendUseCase checkIfIsFriendUseCase = CheckIfIsFriendUseCase.instance;

    public void checkIsFriend(String userId, Callback<Boolean> callback) {
        checkIfIsFriendUseCase.checkIfUserIsFriend(userId, callback);
    }
}
