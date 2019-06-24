package com.jxztev.service.impl.acs4sql;

import com.jxztev.entity.acs4sql.RainSummarizeResponse;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxztev.service.acs4sql.IRainSummarizeService;
import com.jxztev.dao.acs4sql.IRainSummarizeDao;

import com.alibaba.fastjson.JSONArray;
import com.jxztev.entity.acs4sql.RainSummarizeRequest;


import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @desc RainSummarize接口
 */
@Service("rainSummarizeService")
@Transactional(rollbackFor = Exception.class)
public class RainSummarizeService implements IRainSummarizeService {
    public static Integer DEFAULT_PERIOD = Integer.valueOf(24);
    public static String JAVA_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String ORACLE_DATE_PATTERN = "yyyy-mm-dd hh24:mi:ss";
    public static Float DEFAULT_RAIN_FLAG = Float.valueOf(50.0F);

    @Autowired
    @Qualifier("rainSummarizeDao")
    private IRainSummarizeDao rainSummarizeDao;

    public JSONObject rainSummarizeHandler() {
        JSONObject jo = new JSONObject();
        try {
            //获取雨情概述
            String rain = getRainSummary("24");
            //获取水库水情
            //获取河道水情
            JSONObject data = new JSONObject();
            data.put("rain",rain);
            jo.put("data", data);
            jo.put("status", 1);// 1-成功， 0-失败
            jo.put("msg", "执行成功");
        } catch (Exception e) {
            jo.put("data", null);
            jo.put("status", 0);// 1-成功， 0-失败
            jo.put("msg", e.getMessage());// msg-失败信息
        }
        return jo;
    }

    public List<RainSummarizeResponse> getCountyRainList(String hourStr) {

        long bt = System.currentTimeMillis();
        List<RainSummarizeResponse> list = new ArrayList();
        String bgDate = null;
        int INT_HOUR = DEFAULT_PERIOD.intValue();
        try {
            INT_HOUR = Integer.parseInt(hourStr);
        } catch (Exception localException1) {
        }
        try {
           // bgDate =  DateFormatUtils.format(new Date(System.currentTimeMillis() - 3600000 * INT_HOUR), JAVA_DATE_PATTERN);
            bgDate =  "2019-06-15 08:00:00";
            RainSummarizeRequest rainSummarizeRequestParams = new RainSummarizeRequest();
            rainSummarizeRequestParams.setTm(bgDate);
            rainSummarizeRequestParams.setMaxrain(0f);
            list = rainSummarizeDao.rainSummarizeHandler(rainSummarizeRequestParams);
        } catch (Exception e) {
        }
        return list;
    }

    public Boolean isRainOver() {
        if (getStationRainList().size() > 0) return Boolean.valueOf(true);
        return Boolean.valueOf(false);
    }

    public List<RainSummarizeResponse> getStationRainList() {
        String hourStr = "1";
        long bt = System.currentTimeMillis();
        List<RainSummarizeResponse> list = new ArrayList();
        String bgDate = null;
        int INT_HOUR = DEFAULT_PERIOD.intValue();
        try {
            INT_HOUR = Integer.parseInt(hourStr);
        } catch (Exception localException1) {
        }
        float rainFlag = 0;
        if (rainFlag == 0) {
            rainFlag = DEFAULT_RAIN_FLAG;
        }
        try {
            bgDate = DateFormatUtils.format(new Date(System.currentTimeMillis() - 3600000 * INT_HOUR), JAVA_DATE_PATTERN);
            RainSummarizeRequest rainSummarizeRequestParams = new RainSummarizeRequest();
            rainSummarizeRequestParams.setTm(bgDate);
            rainSummarizeRequestParams.setMaxrain(rainFlag);
            list = rainSummarizeDao.rainSummarizeHandler(rainSummarizeRequestParams);
        } catch (Exception e) {
        }
        return list;
    }

