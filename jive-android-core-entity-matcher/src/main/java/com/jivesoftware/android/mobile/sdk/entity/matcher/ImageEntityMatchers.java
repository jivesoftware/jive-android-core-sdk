package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.ImageEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ImageEntityMatchers {
    @Nonnull
    public static Matcher<ImageEntity> imageSize(Matcher<Integer> sizeMatcher) {
        return new PropertyMatcher<Integer, ImageEntity>(sizeMatcher, "size") {
            @Nullable
            @Override
            protected Integer getPropertyValue(@Nonnull ImageEntity item) throws Exception {
                return item.size;
            }
        };
    }

    @Nonnull
    public static Matcher<ImageEntity> imageContentType(String contentType) {
        return new PropertyMatcher<String, ImageEntity>("contentType", contentType) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ImageEntity item) throws Exception {
                return item.contentType;
            }
        };
    }

    @Nonnull
    public static Matcher<ImageEntity> imageName(String name) {
        return new PropertyMatcher<String, ImageEntity>("name", name) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ImageEntity item) throws Exception {
                return item.name;
            }
        };
    }

    @Nonnull
    public static Matcher<ImageEntity> imageRef(Matcher<String> refMatcher) {
        return new PropertyMatcher<String, ImageEntity>(refMatcher, "ref") {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull ImageEntity item) throws Exception {
                return item.ref;
            }
        };
    }

    @Nonnull
    public static Matcher<ImageEntity> imageWidth(Integer width) {
        return new PropertyMatcher<Integer, ImageEntity>("width", width) {
            @Nullable
            @Override
            protected Integer getPropertyValue(@Nonnull ImageEntity item) throws Exception {
                return item.width;
            }
        };
    }

    @Nonnull
    public static Matcher<ImageEntity> imageHeight(Integer height) {
        return new PropertyMatcher<Integer, ImageEntity>("height", height) {
            @Nullable
            @Override
            protected Integer getPropertyValue(@Nonnull ImageEntity item) throws Exception {
                return item.height;
            }
        };
    }
}
