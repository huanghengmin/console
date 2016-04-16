package com.hzih.stp.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.stp.dao.AccountSecurityDao;
import com.hzih.stp.domain.AccountSecurity;

import java.util.List;

public class AccountSecurityDaoImpl extends MyDaoSupport implements AccountSecurityDao {

	@Override
	public void setEntityClass() {
		this.entityClass = AccountSecurity.class;
	}


    /**
     * 通过用户名查找用户
     * @return
     */
    public AccountSecurity findById(long id) {
        String hql = new String("from Account where id=?");
		List<AccountSecurity> list = getHibernateTemplate().find(hql,new long[] { id });
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
    }

}
