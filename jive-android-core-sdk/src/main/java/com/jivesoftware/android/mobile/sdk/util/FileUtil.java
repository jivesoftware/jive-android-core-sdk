package com.jivesoftware.android.mobile.sdk.util;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;

/**
 * Created by mark.schisler on 8/19/14.
 */
@ParametersAreNonnullByDefault
public class FileUtil {
    public static void writeToFile(InputStream inputStream, File file) throws IOException {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.flush();
        } finally {
            if ( output != null ) {
                try {
                    output.close();
                } catch ( IOException e) {
                }
            }
        }
    }
}
