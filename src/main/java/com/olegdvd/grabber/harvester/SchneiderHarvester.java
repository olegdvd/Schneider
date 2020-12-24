package com.olegdvd.grabber.harvester;

import com.google.gson.Gson;
import com.olegdvd.grabber.domain.DanfossGatheredData;
import com.olegdvd.grabber.domain.GatheredData;
import com.olegdvd.grabber.domain.KeysEnum;
import com.olegdvd.grabber.domain.WebClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class SchneiderHarvester implements Harvester {

    private static final Logger LOG = LoggerFactory.getLogger(SchneiderHarvester.class);
    private static final String PAGED_URL = "https://open.danfoss.ru/modal?route=search/index&success=popup&query=";
    private static final Gson GSON = new Gson();

    private final WebClient webClient;

    public SchneiderHarvester(WebClient webClient) {

        this.webClient = webClient;
    }

    @Override
    public GatheredData request(String materialId) {
        GatheredData gatheredData = new DanfossGatheredData();
        if (isEmpty(materialId)) {
            gatheredData.data().put(KeysEnum.NAME.getCode(), "Error (Empty Article)");
            return gatheredData;
        }

//        String fullUrl = getFullUrl(PAGED_URL, materialId);
//        Optional<HttpUrl> url = Optional.ofNullable(HttpUrl.parse(fullUrl));
//        if (url.isPresent()) {
//            Request request = new Request.Builder().url(url.get())
//                    .addHeader("Cookie", "")
//                    .get().build();
//            String htmlString = null;
//            try {
//                Response response = WebClient.CLIENT.newCall(request).execute();
//                LOG.debug("Response from [{}] with: {}", fullUrl, response.code());
//                if (response.code() == 200) {
//                    htmlString = Objects.requireNonNull(response.body()).string();
//                }
//            } catch (IOException e) {
//                LOG.error("Source server is unreachable or changed/wrong URL: {}", fullUrl);
//            }
        String htmlString = webClient.makeServerCall(PAGED_URL, materialId);
        if (isEmpty(htmlString)) {
                gatheredData.data().put(KeysEnum.NAME.getCode(), "Error (Empty Server Response)");
                return gatheredData;
            }
            String data = GSON.fromJson(htmlString, DanfosResponseContainer.class).getData();

            Document pagetoDocument = Jsoup.parse(data);
            gatheredData.data().put(KeysEnum.NAME.getCode(), pagetoDocument.select("td[class=name]").text());
            gatheredData.data().put(KeysEnum.PRICE.getCode(), pagetoDocument.select("td[class=price]").text());
            gatheredData.data().put(KeysEnum.HREF.getCode(), pagetoDocument.select("a[class=tdn]").attr("href"));
            gatheredData.data().put(KeysEnum.URL.getCode(), pagetoDocument.select("a[class=change-qty]").attr("data-url"));

            return gatheredData;
        }


    private String getFullUrl(String pagedUrl, String materialId) {
        return pagedUrl + materialId;
    }

    static class DanfosResponseContainer {

        private String status;
        private String data;

        public DanfosResponseContainer() {
        }

        public DanfosResponseContainer(String status, String data) {
            this.status = status;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}

