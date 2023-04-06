package com.prgaillot.revient.domain.usecases;

import android.telecom.Call;

import com.prgaillot.revient.data.usecases.DeleteStuffUseCaseImpl;
import com.prgaillot.revient.utils.Callback;

public interface DeleteStuffUseCase {

    DeleteStuffUseCase instance = new DeleteStuffUseCaseImpl();

    void deleteStuff(String stuffId, Callback<Void> callback);
}
