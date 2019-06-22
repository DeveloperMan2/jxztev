package com.jxztev.dao.acs4sql;

import org.springframework.stereotype.Repository;
import com.jxztev.dao.acs4sql.IIsRiverOverTopWLDao;
import java.util.List;
import com.jxztev.entity.acs4sql.IsRiverOverTopWLResponse;
import com.jxztev.entity.acs4sql.IsRiverOverTopWLRequest;

/**
 * @desc 河道是否超过最高水位
 */
@Repository("isRiverOverTopWLDao")
public interface IIsRiverOverTopWLDao {
    List<IsRiverOverTopWLResponse> isRiverOverTopWLHandler(IsRiverOverTopWLRequest isRiverOverTopWLRequestParams);
}
