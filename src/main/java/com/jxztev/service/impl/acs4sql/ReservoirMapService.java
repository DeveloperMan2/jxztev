package com.jxztev.service.impl.acs4sql;

import com.jxztev.entity.acs4sql.ReservoirMapResponse;
import com.jxztev.utils.DataFormatUtils;
import com.ztev.commons.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxztev.service.acs4sql.IReservoirMapService;
import com.jxztev.dao.acs4sql.IReservoirMapDao;

	    import com.alibaba.fastjson.JSONArray;
		    import com.jxztev.entity.acs4sql.ReservoirMapRequest;
	

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* @desc ReservoirMap接口
*/
@Service("reservoirMapService")
@Transactional(rollbackFor = Exception.class)
public class ReservoirMapService implements IReservoirMapService {

    //水库包含的站点
    @Value("#{systemConfig[reservoir_stations]}")
    private String reservoirStations;

@Autowired
@Qualifier("reservoirMapDao")
private IReservoirMapDao reservoirMapDao;

    public  List<ReservoirMapResponse> queryReservoirMapList() {
        Map<String, String> mapSS = new HashMap();
        mapSS.put("4", "↓");
        mapSS.put("5", "↑");
        mapSS.put("6", "—");
        Date bgTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", -1, 0), "yyyy-MM-dd HH:mm:ss");
        Date endTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", 0, 1), "yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ReservoirMapRequest reservoirMapRequestParams = new ReservoirMapRequest();
        //reservoirMapRequestParams.setBgTm(formatter.format(bgTm));
        //reservoirMapRequestParams.setEndTm(formatter.format(endTm));
        //reservoirMapRequestParams.setMd( DateUtils.getTodayString("MMdd"));
        //todo 示例数据，仅供测试
        reservoirMapRequestParams.setBgTm("2019-06-15 08:00:00");
        reservoirMapRequestParams.setEndTm("2019-06-15 08:00:00");
        reservoirMapRequestParams.setMd("0608");


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
            } catch (ParseException e) {
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

