package com.jivesoftware.android.httpclient.util;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public final class JiveEntityUtil {
    private JiveEntityUtil() {
    }

    @Nonnull
    public static UrlEncodedFormEntity createForm(@Nonnull String name, @Nonnull String value) {
        return createForm(new BasicNameValuePair(name, value));
    }

    @Nonnull
    public static UrlEncodedFormEntity createForm(@Nonnull NameValuePair nameValuePair) {
        return createForm(Arrays.asList(nameValuePair));
    }

    @Nonnull
    public static UrlEncodedFormEntity createForm(@Nonnull List<? extends NameValuePair> parameters) {
        try {
            return new UrlEncodedFormEntity(parameters);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Nullable
    public static String getContentType(@Nullable HttpEntity httpEntity) {
        if (httpEntity == null) {
            return null;
        } else {
            Header contentTypeHeader = httpEntity.getContentType();
            if (contentTypeHeader == null) {
                return null;
            } else {
                String contentTypeHeaderValue = contentTypeHeader.getValue();
                return contentTypeHeaderValue;
            }
        }
    }

    @Nullable
    public static String getContentEncoding(@Nullable HttpEntity httpEntity) {
        if (httpEntity == null) {
            return null;
        } else {
            Header contentEncodingHeader = httpEntity.getContentEncoding();
            if (contentEncodingHeader == null) {
                return null;
            } else {
                String contentEncodingHeaderValue = contentEncodingHeader.getValue();
                return contentEncodingHeaderValue;
            }
        }
    }

    public static String toString(@Nonnull HttpEntity entity) throws IOException {
        return toString(entity, null);
    }

    /**
     * Modified org.apache.http.util.EntityUtils#toString(HttpEntity, String) to work with MultipartEntity
     */
    @Nonnull
    public static String toString(@Nonnull HttpEntity entity, String defaultCharset) throws IOException {
        // JIVE START
        // removed since some entities throw UnsupportedOperationExceptions
//        InputStream instream = entity.getContent();
//        if (instream == null) {
//            return "";
//        }
        // JIVE END
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int i = (int) entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        String charset = EntityUtils.getContentCharSet(entity);
        if (charset == null) {
            charset = defaultCharset;
        }
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }

        // JIVE START
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i);
        entity.writeTo(byteArrayOutputStream);

        InputStream instream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        // JIVE END

        Reader reader = new InputStreamReader(instream, charset);
        CharArrayBuffer buffer = new CharArrayBuffer(i);
        try {
            char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }

    @Nullable
    public static String getMultipartFormBoundary(@Nonnull HttpEntity httpEntity) {
        Header contentTypeHeader = httpEntity.getContentType();
        if (contentTypeHeader == null) {
            return null;
        }

        HeaderElement[] contentTypeHeaderElements = contentTypeHeader.getElements();
        if (contentTypeHeaderElements == null) {
            return null;
        }

        for (HeaderElement headerElement : contentTypeHeaderElements) {
            if (headerElement != null) {
                NameValuePair boundaryParameterNameValuePair = headerElement.getParameterByName("boundary");
                if (boundaryParameterNameValuePair != null) {
                    String boundary = boundaryParameterNameValuePair.getValue();
                    return boundary;
                }
            }
        }

        return null;
    }
}
