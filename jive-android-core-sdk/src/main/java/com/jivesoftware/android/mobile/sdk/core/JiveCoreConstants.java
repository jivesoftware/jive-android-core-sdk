package com.jivesoftware.android.mobile.sdk.core;

/**
 * Created by mark.schisler on 8/14/14.
 */
public class JiveCoreConstants {
    public static final String USER_AGENT = "jive-android-core";
    public static final String X_JIVE_CLIENT = "X-Jive-Client";
    public static final String CORE_API_V3_PREFIX = "/api/core/v3";
    // must be lowercase because HttpClient requires scheme names to be lowercased.
    public static final String JIVE_CORE_AUTH_SCHEME_NAME = "jivecore";

    public static final String COMMUNICATIONS_STREAM_SOURCE = "communications";
    public static final String CONNECTIONS_STREAM_SOURCE = "connections";
    public static final String EMAIL_WATCHES_STREAM_SOURCE = "watches";
    public static final String CUSTOM_STREAM_SOURCE = "custom";

    public static final String LATEST_LIKES_COLLECTION = "447369365-";
    public static final String SOCIAL_NEWS_COLLECTION = "-1864883264-";
    public static final String LATEST_ACCLAIM_COLLECTION = "1150305777-";
    public static final String WELCOME_TO_JIVE_COLLECTION = "-734134279-";
    public static final String PROFILE_UPDATES_COLLECTION = "2018307648-";
    public static final String PEOPLE_UPDATES_COLLECTION = "61669422-";
    public static final String PLACE_PAGES_COLLECTION = "-2106121635-";

    public static final Integer ANDROID_DEVICE_TYPE = 4;
    public static final String ANALYTICS_APP_ID = "Jive for Android SDK";
    public static final String ANALYTICS_APP_VERSION = "1.0";
    public static final String ANALYTICS_REQUEST_ORIGIN = "Android Native";
    public static final String X_JCAPI_TOKEN = "X-JCAPI-Token";
    public static final String X_JIVE_MOBILE_GATEWAY = "x-jive-mobile-gateway";

    public static final String OAUTH2_CREDENTIALS = "OXNtZ2dqZHQzanlkajl2bTA4ZGc4MnBwaGMwYWx2ZTAuaTpndm8zN3Awd3VyejZrMXZsY2c0M2J2dDZ1Zzg1cTF4Zy5z";
    public static final String OAUTH2_ADDON_UUID = "ede79b6c-50d1-4c57-adf1-4ee2c94234ce";

    public static final String COMMENT = "COMMENT";
    public static final String MESSAGE = "MESSAGE";
    public static final String DISCUSSION = "DISCUSSION";
    public static final String DOCUMENT = "DOCUMENT";

}
