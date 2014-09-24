package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JiveCorePagedRequestOptionsTest {
    private final JiveCorePagedRequestOptions testObject = new JiveCorePagedRequestOptions();

    @Test
    public void testStartIndexInitiallyZero() {
        int actualStartIndex = testObject.getStartIndex();
        assertEquals(0, actualStartIndex);
    }

    @Test
    public void testZeroStartIndexMakesEmptyQueryParameters() {
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>emptyMap(), actualQueryParameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negative_startIndex_throws_IllegalArgumentException() {
        testObject.setStartIndex(-1);
    }

    @Test
    public void testPositiveStartIndex() {
        testObject.setStartIndex(66);

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("startIndex", ImmutableList.of("66")), actualQueryParameters);
    }

    @Test
    public void testPositiveStartIndexAndCountAndFields() {
        testObject.setFields(ImmutableList.of("foo", "bar", "baz"));
        testObject.setCount(55);
        testObject.setStartIndex(66);

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        ImmutableMap<String, List<String>> expectedQueryParameters = ImmutableMap.<String, List<String>>of(
                "count", ImmutableList.of("55"),
                "fields", ImmutableList.of("foo,bar,baz"),
                "startIndex", ImmutableList.of("66"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }
}
