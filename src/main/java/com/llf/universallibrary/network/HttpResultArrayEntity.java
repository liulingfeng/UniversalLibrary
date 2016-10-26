package com.llf.universallibrary.network;

/**
 * Created by llf on 2016/10/24.
 */

public class HttpResultArrayEntity<T> {
    private int Code;
    private String Message;
    private T[] Body;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T[] getBody() {
        return Body;
    }

    public void setBody(T[] body) {
        Body = body;
    }
}
