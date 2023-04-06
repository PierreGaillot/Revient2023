package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.AddStuffDelayUseCaseImpl;
import com.prgaillot.revient.utils.Callback;

public interface AddStuffDelayUseCase {

    AddStuffDelayUseCase instance = new AddStuffDelayUseCaseImpl();

    void addStuffDelay(String stuffId, long delay, Callback<Void> callback);
}
