package org.jtwig.plugins.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class URLEncode {
    public static String encode (String content) {
        try {
            return URLEncoder.encode(content, Charset.defaultCharset().displayName());
        } catch (UnsupportedEncodingException e) {
            return content;
        }
    }
}
