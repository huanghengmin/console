package com.hzih.stp.utils.udp;


import cn.collin.commons.utils.DateUtils;
import com.hzih.stp.utils.*;
import com.hzih.stp.utils.code.RequestMessage;
import com.inetec.common.config.stp.ConfigParser;
import com.inetec.common.config.stp.nodes.*;
import com.inetec.common.exception.Ex;
import com.inetec.common.security.FileMd5;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-12-9
 * Time: 下午6:21
 * To change this template use File | Settings | File Templates.
 */
public class UdpServerHandler extends IoHandlerAdapter {

    private static final Logger logger = Logger.getLogger(UdpServerHandler.class);
    private String appName;
    private String appType;
    private long times;
    private String dir;
    private TargetFile targetFile;
    private UploadService uploadService;



    public void init(String dir, TargetFile targetFile) {
        this.dir = dir;
        this.targetFile = targetFile;
        if(targetFile != null) {

        }

    }

    /**
     * 抛出异常触发的事件
     * @param session
     * @param cause
     * @throws Exception
     */
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        session.close(true);
    }

    /**
     * 连接关闭触发的事件
     * @param session
     * @throws Exception
     */
    public void sessionClosed(IoSession session) throws Exception {
//        logger.info("Session closed...");
    }

    /**
     * 建立连接触发的事件
     * @param session
     * @throws Exception
     */
    public void sessionCreated(IoSession session) throws Exception {
//        logger.info("Session created...");
    }

    /**
     * 声明这里message必须为IoBuffer类型
     * @param session
     * @param message
     * @throws Exception
     */
    public void messageReceived(IoSession session, Object message) {
        if(message instanceof RequestMessage) {
            RequestMessage request = (RequestMessage) message;
            dbReceive(request);
        }
    }

    private void dbReceive(RequestMessage requestMessage) {
        byte[] nameBuff = requestMessage.getNameBody().toByteArray();
        byte[] bodyBuff = requestMessage.getMsgBody().toByteArray();
        String tempFileName = new String(nameBuff);//临时文件名
//        doBeforeReceive(tempFileName);
        String rFilePath;
        if(requestMessage.getFileCode()==0){
            rFilePath = dir + "/" + tempFileName;
        } else {
            rFilePath = dir + "/" + "config.xml_tmp";
        }
        File file = new File(rFilePath);
        if(requestMessage.getProtocolType() == RequestMessage.ProtocolTypeOnly) { //单独一个包
            write(requestMessage, file, bodyBuff, false, true);
        } else if(requestMessage.getProtocolType() == RequestMessage.ProtocolTypeStart) {
            write(requestMessage, file, bodyBuff, false, false);
        } else if(requestMessage.getProtocolType() == RequestMessage.ProtocolType) {  //多个包(除最后一个包);
            write(requestMessage, file, bodyBuff, true, false);
        } else if(requestMessage.getProtocolType() == RequestMessage.ProtocolTypeEnd) { //除最后一个包
            write(requestMessage, file, bodyBuff, true, true);
        }
    }

    /**
     * 最后一个包或者单独一个包(写数据库临时文件)
     * @param requestMessage
     * @param file
     * @param bodyBuff
     * @param isPoint  追加
     * @param isEnd  写文件结束
     */
    private void write(RequestMessage requestMessage,File file, byte[] bodyBuff, boolean isPoint, boolean isEnd) {
        FileOutputStream out = null;
        try {
            if(isPoint){
                out = new FileOutputStream(file,true);
            } else {
                out = new FileOutputStream(file);
            }
            out.write(bodyBuff);
            out.flush();
        } catch (IOException e) {
            logger.error("UdpServerHandler写入临时文件错误",e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("I/O关闭异常",e);
            }
        }
        if(isEnd){
            try {
                String md5 = FileMd5.getFileMD5String(file);
                if(md5.equals(new String(requestMessage.getMd5Body().toByteArray()))) {
                    logger.info("完整性校验通过");
                    if(requestMessage.getFileCode()==0){
//                        if(targetFile != null) {
//                            smbUpload(file,targetFile);
//                        }
                    } else if(requestMessage.getFileCode()==1){
                        processConfig(file.getPath());
                    }
                } else {
                    logger.info("完整性校验失败");
                }
            } catch (Exception e) {
                logger.error("计算MD5错误",e);
            }
        }
    }

    private void smbUpload(File file, TargetFile targetFile) {
        NtlmPasswordAuthentication npa =
                    new NtlmPasswordAuthentication(targetFile.getServerAddress(),
                            targetFile.getUserName(),targetFile.getPassword());
        String fileName = file.getName();
        String url = "smb://" + targetFile.getServerAddress() + ":" + targetFile.getPort() + targetFile.getDir();
        if(url.endsWith("/")) {
            url += fileName;
        } else {
            url += "/" + fileName;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            SmbFile smbFile = new SmbFile(url,npa);
            out = new SmbFileOutputStream(smbFile);
            in = new FileInputStream(file);
            FileUtil.copy(in,out);
        } catch (Exception e) {
            logger.error("上传失败",e);
        } finally {
            if(out!=null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if(in!=null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            file.delete();
        }
    }


    private void processConfig(String tempFileFullName) {
        logger.info("处理系统源端发送的配置文件 开始...");
//        String dir = System.getProperty("ichange.home") + "/temp/" + ServiceUtil.CONFIGFLAG;
        File tempFile = new File(tempFileFullName);
        if(!tempFile.exists()) {
            return;
        }
        try {
//            FileUtils.copy(tempFile, ExternalXml);
            updateConfig(tempFileFullName, StringContext.EXTERNALXML);
            logger.debug("拷贝完成.");
            Configuration config = new Configuration(StringContext.EXTERNALXML);
            logger.debug("加载配置文件..");
//            config.updatePrivated();
            logger.debug("改变配置文件privated 完成.");
            List<Type> types = config.readTypes(Type.s_app_proxy);
            logger.debug("获取代理应用."+types==null?0:types.size());
            if(types!=null&&types.size()>0){
                config = new Configuration(StringContext.INTERNALXML);
                config.updateProxySocketPort(types);
                logger.debug("改变代理配置...");
            }
//            Service.sysLogSendService.offer(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ",目标端接收到配置文件");
//            logger.debug("发送syslog完成.");
        } catch (Ex ex) {
            logger.error("处理系统源端发送的配置文件错误",ex);
        }
        tempFile.delete();
        logger.info("处理系统源端发送的配置文件 结束...");
    }

    public void updateConfig(String tempFileFullName, String externalXml) {
        try {
            ConfigParser sConfig = new ConfigParser(tempFileFullName);
            IChange ichange = sConfig.getRoot();
            Jdbc[] jdbcArrayS = ichange.getAllJdbcs();
            Type[] typeArrayS = ichange.getAllTypes();
            Configuration tConfig = new Configuration(externalXml);
            List<Jdbc> jdbcListT = tConfig.getJdbcs();
            List<Type> typeListT = tConfig.getTypes();
            for(Jdbc jdbc : jdbcListT) {
                tConfig.removeJdbc(jdbc.getJdbcName());
            }
            for (Type type : typeListT) {
                tConfig.removeType(type.getTypeName());
            }
            tConfig.saveExternal();
            for( int i = 0; i < jdbcArrayS.length ; i ++) {
                Jdbc jdbc = jdbcArrayS[i];
                tConfig.addJdbcConfig(jdbc);
            }
            String appNames = "";
            for( int i = 0; i < typeArrayS.length ; i ++) {
                Type type = typeArrayS[i];
                String appName = type.getTypeName();

                if(i < typeArrayS.length -1){
                    appNames += appName + ",";
                } else {
                    appNames += appName;
                }

                String appType = type.getAppType();
                Plugin plugin = type.getPlugin();
                tConfig.addType(type, appType);
                tConfig.addSourcePlugin(plugin,appName,appType);

                if(Type.s_app_db.equals(appType)) {
                    DataBase dataBase = plugin.getDataBase();
                    String dbName = dataBase.getDbName();
                    Set<String> tableNames = dataBase.getTableNames();
                    Iterator<String> iterator = tableNames.iterator();
                    while (iterator.hasNext()) {
                        String tableName = iterator.next();
                        Table table = dataBase.getTable(tableName);
                        tConfig.addSourceTable(appName, dbName,table);
                        Field[] fields = table.getAllFields();
                        for (int j=0;j<fields.length;j++) {
                            Field field = fields[j];
                            tConfig.addSourceField(appName, dbName,tableName, field);
                        }
                    }
                } else if(Type.s_app_proxy.equals(appType)) {
                    IpMac[] ipBlackMacs = plugin.getSourceSocket().getIpblacklist();
                    IpMac[] ipWhiteMacs = plugin.getSourceSocket().getIpwhitelist();
                    tConfig.addProxyBlackIpMac(appName, ipBlackMacs);
                    tConfig.addProxyWhiteIpMac(appName, ipWhiteMacs);
                }

            }
            tConfig.saveExternal();
            if(appNames.length()>0){
                addDeleteStatus(appNames);
            }
        } catch (Ex ex) {
            logger.error("",ex);
        }

    }

    private void addDeleteStatus(String appNames){
        String[][] params = new String[][] {
                { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                { "appNames", appNames }
        };
        ServiceResponse serviceResponse = ServiceUtil.callStp("FileTypeAction_addDeleteStatus.action", params);
        if (serviceResponse != null && serviceResponse.getCode() == 200){
            logger.info("入库成功");
        }
    }


    /**
     * 会话空闲
     * @param session
     * @param status
     * @throws Exception
     */
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        logger.info("Session idle...");
    }

    /**
     * 打开连接触发的事件，它与sessionCreated的区别在于，
     * 一个连接地址（A）第一次请求Server会建立一个Session默认超时时间为1分钟，
     * 此时若未达到超时时间这个连接地址（A）再一次向Server发送请求
     * 即是sessionOpened（连接地址（A）第一次向Server发送请求或者连接超时后
     * 向Server发送请求时会同时触发sessionCreated和sessionOpened两个事件）
     * @param session
     * @throws Exception
     */
    public void sessionOpened(IoSession session) throws Exception {

    }


}
