package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.data.repository.StuffRepository;
import com.prgaillot.revient.domain.usecases.DeleteStuffUseCase;
import com.prgaillot.revient.utils.Callback;

public class DeleteStuffUseCaseImpl implements DeleteStuffUseCase {
    StuffRepository stuffRepository = StuffRepository.getInstance();

    @Override
    public void deleteStuff(String stuffId, Callback<Void> callback) {
        stuffRepository.delete(stuffId, callback);
    }
}
