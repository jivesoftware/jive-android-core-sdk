package com.jivesoftware.android.mobile.sdk.util;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class JiveURLUtilsIsAPIUrlTest {
    private String url;
    private boolean isAPIUrl;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return ImmutableList.<Object []>builder()
                .add(new Object[] { null, false })
                .add(new Object[] { "", false })
                .add(new Object[] { "/api/core/v3/acclaim/1150305777-1150305777/read", true })
                .add(new Object[] { "/jive/api/core/v3/acclaim/1150305777-1150305777/read", true })
                .add(new Object[] { "http://localhost:8080/api/core/v3/acclaim/1150305777-1150305777/read", true })
                .add(new Object[] { "http://localhost:8080/jive/api/core/v3/acclaim/1150305777-1150305777/read", true })
                .add(new Object[] { "http://localhost:8080/people/dmitry", false })
                .add(new Object[] { "/people/dmitry", false })
                .add(new Object[] { "http://localhost:8080/people/dmitry?target=/api/core/v3/inbox", false })
                .add(new Object[] { "http://localhost:8080/api/core/v3/inbox?count=1", true })
                .build();

    }

    public JiveURLUtilsIsAPIUrlTest(String url, boolean APIUrl) {
        this.url = url;
        isAPIUrl = APIUrl;
    }

    @Test
    public void testIsAPIUrl() throws Exception {
        assertEquals("Url type should be detected correctly", isAPIUrl, JiveURLUtil.isAPIUrl(url));
    }
}
