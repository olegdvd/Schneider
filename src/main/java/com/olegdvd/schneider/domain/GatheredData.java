package com.olegdvd.schneider.domain;

import java.util.Map;
import java.util.stream.Collectors;

public interface GatheredData {

    Map<String, Integer> keysMap();

    Map<String, String> data();

    default Map<String, String> generateMap(){
        return keysMap().keySet().stream()
                .collect(Collectors.toMap(String::toString, s -> ""));
    }
}