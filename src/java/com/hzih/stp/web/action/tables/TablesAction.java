package com.hzih.stp.web.action.tables;

import com.hzih.stp.service.LogService;
import com.hzih.stp.utils.Dom4jUtil;
import com.hzih.stp.utils.StringContext;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 15-9-22.
 */
public class TablesAction extends ActionSupport {

    private Logger logger = Logger.getLogger(TablesAction.class);
    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String findColumnStore()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("table_name");
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        StringBuilder sb = new StringBuilder();
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/tables/table[@name='" + name + "']");
            if (selectSingleNode != null) {
                List<Element> elements = selectSingleNode.elements("column");
                sb.append("{success:true,'list':").append(elements.size()).append(",'rows':[");
                for (int i = 0; i < elements.size(); i++) {
                    Element element = elements.get(i);
                    String column_name = element.attributeValue("name");
                    String id = element.attributeValue("id");
                    Element datatype = element.element("datatype");
                    Element column_comment = element.element("comment");
                    sb.append("{");
                    sb.append("id:'").append(id).append("'").append(",");
                    sb.append("Name:'").append(column_name).append("'").append(",");
                    sb.append("DataType:'").append(datatype.getText()).append("'").append(",");
                    sb.append("tj_checked:'").append("0").append("'").append(",");
                    sb.append("zx_checked:'").append("0").append("'").append(",");
                    sb.append("Comment:'").append(column_comment.getText()).append("'");
                    sb.append("}");
                    if (i != (elements.size() - 1)) {
                        sb.append(",");
                    }
                }
                sb.append("]}");
            }
        }
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String findStore()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        StringBuilder sb = new StringBuilder();
        if (doc != null) {
            List<Element> services = doc.selectNodes("/tables/table");
            if (services != null) {
                sb.append("{success:true,'totalCount':").append(services.size()).append(",'rows':[");
                for (int i = 0; i < services.size(); i++) {
                    Element element = services.get(i);
                    String table_name = element.attributeValue("name");
                    String comment = element.attributeValue("comment");
                    sb.append("{");
                    sb.append("value:'").append(table_name).append("'").append(",");
                    sb.append("key:'").append(comment).append("'");
                    sb.append("}");

                    if (i != (services.size() - 1)) {
                        sb.append(",");
                    }
                }
                sb.append("]}");
            }

        }
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("table_name");
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        StringBuilder sb = new StringBuilder();
        if (doc != null) {
            if (name != null) {
                List<Element> selectSingleNodes = doc.selectNodes("/tables/table[contains(@name,"+name+")]");
                if(selectSingleNodes!=null){
                    sb.append("{success:true,'list':").append(selectSingleNodes.size()).append(",'rows':[");
                    for (int i = 0; i < selectSingleNodes.size(); i++) {
                        Element element = selectSingleNodes.get(i);
                        String table_name = element.attributeValue("name");
                        String type = element.attributeValue("type");
                        String comment = element.attributeValue("comment");
                        sb.append("{");
                        sb.append("table_name:'").append(table_name).append("'").append(",");
                        sb.append("type:'").append(type).append("'").append(",");
                        sb.append("comment:'").append(comment).append("'");
                        sb.append("}");
                        if (i != (selectSingleNodes.size() - 1)) {
                            sb.append(",");
                        }
                    }
                    sb.append("]}");
                }
            } else {
                List<Element> services = doc.selectNodes("/tables/table");
                if (services != null) {
                    sb.append("{success:true,'list':").append(services.size()).append(",'rows':[");
                    for (int i = 0; i < services.size(); i++) {
                        Element element = services.get(i);
                        String table_name = element.attributeValue("name");
                        String type = element.attributeValue("type");
                        String comment = element.attributeValue("comment");
                        sb.append("{");
                        sb.append("table_name:'").append(table_name).append("'").append(",");
                        sb.append("type:'").append(type).append("'").append(",");
                        sb.append("comment:'").append(comment).append("'");
                        sb.append("}");
                        if (i != (services.size() - 1)) {
                            sb.append(",");
                        }
                    }
                    sb.append("]}");
                }
            }
        }
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String findTable() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("table_name");
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        StringBuilder sb = new StringBuilder();
        if (doc != null) {
            if (name != null) {
                Element selectSingleNode = (Element) doc.selectSingleNode("/tables/table[@name='" + name + "']");
                if(selectSingleNode!=null){
                    List<Element> elements = selectSingleNode.elements("column");
                    sb.append("{success:true,'list':").append(elements.size()).append(",'rows':[");
                    for (int i = 0; i < elements.size(); i++) {
                        Element element = elements.get(i);
                        String column_name = element.attributeValue("name");
                        String id = element.attributeValue("id");
                        Element datatype = element.element("datatype");
                        Element column_comment = element.element("comment");
                        sb.append("{");
                        sb.append("id:'").append(id).append("'").append(",");
                        sb.append("Name:'").append(column_name).append("'").append(",");
                        sb.append("DataType:'").append(datatype.getText()).append("'").append(",");
                        sb.append("Comment:'").append(column_comment.getText()).append("'");
                        sb.append("}");
                        if (i != (elements.size() - 1)) {
                            sb.append(",");
                        }
                    }
                    sb.append("]}");
                }
            }
        }
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String add() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        String table_name = request.getParameter("table_name");
        String table_des = request.getParameter("table_des");
        String data = request.getParameter("data");
        String msg = null;
        String json = null;
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/tables/table[@name='" + table_name + "']");
            if (selectSingleNode != null) {
                msg = "表名已在在，请更改表名！";
                json = "{success:false,msg:'" + msg + "'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
            } else {
                Element rootElement = doc.getRootElement();
                Element table = rootElement.addElement("table");
                table.addAttribute("name", table_name);
                table.addAttribute("type", "1");
                table.addAttribute("comment", table_des);
                JSONArray array = JSONArray.fromObject(data);
                for (int i = 0;i<array.size();i++) {
                    Object object = array.get(i);
                    JSONObject o = JSONObject.fromObject(object);

                    Element column = table.addElement("column");
                    column.addAttribute("id", String.valueOf(i));
                    column.addAttribute("name", o.getString("Name"));

                    Element column_datatype = column.addElement("datatype");
                    column_datatype.setText(o.getString("DataType"));

                    Element column_comment = column.addElement("comment");
                    column_comment.setText(o.getString("Comment"));
                }
                Dom4jUtil.writeDocumentToFile(doc, StringContext.tables_xml);
                msg = "添加表信息成功"+table_name;
                json = "{success:true,msg:'" + msg + "'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 1);
            }
        } else {
            Document document = DocumentHelper.createDocument();
            Element config = document.addElement("tables");
            Element table = config.addElement("table");
            table.addAttribute("name", table_name);
            table.addAttribute("type", "1");
            table.addAttribute("comment", table_des);
            JSONArray array = JSONArray.fromObject(data);
            for (int i = 0;i<array.size();i++) {
                Object object = array.get(i);
                JSONObject o = JSONObject.fromObject(object);

                Element column = table.addElement("column");
                column.addAttribute("id", String.valueOf(i));
                column.addAttribute("name", o.getString("Name"));

                Element column_datatype = column.addElement("datatype");
                column_datatype.setText(o.getString("DataType"));

                Element column_comment = column.addElement("comment");
                column_comment.setText(o.getString("Comment"));
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            format.setIndent(true);
            try {
                XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.tables_xml)), format);
                try {
                    xmlWriter.write(document);
                    msg = "添加表信息成功"+table_name;
                    json = "{success:true,msg:'" + msg + "'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 1);
                } catch (IOException e) {
                    msg = "添加表信息失败,出现异常";
                    json = "{success:false,msg:'" + msg + "'}";
                    logger.info(e.getMessage(), e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
                } finally {
                    try {
                        xmlWriter.flush();
                        xmlWriter.close();
                    } catch (IOException e) {
                        msg = "添加表信息失败,出现异常";
                        json = "{success:false,msg:'" + msg + "'}";
                        logger.info(e.getMessage(), e);
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                msg = "添加表信息失败,出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                logger.info(e.getMessage(), e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
            } catch (FileNotFoundException e) {
                msg = "添加表信息失败,出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String del() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        String table_name = request.getParameter("table_name");
        String msg = null;
        String json = null;
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/tables/table[@name='" + table_name + "']");
            if (selectSingleNode != null) {
                Element root = doc.getRootElement();
                boolean flag = root.remove(selectSingleNode);
                if (flag) {
                    Dom4jUtil.writeDocumentToFile(doc, StringContext.tables_xml);
                    msg = "删除表成功";
                    json = "{success:true,msg:'" + msg + "'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 1);
                } else {
                    msg = "删除表失败";
                    json = "{success:false,msg:'" + msg + "'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
                }
            } else {
                msg = "删除失败,未找到指定数据表";
                json = "{success:false,msg:'" + msg + "'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
            }
        } else {
            msg = "删除失败,未找到指定数据表";
            json = "{success:false,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String mod_delColumn() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        String table_name = request.getParameter("table_name");
        String id = request.getParameter("id");
        String msg = null;
        String json = null;
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/tables/table[@name='" + table_name + "']/column[@id='" + id + "']");
            if(selectSingleNode!=null){
               Element parent = selectSingleNode.getParent();
                boolean flag = parent.remove(selectSingleNode);
                if (flag) {
                    Dom4jUtil.writeDocumentToFile(doc, StringContext.tables_xml);
                    msg = "删除字段成功";
                    json = "{success:true,msg:'" + msg + "'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 1);
                } else {
                    msg = "删除字段失败";
                    json = "{success:false,msg:'" + msg + "'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
                }
            }
        } else {
            msg = "删除失败,未找到指定数据表";
            json = "{success:false,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String mod() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        String table_name = request.getParameter("table_name");
        String table_des = request.getParameter("table_des");
        String data = request.getParameter("data");
        String msg = null;
        String json = null;
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/tables/table[@name='" + table_name + "']");
            if (selectSingleNode != null) {
                JSONArray array = JSONArray.fromObject(data);
                if(array.size()==0){
                    String old_name = selectSingleNode.attributeValue("name");
                    String old_comment = selectSingleNode.attributeValue("comment");
                    if(table_name.equals(old_name)&&table_des.equals(old_comment)){
                        msg = "数据表信息未修改,请修改后保存";
                        json = "{success:false,msg:'" + msg + "'}";
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
                    }else {
                        selectSingleNode.addAttribute("name", table_name);
                        selectSingleNode.addAttribute("type", "1");
                        selectSingleNode.addAttribute("comment", table_des);
                        Dom4jUtil.writeDocumentToFile(doc, StringContext.tables_xml);
                        msg = "数据表信息修改成功";
                        json = "{success:true,msg:'" + msg + "'}";
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 1);
                    }
                }else {
                    String old_name = selectSingleNode.attributeValue("name");
                    String old_comment = selectSingleNode.attributeValue("comment");
                    if(!table_name.equals(old_name)||!table_des.equals(old_comment)){
                        selectSingleNode.addAttribute("name", table_name);
                        selectSingleNode.addAttribute("type", "1");
                        selectSingleNode.addAttribute("comment", table_des);
                    }
                    for (int i = 0;i<array.size();i++) {
                        Object object = array.get(i);
                        JSONObject o = JSONObject.fromObject(object);
                        String id = o.getString("id");
                        if(id!=null&&!id.equals("null")){
                            Element old_node = (Element) doc.selectSingleNode("/tables/table[@name='" + table_name + "']/column[@id='" + id + "']");
                            old_node.attributeValue("name", o.getString("Name"));
                            Element column_datatype = old_node.element("datatype");
                            if(column_datatype!=null){
                                column_datatype.setText(o.getString("DataType"));
                            }else {
                                Element column_add_datatype = old_node.addElement("datatype");
                                column_add_datatype.setText(o.getString("DataType"));
                            }
                            Element column_comment = old_node.element("comment");
                            if(column_comment!=null){
                                column_comment.setText(o.getString("Comment"));
                            }else {
                                Element column_add_comment = old_node.addElement("comment");
                                column_add_comment.setText(o.getString("Comment"));
                            }
                            Dom4jUtil.writeDocumentToFile(doc, StringContext.tables_xml);
                            msg = "数据表信息修改成功";
                            json = "{success:true,msg:'" + msg + "'}";
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 1);
                        }else {
                            List<Element> selectNodes = doc.selectNodes("/tables/table[@name='" + table_name + "']/column");
                            if(selectNodes!=null){
                                Element column = selectSingleNode.addElement("column");
                                column.addAttribute("id", String.valueOf(selectNodes.size()));
                                column.addAttribute("name", o.getString("Name"));
                                Element column_add_datatype = column.addElement("datatype");
                                column_add_datatype.setText(o.getString("DataType"));
                                Element column_add_comment = column.addElement("comment");
                                column_add_comment.setText(o.getString("Comment"));
                                Dom4jUtil.writeDocumentToFile(doc, StringContext.tables_xml);
                                msg = "数据表信息修改成功";
                                json = "{success:true,msg:'" + msg + "'}";
                                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 1);
                            }
                        }
                    }
                }
            } else {
                msg = "更新失败,未找到指定表";
                json = "{success:false,msg:'" + msg + "'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
            }
        } else {
            msg = "更新失败,未找到指定表";
            json = "{success:false,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "表管理", msg, 0);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
