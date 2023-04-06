package com.prgaillot.revient.ui.StuffDetailsFragment;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.AddStuffDelayUseCase;
import com.prgaillot.revient.domain.usecases.DeleteStuffUseCase;
import com.prgaillot.revient.domain.usecases.GetUserByIdUserCase;
import com.prgaillot.revient.utils.Callback;

public class StuffDetailsViewModel extends ViewModel {
    private final GetUserByIdUserCase getUserByIdUserCase = GetUserByIdUserCase.instance;
    private  final AddStuffDelayUseCase addStuffDelayUseCase = AddStuffDelayUseCase.instance;

    private final DeleteStuffUseCase deleteStuffUseCase = DeleteStuffUseCase.instance;

    void getUser(String userId, Callback<User> callback){
        getUserByIdUserCase.getUserById(userId, callback);
    }

    void addBringDelay(String stuffId, long delay, Callback<Void> callback){
        addStuffDelayUseCase.addStuffDelay(stuffId, delay, callback);
    }

    public void deleteStuff(String stuffId, Callback<Void> callback) {
        deleteStuffUseCase.deleteStuff(stuffId, callback);
    }
}
