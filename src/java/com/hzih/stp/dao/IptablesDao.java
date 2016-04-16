package com.hzih.stp.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-30
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public interface IptablesDao extends BaseDao {

    PageResult listByPage(String clientIp, String serverIp,String port, int pageIndex, int limit);
}
