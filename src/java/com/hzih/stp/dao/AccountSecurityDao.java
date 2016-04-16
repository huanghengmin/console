package com.hzih.stp.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.stp.domain.Account;
import com.hzih.stp.domain.AccountSecurity;

public interface AccountSecurityDao extends BaseDao {

    AccountSecurity findById(long id);
}
