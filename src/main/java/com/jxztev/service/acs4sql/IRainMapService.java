package com.jxztev.service.acs4sql;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc RiverMap接口
 */
public interface IRainMapService {
    JSONObject getRainData(String hourStr);
}

