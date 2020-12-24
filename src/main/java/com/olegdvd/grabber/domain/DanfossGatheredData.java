package com.olegdvd.grabber.domain;

import java.util.HashMap;
import java.util.Map;

public class DanfossGatheredData implements GatheredData {

    private final Map<String, Integer> keysMap = new HashMap<String, Integer>() {{
        put(KeysEnum.NAME.getCode(), 3);
        put(KeysEnum.URL.getCode(), 15);
        put(KeysEnum.PRICE.getCode(), 16);
        put(KeysEnum.HREF.getCode(), 17);
    }};


    private final Map<String, String> map;

    public DanfossGatheredData() {
        this.map = generateMap();
    }

    @Override
    public Map<String, Integer> keysMap() {
        return keysMap;
    }

    @Override
    public Map<String, String> data() {
        return map;
    }
}
