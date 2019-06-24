package com.jxztev.controller.acs4sql;

import com.alibaba.fastjson.JSONObject;
import com.jxztev.entity.acs4sql.RiverMapRequest;
import com.jxztev.entity.acs4sql.RiverMapResponse;
import com.jxztev.service.acs4sql.IRiverMapService;
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

import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @desc：RiverMap接口
 */
@Api(value = "RiverMap", description = "RiverMap接口", tags = "河道地图")
@Controller
@RequestMapping(value = "/rivermap", method = {RequestMethod.GET, RequestMethod.POST})
public class RiverMapController {
    //河道包含的站点
    @Value("#{systemConfig[riverway_stations]}")
    private String riverwayStations;

    //水库包含的站点
    @Value("#{systemConfig[reservoir_stations]}")
    private String reservoirStations;

    @Autowired
    @Qualifier("riverMapService")
    private IRiverMapService riverMapService;

    @RequestMapping(value = "/riverMapHandler")
    @ResponseBody
    @ApiOperation(value = "select RiverMapResponse 对象", httpMethod = "GET", notes = "select RiverMapResponse对象", response = RiverMapResponse.class, responseContainer = "List", tags = "河道地图")
    public JSONObject riverMapHandler() {
        //获取请求参数
        JSONObject jo = new JSONObject();
         riverMapService.queryRiverMapList();
        return jo;
    }
}






