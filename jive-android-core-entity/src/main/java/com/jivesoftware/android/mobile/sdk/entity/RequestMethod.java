package com.jivesoftware.android.mobile.sdk.entity;

public enum RequestMethod {
    GET ("GET"),
    POST ("POST"),
    PUT ("PUT"),
    DELETE ("DELETE");

    private final String name;

    private RequestMethod(String s) {
        name = s;
    }

    public String toString(){
        return name;
    }

}
