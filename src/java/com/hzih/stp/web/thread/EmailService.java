package com.hzih.stp.web.thread;

import JACE.ASX.MessageQueue;
import JACE.ASX.TimeValue;
import com.hzih.stp.utils.Configuration;
import com.hzih.stp.utils.SendMail;
import com.hzih.stp.utils.StringContext;
import com.inetec.common.exception.Ex;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 13-5-23
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
public class EmailService extends Thread {
    private static final Logger logger = Logger.getLogger(EmailService.class);
    public final static String XML_ALERT_CONFIG_PATH = "/data/alert/alert-config.xml";
    private static final String fileName = StringContext.systemPath + XML_ALERT_CONFIG_PATH;
//    public BlockingQueue<String> query;
    private MessageQueue queue;
    private Map<String, String> model;
    private List<String> list;
    public static Map<String,Long> sendMap = new HashMap<String, Long>();
    private int alertInterval = 10;
    private String _title;
    private boolean isRunning = false;


    public void init() {
//        query = new LinkedBlockingQueue<String>(100);
        queue = new MessageQueue();
        Configuration config = null;
        try {
            config = new Configuration(fileName);
        } catch (Ex ex) {

        }
        this.model = config.getAlertEmail();
        this._title = model.get("title");
        this.list = config.getAlertEmailList();
        this.alertInterval = Integer.valueOf(model.get("mailfrequency")) * 60 * 1000;
    }

    /*public String pollQuery() {
        try {
            return query.take();
        } catch (InterruptedException e) {

        }
        return null;
    }*/

    public boolean haveMessages() {
        if(queue == null) {
            queue = new MessageQueue();
            return false;
        }
        return (!queue.isEmpty());
    }

    public void offer(String info) {
        try {
            queue.enqueueTail(new JACE.ASX.MessageBlock(info));
        } catch (InterruptedException e) {
        }
    }

    public void run() {
        isRunning = true;
        while (isRunning()) {
            if(haveMessages()){
                try {
                    String json = queue.dequeueHead(new TimeValue(3000)).base();
                    JSONObject jsonObject = JSONObject.fromObject(json);
                    String text = jsonObject.getString("text");
                    String title = _title + " - " + jsonObject.getString("title");
                    String fileNames = jsonObject.getString("fileName");
                    String key = _title + " - " + jsonObject.getString("title");
                    model.remove("title");
                    model.put("title",title);
                    model.put("fileNames",fileNames);
                    model.put("text",text);
                    for (String toEmail : list) {
                        String sendMapKey = toEmail + key;
                        long lastSendTime = sendMap.get(sendMapKey) == null?0:sendMap.get(sendMapKey);
                        long nowTime = System.currentTimeMillis();
                        if( lastSendTime !=0 && nowTime-lastSendTime <= this.alertInterval ){
                            continue;
                        }
                        boolean isSuccess = sendMessage(model,toEmail);
                        String msg = null;
                        if(isSuccess){
                            sendMap.put(sendMapKey,nowTime);
                            msg = "发给"+toEmail+"邮件已发出,请注意查收!";
                        } else {
                            msg = "发给"+toEmail+"邮件发送失败";
                        }
                        logger.info(msg);
                    }
                } catch (InterruptedException e) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public boolean sendMessage(Map<String, String> model, String toEmail) {
        String emailServer = model.get("server");
        String connectName = model.get("account");
        String password = model.get("password");
        boolean isNeedAuth = true;
        String fromEmail = model.get("email");
        String title = model.get("title");
        String text = model.get("text");
        String fileNames = model.get("fileNames");
        List<String> fileAffix = makeFiles(fileNames);
        return SendMail.sendMessage(emailServer, connectName, password, isNeedAuth,
                fromEmail, toEmail, title, text, fileAffix);

    }

    private List<String> makeFiles(String fileNames) {
        List<String> fileAffix = new ArrayList<String>();
        if(fileNames!=null&&fileNames.length()>0&&!"null".equals(fileNames)) {
            String[] files = fileNames.split(";");
            for (int i=0;i<files.length;i++) {
                fileAffix.add(files[i]);
            }
        }
        return fileAffix;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void close() {
       isRunning = false;
    }
}
