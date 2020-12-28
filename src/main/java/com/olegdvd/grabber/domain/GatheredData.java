package com.olegdvd.grabber.domain;

import java.util.Map;
import java.util.stream.Collectors;

public interface GatheredData {

    /**
     * Returns mapping of predefined names to Excel column indexes (0-based)
     * @see KeysEnum
     *
     *
     * @return defined map of type Map.class
     */
    Map<String, Integer> columnIndexes();

    /**
     * Returns template for padding with gathered values
     *
     * @return
     */
    Map<String, String> gatheringTemplate();

    default Map<String, String> generateMap(){
        return columnIndexes().keySet().stream()
                .collect(Collectors.toMap(String::toString, s -> ""));
    }
}