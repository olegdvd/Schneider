package com.olegdvd.schneider;

import com.google.gson.Gson;
import com.olegdvd.schneider.domain.DanfossGatheredData;
import com.olegdvd.schneider.domain.GatheredData;
import com.olegdvd.schneider.domain.KeysEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.*;

public class Danfos {

    private static final Logger LOG = LoggerFactory.getLogger(Danfos.class);
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build();
    private static final String PAGED_URL = "https://open.danfoss.ru/modal?route=search/index&success=popup&query=";
    private static final Gson GSON = new Gson();

    Danfos() {
    }

    protected GatheredData request(String materialId) {
        GatheredData gatheredData = new DanfossGatheredData();
        if (isEmpty(materialId)) {
            gatheredData.data().put(KeysEnum.NAME.getCode(), "Error (Empty Article)");
            return gatheredData;
        }

        String fullUrl = getFullUrl(PAGED_URL, materialId);
        Optional<HttpUrl> url = Optional.ofNullable(HttpUrl.parse(fullUrl));
        if (url.isPresent()) {
            Request request = new Request.Builder().url(url.get())
                    .addHeader("Cookie", "" )
                    .get().build();
            String html = null;
            try {
                Response response = CLIENT.newCall(request).execute();
                LOG.debug("Response from [{}] with: {}", fullUrl, response.code());
                if (response.code() == 200) {
                    html = Objects.requireNonNull(response.body()).string();
                }
            } catch (IOException e) {
                LOG.error("Source server is unreachable or changed/wrong URL: {}", fullUrl);
            }
            if (isEmpty(html)) {
                gatheredData.data().put(KeysEnum.NAME.getCode(), "Error (Empty Server Response)");
                return gatheredData;
            }
            String data = GSON.fromJson(html, DanfosResponseContainer.class).getData();

            Document pagetoDocument = Jsoup.parse(data);
            gatheredData.data().put(KeysEnum.NAME.getCode(), pagetoDocument.select("td[class=name]").text());
            gatheredData.data().put(KeysEnum.PRICE.getCode(), pagetoDocument.select("td[class=price]").text());
            gatheredData.data().put(KeysEnum.HREF.getCode(), pagetoDocument.select("a[class=tdn]").attr("href"));
            gatheredData.data().put(KeysEnum.URL.getCode(), pagetoDocument.select("a[class=change-qty]").attr("data-url"));

            return gatheredData;
        }
        LOG.warn("Failed to scrap from URL: {}", fullUrl);
        throw new RuntimeException("Failed to parse url: " + fullUrl);
    }

    private String getFullUrl(String pagedUrl, String materialId) {
        return pagedUrl + materialId;
    }
}
