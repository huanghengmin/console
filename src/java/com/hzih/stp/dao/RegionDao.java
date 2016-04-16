package com.hzih.stp.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.stp.domain.Region;

public interface RegionDao extends BaseDao {

    Region findById(Long id);
}
