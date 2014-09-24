package com.jivesoftware.android.mobile.matcher;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@ParametersAreNonnullByDefault
public class MapMatchers {
    @Nonnull
    public static <K, V> Matcher<Map<? extends K, ? extends V>> hasEntry(@Nullable K key, Matcher<? super V> valueMatcher) {
        return Matchers.hasEntry(Matchers.equalTo(key), valueMatcher);
    }
    
    @Nonnull
    public static <K, V> Matcher<Map<? extends K, ? extends V>> hasEntry(Matcher<? super K> keyMatcher, @Nullable V value) {
        return Matchers.hasEntry(keyMatcher, Matchers.equalTo(value));
    }

    @Nonnull
    public static <K, V> Matcher<Map<? extends K, ? extends V>> keySet(Matcher<? super Set<? extends K>> keySetMatcher) {
        return new PropertyMatcher<Set<? extends K>, Map<? extends K, ? extends V>>(keySetMatcher, "keySet") {
            @Nullable
            @Override
            protected Set<? extends K> getPropertyValue(@Nonnull Map<? extends K, ? extends V> item) throws Exception {
                return item.keySet();
            }
        };
    }

    @Nonnull
    public static <K, V> Matcher<Map<? extends K, ? extends V>> values(Matcher<? super Iterable<? extends V>> valuesMatcher) {
        return new PropertyMatcher<Collection<? extends V>, Map<? extends K, ? extends V>>(valuesMatcher, "values") {
            @Nullable
            @Override
            protected Collection<? extends V> getPropertyValue(@Nonnull Map<? extends K, ? extends V> item) throws Exception {
                return item.values();
            }
        };
    }
}
