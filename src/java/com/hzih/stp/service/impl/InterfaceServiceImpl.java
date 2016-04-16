package com.hzih.stp.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.stp.dao.AccountDao;
import com.hzih.stp.dao.InterfaceDao;
import com.hzih.stp.dao.TablesDao;
import com.hzih.stp.domain.Account;
import com.hzih.stp.domain.InterfaceObj;
import com.hzih.stp.service.InterfaceService;
import com.hzih.stp.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 15-9-5
 * Time: 上午11:27
 */
public class InterfaceServiceImpl implements InterfaceService {

    private InterfaceDao interfaceDao;
    private TablesDao tablesDao;
    private AccountDao accountDao;

    public void setInterfaceDao(InterfaceDao interfaceDao) {
        this.interfaceDao = interfaceDao;
    }

    public void setTablesDao(TablesDao tablesDao) {
        this.tablesDao = tablesDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public String selectInterface(String name, /*String status,*/ int start, int limit, Account account) throws Exception {
        String json;
        int accountType = account.getAccountType();
        PageResult pr = interfaceDao.listByPage(name, /*status,*/ start / limit + 1, limit);
        int total = pr.getAllResultsAmount();
        List<InterfaceObj> list = pr.getResults();
        json = "{success:true,total:" + total + ",rows:[";
        if (accountType == 1) {
//            for (InterfaceObj i : list) {
            for (int y = 0; y < list.size(); y++) {
                InterfaceObj i = list.get(y);
                int apply;
                if (StringUtils.isNotBlank(i.getApply()) && i.getApply().length() > 0) {
                    apply = 1;
                } else {
                    apply = 0;
                }
                json += "{id:" + i.getId() + "," +
//                        "name:'"+i.getName()+"'," +
//                        "tableType:'"+i.getTableType()+"'," +
//                        "tableName:'"+i.getTableName()+"'," +
                        "interfaceNumber:'" + i.getInterfaceNumber() +/* "'," +*/
//                        "interfaceType:'"+i.getInterfaceType()+"'," +
//                        "interfaceDesc:'"+i.getInterfaceDesc()+"'," +
//                        "containsPerson:'"+i.getContainsPerson()+"'," +
                        "',tableNameEn:'"+i.getTableNameEn()+
//                        ",queryField:'"+i.getQueryField()+
//                        "',conditionsField:'"+i.getConditionsField()+"'" +
//                        ",url:'"+i.getUrl()+
//                        "',status:'"+i.getStatus()+"'," +
                        "',accountType:"+ accountType +
                        ",apply:" + apply + /*"," +*/
                        ",flag:1}";
                if (y != (list.size() - 1)) {
                    json += ",";
                }
            }
//            }
            json += "]}";
        } else {
            Set<InterfaceObj> set = accountDao.findByName(account.getUserName()).getInterfaceObjSet();
            Map<Long, InterfaceObj> map = new HashMap<Long, InterfaceObj>();
            for (InterfaceObj i : set) {
                map.put(i.getId(), i);
            }
//            for (InterfaceObj i : list) {
            for (int y = 0; y < list.size(); y++) {
                InterfaceObj i = list.get(y);
                int flag = 0;
                if (map.get(i.getId()) != null) {
                    flag = 2;
                } else {
                    flag = 3;
                }
                int apply;
                if (StringUtils.isNotBlank(i.getApply()) && i.getApply().length() > 0) {
                    apply = 0;
                    String[] applys = i.getApply().split(",");
                    for (String _apply : applys) {
                        if (_apply.equals("" + account.getId())) {
                            apply = 3;
                        }
                    }
                } else {
                    apply = 0;
                }
                json += "{id:" + i.getId() + "," +
//                        "name:'"+i.getName()+"'," +
//                        "tableType:'"+i.getTableType()+"'," +
//                        "tableName:'"+i.getTableName()+"'," +
                        "interfaceNumber:'" + i.getInterfaceNumber() + /*"'," +*/
//                        "interfaceType:'"+i.getInterfaceType()+"'," +
//                        "interfaceDesc:'"+i.getInterfaceDesc()+"'," +
//                        "containsPerson:'"+i.getContainsPerson()+"'," +
                        "',tableNameEn:'"+i.getTableNameEn()+
//                        ",queryField:'"+i.getQueryField()+
//                        "',conditionsField:'"+i.getConditionsField()+"'" +
//                        ",url:'"+i.getUrl()+
//                        "',status:'"+i.getStatus()+"'," +
                        "',accountType:"+ accountType +
                        ",apply:" + apply +/* "," +*/
                        ",flag:" + flag + "}";
                if (y != (list.size() - 1)) {
                    json += ",";
                }
            }
            json += "]}";
        }
        return json;
    }

    @Override
    public void authInterface(String[] userNames, long interfaceId) throws Exception {
        for (String userName : userNames) {
//            Account a = (Account) accountDao.retrieveById(Account.class,accountId);
            Account a = accountDao.findByName(userName);
            InterfaceObj i = interfaceDao.findById(interfaceId);
            Set<InterfaceObj> set = a.getInterfaceObjSet();
            boolean isUse = false;
            for (InterfaceObj in : set) {
                if (in.getId() == i.getId()) {
                    isUse = true;
                }
            }
            if (!isUse) {
                set.add(i);
            } else {
                continue;
            }
            a.setInterfaceObjSet(set);
            accountDao.update(a);
            String apply = i.getApply();
            if (StringUtils.isNotBlank(apply)) {
                if (apply.indexOf("," + a.getId() + ",") > -1) {
                    i.setApply(apply.split("," + a.getId() + ",")[0] + "," + apply.split("," + a.getId() + ",")[1]);
                }
                if (apply.startsWith(a.getId() + ",")) {
                    i.setApply(apply.substring(apply.indexOf(a.getId() + ",") + 1));
                }
                if (apply.endsWith("," + a.getId())) {
                    i.setApply(apply.substring(0, apply.indexOf(a.getId() + ",")));
                }
                if (apply.equals("" + a.getId())) {
                    i.setApply("");
                }
                interfaceDao.update(i);
            }
        }

    }

    @Override
    public String selectAuthUser(long interfaceId, int start, int limit) throws Exception {
        InterfaceObj interfaceObj = interfaceDao.findById(interfaceId);
        PageResult pageResult = accountDao.listByPage(2, null, null, start / limit + 1, limit);
        int total = pageResult.getAllResultsAmount();
        List<Account> list = pageResult.getResults();
        String json = "{success:true,total:" + total + ",rows:[";
        String[] applys = new String[0];
        if (StringUtils.isNotBlank(interfaceObj.getApply())) {
            applys = interfaceObj.getApply().split(",");
        }
        for (Account account : list) {
            Set<InterfaceObj> set = account.getInterfaceObjSet();
            boolean isChecked = false;
            for (InterfaceObj i : set) {
                if (i.getId() == interfaceId) {
                    isChecked = true;
                    break;
                }
            }
            int apply = 0;
            if (!isChecked) {
                for (String _apply : applys) {
                    if (_apply.equals("" + account.getId())) {
                        apply = 1;
                    }
                }
            }
            json += "{id:" + account.getId() + ",interfaceId:" + interfaceId + ",userName:'" + account.getUserName() + "',name:'" + account.getName() +
                    "',depart:'" + account.getDepart() + "',apply:" + apply + ",checked:" + isChecked + "},";
        }
        json += "]}";
        return json;
    }

    @Override
    public void unAuthInterface(String userName, long interfaceId) throws Exception {
        Account a = accountDao.findByName(userName);
        InterfaceObj i = interfaceDao.findById(interfaceId);
        Set<InterfaceObj> set = a.getInterfaceObjSet();
        for (InterfaceObj in : set) {
            if (in.getId() == i.getId()) {
                set.remove(in);
                break;
            }
        }
        a.setInterfaceObjSet(set);
        accountDao.update(a);
    }

    @Override
    public void applyInterface(long interfaceId, Long id) throws Exception {
        InterfaceObj i = interfaceDao.findById(interfaceId);
        String apply = i.getApply();
        if (StringUtils.isBlank(apply)) {
            apply = String.valueOf(id);
        } else {
            apply += "," + id;
        }
        i.setApply(apply);
        interfaceDao.update(i);
    }

    @Override
    public void insert(InterfaceObj interfaceObj) throws Exception {

        interfaceDao.create(interfaceObj);
    }

    @Override
    public InterfaceObj findByServiceId(String l) {
        return interfaceDao.findByServiceId(l);
    }

    @Override
    public void del(InterfaceObj interfaceObjObj) throws Exception {
        interfaceDao.del(interfaceObjObj);
    }
}
