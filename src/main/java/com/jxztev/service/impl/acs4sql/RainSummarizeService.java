package com.jxztev.service.impl.acs4sql;

import com.alibaba.fastjson.JSONObject;
import com.jxztev.dao.acs4sql.IRainSummarizeDao;
import com.jxztev.dao.acs4sql.IReservoirMapDao;
import com.jxztev.dao.acs4sql.IRiverMapDao;
import com.jxztev.entity.acs4sql.*;
import com.jxztev.service.acs4sql.IRainMapService;
import com.jxztev.service.acs4sql.IRainSummarizeService;
import com.jxztev.service.acs4sql.IReservoirMapService;
import com.jxztev.service.acs4sql.IRiverMapService;
import com.jxztev.utils.DataFormatUtils;
import com.ztev.commons.date.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
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

    @Autowired
    @Qualifier("rainMapService")
    private IRainMapService rainMapService;

    @Autowired
    @Qualifier("reservoirMapService")
    private IReservoirMapService reservoirMapService;

    @Autowired
    @Qualifier("riverMapService")
    private IRiverMapService riverMapService;

    public String getRainSummary(String hourStr) {
        List<RainSummarizeResponse> list = rainMapService.getCountyRainList(hourStr);
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

    public String getReservoirSummary(){
        String reservoir;
        List<ReservoirMapResponse> reservoirMapResponseList = reservoirMapService.queryReservoirMapList();
        List<ReservoirMapResponse> overTopFlz = reservoirMapService.getOverTopFLZ(reservoirMapResponseList);
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




    /*******************河道水情概述*********************/

    public String getRiverSummary() {
        String riverway;
        List<RiverMapResponse> riverMapResponseList = riverMapService.queryRiverMapList();
        List<RiverMapResponse> overTopWrz = riverMapService.getOverTopWrz(riverMapResponseList);
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
}

