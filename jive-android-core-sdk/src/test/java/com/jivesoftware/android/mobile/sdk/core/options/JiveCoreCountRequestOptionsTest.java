package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JiveCoreCountRequestOptionsTest {
    private final JiveCoreCountRequestOptions testObject = new JiveCoreCountRequestOptions();

    @Test
    public void testCountInitiallyZero() {
        int actualCount = testObject.getCount();
        assertEquals(0, actualCount);
    }

    @Test
    public void testZeroCountMakesEmptyQueryParameters() {
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>emptyMap(), actualQueryParameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negative_count_throws_IllegalArgumentException() {
        testObject.setCount(-1);
    }

    @Test
    public void testPositiveCount() {
        testObject.setCount(55);

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("count", ImmutableList.of("55")), actualQueryParameters);
    }

    @Test
    public void testPositiveCountAndFields() {
        testObject.setFields(ImmutableList.of("foo", "bar", "baz"));
        testObject.setCount(55);

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        ImmutableMap<String, List<String>> expectedQueryParameters = ImmutableMap.<String, List<String>>of(
                "count", ImmutableList.of("55"),
                "fields", ImmutableList.of("foo,bar,baz"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }
}
