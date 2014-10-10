package com.jivesoftware.android.mobile.sdk.entity;

import java.io.Serializable;

public interface ErrorEntity extends Serializable {
    public String getDescription();
    public Integer getErrorCode();
    public String getAPIErrorCode();
}
