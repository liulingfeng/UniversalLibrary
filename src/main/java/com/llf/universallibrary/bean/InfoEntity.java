package com.llf.universallibrary.bean;

import java.io.Serializable;

/**
 * Created by llf on 2016/10/21.
 * 下载信息的事例实体
 */

public class InfoEntity implements Serializable{
    private DataBean data;

    private int error_code;
    private String error_msg;

    public InfoEntity(){}

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public static class DataBean {
        private String curVersion;
        private String appURL;
        private String description;
        private String minVersion;
        private String appName;

        public String getCurVersion() {
            return curVersion;
        }

        public void setCurVersion(String curVersion) {
            this.curVersion = curVersion;
        }

        public String getAppURL() {
            return appURL;
        }

        public void setAppURL(String appURL) {
            this.appURL = appURL;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMinVersion() {
            return minVersion;
        }

        public void setMinVersion(String minVersion) {
            this.minVersion = minVersion;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }
    }
}
