package com.prgaillot.revient.ui.uiModels;

import com.prgaillot.revient.domain.models.FriendRequest;

public class FriendRequestItemUiModel extends FriendRequest {

    String imgUrl;

    String userDisplayName;

    public FriendRequestItemUiModel(String userSendId,String userReceivedIdv, long requestTimestamp, String imgUrl, String userDisplayName) {
        super( userSendId , userReceivedIdv, requestTimestamp);
        this.imgUrl = imgUrl;
        this.userDisplayName = userDisplayName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }
}
