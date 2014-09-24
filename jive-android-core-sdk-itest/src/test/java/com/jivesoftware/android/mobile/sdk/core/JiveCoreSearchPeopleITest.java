package com.jivesoftware.android.mobile.sdk.core;

import com.google.common.base.Strings;
import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreSearchPeopleRequestOptions;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonListEntity;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class JiveCoreSearchPeopleITest extends AbstractITest {
    @Test
    public void testSearchPeople() throws Exception {
        JiveCoreSearchPeopleRequestOptions options = new JiveCoreSearchPeopleRequestOptions();
        options.setSearchTerms(Arrays.asList(USER2.displayName.split("\\s+")));
        PersonListEntity personListEntity = jiveCoreAdmin.searchPeople(options).call();

        // IntelliJ thinks the below <PersonEntity> is redundant, but javac disagrees
        assertThat(personListEntity.list, Matchers.<PersonEntity>hasItem(personEntityDisplayName(USER2.displayName)));
    }

    @Test
    public void testSearchPeopleWithNextLink() throws Exception {
        String commonSearchTerm = Strings.commonPrefix(USER2.displayName, USER3.displayName);
        JiveCoreSearchPeopleRequestOptions options = new JiveCoreSearchPeopleRequestOptions();
        options.setCount(1);
        options.setSearchTerms(Arrays.asList(commonSearchTerm));


        PersonListEntity personListEntity = jiveCoreAdmin.searchPeople(options).call();
        Matcher<Iterable<? super PersonEntity>> hasUser2OrUser3Matcher = hasItem(anyOf(
                personEntityDisplayName(USER2.displayName),
                personEntityDisplayName(USER3.displayName)));
        assertThat(personListEntity.list, hasUser2OrUser3Matcher);

        PersonListEntity nextPersonListEntity = jiveCoreAdmin.searchPeople(personListEntity.links.next).call();
        assertThat(nextPersonListEntity.list, hasUser2OrUser3Matcher);
    }

    private static TypeSafeMatcher<PersonEntity> personEntityDisplayName(@Nonnull String displayName) {
        return new PropertyMatcher<String, PersonEntity>("displayName", displayName) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull PersonEntity item) throws Exception {
                return item.displayName;
            }
        };
    }
}