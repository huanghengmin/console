package com.hzih.stp.web.action.config;

import com.hzih.stp.entity.BackUp;
import com.hzih.stp.service.LogService;
import com.hzih.stp.utils.NetUtils;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 13-5-12
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class BackupAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(BackupAction.class);
    private LogService logService;
    private BackUp backUp;
    public String query() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        try{
            BackUp bUp = BackUp.readBackUp();
            /*boolean isMainStatus = checkHotBack();
            if(!String.valueOf(isMainStatus).equals(bUp.getMainStatus())){
                bUp.setMainStatus(String.valueOf(isMainStatus));
                BackUp.updateBase(bUp);
            }*/
            json = "{success:true,total:1,rows:[{isActive:"+bUp.isActive()+
                    ",isMainSystem:"+bUp.isMainSystem()+",mainIp:'"+bUp.getMainIp()+
                    "',mainPort:"+bUp.getMainPort()+",mainStatus:" + bUp.getMainStatus() +",backupIp:'"+bUp.getBackupIp()+
                    "',backupPort:"+bUp.getBackupPort()+",backupStatus:"+bUp.getBackupStatus()+"}]}";
            logService.newManagerLog("info", SessionUtils.getAccount(request).getUserName(),"双击热备","查找双机热备配置信息成功", 1);
        } catch (Exception e) {
            logger.error("查找双击热备错误", e);
            logService.newManagerLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", "查找双机热备配置信息失败", 0);
            json = "{success:true,total:0,rows:[]}";
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    private boolean checkHotBack() {
        return NetUtils.isTelnetOk("127.0.0.1",8088);
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try{
            String active = request.getParameter("active");
            backUp.setActive("on".equals(active));
            BackUp.updateBase(backUp);
            msg = "更新成功";
            logService.newManagerLog("info", SessionUtils.getAccount(request).getUserName(),"双击热备","更新双机热备配置信息成功", 1);
        } catch (Exception e) {
            logger.error("更新双击热备信息错误", e);
            logService.newManagerLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", "更新双机热备配置信息失败", 0);
            msg = "更新失败";
        }
        actionBase.actionEnd(response,"{success:true,msg:'"+msg+"'}",result);
        return null;
    }

    public String queryList() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        try{
            String type = request.getParameter("type");
            int start = Integer.parseInt(request.getParameter("start"));
            int limit = Integer.parseInt(request.getParameter("limit"));
            BackUp bUp = BackUp.readBackUp();
            List<String> list = null;
            if("ping".equals(type)) {
                list = bUp.getPings();
            }  else if("telnet".equals(type)) {
                list = bUp.getTelnets();
            }  else if ("other".equals(type)) {
                list = bUp.getOthers();
            }
            json = listToJson(list, start, limit);
            logService.newManagerLog("info", SessionUtils.getAccount(request).getUserName(),"双击热备","查找双机热备配置信息成功", 1);
        } catch (Exception e) {
            logger.error("查找双击热备错误", e);
            logService.newManagerLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", "查找双机热备配置信息失败", 0);
            json = "{success:true,total:0,rows:[]}";
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    private String listToJson(List<String> list, int start, int limit) {
        String json = "{success:true,total:"+list.size()+",rows:[";
        int index = 0;
        int count = 0;
        for(String ip:list) {
            if(index==start && count<=limit) {
                json += "{ip:'"+ip+"',flag:2},";
                start ++;
                count ++;
            }
            index ++;
        }
        json += "]}";
        return json;
    }

    public String updateList() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try{
            String type = request.getParameter("type");
            String old = request.getParameter("old");
            String l = request.getParameter("l");
            List<String> list = new ArrayList<String>();
            list.add(old);
            list.add(l);
            BackUp.delete(list,type);
            List<String> list2 = new ArrayList<String>();
            list2.add(l);
            BackUp.insert(list2,type);
            msg = "更新成功";
            logService.newManagerLog("info", SessionUtils.getAccount(request).getUserName(),"双击热备","更新双机热备配置信息成功", 1);
        } catch (Exception e) {
            logger.error("更新双击热备信息错误", e);
            logService.newManagerLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", "更新双机热备配置信息失败", 0);
            msg = "更新失败";
        }
        actionBase.actionEnd(response,"{success:true,msg:'"+msg+"'}",result);
        return null;
    }

    public String insertList() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try{
            String type = request.getParameter("type");
            String[] array = request.getParameterValues("array");
            List<String> list = new ArrayList<String>();
            for (int i=0;i<array.length;i++) {
                list.add(array[i]);
            }
            BackUp.delete(list,type);
            BackUp.insert(list,type);
            msg = "新增成功";
            logService.newManagerLog("info", SessionUtils.getAccount(request).getUserName(),"双击热备","新增双机热备配置信息成功", 1);
        } catch (Exception e) {
            logger.error("更新双击热备信息错误", e);
            logService.newManagerLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", "新增双机热备配置信息失败", 0);
            msg = "新增失败";
        }
        actionBase.actionEnd(response,"{success:true,msg:'"+msg+"'}",result);
        return null;
    }

    public String deleteList() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try{
            String type = request.getParameter("type");
            String[] array = request.getParameterValues("array");
            List<String> list = new ArrayList<String>();
            for (int i=0;i<array.length;i++) {
                list.add(array[i]);
            }
            BackUp.delete(list, type);
            msg = "删除成功";
            logService.newManagerLog("info", SessionUtils.getAccount(request).getUserName(),"双击热备","删除双机热备配置信息成功", 1);
        } catch (Exception e) {
            logger.error("更新双击热备信息错误", e);
            logService.newManagerLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", "删除双机热备配置信息失败", 0);
            msg = "删除失败";
        }
        actionBase.actionEnd(response,"{success:true,msg:'"+msg+"'}",result);
        return null;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public BackUp getBackUp() {
        return backUp;
    }

    public void setBackUp(BackUp backUp) {
        this.backUp = backUp;
    }
}
