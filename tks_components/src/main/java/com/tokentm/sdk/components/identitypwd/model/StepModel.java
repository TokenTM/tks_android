package com.tokentm.sdk.components.identitypwd.model;

import android.support.annotation.DrawableRes;
/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 重置密码进度条
 */
public class StepModel {
    private int viewType;
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
