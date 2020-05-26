package com.jxztev.service.impl.acs4sql;

import com.alibaba.fastjson.JSONObject;
import com.jxztev.service.acs4sql.IRainSummarizeService;
import com.jxztev.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @desc RainSummarize接口
 */
@Service("rainSummarizeService")
@Transactional(rollbackFor = Exception.class)
public class RainSummarizeService implements IRainSummarizeService {


    //河道水情请求地址
    @Value("#{systemConfig[rain_summarize]}")
    private String rainSummarizeUrl;
    @Override
    public JSONObject getRainRiverReservoirSummary() {
        JSONObject result = HttpUtils.doGet(rainSummarizeUrl,"rainwater-access-token","3579adc4-4be7-49bf-87be-f6bfff98606c");
        return result;
    }

}

