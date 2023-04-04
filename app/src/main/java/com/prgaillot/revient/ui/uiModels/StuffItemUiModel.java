package com.prgaillot.revient.ui.uiModels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.prgaillot.revient.domain.models.Stuff;

import java.io.Serializable;


public class StuffItemUiModel implements Serializable, Parcelable {
    Stuff stuff;
    String actionUrl;


    public StuffItemUiModel(Stuff stuff) {
        this.stuff = stuff;
    }

    protected StuffItemUiModel(Parcel in) {
        actionUrl = in.readString();
    }

    public static final Creator<StuffItemUiModel> CREATOR = new Creator<StuffItemUiModel>() {
        @Override
        public StuffItemUiModel createFromParcel(Parcel in) {
            return new StuffItemUiModel(in);
        }

        @Override
        public StuffItemUiModel[] newArray(int size) {
            return new StuffItemUiModel[size];
        }
    };

    public Stuff getStuff() {
        return stuff;
    }

    public void setStuff(Stuff stuff) {
        this.stuff = stuff;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(actionUrl);
    }
}
