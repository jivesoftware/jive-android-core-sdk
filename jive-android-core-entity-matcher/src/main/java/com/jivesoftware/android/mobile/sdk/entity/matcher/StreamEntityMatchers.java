package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.StreamEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StreamEntityMatchers {
    @Nonnull
    public static Matcher<StreamEntity> streamName(String name) {
        return new PropertyMatcher<String, StreamEntity>("name", name) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull StreamEntity item) throws Exception {
                return item.name;
            }
        };
    }
}
