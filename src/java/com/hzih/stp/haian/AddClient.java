package com.hzih.stp.haian;


import com.hzih.stp.utils.FileUtil;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Administrator on 15-9-11.
 */
public class AddClient {
    public static void main(String[] args) {
        System.out.print("请输入配置文件地址:");
        Scanner scanner = new Scanner(System.in);
        String configPath = scanner.nextLine();
        System.out.println("配置文件地址确认:"+configPath);

        Properties pros = new Properties();
        String policeId = "063230";
        String serviceId = "100051";
        String queryStr = " 1=1 ";
        int start = 0;
        int limit = 20;
        String dir = "D:/client";
        InputStream in = null;
        try {
            in = new FileInputStream(configPath);
            pros.load(in);
            policeId = pros.getProperty("policeId");
            serviceId = pros.getProperty("serviceId");
            queryStr = " " + pros.getProperty("queryStr") + " ";
            dir = pros.getProperty("dir");
            String startStr = pros.getProperty("start");
            String limitStr = pros.getProperty("limit");
            start = Integer.parseInt(startStr);
            limit = Integer.parseInt(limitStr);
        } catch (FileNotFoundException e) {
            System.out.println("找不到文件"+configPath);
        } catch (IOException e) {
            System.out.println("读取文件"+configPath+"错误");
        }

        Add add = new Add();
        AddPortType addPortType = add.getAddHttpPort();
        ArrayOfArrayOfString arrayOfArrayOfString = addPortType.getData(policeId, serviceId, queryStr, start, limit, 1);
        writeFile(arrayOfArrayOfString,dir + "/getdata.txt");

        ArrayOfArrayOfString arrayOfArrayOfStringAll = addPortType.getDataAll(policeId, serviceId, queryStr, start, limit);
        writeFile(arrayOfArrayOfStringAll,dir + "/getdataall.txt");

        ArrayOfArrayOfString arrayOfArrayOfStringDw = addPortType.getDataDw(policeId,serviceId,queryStr,"",start,limit,1);
        writeFile(arrayOfArrayOfStringDw,dir + "/getdatadw.txt");

        ArrayOfArrayOfString arrayOfArrayOfStringDwgAll = addPortType.getDataDwAll(policeId,serviceId,queryStr,"",start,limit,1);
        writeFile(arrayOfArrayOfStringDwgAll,dir + "/getdatadwall.txt");

        ArrayOfString arrayOfStringFz = addPortType.getFzData(policeId);
        writeFileEnd(arrayOfStringFz.getString(),dir + "/getFzData.txt");

        String dataCount = addPortType.getDataCount(policeId,serviceId,queryStr,start,limit,1);
        FileUtil.writeOneLine(dataCount, new File(dir + "/getDataCount.txt"));

        ArrayOfArrayOfString arrayOfArrayOfStringDwDb = addPortType.getDataDwDb(policeId,serviceId,queryStr,"",start,limit,1);
        writeFile(arrayOfArrayOfStringDwDb,dir + "/getDataDwDb.txt");
    }

    public static void writeFileEnd(List<String> listStr,String filePath) {
        for (String str : listStr) {
            FileUtil.writeOneLine(str,new File(filePath));
        }
    }
    public static void writeFile(List<ArrayOfString> list,String filePath) {
        for (ArrayOfString arrayOfString : list) {
            List<String> listStr = arrayOfString.getString();
            writeFileEnd(listStr,filePath);
        }
    }
    public static void writeFile(ArrayOfArrayOfString arrayOfArrayOfString,String filePath) {
        List<ArrayOfString> list = arrayOfArrayOfString.getArrayOfString();
        writeFile(list,filePath);
    }
}
