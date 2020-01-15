package com.jxztev.service.impl.acs4sql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxztev.entity.acs4sql.ReservoirMapResponse;
import com.jxztev.service.acs4sql.IReservoirMapService;
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
 * @desc ReservoirMap接口
 */
@Service("reservoirMapService")
@Transactional(rollbackFor = Exception.class)
public class ReservoirMapService implements IReservoirMapService {

    //水库水情请求地址
    @Value("#{systemConfig[reservoir_map_service]}")
    private String reservoirMapServiceUrl;


    public List<ReservoirMapResponse> queryReservoirMapList() {
        Map<String, String> mapSS = new HashMap();
        mapSS.put("4", "↓");
        mapSS.put("5", "↑");
        mapSS.put("6", "—");
        JSONObject result = HttpUtils.doGet(reservoirMapServiceUrl);
        JSONArray listJson = result.getJSONArray("data");
        List<ReservoirMapResponse> reservoirMapResponseList = listJson.toJavaList(ReservoirMapResponse.class);
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
                if (reservoirItem.getTm() != null) {
                    String tm = reservoirItem.getTm();
                    String hour = tm.substring(tm.indexOf("日")+1,tm.indexOf("点")+1);
                    reservoirItem.setHTM(hour);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            }
        }
        return overTopFLZ;
    }

}

