package com.jxztev.service.impl.acs4sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxztev.service.acs4sql.IRainSummarizeService;
import com.jxztev.dao.acs4sql.IRainSummarizeDao;

import com.alibaba.fastjson.JSONArray;
import com.jxztev.entity.acs4sql.RainSummarizeRequest;


import com.alibaba.fastjson.JSONObject;

/**
 * @desc RainSummarize接口
 */
@Service("rainSummarizeService")
@Transactional(rollbackFor = Exception.class)
public class RainSummarizeService implements IRainSummarizeService {
    @Autowired
    @Qualifier("rainSummarizeDao")
    private IRainSummarizeDao rainSummarizeDao;

    public JSONObject rainSummarizeHandler(RainSummarizeRequest rainSummarizeRequestParams) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("data", JSONObject.toJSON(rainSummarizeDao.rainSummarizeHandler(rainSummarizeRequestParams)));
            jo.put("status", 1);// 1-成功， 0-失败
            jo.put("msg", "执行成功");
        } catch (Exception e) {
            jo.put("data", null);
            jo.put("status", 0);// 1-成功， 0-失败
            jo.put("msg", e.getMessage());// msg-失败信息
        }

        return jo;

    }
}

