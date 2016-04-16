package com.hzih.stp.web.action.appauth;

import com.google.protobuf.ByteString;
import com.hzih.stp.service.DataBaseService;
import com.hzih.stp.service.LogService;
import com.hzih.stp.service.XmlOperatorService;
import com.hzih.stp.utils.ServiceUtil;
import com.hzih.stp.utils.StringContext;
import com.hzih.stp.utils.StringUtils;
import com.hzih.stp.utils.code.RequestMessage;
import com.hzih.stp.utils.udp.UdpClientImpl;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.hzih.stp.web.action.app.AppTypeAction;
import com.inetec.common.io.IOUtils;
import com.inetec.common.security.FileMd5;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-20
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class ConfigSendAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(AppTypeAction.class);
    private LogService logService;
	private XmlOperatorService xmlOperatorService;
    private DataBaseService dataBaseService;

    public String send() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
        String msg = null;
        try{
            if(!StringUtils.getPrivated()){
                logger.info("进入数据库处理...");
                dataBaseService.operateDBUpdateApp(StringContext.EXTERNAL);
                logger.info("成功数据库处理...");
            }
//            String[][] params = new String[][] {
//                    { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
//                    { "Command", "configsend" }
//            };
//            ServiceResponse serviceResponse = ServiceUtil.callService(params);
//            int returnCode = serviceResponse.getCode();
            int returnCode = sendToTarget(SessionUtils.getAccount(request).getUserName());
            if ( returnCode == 200) {
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"发送配置","发送配置文件成功!", 1);
//                msg = "<font color=\"green\">发送成功</font>";
                Thread.sleep(1000*3);
                xmlOperatorService.updateTypeAppSend(null,StringContext.INIT_APP);
                logService.newSysLog("INFO","审核管理","修改应用状态","发送配置文件成功后,设置应用的状态为0");
                msg = "<font color=\"green\">发送成功,设置状态成功</font>";
            } else {
                msg = "<font color=\"red\">发送失败:"+returnCode+"</font>";
            }
        } catch (Exception e){
            logger.error("发送配置",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"发送配置","发送配置文件失败!", 0);
            msg = "<font color=\"red\">发送失败!</font>";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        base.actionEnd(response, json ,result);
        return null;
    }

    private int sendToTarget(String userName) throws Exception{
        File file = new File(StringContext.EXTERNALXML);
        String md5 = FileMd5.getFileMD5String(file);
        String srcHost = "1.1.1.2";
        String dstHost = "1.1.1.1";
        int port = 6666;
        InetSocketAddress remote = new InetSocketAddress(dstHost,port);
        InetSocketAddress local = new InetSocketAddress(srcHost,port);

        UdpClientImpl client = new UdpClientImpl(userName,remote,local);
        client.start();

        InputStream is = new FileInputStream(file);

        boolean isFPacket = true;//发送的第1包
        long tempFileSize = file.length();
        int allLen = 0;
        int len = 0;
        byte[] nameBuf = file.getName().getBytes();
        int nameLength = nameBuf.length;
        int bodyMaxLength = ServiceUtil.UDPPACKETSIZE - nameLength -1;
        byte[] buff = new byte[bodyMaxLength];
        while ((len = is.read(buff)) != -1) {
            RequestMessage message = new RequestMessage();
            if( len < bodyMaxLength ) {
                buff = IOUtils.copyArray(buff, len);
            }
            message.setFileCode((byte) 1);
            message.setFileSize(tempFileSize);
            ByteString nameBody = ByteString.copyFrom(nameBuf);
            message.setNameBody(nameBody);
            ByteString msgBody = ByteString.copyFrom(buff);
            message.setMsgBody(msgBody);
            ByteString md5Body = ByteString.copyFrom(md5.getBytes());
            message.setMd5Body(md5Body);
            allLen += len;
            if(isFPacket){
                if(tempFileSize == len) {
                    message.setProtocolType(RequestMessage.ProtocolTypeOnly);
                } else if(tempFileSize > len) {
                    message.setProtocolType(RequestMessage.ProtocolTypeStart);
                }
                isFPacket = false;
            } else {
                if(tempFileSize==(long)allLen) {
                    message.setProtocolType(RequestMessage.ProtocolTypeEnd);
                } else {
                    message.setProtocolType(RequestMessage.ProtocolType);
                }
            }
            client.sendMessage(message);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }

        return 200;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public XmlOperatorService getXmlOperatorService() {
        return xmlOperatorService;
    }

    public void setXmlOperatorService(XmlOperatorService xmlOperatorService) {
        this.xmlOperatorService = xmlOperatorService;
    }

    public DataBaseService getDataBaseService() {
        return dataBaseService;
    }

    public void setDataBaseService(DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }
}
