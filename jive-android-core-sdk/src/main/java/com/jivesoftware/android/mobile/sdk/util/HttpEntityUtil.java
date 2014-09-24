package com.jivesoftware.android.mobile.sdk.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

@ParametersAreNonnullByDefault
public class HttpEntityUtil {
    @Nonnull
    public static InputStream getUntransformedContentInputStream(HttpEntity httpEntity, InputStream maybeTransformedContentInputStream) throws IOException {
        // We aren't handling deflate because some vendors don't implement it properly, and
        // we don't want to work around improper implementations
        // http://stackoverflow.com/a/3932260/9636

        // We don't support compress because Java doesn't have an LZW decoder built in.
        // http://stackoverflow.com/q/2424998/9636

        Header contentEncodingHeader = httpEntity.getContentEncoding();
        InputStream contentInputStream;
        if (contentEncodingHeader == null) {
            contentInputStream = maybeTransformedContentInputStream;
        } else {
            String contentEncoding = contentEncodingHeader.getValue();
            if (contentEncoding == null) {
                contentInputStream = maybeTransformedContentInputStream;
            } else if (contentEncoding.contains("gzip")) {
                contentInputStream = new GZIPInputStream(maybeTransformedContentInputStream);
            } else {
                contentInputStream = maybeTransformedContentInputStream;
            }
        }
        return contentInputStream;
    }
}
