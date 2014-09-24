package com.jivesoftware.android.mobile.sdk.util;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class JiveURLUtilsExtractAPIUrlAndEndpointTest {
    private String url;
    private String apiUrl;
    private String apiEndpoint;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return ImmutableList.<Object[]>builder()
                .add(new Object[] { null, null, null })
                .add(new Object[] { "", "", "" })
                .add(new Object[] { "/api/core/v3/acclaim/1150305777-1150305777/read", "/api/core/v3/acclaim/1150305777-1150305777/read", "/acclaim/1150305777-1150305777/read" })
                .add(new Object[] { "/jive/api/core/v3/acclaim/1150305777-1150305777/read", "/api/core/v3/acclaim/1150305777-1150305777/read", "/acclaim/1150305777-1150305777/read" })
                .add(new Object[] { "http://localhost:8080/api/core/v3/acclaim/1150305777-1150305777/read", "/api/core/v3/acclaim/1150305777-1150305777/read", "/acclaim/1150305777-1150305777/read" })
                .add(new Object[] { "http://localhost:8080/jive/api/core/v3/acclaim/1150305777-1150305777/read", "/api/core/v3/acclaim/1150305777-1150305777/read", "/acclaim/1150305777-1150305777/read" })
                .add(new Object[] { "http://localhost:8080/people/dmitry", "http://localhost:8080/people/dmitry", "http://localhost:8080/people/dmitry" })
                .add(new Object[] { "http://localhost:8080/people/dmitry?target=/api/core/v3/inbox", "http://localhost:8080/people/dmitry?target=/api/core/v3/inbox", "http://localhost:8080/people/dmitry?target=/api/core/v3/inbox" })
                .add(new Object[] { "http://localhost:8080/api/core/v3/inbox?count=1", "/api/core/v3/inbox?count=1", "/inbox" })
                .add(new Object[] { "http://localhost:8080/api/core/v3/inbox?fields=%40all&count=25&before=2013-08-14T22%3A43%3A46.070%2B0000", "/api/core/v3/inbox?fields=%40all&count=25&before=2013-08-14T22%3A43%3A46.070%2B0000", "/inbox" })
                .build();
    }

    public JiveURLUtilsExtractAPIUrlAndEndpointTest(String url, String apiUrl, String apiEndpoint) {
        this.url = url;
        this.apiUrl = apiUrl;
        this.apiEndpoint = apiEndpoint;
    }

    @Test
    public void testExtractAPIEndpoint() throws Exception {
        assertEquals("API endpoint should be extracted properly", apiEndpoint, JiveURLUtil.extractAPIEndpoint(url));
    }
}
