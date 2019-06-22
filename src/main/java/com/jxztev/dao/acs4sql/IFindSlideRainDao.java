package com.jxztev.dao.acs4sql;

import org.springframework.stereotype.Repository;
import com.jxztev.dao.acs4sql.IFindSlideRainDao;
import java.util.List;
import com.jxztev.entity.acs4sql.FindSlideRainResponse;
import com.jxztev.entity.acs4sql.FindSlideRainRequest;

/**
 * @desc FindSlideRain接口
 */
@Repository("findSlideRainDao")
public interface IFindSlideRainDao {
    List<FindSlideRainResponse> findSlideRainHandler(FindSlideRainRequest findSlideRainRequestParams);
}
