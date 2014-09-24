package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.parser.HttpResponseParser;
import com.jivesoftware.android.mobile.sdk.parser.JiveCoreExceptionFactory;

import javax.annotation.Nonnull;

public interface HttpResponseParserFactory<T> {
    @Nonnull
    HttpResponseParser<T> createHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory);
}
