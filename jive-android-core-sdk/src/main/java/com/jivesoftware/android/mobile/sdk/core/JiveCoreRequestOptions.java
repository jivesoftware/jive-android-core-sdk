package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreDirectiveValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreEntityDescriptor;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreEntryStateValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreRelationshipValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreSortValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreObjectTypeValue;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreVerbValue;
import com.jivesoftware.android.mobile.sdk.util.DateFormatUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * General purpose options specification.  Not all capabilities are available on a given API call.  Please consult
 * the Jive platform API documentation to identify what parameters should be used.
 */
@ParametersAreNonnullByDefault
public final class JiveCoreRequestOptions implements JiveCoreQueryParameterProvider {

    private static final Pattern ESCAPE_PATTERN = Pattern.compile("[,\\(\\)\\\\]");

    @Nonnull
    final JiveCoreRequestOptionsValues values = new JiveCoreRequestOptionsValues();

    /**
     * Applies all options defined in the provided options object to the local instance, overwriting
     * any pre-existing values.
     *
     * @param other instance to pull values from
     */
    public JiveCoreRequestOptions overlay(JiveCoreRequestOptions other) {
        values.overlay(other.values);
        return this;
    }

    /**
     * Applies all options defined in the provided options object to the local instance, overwriting
     * any pre-existing values.
     *
     * @param other instance to pull values from
     */
    public JiveCoreRequestOptions overlay(JiveCoreImmutableRequestOptions other) {
        values.overlay(other.values);
        return this;
    }

    public JiveCoreImmutableRequestOptions createImmutableCopy() {
        JiveCoreRequestOptionsValues copy = new JiveCoreRequestOptionsValues();
        copy.overlay(values);
        return new JiveCoreImmutableRequestOptions(copy);
    }

    @Override
    @Nonnull
    public LinkedHashMap<String, List<String>> provideQueryParameters() {
        return values.provideQueryParameters();
    }

