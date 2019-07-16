package com.jxztev.controller.acs4sql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxztev.dao.acs4sql.IMemberDao;
import com.jxztev.entity.acs4sql.Member;
import com.jxztev.entity.acs4sql.RiverMapRequest;
import com.jxztev.entity.acs4sql.RiverMapResponse;
import com.jxztev.service.acs4sql.IRiverMapService;
import com.jxztev.utils.DataFormatUtils;
import com.ztev.commons.date.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @desc：RiverMap接口
 */
@Api(value = "RiverMap", description = "RiverMap接口", tags = "河道地图")
@Controller
@RequestMapping(value = "/rivermap", method = {RequestMethod.GET, RequestMethod.POST})
public class RiverMapController {
//    @Autowired
//    @Qualifier("riverMapService")
//    private IRiverMapService riverMapService;

    @Autowired
    @Qualifier("memberDao")
    IMemberDao memberDao;

    @RequestMapping(value = "/riverMapHandler")
    @ResponseBody
    @ApiOperation(value = "select RiverMapResponse 对象", httpMethod = "GET", notes = "select RiverMapResponse对象", response = RiverMapResponse.class, responseContainer = "List", tags = "河道地图")
    public JSONObject riverMapHandler() {
        Member member = memberDao.get("riverMapHandler");
        JSONObject jo  = JSONObject.parseObject(member.getJsonData());
//        System.out.println("查询结果输出："+member.getJsonData());
        return jo;
    }
}






