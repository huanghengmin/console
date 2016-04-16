package com.hzih.stp.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LicenseUtils {
    private static final Logger logger = Logger.getLogger(LicenseUtils.class);
    /**
     *  权限控制
     * @param isExistLicense    是否存在 usb-key
     * @return
     */
	public List<String> getNeedsLicenses(boolean isExistLicense) {
        boolean privated = StringUtils.getPrivated();


        String qxManager = "";                   //权限管理
//        String wlManager = "";     //网络管理
//        String bjManager = "";      //报警管理
        String sjManager = "";//审计管理
//        String nwpzManager = "";//内网配置管理
//        String wwpzManager = "";//外网配置管理

//        String nwjkManager = "";             //内网运行监控
//        String wwjkManager = "";                             //外网运行监控
//        String nwxtManager = "";//内网系统管理
//        String wwxtManager = "";//外网系统管理
//        String nwsjyManager ="";                                   //内网数据源管理
//        String wwsjyManager ="";                                   //外网数据源管理
//        String nwyyManager = "";                         //内网应用管理
//        String wwyyManager = "";                         //外网应用管理
//        String nwshManager = "";//内网审核管理
//        String wwshManager = "";//外网审核管理
//        String rzglManager = ""; //
        String fwglManager = ""; //
        String lhccManager = ""; //
        String htjkManager = "";

        Properties pros = new Properties();
		try {
			pros.load(getClass().getResourceAsStream("/config.properties"));
			qxManager = pros.getProperty("qxManager");
//			wlManager = pros.getProperty("wlManager");
//			bjManager = pros.getProperty("bjManager");
			sjManager = pros.getProperty("sjManager");
//			nwpzManager = pros.getProperty("nwpzManager");
//			wwpzManager = pros.getProperty("wwpzManager");

//            nwjkManager = pros.getProperty("nwjkManager");
//            wwjkManager = pros.getProperty("wwjkManager");
//            nwxtManager = pros.getProperty("nwxtManager");
//			wwxtManager = pros.getProperty("wwxtManager");
//			nwsjyManager = pros.getProperty("nwsjyManager");
//			wwsjyManager = pros.getProperty("wwsjyManager");
//			nwyyManager = pros.getProperty("nwyyManager");
//			wwyyManager = pros.getProperty("wwyyManager");
//			nwshManager = pros.getProperty("nwshManager");
//			wwshManager = pros.getProperty("wwshManager");
//			rzglManager = pros.getProperty("rzglManager");
            fwglManager = pros.getProperty("fwglManager");
            lhccManager = pros.getProperty("lhccManager");
            htjkManager = pros.getProperty("htjkManager");
		} catch (IOException e) {
			logger.error("",e);
		}

        String permission = "";
        if(privated){     //内网
            permission = qxManager +/* bjManager +*/ sjManager +/* wlManager + nwpzManager +
                         nwxtManager + nwsjyManager + nwyyManager + nwshManager + nwjkManager + rzglManager +*/ fwglManager+lhccManager+htjkManager;
//                         License.getModules();
        } else {      //外网
            permission = qxManager +/* bjManager +*/ sjManager +/* wlManager + wwpzManager +
                         wwxtManager + wwsjyManager + wwyyManager + wwshManager + wwjkManager + rzglManager +*/ fwglManager+lhccManager+htjkManager;
//                         License.getModules();
        }
		String[] permissions = permission.split(":");
		List<String> lps = new ArrayList<String>();
		for (int i = 0; i < permissions.length; i++) {
			lps.add(permissions[i]);
		}
		return lps;
	}

    public static int getChannelCount() {

        return 1;
    }
}
