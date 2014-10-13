package com.jivesoftware.android.mobile.sdk.httpclient;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicLineFormatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;

@ParametersAreNonnullByDefault
public class SerializableHeader implements Header, Serializable {
    @Nonnull
    private final String name;
    private final String value;

    public SerializableHeader(Header header) {
        this(header.getName(), header.getValue());
    }

    public SerializableHeader(String name, @Nullable String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        // no need for non-default formatting in toString()
        return BasicLineFormatter.DEFAULT.formatHeader(null, this).toString();
    }

    public HeaderElement[] getElements() throws ParseException {
        if (this.value != null) {
            // result intentionally not cached, it's probably not used again
            return BasicHeaderValueParser.parseElements(this.value, null);
        } else {
            return new HeaderElement[] {};
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object)this).getClass() != o.getClass()) return false;

        SerializableHeader that = (SerializableHeader) o;

        if (!name.equals(that.name)) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
