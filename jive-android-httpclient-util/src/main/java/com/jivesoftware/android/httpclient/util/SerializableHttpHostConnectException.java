package com.jivesoftware.android.httpclient.util;

import org.apache.http.HttpHost;
import org.apache.http.conn.HttpHostConnectException;

import java.io.IOException;
import java.net.ConnectException;

// https://code.google.com/p/android/issues/detail?id=55371
public class SerializableHttpHostConnectException extends HttpHostConnectException {

    private volatile HttpHost httpHost;

    public SerializableHttpHostConnectException(HttpHostConnectException httpHostConnectException) {
        // HttpHostConnectException's constructor guarantees that HttpHostConnectException's cause
        // will always be a ConnectException
        super(null, (ConnectException) httpHostConnectException.getCause());
        this.httpHost = httpHostConnectException.getHost();
    }

    @Override
    public String getMessage() {
        // must override this since HttpHostConnectException creates the message from the HttpHost
        // we pass to it, which is null.
        return "Connection to " + httpHost + " refused";
    }

    @Override
    public HttpHost getHost() {
        return httpHost;
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
        HttpHost httpHost = getHost();
        if (httpHost == null) {
            stream.writeBoolean(false);
        } else {
            stream.writeBoolean(true);
            stream.writeUTF(httpHost.getHostName());
            stream.writeInt(httpHost.getPort());
            stream.writeUTF(httpHost.getSchemeName());
        }
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        boolean hasHost = stream.readBoolean();
        if (hasHost) {
            String hostName = stream.readUTF();
            int port = stream.readInt();
            String schemeName = stream.readUTF();
            httpHost = new HttpHost(hostName, port, schemeName);
        }
    }
}
