package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.JiveObjectEntity;
import com.jivesoftware.android.mobile.sdk.entity.ResourceEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.URL;
import java.util.Map;

import static com.jivesoftware.android.mobile.matcher.MapMatchers.hasEntry;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ResourceEntityMatchers.resourceRef;

@ParametersAreNonnullByDefault
public class JiveObjectEntityMatchers {
    @Nonnull
    public static <E extends JiveObjectEntity> Matcher<E> objectResources(Matcher<? super Map<? extends String, ? extends ResourceEntity>> resourcesMatcher) {
        return new PropertyMatcher<Map<String, ResourceEntity>, E>(resourcesMatcher, "resources") {
            @Nullable
            @Override
            protected Map<String, ResourceEntity> getPropertyValue(@Nonnull E item) throws Exception {
                return item.resources;
            }
        };
    }

    @Nonnull
    public static <E extends JiveObjectEntity> Matcher<E> objectType(String type) {
        return new PropertyMatcher<String, E>("type", type) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull E item) throws Exception {
                return item.type;
            }
        };
    }

    @Nonnull
    public static <E extends JiveObjectEntity> Matcher<E> objectSelfURL(URL selfURL) {
        return objectResources(hasEntry("self", resourceRef(selfURL)));
    }

    @Nonnull
    public static <E extends JiveObjectEntity> Matcher<E> objectSelfURL(String selfURL) {
        return objectResources(hasEntry("self", resourceRef(selfURL)));
    }
}
