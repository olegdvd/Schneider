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


        try {
            return URLDecoder.decode(response
                    .request()
                    .urlString().substring(baseUrlLength + article.length()), StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Failed to URLdecode server response {}", response.request().urlString(), e);
        }

        return String.valueOf(response.code());

    }
}
