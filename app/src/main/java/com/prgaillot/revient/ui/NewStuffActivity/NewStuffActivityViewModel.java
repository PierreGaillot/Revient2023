package com.prgaillot.revient.ui.NewStuffActivity;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.CreateStuffUseCase;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.GetUserFriendsUseCase;
import com.prgaillot.revient.utils.Callback;

import java.util.HashMap;
import java.util.List;

public class NewStuffActivityViewModel extends ViewModel {
    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;


    public void getCurrentUser(Callback<User> callback){
        getCurrentUserDataUseCase.getCurrentUserData(callback);
    }



}
