package com.jxztev.service.impl.acs4sql;

import com.jxztev.dao.acs4sql.IRainSummarizeDao;
import com.jxztev.entity.acs4sql.RainSummarizeRequest;
import com.jxztev.entity.acs4sql.RainSummarizeResponse;
import com.jxztev.service.acs4sql.IRainMapService;
import com.jxztev.utils.DataFormatUtils;
import com.ztev.commons.date.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("rainMapService")
@Transactional(rollbackFor = Exception.class)
public class RainMapService implements IRainMapService {
    public static Integer DEFAULT_PERIOD = Integer.valueOf(24);
    public static String JAVA_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static Float DEFAULT_RAIN_FLAG = Float.valueOf(50.0F);

    //河道包含的站点
    @Value("#{systemConfig[query_tm]}")
    private String queryTm;

    @Autowired
    @Qualifier("rainSummarizeDao")
    private IRainSummarizeDao rainSummarizeDao;

    public List<RainSummarizeResponse> getCountyRainList(String hourStr) {
        List<RainSummarizeResponse> list = new ArrayList();
        String bgDate = null;
        int INT_HOUR = DEFAULT_PERIOD.intValue();
        try {
            INT_HOUR = Integer.parseInt(hourStr);
        } catch (Exception localException1) {
        }
        try {
            bgDate =  DateFormatUtils.format(new Date(System.currentTimeMillis() - 3600000 * INT_HOUR), JAVA_DATE_PATTERN);
            if (DataFormatUtils.isValidDate(queryTm))
            {
                bgDate = queryTm;
            }
            RainSummarizeRequest rainSummarizeRequestParams = new RainSummarizeRequest();
            rainSummarizeRequestParams.setTm(bgDate);
            rainSummarizeRequestParams.setMaxrain(0f);
            list = rainSummarizeDao.findCountyRainList(rainSummarizeRequestParams);
        } catch (Exception e) {
        }
        return list;
    }

    public List<RainSummarizeResponse> getStationRainList(String hourStr, Float rainFlag) {
        List<RainSummarizeResponse> list = new ArrayList();
        String bgDate = null;
        int INT_HOUR = DEFAULT_PERIOD.intValue();
        try {
            INT_HOUR = Integer.parseInt(hourStr);
        } catch (Exception localException1) {
        }
        if (rainFlag == null) {
            rainFlag = DEFAULT_RAIN_FLAG;
        }
        try {
            bgDate = DateFormatUtils.format(new Date(System.currentTimeMillis() - 3600000 * INT_HOUR), JAVA_DATE_PATTERN);
            if (DataFormatUtils.isValidDate(queryTm))
            {
                bgDate = queryTm;
            }
            RainSummarizeRequest rainSummarizeRequestParams = new RainSummarizeRequest();
            rainSummarizeRequestParams.setTm(bgDate);
            rainSummarizeRequestParams.setMaxrain(rainFlag);
            list = rainSummarizeDao.findStationRainList(rainSummarizeRequestParams);
        } catch (Exception e) {
        }
        return list;
    }

    public List<RainSummarizeResponse> getMaxRainOrderRain(String hourStr, Integer countNum) {
        List<RainSummarizeResponse> list = new ArrayList();
        Map<String, Integer> tmpMap = new HashMap();
        float rainFlag = 0;
        List<RainSummarizeResponse> rawList = getStationRainList(hourStr, rainFlag);
        RainSummarizeResponse countyRainValue = null;
        for (int i = 0; i < rawList.size(); i++) {
            countyRainValue = rawList.get(i);
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

}
