package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.httpclient.SerializableHeader;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
public class JiveCoreException extends IOException {
    /**
     * -1 if unknown
     */
    public final int statusCode;

    @Nonnull
    public final List<SerializableHeader> headers;


    public JiveCoreException(@Nullable String message, HttpResponse httpResponse) {
        this(message, null, httpResponse);
    }

    public JiveCoreException(@Nullable String message, @Nullable Throwable cause, HttpResponse httpResponse) {
        super(message, cause);
        this.statusCode = getStatusCode(httpResponse);
        this.headers = getSerializableHeaders(httpResponse);
    }

    protected JiveCoreException(@Nullable ErrorEntity errorEntity, HttpResponse httpResponse) {
        this(nullableErrorEntityToString(errorEntity), null, httpResponse);
    }

    protected JiveCoreException(@Nullable ErrorEntity errorEntity, @Nullable Throwable cause, HttpResponse httpResponse) {
        this(nullableErrorEntityToString(errorEntity), cause, httpResponse);
    }

    @Nonnull
    private static String nullableErrorEntityToString(@Nullable ErrorEntity errorEntity) {
        if (errorEntity == null) {
            return "no error entity";
        } else {
            String errorEntityString = new JiveJson().toJson(errorEntity);
            return errorEntityString;
        }
    }

    private static int getStatusCode(HttpResponse httpResponse) {
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine == null) {
            return -1;
        } else {
            int statusCode = statusLine.getStatusCode();
            return statusCode;
        }
    }

    @Nonnull
    private static List<SerializableHeader> getSerializableHeaders(HttpResponse httpResponse) {
        Header[] unserializableHeaders = httpResponse.getAllHeaders();
        if (unserializableHeaders == null) {
            return Collections.emptyList();
        } else {
            List<SerializableHeader> serializableHeaders = new ArrayList<SerializableHeader>(unserializableHeaders.length);
            for (Header unserializableHeader : unserializableHeaders) {
                SerializableHeader serializableHeader = new SerializableHeader(unserializableHeader);
                serializableHeaders.add(serializableHeader);
            }
            return serializableHeaders;
        }
    }

}
