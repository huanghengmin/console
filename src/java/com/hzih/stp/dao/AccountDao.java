package com.hzih.stp.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.stp.domain.Account;

public interface AccountDao extends BaseDao {

	PageResult listByPage(int accountType, String userName, String status, int pageIndex, int limit);

	Account findByNameAndPwd(String name, String pwd);

    Account findByName(String userName);

    Account findByNameAndPwd2(String name, String pwd);
}
