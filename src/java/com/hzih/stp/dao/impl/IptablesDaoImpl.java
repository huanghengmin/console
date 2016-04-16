package com.hzih.stp.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.stp.dao.IptablesDao;
import com.hzih.stp.domain.Iptables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-30
 * Time: 下午3:27
 * To change this template use File | Settings | File Templates.
 */
public class IptablesDaoImpl extends MyDaoSupport implements IptablesDao {

    @Override
    public void setEntityClass() {
        this.entityClass = Iptables.class;
    }

    @Override
    public PageResult listByPage(String clientIp, String serverIp, String port, int pageIndex, int limit) {
        String hql = "from Iptables where 1=1 ";
        List paramsList = new ArrayList();
        if (clientIp != null && clientIp.length() > 0) {
            hql += " and clientIp like ?";
            paramsList.add("%" + clientIp + "%");
        }
        if (serverIp != null && serverIp.length() > 0) {
            hql += " and serverIp like ?";
            paramsList.add("%" + serverIp + "%");
        }
        if (port != null && port.length() > 0) {
            hql += " and port = ?";
            paramsList.add( port );
        }
        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }
}
