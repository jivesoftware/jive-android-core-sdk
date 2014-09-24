package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.SessionGrantEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SessionGrantEntityMatchers {
    public static Matcher<SessionGrantEntity> sessionGrantStatus(@Nonnull Matcher<Integer> statusMatcher) {
        return new PropertyMatcher<Integer, SessionGrantEntity>(statusMatcher, "status") {
            @Nullable
            @Override
            protected Integer getPropertyValue(@Nonnull SessionGrantEntity item) throws Exception {
                return item.status;
            }
        };
    }
}
