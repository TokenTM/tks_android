package com.tokentm.sdk.components.identitypwd.model;

import android.support.annotation.DrawableRes;

public class StepModel {
    private int selectIcon;
    private int unSelectIcon;

    public StepModel(int selectIcon, int unSelectIcon) {
        this.selectIcon = selectIcon;
        this.unSelectIcon = unSelectIcon;
    }

    public int getSelectIcon() {
        return selectIcon;
    }

    public void setSelectIcon(@DrawableRes int selectIcon) {
        this.selectIcon = selectIcon;
    }

    public int getUnSelectIcon() {
        return unSelectIcon;
    }

    public void setUnSelectIcon(@DrawableRes int unSelectIcon) {
        this.unSelectIcon = unSelectIcon;
    }

}
