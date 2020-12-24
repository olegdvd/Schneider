package com.olegdvd.grabber.domain;

import java.util.HashMap;
import java.util.Map;

public class SchneiderGatheredData implements GatheredData {

    private final Map<String, Integer> columnMap = new HashMap<String, Integer>() {{
        put(KeysEnum.NAME.getCode(), 2);

    }};

    private final Map<String, String> map;

    public SchneiderGatheredData() {
        this.map = generateMap();
    }

    @Override
    public Map<String, Integer> columnIndexes() {
        return columnMap;
    }

    @Override
    public Map<String, String> gatheringTemplate() {
        return map;
    }
}
