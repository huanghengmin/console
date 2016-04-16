package com.hzih.stp.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.stp.domain.InterfaceObj;

public interface InterfaceDao extends BaseDao {

	PageResult listByPage( String name,/* String status,*/ int pageIndex, int limit) throws Exception;

    InterfaceObj findById(long interfaceId) throws Exception;

    public void del(InterfaceObj interfaceObj) throws Exception;

    InterfaceObj findByServiceId(String l);
}
