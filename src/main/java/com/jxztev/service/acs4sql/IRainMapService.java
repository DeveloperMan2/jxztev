package com.jxztev.service.acs4sql;

import com.jxztev.entity.acs4sql.RainSummarizeResponse;
import com.jxztev.entity.acs4sql.RiverMapResponse;

import java.util.List;

/**
 * @desc RiverMap接口
 */
public interface IRainMapService {
    List<RainSummarizeResponse> getCountyRainList(String hourStr);
    List<RainSummarizeResponse> getStationRainList();
    List<RainSummarizeResponse> getMaxRainOrderRain(String hourStr, Integer countNum);

}

