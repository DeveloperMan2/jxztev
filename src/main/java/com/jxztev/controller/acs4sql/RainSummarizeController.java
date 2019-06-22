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
@Api(value = "RainSummarize", description = "水雨情概述", tags = "水雨情")
@Controller
@RequestMapping(value = "/rainsummarize", method = {RequestMethod.GET, RequestMethod.POST})
public class RainSummarizeController {
    @Autowired
    @Qualifier("rainSummarizeService")
    private IRainSummarizeService rainSummarizeService;

    @RequestMapping(value = "/rainSummarizeHandler")
    @ResponseBody
    @ApiOperation(value = "select RainSummarizeResponse 对象", httpMethod = "GET", notes = "select RainSummarizeResponse对象", response = RainSummarizeResponse.class, responseContainer = "List", tags = "水雨情")
    public JSONObject rainSummarizeHandler(
            @ApiParam(value = "( > ?)tm,传入时间格式：yyyy-mm-dd hh:mi:ss。", name = "tm")
            @RequestParam(required = false, name = "tm") String tm, @ApiParam(value = "( > ?)maxrain,传入雨量数值。", name = "maxrain")
            @RequestParam(required = false, name = "maxrain") String maxrain) {
        RainSummarizeRequest rainSummarizeRequestParams = new RainSummarizeRequest();
        rainSummarizeRequestParams.setTm(tm);
        rainSummarizeRequestParams.setMaxrain(maxrain);
        return rainSummarizeService.rainSummarizeHandler(rainSummarizeRequestParams);
    }
}






