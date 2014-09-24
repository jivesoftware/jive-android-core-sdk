package com.jivesoftware.android.mobile.sdk.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark.schisler on 8/14/14.
 */
public class TokenEntity {
    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("expires_in")
    public int expiresIn;
}
