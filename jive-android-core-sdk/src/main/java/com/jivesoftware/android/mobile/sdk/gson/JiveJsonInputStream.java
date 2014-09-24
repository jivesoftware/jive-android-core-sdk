package com.jivesoftware.android.mobile.sdk.gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class JiveJsonInputStream extends InputStream {
    private BufferedInputStream inputStream;
    protected static final String SKIP_HEADER_STRING = "throw 'allowIllegalResourceCall is false.';";
    private static final byte[] SKIP_HEADER = SKIP_HEADER_STRING.getBytes();

    public JiveJsonInputStream(InputStream inputStream) throws IOException {
        this.inputStream = new BufferedInputStream(inputStream);
        skipHeaderIfPresent();
    }

    private void skipHeaderIfPresent() throws IOException {
        int length = SKIP_HEADER.length;
        inputStream.mark(length);
        byte[] header = new byte[length];
        if ((inputStream.read(header, 0, length) < length) || !Arrays.equals(header, SKIP_HEADER)) {
            // No header
            inputStream.reset();
        }
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public void mark(int readLimit) {
        inputStream.mark(readLimit);
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }

    @Override
    public synchronized void reset() throws IOException {
        inputStream.reset();
    }

    @Override
    public long skip(long byteCount) throws IOException {
        return inputStream.skip(byteCount);
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return inputStream.read(buffer);
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        return inputStream.read(buffer, offset, length);
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }
}
