package com.olegdvd.grabber.domain;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    public String getJSONFromResponseBody(String pagedUrl, String paramName) {
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

    public Response getResponse(String pagedUrl, String paramName) {
        String fullUrl = getFullUrl(pagedUrl, paramName);
        Optional<HttpUrl> url = Optional.ofNullable(HttpUrl.parse(fullUrl));
        Response response = null;
        if (url.isPresent()) {
            Request request = new Request.Builder().url(url.get())
                    .addHeader("Cookie", "")
                    .get().build();
            try {
                response = WebClient.CLIENT.newCall(request).execute();
                LOG.debug("Response from [{}] with: {}", fullUrl, response.code());
                if (response.code() == 200) {
                    return response;
                }
            } catch (IOException e) {
                LOG.error("Source server is unreachable or changed/wrong URL: {}", fullUrl);
            }
        } else {
            LOG.warn("Failed to scrap from URL: {}", fullUrl);
            throw new RuntimeException("Failed to parse url: " + fullUrl);
        }
        return response;
    }
//        Response response = null;
//        Call call = CLIENT.newCall(request);
//        try {
//            response = call.execute();
//        } catch (IOException e) {
//            return "Error";
//        }
//        final int baseUrlLength = BASE_URL.length();
//
//        String result = "";
//
//        try {
//            result = URLDecoder.decode(response
//                    .request()
//                    .url().toString().substring(baseUrlLength + article.length()), StandardCharsets.UTF_8.name());
//            return result;
//        } catch (UnsupportedEncodingException e) {
//            LOG.warn("Failed to URLdecode server response {} and article: {}", result, article, e);
//        } catch (StringIndexOutOfBoundsException ex){
//            LOG.warn("Failed to process string [{}] of article: {} {}", result, article, ex.getMessage());
//        }
        public String getFullUrl (String pagedUrl, String paramName){ return pagedUrl + paramName; }

    }
