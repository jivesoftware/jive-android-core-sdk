package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.ListEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ListEntityMatchers {
    public static <E, L extends ListEntity<E>> Matcher<L> listEntities(Matcher<Iterable<? extends E>> entitiesMatcher) {
        return new PropertyMatcher<List<E>, L>(entitiesMatcher, "list") {
            @Nullable
            @Override
            protected List<E> getPropertyValue(@Nonnull L item) throws Exception {
                return item.list;
            }
        };
    }
}
