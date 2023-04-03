package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.CreateStuffUseCaseImpl;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.utils.Callback;

public interface CreateStuffUseCase {

    CreateStuffUseCase instance = new CreateStuffUseCaseImpl();
    void createStuff(Stuff stuff, Callback<String> success);
}
