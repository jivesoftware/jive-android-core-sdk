package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.ResourceEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.URL;

@ParametersAreNonnullByDefault
public class ResourceEntityMatchers {
    @Nonnull
    public static Matcher<ResourceEntity> resourceRef(String ref) {
        return new PropertyMatcher<String, ResourceEntity>("ref", ref) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ResourceEntity item) throws Exception {
                return item.ref;
            }
        };
    }

    @Nonnull
    public static Matcher<ResourceEntity> resourceRef(URL url) {
        return new PropertyMatcher<String, ResourceEntity>("ref", url.toExternalForm()) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ResourceEntity item) throws Exception {
                return item.ref;
            }
        };
    }
}
