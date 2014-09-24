package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.ContentBodyEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ContentBodyEntityMatchers {
    @Nonnull
    public static Matcher<ContentBodyEntity> contentBodyType(String type) {
        return new PropertyMatcher<String, ContentBodyEntity>("type", type) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ContentBodyEntity item) throws Exception {
                return item.type;
            }
        };
    }

    @Nonnull
    public static Matcher<ContentBodyEntity> contentBodyText(Matcher<String> textMatcher) {
        return new PropertyMatcher<String, ContentBodyEntity>(textMatcher, "text") {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ContentBodyEntity item) throws Exception {
                return item.text;
            }
        };
    }
}
