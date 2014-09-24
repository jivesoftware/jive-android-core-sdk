package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JiveCoreSearchRequestOptionsTest {
    private final JiveCoreSearchRequestOptions testObject = new JiveCoreSearchRequestOptions();

    @Test
    public void copyFilters_withEscapes() {
        testObject.setSearchTerms(ImmutableList.of("f\\oo", "ba,r", "baz(", ")buzz"));
        List<String> actualFilters = testObject.copyFilters();
        assertEquals(ImmutableList.of("search(f\\\\oo,ba\\,r,baz\\(,\\)buzz)"), actualFilters);
    }

    @Test
    public void copyFilters_simple() {
        testObject.setSearchTerms(ImmutableList.of("blah", "what"));
        List<String> actualFilters = testObject.copyFilters();
        assertEquals(ImmutableList.of("search(blah,what)"), actualFilters);
    }

    @Test
    public void searchTerms_initially_null() {
        List<String> actualSearchTerms = testObject.getSearchTerms();
        assertNull(actualSearchTerms);
    }

    @Test
    public void null_searchTerms_makes_empty_queryParameters() {
        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>emptyMap(), actualQueryParameters);
    }

    @Test
    public void empty_searchTerms_makes_empty_queryParameters() {
        testObject.setSearchTerms(Collections.<String>emptyList());

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>emptyMap(), actualQueryParameters);
    }

    @Test
    public void one_searchTerm_without_escapes() {
        testObject.setSearchTerms(ImmutableList.of("foo"));

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("filter", ImmutableList.of("search(foo)")), actualQueryParameters);
    }

    @Test
    public void three_searchTerms_without_escapes() {
        testObject.setSearchTerms(ImmutableList.of("foo", "bar", "baz"));

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("filter", ImmutableList.of("search(foo,bar,baz)")), actualQueryParameters);
    }

    @Test
    public void three_searchTerms_with_escapes() {
        testObject.setSearchTerms(ImmutableList.of("f\\oo", "ba,r", "baz(", ")buzz"));

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        assertEquals(Collections.<String, List<String>>singletonMap("filter", ImmutableList.of("search(f\\\\oo,ba\\,r,baz\\(,\\)buzz)")), actualQueryParameters);
    }

    @Test
    public void mixed_escape_searchTerms_and_order_and_startIndex_and_count_and_fields() {
        testObject.setFields(ImmutableList.of("foo", "bar", "baz"));
        testObject.setCount(55);
        testObject.setStartIndex(66);
        testObject.setOrder(JiveCoreSortedRequestOptions.Order.DATE_JOINED_ASC);
        testObject.setSearchTerms(ImmutableList.of("foo", "ba(r)", "baz"));

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        Map<String, List<String>> expectedQueryParameters = ImmutableMap.<String, List<String>>of(
                "count", ImmutableList.of("55"),
                "fields", ImmutableList.of("foo,bar,baz"),
                "startIndex", ImmutableList.of("66"),
                "sort", ImmutableList.of("dateJoinedAsc"),
                "filter", ImmutableList.of("search(foo,ba\\(r\\),baz)"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }
}
