package com.prgaillot.revient.ui.uiModels;

import com.prgaillot.revient.domain.models.Stuff;

import java.io.Serializable;

public class StuffItemUiModel implements Serializable {
    Stuff stuff;
    String actionUrl;


    public StuffItemUiModel(Stuff stuff) {
        this.stuff = stuff;
    }

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
}
