package com.jxztev.controller.acs4sql;

import com.jxztev.dao.acs4sql.IMemberDao;
import com.jxztev.entity.acs4sql.Member;
import com.ztev.commons.date.DateUtils;
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
//    @Autowired
//    @Qualifier("reservoirMapService")
//    private IReservoirMapService reservoirMapService;

    @Autowired
    @Qualifier("memberDao")
    IMemberDao memberDao;

    @RequestMapping(value = "/reservoirMapHandler")
    @ResponseBody
    @ApiOperation(value = "select ReservoirMapResponse 对象", httpMethod = "GET", notes = "select ReservoirMapResponse对象", response = ReservoirMapResponse.class, responseContainer = "List", tags = "水库地图")
    public JSONObject reservoirMapHandler() {
//        JSONObject jo = new JSONObject();
//        try{
//
//        List<ReservoirMapResponse> reservoirList = reservoirMapService.queryReservoirMapList();
//        List<ReservoirMapResponse> overTopFLZ = reservoirMapService.getOverTopFLZ(reservoirList);
//        //组装水库地图数据模型
//
//        String summarize;//保存水库水情概述信息
//        StringBuffer sbf = new StringBuffer();
//        for( int i = 0; i < overTopFLZ.size(); i++ ){
//            ReservoirMapResponse tMap = overTopFLZ.get(i);
//            if(i > 0){
//                sbf.append("；");//不是第一个则加；分隔
//            }
//            sbf.append(tMap.getCounty() == null ? "" : tMap.getCounty())
//                    .append(" <font color='red'>").append(tMap.getStnm()).append("水库</font>")
//                    .append(tMap.getHTM()).append("超汛限水位")
//                    .append("<font color='red'>").append(tMap.getCfsltdz()).append("</font>").append("米");
//        }
//        if( overTopFLZ.size() == 1 ){
//            summarize = sbf.toString();
//        }else if( overTopFLZ.size() > 1 ){
//            summarize = "有<font color='red'>" + overTopFLZ.size() + "</font>个水库超汛限，分别是：" + sbf.toString() ;
//        }else{
//            if(reservoirList.size() > 0){
//                ReservoirMapResponse tMap = reservoirList.get(0);
//                sbf = new StringBuffer();
//                sbf.append("各大中型水库均在汛限水位以下，其中离汛限水位最近的是")
//                        .append(tMap.getCounty() == null ? "" : tMap.getCounty()).append(" ").append(tMap.getStnm()).append("水库");
//                if( tMap.getCfsltdz() != null && tMap.getStnm() != "三峡"){
//                    sbf.append("，").append(tMap.getHTM()).append("比汛限水位").append(tMap.getCfsltdz()).append("米");
//                }
//                summarize = sbf.toString();
//            }else{
//                summarize = "系统暂无水库水情数据";
//            }
//        }
//        summarize = DateUtils.getTodayString("M月d日") + summarize + "。" ;
//
//        JSONObject data = new JSONObject();
//        data.put("rows",reservoirList);
//        data.put("overTopFLZ",overTopFLZ);
//        data.put("overTopFLZSize",overTopFLZ.size());
//        data.put("upCodes","");
//        data.put("summarize",summarize);
//        jo.put("data",data);
//        jo.put("status", 1);// 1-成功， 0-失败
//        jo.put("msg", "执行成功");
//        }catch (Exception e){
//            jo.put("status", 0);// 1-成功， 0-失败
//            jo.put("msg", "执行失败");
//        }

        Member member = memberDao.get("reservoirMapHandler");
        JSONObject jo  = JSONObject.parseObject(member.getJsonData());
//        System.out.println("查询结果输出："+member.getJsonData());
        return jo;
    }
}






