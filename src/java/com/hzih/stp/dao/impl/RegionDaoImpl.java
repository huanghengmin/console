package com.hzih.stp.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.stp.dao.RegionDao;
import com.hzih.stp.domain.Region;

import java.util.List;


public class RegionDaoImpl extends MyDaoSupport implements RegionDao {

	@Override
	public void setEntityClass() {
		this.entityClass=Region.class;
	}


    @Override
    public Region findById(Long id) {
        String hql=" from Region r where r.regionId ="+id;
        List<Region> districts = getHibernateTemplate().find(hql);
        if(districts!=null){
            return districts.get(0);
        }
        return null;
    }


}
