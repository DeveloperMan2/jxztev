package com.jxztev.service.acs4sql;

import com.jxztev.entity.acs4sql.RiverMapRequest;

import com.alibaba.fastjson.JSONObject;
import com.jxztev.entity.acs4sql.RiverMapResponse;

import java.util.List;

/**
 * @desc RiverMap接口
 */
public interface IRiverMapService {
    List<RiverMapResponse> queryRiverMapList();

    List<RiverMapResponse> getOverTopWrz(List<RiverMapResponse> list);

    JSONObject riverMapHandler();
}

