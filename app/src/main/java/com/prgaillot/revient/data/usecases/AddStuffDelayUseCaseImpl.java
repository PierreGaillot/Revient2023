package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.StuffRepository;
import com.prgaillot.revient.domain.usecases.AddStuffDelayUseCase;
import com.prgaillot.revient.utils.Callback;

public class AddStuffDelayUseCaseImpl implements AddStuffDelayUseCase {
    StuffRepository stuffRepository = StuffRepository.getInstance();

    @Override
    public void addStuffDelay(String stuffId, long delay, Callback<Void> callback) {
        stuffRepository.addDelay(stuffId, delay, callback);
    }
}
