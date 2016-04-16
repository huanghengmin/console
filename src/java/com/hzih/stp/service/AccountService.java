package com.hzih.stp.service;

import com.hzih.stp.domain.Account;
import com.hzih.stp.domain.AccountSecurity;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 下午1:02
 * To change this template use File | Settings | File Templates.
 */
public interface AccountService {
    public String select(int accountType, String userName, String status, int start, int limit,int unLock) throws Exception;

    public String update(Account account ,long[] rIds) throws Exception;

    public String delete(Long id) throws Exception;

    public String insert(Account account, long[] rIds) throws Exception;

    public String checkUserName(String userName) throws Exception;

    public String selectUserNameKeyValue() throws Exception;

    public String selectFlag(String userName, long id) throws Exception;

    public String updateSecurity(AccountSecurity accountSecurity) throws Exception;

    public AccountSecurity selectAccountSecurity(Long id) throws Exception;

    public void updateStatus(long id, String status) throws Exception;

    public String selectUserNameKeyValue(int accountType) throws Exception;
}
