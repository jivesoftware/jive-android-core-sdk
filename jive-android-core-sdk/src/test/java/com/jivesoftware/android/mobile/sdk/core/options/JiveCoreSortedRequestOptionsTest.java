package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JiveCoreSortedRequestOptionsTest {
    private final JiveCoreSortedRequestOptions testObject = new JiveCoreSortedRequestOptions();

    @Test
    public void testOrderInitiallyNull() {
        JiveCoreSortedRequestOptions.Order actualOrder = testObject.getOrder();
        assertNull(actualOrder);
    }

    @Test
    public void testNullOrderMakesEmptyQueryParameters() {
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.emptyMap(), actualQueryParameters);
    }

    @Test
    public void testNonNullOrder() {
        testObject.setOrder(JiveCoreSortedRequestOptions.Order.DATE_CREATED_ASC);

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.singletonMap("sort", ImmutableList.of("dateCreatedAsc")), actualQueryParameters);
    }

    @Test
    public void testNonNullOrderAndStartIndexAndCountAndFields() {
        testObject.setFields(ImmutableList.of("foo", "bar", "baz"));
        testObject.setCount(55);
        testObject.setStartIndex(66);
        testObject.setOrder(JiveCoreSortedRequestOptions.Order.DATE_JOINED_ASC);

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        ImmutableMap<String, List<String>> expectedQueryParameters = ImmutableMap.<String, List<String>>of(
                "count", ImmutableList.of("55"),
                "fields", ImmutableList.of("foo,bar,baz"),
                "startIndex", ImmutableList.of("66"),
                "sort", ImmutableList.of("dateJoinedAsc"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }
}
