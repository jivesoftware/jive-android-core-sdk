package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.ActivityEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ActivityEntityMatchers {
    @Nonnull
    public static Matcher<ActivityEntity> activityVerb(String verb) {
        return new PropertyMatcher<String, ActivityEntity>("verb", verb) {
            @Nullable
            @Override
            protected String getPropertyValue(ActivityEntity item) throws Exception {
                return item.verb;
            }
        };
    }

    @Nonnull
    public static Matcher<ActivityEntity> activityObjectUrl(String objectId) {
        return new PropertyMatcher<String, ActivityEntity>("object.id", objectId) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ActivityEntity item) throws Exception {
                return item.object.id;
            }
        };
    }

    @Nonnull
    public static Matcher<ActivityEntity> activityRead(boolean read) {
        return new PropertyMatcher<Boolean, ActivityEntity>("read", read) {
            @Nullable
            @Override
            protected Boolean getPropertyValue(@Nonnull ActivityEntity item) throws Exception {
                return item.jiveExtension.read;
            }
        };
    }
}
