package com.prgaillot.revient.ui.StuffDetailsFragment;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.GetUserByIdUserCase;
import com.prgaillot.revient.utils.Callback;

public class StuffDetailsViewModel extends ViewModel {
    private final GetUserByIdUserCase getUserByIdUserCase = GetUserByIdUserCase.instance;

    void getUser(String userId, Callback<User> callback){
        getUserByIdUserCase.getUserById(userId, callback);
    }
}
