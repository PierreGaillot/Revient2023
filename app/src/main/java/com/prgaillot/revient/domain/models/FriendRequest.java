package com.prgaillot.revient.domain.models;

public class FriendRequest {
    String userSendId;
    String userReceivedId;
    long requestTimestamp;
    String uId;

    public FriendRequest(){}
    public FriendRequest(String userSendId, String userReceivedId, long requestTimestamp) {
        this.userSendId = userSendId;
        this.userReceivedId = userReceivedId;
        this.requestTimestamp = requestTimestamp;
    }

    public String getUserSendId() {
        return userSendId;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }



    public void setUserSendId(String userSendId) {
        this.userSendId = userSendId;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public String getId() {
        return uId;
    }

    public void setId(String uId) {
        this.uId = uId;
    }

    public String getUserReceivedId() {
        return userReceivedId;
    }

    public void setUserReceivedId(String userReceivedId) {
        this.userReceivedId = userReceivedId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}

