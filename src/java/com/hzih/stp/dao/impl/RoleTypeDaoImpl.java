package com.hzih.stp.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.stp.dao.RoleTypeDao;
import com.hzih.stp.domain.RoleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-7
 * Time: 上午11:01
 * To change this template use File | Settings | File Templates.
 */
public class RoleTypeDaoImpl extends MyDaoSupport implements RoleTypeDao {
    @Override
    public void setEntityClass() {
        this.entityClass = RoleType.class;
    }


    @Override
    public PageResult listPage(int pageIndex, int limit, int accountType) throws Exception {
        String hql = "from RoleType where 1=1 ";
		List paramsList = new ArrayList();
//        hql += " and accountType = ?";
//        paramsList.add(accountType);
		String countHql = "select count(*) " + hql;
		PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
				pageIndex, limit);
		return ps;
    }
}
