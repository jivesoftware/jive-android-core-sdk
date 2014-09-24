package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JiveCoreDateLimitedRequestOptionsTest {
    private JiveCoreDateLimitedRequestOptions testObject;

    @Before
    public void setup() {
        testObject = new JiveCoreDateLimitedRequestOptions();
    }

    @Test
    public void testInitiallyNotCollapseWithNullDate() {
        testObject.setCount(1);

        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of("count", ImmutableList.of("1")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testCollapse() {
        testObject.setCount(1);
        testObject.setCollapse(true);
        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "count", ImmutableList.of("1"),
                        "collapse", ImmutableList.of("true")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testBeforeDate() {
        testObject.setCount(1);
        testObject.setDate(new Date(1000), false);
        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "count", ImmutableList.of("1"),
                        "before", ImmutableList.of("1970-01-01T00:00:01.000+0000")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testAfterDate() {
        testObject.setCount(1);
        testObject.setDate(new Date(2000), true);
        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "count", ImmutableList.of("1"),
                        "after", ImmutableList.of("1970-01-01T00:00:02.000+0000")),
                testObject.copyQueryParameters());
    }

    @Test
    public void testCollapseAndAfterDate() {
        testObject.setCount(1);
        testObject.setCollapse(true);
        testObject.setDate(new Date(3000), true);
        assertEquals((Map<String, ? extends List<String>>) ImmutableMap.of(
                        "count", ImmutableList.of("1"),
                        "collapse", ImmutableList.of("true"),
                        "after", ImmutableList.of("1970-01-01T00:00:03.000+0000")),
                testObject.copyQueryParameters());

    }
}
