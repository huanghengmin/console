package com.hzih.stp.web.thread;

import com.hzih.stp.constant.ServiceConstant;
import com.hzih.stp.service.LogService;
import com.hzih.stp.service.XmlOperatorService;
import com.hzih.stp.utils.Configuration;
import com.hzih.stp.utils.StringContext;
import com.hzih.stp.utils.StringUtils;
import com.inetec.common.config.stp.nodes.*;
import com.inetec.common.exception.Ex;
import com.inetec.ichange.console.config.Constant;
import com.inetec.ichange.console.config.database.DBFactory;
import com.inetec.ichange.console.config.database.IDataBase;
import com.inetec.ichange.console.config.utils.TriggerBean;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-4
 * Time: 下午4:37
 * To change this template use File | Settings | File Templates.
 */
public class TestTriggerThread implements Runnable {
    private final static Logger logger = Logger.getLogger(TestTriggerThread.class);
    private ServletContext servletContext;

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public TestTriggerThread(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void run() {
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);
        LogService logService = (LogService)context.getBean(ServiceConstant.LOG_SERVICE);
        XmlOperatorService xmlOperatorService = (XmlOperatorService)context.getBean(ServiceConstant.XML_OPERATOR_SERVICE);
        //判断为内网还是外网
        boolean privated = StringUtils.getPrivated();
        String xmlType = privated == true?"internal":"external";
        List<Jdbc> jdbcs = null;
        Configuration config = null;
        List<Type> types = null;
        List<Table> tables = null;
        List<Field> fields = new ArrayList<Field>();
        List<String> pkFieldsList = null;
        while (!privated){
            try{
                //获取所有应用
                types = null;
                types = xmlOperatorService.readType(xmlType,"db");
                for(int i=0;i<types.size();i++){
                    //获取应用中的所有表
                    tables = null;
                    config = getConfigTypeXmlHead("external");
                    DataBase s = config.getDataBase(types.get(i).getTypeName());
                    if(!s.getOperation().equals("trigger")){
                        continue;
                    }
                    tables = xmlOperatorService.readTableNames(xmlType,types.get(i).getTypeName(),s.getDbName());
                    Jdbc jdbc = config.getJdbc(s.getDbName());
                    IDataBase db = DBFactory.getDataBase(jdbc, Constant.DB_INTERNAL);
                    db.openConnection();
                    Map<String,String> triggers = null;

                    for(int j=0;j<tables.size();j++){
                        //获取表结构
                        pkFieldsList = new ArrayList<String>();
                        fields = null;
                        fields = config.getSourceFileds(types.get(i).getTypeName(),s.getDbName(),tables.get(j).getTableName());
                        for (Field field : fields) {
                            if(field.isPk()){
                                pkFieldsList.add(field.getFieldName());
                            }
                        }
                        String[] pkFields = pkFieldsList.toArray(new String[pkFieldsList.size()]);
                        //查询触发器
                        triggers = new HashMap<>();
                        //数据库表中存在的触发器
                        triggers = db.getTriggers(tables.get(j));
                        if(tables.get(j).getDeleteTrigger()!= null){
                            //配置文件中存在删除触发器
                            if(triggers != null && triggers.containsKey(tables.get(j).getDeleteTrigger())){
                                //判断table和temptable是否指向正确
                                String triBody = triggers.get(tables.get(j).getDeleteTrigger());
                                if(s.getTempTable()!= null && triBody.toUpperCase().indexOf(s.getTempTable().toUpperCase()) > -1){
                                    ;
                                } else {
                                    //修改触发器
                                    TriggerBean triggerBean = new TriggerBean();
                                    triggerBean.setMonitorDelete(true);
                                    triggerBean.setTableName(tables.get(j).getTableName());
                                    triggerBean.setPkFields(pkFields);
                                    db.updateTriggersByName(tables.get(j).getDeleteTrigger(),s.getTempTable(),triggerBean);
                                    logService.newSysLog("INFO","数据库同步","检测触发器","修改表 "+tables.get(j).getTableName()+" 中删除触发器"+tables.get(j).getDeleteTrigger()+" 成功!");
                                }
                            }else {
                                //创建触发器
                                TriggerBean triggerBean = new TriggerBean();
                                triggerBean.setMonitorDelete(true);
                                triggerBean.setTableName(tables.get(j).getTableName());
                                triggerBean.setPkFields(pkFields);
                                Map<String,String> add_triggers =db.updateTriggersByName(tables.get(j).getDeleteTrigger(),s.getTempTable(),triggerBean);
                                if(add_triggers.get("delete") != null){
                                    tables.get(j).setDeleteTrigger(add_triggers.get("delete"));
                                    config.editSourceTable(types.get(i).getTypeName(),s.getDbName(),tables.get(j));
                                    config.saveExternal();
                                    logService.newSysLog("INFO","数据库同步","检测触发器","新增表 "+tables.get(j).getTableName()+" 中删除触发器"+tables.get(j).getDeleteTrigger()+" 成功!");
                                    triggers.put(tables.get(j).getDeleteTrigger(),tables.get(j).getDeleteTrigger());
                                }
                            }
                            if(triggers.containsKey(tables.get(j).getDeleteTrigger())){
                                triggers.remove(tables.get(j).getDeleteTrigger());
                            }
                        }
                        if(tables.get(j).getUpdateTrigger()!= null){
                            if(triggers != null &&triggers.containsKey(tables.get(j).getUpdateTrigger())){
                                //判断table和temptable是否指向正确
                                String triBody = triggers.get(tables.get(j).getUpdateTrigger());
                                if(s.getTempTable()!= null && triBody.toUpperCase().indexOf(s.getTempTable().toUpperCase()) > -1){
                                    continue;
                                } else {
                                    //修改触发器
                                    TriggerBean triggerBean = new TriggerBean();
                                    triggerBean.setMonitorUpdate(true);
                                    triggerBean.setTableName(tables.get(j).getTableName());
                                    triggerBean.setPkFields(pkFields);
                                    db.updateTriggersByName(tables.get(j).getUpdateTrigger(),s.getTempTable(),triggerBean);
                                    logService.newSysLog("INFO","数据库同步","检测触发器","修改表 "+tables.get(j).getTableName()+" 中更新触发器"+tables.get(j).getUpdateTrigger()+" 成功!");
                                }
                            }else {
                                //创建触发器
                                TriggerBean triggerBean = new TriggerBean();
                                triggerBean.setMonitorUpdate(true);
                                triggerBean.setTableName(tables.get(j).getTableName());
                                triggerBean.setPkFields(pkFields);
                                Map<String,String> add_triggers =db.updateTriggersByName(tables.get(j).getUpdateTrigger(),s.getTempTable(),triggerBean);
                                if(add_triggers.get("update") != null){
                                    tables.get(j).setUpdateTrigger(add_triggers.get("update"));
                                    config.editSourceTable(types.get(i).getTypeName(),s.getDbName(),tables.get(j));
                                    config.saveExternal();
                                    logService.newSysLog("INFO","数据库同步","检测触发器","新增表 "+tables.get(j).getTableName()+" 中更新触发器"+tables.get(j).getDeleteTrigger()+" 成功!");
                                }
                            }
                            if(triggers.containsKey(tables.get(j).getUpdateTrigger())){
                                triggers.remove(tables.get(j).getUpdateTrigger());
                            }
                        }
                        if(tables.get(j).getInsertTrigger()!= null){
                            if(triggers != null &&triggers.containsKey(tables.get(j).getInsertTrigger())){
                                //判断table和temptable是否指向正确
                                String triBody = triggers.get(tables.get(j).getInsertTrigger());
                                if(s.getTempTable()!= null && triBody.toUpperCase().indexOf(s.getTempTable().toUpperCase()) > -1){
                                    continue;
                                } else {
                                    //修改触发器
                                    TriggerBean triggerBean = new TriggerBean();
                                    triggerBean.setMonitorInsert(true);
                                    triggerBean.setTableName(tables.get(j).getTableName());
                                    triggerBean.setPkFields(pkFields);
                                    db.updateTriggersByName(tables.get(j).getInsertTrigger(),s.getTempTable(),triggerBean);
                                    logService.newSysLog("INFO","数据库同步","检测触发器","修改表 "+tables.get(j).getTableName()+" 中更新触发器"+tables.get(j).getInsertTrigger()+" 成功!");
                                }
                            }else {
                                //创建触发器
                                TriggerBean triggerBean = new TriggerBean();
                                triggerBean.setMonitorInsert(true);
                                triggerBean.setTableName(tables.get(j).getTableName());
                                triggerBean.setPkFields(pkFields);
                                Map<String,String> add_triggers =db.updateTriggersByName(tables.get(j).getInsertTrigger(),s.getTempTable(),triggerBean);
                                if(add_triggers.get("insert") != null){
                                    tables.get(j).setInsertTrigger(add_triggers.get("insert"));
                                    config.editSourceTable(types.get(i).getTypeName(),s.getDbName(),tables.get(j));
                                    config.saveExternal();
                                    logService.newSysLog("INFO","数据库同步","检测触发器","新增表 "+tables.get(j).getTableName()+" 中更新触发器"+tables.get(j).getInsertTrigger()+" 成功!");
                                }
                            }
                            if(triggers.containsKey(tables.get(j).getInsertTrigger())){
                                triggers.remove(tables.get(j).getInsertTrigger());
                            }
                        }
                        Iterator it =  triggers.keySet().iterator();
                        while (it.hasNext()){
                            //删除触发器
                            String triName = (String) it.next();
                            Table table = new Table();
                            table.setDeleteTrigger(triName);
                            db.removeTriggerByName(table);
                            logService.newSysLog("INFO","数据库同步","检测触发器","删除表 "+tables.get(j).getTableName()+" 中触发器"+triName+" 成功!");
                        }
                    }
                    db.closeConnection();
                    Thread.sleep((long)10*60*1000);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Configuration getConfigTypeXmlHead(String typeXml) throws Ex {
        Configuration config = null;
        if("internal".equals(typeXml)){
            config = new Configuration(StringContext.INTERNALXML);
        }else if("external".equals(typeXml)){
            config = new Configuration(StringContext.EXTERNALXML);
        }
        return config;
    }
}
