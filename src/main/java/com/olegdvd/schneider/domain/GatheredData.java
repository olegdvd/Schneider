package com.olegdvd.schneider.domain;

import java.util.List;
import java.util.Map;

public interface GatheredData {

    List<String> keys();

    Map<String, String> data();
}
