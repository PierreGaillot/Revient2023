package com.prgaillot.revient.data.usecases;

import com.prgaillot.revient.domain.usecases.repository.StuffRepository;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.usecases.CreateStuffUseCase;
import com.prgaillot.revient.utils.Callback;

public class CreateStuffUseCaseImpl implements CreateStuffUseCase {

    StuffRepository stuffRepository = StuffRepository.getInstance();

    @Override
    public void createStuff(Stuff stuff, Callback<String> success) {
        stuffRepository.createStuff(stuff, success);
    }
}
