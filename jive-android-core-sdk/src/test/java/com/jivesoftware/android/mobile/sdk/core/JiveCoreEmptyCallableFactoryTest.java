package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JiveCoreEmptyCallableFactoryTest {
    @Mock
    private HttpClient mockHttpClient;
    @Mock
    private JiveCoreExceptionFactory mockJiveCoreExceptionFactory;
    @Mock
    private HttpRequestBase mockHttpRequestBase;

    @Test
    public void createEmptyCallableAddsAcceptGzipEncodingHeader() {
        JiveCoreEmptyCallableFactory testObject = new JiveCoreEmptyCallableFactory(mockHttpClient, mockJiveCoreExceptionFactory);
        testObject.createEmptyCallable(mockHttpRequestBase);

        verify(mockHttpRequestBase).setHeader("Accept-Encoding", "gzip");
    }
}
