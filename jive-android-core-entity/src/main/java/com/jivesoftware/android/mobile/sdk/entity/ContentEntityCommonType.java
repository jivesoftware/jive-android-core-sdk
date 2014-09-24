package com.jivesoftware.android.mobile.sdk.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum ContentEntityCommonType {
    DIRECT_MESSAGE("dm"),
    DISCUSSION("discussion"),
    MESSAGE("message"),
    DOCUMENT("document"),
    EXTERNAL_STREAM_ACTIVITY("tilestreamentry"),
    FAVORITE("favorite"),
    FILE("file"),
    IDEA("idea"),
    POLL("poll"),
    POLL_OPTION_IMAGE("pollOptionImage"),
    POST("post"),
    SHARE("share"),
    SLIDE("slide"),
    STAGE("stage"),
    TASK("task"),
    UPDATE("update"),
    VIDEO("video");

    private static Map<String, ContentEntityCommonType> BY_TYPES;
    static {
        BY_TYPES = new HashMap<String, ContentEntityCommonType>();
        for (ContentEntityCommonType contentEntityCommonType : values()) {
            BY_TYPES.put(contentEntityCommonType.type, contentEntityCommonType);
        }
    }

    @Nullable
    public static ContentEntityCommonType fromType(@Nullable String type) {
        if (type == null) {
            return null;
        }
        ContentEntityCommonType contentEntityCommonType = BY_TYPES.get(type);
        return contentEntityCommonType;
    }

    @Nonnull
    public final String type;

    ContentEntityCommonType(@Nonnull String type) {
        this.type = type;
    }


}
