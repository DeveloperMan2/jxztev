package com.jxztev.service.acs4sql;

import com.jxztev.entity.acs4sql.ReservoirMapRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc ReservoirMap接口
 */
public interface IReservoirMapService {
		    JSONObject reservoirMapHandler(ReservoirMapRequest reservoirMapRequestParams);
        
}

