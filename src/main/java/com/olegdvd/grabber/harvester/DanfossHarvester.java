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
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class DanfossHarvester implements Harvester {

    private static final Logger LOG = LoggerFactory.getLogger(DanfossHarvester.class);

    private static final String PAGED_URL = "https://open.danfoss.ru/modal?route=search/index&success=popup&query=";
    private static final Gson GSON = new Gson();
    private final WebClient webClient;

    public DanfossHarvester(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public GatheredData request(String materialId) {
        GatheredData gatheredData = new DanfossGatheredData();
        if (isNotEmpty(materialId)) {
            String htmlString = webClient.getJSONFromResponseBody(PAGED_URL, materialId);
            if (isEmpty(htmlString)) {
                gatheredData.gatheringTemplate().put(KeysEnum.NAME.getCode(), "Error (Empty Server Response)");
            }
            String data = GSON.fromJson(htmlString, DanfosResponseContainer.class).getData();

            Document pagetoDocument = Jsoup.parse(data);
            gatheredData.gatheringTemplate().put(KeysEnum.NAME.getCode(), pagetoDocument.select("td[class=name]").text());
            gatheredData.gatheringTemplate().put(KeysEnum.PRICE.getCode(), pagetoDocument.select("td[class=price]").text());
            gatheredData.gatheringTemplate().put(KeysEnum.HREF.getCode(), pagetoDocument.select("a[class=tdn]").attr("href"));
            gatheredData.gatheringTemplate().put(KeysEnum.URL.getCode(), pagetoDocument.select("a[class=change-qty]").attr("data-url"));
        } else {
            gatheredData.gatheringTemplate().put(KeysEnum.NAME.getCode(), "Error (Empty Article)");
        }

        return gatheredData;
    }

    @Override
    public int getDataColumnNumber() {
        // Number of column in Excel file where article (matherialId) is stored. (0-based)
        return 2;
    }


    private static class DanfosResponseContainer {

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

