package com.hzih.stp.service;

import com.hzih.stp.domain.Account;
import com.hzih.stp.domain.InterfaceObj;

/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 15-9-5
 * Time: 上午11:25
 */
public interface InterfaceService {

    public String selectInterface(String name,/* String status, */int start, int limit, Account account) throws Exception;

    public void authInterface(String[] userNames, long interfaceId) throws Exception;

    public String selectAuthUser(long interfaceId, int start, int limit) throws Exception;

    public void unAuthInterface(String userName, long interfaceId) throws Exception;

    public void applyInterface(long interfaceId, Long id) throws Exception;

    public void insert(InterfaceObj interfaceObjObj) throws Exception;

    public void del(InterfaceObj interfaceObjObj) throws Exception;

    InterfaceObj findByServiceId(String l);
}
