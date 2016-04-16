package com.hzih.stp.web.action.config;

import com.hzih.stp.domain.Iptables;
import com.hzih.stp.service.IptablesService;
import com.hzih.stp.service.LogService;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-30
 * Time: 下午3:46
 * To change this template use File | Settings | File Templates.
 */
public class ManagerIptablesAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(ManagerIptablesAction.class);
    private LogService logService;
    private IptablesService iptablesService;
    private Iptables iptables;
    private Iptables old_iptables;

    private static String str_ssh = "ssh";
    private static String str_webmin = "webmin";
//    private int start;
//    private int limit;


    /**
     * select allow ips throw type(ssh , webmin)
     * @return
     * @throws Exception
     */
    public String select() throws Exception {
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
        String json =null;
        try{
            String type = request.getParameter("type");
            if(type != null && str_ssh.equalsIgnoreCase(type)) {
                json = iptablesService.selectSSH();
            }else if(type != null && str_webmin.equalsIgnoreCase(type)){
                json = iptablesService.selectWebmin();
            }
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "查看防火墙策略成功", 1);
        }catch (Exception e){
            logger.error("防火墙策略",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "查看防火墙策略失败", 0);
        }
        base.actionEnd(response, json ,result);
        return null;
    }

    public String insert() throws Exception {
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
        String json = "{success:true,msg:'新增失败'}";
        try{
            String type = request.getParameter("type");
            String allow_ip = request.getParameter("allow_ip");
            if(allow_ip != null && type != null && str_ssh.equalsIgnoreCase(type)){
                json = iptablesService.insertSSH(null,allow_ip);
            }else if (allow_ip != null && type != null && str_webmin.equalsIgnoreCase(type)){
                json = iptablesService.insertWebmin(null,allow_ip);
            }
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "新增防火墙策略成功", 1);
        }catch (Exception e){
            logger.error("防火墙策略",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "新增防火墙策略失败", 0);
        }finally {
            iptables = null;
        }
        base.actionEnd(response, json ,result);
        return null;
    }

    public String update() throws Exception {
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
        String json = "{success:true,msg:'新增失败'}";
        try{
            String type = request.getParameter("type");
            String old_ip =request.getParameter("old_ip");
            String allow_ip = request.getParameter("allow_ip");
            if(old_ip != null && allow_ip != null && type != null && str_ssh.equalsIgnoreCase(type)){
                json = iptablesService.insertSSH(old_ip,allow_ip);
            }else if (old_ip != null && allow_ip != null && type != null && str_webmin.equalsIgnoreCase(type)){
                json = iptablesService.insertWebmin(old_ip,allow_ip);
            }
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "更新防火墙策略成功", 1);
        }catch (Exception e){
            logger.error("防火墙策略",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "更新防火墙策略失败", 0);
        }finally {
            iptables=null;
            old_iptables = null;
        }
        base.actionEnd(response, json ,result);
        return null;
    }

    public String delete() throws Exception {
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
        String json = "{success:true,msg:'删除失败'}";
        try{
            String type = request.getParameter("type");
            String allow_ip = request.getParameter("allow_ip");
            if(allow_ip != null && type != null && str_ssh.equalsIgnoreCase(type)){
                json = iptablesService.deleteSSH(allow_ip);
            }else if (allow_ip != null && type != null && str_webmin.equalsIgnoreCase(type)){
                json = iptablesService.deleteWebmin(allow_ip);
            }
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "删除防火墙策略成功", 1);
        }catch (Exception e){
            logger.error("防火墙策略",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "删除防火墙策略失败", 0);
        }finally {
            iptables=null;
        }
        base.actionEnd(response, json ,result);
        return null;
    }

    public String application() throws Exception{
        ActionBase base = new ActionBase();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result =	base.actionBegin(request);
        String json = "{success:true,msg:'应用失败'}";
        try{
            String type = request.getParameter("type");
            if( type != null && str_ssh.equalsIgnoreCase(type)){
                //todo
            }else if ( type != null && str_webmin.equalsIgnoreCase(type)){
                json = iptablesService.restartWebmin();
            }
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "查看防火墙策略成功", 1);
        }catch (Exception e){
            logger.error("防火墙策略",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "防火墙策略", "查看防火墙策略失败", 0);
        }finally {
            iptables=null;
        }
        base.actionEnd(response, json ,result);
        return null;
    }


    public Iptables getIptables() {
        return iptables;
    }

    public void setIptables(Iptables iptables) {
        this.iptables = iptables;
    }

    public IptablesService getIptablesService() {
        return iptablesService;
    }

    public void setIptablesService(IptablesService iptablesService) {
        this.iptablesService = iptablesService;
    }

    /*public int getStart() {
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
    }*/

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public Iptables getOld_iptables() {
        return old_iptables;
    }

    public void setOld_iptables(Iptables old_iptables) {
        this.old_iptables = old_iptables;
    }
}
