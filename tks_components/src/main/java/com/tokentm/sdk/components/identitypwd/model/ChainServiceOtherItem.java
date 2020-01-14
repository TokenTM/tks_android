package com.tokentm.sdk.components.identitypwd.model;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证详情 别人查看item
 */
public class ChainServiceOtherItem {

    /**
     * 标题
     */
    private String title;
    /**
     * 信息展示正文   文本显示
     */
    private String content;
    /**
     * 显示那种布局类型
     */
    private int viewType;
    /**
     * 如果是上链/认证的话  显示状态
     */
    private boolean state;


    public ChainServiceOtherItem() {
    }

    public ChainServiceOtherItem(String title, String content, int viewType, boolean state) {
        this.title = title;
        this.content = content;
        this.viewType = viewType;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
