package com.hzih.stp.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.stp.dao.TablesDao;
import com.hzih.stp.domain.Tables;

import java.util.ArrayList;
import java.util.List;

public class TablesDaoImpl extends MyDaoSupport implements TablesDao {

	@Override
	public void setEntityClass() {
		this.entityClass = Tables.class;
	}

    /**
     * 分页查询
     *
     *
     * @param name
     * @param status
     * @param pageIndex
     * @param limit
     * @return
     */
	public PageResult listByPage(String name, String status, int pageIndex, int limit) {
		String hql = "from Tables where 1=1 ";
		List paramsList = new ArrayList();

        if (status != null && status.length() > 0
				&& !status.equalsIgnoreCase("全部")) {
			hql += " and status=?";
			paramsList.add(status);
		}
		String countHql = "select count(*) " + hql;

		PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
				pageIndex, limit);
		return ps;
	}

}
