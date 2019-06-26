package com.jxztev.controller.acs4sql;

import com.alibaba.fastjson.JSONObject;
import com.jxztev.entity.acs4sql.RainSummarizeResponse;
import com.jxztev.service.acs4sql.IRainMapService;
import com.jxztev.service.acs4sql.IRainSummarizeService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/rain", method = {RequestMethod.GET, RequestMethod.POST})
public class RainMapController {
    @Autowired
    @Qualifier("rainMapService")
    private IRainMapService rainMapService;

    @RequestMapping(value = "/rainquery")
    @ResponseBody
    public JSONObject rainQueryHandler(@RequestParam(required = false, name = "hour") String hour) {
        JSONObject jo = new JSONObject();

        String queryHour = hour == null ? "24" : hour;

        int INT_HOUR = 0;
        try{
            INT_HOUR = Integer.parseInt(queryHour);
        }catch(Exception e){
            jo.put("status", 0);// 1-成功， 0-失败
            jo.put("msg", "时长参数异常");
        }

        //取出县代码、县名、最大雨量、平均雨量
        List<RainSummarizeResponse> countRainList = rainMapService.getCountyRainList(String.valueOf(INT_HOUR));
        List<RainSummarizeResponse> maxRainList = rainMapService.getMaxRainOrderRain(String.valueOf(INT_HOUR), 20);

        String bgTM = DateFormatUtils.format(DateUtils.addHours(new Date(), -INT_HOUR), "M月d日H点");
        String endTM = DateFormatUtils.format(new Date(), "d日H点");//结束时间
        String queryTm = bgTM + "~" + endTM;

        JSONObject data = new JSONObject();
        data.put("countRainList",countRainList);
        data.put("maxRainList", maxRainList);
        data.put("queryTm",queryTm);

        jo.put("data",data);
        jo.put("status", 1);// 1-成功， 0-失败
        jo.put("msg", "执行成功");
        return jo;
    }
}
