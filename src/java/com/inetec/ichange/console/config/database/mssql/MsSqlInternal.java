package com.inetec.ichange.console.config.database.mssql;

import com.hzih.stp.utils.StringUtils;
import com.inetec.common.config.stp.EConfig;
import com.inetec.common.config.stp.nodes.Field;
import com.inetec.common.config.stp.nodes.Jdbc;
import com.inetec.common.config.stp.nodes.Table;
import com.inetec.common.exception.E;
import com.inetec.common.exception.Ex;
import com.inetec.common.i18n.Message;
import com.inetec.ichange.console.config.Constant;
import com.inetec.ichange.console.config.database.DBUtil;
import com.inetec.ichange.console.config.database.IInternal;
import com.inetec.ichange.console.config.utils.TriggerBean;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MsSqlInternal extends IInternal {
    public MsSqlInternal(Jdbc jdbc) throws Ex {
        super(jdbc);
        try {
            this.dbutil = new DBUtil(jdbc);
            script.load(this.getClass().getResourceAsStream("/dbscripts/mssql/init-mssql.properties"));
        } catch (IOException e) {
            throw new Ex().set(E.E_IOException, e, new Message("读取数据库配置脚本的时候出现问题"));
        }
    }

    /**
     * 创建触发器��
     *
     * @param triggers
     * @param tempTable
     * @throws com.inetec.common.exception.Ex
     */
    public void createTrigger(TriggerBean[] triggers, String tempTable) throws Ex {
//        logger.info("createTrigger()");
        Statement stmt = null;
//        String dbowner = jdbc.getDbOwner();
        try {
            stmt = conn.createStatement();
            for (int i = 0; i < triggers.length; i++) {
                TriggerBean trigger = triggers[i];
                //ȡ�øı加入批处理����
//                List columns = getFields(trigger.getTableName());
                String newpatterns = "";
                String oldpatterns = "";
                String pknames = "";
                String pktypes = "";
                String[] pks = trigger.getPkFields();
                for (int k = 0; k < pks.length; k++) {
                    Field field = getField(trigger.getTableName(), pks[k]);
                    if(k > 0){
                        newpatterns = newpatterns + "+','+";
                        oldpatterns = oldpatterns + "+','+";
                    }
                    //todo datetime
                    if(field.getJdbcType().equalsIgnoreCase("timestamp") || field.getDbType().equalsIgnoreCase("datetime")){
                        newpatterns = newpatterns + "CONVERT(varchar(30)" + ",inserted."+ field.getFieldName() + ",121)"; //121  yyyy -mm-dd hh :mm :ss [.fff ]
                        oldpatterns = oldpatterns + "CONVERT(varchar(30)" + ",deleted." + field.getFieldName() + ",121)";  //121  yyyy -mm-dd hh :mm :ss [.fff ]
                    }else if (field.getJdbcType().equalsIgnoreCase("varchar")){
                        newpatterns = newpatterns + "inserted." + field.getFieldName();
                        oldpatterns = oldpatterns + "deleted." + field.getFieldName();
                    }else{
                        newpatterns = newpatterns + "CONVERT(varchar(50)" +  ",inserted."+ field.getFieldName() + ")";
                        oldpatterns = oldpatterns + "CONVERT(varchar(50)" +  ",deleted."+ field.getFieldName() + ")";
                    }

                    if(pknames.length() == 0){
                        pknames = field.getFieldName();
                    }else{
                        pknames = pknames + "," + field.getFieldName();
                    }
                    if(pktypes.length() == 0){
                        pktypes = field.getDbType();
                    }else {
                        pktypes = pktypes + "," + field.getDbType();
                    }
                }

                //如果该表没有PK
                if (!pknames.equals("")) {
                    //创建sequence
                    int hashcode = Math.abs((trigger.getTableName() + tempTable).hashCode());
                    if (trigger.isMonitorDelete()) {
                        //加入批处理�
                        String triggerName = "ICHANGE_" + hashcode + "_D";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName));
                            if (!trig.next()) {
                                //�替换掉sql中的参数��
                                String delete = script.getProperty(Constant.S_PROP_DELETE_TRIGGER)
                                        .replaceAll("schema", m_schema) //�替换shema的名称�����
                                        .replaceAll("triggername", triggerName)  //�替换trigger的名称�����
                                        .replaceAll("table_name", trigger.getTableName()) //�替换表的名称����
                                        .replaceAll("temptable", tempTable)
                                        .replaceAll("pkpatterns", oldpatterns)
                                        .replaceAll("pknames", pknames)        //�替换PK的名称�����
                                        .replaceAll("pktypes", pktypes);//�替换Pk的类型������
                                //加入批处理
                                stmt.execute(delete);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                    } else {
                        //ɾ����
                        //加入批处理�
                        String triggerName = "ICHANGE_" + hashcode + "_D";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName));
                            if (trig.next()) {
                                //�替换掉sql中的参数��
                                String delete = script.getProperty(Constant.S_PROP_DROP_TRIGGER)
                                        .replaceAll("schema", m_schema)
                                        .replaceAll("triggername", triggerName);
                                //加入批处理
                                stmt.execute(delete);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                    }
                    if (trigger.isMonitorInsert()) {
                        String triggerName = "ICHANGE_" + hashcode + "_I";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName));
                            if (!trig.next()) {
                                //�替换掉sql中的参数��
                                String insert = script.getProperty(Constant.S_PROP_INSERT_TRIGGER)
                                        .replaceAll("schema", m_schema) //�替换shema的名称�����
                                        .replaceAll("triggername", triggerName)  //�替换trigger的名称�����
                                        .replaceAll("table_name", trigger.getTableName()) //�滻������
                                        .replaceAll("temptable", tempTable)
                                        .replaceAll("pkpatterns", newpatterns)
                                        .replaceAll("pknames", pknames)        //�替换PK的名称�����
                                        .replaceAll("pktypes", pktypes);//�替换Pk的类型������
                                //加入批处理
                                stmt.execute(insert);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                    } else {
                        //ɾ����
                        //加入批处理�
                        String triggerName = "ICHANGE_" + hashcode + "_I";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName));
                            if (trig.next()) {
                                //�替换掉sql中的参数��
                                String delete = script.getProperty(Constant.S_PROP_DROP_TRIGGER)
                                        .replaceAll("schema", m_schema)
                                        .replaceAll("triggername", triggerName);
                                //加入批处理
                                stmt.execute(delete);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                    }
                    if (trigger.isMonitorUpdate()) {
                        String triggerName = "ICHANGE_" + hashcode + "_U";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName));
                            if (!trig.next()) {
                                //�替换掉sql中的参数��
                                String update = script.getProperty(Constant.S_PROP_UPDATE_TRIGGER)
                                        .replaceAll("schema", m_schema) //�替换shema的名称�����
                                        .replaceAll("triggername", triggerName)  //�替换trigger的名称�����
                                        .replaceAll("table_name", trigger.getTableName()) //�滻������
                                        .replaceAll("temptable", tempTable)
                                        .replaceAll("pkpatterns", newpatterns)
                                        .replaceAll("pknames", pknames)        //�替换PK的名称�����
                                        .replaceAll("pktypes", pktypes);//�替换Pk的类型������
                                //加入批处理
                                stmt.execute(update);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                    } else {
                        //ɾ����
                        //加入批处理�
                        String triggerName = "ICHANGE_" + hashcode + "_U";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName));
                            if (trig.next()) {
                                //�替换掉sql中的参数��
                                String delete = script.getProperty(Constant.S_PROP_DROP_TRIGGER)
                                        .replaceAll("schema", m_schema)
                                        .replaceAll("triggername", triggerName);
                                //加入批处理
                                stmt.execute(delete);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                    }
                } else {
                    logger.info("数据表 " + trigger.getTableName() + " 中没有主键,不能创建触发器!");
                }
            }

            //加入批处理
