package com.llf.universallibrary.photopicker;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */

public class Image implements Serializable{
    public String path;
    public String name;
    public long time;

    public boolean isCamera = false;

    public Image(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    public Image(){
        isCamera = true;
    }
}
