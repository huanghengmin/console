package com.hzih.stp.web.action.config;

import com.hzih.stp.constant.AppConstant;
import com.hzih.stp.service.LogService;
import com.hzih.stp.utils.*;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报警配置
 */
public class AlertManagerAction extends ActionSupport{
    private static final Logger logger = Logger.getLogger(AlertManagerAction.class);

    private static final String fileName = System.getProperty("ichange.home") + AppConstant.XML_ALERT_CONFIG_PATH;
    private LogService logService;

    public String loadConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = null;
        try {
            SAXReader reader = new SAXReader();
//            String fileName = SiteContext.getInstance().contextRealPath + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));

            Map<Object, Object> model = new HashMap<Object, Object>();
            Node tempNode = document.selectSingleNode("//config/mailconfig/server");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/port");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/email");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/account");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/password");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/title");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/mailfrequency");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smsnumber");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smstitle");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smsfrequency");
            model.put(tempNode.getName(), tempNode.getText());
            String modelStr = model.toString();
            //{port=25, smsnumber=1006202102010, title=topwalk, email=andyhero168@sina.com, mailfrequency=10, smstitle=topwalk, account=andyhero168@sina.com, server=smtp.sina.com, smsfrequency=10, password=8701271314520}
            json = "{success:true,";
            String[] fields = modelStr.split("\\{")[1].split("\\}")[0].split(",");
            int index = 0;
            for (int i = 0; i < fields.length; i++) {
                if(index == fields.length - 1){
                    json += "'"+ fields[i].split("=")[0].trim()+"':'"+fields[i].split("=")[1].trim()+"'";
                }else{
                    json += "'"+ fields[i].split("=")[0].trim()+"':'"+fields[i].split("=")[1].trim()+"',";
                }
                index ++;
            }
		    json +="}";
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","获取报警配置信息成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","获取报警配置信息失败", 0);
        }
		base.actionEnd(response, json ,result);
        return null;
    }

    public String saveConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
//		String fileName = SiteContext.getInstance().contextRealPath	+ AppConstant.XML_ALERT_CONFIG_PATH;
        SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fileName));
		request.getCharacterEncoding();
		response.setCharacterEncoding("UTF-8");
		Element root = document.getRootElement();
		XMLWriter writer = null;
		try {
			Node tempNode = document.selectSingleNode("//config/mailconfig/server");
			tempNode.setText(request.getParameter("server"));
			tempNode = document.selectSingleNode("//config/mailconfig/port");
			tempNode.setText(request.getParameter("port"));
			tempNode = document.selectSingleNode("//config/mailconfig/email");
			tempNode.setText(request.getParameter("email"));
			tempNode = document.selectSingleNode("//config/mailconfig/account");
			tempNode.setText(request.getParameter("account"));
			tempNode = document.selectSingleNode("//config/mailconfig/password");
			tempNode.setText(request.getParameter("password"));
			tempNode = document.selectSingleNode("//config/mailconfig/title");
			tempNode.setText(request.getParameter("title"));
			tempNode = document.selectSingleNode("//config/mailconfig/mailfrequency");
			tempNode.setText(request.getParameter("mailfrequency"));
			tempNode = document.selectSingleNode("//config/smsconfig/smsnumber");
			tempNode.setText(request.getParameter("smsnumber"));
			tempNode = document.selectSingleNode("//config/smsconfig/smstitle");
			tempNode.setText(request.getParameter("smstitle"));
			tempNode = document.selectSingleNode("//config/smsconfig/smsfrequency");
			tempNode.setText(request.getParameter("smsfrequency"));

			OutputFormat format = OutputFormat.createPrettyPrint();

            format.setEncoding("UTF-8");

			writer = new XMLWriter(new FileOutputStream(fileName),format);

			writer.write(document);
			logger.info("write file success:" + fileName);
			writer.close();
            msg = "修改成功";
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","修改报警配置信息成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","修改报警配置信息失败", 0);
            msg = "修改失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String validateEmail() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        String subject = " 测试邮件 ";
		String text = " 这是一封测试邮件 ";
		String contact = request.getParameter("contact");

//		String fileName = SiteContext.getInstance().contextRealPath
//				+ AppConstant.XML_ALERT_CONFIG_PATH;
        SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fileName));
		try {
			Map<String, String> model = new HashMap<String, String>();
            Node tempNode = document.selectSingleNode("//config/mailconfig/server");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/port");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/email");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/account");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/password");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/title");
            model.put(tempNode.getName(), tempNode.getText());

            tempNode = document.selectSingleNode("//config/smsconfig/smsnumber");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smstitle");
            model.put(tempNode.getName(), tempNode.getText());
            logger.info(" 发送测试邮件...To: " + contact);

//            JavaMailUtil.sendMail(model, subject, text, contact);
            String emailServer = model.get("server");
            String connectName = model.get("account");
            String password = model.get("password");
//            port = sender.get("port");
            boolean isNeedAuth = true;
            String fromEmail = model.get("email");
            String toEmail = contact;
            String title = subject;
//            String text = "这是一封测试邮件";
            List<String> fileAffix = new ArrayList<String>();
