package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.BatchRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.EndpointRequestEntity;
import com.jivesoftware.android.mobile.sdk.entity.RequestMethod;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class BatchRequestMatchers {
    public static Matcher<BatchRequestEntity> keyIs(@Nullable String key) {
        return new PropertyMatcher<String, BatchRequestEntity>("key", key) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull BatchRequestEntity item) {
                return item.key;
            }
        };
    }

    private static abstract class BatchRequestEntityEndpointRequestEntityMatcher<P> extends PropertyMatcher<P, BatchRequestEntity> {
        protected BatchRequestEntityEndpointRequestEntityMatcher(@Nonnull String propertyName, @Nullable P expectedPropertyValue) {
            super(propertyName, expectedPropertyValue);
        }

        @Nullable
        @Override
        protected final P getPropertyValue(@Nonnull BatchRequestEntity item) {
            EndpointRequestEntity endpointRequestEntity = item.request;
            P endpointRequestEntityPropertyValue;
            if (endpointRequestEntity == null) {
                endpointRequestEntityPropertyValue = null;
            } else {
                endpointRequestEntityPropertyValue = getEndpointRequestEntityPropertyValue(endpointRequestEntity);
            }
            return endpointRequestEntityPropertyValue;
        }

        protected abstract P getEndpointRequestEntityPropertyValue(@Nonnull EndpointRequestEntity endpointRequestEntity);
    }

    public static Matcher<BatchRequestEntity> methodIs(@Nullable RequestMethod method) {
        return new BatchRequestEntityEndpointRequestEntityMatcher<RequestMethod>("BatchRequestEntity method", method) {
            @Nullable
            @Override
            protected RequestMethod getEndpointRequestEntityPropertyValue(@Nonnull EndpointRequestEntity endpointRequestEntity) {
                return endpointRequestEntity.method;
            }
        };
    }

    public static Matcher<BatchRequestEntity> endpointIs(@Nullable String endpoint) {
        return new BatchRequestEntityEndpointRequestEntityMatcher<String>("BatchRequestEntity endpoint", endpoint) {
            @Nullable
            @Override
            protected String getEndpointRequestEntityPropertyValue(@Nonnull EndpointRequestEntity endpointRequestEntity) {
                return endpointRequestEntity.endpoint;
            }
        };
    }

    public static Matcher<BatchRequestEntity> queryParamsAre(@Nullable Map<String, Object> params) {
        return new BatchRequestEntityEndpointRequestEntityMatcher<Map<String, Object>>("BatchRequestEntity query params", params) {
            @Nullable
            @Override
            protected Map<String, Object> getEndpointRequestEntityPropertyValue(@Nonnull EndpointRequestEntity endpointRequestEntity) {
                return endpointRequestEntity.queryParams;
            }
        };
    }

    public static Matcher<BatchRequestEntity> noQueryParams() {
        return queryParamsAre(null);
    }
}
