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
    public JSONObject reservoirMapHandler() {
        JSONObject jo = new JSONObject();
        reservoirMapService.queryReservoirMapList();
        return jo;
    }
}