//            stmt.executeBatch();
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new Ex().set(EConfig.E_SQLException, e, new Message("关闭statement时出现异常"));
                }
            }
        }
    }

    /**
     * ɾ����
     *
     * @param triggers
     * @param tempTable
     * @throws com.inetec.common.exception.Ex
     */
    public void removeTrigger(TriggerBean[] triggers, String tempTable) throws Ex {
//        logger.info("removeTrigger()");
        Statement stmt = null;
//        String dbowner = jdbc.getDbOwner();
        try {
            stmt = conn.createStatement();
            for (int i = 0; i < triggers.length; i++) {
                TriggerBean trigger = triggers[i];
                //创建sequence
                int hashcode = Math.abs((trigger.getTableName() + tempTable).hashCode());
                if (trigger.isMonitorDelete()) {
                    //加入批处理�
                    String triggerName = "ICHANGE_" + hashcode + "_D";
                    Statement trigstmt = null;
                    ResultSet trig = null;
                    try {
                        trigstmt = conn.createStatement();
                        trig = trigstmt.executeQuery(script
                                .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                .replaceAll("schema", m_schema)
                                .replaceAll("triggername", triggerName));
                        if (trig.next()) {
                            //�替换掉sql中的参数��
                            String delete = script.getProperty(Constant.S_PROP_DROP_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName);

                            //加入批处理
                            stmt.execute(delete);
                        }
                    } catch (SQLException e) {
                        throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                    } finally {
                        trig.close();
                        trigstmt.close();
                    }
                }
                if (trigger.isMonitorInsert()) {
                    String triggerName = "ICHANGE_" + hashcode + "_I";
                    Statement trigstmt = null;
                    ResultSet trig = null;
                    try {
                        trigstmt = conn.createStatement();
                        trig = trigstmt.executeQuery(script
                                .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                .replaceAll("schema", m_schema)
                                .replaceAll("triggername", triggerName));
                        if (trig.next()) {
                            //�替换掉sql中的参数��
                            String insert = script.getProperty(Constant.S_PROP_DROP_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName);
                            //加入批处理
                            stmt.execute(insert);
                        }
                    } catch (SQLException e) {
                        throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                    } finally {
                        trig.close();
                        trigstmt.close();
                    }
                }
                if (trigger.isMonitorUpdate()) {
                    String triggerName = "ICHANGE_" + hashcode + "_U";
                    Statement trigstmt = null;
                    ResultSet trig = null;
                    try {
                        trigstmt = conn.createStatement();
                        trig = trigstmt.executeQuery(script
                                .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                .replaceAll("schema", m_schema)
                                .replaceAll("triggername", triggerName));
                        if (trig.next()) {
                            //�替换掉sql中的参数��
                            String update = script.getProperty(Constant.S_PROP_DROP_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName);
                            //加入批处理
                            stmt.execute(update);
                        }
                    } catch (SQLException e) {
                        throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                    } finally {
                        trig.close();
                        trigstmt.close();
                    }
                }
            }
            //加入批处理
//            stmt.executeBatch();
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new Ex().set(EConfig.E_SQLException, e, new Message("关闭statement时出现异常"));
                }
            }
        }
    }

    /**
     *创建标记位
     *
     * @param tableNames
     * @throws com.inetec.common.exception.Ex
     */
    public void createFlag(String[] tableNames) throws Ex {
//        logger.info("createFlag()");
        Statement stmt = null;
        try {
//            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (int i = 0; i < tableNames.length; i++) {
                boolean isColumnExist = false;
                String[] fields = this.getFieldNames(tableNames[i]);
                for (int k = 0; k < fields.length; k++) {
                    //如果字段不存在,则添加
                    if (fields[k].equals(Constant.S_FLAG_COLUMN)) {
                        isColumnExist = true;
                    }
                }
                if (!isColumnExist) {
                    //创建Trigger，同时显式创建约束,格式为：表名+Flag的hashcode
                    stmt.execute("ALTER TABLE " + m_schema + "." + tableNames[i] + " ADD " + Constant.S_FLAG_COLUMN + " char(1) CONSTRAINT " + tableNames[i] + "_" + Constant.S_FLAG_COLUMN.hashCode() + " DEFAULT '0' NOT NULL");
                }
            }

        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("设置数据库可以自动提交时出现异常"));
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new Ex().set(EConfig.E_SQLException, e, new Message("关闭statement时出现异常"));
                }
            }
        }
    }

    /**
     * �移除标记位
     *
     * @param tableNames
     * @throws com.inetec.common.exception.Ex
     */
    public void removeFlag(String[] tableNames) throws Ex {
//        logger.info("removeFlag()");
        Statement stmt = null;
        try {
//            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (int i = 0; i < tableNames.length; i++) {
                String[] fields = this.getFieldNames(tableNames[i]);
                for (int k = 0; k < fields.length; k++) {
                    //�如果字段已经存在,则删除
                    if (fields[k].equals(Constant.S_FLAG_COLUMN)) {
                        //首先删除约束
                        stmt.execute("ALTER TABLE " + m_schema + "." + tableNames[i] + " DROP CONSTRAINT " + tableNames[i] + "_" + Constant.S_FLAG_COLUMN.hashCode());
                        //再删除Flag
                        stmt.execute("ALTER TABLE " + m_schema + "." + tableNames[i] + " DROP COLUMN " + Constant.S_FLAG_COLUMN);
                        break;
                    }
                }
            }
//            stmt.executeBatch();
//            try {
//                conn.commit();
//            } catch (SQLException e) {
//                conn.rollback();
//                throw new Ex().set(EConfig.E_SQLException, e, new Message("设置数据库事务提交失败,发生回滚"));
//            }
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("设置数据库可以自动提交时出现异常"));
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new Ex().set(EConfig.E_SQLException, e, new Message("关闭statement时出现异常"));
                }
            }
        }
    }

    @Override
    public void createFunction() throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteFunction() throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createMoreSequence(String appName) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeMoreSequence(String appName) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createTempTable(String temptable) throws Ex {
//        logger.info("createTempTable()");
        Statement tempstmt = null;
        ResultSet temp = null;
        try {
            //创建TODO表,如果不存在就创建
            tempstmt = conn.createStatement();
            temp = tempstmt.executeQuery(script
                    .getProperty(Constant.S_PROP_QUERY_TEMPTABLE)
                    .replaceAll("schema", m_schema)
                    .replaceAll("tablename", temptable));
            if (!temp.next()) {
                tempstmt.execute(script.getProperty(Constant.S_PROP_CREATE_TEMPTABLE).replaceAll("temptable", temptable).replaceAll("schema", m_schema));
            }
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            try {
                if (temp != null)
                    temp.close();
                if (tempstmt != null)
                    tempstmt.close();
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
            }
        }
    }

    public void removeTempTable(String temptable) throws Ex {
//        logger.info("removeTempTable()");
        Statement tempstmt = null;
        ResultSet temp = null;
        try {
            //删除TODO表,如果存在就删除
            tempstmt = conn.createStatement();
            temp = tempstmt.executeQuery(script
                    .getProperty(Constant.S_PROP_QUERY_TEMPTABLE)
                    .replaceAll("schema", m_schema)
                    .replaceAll("tablename", temptable));
            if (temp.next()) {
                tempstmt.execute(script.getProperty(Constant.S_PROP_DROP_TEMPTABLE).replaceAll("schema", m_schema).replaceAll("temptable", temptable));
            }
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            try {
                if (temp != null)
                    temp.close();
                if (tempstmt != null)
                    tempstmt.close();
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
            }
        }
    }

    @Override
    public Map<String, String> createTriggers(TriggerBean[] triggers, String tempTable) throws Ex {
        Statement stmt = null;
//        String dbowner = jdbc.getDbOwner();
        Map<String, String> map = new HashMap<String, String>();
        try {
            stmt = conn.createStatement();
            for (int i = 0; i < triggers.length; i++) {
                TriggerBean trigger = triggers[i];
                //ȡ�øı加入批处理����
//                List columns = getFields(trigger.getTableName());
                String newpatterns = "";
                String oldpatterns = "";
                String pknames = "";
                String pktypes = "";
                String[] pks = trigger.getPkFields();
                for (int k = 0; k < pks.length; k++) {
                    Field field = getField(trigger.getTableName(), pks[k]);
                    if(k > 0){
                        newpatterns = newpatterns + "+','+";
                        oldpatterns = oldpatterns + "+','+";
                    }
                    //todo datetime
                    if(field.getJdbcType().equalsIgnoreCase("timestamp") || field.getDbType().equalsIgnoreCase("datetime")){
                        newpatterns = newpatterns + "CONVERT(varchar(30)" + ",inserted."+ field.getFieldName() + ",121)"; //121  yyyy -mm-dd hh :mm :ss [.fff ]
                        oldpatterns = oldpatterns + "CONVERT(varchar(30)" + ",deleted." + field.getFieldName() + ",121)";  //121  yyyy -mm-dd hh :mm :ss [.fff ]
                    }else if (field.getJdbcType().equalsIgnoreCase("varchar")){
                        newpatterns = newpatterns + "inserted." + field.getFieldName();
                        oldpatterns = oldpatterns + "deleted." + field.getFieldName();
                    }else{
                        newpatterns = newpatterns + "CONVERT(varchar(50)" +  ",inserted."+ field.getFieldName() + ")";
                        oldpatterns = oldpatterns + "CONVERT(varchar(50)" +  ",deleted."+ field.getFieldName() + ")";
                    }

                    if(pknames.length() == 0){
                        pknames = field.getFieldName();
                    }else{
                        pknames = pknames + "," + field.getFieldName();
                    }
                    if(pktypes.length() == 0){
                        pktypes = field.getDbType();
                    }else {
                        pktypes = pktypes + "," + field.getDbType();
                    }
                }

                //如果该表没有PK
                if (!pknames.equals("")) {
                    //创建sequence
                    int hashcode = Math.abs((trigger.getTableName() + tempTable).hashCode());
                    if (trigger.isMonitorDelete()) {
                        //加入批处理�
                        String triggerName = "ICHANGE_" + hashcode + "_D";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName));
                            if (!trig.next()) {
                                //�替换掉sql中的参数��
                                String delete = script.getProperty(Constant.S_PROP_DELETE_TRIGGER)
                                        .replaceAll("schema", m_schema) //�替换shema的名称�����
                                        .replaceAll("triggername", triggerName)  //�替换trigger的名称�����
                                        .replaceAll("table_name", trigger.getTableName()) //��
                                        .replaceAll("temptable", tempTable)
                                        .replaceAll("pkpatterns", oldpatterns)
                                        .replaceAll("pknames", pknames)        //�替换PK的名称�����
                                        .replaceAll("pktypes", pktypes);//�替换Pk的类型������
                                //加入批处理
                                stmt.execute(delete);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                        map.put("delete",triggerName);
                    }
                    if (trigger.isMonitorInsert()) {
                        String triggerName = "ICHANGE_" + hashcode + "_I";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName));
                            if (!trig.next()) {
                                //�替换掉sql中的参数��
                                String insert = script.getProperty(Constant.S_PROP_INSERT_TRIGGER)
                                        .replaceAll("schema", m_schema) //�替换shema的名称�����
                                        .replaceAll("triggername", triggerName)  //�替换trigger的名称�����
                                        .replaceAll("table_name", trigger.getTableName()) //�滻������
                                        .replaceAll("temptable", tempTable)
                                        .replaceAll("pkpatterns", newpatterns)
                                        .replaceAll("pknames", pknames)        //�替换PK的名称�����
                                        .replaceAll("pktypes", pktypes);//�替换Pk的类型������
                                //加入批处理
                                stmt.execute(insert);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                        map.put("insert",triggerName);
                    } 
                    if (trigger.isMonitorUpdate()) {
                        String triggerName = "ICHANGE_" + hashcode + "_U";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName));
                            if (!trig.next()) {
                                //�替换掉sql中的参数��
                                String update = script.getProperty(Constant.S_PROP_UPDATE_TRIGGER)
                                        .replaceAll("schema", m_schema) //�替换shema的名称�����
                                        .replaceAll("triggername", triggerName)  //�替换trigger的名称�����
                                        .replaceAll("table_name", trigger.getTableName()) //�滻������
                                        .replaceAll("temptable", tempTable)
                                        .replaceAll("pkpatterns", newpatterns)
                                        .replaceAll("pknames", pknames)        //�替换PK的名称�����
                                        .replaceAll("pktypes", pktypes);//�替换Pk的类型������
                                //加入批处理
                                stmt.execute(update);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                        map.put("update",triggerName);
                    } 
                } else {
                     logger.warn("数据表 " + trigger.getTableName() + " 中没有主键,不能创建触发器!");
                }
            }

            //加入批处理