//            fileAffix.add("D:/test/stp/1366074007109_0.tmp");
//            fileAffix.add("D:/test/stp/1366107099021_0.tmp");
//            fileAffix.add("D:/test/stp/1366531202249_0.tmp");
            boolean isSuccess = SendMail.sendMessage(emailServer,connectName,password,isNeedAuth,
                fromEmail,toEmail,title,text,fileAffix);
            if(isSuccess){
                msg = "测试邮件已发出,请注意查收!";
            } else {
                msg = "测试邮件发送失败";
            }
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","测试邮件发送报警信息成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","测试邮件发送报警信息失败", 0);
            msg = "测试邮件发送失败,请稍后再试!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }


    public String sendEmail() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        String subject = " 测试邮件 ";
		String text = " 这是一封测试邮件 ";
		String contact = request.getParameter("contact");

		SAXReader reader = new SAXReader();
//		String fileName = SiteContext.getInstance().contextRealPath
//				+ AppConstant.XML_ALERT_CONFIG_PATH;
		Document document = reader.read(new File(fileName));
		try {
			Map<String, String> model = new HashMap<String, String>();
            Node tempNode = document.selectSingleNode("//config/mailconfig/server");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/port");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/email");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/account");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/password");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/title");
            model.put(tempNode.getName(), tempNode.getText());

            tempNode = document.selectSingleNode("//config/smsconfig/smsnumber");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smstitle");
            model.put(tempNode.getName(), tempNode.getText());
            logger.info(" 发送测试邮件...To: " + contact);

//            JavaMailUtil.sendMail(model, subject, text, contact);
            String emailServer = model.get("server");
            String connectName = model.get("account");
            String password = model.get("password");
//            port = sender.get("port");
            boolean isNeedAuth = true;
            String fromEmail = model.get("email");
            String toEmail = contact;
            String title = subject;
//            String text = "这是一封测试邮件";
            List<String> fileAffix = new ArrayList<String>();
//            fileAffix.add("D:/test/stp/1366074007109_0.tmp");
//            fileAffix.add("D:/test/stp/1366107099021_0.tmp");
//            fileAffix.add("D:/test/stp/1366531202249_0.tmp");
            boolean isSuccess = SendMail.sendMessage(emailServer,connectName,password,isNeedAuth,
                fromEmail,toEmail,title,text,fileAffix);
            if(isSuccess){
                msg = "测试邮件已发出,请注意查收!";
            } else {
                msg = "测试邮件发送失败";
            }
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","测试邮件发送报警信息成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","测试邮件发送报警信息失败", 0);
            msg = "测试邮件发送失败,请稍后再试!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String validateShortMessage() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        String subject = " 测试短信 ";
		String text = " 测试短信 ";
		String contact = request.getParameter("contact");

		SAXReader reader = new SAXReader();
