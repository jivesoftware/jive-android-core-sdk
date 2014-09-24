package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JiveCoreSearchContentRequestOptionsTest {

    private final JiveCoreSearchContentRequestOptions testObject = new JiveCoreSearchContentRequestOptions();

    @Test
    public void testInitiallyNotCollapsedAndNullTypes() {
        assertFalse(testObject.isCollapsed());
        assertNull(testObject.getTypes());
    }

    @Test
    public void testNotCollapsedAndNullTypesMakesEmptyQueryParameters() {
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>emptyMap(), actualQueryParameters);
    }

    @Test
    public void testNotCollapsedAndEmptyTypesMakesEmptyQueryParameters() {
        testObject.setTypes(Collections.<String>emptyList());

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>emptyMap(), actualQueryParameters);
    }

    @Test
    public void testCollapsedAndNullTypes() {
        testObject.setCollapsed(true);

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("collapse", ImmutableList.of("true")), actualQueryParameters);
    }

    @Test
    public void testCollapsedAndEmptyTypes() {
        testObject.setCollapsed(true);
        testObject.setTypes(Collections.<String>emptyList());

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("collapse", ImmutableList.of("true")), actualQueryParameters);
    }

    @Test
    public void testNotCollapsedAndOneType() {
        testObject.setTypes(Collections.singletonList("foo"));

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("filter", ImmutableList.of("type(foo)")), actualQueryParameters);
    }

    @Test
    public void testNotCollapsedAndThreeTypes() {
        testObject.setTypes(ImmutableList.of("foo", "bar", "baz"));

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("filter", ImmutableList.of("type(foo,bar,baz)")), actualQueryParameters);
    }

    @Test
    public void testCollapsedAndOneType() {
        testObject.setCollapsed(true);
        testObject.setTypes(Collections.singletonList("foo"));

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        Map<String, List<String>> expectedQueryParameters = ImmutableMap.<String, List<String>>of(
                "filter", ImmutableList.of("type(foo)"),
                "collapse", ImmutableList.of("true"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }

    @Test
    public void testCollapsedAndThreeTypes() {
        testObject.setCollapsed(true);
        testObject.setTypes(ImmutableList.of("foo", "bar", "baz"));

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        Map<String, List<String>> expectedQueryParameters = ImmutableMap.<String, List<String>>of(
                "filter", ImmutableList.of("type(foo,bar,baz)"),
                "collapse", ImmutableList.of("true"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }

    @Test
    public void testCollpasedAndThreeTypesAndMixedEscapeSearchTermsAndOrderAndStartIndexAndCountAndFields() {
        testObject.setFields(ImmutableList.of("foo", "bar", "baz"));
        testObject.setCount(55);
        testObject.setStartIndex(66);
        testObject.setOrder(JiveCoreSortedRequestOptions.Order.DATE_JOINED_ASC);
        testObject.setSearchTerms(ImmutableList.of("foo", "ba(r)", "baz"));
        testObject.setCollapsed(true);
        testObject.setTypes(ImmutableList.of("foo", "bar", "baz"));

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();

        Map<String, List<String>> expectedQueryParameters = Maps.newHashMap();
        expectedQueryParameters.put("count", ImmutableList.of("55"));
        expectedQueryParameters.put("fields", ImmutableList.of("foo,bar,baz"));
        expectedQueryParameters.put("startIndex", ImmutableList.of("66"));
        expectedQueryParameters.put("sort", ImmutableList.of("dateJoinedAsc"));
        expectedQueryParameters.put("filter", ImmutableList.of("search(foo,ba\\(r\\),baz)","type(foo,bar,baz)"));
        expectedQueryParameters.put("collapse", ImmutableList.of("true"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }

}
