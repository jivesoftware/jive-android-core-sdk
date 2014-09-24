package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.MetadataObjectEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MetadataObjectEntityMatchers {
    public static Matcher<MetadataObjectEntity> metadataObjectName(@Nonnull String name) {
        return new PropertyMatcher<String, MetadataObjectEntity>("name", name) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull MetadataObjectEntity item) throws Exception {
                return item.name;
            }
        };
    }

    public static Matcher<MetadataObjectEntity> metadataObjectDescription(@Nonnull String description) {
        return new PropertyMatcher<String, MetadataObjectEntity>("description", description) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull MetadataObjectEntity item) throws Exception {
                return item.description;
            }
        };
    }

    public static Matcher<MetadataObjectEntity> metadataObjectFields(@Nonnull Matcher<Iterable<MetadataObjectEntity.MetadataFieldEntity>> metadataFieldsMatcher) {
        return new PropertyMatcher<List<MetadataObjectEntity.MetadataFieldEntity>, MetadataObjectEntity>(metadataFieldsMatcher, "fields") {
            @Nullable
            @Override
            protected List<MetadataObjectEntity.MetadataFieldEntity> getPropertyValue(@Nonnull MetadataObjectEntity item) throws Exception {
                return item.fields;
            }
        };
    }
}
