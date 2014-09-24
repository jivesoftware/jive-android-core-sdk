package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.util.FileUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mark.schisler on 8/19/14.
 */
public class FileHttpResponseParser extends InputStreamClosingHttpResponseParser<File> {
    @Nonnull
    private final File file;

    public FileHttpResponseParser(@Nonnull JiveCoreExceptionFactory jiveCoreExceptionFactory, @Nonnull File file) {
        super(jiveCoreExceptionFactory);
        this.file = file;
    }

    @Nonnull
    @Override
    protected File parseContentInputStreamedResponseBeforeClosingContentInputStream(@Nonnull HttpResponse httpResponse, int statusCode, @Nonnull HttpEntity httpEntity, @Nonnull InputStream contentInputStream) throws IOException {
        FileUtil.writeToFile(contentInputStream, file);
        return file;
    }
}
