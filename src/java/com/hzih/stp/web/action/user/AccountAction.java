package com.hzih.stp.web.action.user;

import com.hzih.stp.domain.*;
import com.hzih.stp.service.*;
import com.hzih.stp.utils.StringUtils;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.inetec.common.security.DesEncrypterAsPassword;
import com.inetec.ichange.console.config.Constant;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 下午1:04
 * To change this template use File | Settings | File Templates.
 */
public class AccountAction extends ActionSupport{
    private static final Logger logger = Logger.getLogger(AccountAction.class);
    private AccountService accountService;
    private LoginService loginService;
    private SafePolicyService safePolicyService;
    private LogService logService;
    private Account account;
//    private AccountSecurity accountSecurity;
    private Role role;
    private int start;
    private int limit;
    private String userName;
    private String status;
    private RoleService roleService;

    public String select() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        int accountType = 0;
        try {
            accountType = Integer.parseInt(request.getParameter("accountType"));
            int unLock = 0;
            if(StringUtils.isAuthUser(SessionUtils.getAccount(request))){
                unLock = 1;
            }
            json = accountService.select(accountType,userName,status,start,limit,unLock);
            if(accountType==2){
                logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","管理员获取所有用户账号信息成功", 1);
            } else if(accountType==1){
                logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","管理员获取所有管理员账号信息成功", 1);
            }
        } catch (Exception e) {
            if(accountType==2){
                logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理", "管理员获取所有用户账号信息失败" + e.getMessage(), 1);
                logger.error("用户账号管理", e);
            } else if(accountType==1){
                logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理", "管理员获取所有管理员账号信息失败" + e.getMessage(), 1);
                logger.error("管理员账号管理", e);
            }
        }
        account = null;
        role = null;
        userName = null;
        status = null;
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String update() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            if(checkPassword(account.getPassword())){
                long[] rIds = {role.getId()};
                if(StringUtils.isNotBlank(account.getPassword())){
                    DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
                    String password = new String(deap.encrypt(account.getPassword().getBytes()));
                    account.setPassword(password);
                }
                msg = accountService.update(account,rIds);
                logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","修改账户"+account.getUserName()+"信息成功", 1);
            }  else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(), "账号管理","修改账户"+account.getUserName()+"信息密码不符合规则", 0);
                msg = "<font color=\"red\">修改失败,密码不符合规则</font>";
            }
        } catch (Exception e) {
            logger.error("账号管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理","修改账户"+account.getUserName()+"信息失败", 0);
            msg = "<font color=\"red\">修改失败</font>";
        }
        account = null;
        role = null;
        userName = null;
        status = null;
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String selectAccountFlag() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        BufferedReader br = null;
        try {

            long id = Long.parseLong(request.getParameter("id"));
            json = accountService.selectFlag(userName, id);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","获取"+userName+"账号标记信息成功", 1);

        } catch (Exception e) {
            logger.error("账号管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理","获取"+userName+"账号标记信息失败", 0);
        } finally {
            if(br!=null){
                br.close();
            }
            account = null;
            role = null;
            userName = null;
            status = null;
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String updateFlag() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            boolean isFilter = Boolean.parseBoolean(request.getParameter("isFilter"));
            boolean isVirusScan = Boolean.parseBoolean(request.getParameter("isVirusScan"));
            String infoLevel = request.getParameter("infoLevel");
            String appName = request.getParameter("appName");
            AccountSecurity accountSecurity = new AccountSecurity();
            accountSecurity.setId(id);
            accountSecurity.setFilter(isFilter);
            accountSecurity.setVirusScan(isVirusScan);
            accountSecurity.setInfoLevel(infoLevel);
            accountSecurity.setAppName(appName);
            msg = accountService.updateSecurity(accountSecurity);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","修改账户"+userName+"标记信息成功", 1);
        } catch (Exception e) {
            logger.error("账号管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理","修改账户"+userName+"标记信息失败", 0);
            msg = "<font color=\"red\">设置失败</font>";
        }
        account = null;
        role = null;
        userName = null;
        status = null;
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String delete() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String msg = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            msg = accountService.delete(id);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","删除账户"+userName+"信息成功", 1);
        } catch (Exception e) {
            logger.error("账号管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理","删除账户"+userName+"信息失败", 0);
            msg = "<font color=\"red\">删除失败</font>";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        account = null;
        role = null;
        userName = null;
        status = null;
        return null;
    }

    public String unlock() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String msg = null;
        long id = Long.parseLong(request.getParameter("id"));

        try {
            accountService.updateStatus(id, "1");
            msg = "<font color=\"green\">解锁成功,点击[确定]返回列表!</font>";
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","管理员解锁账户"+userName+"信息成功", 1);
        } catch (Exception e) {
            logger.error("账号管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理","管理员解锁账户"+userName+"信息失败", 0);
            msg = "<font color=\"red\">解锁失败</font>";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        account = null;
        role = null;
        userName = null;
        status = null;
        return null;
    }

    public String insert() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String msg = null;
        try {
            if(checkPassword(account.getPassword())){
                long[] rIds = {role.getId()};
                account.setCreatedTime(new Date());
                DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
                String password = new String(deap.encrypt(account.getPassword().getBytes()));
                account.setPassword(password);
                msg = accountService.insert(account,rIds);
                logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","用户新增账户"+account.getUserName()+"信息成功", 1);
            }  else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(), "账号管理","用户新增账户"+account.getUserName()+"信息密码不符合规则", 0);
                msg = "<font color=\"red\">新增失败,密码不符合规则</font>";
            }
        } catch (Exception e) {
            logger.error("账号管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理","用户新增账户"+account.getUserName()+"信息失败", 0);
            msg = "<font color=\"red\">新增失败</font>";
        }
        account = null;
        role = null;
        userName = null;
        status = null;
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    /**
     * 符合的返回true,否则返回false
     * @param password
     * @return
     */
    private boolean checkPassword(String password) {
        if(StringUtils.isBlank(password)){
            return true;
        }
        SafePolicy safePolicy = safePolicyService.getData();
        if(password.matches(safePolicy.getPasswordRules())){
            return true;
        }
        return false;
    }

    public String checkUserName() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false,msg:'校验失败'}";
        try {
            Account a = SessionUtils.getAccount(request);
            String userName = request.getParameter("userName");

            json = accountService.checkUserName(userName);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","用户检查用户名是否存在成功", 1);
        } catch (Exception e) {
            logger.error("账号管理", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理","用户检查用户名是否存在失败", 0);
        }
        account = null;
        role = null;
        userName = null;
        status = null;
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String checkPassword() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            String password = request.getParameter("password");
            if(checkPassword(password)){
                msg = "true";
            } else {
                msg = "不符合安全策略中的密码校验限制";
            }
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "账号管理","用户校验密码是否符合规则成功", 1);
        } catch (Exception e) {
            logger.error("账号管理", e);
            msg = "校验失败";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "账号管理","用户校验密码是否符合规则失败", 0);
        }
        account = null;
        role = null;
        userName = null;
        status = null;
        String json = "{success:false,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String updatePwd() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String msg = null;
        try {
            Account a = SessionUtils.getAccount(request);
            String pwdOld = request.getParameter("pwd");
            DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
            String passwordOld = new String(deap.encrypt(pwdOld.getBytes()));
            if(a.getPassword().equals(passwordOld)) {
                String pwd = request.getParameter("newpwd");
                if(checkPassword(pwd)){
                    long[] rIds = {};
                    String password = new String(deap.encrypt(pwd.getBytes()));
                    a.setPassword(password);
                    msg = accountService.update(a,rIds);
                    logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "修改密码","用户修改密码成功", 1);
                }  else {
                    logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(), "修改密码","用户修改密码不符合规则", 0);
                    msg = "<font color=\"red\">修改失败,密码不符合规则</font>";
                }
            } else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(), "修改密码","用户修改当前密码错误",0);
                msg = "<font color=\"red\">修改失败,当前密码错误</font>";
            }
        } catch (Exception e) {
            logger.error("修改密码", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "修改密码","用户修改密码失败", 0);
            msg = "<font color=\"red\">修改密码失败</font>";
        }
        account = null;
        role = null;
        userName = null;
        status = null;
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String selectUserNameKeyValue() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        int accountType = 0;
        try {
            accountType = Integer.parseInt(request.getParameter("accountType"));
            if(accountType==2||accountType==1){
                json = accountService.selectUserNameKeyValue(accountType);
            } else {
                json = accountService.selectUserNameKeyValue();
            }

//            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "用户日志审计","用户获取所有账号名列表成功", 1);
        } catch (Exception e) {
            logger.error("selectUserNameKeyValue", e);
//            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "用户日志审计","用户获取所有账号名列表失败", 0);
        }
        account = null;
        role = null;
        userName = null;
        status = null;
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String indexPermission() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String msg = null;
        int size = 0;
        try {
            Account account = SessionUtils.getAccount(request);
            Set<Permission> permissions = account.getPermissions();
            SortedMap<Long,Permission> topMap = new TreeMap<Long, Permission>();
            SortedMap<Long,Permission> secondMap = null;
            SortedMap<Long,SortedMap<Long,Permission>> secondMaps = new TreeMap<Long, SortedMap<Long,Permission>>();
            for(Permission p : permissions) {
                long parentId = p.getParentId();
                if(parentId == 0) {
                    topMap.put(p.getId(), p);
                } else {
                    if(secondMaps.get(parentId)==null) {
                        secondMap = new TreeMap<Long, Permission>();
                        secondMaps.put(parentId,secondMap);
                    }
                    secondMaps.get(parentId).put(Long.valueOf(p.getOrder()), p);
                }
            }
            msg = "[";
            for(Map.Entry top : topMap.entrySet()) {
                Permission p = (Permission) top.getValue();
                msg += "{id:'"+p.getCode()+"',title:'"+p.getName()+"',iconCls:'"+p.getIconCls()+"',sun:[";
                SortedMap<Long,Permission> _secondMap = secondMaps.get(top.getKey());
                if(_secondMap != null){
                    for(Map.Entry second : _secondMap.entrySet()) {
                        Permission _p = (Permission) second.getValue();
                        msg += "{id:'"+_p.getCode()+"',title:'"+_p.getName()+"',url:'"+_p.getUrl()+"'},";
                    }
                }
                msg += "]},";
                size ++;
            }
            msg += "]";
            if(isUser(account)){
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "主页加载", "获取用户的权限信息成功", 1);
            } else {
                logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "主页加载","获取用户的权限信息成功",1);
            }
        } catch (Exception e) {
            logger.error("主页加载", e);
            if(isUser(SessionUtils.getAccount(request))){
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "主页加载", "获取用户的权限信息失败", 0);
            } else {
                logService.newManagerLog("ERROE", SessionUtils.getAccount(request).getUserName(), "主页加载","获取用户的权限信息失败",0);
            }
            msg = "<font color=\"red\">权限获取失败</font>";
        }
        String json = "{success:true,total:"+ size +",rows:"+msg+"}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    private boolean isUser(Account account) throws Exception {
        if(account.getAccountType()==2) {
            return true;
        }
        return false;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public SafePolicyService getSafePolicyService() {
        return safePolicyService;
    }

    public void setSafePolicyService(SafePolicyService safePolicyService) {
        this.safePolicyService = safePolicyService;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
