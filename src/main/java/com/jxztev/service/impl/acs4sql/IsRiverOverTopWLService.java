package com.jxztev.service.impl.acs4sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxztev.service.acs4sql.IIsRiverOverTopWLService;
import com.jxztev.dao.acs4sql.IIsRiverOverTopWLDao;

	    import com.alibaba.fastjson.JSONArray;
		    import com.jxztev.entity.acs4sql.IsRiverOverTopWLRequest;
	

import com.alibaba.fastjson.JSONObject;

/**
* @desc 河道是否超过最高水位
*/
@Service("isRiverOverTopWLService")
@Transactional(rollbackFor = Exception.class)
public class IsRiverOverTopWLService implements IIsRiverOverTopWLService {
@Autowired
@Qualifier("isRiverOverTopWLDao")
private IIsRiverOverTopWLDao isRiverOverTopWLDao;

	    public JSONObject isRiverOverTopWLHandler(IsRiverOverTopWLRequest isRiverOverTopWLRequestParams){
    JSONObject jo = new JSONObject();
    try {
    jo.put("data", JSONObject.toJSON(isRiverOverTopWLDao.isRiverOverTopWLHandler(isRiverOverTopWLRequestParams)));
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

