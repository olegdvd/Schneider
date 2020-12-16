package com.olegdvd.schneider.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DanfossGatheredData implements GatheredData {

    private final List<String> keysList = Arrays.asList("name", "url", "price", "href");
    private final Map<String, String> map;

    public DanfossGatheredData() {
        this.map = keysList.stream()
        .collect(Collectors.groupingBy(String::toString), toString());
    }

    @Override
    public List<String> keys() {
        return keysList;
    }

    @Override
    public Map<String, String> data() {
        return map;
    }
}
