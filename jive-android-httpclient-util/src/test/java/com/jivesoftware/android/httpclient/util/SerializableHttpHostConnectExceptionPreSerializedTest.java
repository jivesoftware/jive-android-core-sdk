package com.jivesoftware.android.httpclient.util;

import org.apache.http.HttpHost;
import org.apache.http.conn.HttpHostConnectException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SerializableHttpHostConnectExceptionPreSerializedTest {
    private HttpHostConnectException httpHostConnectException;
    private SerializableHttpHostConnectException testObject;

    @Before
    public void setup() {
        HttpHost httpHost = new HttpHost("https://example.com", 443, "https");
        ConnectException connectException = new ConnectException();
        httpHostConnectException = new HttpHostConnectException(httpHost, connectException);
        testObject = new SerializableHttpHostConnectException(httpHostConnectException);
    }

    @Test
    public void detailMessageEqualsHttpHostConnectExceptionDetailMessage() {
        String expectedDetailMessage = httpHostConnectException.getMessage();
        String actualDetailMessage = testObject.getMessage();

        assertEquals(expectedDetailMessage, actualDetailMessage);
    }

    @Test
    public void causeEqualsHttpHostConnectExceptionCause() {
        Throwable expectedCause = httpHostConnectException.getCause();
        Throwable actualCause = testObject.getCause();

        assertEquals(expectedCause, actualCause);
    }

    @Test
    public void httpHostEqualsHttpHostConnectExceptionHost() {
        HttpHost expectedHttpHost = httpHostConnectException.getHost();
        HttpHost actualHttpHost = testObject.getHost();

        assertEquals(expectedHttpHost, actualHttpHost);
    }

    @Test
    public void serializable() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(testObject);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        SerializableHttpHostConnectException deserializedSerializableHttpHostConnectException = (SerializableHttpHostConnectException) objectInputStream.readObject();

        assertNotNull(deserializedSerializableHttpHostConnectException);
    }
}
