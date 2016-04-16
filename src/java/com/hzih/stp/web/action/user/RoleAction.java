package com.hzih.stp.web.action.user;


import com.hzih.stp.domain.Role;
import com.hzih.stp.domain.RoleType;
import com.hzih.stp.service.LogService;
import com.hzih.stp.service.RoleService;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-8
 * Time: 下午12:24
 * To change this template use File | Settings | File Templates.
 */
public class RoleAction extends ActionSupport {
    private Logger logger = Logger.getLogger(RoleAction.class);
    private RoleService roleService;
    private LogService logService;
    private Role role;
    private RoleType roleType;
    private int start;
    private int limit;

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        int accountType = 0;
        try {
            accountType = Integer.parseInt(request.getParameter("accountType"));
            String[] pIds = request.getParameterValues("pIds");
            role.setCreatedTime(new Date());
            json = roleService.insert(role,pIds);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","新增角色信息成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","新增角色信息失败", 0);
            json = "{success:true,msg:'新增角色失败'}";
        }

        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String delete() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            json = roleService.delete(id);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","删除角色信息成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","删除角色信息失败", 0);
            json = "{success:true,msg:'删除角色失败'}";
        }

        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        int accountType = 0;
        try {
            accountType = Integer.parseInt(request.getParameter("accountType"));
            String[] pIds = request.getParameterValues("pIds");
            json = roleService.update(role,pIds);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","更新角色信息成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","更新角色信息失败", 0);
            json = "{success:true,msg:'更新角色失败'}";
        }

        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String select() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        int accountType = 0;
        try {
            accountType = Integer.parseInt(request.getParameter("accountType"));
            json = roleService.select(start ,limit,accountType);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","获取角色信息成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","获取角色信息失败", 0);
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String readNameKeyValue() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        int accountType = 0;
        try {
            accountType = Integer.parseInt(request.getParameter("accountType"));
            json = roleService.getNameKeyValue(accountType);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "管理","获取所有角色名成功", 1);
        } catch (Exception e) {
            logger.error("管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "管理","获取所有角色名失败", 0);
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String readTypeKeyValue() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        int accountType = 0;
        try {
            accountType = Integer.parseInt(request.getParameter("accountType"));
            json = roleService.getTypeKeyValue(accountType);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","获取所有角色类型名成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","获取所有角色类型名失败", 0);
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String selectRoleType() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        int accountType = 0;
        try {
            accountType = Integer.parseInt(request.getParameter("accountType"));
            json = roleService.selectRoleType(start, limit, accountType);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","获取所有角色类型名成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","获取所有角色类型名失败", 0);
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String updateRoleType() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String msg = null;
        int accountType = 0;
        try {
//            accountType = Integer.parseInt(request.getParameter("accountType"));
            String permission = "";
            if ("on".equals(roleType.getRead())){
                permission += "read,";
            }
            if ("on".equals(roleType.getRename())){
                permission += "rename,";
            }
            if ("on".equals(roleType.getDelete())){
                permission += "delete,";
            }

            if(permission.length()>1){
                permission = permission.substring(0,permission.length()-1);
            }

            if("on".equals(roleType.getUnLock())){
                roleType.setAutoUnLock(1);
            } else {
                roleType.setAutoUnLock(0);
            }
            roleType.setPermission(permission);
            roleService.updateRoleType(roleType);
            msg = "修改成功";
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","获取所有角色类型名成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","获取所有角色类型名失败", 0);
            msg = "修改失败";
        } finally {
            roleType = null;
        }

        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String permissionInsert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        try {
            json = roleService.getPermissionInsert(start,limit);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","获取角色权限信息用于新增成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","获取角色权限信息用于新增失败", 0);
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String permissionUpdate() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            json = roleService.getPermissionUpdate(start,limit,id);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","获取角色权限信息用于修改成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","获取角色权限信息用于修改失败", 0);
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String check() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        try {
            String name = request.getParameter("name");
            json = roleService.checkRoleName(name);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","校验角色名是否存在成功", 1);
        } catch (Exception e) {
            logger.error("角色管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "角色管理","校验角色名是否存在失败", 0);
            json = "{success:true,msg:'校验角色名是否存在失败'}";
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }



    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
