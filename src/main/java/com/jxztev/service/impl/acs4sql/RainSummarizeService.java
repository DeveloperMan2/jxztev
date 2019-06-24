package com.jxztev.service.impl.acs4sql;

import com.alibaba.fastjson.JSONObject;
import com.jxztev.dao.acs4sql.IRainSummarizeDao;
import com.jxztev.dao.acs4sql.IReservoirMapDao;
import com.jxztev.dao.acs4sql.IRiverMapDao;
import com.jxztev.entity.acs4sql.*;
import com.jxztev.service.acs4sql.IRainSummarizeService;
import com.jxztev.utils.DataFormatUtils;
import com.ztev.commons.date.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    //河道包含的站点
    @Value("#{systemConfig[riverway_stations]}")
    private String riverwayStations;

    //水库包含的站点
    @Value("#{systemConfig[reservoir_stations]}")
    private String reservoirStations;

    @Autowired
    @Qualifier("rainSummarizeDao")
    private IRainSummarizeDao rainSummarizeDao;

    @Autowired
    @Qualifier("reservoirMapDao")
    private IReservoirMapDao reservoirMapDao;


    @Autowired
    @Qualifier("riverMapDao")
    private IRiverMapDao riverMapDao;


    public JSONObject rainSummarizeHandler() {
        JSONObject jo = new JSONObject();
        try {
            //获取雨情概述
            String rain = getRainSummary("24");
            //获取水库水情
            String reservoir = getReservoirSummary();
            //获取河道水情
            String river = getRiverSummary();
            JSONObject data = new JSONObject();
            data.put("rain", rain);
            data.put("reservoir",reservoir);
            data.put("river",river);
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
        List<RainSummarizeResponse> list = new ArrayList();
        String bgDate = null;
        int INT_HOUR = DEFAULT_PERIOD.intValue();
        try {
            INT_HOUR = Integer.parseInt(hourStr);
        } catch (Exception localException1) {
        }
        try {
            // bgDate =  DateFormatUtils.format(new Date(System.currentTimeMillis() - 3600000 * INT_HOUR), JAVA_DATE_PATTERN);
            bgDate = "2019-06-15 08:00:00";
            RainSummarizeRequest rainSummarizeRequestParams = new RainSummarizeRequest();
            rainSummarizeRequestParams.setTm(bgDate);
            rainSummarizeRequestParams.setMaxrain(0f);
            list = rainSummarizeDao.findCountyRainList(rainSummarizeRequestParams);
        } catch (Exception e) {
        }
        return list;
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
           // bgDate = DateFormatUtils.format(new Date(System.currentTimeMillis() - 3600000 * INT_HOUR), JAVA_DATE_PATTERN);
            bgDate = "2019-06-15 08:00:00";
            RainSummarizeRequest rainSummarizeRequestParams = new RainSummarizeRequest();
            rainSummarizeRequestParams.setTm(bgDate);
            rainSummarizeRequestParams.setMaxrain(rainFlag);
            list = rainSummarizeDao.findCountyRainList(rainSummarizeRequestParams);
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
        StringBuffer sb = new StringBuffer();
        StringBuffer brsb = new StringBuffer();
        StringBuffer dbrsb = new StringBuffer();
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

    /**************水库水情概述****************/

    private String getReservoirSummary(){
        String reservoir;
        List<ReservoirMapResponse> reservoirMapResponseList = queryReservoirMapList();
        List<ReservoirMapResponse> overTopFlz = getOverTopFLZ(reservoirMapResponseList);
        StringBuffer reservoirSbf = new StringBuffer();
        ReservoirMapResponse reservoirMapResponse = null;
        for( int i = 0; i < overTopFlz.size(); i++ ){
            reservoirMapResponse = overTopFlz.get(i);
            if(i > 0){
                reservoirSbf.append("；<br>　　　　　");//不是第一个则加；分隔
            }
            reservoirSbf.append(reservoirMapResponse.getCounty())
                    .append(" <font color='red'>").append(reservoirMapResponse.getStnm()).append("水库</font>").append(reservoirMapResponse.getTm())
                    .append("超汛限水位").append("<font color='red'>").append(reservoirMapResponse.getCfsltdz()).append("</font>").append("米");
        }
        if( overTopFlz.size() == 1 ){
            reservoir = reservoirSbf.toString();
        }else if( overTopFlz.size() > 1 ){
            reservoir = "有<font color='red'>" + overTopFlz.size() + "</font>个水库超汛限，"  + reservoirSbf.toString() ;
        }else{
            if(reservoirMapResponseList.size() > 0){
                reservoirMapResponse = reservoirMapResponseList.get(0);
                reservoirSbf = new StringBuffer();
                reservoirSbf.append("各大中型水库均在汛限水位以下，其中离汛限水位最近的是")
                        .append(reservoirMapResponse.getCounty()).append(" ").append(reservoirMapResponse.getStnm()).append("水库");
                if( reservoirMapResponse.getCfsltdz() != null ){
                    reservoirSbf.append("，").append(reservoirMapResponse.getTm()).append("比汛限水位").append(reservoirMapResponse.getCfsltdz()).append("米");
                }
                reservoir = reservoirSbf.toString();
            }else{
                reservoir = "系统暂无水库水情数据";
            }
        }
        reservoir = reservoir + "。";
        return reservoir;
    }


    private  List<ReservoirMapResponse> queryReservoirMapList() {
        Map<String, String> mapSS = new HashMap();
        mapSS.put("4", "↓");
        mapSS.put("5", "↑");
        mapSS.put("6", "—");
        Date bgTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", -1, 0), "yyyy-MM-dd HH:mm:ss");
        Date endTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", 0, 1), "yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ReservoirMapRequest reservoirMapRequestParams = new ReservoirMapRequest();
       // reservoirMapRequestParams.setBgTm(formatter.format(bgTm));
        reservoirMapRequestParams.setBgTm("2019-06-15 08:00:00");
        reservoirMapRequestParams.setEndTm(formatter.format(endTm));

        List<String> reservoirStationsList = new ArrayList<>();
        if (null != reservoirStations && !reservoirStations.equals("")) {
            for (String v : reservoirStations.split(",")) {
                reservoirStationsList.add(v);
            }
            reservoirMapRequestParams.setStationsList(reservoirStationsList);
        }
        List<ReservoirMapResponse> reservoirMapResponseList = reservoirMapDao.reservoirMapHandler(reservoirMapRequestParams);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
        for (ReservoirMapResponse reservoirItem : reservoirMapResponseList) {
            reservoirItem.setStnm(reservoirItem.getStnm().trim().replaceAll("/*", "").replaceAll("水库", ""));
            reservoirItem.setRz(DataFormatUtils.getRoundString(reservoirItem.getRz(), 2));
            reservoirItem.setFfsltdz(DataFormatUtils.getRoundString(reservoirItem.getFfsltdz(), 2));
            reservoirItem.setBfsltdz(DataFormatUtils.getRoundString(reservoirItem.getBfsltdz(), 2));
            reservoirItem.setFsltdz(DataFormatUtils.getRoundString(reservoirItem.getFsltdz(), 2));

            reservoirItem.setInq(DataFormatUtils.getValidString(reservoirItem.getInq(), 3));
            reservoirItem.setOtq(DataFormatUtils.getValidString(reservoirItem.getOtq(), 3));
            reservoirItem.setW(DataFormatUtils.getValidString(reservoirItem.getW(), 3));
            reservoirItem.setStyle("normal");
            reservoirItem.setImg("normal.gif");
            reservoirItem.setRwptn(mapSS.get(reservoirItem.getRwptn()));
            if (reservoirItem.getCfsltdz() != null) {
                reservoirItem.setCfsltdz(DataFormatUtils.getRoundString(DataFormatUtils.getDouble(reservoirItem.getCfsltdz()), 2));
                if (DataFormatUtils.getDouble(reservoirItem.getCfsltdz()).doubleValue() >= 0.0D) {
                    reservoirItem.setStyle("warn");
                    reservoirItem.setImg("alarm.gif");
                }
            }
            //必须捕获异常
            try {
                if(reservoirItem.getTm() != null){
                    Date date = simpleDateFormat.parse(reservoirItem.getTm());
                    reservoirItem.setHTM(DateUtils.formatDate(date, "H点"));
                    reservoirItem.setTm(DateUtils.formatDate(date, "M月d日 H点m分"));
                }
            } catch (ParseException px) {
                px.printStackTrace();
            }
        }
        return reservoirMapResponseList;
    }

    public List<ReservoirMapResponse> getOverTopFLZ(List<ReservoirMapResponse> list) {
        List<ReservoirMapResponse> overTopFLZ = new ArrayList<>();
        for (ReservoirMapResponse reservoir : list) {
            try {
                if (reservoir.getCfsltdz() != null) {
                    double CWRZ = Double.valueOf(reservoir.getCfsltdz()).doubleValue();
                    if (CWRZ >= 0.0D) {
                        overTopFLZ.add(reservoir);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return overTopFLZ;
    }


    /*******************河道水情概述*********************/

    private String getRiverSummary() {
        String riverway;
        List<RiverMapResponse> riverMapResponseList = queryRiverMapList();
        List<RiverMapResponse> overTopWrz = getOverTopWrz(riverMapResponseList);
        StringBuffer riverSbf = new StringBuffer();
        RiverMapResponse riverMapResponse = null;
        for( int i = 0; i < overTopWrz.size(); i++ ){
            riverMapResponse = overTopWrz.get(i);
            if(i > 0){
                riverSbf.append("；<br>　　　　　");//不是第一个则加；分隔
            }
            riverSbf.append(riverMapResponse.getCounty())
                    .append(" <font color='red'>").append(riverMapResponse.getStnm()).append("站</font>").append(riverMapResponse.getTm())
                    .append("超警戒水位").append("<font color='red'>").append(riverMapResponse.getCwrz()).append("</font>").append("米");
        }
        if( overTopWrz.size() == 1 ){
            riverway = riverSbf.toString();
        }else if( overTopWrz.size() > 1 ){
            riverway = "有<font color='red'>" + overTopWrz.size() + "</font>个站超警戒，" + riverSbf.toString() ;
        }else{
            if(riverMapResponseList.size() > 0){
                riverMapResponse = riverMapResponseList.get(0);
                riverSbf = new StringBuffer();
                riverSbf.append("各江河重点站均在警戒水位以下，其中离警戒水位最近的是")
                        .append(riverMapResponse.getCounty()).append(" ").append(riverMapResponse.getStnm()).append("站");
                if(riverMapResponse.getCwrz() != null ){
                    riverSbf.append("，").append(riverMapResponse.getTm()).append("比警戒水位").append(riverMapResponse.getCwrz()).append("米");
                }
                riverway = riverSbf.toString();
            }else{
                riverway = "系统暂无河道水情数据";
            }
        }
        riverway = riverway + "。";
        return riverway;
    }

    private  List<RiverMapResponse> queryRiverMapList() {
        Map<String, String> mapSS = new HashMap();
        mapSS.put("4", "↓");
        mapSS.put("5", "↑");
        mapSS.put("6", "—");
        Date bgTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", -1, 0), "yyyy-MM-dd HH:mm:ss");
        Date endTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", 0, 1), "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        RiverMapRequest riverMapRequestParams = new RiverMapRequest();
       // riverMapRequestParams.setBgTm(formatter.format(bgTm));
        riverMapRequestParams.setBgTm("2019-06-15 08:00:00");
        riverMapRequestParams.setEndTm(formatter.format(endTm));

        List<String> riverStationsList = new ArrayList<>();
        if (null != riverwayStations && !riverwayStations.equals("")) {
            for (String v : riverwayStations.split(",")) {
                riverStationsList.add(v);
            }
            riverMapRequestParams.setStationsList(riverStationsList);
        }
        List<RiverMapResponse> riverMapResponseList = riverMapDao.riverMapHandler(riverMapRequestParams);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
        for (RiverMapResponse riverItem : riverMapResponseList) {
            riverItem.setStnm(riverItem.getStnm().trim());
            riverItem.setZ(DataFormatUtils.getRoundString(riverItem.getZ(), 2));
            riverItem.setQ(DataFormatUtils.getValidString(riverItem.getQ(), 3));
            riverItem.setWrz(DataFormatUtils.getRoundString(riverItem.getWrz(), 2));
            riverItem.setObhtz(DataFormatUtils.getRoundString(riverItem.getObhtz(), 2));
            //必须捕获异常
            try {
                if (riverItem.getTm() != null) {
                    Date date = simpleDateFormat.parse(riverItem.getTm());
                    riverItem.setHTM(DateUtils.formatDate(date, "H点"));
                    riverItem.setTm(DateUtils.formatDate(date, "M月d日 H点m分"));
                }
            } catch (ParseException px) {
                px.printStackTrace();
            }
        }
        return riverMapResponseList;
    }

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
                e.printStackTrace();
            }
        }
        return overTopWrz;
    }

}

