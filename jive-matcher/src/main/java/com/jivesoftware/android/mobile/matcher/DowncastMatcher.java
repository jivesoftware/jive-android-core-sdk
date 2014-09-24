package com.jivesoftware.android.mobile.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DowncastMatcher<T, U extends T> extends TypeSafeMatcher<T> {
    private final Class<U> downcastClass;
    private final Matcher<U> downcastedMatcher;

    public DowncastMatcher(Class<U> downcastClass, Matcher<U> downcastedMatcher) {
        super(downcastClass);
        this.downcastClass = downcastClass;
        this.downcastedMatcher = downcastedMatcher;
    }

    @Override
    protected boolean matchesSafely(T item) {
        boolean isInstance = downcastClass.isInstance(item);
        if (isInstance) {
            boolean matches = downcastedMatcher.matches(item);
            return matches;
        } else {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        downcastedMatcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        downcastedMatcher.describeMismatch(item, mismatchDescription);
    }
}
