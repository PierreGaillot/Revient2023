package com.prgaillot.revient.domain.models;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class User implements Serializable {

    private String uid;
    private String displayName;
    private String imgUrl;
    private String email;

    @Nullable
    private List<String> friendsUid;

    public User(String uid, String displayName, String imgUrl, String email) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;

        if(imgUrl.equals("null")){
            this.imgUrl ="https://media.istockphoto.com/id/1327592506/vector/default-avatar-photo-placeholder-icon-grey-profile-picture-business-man.jpg?s=612x612&w=0&k=20&c=BpR0FVaEa5F24GIw7K8nMWiiGmbb8qmhfkpXcp1dhQg=";
        } else {
            this.imgUrl = imgUrl;
        }
    }

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImgUrl() {
//        if (imgUrl == null) {
//            return "https://media.istockphoto.com/id/1327592506/vector/default-avatar-photo-placeholder-icon-grey-profile-picture-business-man.jpg?s=612x612&w=0&k=20&c=BpR0FVaEa5F24GIw7K8nMWiiGmbb8qmhfkpXcp1dhQg=";
//        } else {
            return imgUrl;
//        }
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    public List<String> getFriendsUid() {
        if (friendsUid != null) {
            return friendsUid;
        } else {
            return new ArrayList<>();
        }
    }

    public void setFriendsUid(@Nullable List<String> friendsUid) {
        this.friendsUid = friendsUid;
    }

}
