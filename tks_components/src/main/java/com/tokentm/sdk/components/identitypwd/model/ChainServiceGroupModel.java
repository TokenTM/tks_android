package com.tokentm.sdk.components.identitypwd.model;

import java.util.List;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证页  认证详情  的分组
 */
public class ChainServiceGroupModel {
    private ChainServiceGroup group;
    private List<ChainServiceItem> groupList;

    public ChainServiceGroupModel() {
    }
    public ChainServiceGroupModel(ChainServiceGroup group, List<ChainServiceItem> groupList) {
        this.group = group;
        this.groupList = groupList;
    }

    public ChainServiceGroup getGroup() {
        return group;
    }

    public void setGroup(ChainServiceGroup group) {
        this.group = group;
    }

    public List<ChainServiceItem> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<ChainServiceItem> groupList) {
        this.groupList = groupList;
    }
}
