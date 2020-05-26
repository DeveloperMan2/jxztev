package com.jxztev.service.impl.acs4sql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxztev.entity.acs4sql.RiverMapResponse;
import com.jxztev.service.acs4sql.IRiverMapService;
import com.jxztev.utils.DataFormatUtils;
import com.jxztev.utils.HttpUtils;
import com.ztev.commons.date.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @desc RiverMap接口
 */
@Service("riverMapService")
@Transactional(rollbackFor = Exception.class)
public class RiverMapService implements IRiverMapService {

    //河道水情请求地址
    @Value("#{systemConfig[river_map_service]}")
    private String riverMapServiceUrl;


    public List<RiverMapResponse> getOverTopWrz(List<RiverMapResponse> list) {
        List<RiverMapResponse> overTopWrz = new ArrayList<>();
        for (RiverMapResponse riverMapResponse : list) {
            try {
                if (riverMapResponse.getCwrz() != null) {
                    double CWRZ = Double.valueOf(riverMapResponse.getCwrz()).doubleValue();
                    if (CWRZ >= 0.0D) {
                        overTopWrz.add(riverMapResponse);
                    }
                }
            } catch (Exception e) {
            }
        }
        return overTopWrz;
    }


    public JSONObject riverMapHandler() {

        JSONObject jo = new JSONObject();
        try {
            //组装页面数据模型
            Map<String, String> mapSS = new HashMap();
            mapSS.put("4", "↓");
            mapSS.put("5", "↑");
            mapSS.put("6", "—");
            JSONObject result = HttpUtils.doGet(riverMapServiceUrl);
            JSONArray listJson = result.getJSONArray("data");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
            List<RiverMapResponse> list = listJson.toJavaList(RiverMapResponse.class);
            for (int i = 0; i < list.size(); i++) {
                RiverMapResponse tMap = list.get(i);
                try {
                    tMap.setStnm(tMap.getStnm().trim());
                    tMap.setZ(DataFormatUtils.getRoundString(tMap.getZ(), 2));
                    tMap.setQ(DataFormatUtils.getValidString(tMap.getQ(), 3));
                    tMap.setWrz(DataFormatUtils.getRoundString(tMap.getWrz(), 2));
                    tMap.setObhtz(DataFormatUtils.getRoundString(tMap.getObhtz(), 2));
                    //必须捕获异常
                    try {
                        if (tMap.getTm() != null) {
                            String tm = tMap.getTm();
                            String hour = tm.substring(tm.indexOf("日")+1,tm.indexOf("点")+1);
                            tMap.setHTM(hour);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tMap.setStyle("normal");
                    tMap.setImg("normal.gif");

//                    tMap.setWptn(tMap.getWptn());

                    if (tMap.getCwrz() != null) {
                        double CWRZ = DataFormatUtils.getRound(tMap.getCwrz(), 2).doubleValue();
                        tMap.setCwrz(String.valueOf(CWRZ));

                        if (CWRZ >= 0.0D) {
                            tMap.setStyle("warn");
                            tMap.setImg("alarm.gif");
                        }
                    }
                } catch (Exception e) {
                    // System.out.println(e);
                }
            }

            JSONObject htmlModel = new JSONObject();
            htmlModel.put("rows", JSON.toJSON(list));
            /** 左上显示的站点**/
            htmlModel.put("upCodes", "62304150");
            /** 超警戒的测站信息**/
            List overTopWrz = this.getOverTopWrz(list);
            htmlModel.put("overTopWrz", JSON.toJSON(overTopWrz));
            /** 超警戒的测站数量**/
            htmlModel.put("overTopWrzSize", overTopWrz.size());
            String summarize = "";//保存河道水情概述信息
            StringBuffer sbf = new StringBuffer();
            for (int i = 0; i < overTopWrz.size(); i++) {
                RiverMapResponse tMap = (RiverMapResponse) overTopWrz.get(i);
                if (i > 0) {
                    sbf.append("；");//不是第一个则加；分隔
                }
                sbf.append(tMap.getCounty() == null ? "" : tMap.getCounty())
                        .append(" <font color='red'>").append(tMap.getStnm()).append("站</font>")
                        .append(tMap.getHTM()).append("超警戒水位")
                        .append("<font color='red'>").append(tMap.getCwrz()).append("</font>").append("米");
            }
            if (overTopWrz.size() == 1) {
                summarize = sbf.toString();
            } else if (overTopWrz.size() > 1) {
                summarize = "有<font color='red'>" + overTopWrz.size() + "</font>个站超警戒，如下";
            } else {
                if (list.size() > 0) {
                    RiverMapResponse tMap = list.get(0);
                    sbf = new StringBuffer();
                    sbf.append("各江河重点站均在警戒水位以下，其中离警戒水位最近的是")
                            .append(tMap.getCounty() == null ? "" : tMap.getCounty()).append(" ").append(tMap.getStnm()).append("站");
                    if (tMap.getCwrz() != null) {
                        sbf.append("，").append(tMap.getHTM()).append("比警戒水位").append(tMap.getCwrz()).append("米").append("。");
                    }
                    summarize = sbf.toString();
                } else {
                    summarize = "系统暂无河道水情数据！";
                }
            }
            summarize = DateUtils.getTodayString("M月d日") + summarize;

            /**设置水库水情概述 **/
            htmlModel.put("summarize", summarize);
            jo.put("data", htmlModel);

            jo.put("status", 1);// 1-成功， 0-失败
            jo.put("msg", "执行成功");
        } catch (Exception e) {
            jo.put("data", null);
            jo.put("status", 0);// 1-成功， 0-失败
            jo.put("msg", e.getMessage());// msg-失败信息
        }
        return jo;
    }

}

