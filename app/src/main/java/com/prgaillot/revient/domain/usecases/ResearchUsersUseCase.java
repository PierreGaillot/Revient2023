package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.ResearchUsersUseCaseImpl;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

import java.util.List;

public interface ResearchUsersUseCase {

    ResearchUsersUseCase instance = new ResearchUsersUseCaseImpl();

    void researchUsersById(String newText, Callback<List<User>> callback);
}
