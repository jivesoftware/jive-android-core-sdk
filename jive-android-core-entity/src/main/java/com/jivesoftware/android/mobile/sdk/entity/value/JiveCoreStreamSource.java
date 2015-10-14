package com.jivesoftware.android.mobile.sdk.entity.value;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Commonly used stream sources.
 */
@ParametersAreNonnullByDefault
public enum JiveCoreStreamSource implements JiveCoreStreamSourceValue {

    all,

    communications,

    connections,

    context,

    custom,

    profile,

    watches,

    publication

}

