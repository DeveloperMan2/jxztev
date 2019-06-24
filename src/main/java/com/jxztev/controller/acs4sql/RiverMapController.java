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
    public JSONObject riverMapHandler(
            /*@ApiParam(value = "( >= ?)bgTm,起始时间。", name = "bgTm")
            @RequestParam(required = false, name = "bgTm") String bgTm, @ApiParam(value = "( < ?)endTm,截至时间。", name = "endTm")
            @RequestParam(required = false, name = "endTm") String endTm, @ApiParam(value = "( in (?))stations,测站编码列表，以逗号分隔,参数格式为:value1,value2,value3。", name = "stations")
            @RequestParam(required = false, name = "stations") String stations*/) {
        //获取请求参数
        Date bgTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", -1, 0), "yyyy-MM-dd HH:mm:ss");
        Date endTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", 0, 1), "yyyy-MM-dd HH:mm:ss");
        String stations = riverwayStations;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        RiverMapRequest riverMapRequestParams = new RiverMapRequest();
        riverMapRequestParams.setBgTm(formatter.format(bgTm));
        riverMapRequestParams.setEndTm(formatter.format(endTm));

        System.out.println(riverwayStations);
        System.out.println(formatter.format(bgTm));
        System.out.println(formatter.format(endTm));

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






