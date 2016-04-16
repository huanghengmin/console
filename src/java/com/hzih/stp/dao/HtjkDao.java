package com.hzih.stp.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.stp.domain.Htjk;

public interface HtjkDao extends BaseDao {

    Htjk findById(int id) throws Exception;

    void modify(Htjk htjk);
}
