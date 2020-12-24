package com.olegdvd.grabber.harvester;

import com.olegdvd.grabber.domain.GatheredData;

public interface Harvester {

    GatheredData request(String id);

}
