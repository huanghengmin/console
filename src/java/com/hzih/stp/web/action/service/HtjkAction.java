package com.hzih.stp.web.action.service;

import com.hzih.stp.dao.HtjkDao;
import com.hzih.stp.domain.Account;
import com.hzih.stp.domain.Htjk;
import com.hzih.stp.service.AccountService;
import com.hzih.stp.service.LogService;
import com.hzih.stp.service.LoginService;
import com.hzih.stp.web.action.ActionBase;
import com.inetec.common.security.DesEncrypterAsPassword;
import com.inetec.ichange.console.config.Constant;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2015/12/2.
 */
public class HtjkAction extends ActionSupport {
    private HtjkDao htjkDao;
    private LogService logService;
    private LoginService loginService;

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public HtjkDao getHtjkDao() {
        return htjkDao;
    }

    public void setHtjkDao(HtjkDao htjkDao) {
        this.htjkDao = htjkDao;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String findPerson()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Htjk htjk = htjkDao.findById(1);
        StringBuilder sb = new StringBuilder();
        sb.append("{success:true,'totalCount':").append(1).append(",'rows':[");
        sb.append("{");
        sb.append("id:'").append(htjk.getId()).append("'").append(",");
        sb.append("name:'").append(htjk.getName()).append("'").append(",");
        sb.append("info:'").append(htjk.getInfo()).append("'").append(",");
        sb.append("status:'").append(htjk.getStatus()).append("'");
        sb.append("}");
        sb.append("]}");
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String modifyPersonOpen()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Htjk htjk = htjkDao.findById(1);
        htjk.setStatus(1);
        htjkDao.modify(htjk);
        String msg = "开启人口信息后台服务接口成功";
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String modifyPersonClose()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Htjk htjk = htjkDao.findById(1);
        htjk.setStatus(0);
        htjkDao.modify(htjk);
        String msg = "停止人口信息后台服务接口成功";
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String modifyPhotoOpen()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Htjk htjk = htjkDao.findById(2);
        htjk.setStatus(1);
        htjkDao.modify(htjk);
        String msg = "开启照片信息后台服务接口成功";
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String modifyPhotoClose()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Htjk htjk = htjkDao.findById(2);
        htjk.setStatus(0);
        htjkDao.modify(htjk);
        String msg = "停止照片信息后台服务接口成功";
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findPhoto()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Htjk htjk = htjkDao.findById(2);
        StringBuilder sb = new StringBuilder();
        sb.append("{success:true,'totalCount':").append(1).append(",'rows':[");
        sb.append("{");
        sb.append("id:'").append(htjk.getId()).append("'").append(",");
        sb.append("name:'").append(htjk.getName()).append("'").append(",");
        sb.append("info:'").append(htjk.getInfo()).append("'").append(",");
        sb.append("status:'").append(htjk.getStatus()).append("'");
        sb.append("}");
        sb.append("]}");
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String person()throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String GMSFHM = request.getParameter("GMSFHM");
        String username = request.getParameter("YHM");
        String password = request.getParameter("MM");
        String YHM = "064740";
        String MM = "111111";
        String json = "{\"CODE\":\"1\"}";
        String FWID = "ntga2015100029";
        Htjk htjk = htjkDao.findById(1);
        if (htjk != null && htjk.getStatus() == 1) {
            DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
            String pwd = new String(deap.encrypt(password.getBytes()));
            Account account = loginService.getAccountByNameAndPwd(username, pwd);
            if (account != null) {
                String URL = "http://10.36.11.93:10017/ntgajk/requestData.do";
                json = InterfaceUtils.getMsgForGmsfhm(FWID, GMSFHM, YHM, MM, URL);
                logService.newLog("INFO", YHM, "后台服务", "后台人口数据请求成功," + FWID + "," + GMSFHM + "," + YHM + "," + MM + request.getRemoteAddr(), 1);
                writer.write(json);
                writer.close();
            } else {
                logService.newLog("ERROR", YHM, "后台服务", "用户不存在" +username+","+ FWID + "," + GMSFHM + "," + YHM + "," + MM + request.getRemoteAddr(), 1);
                json = "{\"CODE\":\"8\"}";
                writer.write(json);
                writer.close();
            }
        } else {
            logService.newLog("ERROR", YHM, "后台服务", "后台人口服务已禁止访问" + FWID + "," + GMSFHM + "," + YHM + "," + MM + request.getRemoteAddr(), 1);
            json = "{\"CODE\":\"11\"}";
            writer.write(json);
            writer.close();
        }
        return null;
    }

    public String photo()throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String GMSFHM = request.getParameter("GMSFHM");
//        String YHM = request.getParameter("YHM");
//        String MM = request.getParameter("MM");
        String username = request.getParameter("YHM");
        String password = request.getParameter("MM");
        String json = "{\"CODE\":\"1\"}";
        String YHM = "064740";
        String MM = "111111";
        String FWID = "ntga2015100030";
        Htjk htjk = htjkDao.findById(2);
        if(htjk!=null&&htjk.getStatus()==1) {
            DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
            String pwd = new String(deap.encrypt(password.getBytes()));
            Account account = loginService.getAccountByNameAndPwd(username, pwd);
            if (account != null) {
                String URL = "http://10.36.11.93:10017/ntgajk/requestData.do";
                json = InterfaceUtils.getMsgForGmsfhm(FWID, GMSFHM, YHM, MM, URL);
                logService.newLog("INFO", YHM, "后台服务", "后台照片数据请求成功," + FWID + "," + GMSFHM + "," + YHM + "," + MM + request.getRemoteAddr(), 1);
                writer.write(json);
                writer.close();
            }else {
                logService.newLog("ERROR", YHM, "后台服务", "用户不存在" +username+","+ FWID + "," + GMSFHM + "," + YHM + "," + MM + request.getRemoteAddr(), 1);
                json = "{\"CODE\":\"8\"}";
                writer.write(json);
                writer.close();
            }
        }else {
            logService.newLog("ERROR", YHM, "后台服务", "后台照片服务已禁止访问"+FWID+","+GMSFHM+","+YHM+","+MM+request.getRemoteAddr(), 1);
            json = "{\"CODE\":\"11\"}";
            writer.write(json);
            writer.close();
        }
        return null;
    }
}
