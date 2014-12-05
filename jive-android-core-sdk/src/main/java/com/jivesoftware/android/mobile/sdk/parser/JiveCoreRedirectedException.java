package com.jivesoftware.android.mobile.sdk.parser;

import org.apache.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiveCoreRedirectedException extends JiveCoreException {
    @Nullable
    public final String location;
    public JiveCoreRedirectedException(HttpResponse httpResponse, @Nullable String location) {
        super((String)null, httpResponse);
        this.location = location;
    }
}
