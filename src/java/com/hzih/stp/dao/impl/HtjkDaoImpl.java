package com.hzih.stp.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.stp.dao.HtjkDao;
import com.hzih.stp.domain.Htjk;

import java.util.List;

public class HtjkDaoImpl extends MyDaoSupport implements HtjkDao {

	@Override
	public void setEntityClass() {
		this.entityClass = Htjk.class;
	}


    @Override
    public Htjk findById(int id) throws Exception {
        String hql = new String("from Htjk where id=?");
        List<Htjk> list = getHibernateTemplate().find(hql,new Integer[] { id });
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void modify(Htjk htjk) {
        getHibernateTemplate().update(htjk);
    }
}
