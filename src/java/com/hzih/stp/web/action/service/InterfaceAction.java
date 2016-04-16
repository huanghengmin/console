package com.hzih.stp.web.action.service;

import com.hzih.stp.dao.RegionDao;
import com.hzih.stp.domain.Account;
import com.hzih.stp.domain.InterfaceObj;
import com.hzih.stp.domain.Region;
import com.hzih.stp.haian.ArrayOfArrayOfString;
import com.hzih.stp.haian.ArrayOfString;
import com.hzih.stp.service.InterfaceService;
import com.hzih.stp.service.LogService;
import com.hzih.stp.utils.Dom4jUtil;
import com.hzih.stp.utils.FileUtil;
import com.hzih.stp.utils.StringContext;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.Element;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 15-9-5
 * Time: 上午10:08
 */
public class InterfaceAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(InterfaceAction.class);

    private LogService logService;
    private InterfaceService interfaceService;
    private RegionDao regionDao;
    private String name;
    //    private String status;
    private int start;
    private int limit;
    private long interfaceId;
    private long[] accountIds;
    private String[] userNames;
    private String userName;
    private InterfaceObj interfaceObj;

    public RegionDao getRegionDao() {
        return regionDao;
    }

    public void setRegionDao(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    public String select() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        try {
            json = interfaceService.selectInterface(name,/* status,*/ start, limit, SessionUtils.getAccount(request));
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "接口服务", "获取接口服务信息成功", 1);
        } catch (Exception e) {
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "接口服务", "获取接口服务信息失败" + e.getMessage(), 1);
            logger.error("接口服务", e);
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

    public InterfaceService getInterfaceService() {
        return interfaceService;
    }

    public String findColumnStore()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("table_name");
        String interfaceNumber = request.getParameter("interfaceNumber");
        StringBuilder sb = new StringBuilder();
        if(interfaceNumber!=null) {
            InterfaceObj interfaceObj = interfaceService.findByServiceId(interfaceNumber);
            if(interfaceObj!=null) {
                String conditionsField = interfaceObj.getConditionsField();
                Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
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
//                            sb.append("tj_checked:'").append("0").append("'").append(",");
                            if(conditionsField.contains(column_name)) {
                                sb.append("checked:'").append(true).append("'").append(",");
                            }else {
                                sb.append("checked:'").append(false).append("'").append(",");
                            }
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
        }
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

   /* public String findQyStore() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        String serviceId = request.getParameter("serviceId");
        InterfaceObj service = interfaceService.findByServiceId(serviceId);
        StringBuilder sb = new StringBuilder();
        if (service != null) {
//            String qyFields = service.getQueryField();
           *//* if (qyFields != null && qyFields.contains(",")) {
                String[] qys = qyFields.split(",");
                String tableName = service.getTableNameEn();
                if (doc != null) {
                    sb.append("{success:true,'totalCount':").append(qys.length).append(",'rows':[");
                    for (int i = 0; i < qys.length; i++) {
                        String field = qys[i];
                        if (field != null) {
                            Element columnElement = (Element) doc.selectSingleNode("/tables/table[@name='" + tableName + "']/column[@name='" + field + "']");
                            String name = columnElement.attributeValue("name");
                            Element comment = columnElement.element("comment");
                            sb.append("{");
                            sb.append("value:'").append(name).append("'").append(",");
                            sb.append("key:'").append(comment.getText()).append("'");
                            sb.append("}");
                            if (i != (qys.length - 1)) {
                                sb.append(",");
                            }
                        }
                    }
                    sb.append("]}");
                } else {
                    sb.append("{success:true,'totalCount':").append(0).append(",'rows':[");
                    sb.append("]}");
                }
            } else {*//*
                sb.append("{success:true,'totalCount':").append(0).append(",'rows':[");
                sb.append("]}");
//            }
        }
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }*/

   /* public String swithValue(String name) {
        String display = null;
        switch (name) {
            case "RYSBH_TJ": display = "人员识别号"; break;
            case "SJBH_TJ": display = "事件编号"; break;
            case "XM_TJ": display = "姓名"; break;
            case "XB_TJ": display = "性别"; break;
            case "MZ_TJ": display = "民族"; break;
            case "CSRQ_TJ": display = "死亡日期"; break;
            case "SWYY_TJ": display = "死亡原因"; break;
            case "SWSJ_TJ": display = "死亡时间"; break;
            case "SFHM_TJ": display = "身份证号"; break;
            case "HH_TJ": display = "户号"; break;
            case "HKLX_TJ": display = "户口类型"; break;
            case "YHZGX_TJ": display = "与户主关系"; break;
            case "JGGJ_TJ": display = "籍贯国籍"; break;
            case "JGSSX_TJ": display = "籍贯省市县"; break;
            case "ZPXH_TJ": display = "照片序号"; break;
            case "ZZPBS_TJ": display = "证件照标识"; break;
            case "URL_TJ": display = "照片地址"; break;
            case "SSXQ_TJ": display = "所属县区"; break;
            case "PCSBM_TJ": display = "派出所编码"; break;
            case "GDSJ_TJ": display = "古代时间"; break;
            case "GLZT_TJ": display = "管理状态"; break;
        }
        return display;
    }*/

   /* public boolean showValue(String name) {
        if(name.equals("GMSFHM")){
            name = "SFHM_XS";
        }else {
           name+="_XS";
        }
        Document doc = Dom4jUtil.getDocument(StringContext.filed_xml);
        if(doc!=null) {
            Element element = (Element)doc.selectSingleNode("/config/XSZD/cloumn[@name='" + name + "']");
            if(element!=null&&element.getText().equals("1")){
                return true;
            }
        }
        return false;
    }*/

   /* public String findQyStoreAll() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        StringBuilder sb = new StringBuilder();
        Document doc = Dom4jUtil.getDocument(StringContext.filed_xml);
        if(doc!=null){
            List<Element> tj_cloumns = doc.selectNodes("/config/TJZD/cloumn[text()=\"1\"]");
            if(tj_cloumns!=null&&tj_cloumns.size()>0){
                sb.append("{success:true,'totalCount':").append(tj_cloumns.size()).append(",'rows':[");
                for (int i=0;i<tj_cloumns.size();i++){
                    Element cloumn = tj_cloumns.get(i);
                    String value = cloumn.attributeValue("name");
                    sb.append("{");
                    sb.append("value:'").append(value).append("'").append(",");
                    sb.append("key:'").append(swithValue(value)).append("'");
                    sb.append("}");
                    if (i != (tj_cloumns.size() - 1)) {
                        sb.append(",");
                    }
                }
                sb.append("]}");
            }
        }else {
            sb.append("{success:true,'totalCount':").append(0).append(",'rows':[");
            sb.append("]}");
        }
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String saveField() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String RYSBH_XS = (request.getParameter("RYSBH_XS") != null) ? "1" : "0";
        String SJBH_XS = (request.getParameter("SJBH_XS") != null) ? "1" : "0";
        String XM_XS = (request.getParameter("XM_XS") != null) ? "1" : "0";
        String XB_XS = (request.getParameter("XB_XS") != null) ? "1" : "0";
        String MZ_XS = (request.getParameter("MZ_XS") != null) ? "1" : "0";
        String CSRQ_XS = (request.getParameter("CSRQ_XS") != null) ? "1" : "0";
        String SWYY_XS = (request.getParameter("SWYY_XS") != null) ? "1" : "0";
        String SWSJ_XS = (request.getParameter("SWSJ_XS") != null) ? "1" : "0";
        String SFHM_XS = (request.getParameter("SFHM_XS") != null) ? "1" : "0";
        String HH_XS = (request.getParameter("HH_XS") != null) ? "1" : "0";
        String HKLX_XS = (request.getParameter("HKLX_XS") != null) ? "1" : "0";
        String YHZGX_XS = (request.getParameter("YHZGX_XS") != null) ? "1" : "0";
        String JGGJ_XS = (request.getParameter("JGGJ_XS") != null) ? "1" : "0";
        String JGSSX_XS = (request.getParameter("JGSSX_XS") != null) ? "1" : "0";
        String ZPXH_XS = (request.getParameter("ZPXH_XS") != null) ? "1" : "0";
        String ZZPBS_XS = (request.getParameter("ZZPBS_XS") != null) ? "1" : "0";
        String URL_XS = (request.getParameter("URL_XS") != null) ? "1" : "0";
        String SSXQ_XS = (request.getParameter("SSXQ_XS") != null) ? "1" : "0";
        String PCSBM_XS = (request.getParameter("PCSBM_XS") != null) ? "1" : "0";
        String GDSJ_XS = (request.getParameter("GDSJ_XS") != null) ? "1" : "0";
        String GLZT_XS = (request.getParameter("GLZT_XS") != null) ? "1" : "0";


        String RYSBH_TJ = (request.getParameter("RYSBH_TJ") != null) ? "1" : "0";
        String SJBH_TJ = (request.getParameter("SJBH_TJ") != null) ? "1" : "0";
        String XM_TJ = (request.getParameter("XM_TJ") != null) ? "1" : "0";
        String XB_TJ = (request.getParameter("XB_TJ") != null) ? "1" : "0";
        String MZ_TJ = (request.getParameter("MZ_TJ") != null) ? "1" : "0";
        String CSRQ_TJ = (request.getParameter("CSRQ_TJ") != null) ? "1" : "0";
        String SWYY_TJ = (request.getParameter("SWYY_TJ") != null) ? "1" : "0";
        String SWSJ_TJ = (request.getParameter("SWSJ_TJ") != null) ? "1" : "0";
        String SFHM_TJ = (request.getParameter("SFHM_TJ") != null) ? "1" : "0";
        String HH_TJ = (request.getParameter("HH_TJ") != null) ? "1" : "0";
        String HKLX_TJ = (request.getParameter("HKLX_TJ") != null) ? "1" : "0";
        String YHZGX_TJ = (request.getParameter("YHZGX_TJ") != null) ? "1" : "0";
        String JGGJ_TJ = (request.getParameter("JGGJ_TJ") != null) ? "1" : "0";
        String JGSSX_TJ = (request.getParameter("JGSSX_TJ") != null) ? "1" : "0";
        String ZPXH_TJ = (request.getParameter("ZPXH_TJ") != null) ? "1" : "0";
        String ZZPBS_TJ = (request.getParameter("ZZPBS_TJ") != null) ? "1" : "0";
        String URL_TJ = (request.getParameter("URL_TJ") != null) ? "1" : "0";
        String SSXQ_TJ = (request.getParameter("SSXQ_TJ") != null) ? "1" : "0";
        String PCSBM_TJ = (request.getParameter("PCSBM_TJ") != null) ? "1" : "0";
        String GDSJ_TJ = (request.getParameter("GDSJ_TJ") != null) ? "1" : "0";
        String GLZT_TJ = (request.getParameter("GLZT_TJ") != null) ? "1" : "0";

        Document doc = Dom4jUtil.getDocument(StringContext.filed_xml);
        String msg = null;
        String json = null;
        if (doc != null) {
            List<Element> xs_selectNodes = doc.selectNodes("/config/XSZD/cloumn");
            if (xs_selectNodes != null) {
                for (Element node : xs_selectNodes) {
                    if (node.attributeValue("name").equals("RYSBH_XS")) {
                        node.setText(RYSBH_XS);
                    } else if (node.attributeValue("name").equals("SJBH_XS")) {
                        node.setText(SJBH_XS);
                    } else if (node.attributeValue("name").equals("XM_XS")) {
                        node.setText(XM_XS);
                    } else if (node.attributeValue("name").equals("XB_XS")) {
                        node.setText(XB_XS);
                    } else if (node.attributeValue("name").equals("MZ_XS")) {
                        node.setText(MZ_XS);
                    } else if (node.attributeValue("name").equals("CSRQ_XS")) {
                        node.setText(CSRQ_XS);
                    } else if (node.attributeValue("name").equals("SWYY_XS")) {
                        node.setText(XB_XS);
                    } else if (node.attributeValue("name").equals("SWSJ_XS")) {
                        node.setText(SWSJ_XS);
                    } else if (node.attributeValue("name").equals("SFHM_XS")) {
                        node.setText(SFHM_XS);
                    } else if (node.attributeValue("name").equals("HH_XS")) {
                        node.setText(HH_XS);
                    } else if (node.attributeValue("name").equals("HKLX_XS")) {
                        node.setText(HKLX_XS);
                    } else if (node.attributeValue("name").equals("YHZGX_XS")) {
                        node.setText(YHZGX_XS);
                    } else if (node.attributeValue("name").equals("JGGJ_XS")) {
                        node.setText(JGGJ_XS);
                    } else if (node.attributeValue("name").equals("JGSSX_XS")) {
                        node.setText(JGSSX_XS);
                    } else if (node.attributeValue("name").equals("ZPXH_XS")) {
                        node.setText(ZPXH_XS);
                    } else if (node.attributeValue("name").equals("ZZPBS_XS")) {
                        node.setText(ZZPBS_XS);
                    } else if (node.attributeValue("name").equals("URL_XS")) {
                        node.setText(URL_XS);
                    } else if (node.attributeValue("name").equals("SSXQ_XS")) {
                        node.setText(SSXQ_XS);
                    } else if (node.attributeValue("name").equals("PCSBM_XS")) {
                        node.setText(PCSBM_XS);
                    } else if (node.attributeValue("name").equals("GDSJ_XS")) {
                        node.setText(GDSJ_XS);
                    } else if (node.attributeValue("name").equals("GLZT_XS")) {
                        node.setText(GLZT_XS);
                    }
                }
            }
            List<Element> tj_selectNodes = doc.selectNodes("/config/TJZD/cloumn");
            if (tj_selectNodes != null) {
                for (Element node : tj_selectNodes) {
                    if (node.attributeValue("name").equals("RYSBH_TJ")) {
                        node.setText(RYSBH_TJ);
                    } else if (node.attributeValue("name").equals("SJBH_TJ")) {
                        node.setText(SJBH_TJ);
                    } else if (node.attributeValue("name").equals("XM_TJ")) {
                        node.setText(XM_TJ);
                    } else if (node.attributeValue("name").equals("XB_TJ")) {
                        node.setText(XB_TJ);
                    } else if (node.attributeValue("name").equals("MZ_TJ")) {
                        node.setText(MZ_TJ);
                    } else if (node.attributeValue("name").equals("CSRQ_TJ")) {
                        node.setText(CSRQ_TJ);
                    } else if (node.attributeValue("name").equals("SWYY_TJ")) {
                        node.setText(XB_TJ);
                    } else if (node.attributeValue("name").equals("SWSJ_TJ")) {
                        node.setText(SWSJ_TJ);
                    } else if (node.attributeValue("name").equals("SFHM_TJ")) {
                        node.setText(SFHM_TJ);
                    } else if (node.attributeValue("name").equals("HH_TJ")) {
                        node.setText(HH_TJ);
                    } else if (node.attributeValue("name").equals("HKLX_TJ")) {
                        node.setText(HKLX_TJ);
                    } else if (node.attributeValue("name").equals("YHZGX_TJ")) {
                        node.setText(YHZGX_TJ);
                    } else if (node.attributeValue("name").equals("JGGJ_TJ")) {
                        node.setText(JGGJ_TJ);
                    } else if (node.attributeValue("name").equals("JGSSX_TJ")) {
                        node.setText(JGSSX_TJ);
                    } else if (node.attributeValue("name").equals("ZPXH_TJ")) {
                        node.setText(ZPXH_TJ);
                    } else if (node.attributeValue("name").equals("ZZPBS_TJ")) {
                        node.setText(ZZPBS_TJ);
                    } else if (node.attributeValue("name").equals("URL_TJ")) {
                        node.setText(URL_TJ);
                    } else if (node.attributeValue("name").equals("SSXQ_TJ")) {
                        node.setText(SSXQ_TJ);
                    } else if (node.attributeValue("name").equals("PCSBM_TJ")) {
                        node.setText(PCSBM_TJ);
                    } else if (node.attributeValue("name").equals("GDSJ_TJ")) {
                        node.setText(GDSJ_TJ);
                    } else if (node.attributeValue("name").equals("GLZT_TJ")) {
                        node.setText(GLZT_TJ);
                    }
                }

                Dom4jUtil.writeDocumentToFile(doc, StringContext.filed_xml);
                msg = "保存字段配置成功";
                json = "{success:true,msg:'" + msg + "'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "字段配置", msg,1);
            }
        } else{
        Document document = DocumentHelper.createDocument();
        Element config = document.addElement("config");
        Element XSZD_el = config.addElement("XSZD");
            Element RYSBH_XS_EL = XSZD_el.addElement("cloumn");
            RYSBH_XS_EL.addAttribute("name","RYSBH_XS");
            RYSBH_XS_EL.setText(RYSBH_XS);

            Element SJBH_XS_EL = XSZD_el.addElement("cloumn");
            SJBH_XS_EL.addAttribute("name","SJBH_XS");
            SJBH_XS_EL.setText(SJBH_XS);

            Element XM_XS_EL = XSZD_el.addElement("cloumn");
            XM_XS_EL.addAttribute("name","XM_XS");
            XM_XS_EL.setText(XM_XS);

            Element XB_XS_EL = XSZD_el.addElement("cloumn");
            XB_XS_EL.addAttribute("name","XB_XS");
            XB_XS_EL.setText(XB_XS);

            Element MZ_XS_EL = XSZD_el.addElement("cloumn");
            MZ_XS_EL.addAttribute("name","MZ_XS");
            MZ_XS_EL.setText(MZ_XS);

            Element CSRQ_XS_EL = XSZD_el.addElement("cloumn");
            CSRQ_XS_EL.addAttribute("name","CSRQ_XS");
            CSRQ_XS_EL.setText(CSRQ_XS);

            Element SWYY_XS_EL = XSZD_el.addElement("cloumn");
            SWYY_XS_EL.addAttribute("name","SWYY_XS");
            SWYY_XS_EL.setText(SWYY_XS);

            Element SWSJ_XS_EL = XSZD_el.addElement("cloumn");
            SWSJ_XS_EL.addAttribute("name","SWSJ_XS");
            SWSJ_XS_EL.setText(SWSJ_XS);

            Element SFHM_XS_EL = XSZD_el.addElement("cloumn");
            SFHM_XS_EL.addAttribute("name","SFHM_XS");
            SFHM_XS_EL.setText(SFHM_XS);

            Element HH_XS_EL = XSZD_el.addElement("cloumn");
            HH_XS_EL.addAttribute("name","HH_XS");
            HH_XS_EL.setText(HH_XS);

            Element HKLX_XS_EL = XSZD_el.addElement("cloumn");
            HKLX_XS_EL.addAttribute("name","HKLX_XS");
            HKLX_XS_EL.setText(HKLX_XS);

            Element YHZGX_XS_EL = XSZD_el.addElement("cloumn");
            YHZGX_XS_EL.addAttribute("name","YHZGX_XS");
            YHZGX_XS_EL.setText(YHZGX_XS);

            Element JGGJ_XS_EL = XSZD_el.addElement("cloumn");
            JGGJ_XS_EL.addAttribute("name","JGGJ_XS");
            JGGJ_XS_EL.setText(JGGJ_XS);

            Element JGSSX_XS_EL = XSZD_el.addElement("cloumn");
            JGSSX_XS_EL.addAttribute("name","JGSSX_XS");
            JGSSX_XS_EL.setText(JGSSX_XS);

            Element ZPXH_XS_EL = XSZD_el.addElement("cloumn");
            ZPXH_XS_EL.addAttribute("name","ZPXH_XS");
            ZPXH_XS_EL.setText(ZPXH_XS);

            Element ZZPBS_XS_EL = XSZD_el.addElement("cloumn");
            ZZPBS_XS_EL.addAttribute("name","ZZPBS_XS");
            ZZPBS_XS_EL.setText(ZZPBS_XS);

            Element URL_XS_EL = XSZD_el.addElement("cloumn");
            URL_XS_EL.addAttribute("name","URL_XS");
            URL_XS_EL.setText(URL_XS);

            Element SSXQ_XS_EL = XSZD_el.addElement("cloumn");
            SSXQ_XS_EL.addAttribute("name","SSXQ_XS");
            SSXQ_XS_EL.setText(SSXQ_XS);

            Element PCSBM_XS_EL = XSZD_el.addElement("cloumn");
            PCSBM_XS_EL.addAttribute("name","PCSBM_XS");
            PCSBM_XS_EL.setText(PCSBM_XS);

            Element GDSJ_XS_EL = XSZD_el.addElement("cloumn");
            GDSJ_XS_EL.addAttribute("name","GDSJ_XS");
            GDSJ_XS_EL.setText(GDSJ_XS);

            Element GLZT_XS_EL = XSZD_el.addElement("cloumn");
            GLZT_XS_EL.addAttribute("name","GLZT_XS");
            GLZT_XS_EL.setText(GLZT_XS);

            Element TJZD_el = config.addElement("TJZD");
            Element RYSBH_TJ_EL = TJZD_el.addElement("cloumn");
            RYSBH_TJ_EL.addAttribute("name","RYSBH_TJ");
            RYSBH_TJ_EL.setText(RYSBH_TJ);

            Element SJBH_TJ_EL = TJZD_el.addElement("cloumn");
            SJBH_TJ_EL.addAttribute("name","SJBH_TJ");
            SJBH_TJ_EL.setText(SJBH_TJ);

            Element XM_TJ_EL = TJZD_el.addElement("cloumn");
            XM_TJ_EL.addAttribute("name","XM_TJ");
            XM_TJ_EL.setText(XM_TJ);

            Element XB_TJ_EL = TJZD_el.addElement("cloumn");
            XB_TJ_EL.addAttribute("name","XB_TJ");
            XB_TJ_EL.setText(XB_TJ);

            Element MZ_TJ_EL = TJZD_el.addElement("cloumn");
            MZ_TJ_EL.addAttribute("name","MZ_TJ");
            MZ_TJ_EL.setText(MZ_TJ);

            Element CSRQ_TJ_EL = TJZD_el.addElement("cloumn");
            CSRQ_TJ_EL.addAttribute("name","CSRQ_TJ");
            CSRQ_TJ_EL.setText(CSRQ_TJ);

            Element SWYY_TJ_EL = TJZD_el.addElement("cloumn");
            SWYY_TJ_EL.addAttribute("name","SWYY_TJ");
            SWYY_TJ_EL.setText(SWYY_TJ);

            Element SWSJ_TJ_EL = TJZD_el.addElement("cloumn");
            SWSJ_TJ_EL.addAttribute("name","SWSJ_TJ");
            SWSJ_TJ_EL.setText(SWSJ_TJ);

            Element SFHM_TJ_EL = TJZD_el.addElement("cloumn");
            SFHM_TJ_EL.addAttribute("name","SFHM_TJ");
            SFHM_TJ_EL.setText(SFHM_TJ);

            Element HH_TJ_EL = TJZD_el.addElement("cloumn");
            HH_TJ_EL.addAttribute("name","HH_TJ");
            HH_TJ_EL.setText(HH_TJ);

            Element HKLX_TJ_EL = TJZD_el.addElement("cloumn");
            HKLX_TJ_EL.addAttribute("name","HKLX_TJ");
            HKLX_TJ_EL.setText(HKLX_TJ);

            Element YHZGX_TJ_EL = TJZD_el.addElement("cloumn");
            YHZGX_TJ_EL.addAttribute("name","YHZGX_TJ");
            YHZGX_TJ_EL.setText(YHZGX_TJ);

            Element JGGJ_TJ_EL = TJZD_el.addElement("cloumn");
            JGGJ_TJ_EL.addAttribute("name","JGGJ_TJ");
            JGGJ_TJ_EL.setText(JGGJ_TJ);

            Element JGSSX_TJ_EL = TJZD_el.addElement("cloumn");
            JGSSX_TJ_EL.addAttribute("name","JGSSX_TJ");
            JGSSX_TJ_EL.setText(JGSSX_TJ);

            Element ZPXH_TJ_EL = TJZD_el.addElement("cloumn");
            ZPXH_TJ_EL.addAttribute("name","ZPXH_TJ");
            ZPXH_TJ_EL.setText(ZPXH_TJ);

            Element ZZPBS_TJ_EL = TJZD_el.addElement("cloumn");
            ZZPBS_TJ_EL.addAttribute("name","ZZPBS_TJ");
            ZZPBS_TJ_EL.setText(ZZPBS_TJ);

            Element URL_TJ_EL = TJZD_el.addElement("cloumn");
            URL_TJ_EL.addAttribute("name","URL_TJ");
            URL_TJ_EL.setText(URL_TJ);

            Element SSXQ_TJ_EL = TJZD_el.addElement("cloumn");
            SSXQ_TJ_EL.addAttribute("name","SSXQ_TJ");
            SSXQ_TJ_EL.setText(SSXQ_TJ);

            Element PCSBM_TJ_EL = TJZD_el.addElement("cloumn");
            PCSBM_TJ_EL.addAttribute("name","PCSBM_TJ");
            PCSBM_TJ_EL.setText(PCSBM_TJ);

            Element GDSJ_TJ_EL = TJZD_el.addElement("cloumn");
            GDSJ_TJ_EL.addAttribute("name","GDSJ_TJ");
            GDSJ_TJ_EL.setText(GDSJ_TJ);

            Element GLZT_TJ_EL = TJZD_el.addElement("cloumn");
            GLZT_TJ_EL.addAttribute("name","GLZT_TJ");
            GLZT_TJ_EL.setText(GLZT_TJ);

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        format.setIndent(true);
        try {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.filed_xml)), format);
            try {
                xmlWriter.write(document);
                msg = "保存字段配置成功";
                json = "{success:true,msg:'" + msg + "'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "字段配置", msg,1);
            } catch (IOException e) {
                msg = "保存字段配置失败,出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                    logger.info(e.getMessage(), e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "字段配置", msg,0);
            } finally {
                try {
                    xmlWriter.flush();
                    xmlWriter.close();
                } catch (IOException e) {
                    msg = "保存字段配置失败,出现异常";
                    json = "{success:false,msg:'" + msg + "'}";
                        logger.info(e.getMessage(), e);
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "字段配置", msg,0);
                    }
                }
        } catch (UnsupportedEncodingException e) {
            msg = "保存字段配置失败,出现异常";
            json = "{success:false,msg:'" + msg + "'}";
                logger.info(e.getMessage(), e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "字段配置", msg,0);
        } catch (FileNotFoundException e) {
            msg = "保存字段配置失败,出现异常";
            json = "{success:false,msg:'" + msg + "'}";
                logger.info(e.getMessage(), e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "字段配置", msg,0);
        }
    }
    actionBase.actionEnd(response,json,result);
    return null;
}

    public String findField() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Document doc = Dom4jUtil.getDocument(StringContext.filed_xml);
        StringBuilder sb = new StringBuilder();
        if (doc != null) {
            sb.append("[");
            sb.append("{");
            List<Element> xs_cloumns = doc.selectNodes("/config/XSZD/cloumn");
            List<Element> tj_cloumns = doc.selectNodes("/config/TJZD/cloumn");
            if (xs_cloumns != null) {
                for (int i = 0; i < xs_cloumns.size(); i++) {
                    Element element = xs_cloumns.get(i);
                    String name = element.attributeValue("name");
                    String text = element.getText();
                    sb.append(name+":'").append(text).append("'");
                    if (i != (xs_cloumns.size() - 1)) {
                        sb.append(",");
                    }
                }
            }

            if (tj_cloumns != null) {
                if(xs_cloumns.size()>0&&tj_cloumns.size()>0){
                    sb.append(",");
                }
                for (int i = 0; i < tj_cloumns.size(); i++) {
                    Element element = tj_cloumns.get(i);
                    String name = element.attributeValue("name");
                    String text = element.getText();
                    sb.append(name+":'").append(text).append("'");
                    if (i != (tj_cloumns.size() - 1)) {
                        sb.append(",");
                    }
                }
            }
            sb.append("}");
            sb.append("]");
        }
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }*/

    public String findGrid() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
//        String policeId = request.getParameter("policeId");
        String serviceId = request.getParameter("serviceId");
        String gmsfhm = request.getParameter("gmsfhm");
//        String queryStr = request.getParameter("queryStr");
//        int start = Integer.parseInt(request.getParameter("start"));
//        int limit = Integer.parseInt(request.getParameter("end"));
        String YHM = "064740";
        String MM = "111111";
        String url = "http://10.36.11.93:10017/ntgajk/requestData.do";
        String FWID_person = "ntga2015100029";
        String FWID_photo = "ntga2015100030";
        InterfaceObj service = interfaceService.findByServiceId(serviceId);
        String tableName = service.getTableNameEn();
        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        StringBuilder datas = new StringBuilder();
        String result_msg = null;
        boolean result_success = false;
        //展示字段
        String zxString = service.getConditionsField();
        String[] zx = null;
        if (zxString.contains(",")) {
            zx = zxString.split(",");
        }
        //数据内容
        datas.append("'data':").append("[");
        if (doc != null) {
            Element table = (Element) doc.selectSingleNode("/tables/table[@name='" + tableName + "']");
            if (table != null) {
                String type = table.attributeValue("type");
                if (type.equals("0")) {
                    Account account = SessionUtils.getAccount(request);
                    String json = InterfaceUtils.getMsgForGmsfhm(FWID_person, gmsfhm, YHM, MM, url);
                    if (json != null) {
                        logService.newLog("INFO", account.getUserName(), "接口服务", "数据请求成功," + FWID_person + "," + gmsfhm + "," + account.getUserName(), 1);
                        JSONObject jsonObject = JSONObject.fromObject(json);
                        int code = -1;
                        try {
                            code = jsonObject.getInt("CODE");;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (1 == code) {
                            Object rs = null;
                            try {
                                rs = jsonObject.get("RESULT");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(rs!=null) {
                                JSONArray array = JSONArray.fromObject(rs.toString());
                                if (array!=null&&array.size() > 0) {
                                    JSONObject object = array.getJSONObject(0);
                                    if (object != null){
                                        if (zx != null && zx.length > 0) {
                                            datas.append("{");
                                            result_msg = "获取数据成功";
                                            result_success = true;
                                            for (int i = 0; i < zx.length; i++) {
                                                Element selectSingleNode = (Element) doc.selectSingleNode("/tables/table[@name='" + tableName + "']/column[@name='" + zx[i] + "']");
                                                if (selectSingleNode != null) {
                                                    String name = selectSingleNode.attributeValue("name");
                                                    if (name!=null&&name.length()>0){
                                                        if (name.equals("URL") || name.equals("ZP")) {
                                                            String rpath = request.getRealPath("/");
                                                            File f = new File(rpath + "/gmpt/" + gmsfhm + ".jpg");
                                                            if (!f.exists()) {
                                                                File dir = f.getParentFile();
                                                                if (!dir.exists())
                                                                    dir.mkdirs();
                                                                //获取照片信息接口
                                                                String json_photo = InterfaceUtils.getMsgForGmsfhm(FWID_photo, gmsfhm, YHM, MM, url);
                                                                logService.newLog("INFO", account.getUserName(), "接口服务", "数据请求成功," + FWID_photo + "," + gmsfhm + "," + account.getUserName(), 1);
                                                                JSONObject json_Object = JSONObject.fromObject(json_photo);
                                                                int code_photo = json_Object.getInt("CODE");
                                                                if (code_photo == 1) {
                                                                    String rs_photo = json_Object.get("RESULT").toString();
                                                                    JSONArray array_photo = JSONArray.fromObject(rs_photo);
                                                                    JSONObject object_photo = array_photo.getJSONObject(0);
                                                                    byte[] bytes = null;
                                                                    try {
                                                                        Object ss = object_photo.get(name);
                                                                        sun.misc.BASE64Decoder base64Decoder = new BASE64Decoder();
                                                                        bytes = base64Decoder.decodeBuffer(ss.toString());
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                    FileOutputStream outputStream = new FileOutputStream(rpath + "/gmpt/" + gmsfhm + ".jpg");
                                                                    outputStream.write(bytes);
                                                                    outputStream.flush();
                                                                    outputStream.close();
                                                                    datas.append("'" + name + "': '" + "../../gmpt/" + gmsfhm + ".jpg" + "'");
                                                                }
                                                            } else {
                                                                datas.append("'" + name + "': '" + "../../gmpt/" + gmsfhm + ".jpg" + "'");
                                                            }
                                                            if (i != (zx.length - 1)) {
                                                                datas.append(",");
                                                            }
                                                        } else if (name.equals("CSRQ") || name.equals("RESERVE93")) {
                                                            Object ss = null;
                                                            try {
                                                                ss = object.get(name);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            if (ss != null) {
                                                                String value = ss.toString();
                                                                if (value != null && value.length() > 0) {
                                                                    if (value.contains(" ")) {
                                                                        datas.append("'" + name + "': '" + value.split(" ")[0] + "'");
                                                                    } else {
                                                                        datas.append("'" + name + "': '" + value + "'");
                                                                    }
                                                                } else {
                                                                    datas.append("'" + name + "': ''");
                                                                }
                                                            } else {
                                                                datas.append("'" + name + "': ''");
                                                            }
                                                            if (i != (zx.length - 1)) {
                                                                datas.append(",");
                                                            }
                                                        }else if(name.equals("SSXQ")){
                                                            Object ss = "";
                                                            try {
                                                                ss = object.get(name);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            if(ss!=null){
                                                                String ssxq = ss.toString();
                                                                if(ssxq!=null&&ssxq.length()>0){
                                                                    Region region = regionDao.findById(Long.parseLong(ssxq));
                                                                    if(region!=null){
                                                                        datas.append("'" + name + "': '" + region.getRegionName() + "'");
                                                                    }else {
                                                                        datas.append("'" + name + "': '"+ssxq+"'");
                                                                    }
                                                                }else {
                                                                    datas.append("'" + name + "': ''");
                                                                }
                                                            }else {
                                                                datas.append("'" + name + "': ''");
                                                            }
                                                            if (i != (zx.length - 1)) {
                                                                datas.append(",");
                                                            }
                                                        } else {
                                                            Object ss = "";
                                                            try {
                                                                ss = object.get(name);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            datas.append("'" + name + "': '" + ss + "'");
                                                            if (i != (zx.length - 1)) {
                                                                datas.append(",");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            datas.append("}");
                                        }else {
                                            result_success = false;
                                            result_msg = "未定义展示字段";
                                        }
                                    }else {
                                        result_success = false;
                                        result_msg = "查无此人";
                                    }
                                }else {
                                    result_success = false;
                                    result_msg = "查无此人";
                                }
                            }else {
                                result_success = false;
                                result_msg = "查无此人";
                            }
                        } else {
                            result_success = false;
                            result_msg = "服务器返回:"+InterfaceUtils.getCode(code);
                        }
                    }else {
                        result_success = false;
                        result_msg = "网络故障,请重新查询";
                    }
                } else {
                    Account account = SessionUtils.getAccount(request);
                    String json = InterfaceUtils.getMsgForGmsfhm(serviceId, gmsfhm, YHM, MM, url);
                    if(json!=null){
                        logService.newLog("INFO", account.getUserName(), "接口服务", "数据请求成功," + serviceId + "," + gmsfhm + "," + account.getUserName(), 1);
                        JSONObject jsonObject = JSONObject.fromObject(json);
                        int code = -1;
                        try {
                            code = jsonObject.getInt("CODE");;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (1 == code) {
                            Object rs = null;
                            try {
                                rs = jsonObject.get("RESULT");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (rs != null) {
                                JSONArray array = JSONArray.fromObject(rs);
                                if (array != null && array.size() > 0) {
                                    JSONObject object = array.getJSONObject(0);
                                    if (object != null){
                                        if (zx != null && zx.length > 0) {
                                            datas.append("{");
                                            result_msg = "获取数据成功";
                                            result_success = true;
                                            for (int i = 0; i < zx.length; i++) {
                                                Element selectSingleNode = (Element) doc.selectSingleNode("/tables/table[@name='" + tableName + "']/column[@name='" + zx[i] + "']");
                                                if (selectSingleNode != null) {
                                                    String name = selectSingleNode.attributeValue("name");
                                                    if (name.equals("URL") || name.equals("ZP")) {
                                                        String rpath = request.getRealPath("/");
                                                        File f = new File(rpath + "/gmpt/" + gmsfhm + ".jpg");
                                                        if (!f.exists()) {
                                                            File dir = f.getParentFile();
                                                            if (!dir.exists())
                                                                dir.mkdirs();
                                                            byte[] bytes = null;
                                                            try {
                                                                Object ss = object.get(name);
                                                                sun.misc.BASE64Decoder base64Decoder = new BASE64Decoder();
                                                                bytes = base64Decoder.decodeBuffer(ss.toString());
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            FileOutputStream outputStream = new FileOutputStream(rpath + "/gmpt/" + gmsfhm + ".jpg");
                                                            outputStream.write(bytes);
                                                            outputStream.flush();
                                                            outputStream.close();
                                                            datas.append("'" + name + "': '" + "../../gmpt/" + gmsfhm + ".jpg" + "'");
                                                        } else {
                                                            datas.append("'" + name + "': '" + "../../gmpt/" + gmsfhm + ".jpg" + "'");
                                                        }
                                                        if (i != (zx.length - 1)) {
                                                            datas.append(",");
                                                        }
                                                    } else if (name.equals("CSRQ") || name.equals("RESERVE93")) {
                                                        Object ss = null;
                                                        try {
                                                            ss = object.get(name);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (ss != null) {
                                                            String value = ss.toString();
                                                            if (value != null && value.length() > 0) {
                                                                if (value.contains(" ")) {
                                                                    datas.append("'" + name + "': '" + value.split(" ")[0] + "'");
                                                                } else {
                                                                    datas.append("'" + name + "': '" + value + "'");
                                                                }
                                                            } else {
                                                                datas.append("'" + name + "': ''");
                                                            }
                                                        } else {
                                                            datas.append("'" + name + "': ''");
                                                        }

                                                        if (i != (zx.length - 1)) {
                                                            datas.append(",");
                                                        }
                                                    }else if(name.equals("SSXQ")){
                                                        Object ss = "";
                                                        try {
                                                            ss = object.get(name);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        if(ss!=null){
                                                            String ssxq = ss.toString();
                                                            if(ssxq!=null&&ssxq.length()>0){
                                                                Region region = regionDao.findById(Long.parseLong(ssxq));
                                                                if(region!=null){
                                                                    datas.append("'" + name + "': '" + region.getRegionName() + "'");
                                                                }else {
                                                                    datas.append("'" + name + "': '"+ssxq+"'");
                                                                }
                                                            }else {
                                                                datas.append("'" + name + "': ''");
                                                            }
                                                        }else {
                                                            datas.append("'" + name + "': ''");
                                                        }
                                                        if (i != (zx.length - 1)) {
                                                            datas.append(",");
                                                        }
                                                    }else {
                                                        Object ss = "";
                                                        try {
                                                            ss = object.get(name);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        datas.append("'" + name + "': '" + ss + "'");
                                                        if (i != (zx.length - 1)) {
                                                            datas.append(",");
                                                        }
                                                    }
                                                }
                                            }
                                            datas.append("}");
                                        } else {
                                            result_success = false;
                                            result_msg = "未定义展示字段";
                                        }
                                    }else {
                                        result_success = false;
                                        result_msg = "查无此人";
                                    }
                                }else {
                                    result_success = false;
                                    result_msg = "查无此人";
                                }
                            }else {
                                result_success = false;
                                result_msg = "查无此人";
                            }
                        }else {
                            result_success = false;
                            result_msg = "服务器返回:"+InterfaceUtils.getCode(code);
                        }
                    }else {
                        result_success = false;
                        result_msg = "网络故障,请重新查询";
                    }
                }
            } else {
                result_success = false;
                result_msg = "数据表未找到，查询出错";
            }
        } else {
            result_success = false;
            result_msg = "数据表配置文件未找到，查询出错";
        }

        datas.append("]");

        StringBuilder columModles = new StringBuilder();

        columModles.append("'columModles':").append("[");
        if (zx != null && zx.length > 0) {
            for (int i = 0; i < zx.length; i++) {
                Element selectSingleNode = (Element) doc.selectSingleNode("/tables/table[@name='" + tableName + "']/column[@name='" + zx[i] + "']");
                if (selectSingleNode != null) {
                    Element comment = selectSingleNode.element("comment");
                    String commentText = comment.getText();
                    if (zx[i].equals("URL")) {
                        columModles.append("{'header': '" + commentText + "','dataIndex': '" + zx[i] + "',renderer: function(value, p, r){return '<img onload =javascript:reSizeImage(50,50,this); src=\"' + value + '\">';}}");
                    } else if(zx[i].equals("XB")) {
                        columModles.append("{'header': '" + commentText + "','dataIndex': '" + zx[i] + "',renderer: show_sex}");
                    }else if(zx[i].equals("MZ")) {
                        columModles.append("{'header': '" + commentText + "','dataIndex': '" + zx[i] + "',renderer: show_mz}");
                    }/*else if(zx[i].equals("SSXQ")) {
                        columModles.append("{'header': '" + commentText + "','dataIndex': '" + zx[i] + "'}");
                    }*/else {
                        columModles.append("{'header': '" + commentText + "','dataIndex': '" + zx[i] + "'}");
                    }
                    if (i != (zx.length - 1)) {
                        columModles.append(",");
                    }
                }
            }
        }
        columModles.append("]");

        StringBuilder fields = new StringBuilder();
        fields.append("'fieldsNames':").append("[");
        if (zx != null && zx.length > 0) {
            for (int i = 0; i < zx.length; i++) {
                fields.append("{'name': '" + zx[i] + "'}");
                if (i != (zx.length - 1)) {
                    fields.append(",");
                }
            }
        }
        fields.append("]");


        StringBuilder json = new StringBuilder();

        json.append("{success:"+result_success+",'msg':'"+result_msg+"'");
        json.append(",");
        json.append(datas);
        json.append(",");
        json.append(columModles);
        json.append(",");
        json.append(fields);
        json.append("}");

        /* json = "{success:true,'msg':'success'"+",'" +
                    "data':[" +
                            "{" +
                                "'number':'1'" + "," +
                                "'text1': '3'" + "," +
                                "'info1': '4'" + "," +
                                "'special1': '5'"    +
                            "}" +
                        "]" + "," +
                    "'columModles':" +
                        "[" +
                            "{'header': '序号','dataIndex': 'number','width':40}," +
                            "{'header': '编码','dataIndex': 'text1'}," +
                            "{'header': '名称','dataIndex': 'info1'}," +
                            "{'header': '金额','dataIndex': 'special1'}" +
                        "]" + "," +
                    "'fieldsNames':" +
                        "[" +
                            "{name: 'number'}" + "," +
                            "{name: 'text1'}" + "," +
                            "{name: 'info1'}" + "," +
                            "{name: 'special1'}" +
                        "]" +
                "}";*/

        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

   /* public String findGridAll() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String tj = request.getParameter("tj");
        String value = request.getParameter("value");
        int start = Integer.parseInt(request.getParameter("start"));
        int limit = Integer.parseInt(request.getParameter("end"));
        String query_rKxx = "1=1";
        String query_rKys = "1=1";
        String query_rKzp = "1=1";
        String query_zXxx = "1=1";
        if (tj != null && value != null) {
            if (tj.equals("RYSBH_TJ")) {
                query_rKxx += " and RYSBH='" + value + "'";
                query_rKys += " and RYSBH='" + value + "'";
                query_rKzp += " and RYSBH='" + value + "'";
                query_zXxx += " and RYSBH='" + value + "'";
            } else if (tj.equals("SJBH_TJ")) {
                query_rKxx += " and SJBH='" + value + "'";
                query_rKys += " and SJBH='" + value + "'";
//            query_rKzp +=" and SJBH='"+value+"'";
//            query_zXxx +=" and SJBH='"+value+"'";
            } else if (tj.equals("XM_TJ")) {
                query_rKxx += " and XM='" + value + "'";
                query_rKys += " and XM='" + value + "'";
                query_rKzp += " and XM='" + value + "'";
                query_zXxx += " and XM='" + value + "'";
            } else if (tj.equals("XB_TJ")) {
                query_rKxx += " and XB='" + value + "'";
                query_rKys += " and XB='" + value + "'";
//            query_rKzp +=" and XB='"+value+"'";
//            query_zXxx +=" and XB='"+value+"'";
            } else if (tj.equals("MZ_TJ")) {
                query_rKxx += " and MZ='" + value + "'";
                query_rKys += " and MZ='" + value + "'";
//            query_rKzp +=" and MZ='"+value+"'";
//            query_zXxx +=" and MZ='"+value+"'";
            } else if (tj.equals("CSRQ_TJ")) {
                query_rKxx += " and CSRQ='" + value + "'";
//            query_rKys +=" and CSRQ='"+value+"'";
//            query_rKzp +=" and CSRQ='"+value+"'";
//            query_zXxx +=" and CSRQ='"+value+"'";
            } else if (tj.equals("SWYY_TJ")) {
                query_rKxx += " and SWYY='" + value + "'";
//            query_rKys +=" and SWYY='"+value+"'";
//            query_rKzp +=" and SWYY='"+value+"'";
                query_zXxx += " and SWYY='" + value + "'";
            } else if (tj.equals("SWSJ_TJ")) {
                query_rKxx += " and SWSJ='" + value + "'";
//            query_rKys +=" and SWSJ='"+value+"'";
//            query_rKzp +=" and SWSJ='"+value+"'";
                query_zXxx += " and SWSJ='" + value + "'";
            } else if (tj.equals("SFHM_TJ")) {
                query_rKxx += " and SFHM='" + value + "'";
                query_rKys += " and GMSFHM='" + value + "'";
                query_rKzp += " and GMSFHM='" + value + "'";
                query_zXxx += " and SFHM='" + value + "'";
            } else if (tj.equals("HH_TJ")) {
                query_rKxx += " and HH='" + value + "'";
//            query_rKys +=" and HH='"+value+"'";
//            query_rKzp +=" and HH='"+value+"'";
//            query_zXxx +=" and HH='"+value+"'";
            } else if (tj.equals("HKLX_TJ")) {
                query_rKxx += " and HKLX='" + value + "'";
//            query_rKys +=" and HKLX='"+value+"'";
//            query_rKzp +=" and HKLX='"+value+"'";
//            query_zXxx +=" and HKLX='"+value+"'";
            } else if (tj.equals("YHZGX_TJ")) {
                query_rKxx += " and YHZGX='" + value + "'";
//            query_rKys +=" and YHZGX='"+value+"'";
//            query_rKzp +=" and YHZGX='"+value+"'";
//            query_zXxx +=" and YHZGX='"+value+"'";
            }else if (tj.equals("JGGJ_TJ")) {
//            query_rKxx +=" and JGGJ='"+value+"'";
                query_rKys += " and JGGJ='" + value + "'";
//            query_rKzp +=" and JGGJ='"+value+"'";
//            query_zXxx +=" and JGGJ='"+value+"'";
            } else if (tj.equals("JGSSX_TJ")) {
//            query_rKxx +=" and JGSSX='"+value+"'";
                query_rKys += " and JGSSX='" + value + "'";
//            query_rKzp +=" and JGSSX='"+value+"'";
//            query_zXxx +=" and JGSSX='"+value+"'";
            } else if (tj.equals("ZPXH_TJ")) {
//            query_rKxx +=" and ZPXH='"+value+"'";
//            query_rKys +=" and ZPXH='"+value+"'";
                query_rKzp += " and ZPXH='" + value + "'";
//            query_zXxx +=" and ZPXH='"+value+"'";
            } else if (tj.equals("ZZPBS_TJ")) {
//            query_rKxx +=" and ZZPBS='"+value+"'";
//            query_rKys +=" and ZZPBS='"+value+"'";
                query_rKzp += " and ZZPBS='" + value + "'";
//            query_zXxx +=" and ZZPBS='"+value+"'";
            } else if (tj.equals("URL_TJ")) {
//            query_rKxx +=" and URL='"+value+"'";
//            query_rKys +=" and URL='"+value+"'";
                query_rKzp += " and URL='" + value + "'";
//            query_zXxx +=" and URL='"+value+"'";
            } else if (tj.equals("SSXQ_TJ")) {
//            query_rKxx +=" and SSXQ='"+value+"'";
//            query_rKys +=" and SSXQ='"+value+"'";
//            query_rKzp +=" and SSXQ='"+value+"'";
                query_zXxx += " and SSXQ='" + value + "'";
            } else if (tj.equals("PCSBM_TJ")) {
//            query_rKxx +=" and PCSBM ='"+value+"'";
//            query_rKys +=" and PCSBM ='"+value+"'";
//            query_rKzp +=" and PCSBM ='"+value+"'";
                query_zXxx += " and PCSBM='" + value + "'";
            } else if (tj.equals("GDSJ_TJ")) {
//            query_rKxx +=" and GDSJ ='"+value+"'";
//            query_rKys +=" and GDSJ ='"+value+"'";
//            query_rKzp +=" and GDSJ ='"+value+"'";
                query_zXxx += " and GDSJ='" + value + "'";
            } else if (tj.equals("GLZT_TJ")) {
//            query_rKxx +=" and GLZT ='"+value+"'";
//            query_rKys +=" and GLZT ='"+value+"'";
//            query_rKzp +=" and GLZT ='"+value+"'";
                query_zXxx += " and GLZT='" + value + "'";
            }
        }

        Document doc = Dom4jUtil.getDocument(StringContext.tables_xml);
        String policeId = "064740";
        String serviceId_zXxx = "100103";   //注销信息
        String serviceId_rKxx = "100097";   //人口信息
        String serviceId_rKzp = "100098";   //人口照片
        String serviceId_rKys = "100088";   //人口要素
        ////注销信息
        StringBuilder datas_zXxx = new StringBuilder();
        datas_zXxx.append("'data_zXxx':").append("[");
        List<Element> selectNodes_zXxx = doc.selectNodes("/tables/table[@name='DRCZRKZXXX']/column");
        if (doc != null) {
            Add add_h = new Add();
            AddPortType addPortType = add_h.getAddHttpPort();
            logger.info("policeId:" + policeId + "," +"serviceId:" + serviceId_zXxx +  ",queryStr" + serviceId_zXxx + "," + start + "," + limit);
            ArrayOfArrayOfString arrayOfArrayOfString = addPortType.getData(policeId, serviceId_zXxx, query_zXxx, start, limit, 1);
            File file = new File(StringContext.systemPath + "/getdata_zXxx.txt");
            if (file.exists()) {
                file.delete();
            }
            writeFile(arrayOfArrayOfString, StringContext.systemPath + "/getdata_zXxx.txt");
            if (file.exists()) {
                List<String> listStr = FileUtil.readFileByLines(StringContext.systemPath + "/getdata_zXxx.txt");
                int columns = selectNodes_zXxx.size();
                Map<Integer, String> data_header = new HashMap<>();
                int add = 0;
                for (int i = 0; i < listStr.size(); i++) {
                    if (i < columns) {
                        data_header.put(i, listStr.get(i));
                    } else {
                        int key = i % columns;
                        if (key == 0) {
                            datas_zXxx.append("{");
                            if (data_header.containsKey(key)) {
                                datas_zXxx.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'").append(",");
                                add += 1;
                            }
                        } else if (key + 1 == columns) {
                            if (data_header.containsKey(key)) {
                                datas_zXxx.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'");
                                add = 0;
                            }
                            datas_zXxx.append("}");
                            if (i != (listStr.size() - 1)) {
                                datas_zXxx.append(",");
                            }
                        } else {
                            if (data_header.containsKey(key)) {
                                datas_zXxx.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'");
                                add += 1;
                                if (add == data_header.size()) {
                                    add = 0;
                                } else {
                                    datas_zXxx.append(",");
                                }
                            }
                        }
                    }
                }
            }
        }
        datas_zXxx.append("]");

        StringBuilder columModles_zXxx = new StringBuilder();
        columModles_zXxx.append("'columModles_zXxx':").append("[");
        if (selectNodes_zXxx != null) {
            for (int i = 0; i < selectNodes_zXxx.size(); i++) {
                Element comment = selectNodes_zXxx.get(i);
                String header = comment.attributeValue("name");
                String commentText = comment.elementText("comment");
                if(showValue(header)) {
                    if (header.equals("URL")) {
                        columModles_zXxx.append("{'header': '" + commentText + "','dataIndex': '" + header + "',renderer: function(value, p, r){return '<img src=\"' + value + '\">';}}");
                    } else {
                        columModles_zXxx.append("{'header': '" + commentText + "','dataIndex': '" + header + "'}");
                    }
                }else {
                    if (header.equals("URL")) {
                        columModles_zXxx.append("{'header': '" + commentText + "',hidden:true,'dataIndex': '" + header + "',renderer: function(value, p, r){return '<img src=\"' + value + '\">';}}");
                    } else {
                        columModles_zXxx.append("{'header': '" + commentText + "',hidden:true,'dataIndex': '" + header + "'}");
                    }
                }

                if (i != (selectNodes_zXxx.size() - 1)) {
                    columModles_zXxx.append(",");
                }
            }
        }
        columModles_zXxx.append("]");

        StringBuilder fields_zXxx = new StringBuilder();
        fields_zXxx.append("'fieldsNames_zXxx':").append("[");
        if (selectNodes_zXxx != null) {
            for (int i = 0; i < selectNodes_zXxx.size(); i++) {
                Element comment = selectNodes_zXxx.get(i);
                String header = comment.attributeValue("name");
                fields_zXxx.append("{'name': '" + header + "'}");
                if (i != (selectNodes_zXxx.size() - 1)) {
                    fields_zXxx.append(",");
                }
            }
        }
        fields_zXxx.append("]");


        //人口信息
        StringBuilder datas_rKxx = new StringBuilder();
        datas_rKxx.append("'data_rKxx':").append("[");
        List<Element> selectNodes_rKxx = doc.selectNodes("/tables/table[@name='DRCZRKXX']/column");
        if (doc != null) {
            Add add_h = new Add();
            AddPortType addPortType = add_h.getAddHttpPort();
            logger.info("policeId:" + policeId + "," + "serviceId:" + serviceId_rKxx +  ",queryStr" + serviceId_rKxx + "," + start + "," + limit);
            ArrayOfArrayOfString arrayOfArrayOfString = addPortType.getData(policeId, serviceId_rKxx, query_rKxx, start, limit, 1);
            File file = new File(StringContext.systemPath + "/getdata_rKxx.txt");
            if (file.exists()) {
                file.delete();
            }
            writeFile(arrayOfArrayOfString, StringContext.systemPath + "/getdata_rKxx.txt");
            if (file.exists()) {
                List<String> listStr = FileUtil.readFileByLines(StringContext.systemPath + "/getdata_rKxx.txt");
                int columns = selectNodes_rKxx.size();
                Map<Integer, String> data_header = new HashMap<>();
                int add = 0;
                for (int i = 0; i < listStr.size(); i++) {
                    if (i < columns) {
                        data_header.put(i, listStr.get(i));
                    } else {
                        int key = i % columns;
                        if (key == 0) {
                            datas_rKxx.append("{");
                            if (data_header.containsKey(key)) {
                                datas_rKxx.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'").append(",");
                                add += 1;
                            }
                        } else if (key + 1 == columns) {
                            if (data_header.containsKey(key)) {
                                datas_rKxx.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'");
                                add = 0;
                            }
                            datas_rKxx.append("}");
                            if (i != (listStr.size() - 1)) {
                                datas_rKxx.append(",");
                            }
                        } else {
                            if (data_header.containsKey(key)) {
                                datas_rKxx.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'");
                                add += 1;
                                if (add == data_header.size()) {
                                    add = 0;
                                } else {
                                    datas_rKxx.append(",");
                                }
                            }
                        }
                    }
                }
            }
        }
        datas_rKxx.append("]");

        StringBuilder columModles_rKxx = new StringBuilder();
        columModles_rKxx.append("'columModles_rKxx':").append("[");
        if (selectNodes_rKxx != null) {
            for (int i = 0; i < selectNodes_rKxx.size(); i++) {
                Element comment = selectNodes_rKxx.get(i);
                String header = comment.attributeValue("name");
                String commentText = comment.elementText("comment");
                if(showValue(header)) {
                    if (header.equals("URL")) {
                        columModles_rKxx.append("{'header': '" + commentText + "','dataIndex': '" + header + "',renderer: function(value, p, r){return '<img src=\"' + value + '\">';}}");
                    } else {
                        columModles_rKxx.append("{'header': '" + commentText + "','dataIndex': '" + header + "'}");
                    }
                }else {
                    if (header.equals("URL")) {
                        columModles_rKxx.append("{'header': '" + commentText + "',hidden:true,'dataIndex': '" + header + "',renderer: function(value, p, r){return '<img src=\"' + value + '\">';}}");
                    } else {
                        columModles_rKxx.append("{'header': '" + commentText + "',hidden:true,'dataIndex': '" + header + "'}");
                    }
                }
                if (i != (selectNodes_rKxx.size() - 1)) {
                    columModles_rKxx.append(",");
                }
            }
        }
        columModles_rKxx.append("]");

        StringBuilder fields_rkxx = new StringBuilder();
        fields_rkxx.append("'fieldsNames_rkxx':").append("[");
        if (selectNodes_rKxx != null) {
            for (int i = 0; i < selectNodes_rKxx.size(); i++) {
                Element comment = selectNodes_rKxx.get(i);
                String header = comment.attributeValue("name");
                fields_rkxx.append("{'name': '" + header + "'}");
                if (i != (selectNodes_rKxx.size() - 1)) {
                    fields_rkxx.append(",");
                }
            }
        }
        fields_rkxx.append("]");

        //照片信息
        StringBuilder datas_rKzp = new StringBuilder();
        datas_rKzp.append("'data_rKzp':").append("[");
        List<Element> selectNodes_rKzp = doc.selectNodes("/tables/table[@name='DRCZRKZPXX']/column");
        if (doc != null) {
            Add add_h = new Add();
            AddPortType addPortType = add_h.getAddHttpPort();
             logger.info("policeId:" + policeId + "," + "serviceId:" + serviceId_rKzp +   ",queryStr" + serviceId_rKzp + "," + start + "," + limit);
            ArrayOfArrayOfString arrayOfArrayOfString = addPortType.getData(policeId, serviceId_rKzp, query_rKzp, start, limit, 1);
            File file = new File(StringContext.systemPath + "/getdata_rKzp.txt");
            if (file.exists()) {
                file.delete();
            }
            writeFile(arrayOfArrayOfString, StringContext.systemPath + "/getdata_rKzp.txt");
            if (file.exists()) {
                List<String> listStr = FileUtil.readFileByLines(StringContext.systemPath + "/getdata_rKzp.txt");
                int columns = selectNodes_rKzp.size();
                Map<Integer, String> data_header = new HashMap<>();
                int add = 0;
                for (int i = 0; i < listStr.size(); i++) {
                    if (i < columns) {
                        data_header.put(i, listStr.get(i));
                    } else {
                        int key = i % columns;
                        if (key == 0) {
                            datas_rKzp.append("{");
                            if (data_header.containsKey(key)) {
                                datas_rKzp.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'").append(",");
                                add += 1;
                            }
                        } else if (key + 1 == columns) {
                            if (data_header.containsKey(key)) {
                                datas_rKzp.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'");
                                add = 0;
                            }
                            datas_rKzp.append("}");
                            if (i != (listStr.size() - 1)) {
                                datas_rKzp.append(",");
                            }
                        } else {
                            if (data_header.containsKey(key)) {
                                datas_rKzp.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'");
                                add += 1;
                                if (add == data_header.size()) {
                                    add = 0;
                                } else {
                                    datas_rKzp.append(",");
                                }
                            }
                        }
                    }
                }
            }
        }
        datas_rKzp.append("]");

        StringBuilder columModles_rKzp = new StringBuilder();
        columModles_rKzp.append("'columModles_rKzp':").append("[");
        if (selectNodes_rKzp != null) {
            for (int i = 0; i < selectNodes_rKzp.size(); i++) {
                Element comment = selectNodes_rKzp.get(i);
                String header = comment.attributeValue("name");
                String commentText = comment.elementText("comment");
                if(showValue(header)) {
                    if (header.equals("URL")) {
                        columModles_rKzp.append("{'header': '" + commentText + "','dataIndex': '" + header + "',renderer: function(value, p, r){return '<img src=\"' + value + '\">';}}");
                    } else {
                        columModles_rKzp.append("{'header': '" + commentText + "','dataIndex': '" + header + "'}");
                    }
                }else {
                    if (header.equals("URL")) {
                        columModles_rKzp.append("{'header': '" + commentText + "',hidden:true,'dataIndex': '" + header + "',renderer: function(value, p, r){return '<img src=\"' + value + '\">';}}");
                    } else {
                        columModles_rKzp.append("{'header': '" + commentText + "',hidden:true,'dataIndex': '" + header + "'}");
                    }
                }
                if (i != (selectNodes_rKzp.size() - 1)) {
                    columModles_rKzp.append(",");
                }
            }
        }
        columModles_rKzp.append("]");

        StringBuilder fields_rkzp = new StringBuilder();
        fields_rkzp.append("'fieldsNames_rkzp':").append("[");
        if (selectNodes_rKzp != null) {
            for (int i = 0; i < selectNodes_rKzp.size(); i++) {
                Element comment = selectNodes_rKzp.get(i);
                String header = comment.attributeValue("name");
                fields_rkzp.append("{'name': '" + header + "'}");
                if (i != (selectNodes_rKzp.size() - 1)) {
                    fields_rkzp.append(",");
                }
            }
        }
        fields_rkzp.append("]");


        //人口要素
        StringBuilder datas_rKys = new StringBuilder();
        datas_rKys.append("'data_rKys':").append("[");
        List<Element> selectNodes_rKys = doc.selectNodes("/tables/table[@name='DRCZRKYS']/column");
        if (doc != null) {
            Add add_h = new Add();
            AddPortType addPortType = add_h.getAddHttpPort();
             logger.info("policeId:" + policeId + "," + "serviceId:" + serviceId_rKys +    ",queryStr" + query_rKys + "," + start + "," + limit);
            ArrayOfArrayOfString arrayOfArrayOfString = addPortType.getData(policeId, serviceId_rKys, query_rKys, start, limit, 1);
            File file = new File(StringContext.systemPath + "/getdata_rKys.txt");
            if (file.exists()) {
                file.delete();
            }
            writeFile(arrayOfArrayOfString, StringContext.systemPath + "/getdata_rKys.txt");
            if (file.exists()) {
                List<String> listStr = FileUtil.readFileByLines(StringContext.systemPath + "/getdata_rKys.txt");
                int columns = selectNodes_rKys.size();
                Map<Integer, String> data_header = new HashMap<>();
                int add = 0;
                for (int i = 0; i < listStr.size(); i++) {
                    if (i < columns) {
                        data_header.put(i, listStr.get(i));
                    } else {
                        int key = i % columns;
                        if (key == 0) {
                            datas_rKys.append("{");
                            if (data_header.containsKey(key)) {
                                datas_rKys.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'").append(",");
                                add += 1;
                            }
                        } else if (key + 1 == columns) {
                            if (data_header.containsKey(key)) {
                                datas_rKys.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'");
                                add = 0;
                            }
                            datas_rKys.append("}");
                            if (i != (listStr.size() - 1)) {
                                datas_rKys.append(",");
                            }
                        } else {
                            if (data_header.containsKey(key)) {
                                datas_rKys.append("'" + data_header.get(key) + "': '" + listStr.get(i) + "'");
                                add += 1;
                                if (add == data_header.size()) {
                                    add = 0;
                                } else {
                                    datas_rKys.append(",");
                                }
                            }
                        }
                    }
                }
            }
        }
        datas_rKys.append("]");

        StringBuilder columModles_rKys = new StringBuilder();
        columModles_rKys.append("'columModles_rKys':").append("[");
        if (selectNodes_rKys != null) {
            for (int i = 0; i < selectNodes_rKys.size(); i++) {
                Element comment = selectNodes_rKys.get(i);
                String header = comment.attributeValue("name");
                String commentText = comment.elementText("comment");
                if(showValue(header)) {
                    if (header.equals("URL")) {
                        columModles_rKys.append("{'header': '" + commentText + "','dataIndex': '" + header + "',renderer: function(value, p, r){return '<img src=\"' + value + '\">';}}");
                    } else {
                        columModles_rKys.append("{'header': '" + commentText + "','dataIndex': '" + header + "'}");
                    }
                }else {
                    if (header.equals("URL")) {
                        columModles_rKys.append("{'header': '" + commentText + "',hidden:true,'dataIndex': '" + header + "',renderer: function(value, p, r){return '<img src=\"' + value + '\">';}}");
                    } else {
                        columModles_rKys.append("{'header': '" + commentText + "',hidden:true,'dataIndex': '" + header + "'}");
                    }
                }
                if (i != (selectNodes_rKys.size() - 1)) {
                    columModles_rKys.append(",");
                }
            }
        }
        columModles_rKys.append("]");

        StringBuilder fields_rkys = new StringBuilder();
        fields_rkys.append("'fieldsNames_rkys':").append("[");
        if (selectNodes_rKys != null) {
            for (int i = 0; i < selectNodes_rKys.size(); i++) {
                Element comment = selectNodes_rKys.get(i);
                String header = comment.attributeValue("name");
                fields_rkys.append("{'name': '" + header + "'}");
                if (i != (selectNodes_rKys.size() - 1)) {
                    fields_rkys.append(",");
                }
            }
        }
        fields_rkys.append("]");

        StringBuilder json = new StringBuilder();
        json.append("{success:true,'msg':'success'");
        json.append(",");
        json.append(datas_zXxx);
        json.append(",");
        json.append(columModles_zXxx);
        json.append(",");
        json.append(fields_zXxx);
        json.append(",");
        json.append(datas_rKxx);
        json.append(",");
        json.append(columModles_rKxx);
        json.append(",");
        json.append(fields_rkxx);
        json.append(",");
        json.append(datas_rKzp);
        json.append(",");
        json.append(columModles_rKzp);
        json.append(",");
        json.append(fields_rkzp);
        json.append(",");
        json.append(datas_rKys);
        json.append(",");
        json.append(columModles_rKys);
        json.append(",");
        json.append(fields_rkys);
        json.append("}");
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }*/

    public static void writeFileEnd(List<String> listStr, String filePath) {
        for (String str : listStr) {
            FileUtil.writeOneLine(str, new File(filePath));
        }
    }

    public static void writeFile(List<ArrayOfString> list, String filePath) {
        for (ArrayOfString arrayOfString : list) {
            List<String> listStr = arrayOfString.getString();
            writeFileEnd(listStr, filePath);
        }
    }

    public static void writeFile(ArrayOfArrayOfString arrayOfArrayOfString, String filePath) {
        List<ArrayOfString> list = arrayOfArrayOfString.getArrayOfString();
        writeFile(list, filePath);
    }

    public String del() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String interfaceId = request.getParameter("interfaceId");
        String json = null;
        String msg = null;
        try {
            interfaceService.del(new InterfaceObj(Long.parseLong(interfaceId)));
            msg = "删除接口服务成功";
            json = "{success:true,msg:'" + msg + "'}";
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "接口服务", "删除接口服务接口服务" +interfaceId + "成功", 1);
        } catch (Exception e) {
            msg = "删除接口服务失败";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "接口服务", "删除接口服务信息失败" + e.getMessage(), 1);
            logger.error("接口服务", e);
            json = "{success:false,msg:'" + msg + "'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String selectAuthUser() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        try {
            json = interfaceService.selectAuthUser(interfaceId, start, limit);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "接口服务", "获取授权接口服务" + interfaceId + "的用户列表成功", 1);

        } catch (Exception e) {
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "接口服务", "获取授权接口服务" + interfaceId + "的用户列表失败" + e.getMessage(), 1);
            logger.error("接口服务", e);
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String authInterface() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try {
            interfaceService.authInterface(userNames, interfaceId);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "接口服务", "授权" + userName + "接口服务" + interfaceId + "成功", 1);
            msg = "授权成功";
        } catch (Exception e) {
            msg = "授权失败";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "接口服务", "授权" + userName + "接口服务" + interfaceId + "失败" + e.getMessage(), 1);
            logger.error("接口服务", e);
        }

        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String unAuthInterface() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try {
            interfaceService.unAuthInterface(userName, interfaceId);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "接口服务", "解除" + userName + "的授权接口服务" + interfaceId + "成功", 1);
            msg = "解除授权成功";
        } catch (Exception e) {
            msg = "解除授权失败";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "接口服务", "解除" + userName + "的授权接口服务" + interfaceId + "失败" + e.getMessage(), 1);
            logger.error("接口服务", e);
        }

        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String[] zxArray = request.getParameterValues("zxArray");
//        String tjArray = request.getParameter("tjArray");

        String msg = null;
        try {
            StringBuilder tj_sb = new StringBuilder();
            for (int i = 0; i < zxArray.length; i++) {
                tj_sb.append(zxArray[i]);
                if (i != (zxArray.length - 1)) {
                    tj_sb.append(",");
                }
            }
            interfaceObj.setConditionsField(tj_sb.toString());
//            JSONArray tjJSONArray = JSONArray.fromObject(tjArray);

         /*   StringBuilder tj_sb = new StringBuilder();
            for (int i = 0; i < tjJSONArray.size(); i++) {
                Object object = tjJSONArray.get(i);
                JSONObject o = JSONObject.fromObject(object);
                if (o.getBoolean("success")) {
                    tj_sb.append(o.getString("field"));
                    if (i != (tjJSONArray.size() - 1)) {
                        tj_sb.append(",");
                    }
                }
                interfaceObj.setQueryField(tj_sb.toString());
            }*/
            interfaceObj.setApply("");
            interfaceService.insert(interfaceObj);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "接口服务", "新增接口服务接口服务" + interfaceObj.getInterfaceNumber() + "成功", 1);
            msg = "新增接口服务成功";
        } catch (Exception e) {
            msg = "新增接口服务失败";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "接口服务", "新增接口服务接口服务" + interfaceObj.getInterfaceNumber() + "失败" + e.getMessage(), 1);
            logger.error("接口服务", e);
        }

        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String applyInterface() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try {
            interfaceService.applyInterface(interfaceId, SessionUtils.getAccount(request).getId());
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "接口服务", "申请授权接口服务" + interfaceId + "成功", 1);
            msg = "申请成功,等待授权";
        } catch (Exception e) {
            msg = "申请失败";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "接口服务", "申请授权接口服务" + interfaceId + "失败" + e.getMessage(), 1);
            logger.error("接口服务", e);
        }

        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setInterfaceService(InterfaceService interfaceService) {
        this.interfaceService = interfaceService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  /*  public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }*/

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

    public long getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(long interfaceId) {
        this.interfaceId = interfaceId;
    }

    public long[] getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(long[] accountIds) {
        this.accountIds = accountIds;
    }

    public String[] getUserNames() {
        return userNames;
    }

    public void setUserNames(String[] userNames) {
        this.userNames = userNames;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public InterfaceObj getInterfaceObj() {
        return interfaceObj;
    }

    public void setInterfaceObj(InterfaceObj interfaceObj) {
        this.interfaceObj = interfaceObj;
    }
}
