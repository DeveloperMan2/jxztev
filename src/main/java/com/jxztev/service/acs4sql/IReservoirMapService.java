package com.jxztev.service.acs4sql;

import com.jxztev.entity.acs4sql.ReservoirMapRequest;

import com.alibaba.fastjson.JSONObject;
import com.jxztev.entity.acs4sql.ReservoirMapResponse;

import java.util.List;

/**
 * @desc ReservoirMap接口
 */
public interface IReservoirMapService {
	List<ReservoirMapResponse> queryReservoirMapList();
	List<ReservoirMapResponse> getOverTopFLZ(List<ReservoirMapResponse> list);
        
}

