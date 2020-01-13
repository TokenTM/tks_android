package com.tokentm.sdk.components.identitypwd.model;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证详情item
 */
public class ChainServiceItem {
    private String title;
    private String content;



    public ChainServiceItem() {
    }

    public ChainServiceItem(String title, String content) {
        this.title = title;
        this.content = content;
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
}
