package com.jivesoftware.android.mobile.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;

public class IterableMatchers {
    @Nonnull
    public static <E> Matcher<Iterable<? extends E>> containsAll(@Nonnull final Matcher<E> matcher) {
        return new TypeSafeMatcher<Iterable<? extends E>>() {
            @Override
            protected boolean matchesSafely(Iterable<? extends E> iterable) {
                boolean matchedOnce = false;
                for (E item : iterable) {
                    if (matcher.matches(item)) {
                        matchedOnce = true;
                    } else {
                        return false;
                    }
                }
                return matchedOnce;
            }

            @Override
            public void describeTo(Description description) {
                description.
                        appendText("iterable containing all ").
                        appendDescriptionOf(matcher);
            }
        };
    }
}
