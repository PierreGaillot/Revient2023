package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.RemoveFriendUseCaseImpl;
import com.prgaillot.revient.utils.Callback;

public interface RemoveFriendUseCase {

    RemoveFriendUseCase instance = new RemoveFriendUseCaseImpl();

    void removeFriend(String friendId, Callback<Void> callback);

}