//            stmt.executeBatch();
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new Ex().set(EConfig.E_SQLException, e, new Message("关闭statement时出现异常"));
                }
            }
        }
        return map;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> createMoreTiggers(TriggerBean[] triggers, String tempTable,String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> updateTriggersByName(String triggerName, String tempTable, TriggerBean trigger) throws Ex {
        Statement stmt = null;
//        String dbowner = jdbc.getDbOwner();
        Map<String, String> map = new HashMap<String, String>();
        try {
            stmt = conn.createStatement();
            //ȡ�øı加入批处理����
//                List columns = getFields(trigger.getTableName());
            String newpatterns = "";
            String oldpatterns = "";
            String pknames = "";
            String pktypes = "";
            String[] pks = trigger.getPkFields();
            for (int k = 0; k < pks.length; k++) {
                Field field = getField(trigger.getTableName(), pks[k]);
                if(k > 0){
                    newpatterns = newpatterns + "+','+";
                    oldpatterns = oldpatterns + "+','+";
                }
                //todo datetime
                if(field.getJdbcType().equalsIgnoreCase("timestamp") || field.getDbType().equalsIgnoreCase("datetime")){
                    newpatterns = newpatterns + "CONVERT(varchar(30)" + ",inserted."+ field.getFieldName() + ",121)"; //121  yyyy -mm-dd hh :mm :ss [.fff ]
                    oldpatterns = oldpatterns + "CONVERT(varchar(30)" + ",deleted." + field.getFieldName() + ",121)";  //121  yyyy -mm-dd hh :mm :ss [.fff ]
                }else if (field.getJdbcType().equalsIgnoreCase("varchar")){
                    newpatterns = newpatterns + "inserted." + field.getFieldName();
                    oldpatterns = oldpatterns + "deleted." + field.getFieldName();
                }else{
                    newpatterns = newpatterns + "CONVERT(varchar(50)" +  ",inserted."+ field.getFieldName() + ")";
                    oldpatterns = oldpatterns + "CONVERT(varchar(50)" +  ",deleted."+ field.getFieldName() + ")";
                }

                if(pknames.length() == 0){
                    pknames = field.getFieldName();
                }else{
                    pknames = pknames + "," + field.getFieldName();
                }
                if(pktypes.length() == 0){
                    pktypes = field.getDbType();
                }else {
                    pktypes = pktypes + "," + field.getDbType();
                }
            }

            //如果该表没有PK
            if (!pknames.equals("")) {
                //创建sequence
                int hashcode = Math.abs((trigger.getTableName() + tempTable).hashCode());
                if (trigger.isMonitorDelete()) {
                    //加入批处理�
                    Statement trigstmt = null;
                    try {
                        trigstmt = conn.createStatement();
                        //�替换掉sql中的参数��
                        String delete = script.getProperty(Constant.S_PROP_DELETE_TRIGGER)
                                .replaceAll("schema", m_schema) //�替换shema的名称�����
                                .replaceAll("triggername", triggerName)  //�替换trigger的名称�����
                                .replaceAll("table_name", trigger.getTableName()) //��
                                .replaceAll("temptable", tempTable)
                                .replaceAll("pkpatterns", oldpatterns)
                                .replaceAll("pknames", pknames)        //�替换PK的名称�����
                                .replaceAll("pktypes", pktypes);//�替换Pk的类型������
                        //加入批处理
                        stmt.execute(delete);
                    } catch (SQLException e) {
                        throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                    } finally {
                        trigstmt.close();
                    }
                    map.put("delete", triggerName);
                }
                if (trigger.isMonitorInsert()) {
                    Statement trigstmt = null;
                    try {
                        trigstmt = conn.createStatement();
                        //�替换掉sql中的参数��
                        String insert = script.getProperty(Constant.S_PROP_INSERT_TRIGGER)
                                .replaceAll("schema", m_schema) //�替换shema的名称�����
                                .replaceAll("triggername", triggerName)  //�替换trigger的名称�����
                                .replaceAll("table_name", trigger.getTableName()) //�滻������
                                .replaceAll("temptable", tempTable)
                                .replaceAll("pkpatterns", newpatterns)
                                .replaceAll("pknames", pknames)        //�替换PK的名称�����
                                .replaceAll("pktypes", pktypes);//�替换Pk的类型������
                        //加入批处理
                        stmt.execute(insert);
                    } catch (SQLException e) {
                        throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                    } finally {
                        trigstmt.close();
                    }
                    map.put("insert", triggerName);
                }
                if (trigger.isMonitorUpdate()) {
                    Statement trigstmt = null;
                    try {
                        trigstmt = conn.createStatement();
                        //�替换掉sql中的参数��
                        String update = script.getProperty(Constant.S_PROP_UPDATE_TRIGGER)
                                .replaceAll("schema", m_schema) //�替换shema的名称�����
                                .replaceAll("triggername", triggerName)  //�替换trigger的名称�����
                                .replaceAll("table_name", trigger.getTableName()) //�滻������
                                .replaceAll("temptable", tempTable)
                                .replaceAll("pkpatterns", newpatterns)
                                .replaceAll("pknames", pknames)        //�替换PK的名称�����
                                .replaceAll("pktypes", pktypes);//�替换Pk的类型������
                        //加入批处理
                        stmt.execute(update);
                    } catch (SQLException e) {
                        throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                    } finally {
                        trigstmt.close();
                    }
                    map.put("update", triggerName);
                }
            } else {
                logger.warn("数据表 " + trigger.getTableName() + " 中没有主键,不能创建触发器!");
            }

            //加入批处理
//            stmt.executeBatch();
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new Ex().set(EConfig.E_SQLException, e, new Message("关闭statement时出现异常"));
                }
            }
        }
        return map;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getTriggers(Table table) throws Ex {
        Statement stmt = null;
        ResultSet rs = null;
        String dbowner = jdbc.getDbOwner().toUpperCase();
        Map<String,String> triggers = new HashMap<>();
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(script
                    .getProperty(Constant.S_PROP_QUERY_ALL_TRIGGER)
                    .replaceAll("schema", dbowner)
                    .replaceAll("tablename", table.getTableName().toUpperCase()));
            while (rs.next()){
                triggers.put(rs.getString(1),rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return triggers;
    }

    @Override
    public void removeTriggerByName(Table table) throws Ex {
        Statement stmt = null;
//        String dbowner = jdbc.getDbOwner();
        try {
            stmt = conn.createStatement();
                //创建sequence
                if (StringUtils.isNotBlank(table.getDeleteTrigger())) {
                    //加入批处理�
                    String triggerName = table.getDeleteTrigger();
                    Statement trigstmt = null;
                    ResultSet trig = null;
                    try {
                        trigstmt = conn.createStatement();
                        trig = trigstmt.executeQuery(script
                                .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                .replaceAll("schema", m_schema)
                                .replaceAll("triggername", triggerName));
                        if (trig.next()) {
                            //�替换掉sql中的参数��
                            String delete = script.getProperty(Constant.S_PROP_DROP_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName);

                            //加入批处理
                            stmt.execute(delete);
                        }
                    } catch (SQLException e) {
                        throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                    } finally {
                        trig.close();
                        trigstmt.close();
                    }
                }
                if (StringUtils.isNotBlank(table.getInsertTrigger())) {
                    String triggerName = table.getInsertTrigger();
                    Statement trigstmt = null;
                    ResultSet trig = null;
                    try {
                        trigstmt = conn.createStatement();
                        trig = trigstmt.executeQuery(script
                                .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                .replaceAll("schema", m_schema)
                                .replaceAll("triggername", triggerName));
                        if (trig.next()) {
                            //�替换掉sql中的参数��
                            String insert = script.getProperty(Constant.S_PROP_DROP_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName);
                            //加入批处理
                            stmt.execute(insert);
                        }
                    } catch (SQLException e) {
                        throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                    } finally {
                        trig.close();
                        trigstmt.close();
                    }
                }
                if (StringUtils.isNotBlank(table.getDeleteTrigger())) {
                    String triggerName = table.getUpdateTrigger();
                    Statement trigstmt = null;
                    ResultSet trig = null;
                    try {
                        trigstmt = conn.createStatement();
                        trig = trigstmt.executeQuery(script
                                .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                .replaceAll("schema", m_schema)
                                .replaceAll("triggername", triggerName));
                        if (trig.next()) {
                            //�替换掉sql中的参数��
                            String update = script.getProperty(Constant.S_PROP_DROP_TRIGGER)
                                    .replaceAll("schema", m_schema)
                                    .replaceAll("triggername", triggerName);
                            //加入批处理
                            stmt.execute(update);
                        }
                    } catch (SQLException e) {
                        throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                    } finally {
                        trig.close();
                        trigstmt.close();
                    }
                }
            //加入批处理
//            stmt.executeBatch();
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new Ex().set(EConfig.E_SQLException, e, new Message("关闭statement时出现异常"));
                }
            }
        }
    }
}
