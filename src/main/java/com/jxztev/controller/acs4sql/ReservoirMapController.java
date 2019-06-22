package com.jxztev.controller.acs4sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import com.jxztev.service.acs4sql.IReservoirMapService;
import com.jxztev.entity.acs4sql.ReservoirMapResponse;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiParam;
import com.jxztev.entity.acs4sql.ReservoirMapRequest;

/**
 * @desc：ReservoirMap接口
 */
@Api(value = "ReservoirMap", description = "ReservoirMap接口", tags = "水库地图")
@Controller
@RequestMapping(value = "/reservoirmap", method = {RequestMethod.GET, RequestMethod.POST})
public class ReservoirMapController {
    @Autowired
    @Qualifier("reservoirMapService")
    private IReservoirMapService reservoirMapService;

    @RequestMapping(value = "/reservoirMapHandler")
    @ResponseBody
    @ApiOperation(value = "select ReservoirMapResponse 对象", httpMethod = "GET", notes = "select ReservoirMapResponse对象", response = ReservoirMapResponse.class, responseContainer = "List", tags = "水库地图")
    public JSONObject reservoirMapHandler(
            @ApiParam(value = "(?)md,月日，如0608。", name = "md")
            @RequestParam(required = false, name = "md") String md,
            @ApiParam(value = "( >= ?)bgTm,起始时间。", name = "bgTm")
            @RequestParam(required = false, name = "bgTm") String bgTm,
            @ApiParam(value = "( < ?)endTm,截至时间。", name = "endTm")
            @RequestParam(required = false, name = "endTm") String endTm,
            @ApiParam(value = "( in (?))stations,测站编码列表，以逗号分隔,参数格式为:value1,value2,value3。", name = "stations")
            @RequestParam(required = false, name = "stations") String stations) {
        ReservoirMapRequest reservoirMapRequestParams = new ReservoirMapRequest();
        reservoirMapRequestParams.setMd(md);
        reservoirMapRequestParams.setBgTm(bgTm);
        reservoirMapRequestParams.setEndTm(endTm);
        //in/not in子句參數，需要先转化为List，前台输入参数格式为"value1,value2,value3"
        List<String> stationsList = new ArrayList<String>();
        if (null != stations && !stations.equals("")) {
            for (String v : stations.split(",")) {
                stationsList.add(v);
            }
            reservoirMapRequestParams.setStationsList(stationsList);
        }

        return reservoirMapService.reservoirMapHandler(reservoirMapRequestParams);
    }
}