//		String fileName = SiteContext.getInstance().contextRealPath
//				+ AppConstant.XML_ALERT_CONFIG_PATH;
		Document document = reader.read(new File(fileName));
		try {
			Map<String, String> model = new HashMap<String, String>();
            Node tempNode = document.selectSingleNode("//config/mailconfig/server");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/port");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/email");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/account");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/password");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/title");
            model.put(tempNode.getName(), tempNode.getText());

            tempNode = document.selectSingleNode("//config/smsconfig/smsnumber");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smstitle");
            model.put(tempNode.getName(), tempNode.getText());
            logger.info(" 发送测试短信...To: " + contact);

            JavaShortMessageUtil.sendMessage(model, subject, text, contact);

            msg = "测试短信已发出,请注意查收!";
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","测试短信发送报警信息成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","测试短信发送报警信息失败", 0);
            msg = "测试短信发送失败,请稍后再试!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String queryEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = null;
        try {
            int start = Integer.parseInt(request.getParameter("start"));
            int limit = Integer.parseInt(request.getParameter("limit"));
            json = getQueryEmails(start,limit);
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","查找收件人列表信息成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","查找收件人列表信息失败", 0);
            json = "{success:true:total:0,rows:[]}";
        }
		base.actionEnd(response, json ,result);
        return null;
    }

    private String getQueryEmails(int start, int limit) throws DocumentException {
        SAXReader reader = new SAXReader();
//		String fileName = SiteContext.getInstance().contextRealPath
//				+ AppConstant.XML_ALERT_CONFIG_PATH;
		Document document = reader.read(new File(fileName));
        List<Node> emails = document.selectNodes("//config/mailconfig/emails/email");
        int index = 0;
        int count = 0;
        String json = "{success:true,total:"+emails.size()+",rows:[";
        for(Node email : emails){
            if(index==start && count < limit) {
                json += "{email:'"+email.getText()+"',flag:2},";
                start ++;
                count ++;
            }
            index ++;
        }
        json += "]}";
        return json;
    }

    public String checkEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        try {
            SAXReader reader = new SAXReader();
//            String fileName = SiteContext.getInstance().contextRealPath
//                    + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));
            List<Node> emails = document.selectNodes("//config/mailconfig/emails/email");
            String email = request.getParameter("email");
            for(Node e : emails){
                if(e.getText().equals(email)) {
                    msg = "Email已经存在";
                    break;
                }
            }
            if(msg == null) {
                msg = "0000";
            }
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","校验email成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","校验email失败", 0);
            msg = "校验email失败,请稍后再试!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String deleteEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        try {
            SAXReader reader = new SAXReader();
//            String fileName = SiteContext.getInstance().contextRealPath
//                    + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));
            Element emails = (Element) document.selectSingleNode("//config/mailconfig/emails");
            String[] emailArray = request.getParameterValues("emailArray");
            for(int i=0;i<emailArray.length;i++) {
                List<Element> _emails = document.selectNodes("//config/mailconfig/emails/email");
                for(Element e : _emails) {
                    if(emailArray[i].equals(e.getText())) {
                        emails.remove(e);
                        break;
                    }
                }
            }
            File file = new File(fileName);
            FileInputStream fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            while (fin.read(bytes) < 0) fin.close();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter output = new XMLWriter(new FileOutputStream(file),format);
            if(document != null){
                output.write(document);
            }
            output.close();
            try {
                String[][] params = new String[][] {
                        { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                        { "Command", "restartservice" },
                        { "restarttype", "email" }
                };
                ServiceResponse serviceResponse = ServiceUtil.callService(params);
                if(serviceResponse.getCode()==200) {
                    msg = "新增成功,重启邮件服务成功,</br>点击[确定]返回列表!";
                } else {
                    msg = "新增成功,重启邮件服务失败,</br>点击[确定]返回列表!";
                }
            } catch (Exception ex) {
                msg = "新增成功,重启邮件服务失败,</br>点击[确定]返回列表!";
                logger.error("重启邮件服务失败", ex);
            }
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","用删除Email地址成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","删除Email地址失败", 0);
            msg = "删除Email地址失败,请稍后再试!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String insertEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        try {
            SAXReader reader = new SAXReader();
//            String fileName = SiteContext.getInstance().contextRealPath
//                    + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));
            Element emails = (Element) document.selectSingleNode("//config/mailconfig/emails");
            String[] emailArray = request.getParameterValues("emailArray");
            for(int i=0;i<emailArray.length;i++) {
                List<Element> _emails = document.selectNodes("//config/mailconfig/emails/email");
                boolean isExist = false;
                for(Element e : _emails) {
                    if(emailArray[i].equals(e.getText())) {
                        isExist = true;
                        break;
                    }
                }
                if(!isExist){
                    Element child = emails.addElement("email");
                    child.setText(emailArray[i]);
                }
            }
            File file = new File(fileName);
            FileInputStream fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            while (fin.read(bytes) < 0) fin.close();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter output = new XMLWriter(new FileOutputStream(file),format);
            if(document != null){
                output.write(document);
            }
            output.close();
            try {
                String[][] params = new String[][] {
                        { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                        { "Command", "restartservice" },
                        { "restarttype", "email" }
                };
                ServiceResponse serviceResponse = ServiceUtil.callService(params);
                if(serviceResponse.getCode()==200) {
                    msg = "删除成功,重启邮件服务成功,</br>点击[确定]返回列表!";
                } else {
                    msg = "删除成功,重启邮件服务失败,</br>点击[确定]返回列表!";
                }
            } catch (Exception ex) {
                msg = "删除成功,重启邮件服务失败,</br>点击[确定]返回列表!";
                logger.error("重启邮件服务失败", ex);
            }
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","新增Email地址成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","新增Email地址失败", 0);
            msg = "新增Email地址失败!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String updateEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        try {
            SAXReader reader = new SAXReader();
//            String fileName = SiteContext.getInstance().contextRealPath
//                    + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));
            String oldEmail = request.getParameter("oldEmail");
            String email = request.getParameter("email");
            List<Element> emails = document.selectNodes("//config/mailconfig/emails/email");
            for(Element e : emails) {
                if(oldEmail.equals(e.getText())) {
                    e.setText(email);
                    break;
                }
            }
            File file = new File(fileName);
            FileInputStream fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            while (fin.read(bytes) < 0) fin.close();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter output = new XMLWriter(new FileOutputStream(file),format);
            if(document != null){
                output.write(document);
            }
            output.close();
            try {
                String[][] params = new String[][] {
                        { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                        { "Command", "restartservice" },
                        { "restarttype", "email" }
                };
                ServiceResponse serviceResponse = ServiceUtil.callService(params);
                if(serviceResponse.getCode()==200) {
                    msg = "修改成功,重启邮件服务成功,</br>点击[确定]返回列表!";
                } else {
                    msg = "修改成功,重启邮件服务失败,</br>点击[确定]返回列表!";
                }
            } catch (Exception ex) {
                msg = "修改成功,重启邮件服务失败,</br>点击[确定]返回列表!";
                logger.error("重启邮件服务失败", ex);
            }
            logService.newManagerLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","修改Email地址成功", 1);
        } catch (Exception e) {
            logger.error("报警配置", e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "报警配置","修改Email地址失败", 0);
            msg = "修改Email地址失败,请稍后再试!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
