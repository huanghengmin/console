package com.inetec.ichange.console.config.database.oracle;

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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * change by ztt
 */
public class OracleInternal extends IInternal {
	private Logger m_logger = Logger.getLogger(OracleInternal.class);
    public OracleInternal(Jdbc jdbc) throws Ex {
        super(jdbc);
        try {
            this.dbutil = new DBUtil(jdbc);
            this.m_schema = jdbc.getDbOwner().toUpperCase();
            script.load(this.getClass().getResourceAsStream("/dbscripts/oracle/init-oracle9i.properties"));
        } catch (IOException e) {
            throw new Ex().set(E.E_IOException, e, new Message("读取数据库配置脚本的时候出现问题!"));
        }
    }


    /**
     * todo todelete
     * create  inserttrigger , updatetrigger or deletetrigger  of one table
     * @param triggers
     * @param tempTable
     * @throws Ex
     */
    @Override
    public void createTrigger(TriggerBean[] triggers, String tempTable) throws Ex {

    }

    /**
     * delete  insert,update,delete trigger of one table
     * @param triggers
     * @param tempTable
     * @throws Ex
     */
    @Override
    public void removeTrigger(TriggerBean[] triggers, String tempTable) throws Ex {
        if (m_logger.isDebugEnabled())
            m_logger.debug("removeTrigger()");
        tempTable = tempTable.toUpperCase();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            for (int i = 0; i < triggers.length; i++) {
                TriggerBean trigger = triggers[i];
                //创建sequence
                int hashcode = Math.abs((trigger.getTableName() + tempTable).hashCode());
                if (trigger.isMonitorDelete()) {
                    //加入批处理,取表的hashCode作为名称
                    String triggerName = "ICHANGE_" + hashcode + "_D";
                    Statement trigstmt = null;
                    ResultSet trig = null;
                    try {
                        trigstmt = conn.createStatement();
                        trig = trigstmt.executeQuery(script
                                .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                .replaceAll("schema", m_schema)
                                .replaceAll("triggername", triggerName.toUpperCase()));
                        if (trig.next()) {
                            //替换掉sql中的参数��
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
                                .replaceAll("triggername", triggerName.toUpperCase()));
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
                                .replaceAll("triggername", triggerName.toUpperCase()));
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


    @Override
    public void createFlag(String[] tableNames) throws Ex {
        if (m_logger.isDebugEnabled())
            m_logger.debug("createFlag()");
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            for (int i = 0; i < tableNames.length; i++) {
                boolean isColumnExist = false;
                String[] fields = this.getFieldNames(tableNames[i]);
                for (int k = 0; k < fields.length; k++) {
                    if (fields[k].equals(Constant.S_FLAG_COLUMN)) {
                        isColumnExist = true;
                    }
                }
                if (!isColumnExist) {
                    stmt.execute("ALTER TABLE " + m_schema + "." + tableNames[i] + " ADD " + Constant.S_FLAG_COLUMN + " char(1) DEFAULT '0'");
                }
            }
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("加入批处理失败"));
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("数据库出现异常"));
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
     * for flag process
     * @param tableNames
     * @throws Ex
     */
    @Override
    public void removeFlag(String[] tableNames) throws Ex {

        if (m_logger.isDebugEnabled())
            m_logger.debug("removeFlag()");
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            for (int i = 0; i < tableNames.length; i++) {
                boolean isColumnExist = false;
                String[] fields = this.getFieldNames(tableNames[i]);
                for (int k = 0; k < fields.length; k++) {
                    if (fields[k].equals(Constant.S_FLAG_COLUMN)) {
                        stmt.execute("ALTER TABLE " + m_schema + "." + tableNames[i] + " DROP COLUMN " + Constant.S_FLAG_COLUMN);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("加入批处理失败"));
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("数据库出现异常"));
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
     * for addtrigger (more temps)
     * used in addtrigger
     * @throws Ex
     */
    @Override
    public void createFunction() throws Ex {
        Statement functionstmt = null;
        try {
            functionstmt = conn.createStatement();
            functionstmt.execute(script
                    .getProperty(Constant.S_PROC_CREATE_FUNCTION_VAR2ARR)
                    .replaceAll("schema", m_schema)
                    .replaceAll("dbname",jdbc.getJdbcName())
            );
            functionstmt.execute(script
                    .getProperty(Constant.S_PROC_CREATE_FUNCTION_GETTABLENAME)
                    .replaceAll("schema", m_schema)
                    .replaceAll("dbname", jdbc.getJdbcName())
            );
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            try {
                if (functionstmt != null)
                    functionstmt.close();
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
            }
        }
    }

    @Override
    public void deleteFunction() throws Ex {
        Statement functionstmt = null;
        ResultSet function_var2arr = null,function_gettablename = null;
        try {
            functionstmt = conn.createStatement();
            function_var2arr = functionstmt.executeQuery(script
                    .getProperty(Constant.S_PROC_QUERY_FUNCTION)
                    .replaceAll("fun_name", jdbc.getJdbcName().toUpperCase() + "_STRING_ARRAY")
            );
            if (function_var2arr.next()) {
                functionstmt.execute(script.getProperty(Constant.S_PROC_DELTE_FUNCTION_VAR2ARR).replaceAll("schema", m_schema).replaceAll("dbname",jdbc.getJdbcName().toUpperCase()));
            }
            function_gettablename = functionstmt.executeQuery(script
                    .getProperty(Constant.S_PROC_QUERY_FUNCTION)
                    .replaceAll("fun_name", jdbc.getJdbcName().toUpperCase() + "_GET_TEMPTABLE_NAME")
            );
            if (function_gettablename.next()) {
                functionstmt.execute(script.getProperty(Constant.S_PROC_DELETE_FUNCTION_GETTABLENAME).replaceAll("schema", m_schema).replaceAll("dbname",jdbc.getJdbcName().toUpperCase()));
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            try {
                if (function_var2arr != null)
                    function_var2arr.close();
                if (function_gettablename != null)
                    function_gettablename.close();
                if (functionstmt != null)
                    functionstmt.close();
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
            }
        }
    }

    /**
     * for addtrigger (more temps)
     * used in addtrigger
     * @param appName
     * @throws Ex
     */
    @Override
    public void createMoreSequence(String appName) throws Ex {
        Statement seqstmt = null;
        ResultSet seq = null;
        try {
            seqstmt = conn.createStatement();
            seq = seqstmt.executeQuery(script
                    .getProperty(Constant.S_PROC_QUERY_MORE_SEQUENCE)
                    .replaceAll("schema", m_schema)
                    .replaceAll("seq_name", ("INETEC_" + appName).toUpperCase())
            );
            if (!seq.next()) {
                seqstmt.execute(script.getProperty(Constant.S_PROC_CREATE_MORE_SEQUENCE).replaceAll("schema", m_schema).replaceAll("app", appName.toUpperCase()));
            }
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            try {
                if (seq != null)
                    seq.close();
                if (seqstmt != null)
                    seqstmt.close();
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
            }
        }
    }

    /**
     * for addtrigger (more temps)
     * used in addtrigger
     * @param appName
     * @throws Ex
     */
    @Override
    public void removeMoreSequence(String appName) throws Ex {
        logger.info("removeSequence()");
        Statement seqstmt = null;
        ResultSet seq = null;
        try {
            //加入批处理
            seqstmt = conn.createStatement();
            seq = seqstmt.executeQuery(script
                    .getProperty(Constant.S_PROP_QUERY_SEQUENCE)
                    .replaceAll("schema", m_schema));
            if (seq.next()) {
                seqstmt.execute(script.getProperty(Constant.S_PROP_DROP_SEQUENCE).replaceAll("schema", m_schema));
            }
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            try {
                if (seq != null)
                    seq.close();
                if (seqstmt != null)
                    seqstmt.close();
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
            }
        }
    }

    @Override
    public void createTempTable(String temptable) throws Ex {
        if (m_logger.isDebugEnabled())
            m_logger.debug("createTempTable()");
        temptable = temptable.toUpperCase();
        String[] tempTables = temptable.trim().split(",");
        Statement tempstmt = null;
        ResultSet temp = null;
        try {
            tempstmt = conn.createStatement();
            for(int i = 0 ;i< tempTables.length ; i++){
                temp = tempstmt.executeQuery(script
                        .getProperty(Constant.S_PROP_QUERY_TEMPTABLE)
                        .replaceAll("schema", m_schema)
                        .replaceAll("tablename", tempTables[i].toUpperCase()));
                if (!temp.next()) {
                    tempstmt.execute(script.getProperty(Constant.S_PROP_CREATE_TEMPTABLE).replaceAll("temptable", tempTables[i].toUpperCase()).replaceAll("schema", m_schema));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    public void removeTempTable(String temptable) throws Ex {
        if (m_logger.isDebugEnabled())
            m_logger.debug("removeTempTable()");
        temptable = temptable.toUpperCase();
        String[] tempTables = temptable.trim().split(",");
        Statement tempstmt = null;
        ResultSet temp = null;
        try {
            tempstmt = conn.createStatement();
            for(int i = 0 ;i< tempTables.length ; i++){
                temp = tempstmt.executeQuery(script
                        .getProperty(Constant.S_PROP_QUERY_TEMPTABLE)
                        .replaceAll("schema", m_schema)
                        .replaceAll("tablename", tempTables[i].toUpperCase()));
                if (temp.next()) {
                    tempstmt.execute(script.getProperty(Constant.S_PROP_DROP_TEMPTABLE).replaceAll("schema", m_schema).replaceAll("temptable", tempTables[i].toUpperCase()));
                }
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

    /**
     * create  inserttrigger , updatetrigger or deletetrigger  of one table
     * used in trigger
     * @param triggers
     * @param tempTable
     * @throws Ex
     */
    @Override
    public Map<String, String> createTriggers(TriggerBean[] triggers, String tempTable) throws Ex {
        if (m_logger.isDebugEnabled())
            m_logger.debug("createTriggers()");
        Map<String, String> map = new HashMap<String, String>();
        tempTable = tempTable.toUpperCase();
        Statement stmt = null;
        String dbowner = jdbc.getDbOwner().toUpperCase();
        try {
            createSequence();
        } catch (Ex ex) {
            //okay;
        }
        try {
            stmt = conn.createStatement();
            for (int i = 0; i < triggers.length; i++) {
                TriggerBean trigger = triggers[i];
                //取得改表的主键及其类型
                List columns = getFields(trigger.getTableName());
                String newpatterns = "";   //for add,update trigger
                String oldpatterns = "";   //for delete trigger
                String pknames = "";
                String pktypes = "";
                String[] pks = trigger.getPkFields();
                for (int k = 0; k < pks.length; k++) {
                    Field field = getField(trigger.getTableName(), pks[k]);
                    if(k > 0){
                        newpatterns = newpatterns + "||','||";
                        oldpatterns = oldpatterns + "||','||";
                    }
                    if(field.getJdbcType().equalsIgnoreCase("date") || field.getDbType().equalsIgnoreCase("date")){
                        newpatterns = newpatterns + "TO_CHAR(:new." + field.getFieldName() + ",'YYYY-MM-DD HH24:MI:SS')";
                        oldpatterns = oldpatterns + "TO_CHAR(:old." + field.getFieldName() + ",'YYYY-MM-DD HH24:MI:SS')";
                    }else if(field.getJdbcType().equalsIgnoreCase("timestamp")){
                        newpatterns = newpatterns + "TO_CHAR(:new." + field.getFieldName() + ",'YYYY-MM-DD HH24:MI:SS.FF')";
                        oldpatterns = oldpatterns + "TO_CHAR(:old." + field.getFieldName() + ",'YYYY-MM-DD HH24:MI:SS.FF')";
                    }else{
                        newpatterns = newpatterns + "TO_CHAR(:new." + field.getFieldName() + ")";
                        oldpatterns = oldpatterns + "TO_CHAR(:old." + field.getFieldName() + ")";
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

                if (!pknames.equals("")) {

                    //创建sequence
                    int hashcode = Math.abs((trigger.getTableName() + tempTable).hashCode());
                    if (trigger.isMonitorDelete()) {
                        //触发器的名称,取表的hashCode作为名称
                        String triggerName = "ICHANGE_" + hashcode + "_D";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", dbowner)
                                    .replaceAll("triggername", triggerName.toUpperCase()));
                            if (!trig.next()) {
                                //替换掉sql中的参数
                                String delete = script.getProperty(Constant.S_PROP_DELETE_TRIGGER)
                                        .replaceAll("schema", dbowner) //替换shema的名称
                                        .replaceAll("triggername", triggerName)  //替换trigger的名称
                                        .replaceAll("table_name", trigger.getTableName()) //替换表的名称
                                        .replaceAll("temptable", tempTable)
                                        .replaceAll("pkpatterns", oldpatterns)
                                        .replaceAll("pknames", pknames)        //替换PK的名称
                                        .replaceAll("pktypes", pktypes);//替换Pk的类型
                                //加入批处理
                                stmt.addBatch(delete);
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
                                    .replaceAll("schema", dbowner)
                                    .replaceAll("triggername", triggerName));
                            if (!trig.next()) {
                                //替换掉sql中的参数
                                String insert = script.getProperty(Constant.S_PROP_INSERT_TRIGGER)
                                        .replaceAll("schema", dbowner) //替换shema的名称
                                        .replaceAll("triggername", triggerName)  //替换trigger的名称
                                        .replaceAll("table_name", trigger.getTableName()) //替换表的名称
                                        .replaceAll("temptable", tempTable)
                                        .replaceAll("pkpatterns", newpatterns)
                                        .replaceAll("pknames", pknames)        //替换PK的名称
                                        .replaceAll("pktypes", pktypes);//替换Pk的类型
                                //加入批处理
                                stmt.addBatch(insert);
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
                                    .replaceAll("schema", dbowner)
                                    .replaceAll("triggername", triggerName));
                            if (!trig.next()) {
                                //替换掉sql中的参数
                                String update = script.getProperty(Constant.S_PROP_UPDATE_TRIGGER)
                                        .replaceAll("schema", dbowner) //替换shema的名称
                                        .replaceAll("triggername", triggerName)  //替换trigger的名称
                                        .replaceAll("table_name", trigger.getTableName()) //替换表的名称
                                        .replaceAll("temptable", tempTable)
                                        .replaceAll("pkpatterns", newpatterns)
                                        .replaceAll("pknames", pknames)        //替换PK的名称
                                        .replaceAll("pktypes", pktypes);//替换Pk的类型
                                //加入批处理
                                stmt.addBatch(update);
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
                    m_logger.warn("数据表 " + trigger.getTableName() + " 中没有主键,不能创建触发器!");
                }
            }
            //批量处理
            stmt.executeBatch();
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
        return map;
    }

    /**
     * create insert trigger of one table
     * used in  addtrigger
     * @param triggers
     * @param tempTable
     * @param appName
     * @return
     * @throws Ex
     */
    @Override
    public Map<String, String> createMoreTiggers(TriggerBean[] triggers, String tempTable, String appName) throws Ex {
        if (m_logger.isDebugEnabled())
            m_logger.debug("createMoreTiggers()");
        Map<String, String> map = new HashMap<String, String>();
        tempTable = tempTable.toUpperCase();
        Statement stmt = null;
        String dbowner = jdbc.getDbOwner().toUpperCase();
        try {
            createFunction();
            createMoreSequence(appName);
        } catch (Ex ex) {
            //okay;
        }
        try {
            stmt = conn.createStatement();
            for (int i = 0; i < triggers.length; i++) {
                TriggerBean trigger = triggers[i];
                //取得改表的主键及其类型
                List columns = getFields(trigger.getTableName());
                String newpatterns = "";   //for add,update trigger
                String oldpatterns = "";   //for delete trigger
                String pknames = "";
                String pktypes = "";
                String[] pks = trigger.getPkFields();
                for (int k = 0; k < pks.length; k++) {
                    Field field = getField(trigger.getTableName(), pks[k]);
                    if(k > 0){
                        newpatterns = newpatterns + "||','||";
                        oldpatterns = oldpatterns + "||','||";
                    }
                    if(field.getJdbcType().equalsIgnoreCase("timestamp")){
                        newpatterns = newpatterns + "TO_CHAR(:new." + field.getFieldName() + ",'YYYY-MM-DD HH24:MI:SS.FF')";
                        oldpatterns = oldpatterns + "TO_CHAR(:old." + field.getFieldName() + ",'YYYY-MM-DD HH24:MI:SS.FF')";
                    }else if(field.getJdbcType().equalsIgnoreCase("date") || field.getDbType().equalsIgnoreCase("date")){
                        newpatterns = newpatterns + "TO_CHAR(:new." + field.getFieldName() + ",'YYYY-MM-DD HH24:MI:SS')";
                        oldpatterns = oldpatterns + "TO_CHAR(:old." + field.getFieldName() + ",'YYYY-MM-DD HH24:MI:SS')";
                    }else{
                        newpatterns = newpatterns + "TO_CHAR(:new." + field.getFieldName() + ")";
                        oldpatterns = oldpatterns + "TO_CHAR(:old." + field.getFieldName() + ")";
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

                if (!pknames.equals("")) {
                    //创建trigger
                    int hashcode = Math.abs((trigger.getTableName() + tempTable).hashCode());
                    if (trigger.isMonitorInsert()) {
                        String triggerName = "ICHANGE_" + hashcode + "_I";
                        Statement trigstmt = null;
                        ResultSet trig = null;
                        try {
                            trigstmt = conn.createStatement();
                            trig = trigstmt.executeQuery(script
                                    .getProperty(Constant.S_PROP_QUERY_TRIGGER)
                                    .replaceAll("schema", dbowner)
                                    .replaceAll("triggername", triggerName));
                            if (!trig.next()) {
                                //替换掉sql中的参数
                                String insert = script.getProperty(Constant.S_PROC_INSERT_MORE_TRIGGER)
                                        .replaceAll("schema", dbowner) //替换shema的名称
                                        .replaceAll("triggername", triggerName)  //替换trigger的名称
                                        .replaceAll("table_name_", trigger.getTableName()) //替换表的名称
                                        .replaceAll("temp_table_str", tempTable)
                                        .replaceAll("temp_table_num",String.valueOf(tempTable.split(",").length))
                                        .replaceAll("sequence","INETEC_" + appName)
                                        .replaceAll("dbname_str",jdbc.getJdbcName())
                                        .replaceAll("pkpatterns", newpatterns)
                                        .replaceAll("pknames", pknames)        //替换PK的名称
                                        .replaceAll("pktypes", pktypes);//替换Pk的类型
                                //加入批处理
                                stmt.addBatch(insert);
                            }
                        } catch (SQLException e) {
                            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
                        } finally {
                            trig.close();
                            trigstmt.close();
                        }
                        map.put("insert",triggerName);
                    }
                } else {
                    m_logger.warn("数据表 " + trigger.getTableName() + " 中没有主键,不能创建触发器!");
                }
            }
            //批量处理
            stmt.executeBatch();
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
        return map;
    }

    /**
     * todo todelte
     * @param triggerName
     * @param tempTable
     * @param trigger
     * @return
     * @throws Ex
     */
    @Override
    public Map<String, String> updateTriggersByName(String triggerName, String tempTable, TriggerBean trigger) throws Ex {
        return null;
    }

    @Override
    public Map<String, String> getTriggers(Table table) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeTriggerByName(Table table) throws Ex {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            if (StringUtils.isNotBlank(table.getDeleteTrigger())) {
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
            if (StringUtils.isNotBlank(table.getUpdateTrigger())) {
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

    /**
     * one db for one sequence
     * for add,update,delete trigger
     * @throws Ex
     */
    public void createSequence() throws Ex {
//        logger.info("createSequence()");
        Statement seqstmt = null;
        ResultSet seq = null;
        try {
            seqstmt = conn.createStatement();
            seq = seqstmt.executeQuery(script
                    .getProperty(Constant.S_PROP_QUERY_SEQUENCE)
                    .replaceAll("schema", m_schema));
            if (!seq.next()) {
                seqstmt.execute(script.getProperty(Constant.S_PROP_CREATE_SEQUENCE).replaceAll("schema", m_schema));
            }
        } catch (SQLException e) {
            throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
        } finally {
            try {
                if (seq != null)
                    seq.close();
                if (seqstmt != null)
                    seqstmt.close();
            } catch (SQLException e) {
                throw new Ex().set(EConfig.E_SQLException, e, new Message("数据出现异常"));
            }
        }
    }
}
