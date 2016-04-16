package com.hzih.stp.utils;

import org.apache.log4j.Logger;

import java.sql.*;

public class SqliteUtil {
    private static final Logger logger = Logger.getLogger(SqliteUtil.class);

    public static synchronized Connection connect() {
        String fileName = null;
        if(System.getProperty("ichange.home").contains("/usr/app/stp")) {
                fileName = System.getProperty("ichange.home") + "/database/filechange.sqlite";
            } else {
                fileName = "/data/database/filechange.sqlite";
            }
        try{
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + fileName);
        } catch (Exception e) {
            logger.error("connect is error",e);
        }
        return null;
    }

    public static synchronized void delete(String appName,String fileFullName) {
        Connection conn = connect();
        if(conn!=null) {
            String sql = "delete FROM filechangeindex where appName ='"+appName+"' and fileFullName = '"+fileFullName+"'";
            try {
                Statement stmt = conn.createStatement();
                int result = stmt.executeUpdate(sql);
                if(result==0) {
                    logger.info("delete filechangeindex success");
                }
            } catch (SQLException e) {
                logger.error("delete error",e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }

        }
    }


}
