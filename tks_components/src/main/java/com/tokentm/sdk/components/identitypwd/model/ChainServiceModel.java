package com.tokentm.sdk.components.identitypwd.model;

import android.support.annotation.DrawableRes;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证顶部 链信服务顶部列表数据
 */
public class ChainServiceModel {

    private int selectIcon;
    private String title;
    private CertificationType certificationType;


    public ChainServiceModel() {
    }

    public ChainServiceModel(int selectIcon, String title, CertificationType certificationType) {
        this.selectIcon = selectIcon;
        this.title = title;
        this.certificationType = certificationType;
    }

    public int getSelectIcon() {
        return selectIcon;
    }

    public void setSelectIcon(@DrawableRes int selectIcon) {
        this.selectIcon = selectIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CertificationType getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(CertificationType certificationType) {
        this.certificationType = certificationType;
    }
}
