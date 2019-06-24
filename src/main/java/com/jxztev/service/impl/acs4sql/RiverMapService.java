package com.jxztev.service.impl.acs4sql;

import com.jxztev.dao.acs4sql.IRiverMapDao;
import com.jxztev.entity.acs4sql.RiverMapRequest;
import com.jxztev.entity.acs4sql.RiverMapResponse;
import com.jxztev.service.acs4sql.IRiverMapService;
import com.jxztev.utils.DataFormatUtils;
import com.ztev.commons.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    //河道包含的站点
    @Value("#{systemConfig[riverway_stations]}")
    private String riverwayStations;

    @Autowired
    @Qualifier("riverMapDao")
    private IRiverMapDao riverMapDao;

    public  List<RiverMapResponse> queryRiverMapList() {
        Map<String, String> mapSS = new HashMap();
        mapSS.put("4", "↓");
        mapSS.put("5", "↑");
        mapSS.put("6", "—");
        Date bgTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", -1, 0), "yyyy-MM-dd HH:mm:ss");
        Date endTm = DateUtils.parseDate(DateUtils.getSpaceTime("yyyy-MM-dd HH:00:00", 0, 1), "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        RiverMapRequest riverMapRequestParams = new RiverMapRequest();
        riverMapRequestParams.setBgTm(formatter.format(bgTm));
        //riverMapRequestParams.setBgTm("2019-06-15 08:00:00");
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
            } catch (ParseException e) {
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
            }
        }
        return overTopWrz;
    }

}

