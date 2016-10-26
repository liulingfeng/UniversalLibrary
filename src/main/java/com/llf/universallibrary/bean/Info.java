package com.llf.universallibrary.bean;

import java.io.Serializable;

/**
 * Created by llf on 2016/10/24.
 * 轮播bannel的实体类，只做事例，按实际情况来定
 */

public class Info implements Serializable{
    private String url;
    private String title;

    public Info(String title, String url) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
