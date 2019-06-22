package com.jxztev.service.impl.acs4sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxztev.service.acs4sql.IIsReservoirOverTopFLZService;
import com.jxztev.dao.acs4sql.IIsReservoirOverTopFLZDao;

	    import com.alibaba.fastjson.JSONArray;
		    import com.jxztev.entity.acs4sql.IsReservoirOverTopFLZRequest;
	

import com.alibaba.fastjson.JSONObject;

/**
* @desc IsReservoirOverTopFLZ接口
*/
@Service("isReservoirOverTopFLZService")
@Transactional(rollbackFor = Exception.class)
public class IsReservoirOverTopFLZService implements IIsReservoirOverTopFLZService {
@Autowired
@Qualifier("isReservoirOverTopFLZDao")
private IIsReservoirOverTopFLZDao isReservoirOverTopFLZDao;

	    public JSONObject isReservoirOverTopFLZHandler(IsReservoirOverTopFLZRequest isReservoirOverTopFLZRequestParams){
    JSONObject jo = new JSONObject();
    try {
    jo.put("data", JSONObject.toJSON(isReservoirOverTopFLZDao.isReservoirOverTopFLZHandler(isReservoirOverTopFLZRequestParams)));
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