    @Nonnull
    public JiveCoreRequestOptions setEntityDescriptorFilter(int objectType, long objectId) {
        JiveCoreEntityDescriptor entityDescriptor = new JiveCoreEntityDescriptor(objectType, objectId);
        values.putFilter("entityDescriptor", Collections.singletonList(entityDescriptor.toString()));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setEntityDescriptorFilter(JiveCoreEntityDescriptor... entityDescriptors) {
        values.putFilter("entityDescriptor", toUnescapedList(Arrays.asList(entityDescriptors)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setRelationshipFilter(JiveCoreRelationshipValue relationship) {
        values.putFilter("relationship", toEscapedList(Collections.singletonList(relationship.toString())));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setSearchTermFilter(Iterable<String> searchTerms) {
        values.putFilter("search", toEscapedList(searchTerms));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setTagFilter(Iterable<String> tags) {
        values.putFilter("tag", toEscapedList(tags));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setTypeFilter(Iterable<? extends JiveCoreObjectTypeValue> types) {
        values.putFilter("type", toEscapedList(types));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setVerbFilter(Iterable<? extends JiveCoreVerbValue> verbs) {
        values.putFilter("verb", toEscapedList(verbs));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAuthorFilter(Iterable<String> authors) {
        values.putFilter("author", toEscapedList(authors));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setPlaceFilter(Iterable<String> placeUris) {
        values.putFilter("place", toEscapedList(placeUris));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setEntryStateFilter(Iterable<? extends JiveCoreEntryStateValue> entryStateValues) {
        values.putFilter("entryState", toEscapedList(entryStateValues));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setUnreadFilter(boolean flag) {
        updateFlagFilter("unread", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setNotificationsFilter(boolean flag) {
        updateFlagFilter("notifications", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setDirectives(Iterable<JiveCoreDirectiveValue> directiveList) {
        for (JiveCoreDirectiveValue directive : directiveList) {
            values.putDirective(directive.toString(), Collections.<String>emptyList());
        }
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setCollapseSkipCollectionIdsDirective(Iterable<String> ids) {
        values.putDirective("collapseSkip", toEscapedList(ids));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setSort(JiveCoreSortValue sortOrder) {
        values.putQueryParameter("sort", Collections.singletonList(sortOrder.toString()));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setPublished(Date date) {
        values.putQueryParameter("published", Collections.singletonList(toFormattedDate(date)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setUpdated(Date date) {
        values.putQueryParameter("updated", Collections.singletonList(toFormattedDate(date)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAfter(Date date) {
        values.removeQueryParameter("before");
        values.putQueryParameter("after", Collections.singletonList(toFormattedDate(date)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setBefore(Date date) {
        values.putQueryParameter("before", Collections.singletonList(toFormattedDate(date)));
        values.removeQueryParameter("after");
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAuthors(Iterable<PersonEntity> authors) {
        ArrayList<String> transformed = new ArrayList<String>();
        for (PersonEntity personEntity : authors) {
            transformed.add(personEntity.resources.get("self").ref);
        }
        values.putQueryParameter("authors", transformed);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setFields(Iterable<String> fieldNames) {
        values.putQueryParameter("fields", Collections.singletonList(toJoinedString(fieldNames)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAnchor(String uri) {
        values.putQueryParameter("anchor", Collections.singletonList(uri));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAutoCategorize(boolean flag) {
        updateFlagQueryParameter("autoCategorize", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setCollapsed(boolean flag) {
        updateFlagQueryParameter("collapse", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setOldestUnread(boolean flag) {
        updateFlagQueryParameter("oldestUnread", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAbridged(boolean flag) {
        updateFlagQueryParameter("abridged", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setActiveOnly(boolean flag) {
        updateFlagQueryParameter("activeOnly", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setExclude(boolean flag) {
        updateFlagQueryParameter("exclude", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setExcludeReplies(boolean flag) {
        updateFlagQueryParameter("excludeReplies", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setMinor(boolean flag) {
        updateFlagQueryParameter("minor", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setAuthorComment(boolean flag) {
        updateFlagQueryParameter("author", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setIsCreate(boolean flag) {
        updateFlagQueryParameter("isCreate", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setHierarchical(boolean flag) {
        updateFlagQueryParameter("hierarchical", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setIncludeBlogs(boolean flag) {
        updateFlagQueryParameter("includeBlogs", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setIncludeChildrenOutcomes(boolean flag) {
        updateFlagQueryParameter("includeChildrenOutcomes", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setSendEmails(boolean flag) {
        updateFlagQueryParameter("sendEmails", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setPreserveAspectRatio(boolean flag) {
        updateFlagQueryParameter("preserveAspectRatio", flag);
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setCount(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Invalid count: " + count);
        }
        values.putQueryParameter("count", Collections.singletonList(Integer.toString(count)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setStartIndex(int startIndex) {
        if (startIndex < 0) {
            throw new IllegalArgumentException("Invalid startIndex: " + startIndex);
        }
        values.putQueryParameter("startIndex", Collections.singletonList(Integer.toString(startIndex)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setMax(int max) {
        if (max <= 0) {
            throw new IllegalArgumentException("Invalid max: " + max);
        }
        values.putQueryParameter("max", Collections.singletonList(Integer.toString(max)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setWidth(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Invalid width: " + width);
        }
        values.putQueryParameter("width", Collections.singletonList(Integer.toString(width)));
        return this;
    }

    @Nonnull
    public JiveCoreRequestOptions setHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Invalid height: " + height);
        }
        values.putQueryParameter("height", Collections.singletonList(Integer.toString(height)));
        return this;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiveCoreRequestOptions that = (JiveCoreRequestOptions) o;

        if (!values.equals(that.values)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    @Nonnull
    public String toString() {
        return "{" + getClass().getSimpleName() + " values=" + values + "}";
    }

    private void updateFlagFilter(String filter, boolean value) {
        if (value) {
            values.putFilter(filter, Collections.<String>emptyList());
        } else {
            values.removeFilter(filter);
        }
    }

    private void updateFlagQueryParameter(String queryParameter, boolean value) {
        values.putQueryParameter(queryParameter, Collections.singletonList(Boolean.toString(value)));
    }

    @Nonnull
    private static List<String> toUnescapedList(Iterable<?> values) {
        ArrayList<String> result = new ArrayList<String>();
        for (Object item : values) {
            result.add(item.toString());
        }
        return result;
    }

    @Nonnull
    private static List<String> toEscapedList(Iterable<?> values) {
        List<String> result = new ArrayList<String>();
        for (Object value : values) {
            String escaped = escape(value);
            result.add(escaped);
        }
        return result;
    }

    @Nonnull
    private static String toJoinedString(Iterable<?> values) {
        StringBuilder builder = new StringBuilder();
        for (Object value : values) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            String escaped = escape(value);
            builder.append(escaped);
        }
        return builder.toString();
    }

    @Nonnull
    private static String escape(Object value) {
        String unescaped = value.toString();
        Matcher matcher = ESCAPE_PATTERN.matcher(unescaped);
        return matcher.replaceAll("\\\\$0");
    }

    @Nonnull
    private static String toFormattedDate(Date date) {
        DateFormat gmtIso8601DateFormat = DateFormatUtil.getGmtIso8601DateFormat();
        return gmtIso8601DateFormat.format(date);
    }

}
