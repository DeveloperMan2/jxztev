package com.jxztev.service.acs4sql;

import com.jxztev.entity.acs4sql.RiverMapRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc RiverMap接口
 */
public interface IRiverMapService {
    JSONObject riverMapHandler(RiverMapRequest riverMapRequestParams);

}

