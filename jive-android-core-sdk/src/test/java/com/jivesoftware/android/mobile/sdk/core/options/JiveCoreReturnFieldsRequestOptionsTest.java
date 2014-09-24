package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JiveCoreReturnFieldsRequestOptionsTest {
    private final JiveCoreReturnFieldsRequestOptions testObject = new JiveCoreReturnFieldsRequestOptions();

    @Test
    public void testInitiallyNullFields() {
        List<String> actualFields = testObject.getFields();
        assertNull(actualFields);
    }

    @Test
    public void testQueryParametersIsMutable() {
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        actualQueryParameters.put("foo", ImmutableList.of("bar")); // if the map is immutable, this will throw UnsupportedOperationException
    }

    @Test
    public void testNullFieldsMakesEmptyQueryParameters() {
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>emptyMap(), actualQueryParameters);
    }

    @Test
    public void testOneField() {
        testObject.setFields(Collections.singletonList("foo"));
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("fields", ImmutableList.of("foo")), actualQueryParameters);
    }

    @Test
    public void testMultipleFields() {
        testObject.setFields(ImmutableList.of("foo", "bar", "baz"));
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("fields", ImmutableList.of("foo,bar,baz")), actualQueryParameters);
    }
}
