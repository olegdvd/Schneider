package com.olegdvd.schneider;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

class UrlContentGrabber {
    private static final Logger LOG = LoggerFactory.getLogger(UrlContentGrabber.class);
    private static final String BASE_URL = "https://www.se.com/ww/en/product/";
//    private static final String BASE_URL = "https://www.se.com/ua/uk/product/";

    String request(String article) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + article)
                .build();

        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            return "Error";
        }

        final int baseUrlLength = BASE_URL.length();

        String result = "";

        try {
            result = URLDecoder.decode(response
                    .request()
                    .urlString().substring(baseUrlLength + article.length() + 1), StandardCharsets.UTF_8.name());
            return result;
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Failed to URLdecode server response {} and article: {}", result, article, e);
        } catch (StringIndexOutOfBoundsException ex){
            LOG.warn("Failed to process string [{}] of article: {} {}", result, article, ex.getMessage());
        }

        return String.valueOf(response.code());

    }
}
