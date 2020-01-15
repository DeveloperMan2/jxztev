package com.jxztev.service.impl.acs4sql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxztev.entity.acs4sql.RainSummarizeResponse;
import com.jxztev.service.acs4sql.IRainMapService;
import com.jxztev.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("rainMapService")
@Transactional(rollbackFor = Exception.class)
public class RainMapService implements IRainMapService {
    //雨情请求地址
    @Value("#{systemConfig[rain_map_service]}")
    private String rainMapServiceUrl;

    /**
     * 调用共享平台服务接口
     *
     * @param hourStr
     * @return
     */
    public JSONObject getRainData(String hourStr) {
        JSONObject result = HttpUtils.doGet(rainMapServiceUrl + "&hour=" + hourStr);
//        JSONArray maxRainListJson = result.getJSONObject("data").getJSONArray("maxRainList");
//        List<RainSummarizeResponse> rawList = maxRainListJson.toJavaList(RainSummarizeResponse.class);
//
//        Integer countNum = 20;
//        List<RainSummarizeResponse> list = new ArrayList();
//        Map<String, Integer> tmpMap = new HashMap();
//        RainSummarizeResponse countyRainValue;
//        for (int i = 0; i < rawList.size(); i++) {
//            countyRainValue = rawList.get(i);
//            String countyname = "未知";
//            if (countyRainValue.getCnnm() != null) {
//                countyname = countyRainValue.getCnnm().trim();
//            } else {
//                countyRainValue.setCnnm(countyname);
//            }
//
//            if (tmpMap.get(countyname) != null) {
//                int count = ((Integer) tmpMap.get(countyname)).intValue();
//                tmpMap.put(countyname, Integer.valueOf(count + 1));
//                if (count < 3) {
//                    list.add(countyRainValue);
//                }
//            } else {
//                tmpMap.put(countyname, Integer.valueOf(1));
//                list.add(countyRainValue);
//            }
//            if (list.size() >= countNum.intValue()) {
//                break;
//            }
//        }
//        result.getJSONObject("data").put("maxRainList",list);
        return result;
    }

}
