package com.hzih.stp.web.action.service;

import com.inetec.common.io.IOUtils;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.*;

/**
 * Created by Administrator on 2015/12/2.
 */
public class InterfaceUtils {
    /**
     *
     * @param fwid
     * @param gmsfhm
     * @param yhm
     * @param mm
     * @param url
     * @return
     * @throws Exception
     */
    public static String getMsgForGmsfhm(String fwid, String gmsfhm, String yhm, String mm, String url){
        JSONObject jsonObj=new JSONObject();
        jsonObj.put("FWID",fwid);
        jsonObj.put("GMSFHM", gmsfhm);
        jsonObj.put("YHM", yhm);
        jsonObj.put("MM", mm);
        String paramData = "params="+jsonObj.toString();
        URL localURL = null;
        try {
            localURL = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
        URLConnection connection = null;
        try {
            connection = localURL.openConnection();
        } catch (IOException e) {
            return null;
        }
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        httpURLConnection.setDoOutput(true);
        try {
            httpURLConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            return null;
        }
        httpURLConnection.setRequestProperty("Charset", "utf-8");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(paramData.length()));
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        byte[]  bytes = null;
        try {
            try {
                outputStream = httpURLConnection.getOutputStream();
            } catch (IOException e) {
                return null;
            }
            try {
                outputStreamWriter = new OutputStreamWriter(outputStream,"utf-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
            try {
                outputStreamWriter.write(paramData.toString());
            } catch (IOException e) {
                return null;
            }
            try {
                outputStreamWriter.flush();
            } catch (IOException e) {
                return null;
            }
            try {
                if (httpURLConnection.getResponseCode() >= 300) {
                    throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
                }
            } catch (Exception e) {
                return null;
            }
            try {
                inputStream = httpURLConnection.getInputStream();
            } catch (IOException e) {
                return null;
            }
            bytes = readInputStream(inputStream);
        } finally {
         if (outputStreamWriter != null) {
             try {
                 outputStreamWriter.close();
             } catch (IOException e) {
                 return null;
             }
         }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    return null;
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(bytes!=null)
            try {
                return new String(bytes,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        else
            return null;
        return null;
    }

    public static void main(String args[])throws Exception {
        String json = InterfaceUtils.getMsgForGmsfhm("ntga2015100030","320621198604176935","064740","111111","http://10.36.11.93:10017/ntgajk/requestData.do");
        System.out.println(json);
        JSONObject jsonObj=new JSONObject();
        jsonObj.put("FWID","ntga2015100029");
        jsonObj.put("GMSFHM", "320621198604176935");
        jsonObj.put("YHM", "064740");
        jsonObj.put("MM", "111111");
        String paramData = "params="+jsonObj.toString();
        URL localURL = new URL("http://10.36.11.93:10017/ntgajk/requestData.do");
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Charset", "utf-8");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(paramData.length()));
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        byte[]  bytes = null;
        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream,"utf-8");
            outputStreamWriter.write(paramData.toString());
            outputStreamWriter.flush();
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            inputStream = httpURLConnection.getInputStream();
            bytes = readInputStream(inputStream);
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        System.out.println(new String(bytes, "UTF-8"));
    }

    public static byte[] readInputStream(InputStream in){
        byte[] buffer  = new byte[1024];
        int len =-1;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            while ((len=in.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getCode(int code){
        switch (code){
            case 1:
                return  "正常返回";
            case 2:
                return  "服务ID不存在";
            case 3:
                return  "条件字段非法";
            case 4:
                return  "数据源失效";
            case 5:
                return  "输入参数出错";
            case 6:
                return  "服务出错（连接异常等情况）";
            case 7:
                return  "输入格式错误";
            case 8:
                return  "用户未授权或者用户不存在";
            case 9:
                return  "超出日最大调用数";
            case 10:
                return  "服务超时";
        }
        return null;
    }
}