    public List<RainSummarizeResponse> getMaxRainOrderRain(String hourStr, Integer countNum) {
        List<RainSummarizeResponse> list = new ArrayList();
        Map<String, Integer> tmpMap = new HashMap();
        List<RainSummarizeResponse> rawList = getStationRainList();
        RainSummarizeResponse countyRainValue = null;
        for (int i = 0; i < rawList.size(); i++) {
            countyRainValue = (RainSummarizeResponse) rawList.get(i);
            String countyname = "未知";
            if (countyRainValue.getCnnm() != null) {
                countyname = countyRainValue.getCnnm().trim();
            } else {
                countyRainValue.setCnnm(countyname);
            }

            if (tmpMap.get(countyname) != null) {
                int count = ((Integer) tmpMap.get(countyname)).intValue();
                tmpMap.put(countyname, Integer.valueOf(count + 1));
                if (count < 3) {
                    list.add(countyRainValue);
                }
            } else {
                tmpMap.put(countyname, Integer.valueOf(1));
                list.add(countyRainValue);
            }
            if (list.size() >= countNum.intValue()) {
                break;
            }
        }
        return list;
    }

    public String getRainSummary(String hourStr) {
        List<RainSummarizeResponse> list = getCountyRainList(hourStr);
        StringBuffer sb = new StringBuffer("");
        StringBuffer brsb = new StringBuffer("");
        StringBuffer dbrsb = new StringBuffer("");
        sb.append(DateFormatUtils.format(new Date(System.currentTimeMillis() - 86400000L), "M月d日H时"));
        sb.append("~");
        sb.append(DateFormatUtils.format(new Date(System.currentTimeMillis()), "M月d日H时"));
        sb.append("全省");
        int zr = 0;
        int dr = 0;
        int br = 0;
        int dbr = 0;
        int flag = 0;
        float drp = 0.0F;
        for (RainSummarizeResponse rainValue : list) {
            if (rainValue.getMaxrain() != null) {
                drp = Math.round(rainValue.getMaxrain());
            }
            if (drp > 0.0F) {
                flag = 1;
            }
            if ((drp >= 10.0F) && (drp < 24.0F)) {
                zr++;
            }
            if ((drp >= 25.0F) && (drp < 49.0F)) {
                dr++;
            }
            if ((drp >= 50.0F) && (drp < 99.0F)) {
                br++;
                if (br > 1) {
                    brsb = brsb.append("、");
                }
                brsb = brsb.append("<font color='red'>").append(rainValue.getCnnm()).append("</font>").append(drp).append("毫米");
            }
            if (drp >= 100.0F) {
                dbr++;
                if (dbr > 1) {
                    dbrsb = dbrsb.append("、");
                }
                dbrsb = dbrsb.append("<font color='red'>").append(rainValue.getCnnm()).append("</font>").append(drp).append("毫米");
            }
        }

        if (dbr > 0) {
            sb.append("有<font color='red'>").append(dbr).append("</font>个县下了大暴雨，").append(dbrsb);
            flag += 1000;
        }
        if (br > 0) {
            if (flag > 1) {
                sb.append("；<br>");
            }

            sb.append("有<font color='red'>").append(br).append("</font>个县下了暴雨，").append(brsb);
            flag += 1000;
        }
        if (dr > 0) {
            if (flag > 1)
                sb.append("；<br>");
            sb.append("有").append(dr).append("个县下了大雨");
            flag += 100;
        }
        if (zr > 0) {
            if (flag > 1) {
                sb.append("；");
            }
            sb.append("有").append(zr).append("个县下了中雨");
            flag += 10;
        }
        if (flag == 1) {
            sb.append("部分地方下了小雨");
        }
        if (flag == 0) {
            sb.append("无雨");
        }
        if ((flag < 1000) && (flag > 0)) {
            sb.append("；其中");
            sb.append(list.get(0).getCnnm()).append(list.get(0).getMaxrain()).append("毫米最大");
        }
        sb.append("。");
        return sb.toString();
    }

}

