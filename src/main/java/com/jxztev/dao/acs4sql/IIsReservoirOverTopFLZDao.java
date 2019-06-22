package com.jxztev.dao.acs4sql;

import org.springframework.stereotype.Repository;
import com.jxztev.dao.acs4sql.IIsReservoirOverTopFLZDao;
import java.util.List;
import com.jxztev.entity.acs4sql.IsReservoirOverTopFLZResponse;
import com.jxztev.entity.acs4sql.IsReservoirOverTopFLZRequest;

/**
 * @desc IsReservoirOverTopFLZ接口
 */
@Repository("isReservoirOverTopFLZDao")
public interface IIsReservoirOverTopFLZDao {
    List<IsReservoirOverTopFLZResponse> isReservoirOverTopFLZHandler(IsReservoirOverTopFLZRequest isReservoirOverTopFLZRequestParams);
}
