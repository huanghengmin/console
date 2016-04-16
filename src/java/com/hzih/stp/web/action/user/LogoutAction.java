package com.hzih.stp.web.action.user;

import com.hzih.stp.domain.Account;
import com.hzih.stp.service.LogService;
import com.hzih.stp.web.SessionUtils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

/**
 *   用户退出系统
 */

public class LogoutAction extends ActionSupport {
	private LogService logService;

	public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		Account account = SessionUtils.getAccount(request);
		String userName = null;
		if(account!=null){
			userName = account.getUserName();
			SessionUtils.removeAccount(request);
			SessionUtils.invalidateSession(request);
            if(isUser(account)){
                logService.newLog("INFO", userName, "登录", "退出成功", 1);
            } else {
                logService.newManagerLog("INFO", userName, "登录", "退出成功", 1);

            }
        }
		return "success";
	}

    private boolean isUser(Account account) throws Exception {
        if(account.getAccountType()==2){
            return true;
        }
        return false;
    }

	public void setLogService(LogService logService) {
		this.logService = logService;
	}
}
