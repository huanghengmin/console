package com.hzih.stp.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.stp.domain.DeleteStatus;
import com.hzih.stp.domain.SafePolicy;

import java.util.List;

public interface DeleteStatusDao extends BaseDao{

    public PageResult listByPage(int pageIndex, int limit) throws Exception;

    public DeleteStatus findByAppName(String appName) throws Exception;

    public void deleteByAppName(String appName) throws Exception;

    public List<DeleteStatus> findByFlagType(int flagType) throws Exception;

    public void deleteAll() throws Exception;
}
