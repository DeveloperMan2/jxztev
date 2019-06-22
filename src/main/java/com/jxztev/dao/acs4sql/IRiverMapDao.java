package com.jxztev.dao.acs4sql;

import org.springframework.stereotype.Repository;
import com.jxztev.dao.acs4sql.IRiverMapDao;
import java.util.List;
import com.jxztev.entity.acs4sql.RiverMapResponse;
import com.jxztev.entity.acs4sql.RiverMapRequest;

/**
 * @desc RiverMap接口
 */
@Repository("riverMapDao")
public interface IRiverMapDao {
    List<RiverMapResponse> riverMapHandler(RiverMapRequest riverMapRequestParams);
}
