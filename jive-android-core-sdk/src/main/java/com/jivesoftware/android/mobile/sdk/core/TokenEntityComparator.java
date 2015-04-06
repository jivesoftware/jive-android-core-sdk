package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.TokenEntity;
import com.jivesoftware.android.mobile.sdk.util.ComparatorEngine;
import com.jivesoftware.android.mobile.sdk.util.NonnullComparableComparator;
import com.jivesoftware.android.mobile.sdk.util.NonnullComparator;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TokenEntityComparator extends NonnullComparator<TokenEntity> {
    public static final TokenEntityComparator SINGLETON = new TokenEntityComparator();

    private final NonnullComparableComparator<String> nonnullStringComparator = new NonnullComparableComparator<String>();
    private final NonnullComparableComparator<Long> nonnullIntegerComparator = new NonnullComparableComparator<Long>();
    @Override
    public int compareNonNull(TokenEntity lhs, TokenEntity rhs) {
        int comparison = new ComparatorEngine().
                compareAnd(nonnullStringComparator, lhs.accessToken, rhs.accessToken).
                compareAnd(nonnullIntegerComparator, lhs.expiresIn, rhs.expiresIn).
                compareAnd(nonnullStringComparator, lhs.refreshToken, rhs.refreshToken).
                compare(nonnullStringComparator, lhs.tokenType, rhs.tokenType);
        return comparison;
    }
}
