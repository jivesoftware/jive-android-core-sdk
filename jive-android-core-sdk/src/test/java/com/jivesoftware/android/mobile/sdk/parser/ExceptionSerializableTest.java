package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.FakeHttpServer;
import com.jivesoftware.android.mobile.sdk.entity.SimpleErrorEntity;
import com.jivesoftware.android.mobile.sdk.httpclient.SerializableHeader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ThrowableInstanceNeverThrown"})
public class ExceptionSerializableTest {
    private static final String CONTENT = "<html><body>Content!</body></html>";

    private DefaultHttpClient defaultHttpClient;
    private HttpResponse httpResponse;
    private HttpEntity httpEntity;

    @Before
    public void setup() throws Exception {
        defaultHttpClient = new DefaultHttpClient();

        FakeHttpServer fakeHttpServer = new FakeHttpServer();
        HttpGet httpGet = fakeHttpServer.startServerThreadAndCompleteNormallyWhenClientConnects(
                "HTTP/1.1 200 OK",
                new String[]{
                        "Content-Type: text/html;charset=UTF-8",
                        "Content-Encoding: identity",
                        "Content-Language: en-US",
                        "Connection: close",
                        "Content-Length: " + CONTENT.length(),
                },
                CONTENT);
        httpResponse = defaultHttpClient.execute(httpGet);
        fakeHttpServer.stopServerThread();
        assertNotNull(httpResponse);
        httpEntity = httpResponse.getEntity();
        assertNotNull(httpEntity);
    }

    @After
    public void tearDown() throws Exception {
        if (httpEntity != null) {
            httpEntity.consumeContent();
        }

        if (defaultHttpClient != null) {
            ClientConnectionManager connectionManager = defaultHttpClient.getConnectionManager();
            if (connectionManager != null) {
                connectionManager.shutdown();
            }
        }
    }

    @Test
    public void serializeJiveCoreAPIException() throws Exception {
        SimpleErrorEntity errorEntity = new SimpleErrorEntity("error", "description");
        JiveCoreAPIException testObject = serializeAndDeserialize(JiveCoreAPIException.class, new JiveCoreAPIException(httpResponse, errorEntity, "apiErrorCode"));

        assertEquals(errorEntity, testObject.errorEntity);
        assertEquals("apiErrorCode", testObject.apiErrorCode);
    }

    @Test
    public void serializeJiveCoreException() throws Exception {
        IOException causeIOException = new IOException();
        JiveCoreException testObject = serializeAndDeserialize(JiveCoreException.class, new JiveCoreException("message", causeIOException, httpResponse));

        assertEquals("message", testObject.getMessage());
        assertEquals(IOException.class, testObject.getCause().getClass());
        assertEquals(200, testObject.statusCode);
        assertEquals(
                Arrays.asList(
                        new SerializableHeader("Content-Type", "text/html;charset=UTF-8"),
                        new SerializableHeader("Content-Encoding", "identity"),
                        new SerializableHeader("Content-Language", "en-US"),
                        new SerializableHeader("Connection", "close"),
                        new SerializableHeader("Content-Length", "34")),
                testObject.headers);
    }

    @Test
    public void serializeJiveCoreHttpEntitiedException() throws Exception {
        JiveCoreHttpEntitiedException testObject = serializeAndDeserialize(JiveCoreHttpEntitiedException.class, new JiveCoreHttpEntitiedException(null, httpResponse, httpEntity));

        assertTrue(testObject.hasEntity);
        assertFalse(testObject.repeatable);
        assertFalse(testObject.chunked);
        assertEquals(CONTENT.length(), testObject.contentLength);
        assertEquals(new SerializableHeader("Content-Type", "text/html;charset=UTF-8"), testObject.contentTypeSerializableHeader);
        assertEquals(new SerializableHeader("Content-Encoding", "identity"), testObject.contentEncodingSerializableHeader);
        assertTrue(testObject.streaming);
    }

    @Test
    public void serializeJiveCoreInvalidJsonException() throws Exception {
        serializeAndDeserialize(JiveCoreInvalidJsonException.class, new JiveCoreInvalidJsonException(null, httpResponse));
    }

    @Test
    public void serializeJiveCoreLoginRequiredException() throws Exception {
        SimpleErrorEntity errorEntity = new SimpleErrorEntity("error", "description");
        JiveCoreLoginRequiredException testObject = serializeAndDeserialize(JiveCoreLoginRequiredException.class, new JiveCoreLoginRequiredException(httpResponse, errorEntity));

        assertEquals(errorEntity, testObject.errorEntity);
    }

    @Test
    public void serializeJiveCoreMobileGatewayException() throws Exception {
        serializeAndDeserialize(JiveCoreMobileGatewayException.class, new JiveCoreMobileGatewayException(httpResponse));
    }

    @Test
    public void serializeJiveCoreNullContentInputStreamException() throws Exception {
        serializeAndDeserialize(JiveCoreNullContentInputStreamException.class, new JiveCoreNullContentInputStreamException(httpResponse, httpEntity));
    }

    @Test
    public void serializeJiveCoreNullHttpEntityException() throws Exception {
        serializeAndDeserialize(JiveCoreNullHttpEntityException.class, new JiveCoreNullHttpEntityException(httpResponse));
    }

    @Test
    public void serializeJiveCoreNullHttpResponseException() throws Exception {
        serializeAndDeserialize(JiveCoreNullHttpResponseException.class, new JiveCoreNullHttpResponseException());
    }

    @Test
    public void serializeJiveCoreOAuthException() throws Exception {
        serializeAndDeserialize(JiveCoreOAuthException.class, new JiveCoreOAuthException(httpResponse, new SimpleErrorEntity("error", "description")));
    }

    @Test
    public void serializeJiveCoreOAuthInvalidClientException() throws Exception {
        serializeAndDeserialize(JiveCoreOAuthInvalidClientException.class, new JiveCoreOAuthInvalidClientException(httpResponse, new SimpleErrorEntity("error", "description")));
    }

    @Test
    public void serializeJiveCoreOAuthTemporarilyUnavailableException() throws Exception {
        serializeAndDeserialize(JiveCoreOAuthTemporarilyUnavailableException.class, new JiveCoreOAuthTemporarilyUnavailableException(httpResponse, new SimpleErrorEntity("error", "description")));
    }

    @Test
    public void serializeJiveCoreParsedException() throws Exception {
        SimpleErrorEntity errorEntity = new SimpleErrorEntity("error", "description");
        JiveCoreParsedException testObject = serializeAndDeserialize(JiveCoreParsedException.class, new JiveCoreParsedException(httpResponse, errorEntity));

        assertEquals(errorEntity, testObject.errorEntity);
    }

    @Test
    public void serializeJiveCoreServerException() throws Exception {
        serializeAndDeserialize(JiveCoreServerException.class, new JiveCoreServerException(httpResponse));
    }

    @Test
    public void serializeJiveCoreUnknownException() throws Exception {
        JiveCoreUnknownException testObject = serializeAndDeserialize(JiveCoreUnknownException.class, new JiveCoreUnknownException(httpResponse, httpEntity, new byte[] {42}));

        assertArrayEquals(new byte[] {42}, testObject.contentBodyBytes);
    }

    @Nonnull
    private static <S extends Serializable> S serializeAndDeserialize(@Nonnull Class<S> clazz, @Nonnull S serializable) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
        }

        S deserializedSerializable;
        {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            deserializedSerializable = clazz.cast(objectInputStream.readObject());
            objectInputStream.close();
        }

        assertNotNull(deserializedSerializable);
        return deserializedSerializable;
    }
}
