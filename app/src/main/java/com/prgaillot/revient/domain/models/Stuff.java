package com.prgaillot.revient.domain.models;

import javax.annotation.Nullable;

public class Stuff {

    String uid;
    String displayName;
    String ownerId;
    @Nullable
    String borrowerId;
    @Nullable
    long creationTimeStamp, initialLoanDateTimestamp, initialLoanDurationTimestamp, additionalDelay;

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

    public String getImgUrl() {
        return "stuffImg/"+ uid + ".jpeg";
    }

    public long getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(long creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public long getInitialLoanDateTimestamp() {
        return initialLoanDateTimestamp;
    }

    public void setInitialLoanDateTimestamp(long initialLoanDateTimestamp) {
        this.initialLoanDateTimestamp = initialLoanDateTimestamp;
    }

    public long getInitialLoanDurationTimestamp() {
        return initialLoanDurationTimestamp;
    }

    public void setInitialLoanDurationTimestamp(long initialLoanDurationTimestamp) {
        this.initialLoanDurationTimestamp = initialLoanDurationTimestamp;
    }

    public long getBackTimeTimestamp() {
        if(borrowerId == null){
            return 0;
        } else {
           return  (initialLoanDateTimestamp + initialLoanDurationTimestamp + additionalDelay);
        }
    }

    public long getAdditionalDelay() {
        return additionalDelay;
    }

    public void setAdditionalDelay(long additionalDelay) {
        this.additionalDelay = this.additionalDelay + additionalDelay;
    }
}
