package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JiveCoreJiveJsonCallableFactoryTest {
    @Mock
    private HttpClient mockHttpClient;
    @Mock
    private JiveJson mockJiveJson;
    @Mock
    private JiveCoreExceptionFactory mockJiveCoreExceptionFactory;
    @Mock
    private HttpRequestBase mockHttpRequestBase;

    @Test
    public void createEmptyCallableAddsAcceptGzipEncodingHeader() {
        JiveCoreJiveJsonCallableFactory testObject = new JiveCoreJiveJsonCallableFactory(mockHttpClient, mockJiveJson, mockJiveCoreExceptionFactory);
        testObject.createGsonCallable(mockHttpRequestBase, TokenEntity.class);

        verify(mockHttpRequestBase).setHeader("Accept-Encoding", "gzip");
    }
}
