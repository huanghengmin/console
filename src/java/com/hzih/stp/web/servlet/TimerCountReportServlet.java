package com.hzih.stp.web.servlet;

import com.hzih.stp.constant.AppConstant;
import com.hzih.stp.utils.DateUtils;
import com.hzih.stp.web.SiteContext;
import com.inetec.common.util.OSInfo;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class TimerCountReportServlet extends DispatcherServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(TimerCountReportServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext servletContext = config.getServletContext();
		Thread buildReportThread = new Thread(new BuildReportThread(
				servletContext));
		buildReportThread.start();
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

class BuildReportThread implements Runnable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(BuildReportThread.class);

	private ServletContext servletContext;

	public BuildReportThread() {

	}

	public BuildReportThread(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void run() {
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
//
//		BusinessReportService brs = (BusinessReportService) context
//				.getBean(ServiceConstant.BUSINESS_REPORT_SERVICE);
//		EquipmentReportService ers = (EquipmentReportService) context
//				.getBean(ServiceConstant.EQUIPMENT_REPORT_SERVICE);

		while (true) {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE, -1);
			Date date = now.getTime();

			try {
				//brs.updBuildReport(DateUtils.getNow());// only for test
				//ers.updBuildReport(DateUtils.getNow());// only for test

				Calendar trigger = Calendar.getInstance();
				trigger.setTimeInMillis(System.currentTimeMillis());
				if (trigger.get(Calendar.HOUR_OF_DAY) == 2) {
//					brs.updBuildReport(date);
//					ers.updBuildReport(date);
				}
				if(trigger.get(Calendar.HOUR_OF_DAY) > 2){
					logger.info("开始report修复逻辑");
//					brs.updBuildReport2(date);
                    backup();
//					ers.updBuildReport2(date);
					logger.info("结束report修复逻辑");
				}
				Thread.sleep(3600 * 1000);
			} catch (InterruptedException e) {

			} catch (Exception ex) {
			}
		}
	}

    private void backup() {
        try{
            SAXReader reader = new SAXReader();
            String fileName = SiteContext.getInstance().contextRealPath
                    + AppConstant.BACKUP_CONFIG_PATH;
            String dbFile = SiteContext.getInstance().contextRealPath
                    + AppConstant.XML_DB_CONFIG_PATH;
            Document doc = reader.read(new File(fileName));
            String conf_enabled = doc.selectSingleNode("//backup/conf_enabled").getText();
            if("0".equals(conf_enabled)){
                return;
            }
            Document dbDoc = reader.read(new File(dbFile));
            String user = dbDoc.selectSingleNode("//config/maindb/username").getText();
            String pass = dbDoc.selectSingleNode("//config/maindb/password").getText();
            String dbname = dbDoc.selectSingleNode("//config/maindb/dbname").getText();

            String conf_file_path = doc.selectSingleNode("//backup/conf_file_path").getText();

            String conf_type = doc.selectSingleNode("//backup/conf_type").getText();
            boolean isBackUp = false;
            String backFile = conf_file_path + "/" + DateUtils.formatDate(new Date(),"yyyy-MM-dd")+".sql";
            File file = new File(backFile);
            if(file.exists()){
                return;
            }
            Calendar calendar = Calendar.getInstance();
            if("day".equalsIgnoreCase(conf_type)) {
                String time = doc.selectSingleNode("//backup/conf_time").getText();
                int timeH = Integer.parseInt(time.split(":")[0]);
                int timeS = Integer.parseInt(time.split(":")[0]);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int second = calendar.get(Calendar.SECOND);
                if(hour == timeH){
                    if(second > timeS){
                        isBackUp = true;
                    }
                } else if(hour > timeH) {
                    isBackUp = true;
                }
            } else if("week".equalsIgnoreCase(conf_type)) {
                int week_day = Integer.parseInt(doc.selectSingleNode("//backup/conf_day").getText());
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                int r = day - week_day;
                if(r == 1 || r == -6){
                    String time = doc.selectSingleNode("//backup/conf_time2").getText();
                    int timeH = Integer.parseInt(time.split(":")[0]);
                    int timeS = Integer.parseInt(time.split(":")[0]);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int second = calendar.get(Calendar.SECOND);
                    if(hour == timeH){
                        if(second > timeS){
                            isBackUp = true;
                        }
                    } else if(hour > timeH) {
                        isBackUp = true;
                    }
                }
            } else if("month".equalsIgnoreCase(conf_type)) {
                int month_day = Integer.parseInt(doc.selectSingleNode("//backup/conf_month_day").getText());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                if(month_day == day){
                    String time = doc.selectSingleNode("//backup/conf_time3").getText();
                    int timeH = Integer.parseInt(time.split(":")[0]);
                    int timeS = Integer.parseInt(time.split(":")[0]);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int second = calendar.get(Calendar.SECOND);
                    if(hour == timeH){
                        if(second > timeS){
                            isBackUp = true;
                        }
                    } else if(hour > timeH) {
                        isBackUp = true;
                    }
                }
            }
            if(isBackUp) {
                backupDB(user,pass,dbname,backFile);
            }

        } catch (Exception e) {
            logger.error("备份错误",e);
        }


    }

    public static void backupDB(String userName,String password,String dbName,String backFile) {
        try {
            Runtime rt = Runtime.getRuntime();

            // 调用 mysql 的 cmd:
            Process child = null;// 设置导出编码为utf8。这里必须是utf8
            OSInfo osInfo = OSInfo.getOSInfo();
            if(osInfo.isLinux()){
                child = rt.exec("mysqldump -u"+userName+" -p"+password+" "+dbName);
                // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
                InputStream in = child.getInputStream();// 控制台的输出信息作为输入流

                InputStreamReader xx = new InputStreamReader(in, "utf8");// 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码

                String inStr;
                StringBuffer sb = new StringBuffer("");
                String outStr;
                // 组合控制台输出信息字符串
                BufferedReader br = new BufferedReader(xx);
                while ((inStr = br.readLine()) != null) {
                    sb.append(inStr + "\r\n");
                }
                outStr = sb.toString();

                // 要用来做导入用的sql目标文件：
                FileOutputStream fout = new FileOutputStream(backFile);
                OutputStreamWriter writer = new OutputStreamWriter(fout, "utf8");
                writer.write(outStr);
                // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免
                writer.flush();

                // 别忘记关闭输入输出流
                in.close();
                xx.close();
                br.close();
                writer.close();
                fout.close();
                logger.info("备份导出数据表" + backFile + "成功");
            }
        } catch (Exception e) {
            logger.error("备份导出数据表错误",e);
        }

    }

}