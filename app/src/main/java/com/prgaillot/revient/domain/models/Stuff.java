package com.prgaillot.revient.domain.models;

import javax.annotation.Nullable;

public class Stuff {

    String uid;
    String displayName;
    String ownerId;
    @Nullable
    String borrowerId;

    public Stuff() {
    }

    public Stuff(String ownerId, String displayName) {
        this.ownerId = ownerId;
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Nullable
    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(@Nullable String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
