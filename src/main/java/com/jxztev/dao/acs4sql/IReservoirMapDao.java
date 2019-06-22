package com.jxztev.dao.acs4sql;

import org.springframework.stereotype.Repository;
import com.jxztev.dao.acs4sql.IReservoirMapDao;
import java.util.List;
import com.jxztev.entity.acs4sql.ReservoirMapResponse;
import com.jxztev.entity.acs4sql.ReservoirMapRequest;

/**
 * @desc ReservoirMap接口
 */
@Repository("reservoirMapDao")
public interface IReservoirMapDao {
    List<ReservoirMapResponse> reservoirMapHandler(ReservoirMapRequest reservoirMapRequestParams);
}
