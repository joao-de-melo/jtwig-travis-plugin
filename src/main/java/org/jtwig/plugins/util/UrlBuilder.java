package org.jtwig.plugins.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UrlBuilder {
    public static UrlBuilder url (String baseUrl) {
        return new UrlBuilder(baseUrl);
    }

    private final String baseUrl;
    private final List<String> paths = new ArrayList<>();
    private final List<NameValuePair> queryParameters = new ArrayList<>();

    UrlBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public UrlBuilder addToPath (String path) {
        paths.add(path);
        return this;
    }

    public UrlBuilder addToPathEscaped (String path) {
        paths.add(URLEncode.encode(path));
        return this;
    }

    public UrlBuilder addQueryParam (String key, String value) {
        queryParameters.add(new BasicNameValuePair(key, value));
        return this;
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        Iterator<String> iterator = paths.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append("/").append(iterator.next());
        }

        if (!queryParameters.isEmpty()) {
            stringBuilder.append("?");
            for (int i = 0; i < queryParameters.size(); i++) {
                NameValuePair nameValuePair = queryParameters.get(i);
                stringBuilder.append(nameValuePair.getName())
                        .append("=")
                        .append(nameValuePair.getValue());

                if (i < queryParameters.size() - 1) {
                    stringBuilder.append("&");
                }
            }
        }

        return stringBuilder.toString();
    }
}
