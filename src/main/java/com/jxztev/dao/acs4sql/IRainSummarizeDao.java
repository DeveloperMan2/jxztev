package com.jxztev.dao.acs4sql;

import org.springframework.stereotype.Repository;
import com.jxztev.dao.acs4sql.IRainSummarizeDao;
import java.util.List;
import com.jxztev.entity.acs4sql.RainSummarizeResponse;
import com.jxztev.entity.acs4sql.RainSummarizeRequest;

/**
 * @desc RainSummarize接口
 */
@Repository("rainSummarizeDao")
public interface IRainSummarizeDao {
    List<RainSummarizeResponse> rainSummarizeHandler(RainSummarizeRequest rainSummarizeRequestParams);
}
