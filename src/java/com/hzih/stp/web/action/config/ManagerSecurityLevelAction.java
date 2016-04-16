package com.hzih.stp.web.action.config;

import com.hzih.stp.domain.Account;
import com.hzih.stp.domain.SecurityLevel;
import com.hzih.stp.service.LogService;
import com.hzih.stp.service.SecurityLevelService;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 加密等级表管理
 */
public class ManagerSecurityLevelAction extends ActionSupport {

	private static final Logger logger = Logger.getLogger(ManagerSecurityLevelAction.class);
	private SecurityLevelService securityLevelService;
	private LogService logService;
    private SecurityLevel securityLevel;
    private String[] ids;
	
    public String select() throws Exception {
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
        String json =null;
        try{
            int start = ServletRequestUtils.getIntParameter(request, "start");
            int limit = ServletRequestUtils.getIntParameter(request, "limit");
            int pageIndex = start / limit + 1;
            json = securityLevelService.getPages(pageIndex,limit);
            logger.info("信息安全策略查看、查询功能");
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "查找加密等级信息成功", 1);
        } catch (Exception e){
            logger.error("信息安全策略",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "查找加密等级信息失败", 0);
        }
        base.actionEnd(response, json ,result);
		return null;
	}
    
    public String readLevelKeyValue() throws Exception {
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
        String json =null;
        try{
            json = securityLevelService.getLevelKeyValue();
            logger.info("组建信息等级说明");
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "组建信息等级说明成功", 1);
        } catch (Exception e){
            logger.error("信息安全策略",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "组建信息等级说明失败", 0);
        }
        base.actionEnd(response, json ,result);
		return null;
	}

	public String insert() throws Exception {
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
		String json = null;
        try {
            json = securityLevelService.insert(securityLevel);
            logger.info("新增信息安全策略完成");
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "新增策略"+securityLevel.getLevelInfo()+"成功", 1);
        }catch (Exception e) {
            logger.error("信息安全策略", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "新策略"+securityLevel.getLevelInfo()+"失败", 0);
            json = "{success:true,msg:'新增失败'}";
        }
        securityLevel = null;
        base.actionEnd(response,json,result);
		return null;
	}

	public String delete() throws Exception {
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
		String json = null;
        try{
            json = securityLevelService.delete(ids);
            logger.info("删除信息安全策略完成");
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "删除策略成功", 1);
        }catch (Exception e) {
            logger.info("信息安全策略",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "删除策略失败", 0);
            json = "{success:true,msg:'删除失败'}";
        }
        base.actionEnd(response,json,result);
		return null;
	}

	public String update() throws Exception {
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
        String json = null;
        try{
            Account account = SessionUtils.getAccount(request);
            json = securityLevelService.update(securityLevel);
            logger.info("修改信息安全策略完成");
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "修改策略成功", 1);
        }catch (Exception e) {
            logger.info("信息安全策略",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "信息安全策略", "修改策略失败", 0);
            json = "{success:true,msg:'修改失败'}";
        }
        securityLevel = null;
        base.actionEnd(response,json,result);
		return null;
	}

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setSecurityLevelService(SecurityLevelService securityLevelService) {
        this.securityLevelService = securityLevelService;
    }

    public SecurityLevel getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(SecurityLevel securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
