package com.jxztev.service.impl.acs4sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxztev.service.acs4sql.IRiverMapService;
import com.jxztev.dao.acs4sql.IRiverMapDao;

	    import com.alibaba.fastjson.JSONArray;
		    import com.jxztev.entity.acs4sql.RiverMapRequest;
	

import com.alibaba.fastjson.JSONObject;

/**
* @desc RiverMap接口
*/
@Service("riverMapService")
@Transactional(rollbackFor = Exception.class)
public class RiverMapService implements IRiverMapService {
@Autowired
@Qualifier("riverMapDao")
private IRiverMapDao riverMapDao;

	    public JSONObject riverMapHandler(RiverMapRequest riverMapRequestParams){
    JSONObject jo = new JSONObject();
    try {
    jo.put("data", JSONObject.toJSON(riverMapDao.riverMapHandler(riverMapRequestParams)));
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

