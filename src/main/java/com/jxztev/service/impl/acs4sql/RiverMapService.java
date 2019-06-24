package com.jxztev.service.impl.acs4sql;

import com.alibaba.fastjson.JSON;
import com.jxztev.entity.acs4sql.RiverMapResponse;
import com.jxztev.utils.DataFormatUtils;
import com.ztev.commons.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxztev.service.acs4sql.IRiverMapService;
import com.jxztev.dao.acs4sql.IRiverMapDao;

import com.alibaba.fastjson.JSONArray;
import com.jxztev.entity.acs4sql.RiverMapRequest;


import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @desc RiverMap接口
 */
@Service("riverMapService")
@Transactional(rollbackFor = Exception.class)
public class RiverMapService implements IRiverMapService {
    @Autowired
    @Qualifier("riverMapDao")
    private IRiverMapDao riverMapDao;

    public JSONObject riverMapHandler(RiverMapRequest riverMapRequestParams) {
        JSONObject jo = new JSONObject();
        try {
            //组装页面数据模型
            Map<String, String> mapSS = new HashMap();
            mapSS.put("4", "↓");
            mapSS.put("5", "↑");
            mapSS.put("6", "—");
            JSONArray listJson = (JSONArray) JSONObject.toJSON(riverMapDao.riverMapHandler(riverMapRequestParams));
            List<RiverMapResponse> list = listJson.toJavaList(RiverMapResponse.class);
            for (int i = 0; i < list.size(); i++)
            {
                RiverMapResponse tMap = list.get(i);
                try {
                    tMap.setStnm(tMap.getStnm().trim());
                    /*tMap.put("Z", DataFormatUtils.getRoundString(tMap.get("Z"), 2));
                    tMap.put("Q", DataFormatUtils.getValidString(tMap.get("Q"), 3));
                    tMap.put("WRZ", DataFormatUtils.getRoundString(tMap.get("WRZ"), 2));
                    tMap.put("OBHTZ", DataFormatUtils.getRoundString(tMap.get("OBHTZ"), 2));
                    tMap.put("HTM", DateUtils.formatDate((Date)tMap.get("TM"), "H点"));
                    tMap.put("TM", DateUtils.formatDate((Date)tMap.get("TM"), "M月d日 H点m分"));
                    tMap.put("style", "normal");
                    tMap.put("img", "normal.gif");

                    tMap.put("WPTN", mapSS.get(tMap.get("WPTN")));

                    if (tMap.get("CWRZ") != null)
                    {
                        double CWRZ = DataFormatUtils.getRound(tMap.get("CWRZ"), 2).doubleValue();
                        tMap.put("CWRZ", Double.valueOf(CWRZ));

                        if (CWRZ >= 0.0D) {
                            tMap.put("style", "warn");
                            tMap.put("img", "alarm.gif");
                        }
                    }*/
                }
                catch (Exception e) {
                    System.out.println(e);
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
                //todo 待确认 HTM 是否就是TM
                sbf.append(tMap.getCounty())
                        .append(" <font color='red'>").append(tMap.getStnm()).append("站</font>")
                        .append(tMap.getTm()).append("超警戒水位")
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
                            .append(tMap.getCounty()).append(" ").append(tMap.getStnm()).append("站");
                    if (tMap.getCwrz() != null) {
                        sbf.append("，").append(tMap.getTm()).append("比警戒水位").append(tMap.getCwrz()).append("米").append("。");
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

    public List getOverTopWrz(List list) {
        List overTopWrz = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            RiverMapResponse tMap = (RiverMapResponse) list.get(i);
            try {
                if (tMap.getCwrz() != null) {
                    double CWRZ = Double.valueOf(tMap.getCwrz().toString()).doubleValue();
                    if (CWRZ >= 0.0D) {
                        overTopWrz.add(tMap);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return overTopWrz;
    }
}

