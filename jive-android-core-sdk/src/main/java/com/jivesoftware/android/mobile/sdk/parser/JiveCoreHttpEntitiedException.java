package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.httpclient.SerializableHeader;
import com.jivesoftware.android.mobile.sdk.util.HttpEntityUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreHttpEntitiedException extends JiveCoreException {
    public final boolean hasEntity;
    public final boolean repeatable;
    public final boolean chunked;
    public final long contentLength;
    @Nullable
    public final SerializableHeader contentTypeSerializableHeader;
    @Nullable
    public final SerializableHeader contentEncodingSerializableHeader;
    public final boolean streaming;

    public JiveCoreHttpEntitiedException(@Nullable String message, HttpResponse httpResponse, @Nullable HttpEntity httpEntity) {
        this(message, null, httpResponse, httpEntity);
    }

    public JiveCoreHttpEntitiedException(@Nullable String message, @Nullable Throwable cause, HttpResponse httpResponse, @Nullable HttpEntity httpEntity) {
        super((String)null, httpResponse);
        if (httpEntity == null) {
            hasEntity = false;
            this.repeatable = false;
            this.chunked = false;
            this.contentLength = -1;
            this.contentTypeSerializableHeader = null;
            this.contentEncodingSerializableHeader = null;
            this.streaming = false;
        } else {
            hasEntity = true;
            this.repeatable = httpEntity.isRepeatable();
            this.chunked = httpEntity.isChunked();
            this.contentLength = httpEntity.getContentLength();
            this.contentTypeSerializableHeader = HttpEntityUtil.getContentTypeSerializableHeader(httpEntity);
            this.contentEncodingSerializableHeader = HttpEntityUtil.getContentEncodingSerializableHeader(httpEntity);
            this.streaming = httpEntity.isStreaming();
        }
    }
}
