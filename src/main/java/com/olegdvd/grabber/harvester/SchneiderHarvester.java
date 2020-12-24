package com.olegdvd.grabber.harvester;

import com.olegdvd.grabber.domain.GatheredData;
import com.olegdvd.grabber.domain.KeysEnum;
import com.olegdvd.grabber.domain.SchneiderGatheredData;
import com.olegdvd.grabber.domain.WebClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class SchneiderHarvester implements Harvester {

    private static final Logger LOG = LoggerFactory.getLogger(SchneiderHarvester.class);
    private static final String PAGED_URL = "https://www.se.com/ww/en/product/";

    private final WebClient webClient;

    public SchneiderHarvester(WebClient webClient) {

        this.webClient = webClient;
    }

    @Override
    public GatheredData request(String article) {
        GatheredData gatheredData = new SchneiderGatheredData();
        if (isEmpty(article)) {
            gatheredData.gatheringTemplate().put(KeysEnum.NAME.getCode(), "Error (Empty Article)");

            return gatheredData;
        }
        Response response = webClient.getResponse(PAGED_URL, article);
        String result = "";
        try {
            final int baseUrlLength = PAGED_URL.length();
            result = URLDecoder.decode(response
                    .request()
                    .url().toString().substring(baseUrlLength + article.length()), StandardCharsets.UTF_8.name());

            return gatheredData;
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Failed to URLdecode server response {} and article: {}", result, article, e);
        } catch (StringIndexOutOfBoundsException ex) {
            LOG.warn("Failed to process string [{}] of article: {} {}", result, article, ex.getMessage());
        }
        if (isEmpty(result)) {
            gatheredData.gatheringTemplate().put(KeysEnum.NAME.getCode(), "Error (Empty Server Response)");

            return gatheredData;
        }
        gatheredData.gatheringTemplate().put(KeysEnum.NAME.getCode(), result);

        return gatheredData;
    }

}

