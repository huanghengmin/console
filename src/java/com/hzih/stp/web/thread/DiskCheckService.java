package com.hzih.stp.web.thread;

import com.hzih.stp.constant.AppConstant;
import com.hzih.stp.domain.EquipmentLog;
import com.hzih.stp.utils.ServiceResponse;
import com.hzih.stp.utils.ServiceUtil;
import com.hzih.stp.utils.StringUtils;
import com.hzih.stp.web.SiteContext;
import com.hzih.stp.web.action.config.AlertManagerAction;
import com.hzih.stp.web.servlet.SiteContextLoaderServlet;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by 钱晓盼 on 15-5-11.
 */
public class DiskCheckService extends Thread {
    final static Logger logger = Logger.getLogger(DiskCheckService.class);

    private boolean isSend = false;

    @Override
    public void run() {
        //TODO check and send mail
        while (true){
            SAXReader reader = new SAXReader();
            Document document = null;
            int idx = 1000 * 60 * 10;
            try {
                document = reader.read(new File(System.getProperty("ichange.home") + AppConstant.XML_ALERT_CONFIG_PATH));
                Node tempNode = document.selectSingleNode("//config/mailconfig/mailfrequency");
                idx = 1000 * 60 * Integer.valueOf(tempNode.getText());
            } catch (Exception e) {
            }
            try{
                check();
                log();
            } catch (Exception e){
                logger.error("",e);
            } finally {
                try {
                    Thread.sleep(idx);
                } catch (InterruptedException e) {
                }
            }
        }

    }

    private void log(){
        String[][] params = new String[][] {
					{ "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" }
        };
		ServiceResponse serviceResponse = ServiceUtil.callStp("AlertAction_logE.action", params);
		if (serviceResponse != null && serviceResponse.getCode() == 200){
            logger.info("入库成功");
        }


    }

    public void check() {
        String diskMsg = "";
        try{
            SAXReader reader = new SAXReader();
            String fileName = SiteContext.getInstance().contextRealPath
                    + AppConstant.XML_DB_CONFIG_PATH;
            Document doc = reader.read(new File(fileName));
            int dbUsed = Integer.parseInt(doc.selectSingleNode("//config/maindb/dbused").getText()); //审计库容量告警%值
            int diskUsed = getDataBaseAtDisk(doc);            //审计库所在磁盘使用容量(%)
            if(diskUsed >= dbUsed){
//                diskMsg = "审计库总容量达到警戒容量";
                if(!isSend) {
                    isSend = sendMail();

                }
//                EquipmentLog equipmentLog = new EquipmentLog();
//                equipmentLog.setLevel("WARN");
//                equipmentLog.setLogInfo(diskMsg);
//                equipmentLog.setEquipmentName("stp");
//                equipmentLog.setLogTime(date);
//                equipmentLog.setLinkName("B/SAccess");
//                logService.newLog(equipmentLog);

            }
        } catch (Exception e) {

        }
    }

    private boolean sendMail(){
//        String[][] params = new String[][] {
//                    { "SERVICEREQUESTTYPE", "SERVICECONTROLEMAIL" },
//					{ "Command", "1" }  // 审计库总容量达到警戒容量
//        };
//		ServiceResponse serviceResponse = ServiceUtil.callService(params);
//		if (serviceResponse != null && serviceResponse.getCode() == 200){
//            logger.info("审计库总容量达到警戒容量,成功通知邮件服务发送");
//        }

        String diskMsg = "审计库所在磁盘空间充足";
        java.util.Date date = new java.util.Date();
        diskMsg = "审计库总容量达到警戒容量";
        String p = "内网目标端";
        if(!StringUtils.getPrivated()){  // 源端 -- 非可信服务
            p = "外网源端";
        }
        String msg = "{level:'email',title:'"+p+"不正常的警告'," +
                "text:'" +
                "审计库总容量达到警戒容量," +
                "具体的值可以通过[配置管理]的[审计库配置]查看',fileName:''}";

        SiteContextLoaderServlet.emailService.offer(msg);
        return true;
    }


    private int getDataBaseAtDisk(Document doc) {
        String ip = doc.selectSingleNode("//config/maindb/dbip").getText();
        String port = doc.selectSingleNode("//config/maindb/dbport").getText();
        String dbName = "information_schema";
        String userName = doc.selectSingleNode("//config/maindb/username").getText();
        String password = doc.selectSingleNode("//config/maindb/password").getText();
        String dataDir = null;
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ip
                    + ":" + port + "/" + dbName
                    + "?useUnicode=true&characterEncoding=utf-8";
            conn = DriverManager.getConnection(url, userName, password);
            stat = conn.createStatement();
            rs = stat.executeQuery("select VARIABLE_VALUE from `GLOBAL_VARIABLES` where VARIABLE_NAME = 'datadir';");
            if (rs != null && rs.next()) {
                dataDir = rs.getString(1);
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                if (null != rs) {
                    rs.close();
                    rs = null;
                }
                if (null != stat) {
                    stat.close();
                    stat = null;
                }
                if (null != conn) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {

            }
        }
        int used = 100;
        Proc proc;
        OSInfo osinfo = OSInfo.getOSInfo();
        if (osinfo.isWin()) {
        }
        if (osinfo.isLinux()) {
            proc = new Proc();
            proc.exec("df "+dataDir);
            String result = proc.getOutput();
            String[] lines = result.split("\n");
            String[] str = lines[1].split("\\s");
            for (int i = 0;i<str.length;i++){
                if(str[i].endsWith("%") ){
                    used = Integer.parseInt(str[i].split("%")[0]);
                }
            }
        }
        return used;
    }
}
