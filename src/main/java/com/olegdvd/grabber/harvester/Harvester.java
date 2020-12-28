package com.olegdvd.grabber.harvester;

import com.olegdvd.grabber.domain.GatheredData;

public interface Harvester {

    GatheredData request(String id);

    int getDataColumnNumber();     // Number of column in Excel sheet where article (matherialId) is stored. (0-based)
}
