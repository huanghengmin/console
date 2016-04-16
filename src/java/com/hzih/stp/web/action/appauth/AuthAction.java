package com.hzih.stp.web.action.appauth;

import com.hzih.stp.domain.Account;
import com.hzih.stp.domain.AccountSecurity;
import com.hzih.stp.domain.SecurityLevel;
import com.hzih.stp.entity.TypeBase;
import com.hzih.stp.entity.TypeSafe;
import com.hzih.stp.service.*;
import com.hzih.stp.utils.StringContext;
import com.hzih.stp.utils.StringUtils;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.inetec.common.exception.Ex;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-7-25
 * Time: 下午3:08
 * 审核管理
 */
public class AuthAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(AuthAction.class);
    private XmlOperatorService xmlOperatorService;
    private DataBaseService dataBaseService;
    private LogService logService;
    private DeleteStatusService deleteStatusService;
    private SecurityLevelService securityLevelService;
    private AccountService accountService;
    private String appName;
    private String plugin;
    private TypeBase typeBase;
    private TypeSafe typeSafe;

    public String delete() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String msg = null;
        Account account = SessionUtils.getAccount(request);
        try{
            if(StringUtils.isAuthUserConfig(account)){
                //数据库操作失败仍然允许删除
                //by ztt for can't connect the db
                try{
                    dataBaseService.operateDBRemoveApp(plugin, appName);
                    deleteStatusService.deleteByAppName(appName);
                } catch (Ex e){
                    logger.error("操作数据库失败",e);
                }
                if(plugin.equals(StringContext.EXTERNAL)){
                    xmlOperatorService.deleteExternalTypeByName(appName,1);
                } else if(plugin.equals(StringContext.INTERNAL)){
                    xmlOperatorService.deleteInternalTypeByName(appName,1);
                }
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"审核管理","删除应用"+appName+"成功!", 1);
                msg = "删除批准成功,点击[确定]返回列表!";
            } else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","删除应用"+appName+"失败,没有权限!", 0);
                msg = "删除批准失败,没有权限!";
            }
        } catch (Ex e){
            logger.error("审核管理",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"审核管理","删除应用"+appName+"失败!", 0);
            msg = "删除批准失败";
        } catch (Exception e) {
            logger.error("审核管理",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"审核管理","删除数据库中应用"+appName+"对应的记录失败!", 0);
            msg = "删除批准成功,数据库记录删除失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        base.actionEnd(response, json ,result);
        return null;
    }

    public String start() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String msg = null;
        Account account = SessionUtils.getAccount(request);
        try{
            if(StringUtils.isAuthUserConfig(account)){
                xmlOperatorService.updateTypeActive(plugin, appName, true);
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"审核管理","启动应用"+appName+"成功!", 1);
                msg = "应用启动成功,点击[确定]返回列表!";
            } else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","启动应用"+appName+"失败,没有权限!", 0);
                msg = "应用启动失败,没有权限!";
            }
        } catch (Ex e){
            logger.error("审核管理",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"审核管理","启动应用"+appName+"失败!", 0);
            msg = "应用启动失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        base.actionEnd(response, json ,result);
        return null;
    }

    public String stop() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String msg = null;
        Account account = SessionUtils.getAccount(request);
        try{
            if(StringUtils.isAuthUserConfig(account)){
                xmlOperatorService.updateTypeActive(plugin, appName, false);
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"审核管理","停止应用"+appName+"成功!", 1);
                msg = "应用停止成功,点击[确定]返回列表!";
            } else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","停止应用"+appName+"失败,没有权限!", 0);
                msg = "应用停止失败,没有权限!";
            }
        } catch (Ex e){
            logger.error("审核管理",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"审核管理","停止应用"+appName+"失败!", 0);
            msg = "应用停止失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        base.actionEnd(response, json ,result);
        return null;
    }

    public String allow() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String msg = null;
        Account account = SessionUtils.getAccount(request);
        boolean isOk = false;
        try{
            if(StringUtils.isAuthUserConfig(account)){
                xmlOperatorService.updateTypeAllow(plugin, appName, true);
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "审核管理", "审核通过应用" + appName + "成功!", 1);
                msg = "应用审核通过成功";
                isOk = true;
            } else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","审核通过应用"+appName+"失败,没有权限!", 0);
                msg = "应用审核通过失败,没有权限!";
            }
        } catch (Ex e){
            logger.error("审核管理",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"审核管理","审核通过应用"+appName+"失败!", 0);
            msg = "应用审核通过失败";

        }
        String json = "{success:true,msg:'"+msg+"',flag:"+isOk+"}";
        base.actionEnd(response, json ,result);
        return null;
    }

    public String updateFile() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);

        String msg = null;
        Account account = SessionUtils.getAccount(request);
        try{
            if(StringUtils.isAuthUserConfig(account)){
                AccountSecurity as = accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId());
                int flagType = Integer.parseInt(as.getInfoLevel());
                if(typeBase.getInfoLevel()>flagType) {
                    logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"安全属性失败,没有权限!", 0);
                    msg = "设置失败,权限等级过低!";
                } else {
                    String isFilter = request.getParameter("typeBase.isFilter");
                    String isVirusScan = request.getParameter("typeBase.isVirusScan");
                    typeBase.setFilter(Boolean.parseBoolean(isFilter));
                    typeBase.setVirusScan(Boolean.parseBoolean(isVirusScan));
                    SecurityLevel securityLevel = securityLevelService.findByLevelInfo(typeBase.getInfoLevel());
                    typeBase.setSecurityFlag(securityLevel.getSecurityFlag());
                    xmlOperatorService.updateSecurityFile(typeBase);
                    xmlOperatorService.updateTypeAppSend(typeBase.getAppName(),StringContext.UPDATE_APP);
                    deleteStatusService.updateFlagType(typeBase.getAppName(),securityLevel.getSecurityLevel());
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"的安全属性成功!", 1);
                    msg = "设置成功,点击[确定]返回列表!";
                }
            } else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"安全属性失败,没有权限!", 0);
                msg = "设置失败,没有权限!";
            }
        } catch (Exception e){
            logger.error("审核管理",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"的安全属性失败!", 0);
            msg = "设置失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        base.actionEnd(response, json ,result);
        return null;
    }

    public String updateDB() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String msg = null;
        Account account = SessionUtils.getAccount(request);
        try{
            if(StringUtils.isAuthUserConfig(account)){
                AccountSecurity as = accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId());
                int flagType = Integer.parseInt(as.getInfoLevel());
                if(typeBase.getInfoLevel()>flagType) {
                    logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"安全属性失败,没有权限!", 0);
                    msg = "设置失败,权限等级过低!";
                } else {
                    String isFilter = request.getParameter("typeBase.isFilter");
                    String isVirusScan = request.getParameter("typeBase.isVirusScan");
                    typeBase.setFilter(Boolean.parseBoolean(isFilter));
                    typeBase.setVirusScan(Boolean.parseBoolean(isVirusScan));
                    SecurityLevel securityLevel = securityLevelService.findByLevelInfo(typeBase.getInfoLevel());
                    typeBase.setSecurityFlag(securityLevel.getSecurityFlag());
                    xmlOperatorService.updateSecurityDB(typeBase);
                    xmlOperatorService.updateTypeAppSend(typeBase.getAppName(),StringContext.UPDATE_APP);
                    deleteStatusService.updateFlagType(typeBase.getAppName(),securityLevel.getSecurityLevel());
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"的安全属性成功!", 1);
                    msg = "设置成功,点击[确定]返回列表!";
                }
            } else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"失败,没有权限!", 0);
                msg = "设置失败,没有权限!";
            }
        } catch (Exception e){
            logger.error("审核管理",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"的安全属性失败!", 0);
            msg = "设置失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        base.actionEnd(response, json ,result);
        return null;
    }

    public String updateProxy() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String msg = null;
        Account account = SessionUtils.getAccount(request);
        try{
            if(StringUtils.isAuthUserConfig(account)){
                AccountSecurity as = accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId());
                int flagType = Integer.parseInt(as.getInfoLevel());
                if(typeBase.getInfoLevel()>flagType) {
                    logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"安全属性失败,没有权限!", 0);
                    msg = "设置失败,权限等级过低!";
                } else {
                    String isFilter = request.getParameter("typeBase.isFilter");
                    String isVirusScan = request.getParameter("typeBase.isVirusScan");
                    String clientauthenable = request.getParameter("typeSafe.clientauthenable");
                    typeBase.setFilter(Boolean.parseBoolean(isFilter));
                    typeBase.setVirusScan(Boolean.parseBoolean(isVirusScan));
                    typeSafe.setClientauthenable(Boolean.parseBoolean(clientauthenable));
                    SecurityLevel securityLevel = securityLevelService.findByLevelInfo(typeBase.getInfoLevel());
                    typeBase.setSecurityFlag(securityLevel.getSecurityFlag());
                    xmlOperatorService.updateSecurityProxy(typeBase, typeSafe);
                    xmlOperatorService.updateTypeAppSend(typeBase.getAppName(),StringContext.UPDATE_APP);
                    deleteStatusService.updateFlagType(typeBase.getAppName(),securityLevel.getSecurityLevel());
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"的安全属性成功!", 1);
                    msg = "设置成功,点击[确定]返回列表!";
                }
            } else {
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"失败,没有权限!", 0);
                msg = "设置失败,没有权限!";
            }
        } catch (Exception e){
            logger.error("审核管理",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"审核管理","设置应用"+typeBase.getAppName()+"的安全属性失败!", 0);
            msg = "设置失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        base.actionEnd(response, json ,result);
        return null;
    }

    public void setXmlOperatorService(XmlOperatorService xmlOperatorService) {
        this.xmlOperatorService = xmlOperatorService;
    }

    public void setDataBaseService(DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setDeleteStatusService(DeleteStatusService deleteStatusService) {
        this.deleteStatusService = deleteStatusService;
    }

    public void setSecurityLevelService(SecurityLevelService securityLevelService) {
        this.securityLevelService = securityLevelService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public TypeBase getTypeBase() {
        return typeBase;
    }

    public void setTypeBase(TypeBase typeBase) {
        this.typeBase = typeBase;
    }

    public TypeSafe getTypeSafe() {
        return typeSafe;
    }

    public void setTypeSafe(TypeSafe typeSafe) {
        this.typeSafe = typeSafe;
    }
}
