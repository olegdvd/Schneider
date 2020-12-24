package com.olegdvd.grabber.domain;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class WebClient {

    public static OkHttpClient CLIENT = null;
    private static final Logger LOG = LoggerFactory.getLogger(WebClient.class);
    public WebClient() {
        CLIENT = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public String makeServerCall(String pagedUrl, String paramName) {
        String fullUrl = getFullUrl(pagedUrl, paramName);
        Optional<HttpUrl> url = Optional.ofNullable(HttpUrl.parse(fullUrl));
        String htmlString = "";
        if (url.isPresent()) {
            Request request = new Request.Builder().url(url.get())
                    .addHeader("Cookie", "")
                    .get().build();
            try {
                Response response = WebClient.CLIENT.newCall(request).execute();
                LOG.debug("Response from [{}] with: {}", fullUrl, response.code());
                if (response.code() == 200) {
                    htmlString = Objects.requireNonNull(response.body()).string();
                }
            } catch (IOException e) {
                LOG.error("Source server is unreachable or changed/wrong URL: {}", fullUrl);
            }
        } else {
            LOG.warn("Failed to scrap from URL: {}", fullUrl);
            throw new RuntimeException("Failed to parse url: " + fullUrl);
        }
        return htmlString;
    }

        public String getFullUrl (String pagedUrl, String paramName){ return pagedUrl + paramName; }

    }