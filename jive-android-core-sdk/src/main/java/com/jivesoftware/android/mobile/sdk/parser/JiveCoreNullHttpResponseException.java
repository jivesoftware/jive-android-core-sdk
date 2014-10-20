package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.httpclient.SerializableHeader;

import java.util.Collections;

public class JiveCoreNullHttpResponseException extends JiveCoreException {
    public JiveCoreNullHttpResponseException() {
        super(null, null, -1, Collections.<SerializableHeader>emptyList());
    }
}
