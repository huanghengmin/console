package com.hzih.stp.web.servlet;


import com.hzih.stp.constant.AppConstant;
import com.hzih.stp.constant.ServiceConstant;
import com.hzih.stp.domain.SafePolicy;
import com.hzih.stp.service.SafePolicyService;
import com.hzih.stp.utils.Configuration;
import com.hzih.stp.utils.ServiceUtil;
import com.hzih.stp.utils.StringContext;
import com.hzih.stp.utils.StringUtils;
import com.hzih.stp.utils.udp.UdpServer;
import com.hzih.stp.utils.udp.UploadService;
import com.hzih.stp.web.SiteContext;
import com.hzih.stp.web.thread.*;
import com.inetec.common.config.stp.nodes.Plugin;
import com.inetec.common.config.stp.nodes.TargetFile;
import com.inetec.common.exception.Ex;
import com.inetec.common.util.OSInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

public class SiteContextLoaderServlet extends DispatcherServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(SiteContextLoaderServlet.class);
    public static EmailService emailService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext servletContext = config.getServletContext();
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);

		SiteContext.getInstance().contextRealPath = config.getServletContext()
				.getRealPath("/");

		// set constants value to app context
		servletContext.setAttribute("appConstant", new AppConstant());

	/*	Properties pros = new Properties();
		try {
			pros.load(getClass().getResourceAsStream("/config.properties"));
			ServiceUtil.serviceUrl = pros.getProperty("serviceUrl");
			ServiceUtil.platformUrl = pros.getProperty("platformUrl");
			ServiceUtil.consoleUrl = pros.getProperty("externalConsoleUrl");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		SafePolicyService service = (SafePolicyService)context.getBean(ServiceConstant.SAFEPOLICY_SERVICE);
		SafePolicy data = service.getData();
		SiteContext.getInstance().safePolicy = data;

       /* String srcHost = "1.1.1.2";
        String dstHost = "1.1.1.1";

        InetSocketAddress s = new InetSocketAddress(srcHost,6700);

        InetSocketAddress t = new InetSocketAddress(dstHost,6700);

        ServiceUtil.addressMap.put("s",s);

        ServiceUtil.addressMap.put("t",t);

        try{
            OSInfo osInfo = OSInfo.getOSInfo();
            if(osInfo.isLinux()){
                new SystemStatusService().start();
                new DiskCheckService().start();
                if(StringUtils.getPrivated()){
                    runUploadService();
//                    runReceiveService();
//                    runConfigReceiveService();
                }
            } else {
//                new SystemStatusService().start();

            }


            emailService = new EmailService();
            emailService.init();
            emailService.start();
        }catch (Exception e) {
            logger.error("启动自身监控线程出错", e);
        }*/

//        TestConnectThread testConnectThread = new TestConnectThread(servletContext);
//        Thread thread = new Thread(testConnectThread);
//        TestTriggerThread testTriggerThread = new TestTriggerThread(servletContext);
//        Thread trigger_thread = new Thread(testTriggerThread);
//        thread.start();
//        trigger_thread.start();

	}

    public static UploadService uploadService = new UploadService();
    public static boolean isUploadService = false;

    private void runUploadService() throws Ex{
        Configuration config = new Configuration(StringContext.INTERNALXML);
        TargetFile targetFile = config.getTargetFile("flag", Plugin.s_target_plugin);
        if(isUploadService) {
            return;
        }
        uploadService.init(targetFile);
        uploadService.start();
    }

    private void runReceiveService() throws Ex {
        UdpServer udpServer = new UdpServer();
        udpServer.init();
        new Thread(udpServer).start();
        String dstHost = "1.1.1.1";
        int port = 6700;
        InetSocketAddress target = ServiceUtil.addressMap.get("t");
        Configuration config = new Configuration(StringContext.INTERNALXML);
        TargetFile targetFile = config.getTargetFile("flag", Plugin.s_target_plugin);
        udpServer.startUp(target, "/data/flag", targetFile);

    }

    private void runConfigReceiveService() {
        UdpServer udpServer = new UdpServer();
        udpServer.init();
        new Thread(udpServer).start();
        String dstHost = "1.1.1.1";
        int port = 6666;
        InetSocketAddress target = new InetSocketAddress(dstHost,port);
        udpServer.startUp(target, StringContext.SAVEFILEPATH, null);
    }

    @Override
	public ServletConfig getServletConfig() {
		// do nothing
		return null;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// do nothing
	}

	@Override
	public String getServletInfo() {
		// do nothing
		return null;
	}

	@Override
	public void destroy() {

	}

}
