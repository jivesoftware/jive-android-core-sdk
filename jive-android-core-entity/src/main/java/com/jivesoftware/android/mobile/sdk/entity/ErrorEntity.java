package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.Nullable;
import java.io.Serializable;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include = NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public interface ErrorEntity extends Serializable {
    public String getDescription();
    public Integer getErrorCode();
    @Nullable
    public String getAPIErrorCode();
}
