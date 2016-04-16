package com.inetec.ichange.console.config.database;

import com.inetec.common.config.stp.nodes.Field;
import com.inetec.common.config.stp.nodes.Table;
import com.inetec.common.exception.Ex;
import com.inetec.ichange.console.config.utils.TriggerBean;

import java.util.List;
import java.util.Map;


public interface IDataBase {

    public abstract String[] getTableNames() throws Ex;


    public abstract String[] getFieldNames(String tableName) throws Ex;


    public abstract List getFields(String tableName) throws Ex;


    public abstract Field getField(String tableName, String fieldName) throws Ex;


    public abstract void openConnection() throws Ex;
    /** add by ztt */
    public List getForeignKey(String tablename) throws Ex;
    public abstract void closeConnection() throws Ex;


    public abstract void createTrigger(TriggerBean[] triggers, String tempTable) throws Ex;

    public abstract void removeTrigger(TriggerBean[] triggers, String tempTable) throws Ex;

    public abstract void createFlag(String[] tableNames) throws Ex;

    public abstract void removeFlag(String[] tableNames) throws Ex;


    public abstract boolean isConnectable() throws Ex;

    public abstract void createFunction() throws Ex;

    public abstract void deleteFunction() throws Ex;

    public abstract void createSequence() throws Ex;

    public abstract void createMoreSequence(String appName) throws Ex;

    public abstract void removeSequence() throws Ex;

    public abstract void removeMoreSequence(String appName) throws Ex;


    public abstract void createTempTable(String temptable) throws Ex;

    public abstract void removeTempTable(String temptable) throws Ex;

    /**
     * 创建触发器,并返回触发器的名称集合
     * @param triggers
     * @param tempTable
     * @return
     * @throws Ex
     */
    public Map<String,String> createTriggers(TriggerBean[] triggers, String tempTable) throws Ex;

    public Map<String,String> createMoreTiggers(TriggerBean[] triggers, String tempTable,String appName) throws Ex;

    public Map<String,String> updateTriggersByName(String triggerName,String tempTable,TriggerBean trigger) throws Ex;

    /**
     * 查询触发器
     */
    public Map<String,String> getTriggers(Table table) throws Ex;

    /**
     * 根据触发器名字删除触发器
     * @param table
     * @throws Ex
     */
    public void removeTriggerByName(Table table) throws Ex;
}
