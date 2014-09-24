package com.jivesoftware.android.mobile.sdk.entity.matcher;

import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.entity.MemberEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.PlaceEntity;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MemberEntityMatchers {
    public static Matcher<MemberEntity> memberPerson(Matcher<? super PersonEntity> personMatcher) {
        return new PropertyMatcher<PersonEntity, MemberEntity>(personMatcher, "person") {
            @Nullable
            @Override
            protected PersonEntity getPropertyValue(@Nonnull MemberEntity item) throws Exception {
                return item.person;
            }
        };
    }
    public static Matcher<MemberEntity> memberGroup(Matcher<? super PlaceEntity> groupMatcher) {
        return new PropertyMatcher<PlaceEntity, MemberEntity>(groupMatcher, "group") {
            @Nullable
            @Override
            protected PlaceEntity getPropertyValue(@Nonnull MemberEntity item) throws Exception {
                return item.group;
            }
        };
    }
}
