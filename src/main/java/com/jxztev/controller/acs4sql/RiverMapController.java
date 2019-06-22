package com.jxztev.controller.acs4sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import com.alibaba.fastjson.JSONObject;

import com.jxztev.service.acs4sql.IRiverMapService;
import com.jxztev.entity.acs4sql.RiverMapResponse;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiParam;
import com.jxztev.entity.acs4sql.RiverMapRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc：RiverMap接口
 */
@Api(value = "RiverMap", description = "RiverMap接口", tags = "河道地图")
@Controller
@RequestMapping(value = "/rivermap", method = {RequestMethod.GET, RequestMethod.POST})
public class RiverMapController {
    @Autowired
    @Qualifier("riverMapService")
    private IRiverMapService riverMapService;

    @RequestMapping(value = "/riverMapHandler")
    @ResponseBody
    @ApiOperation(value = "select RiverMapResponse 对象", httpMethod = "GET", notes = "select RiverMapResponse对象", response = RiverMapResponse.class, responseContainer = "List", tags = "河道地图")
    public JSONObject riverMapHandler(
            @ApiParam(value = "( >= ?)bgTm,起始时间。", name = "bgTm")
            @RequestParam(required = false, name = "bgTm") String bgTm, @ApiParam(value = "( < ?)endTm,截至时间。", name = "endTm")
            @RequestParam(required = false, name = "endTm") String endTm, @ApiParam(value = "( in (?))stations,测站编码列表，以逗号分隔,参数格式为:value1,value2,value3。", name = "stations")
            @RequestParam(required = false, name = "stations") String stations) {
        RiverMapRequest riverMapRequestParams = new RiverMapRequest();
        riverMapRequestParams.setBgTm(bgTm);
        riverMapRequestParams.setEndTm(endTm);
        List<String> stationsList = new ArrayList<String>();
        if (null != stations && !stations.equals("")) {
            for (String v : stations.split(",")) {
                stationsList.add(v);
            }
            riverMapRequestParams.setStationsList(stationsList);
        }

        return riverMapService.riverMapHandler(riverMapRequestParams);
    }
}






