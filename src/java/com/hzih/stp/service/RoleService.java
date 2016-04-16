package com.hzih.stp.service;

import com.hzih.stp.domain.Role;
import com.hzih.stp.domain.RoleType;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-8
 * Time: 下午12:24
 * To change this template use File | Settings | File Templates.
 */
public interface RoleService {
    public String insert(Role role, String[] pIds) throws Exception;

    public String delete(long id) throws Exception;

    public String update(Role role, String[] pIds) throws Exception;

    public String select(int start, int limit, int accountType) throws Exception;

    public String getPermissionInsert(int start, int limit) throws Exception;

    public String getPermissionUpdate(int start, int limit,long id) throws Exception;

    public String getNameKeyValue(int accountType) throws Exception;

    public String checkRoleName(String name) throws Exception;

    public String getTypeKeyValue(int accountType) throws Exception;

    public String selectRoleType(int start, int limit, int accountType) throws Exception;

    public RoleType updateRoleType(RoleType roleType) throws Exception;

    public RoleType getRoleType(long id) throws Exception;
}
