package com.jxztev.service.acs4sql;

import com.jxztev.entity.acs4sql.IsRiverOverTopWLRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc 河道是否超过最高水位
 */
public interface IIsRiverOverTopWLService {
		    JSONObject isRiverOverTopWLHandler(IsRiverOverTopWLRequest isRiverOverTopWLRequestParams);
        
}

