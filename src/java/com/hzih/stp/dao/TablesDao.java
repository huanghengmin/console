package com.hzih.stp.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

public interface TablesDao extends BaseDao {

	PageResult listByPage(String name, String status, int pageIndex, int limit);
}
