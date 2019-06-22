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

import com.jxztev.service.acs4sql.IFindSlideRainService;
import com.jxztev.entity.acs4sql.FindSlideRainResponse;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiParam;
import com.jxztev.entity.acs4sql.FindSlideRainRequest;

/**
 * @desc：FindSlideRain接口
 */
@Api(value = "FindSlideRain", description = "FindSlideRain接口", tags = "水雨情")
@Controller
@RequestMapping(value = "/findsliderain", method = {RequestMethod.GET, RequestMethod.POST})
public class FindSlideRainController {
    @Autowired
    @Qualifier("findSlideRainService")
    private IFindSlideRainService findSlideRainService;

    @RequestMapping(value = "/findSlideRainHandler")
    @ResponseBody
    @ApiOperation(value = "select FindSlideRainResponse 对象", httpMethod = "GET", notes = "select FindSlideRainResponse对象", response = FindSlideRainResponse.class, responseContainer = "List", tags = "水雨情")
    public JSONObject findSlideRainHandler(
            @ApiParam(value = "( = ?)hours,小时。", name = "hours")
            @RequestParam(required = false, name = "hours") Integer hours,
            @ApiParam(value = "( = ?)period,周期。", name = "period")
            @RequestParam(required = false, name = "period") Integer period,
            @ApiParam(value = "( > ?)drp。", name = "drp")
            @RequestParam(required = false, name = "drp") float drp) {
        FindSlideRainRequest findSlideRainRequestParams = new FindSlideRainRequest();
        findSlideRainRequestParams.setHours(hours);
        findSlideRainRequestParams.setPeriod(period);
        findSlideRainRequestParams.setDrp(drp);
        return findSlideRainService.findSlideRainHandler(findSlideRainRequestParams);
    }
}






