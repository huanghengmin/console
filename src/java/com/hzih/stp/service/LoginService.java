package com.hzih.stp.service;

import com.hzih.stp.domain.Account;

public interface LoginService {

	Account getAccountByNameAndPwd(String name, String pwd) ;

    Account getAccountByName(String name) ;

    Account getAccountByNameAndPwd2(String name, String password);
}
