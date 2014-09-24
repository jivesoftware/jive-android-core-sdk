package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.PlaceEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlaceEntityMatchers {
    public static Matcher<PlaceEntity> placeName(String name) {
        return new PropertyMatcher<String, PlaceEntity>("name", name) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull PlaceEntity item) throws Exception {
                return item.name;
            }
        };
    }
}
