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

import com.jxztev.service.acs4sql.IIsRiverOverTopWLService;
import com.jxztev.entity.acs4sql.IsRiverOverTopWLResponse;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiParam;
import com.jxztev.entity.acs4sql.IsRiverOverTopWLRequest;

/**
 * @desc：河道是否超过最高水位
 */
@Api(value = "IsRiverOverTopWL", description = "河道是否超过最高水位", tags = "河道地图")
@Controller
@RequestMapping(value = "/isriverovertopwl", method = {RequestMethod.GET, RequestMethod.POST})
public class IsRiverOverTopWLController {
    @Autowired
    @Qualifier("isRiverOverTopWLService")
    private IIsRiverOverTopWLService isRiverOverTopWLService;

    @RequestMapping(value = "/isRiverOverTopWLHandler")
    @ResponseBody
    @ApiOperation(value = "select IsRiverOverTopWLResponse 对象", httpMethod = "GET", notes = "select IsRiverOverTopWLResponse对象", response = IsRiverOverTopWLResponse.class, responseContainer = "List", tags = "河道地图")
    public JSONObject isRiverOverTopWLHandler(
            @ApiParam(value = "( >= ?)bgTm,起始时间。", name = "bgTm")
            @RequestParam(required = false, name = "bgTm") String bgTm, @ApiParam(value = "( < ?)endTm,截至时间。", name = "endTm")
            @RequestParam(required = false, name = "endTm") String endTm, @ApiParam(value = "( in (?))stations,测站编码列表，以逗号分隔,参数格式为:value1,value2,value3。", name = "stations")
            @RequestParam(required = false, name = "stations") String stations) {
        IsRiverOverTopWLRequest isRiverOverTopWLRequestParams = new IsRiverOverTopWLRequest();
        isRiverOverTopWLRequestParams.setBgTm(bgTm);
        isRiverOverTopWLRequestParams.setEndTm(endTm);
        //in/not in子句參數，需要先转化为List，前台输入参数格式为"value1,value2,value3"
        List<String> stationsList = new ArrayList<String>();
        if (null != stations && !stations.equals("")) {
            for (String v : stations.split(",")) {
                stationsList.add(v);
            }
            isRiverOverTopWLRequestParams.setStationsList(stationsList);
        }

        return isRiverOverTopWLService.isRiverOverTopWLHandler(isRiverOverTopWLRequestParams);
    }
}






