package com.jivesoftware.android.mobile.matcher;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public abstract class ArrayPropertyMatcher<P, T> extends PropertyMatcher<List<P>, T> {
    public ArrayPropertyMatcher(@Nonnull String propertyName, @Nullable List<P> expectedPropertyValue) {
        super(propertyName, expectedPropertyValue);
    }

    public ArrayPropertyMatcher(@Nonnull Matcher<? super List<P>> propertyValueMatcher, @Nonnull String propertyName) {
        super(propertyValueMatcher, propertyName);
    }

    @Nullable
    @Override
    protected List<P> getPropertyValue(@Nonnull T item) throws Exception {
        P[] arrayPropertyValue = getArrayPropertyValue(item);
        if (arrayPropertyValue == null) {
            return null;
        } else {
            return asList(arrayPropertyValue);
        }
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private List<P> asList(@Nonnull P[] arrayPropertyValue) {
        return Arrays.asList(arrayPropertyValue);
    }

    @Nullable
    protected abstract P[] getArrayPropertyValue(@Nonnull T item) throws Exception;
}
