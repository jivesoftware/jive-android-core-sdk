package com.jivesoftware.android.mobile.sdk.core;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JiveCoreRequestOptionsValuesTest {

    private JiveCoreRequestOptionsValues testObject;

    @Before
    public void setUp() throws Exception {
        testObject = new JiveCoreRequestOptionsValues();
    }

    @Test
    public void putFilter() throws Exception {
        testObject.putFilter("foo", Arrays.asList("bar", "bah"));
        String actual = queryParametersAsString();
        assertEquals("filter=foo(bar,bah);", actual);
    }

    @Test
    public void removeFilter() throws Exception {
        testObject.putFilter("foo", Arrays.asList("bar", "bah"));
        testObject.removeFilter("foo");
        String actual = queryParametersAsString();
        assertEquals("", actual);
    }

    @Test
    public void putDirective() throws Exception {
        testObject.putDirective("foo", Arrays.asList("bar", "bah"));
        String actual = queryParametersAsString();
        assertEquals("directive=foo(bar,bah);", actual);
    }

    @Test
    public void removeDirective() throws Exception {
        testObject.putDirective("foo", Arrays.asList("bar", "bah"));
        testObject.removeDirective("foo");
        String actual = queryParametersAsString();
        assertEquals("", actual);
    }

    @Test
    public void putQueryParameter() throws Exception {
        testObject.putQueryParameter("foo", Arrays.asList("bar", "bah"));
        String actual = queryParametersAsString();
        assertEquals("foo=bar,bah;", actual);
    }

    @Test
    public void removeQueryParameter() throws Exception {
        testObject.putQueryParameter("foo", Arrays.asList("bar", "bah"));
        testObject.removeQueryParameter("foo");
        String actual = queryParametersAsString();
        assertEquals("", actual);
    }


    private String queryParametersAsString() {
        StringBuilder builder = new StringBuilder();
        Map<String, List<String>> stringListMap = testObject.provideQueryParameters();
        for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(entry.getKey())
                    .append("=")
                    .append(iterableToString(entry.getValue()))
                    .append(";");
        }
        return builder.toString();
    }

    private String iterableToString(Iterable<String> iterable) {
        StringBuilder builder = new StringBuilder();
        for (String item : iterable) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(item);
        }
        return builder.toString();
    }

}