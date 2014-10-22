package com.jivesoftware.android.mobile.sdk.matcher;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreQueryParameterProvider;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.contains;

@ParametersAreNonnullByDefault
public final class JiveCoreRequestOptionsMatchers {

    private static final Pattern SPEC_PATTERN = Pattern.compile("^([^\\(]+?)(?:\\((.*)\\))?$");
    private static final Pattern SPLITTER_PATTERN = Pattern.compile("(?<!\\\\),");

    private JiveCoreRequestOptionsMatchers() {
        // Prevent construction
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsEntityDescriptorFilters(
            Matcher<Iterable<? extends String>> typeMatcher) {
        return optionsQueryParameter("filter", optionsSpecMatcher("filter", "entityDescriptor", false, typeMatcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsRelationshipFilters(
            Matcher<Iterable<? extends String>> typeMatcher) {
        return optionsQueryParameter("filter", optionsFilter("relationship", typeMatcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsSearchTermFilters(
            Matcher<Iterable<? extends String>> matcher) {
        return optionsQueryParameter("filter", optionsFilter("search", matcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsTagFilters(
            Matcher<Iterable<? extends String>> matcher) {
        return optionsQueryParameter("filter", optionsFilter("tag", matcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsTypeFilters(
            Matcher<Iterable<? extends String>> typeMatcher) {
        return optionsQueryParameter("filter", optionsFilter("type", typeMatcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsVerbFilters(
            Matcher<Iterable<? extends String>> typeMatcher) {
        return optionsQueryParameter("filter", optionsFilter("verb", typeMatcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsAuthorFilters(
            Matcher<Iterable<? extends String>> typeMatcher) {
        return optionsQueryParameter("filter", optionsFilter("author", typeMatcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsPlaceFilters(
            Matcher<Iterable<? extends String>> typeMatcher) {
        return optionsQueryParameter("filter", optionsFilter("place", typeMatcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsEntryStateFilters(
            Matcher<Iterable<? extends String>> typeMatcher) {
        return optionsQueryParameter("filter", optionsFilter("entryState", typeMatcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsUnreadFilter() {
        return optionsQueryParameter("filter", optionsHasFilter("unread"));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsNotificationsFilter() {
        return optionsQueryParameter("filter", optionsHasFilter("notifications"));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsDirectives(
            Matcher<Iterable<? extends String>> matcher) {
        return optionsQueryParameter("directive", matcher);
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsCollapseSkipCollectionIdsDirective(
            Matcher<Iterable<? extends String>> matcher) {
        return optionsQueryParameter("directive", optionsDirective("collapseSkip", matcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsIncludeRtcDirective() {
        return optionsQueryParameter("directive", optionsHasDirective("include_rtc"));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsCollapseDirective() {
        return optionsQueryParameter("directive", optionsHasDirective("collapse"));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsSort(String expected) {
        return optionsQueryParameter("sort", contains(expected));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsPublished(String expected) {
        return optionsQueryParameter("published", contains(expected));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsUpdated(String expected) {
        return optionsQueryParameter("updated", contains(expected));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsBefore(String expected) {
        return optionsQueryParameter("before", contains(expected));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsAfter(String expected) {
        return optionsQueryParameter("after", contains(expected));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsAuthors(
            Matcher<Iterable<? extends String>> matcher) {
        return optionsQueryParameter("authors", matcher);
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsFields(
            Matcher<Iterable<? extends String>> matcher) {
        return optionsQueryParameter("fields", splitString(matcher));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsAnchorUri(String expected) {
        return optionsQueryParameter("anchor", contains(expected));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsAutoCategorize() {
        return optionsHasQueryFlagParameter("autoCategorize");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsCollapse() {
        return optionsHasQueryFlagParameter("collapse");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsOldestUnread() {
        return optionsHasQueryFlagParameter("oldestUnread");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsAbridged() {
        return optionsHasQueryFlagParameter("abridged");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsActiveOnly() {
        return optionsHasQueryFlagParameter("activeOnly");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsExclude() {
        return optionsHasQueryFlagParameter("exclude");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsExcludeReplies() {
        return optionsHasQueryFlagParameter("excludeReplies");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsMinor() {
        return optionsHasQueryFlagParameter("minor");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsAuthorComment() {
        return optionsHasQueryFlagParameter("author");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsIsCreate() {
        return optionsHasQueryFlagParameter("isCreate");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsHierarchical() {
        return optionsHasQueryFlagParameter("hierarchical");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsIncludeBlogs() {
        return optionsHasQueryFlagParameter("includeBlogs");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsIncludeChildrenOutcomes() {
        return optionsHasQueryFlagParameter("includeChildrenOutcomes");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsSendEmails() {
        return optionsHasQueryFlagParameter("sendEmails");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsPreserveAspectRatio() {
        return optionsHasQueryFlagParameter("preserveAspectRatio");
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsCount(int expected) {
        return optionsQueryParameter("count", contains(Integer.toString(expected)));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsStartIndex(int expected) {
        return optionsQueryParameter("startIndex", contains(Integer.toString(expected)));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsMax(int expected) {
        return optionsQueryParameter("max", contains(Integer.toString(expected)));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsWidth(int expected) {
        return optionsQueryParameter("width", contains(Integer.toString(expected)));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsHeight(int expected) {
        return optionsQueryParameter("height", contains(Integer.toString(expected)));
    }

    @Nonnull
    public static Matcher<JiveCoreQueryParameterProvider> optionsQueryParameter(
            final String parameterName,
            @Nonnull final Matcher<? super Iterable<? extends String>> valuesMatcher
    ) {
        return new TypeSafeMatcher<JiveCoreQueryParameterProvider>() {
            @Override
            protected boolean matchesSafely(JiveCoreQueryParameterProvider options) {
                Map<String, List<String>> params = options.provideQueryParameters();
                if (params.containsKey(parameterName)) {
                    return valuesMatcher.matches(params.get(parameterName));
                } else {
                    return valuesMatcher.matches(Collections.emptyList());
                }
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("query parameter '")
                        .appendText(parameterName)
                        .appendText("' ")
                        .appendDescriptionOf(valuesMatcher);
            }
        };
    }

    @Nonnull
    private static Matcher<JiveCoreQueryParameterProvider> optionsHasQueryFlagParameter(final String parameterName) {
        return new TypeSafeMatcher<JiveCoreQueryParameterProvider>() {
            @Override
            protected boolean matchesSafely(JiveCoreQueryParameterProvider options) {
                Map<String, List<String>> params = options.provideQueryParameters();
                if (!params.containsKey(parameterName)) {
                    return false;
                }
                List<String> strings = params.get(parameterName);
                return !strings.isEmpty() && Boolean.parseBoolean(strings.get(0));
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("has query flag parameter '")
                        .appendText(parameterName)
                        .appendText("'");
            }
        };
    }

    @Nonnull
    public static Matcher<? super Iterable<? extends String>> optionsFilter(
            @Nonnull final String filterName,
            @Nonnull final Matcher<? super Iterable<? extends String>> valueMatcher) {
        return optionsSpecMatcher("filter", filterName, true, valueMatcher);
    }

    @Nonnull
    public static Matcher<? super Iterable<? extends String>> optionsDirective(
            @Nonnull final String directiveName,
            @Nonnull final Matcher<? super Iterable<? extends String>> valueMatcher) {
        return optionsSpecMatcher("directive", directiveName, true, valueMatcher);
    }

    @Nonnull
    public static Matcher<? super Iterable<? extends String>> optionsHasFilter(final String filterName) {
        return optionsHasSpec("filter", filterName);
    }

    @Nonnull
    public static Matcher<? super Iterable<? extends String>> optionsHasDirective(final String directiveName) {
        return optionsHasSpec("directive", directiveName);
    }

    @Nonnull
    private static Matcher<? super Iterable<? extends String>> optionsSpecMatcher(
            final String specType,
            final String specName,
            final boolean doSplit,
            final Matcher<? super Iterable<? extends String>> valueMatcher) {
        return new TypeSafeMatcher<Iterable<? extends String>>() {
            @Override
            protected boolean matchesSafely(Iterable<? extends String> values) {
                ArrayList<String> list = new ArrayList<String>();
                for (String value : values) {
                    java.util.regex.Matcher matcher = SPEC_PATTERN.matcher(value);
                    if (matcher.matches()) {
                        String filter = matcher.group(1);
                        if (specName.equals(filter)) {
                            String innards = matcher.group(2);
                            if (doSplit) {
                                String[] split = SPLITTER_PATTERN.split(innards);
                                list.addAll(Arrays.asList(split));
                            } else {
                                list.add(innards);
                            }
                        }
                    }
                }
                return valueMatcher.matches(list);
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText(specType)
                        .appendText(" type '")
                        .appendText(specName)
                        .appendText("' values ")
                        .appendDescriptionOf(valueMatcher);
            }
        };
    }

    @Nonnull
    private static Matcher<? super Iterable<? extends String>> optionsHasSpec(
            final String specType,
            final String specName) {
        return new TypeSafeMatcher<Iterable<? extends String>>() {
            @Override
            protected boolean matchesSafely(Iterable<? extends String> values) {
                boolean found = false;
                for (String value : values) {
                    java.util.regex.Matcher matcher = SPEC_PATTERN.matcher(value);
                    if (matcher.matches()) {
                        String filter = matcher.group(1);
                        if (specName.equals(filter)) {
                            found = true;
                            break;
                        }
                    }
                }
                return found;
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("has ")
                        .appendText(specType)
                        .appendText(" '")
                        .appendText(specName)
                        .appendText("'");
            }
        };
    }

    @Nonnull
    private static Matcher<Iterable<? extends String>> splitString(
            @Nonnull final Matcher<? super Iterable<? extends String>> valueMatcher) {
        return new TypeSafeMatcher<Iterable<? extends String>>() {
            @Override
            protected boolean matchesSafely(Iterable<? extends String> items) {
                ArrayList<String> result = new ArrayList<String>();
                for (String item : items) {
                    String[] split = SPLITTER_PATTERN.split(item);
                    result.addAll(Arrays.asList(split));
                }
                return valueMatcher.matches(result);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" split strings ")
                        .appendDescriptionOf(valueMatcher);
            }
        };
    }

}
