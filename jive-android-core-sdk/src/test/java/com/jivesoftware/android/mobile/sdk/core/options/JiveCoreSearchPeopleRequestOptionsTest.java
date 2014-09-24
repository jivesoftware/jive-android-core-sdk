package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JiveCoreSearchPeopleRequestOptionsTest {
    private final JiveCoreSearchPeopleRequestOptions testObject = new JiveCoreSearchPeopleRequestOptions();

    @Test
    public void testInitiallyNotNameOnly() {
        assertFalse(testObject.isNameOnly());
    }

    @Test
    public void testNotNameOnlyMakesEmptyQueryParameters() {
        assertEquals(Collections.<String, List<String>>emptyMap(), testObject.copyQueryParameters());
    }

    @Test
    public void testNameOnly() {
        testObject.setNameOnly(true);

        assertEquals(Collections.<String, List<String>>singletonMap("filter", ImmutableList.of("nameonly")), testObject.copyQueryParameters());
    }

    @Test
    public void testNameOnlyAndMixedEscapeSearchTermsAndOrderAndStartIndexAndCountAndFields() {
        testObject.setFields(ImmutableList.of("foo", "bar", "baz"));
        testObject.setCount(55);
        testObject.setStartIndex(66);
        testObject.setOrder(JiveCoreSortedRequestOptions.Order.DATE_JOINED_ASC);
        testObject.setSearchTerms(ImmutableList.of("foo", "ba(r)", "baz"));
        testObject.setNameOnly(true);

        Map<String, List<String>> actualQueryParameters = testObject.copyQueryParameters();
        Map<String, List<String>> expectedQueryParameters = ImmutableMap.<String, List<String>>of(
                "count", ImmutableList.of("55"),
                "fields", ImmutableList.of("foo,bar,baz"),
                "startIndex", ImmutableList.of("66"),
                "sort", ImmutableList.of("dateJoinedAsc"),
                "filter", ImmutableList.of("search(foo,ba\\(r\\),baz)", "nameonly"));
        assertEquals(expectedQueryParameters, actualQueryParameters);
    }
}
