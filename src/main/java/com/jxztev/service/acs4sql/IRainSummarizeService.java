package com.jxztev.service.acs4sql;

import com.jxztev.entity.acs4sql.RainSummarizeRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc RainSummarize接口
 */
public interface IRainSummarizeService {
    JSONObject rainSummarizeHandler(RainSummarizeRequest rainSummarizeRequestParams);

}

