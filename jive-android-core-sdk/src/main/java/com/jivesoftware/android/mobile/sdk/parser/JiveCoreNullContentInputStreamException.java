package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreNullContentInputStreamException extends JiveCoreHttpEntitiedException {
    public JiveCoreNullContentInputStreamException(HttpResponse httpResponse, HttpEntity httpEntity) {
        super(null, httpResponse, httpEntity);
    }
}
