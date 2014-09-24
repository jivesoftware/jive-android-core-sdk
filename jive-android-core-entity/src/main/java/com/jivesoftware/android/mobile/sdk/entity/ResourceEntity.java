package com.jivesoftware.android.mobile.sdk.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class ResourceEntity {
    @SerializedName("allowed")
    public Set<String> allowedMethods;
    public String ref;
}
