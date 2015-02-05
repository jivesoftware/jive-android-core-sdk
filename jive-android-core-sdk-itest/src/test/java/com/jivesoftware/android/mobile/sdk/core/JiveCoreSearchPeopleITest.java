package com.jivesoftware.android.mobile.sdk.core;

import com.google.common.base.Strings;
import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class JiveCoreSearchPeopleITest extends AbstractITest {
    @Test
    public void testSearchPeople() throws Exception {
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setSearchTermFilter(Arrays.asList(USER2.displayName.split("\\s+")));
        PersonListEntity personListEntity = jiveCoreAdmin.searchPeople(options).call();

        // IntelliJ thinks the below <PersonEntity> is redundant, but javac disagrees
        assertThat(personListEntity.list, Matchers.<PersonEntity>hasItem(personEntityDisplayName(USER2.displayName)));
    }

    @Test
    public void testSearchPeopleWithNextLink() throws Exception {
        String commonSearchTerm = Strings.commonPrefix(USER2.displayName, USER3.displayName);
        JiveCoreRequestOptions options = new JiveCoreRequestOptions();
        options.setCount(1);
        options.setSearchTermFilter(Arrays.asList(commonSearchTerm));

        PersonListEntity personListEntity = jiveCoreAdmin.searchPeople(options).call();
        assertEquals(1, personListEntity.list.size());

        PersonListEntity nextPersonListEntity = jiveCoreAdmin.searchPeople(personListEntity.links.next).call();
        assertEquals(1, nextPersonListEntity.list.size());
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
