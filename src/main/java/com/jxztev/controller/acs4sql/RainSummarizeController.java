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

import com.jxztev.service.acs4sql.IRainSummarizeService;
import com.jxztev.entity.acs4sql.RainSummarizeResponse;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiParam;
import com.jxztev.entity.acs4sql.RainSummarizeRequest;

/**
 * @desc：RainSummarize接口
 */
@Api(value = "RainSummarize", description = "RainSummarize接口", tags = "水雨情概述")
@Controller
@RequestMapping(value = "/rainsummarize", method = {RequestMethod.GET, RequestMethod.POST})
public class RainSummarizeController {
    @Autowired
    @Qualifier("rainSummarizeService")
    private IRainSummarizeService rainSummarizeService;

    @RequestMapping(value = "/rainSummarizeHandler")
    @ResponseBody
    @ApiOperation(value = "select RainSummarizeResponse 对象", httpMethod = "GET", notes = "select RainSummarizeResponse对象", response = RainSummarizeResponse.class, responseContainer = "List", tags = "水雨情概述")
    public JSONObject rainSummarizeHandler() {
        return rainSummarizeService.rainSummarizeHandler();
    }
}






