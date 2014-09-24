package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.AttachmentEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AttachmentEntityMatchers {
    @Nonnull
    public static Matcher<AttachmentEntity> attachmentName(String name) {
        return new PropertyMatcher<String, AttachmentEntity>("name", name) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull AttachmentEntity item) throws Exception {
                return item.name;
            }
        };
    }

    @Nonnull
    public static Matcher<AttachmentEntity> attachmentContentType(String contentType) {
        return new PropertyMatcher<String, AttachmentEntity>("contentType", contentType) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull AttachmentEntity item) throws Exception {
                return item.contentType;
            }
        };
    }

    @Nonnull
    public static Matcher<AttachmentEntity> attachmentSize(Matcher<Long> sizeMatcher) {
        return new PropertyMatcher<Long, AttachmentEntity>(sizeMatcher, "size") {
            @Nullable
            @Override
            protected Long getPropertyValue(@Nonnull AttachmentEntity item) throws Exception {
                return item.size;
            }
        };
    }

    @Nonnull
    public static Matcher<AttachmentEntity> attachmentUrl(Matcher<String> urlMatcher) {
        return new PropertyMatcher<String, AttachmentEntity>(urlMatcher, "url") {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull AttachmentEntity item) throws Exception {
                return item.url;
            }
        };
    }
}
