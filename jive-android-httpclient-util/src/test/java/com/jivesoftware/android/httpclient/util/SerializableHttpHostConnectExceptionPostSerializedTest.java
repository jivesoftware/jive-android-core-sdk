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

public class SerializableHttpHostConnectExceptionPostSerializedTest {
    private HttpHostConnectException httpHostConnectException;
    private SerializableHttpHostConnectException testObject;

    @Before
    public void setup() throws Exception {
        HttpHost httpHost = new HttpHost("https://example.com", 443, "https");
        ConnectException connectException = new ConnectException();
        httpHostConnectException = new HttpHostConnectException(httpHost, connectException);
        SerializableHttpHostConnectException serializableHttpHostConnectException = new SerializableHttpHostConnectException(httpHostConnectException);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(serializableHttpHostConnectException);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        testObject = (SerializableHttpHostConnectException) objectInputStream.readObject();
        assertNotNull(testObject);
    }

    @Test
    public void detailMessageEqualsHttpHostConnectExceptionDetailMessage() {
        String expectedDetailMessage = httpHostConnectException.getMessage();
        String actualDetailMessage = testObject.getMessage();

        assertEquals(expectedDetailMessage, actualDetailMessage);
    }

    @Test
    public void causeNotNull() {
        // cause is no longer equal to httpHostConnectException.getCause() because
        // ConnectException doesn't implement equals
        Throwable actualCause = testObject.getCause();

        assertNotNull(actualCause);
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
