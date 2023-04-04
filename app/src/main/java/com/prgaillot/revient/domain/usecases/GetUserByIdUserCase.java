package com.prgaillot.revient.domain.usecases;

import com.prgaillot.revient.data.usecases.GetUserByIdUseCaseImpl;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

public interface GetUserByIdUserCase {

    GetUserByIdUserCase instance = new GetUserByIdUseCaseImpl();

    void getUserById(String userId, Callback<User> callback);


}
