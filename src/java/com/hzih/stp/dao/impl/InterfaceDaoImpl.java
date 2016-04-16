package com.hzih.stp.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.stp.dao.InterfaceDao;
import com.hzih.stp.domain.InterfaceObj;

import java.util.ArrayList;
import java.util.List;

public class InterfaceDaoImpl extends MyDaoSupport implements InterfaceDao {

	@Override
	public void setEntityClass() {
		this.entityClass = InterfaceObj.class;
	}

    /**
     * 分页查询
     *
     *
     * @param name
//     * @param status
     * @param pageIndex
     * @param limit
     * @return
     */
	public PageResult listByPage(String name, /*String status, */int pageIndex, int limit) {
		String hql = "from InterfaceObj where 1=1 ";
		List paramsList = new ArrayList();
		if (name != null && name.length() > 0) {
			hql += " and name like ?";
			paramsList.add("%" + name + "%");
		}
       /* if (status != null && status.length() > 0
				&& !status.equalsIgnoreCase("全部")) {
			hql += " and status=?";
			paramsList.add(status);
		}*/
		String countHql = "select count(*) " + hql;

		PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
				pageIndex, limit);
		return ps;
	}

    @Override
    public InterfaceObj findById(long interfaceId) throws Exception {
        String hql = new String("from InterfaceObj where id=?");
        List<InterfaceObj> list = getHibernateTemplate().find(hql,new Long[] { interfaceId });
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

	@Override
	public InterfaceObj findByServiceId(String l) {
		String hql = new String(" from InterfaceObj where interfaceNumber=?");
		List<InterfaceObj> list = getHibernateTemplate().find(hql,new String[] { l });
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void del(InterfaceObj interfaceObj) throws Exception {
		getHibernateTemplate().delete(interfaceObj);
	}

}
