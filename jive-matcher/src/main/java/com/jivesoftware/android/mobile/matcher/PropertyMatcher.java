package com.jivesoftware.android.mobile.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class PropertyMatcher<P, T> extends TypeSafeMatcher<T> {
    @Nonnull
    private final String propertyName;
    @Nonnull
    private final Matcher<? super P> propertyValueMatcher;

    protected PropertyMatcher(@Nonnull String propertyName, @Nullable final P expectedPropertyValue) {
        this.propertyName = propertyName;
        //TypeSafeMatcher won't accept nulls, so we have to use BaseMatcher
        this.propertyValueMatcher = new BaseMatcher<P>() {
            @Override
            public boolean matches(Object item) {
                boolean matches;
                if (expectedPropertyValue == null) {
                    matches = (item == null);
                } else {
                    matches = expectedPropertyValue.equals(item);
                }
                return matches;
            }

            @Override
            public void describeTo(Description description) {
                Object expectedPropertyDescriptionValue = getPropertyDescriptionValue(expectedPropertyValue);
                description.appendValue(expectedPropertyDescriptionValue);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {
                // we know the cast below is safe because we only call this method from  describeMismatchSafely,
                // and we always pass a P.
                Object actualPropertyDescriptionValue = getPropertyDescriptionValue((P)item);
                mismatchDescription.appendValue(actualPropertyDescriptionValue);
            }
        };
    }

    // reverse the parameters to prevent ambiguous method calls when the second parameter is null.
    protected PropertyMatcher(@Nonnull Matcher<? super P> propertyValueMatcher, @Nonnull String propertyName) {
        this.propertyValueMatcher = propertyValueMatcher;
        this.propertyName = propertyName;
    }

    @Override
    protected boolean matchesSafely(T item) {
        if (item == null) {
            throw new AssertionError("TypeSafeMatcher guarantees that item is non null");
        } else {
            boolean matches;
            try {
                P actualPropertyValue = getPropertyValue(item);
                matches = propertyValueMatcher.matches(actualPropertyValue);
            } catch (Exception e) {
                matches = false;
            }
            return matches;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.
                appendText(propertyName).
                appendText(" is ");
        propertyValueMatcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        if (item == null) {
            throw new AssertionError("TypeSafeMatcher guarantees that item is non null");
        } else {
            P actualPropertyValue;
            Exception exception;
            try {
                actualPropertyValue = getPropertyValue(item);
                exception = null;
            } catch (Exception e) {
                actualPropertyValue = null;
                exception = e;
            }

            mismatchDescription.appendText(propertyName);
            if (exception == null) {
                mismatchDescription.appendText(" was ");
                propertyValueMatcher.describeMismatch(actualPropertyValue, mismatchDescription);
            } else {
                StringWriter stringWriter = new StringWriter();
                exception.printStackTrace(new PrintWriter(stringWriter));
                String stackTrace = stringWriter.toString();
                mismatchDescription.
                        appendText(" couldn't be determined: ").
                        appendValue(stackTrace);
            }
        }
    }

    @Nullable
    protected abstract P getPropertyValue(@Nonnull T item) throws Exception;

    @Nullable
    protected Object getPropertyDescriptionValue(@Nullable P propertyValue) {
        return propertyValue;
    }
}
