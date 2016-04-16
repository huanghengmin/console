package com.hzih.stp.web.thread;

import com.hzih.stp.constant.ServiceConstant;
import com.hzih.stp.service.LogService;
import com.hzih.stp.utils.Configuration;
import com.hzih.stp.utils.StringContext;
import com.hzih.stp.utils.StringUtils;
import com.inetec.common.config.stp.nodes.Jdbc;
import com.inetec.ichange.console.config.Constant;
import com.inetec.ichange.console.config.database.DBFactory;
import com.inetec.ichange.console.config.database.IDataBase;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ztt  数据库连接检测
 * Date: 13-11-4
 * Time: 下午3:29
 * To change this template use File | Settings | File Templates.
 */
public class TestConnectThread implements Runnable {
    private final static Logger logger = Logger.getLogger(TestConnectThread.class);
    private ServletContext servletContext;

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public TestConnectThread(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void run() {
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);

        LogService logService = (LogService)context.getBean(ServiceConstant.LOG_SERVICE);
       //判断为内网还是外网
        boolean privated = StringUtils.getPrivated();
        List<Jdbc> jdbcs = null;
        Configuration config = null;
        //获取当前数据库所有配置
        while (true){
            try{
                if(privated == true){    //内网
                    config = new Configuration(StringContext.INTERNALXML);
                } else {            //外网
                    config = new Configuration(StringContext.EXTERNALXML);
                }
                jdbcs = config.getJdbcs();
                boolean isConnectable = false;
                for(int i = 0;i<jdbcs.size();i++){
                    try{
                        IDataBase db = DBFactory.getDataBase(jdbcs.get(i), Constant.DB_INTERNAL);
                        db.openConnection();
                        isConnectable = db.isConnectable();
                        db.closeConnection();
                        if(isConnectable == false){
                            logger.warn("测试 " + (privated == true ? "可信端" : "非可信端") + "数据源 " + jdbcs.get(i).getJdbcName() + " 失败,不能连通!");
                            if(logService != null){
                                logService.newSysLog("WARN","数据库源","测试数据库连接","测试 " + (privated == true ? "可信端" : "非可信端") + "数据源 " + jdbcs.get(i).getJdbcName() + "失败!");
                            }
                        }
                    }catch (Exception e){
                        logger.error("测试 "+(privated == true ? "可信端":"非可信端") + "数据源 " + jdbcs.get(i).getJdbcName() + " 失败!");
                        if(logService != null){
                            logService.newSysLog("WARN","数据源","测试数据库连接","测试 " + (privated == true ? "可信端" : "非可信端") + "数据源 " + jdbcs.get(i).getJdbcName() + "失败!");
                        }
                    }
                }
                Thread.sleep((long)10*60*1000);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}
