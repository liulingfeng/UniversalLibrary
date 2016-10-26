package com.llf.universallibrary.photopicker;

import java.util.List;

/**
 * Created by llf on 2016/10/17.
 */

public class Folder {
    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

    public boolean isAll = false;

    public Folder(){

    }

    public Folder(boolean isAll){
        this.isAll = isAll;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
