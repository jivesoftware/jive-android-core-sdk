package com.jivesoftware.android.mobile.sdk.util;

import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreCountRequestOptions;
import org.junit.Test;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JiveURIUtilTest {
    @Test
    public void testCreateURIWithOptions() throws Exception {
        JiveCoreCountRequestOptions options = new JiveCoreCountRequestOptions();
        options.setCount(2);
        options.setFields(Arrays.asList("foo", "bar"));

        URI actual = JiveURIUtil.createURI(new URL("http://jivesoftware.com"), "/path", options);
        assertEquals(new URI("http://jivesoftware.com/path?fields=foo%2Cbar&count=2"), actual);
    }

    @Test
    public void testWhenUrlHasNoQueryParamsButIsFullyFormedThenQuestionMarkIsNotAppended() throws Exception {
        JiveCoreCountRequestOptions options = new JiveCoreCountRequestOptions();
        String url = "http://jivesoftware.com/?q=whatever";
        URI actual = JiveURIUtil.createURI(new URL(url), "", options);
        assertEquals(new URI(url), actual);
    }
}
