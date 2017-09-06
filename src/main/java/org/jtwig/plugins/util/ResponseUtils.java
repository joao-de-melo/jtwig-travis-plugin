package org.jtwig.plugins.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;

public class ResponseUtils {
    public static String toLogString(HttpResponse response) {
        return response.toString() + "\nContent: \n" + getContent(response);
    }

    public static String getContent(HttpResponse httpResponse) {
        try (InputStream input = httpResponse.getEntity().getContent()) {
            return IOUtils.toString(input);
        } catch (IOException e) {
            return "";
        }
    }
}
