package com.jxztev.service.impl.acs4sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxztev.service.acs4sql.IFindSlideRainService;
import com.jxztev.dao.acs4sql.IFindSlideRainDao;

	    import com.alibaba.fastjson.JSONArray;
		    import com.jxztev.entity.acs4sql.FindSlideRainRequest;
	

import com.alibaba.fastjson.JSONObject;

/**
* @desc FindSlideRain接口
*/
@Service("findSlideRainService")
@Transactional(rollbackFor = Exception.class)
public class FindSlideRainService implements IFindSlideRainService {
@Autowired
@Qualifier("findSlideRainDao")
private IFindSlideRainDao findSlideRainDao;

	    public JSONObject findSlideRainHandler(FindSlideRainRequest findSlideRainRequestParams){
    JSONObject jo = new JSONObject();
    try {
    jo.put("data", JSONObject.toJSON(findSlideRainDao.findSlideRainHandler(findSlideRainRequestParams)));
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

