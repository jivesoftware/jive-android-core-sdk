package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ErrorEntityMatchers {
    @Nonnull
    public static <E extends ErrorEntity> Matcher<E> errorDescription(String description) {
        return new PropertyMatcher<String, E>("description", description) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull E item) throws Exception {
                return item.getDescription();
            }
        };
    }

    @Nonnull
    public static <E extends ErrorEntity> Matcher<E> errorErrorCode(Integer errorCode) {
        return new PropertyMatcher<Integer, E>("errorCode", errorCode) {
            @Nullable
            @Override
            protected Integer getPropertyValue(@Nonnull E item) throws Exception {
                return item.getErrorCode();
            }
        };
    }

    @Nonnull
    public static <E extends ErrorEntity> Matcher<E> errorAPIErrorCode(String apiErrorCode) {
        return new PropertyMatcher<String, E>("apiErrorCode", apiErrorCode) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull E item) throws Exception {
                return item.getAPIErrorCode();
            }
        };
    }
}
