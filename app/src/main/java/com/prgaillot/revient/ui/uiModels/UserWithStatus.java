package com.prgaillot.revient.ui.uiModels;

import com.prgaillot.revient.domain.models.User;

public class UserWithStatus {
    User user;
    int status;

    public UserWithStatus(User user, int status) {
        this.user = user;
        this.status = status;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
