/*
package com.hzih.stp.web.action.service;

import com.hzih.stp.service.LogService;
import com.hzih.stp.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

*/
/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 15-9-5
 * Time: 上午10:08
 *//*

public class HttpInterfaceAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(HttpInterfaceAction.class);

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public LogService getLogService() {

        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    private LogService logService;
    private int start;
    private int limit;

    public String getCodeString(int code) {
        switch (code) {
            case 1:
                return "正常返回。";
            case 2:
                return "服务ID不存在。";
            case 3:
                return "条件字段非法。";
            case 4:
                return "数据源失效。";
            case 5:
                return "输入参数出错。";
            case 6:
                return "服务出错（连接异常等情况）。";
            case 7:
                return "输入格式错误。";
            case 8:
                return "用户未授权或者用户不存在。";
            case 9:
                return "超出日最大调用数。";
            case 10:
                return "服务超时。";

        }
        return null;
    }


    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String gmsfhm = request.getParameter("GMSFHM");
        String xm = request.getParameter("XM");
        String yhm = request.getParameter("YHM");
        String mm = request.getParameter("MM");
        String url = "http://10.36.11.93:10017/ntgajk/requestData.do";
        if (xm == null) {
            String fwid = request.getParameter("FWID");  //姓名查询服务ID
            String json = getPeopleForGMSFHM(fwid, gmsfhm, yhm, mm, url);
            JSONObject jsonObject = JSONObject.fromObject(json);
            int code = jsonObject.getInt("CODE");
            if (1 == code) {
                StringBuilder columModles = new StringBuilder();
                columModles.append("'columModles':").append("[");
                columModles.append("{'header': '身份证','dataIndex': 'GMSFHM'}").append(",");
                columModles.append("{'header': '姓名','dataIndex': 'XM'}").append(",");
                columModles.append("{'header': '性别','dataIndex': 'XB'}").append(",");
                columModles.append("{'header': '民族','dataIndex': 'MZ'}").append(",");
                columModles.append("{'header': '出生日期','dataIndex': 'CSRQ'}").append(",");
                columModles.append("{'header': '服务处所','dataIndex': 'FWCS'}").append(",");
                columModles.append("{'header': '电话','dataIndex': 'DH'}").append(",");
                columModles.append("{'header': '移动电话','dataIndex': 'YDDH'}").append(",");
                columModles.append("{'header': '户籍地址','dataIndex': 'HJDZ'}").append(",");
                columModles.append("{'header': '现地址','dataIndex': 'XZZ'}").append(",");
                columModles.append("{'header': '照片','dataIndex': 'URL'}").append(",");  //另外一张表的信息
                columModles.append("{'header': '死亡时间','dataIndex': 'RESERVE93'}");
                columModles.append("]");

                StringBuilder fields = new StringBuilder();
                fields.append("'fieldsNames':").append("[");
                fields.append("{'name': 'GMSFHM'}").append(",");
                fields.append("{'name': 'XM'}").append(",");
                fields.append("{'name': 'XB'}").append(",");
                fields.append("{'name': 'MZ'}").append(",");
                fields.append("{'name': 'CSRQ'}").append(",");
                fields.append("{'name': 'FWCS'}").append(",");
                fields.append("{'name': 'DH'}").append(",");
                fields.append("{'name': 'YDDH'}").append(",");
                fields.append("{'name': 'HJDZ'}").append(",");
                fields.append("{'name': 'XZZ'}").append(",");
                fields.append("{'name': 'URL'}").append(","); //另外一张表的信息
                fields.append("{'name': 'RESERVE93'}");
                fields.append("]");

                String rs = jsonObject.get("RESULT").toString();
                JSONArray array = JSONArray.fromObject(rs);
                StringBuilder datas = new StringBuilder();
                datas.append("'data':").append("[");
                for (int i = 0; i < array.size(); i++) {
                    datas.append("{");
                    JSONObject object = array.getJSONObject(i);
                    String gsm = object.getString("GMSFHM");
                    datas.append("GMSFHM:'").append(gsm).append("'").append(",");
                    String XM = object.getString("XM");
                    datas.append("XM:'").append(XM).append("'").append(",");
                    String XB = object.getString("XB");
                    datas.append("XB:'").append(XB).append("'").append(",");
                    String MZ = object.getString("MZ");
                    datas.append("MZ:'").append(MZ).append("'").append(",");
                    String CSRQ = object.getString("CSRQ");
                    datas.append("CSRQ:'").append(CSRQ).append("'").append(",");
                    String FWCS = object.getString("FWCS");
                    datas.append("FWCS:'").append(FWCS).append("'").append(",");
                    String DH = object.getString("DH");
                    datas.append("DH:'").append(DH).append("'").append(",");
                    String YDDH = object.getString("YDDH");
                    datas.append("YDDH:'").append(YDDH).append("'").append(",");
                    String HJDZ = object.getString("HJDZ");
                    datas.append("HJDZ:'").append(HJDZ).append("'").append(",");
                    String XZZ = object.getString("XZZ");
                    datas.append("XZZ:'").append(XZZ).append("'").append(",");
                    String RESERVE93 = object.getString("RESERVE93");
                    datas.append("RESERVE93:'").append(RESERVE93).append("'").append(",");

                    //查询用户照片信息
                    String photo = getPhotoMsg(fwid,gsm,yhm,mm,url);
                    JSONObject photo_object = JSONObject.fromObject(photo);
                    int photo_code = photo_object.getInt("CODE");
                    if(photo_code==1){
                        String URL = photo_object.getString("URL");  //另外一张表的信息
                        datas.append("URL:'").append(URL).append("'");
                    }else {
                        datas.append("URL:'").append("").append("'");
                    }

                    datas.append("}");
                    if (i != array.size() - 1) {
                        datas.append(",");
                    }
                }
                datas.append("]");


                StringBuilder r = new StringBuilder();
                r.append("{success:true,'msg':'success'");
                r.append(",");
                r.append(datas);
                r.append(",");
                r.append(columModles);
                r.append(",");
                r.append(fields);
                r.append("}");

                actionBase.actionEnd(response, r.toString(), result);

            } else {
                String ret = getCodeString(code);
                StringBuilder r = new StringBuilder();
                r.append("{success:false,'code':'"+ret+"'");
                r.append("}");
                actionBase.actionEnd(response, r.toString(), result);
            }
        } else {
            String fwid = request.getParameter("FWID"); //身份证查询服务ID
            String json = getPeopleForXM(fwid, xm, yhm, mm, url);
            JSONObject jsonObject = JSONObject.fromObject(json);
            int code = jsonObject.getInt("CODE");
            if (1 == code) {
                StringBuilder columModles = new StringBuilder();
                columModles.append("'columModles':").append("[");
                columModles.append("{'header': '身份证','dataIndex': 'GMSFHM'}").append(",");
                columModles.append("{'header': '姓名','dataIndex': 'XM'}").append(",");
                columModles.append("{'header': '性别','dataIndex': 'XB'}").append(",");
                columModles.append("{'header': '民族','dataIndex': 'MZ'}").append(",");
                columModles.append("{'header': '出生日期','dataIndex': 'CSRQ'}").append(",");
                columModles.append("{'header': '服务处所','dataIndex': 'FWCS'}").append(",");
                columModles.append("{'header': '电话','dataIndex': 'DH'}").append(",");
                columModles.append("{'header': '移动电话','dataIndex': 'YDDH'}").append(",");
                columModles.append("{'header': '户籍地址','dataIndex': 'HJDZ'}").append(",");
                columModles.append("{'header': '现地址','dataIndex': 'XZZ'}").append(",");
                columModles.append("{'header': '照片','dataIndex': 'URL'}").append(",");  //另外一张表的信息
                columModles.append("{'header': '死亡时间','dataIndex': 'RESERVE93'}");
                columModles.append("]");

                StringBuilder fields = new StringBuilder();
                fields.append("'fieldsNames':").append("[");
                fields.append("{'name': 'GMSFHM'}").append(",");
                fields.append("{'name': 'XM'}").append(",");
                fields.append("{'name': 'XB'}").append(",");
                fields.append("{'name': 'MZ'}").append(",");
                fields.append("{'name': 'CSRQ'}").append(",");
                fields.append("{'name': 'FWCS'}").append(",");
                fields.append("{'name': 'DH'}").append(",");
                fields.append("{'name': 'YDDH'}").append(",");
                fields.append("{'name': 'HJDZ'}").append(",");
                fields.append("{'name': 'XZZ'}").append(",");
                fields.append("{'name': 'URL'}").append(","); //另外一张表的信息
                fields.append("{'name': 'RESERVE93'}");
                fields.append("]");

                String rs = jsonObject.get("RESULT").toString();
                JSONArray array = JSONArray.fromObject(rs);
                StringBuilder datas = new StringBuilder();
                datas.append("'data':").append("[");
                for (int i = 0; i < array.size(); i++) {
                    datas.append("{");
                    JSONObject object = array.getJSONObject(i);
                    String gsm = object.getString("GMSFHM");
                    datas.append("GMSFHM:'").append(gsm).append("'").append(",");
                    String XM = object.getString("XM");
                    datas.append("XM:'").append(XM).append("'").append(",");
                    String XB = object.getString("XB");
                    datas.append("XB:'").append(XB).append("'").append(",");
                    String MZ = object.getString("MZ");
                    datas.append("MZ:'").append(MZ).append("'").append(",");
                    String CSRQ = object.getString("CSRQ");
                    datas.append("CSRQ:'").append(CSRQ).append("'").append(",");
                    String FWCS = object.getString("FWCS");
                    datas.append("FWCS:'").append(FWCS).append("'").append(",");
                    String DH = object.getString("DH");
                    datas.append("DH:'").append(DH).append("'").append(",");
                    String YDDH = object.getString("YDDH");
                    datas.append("YDDH:'").append(YDDH).append("'").append(",");
                    String HJDZ = object.getString("HJDZ");
                    datas.append("HJDZ:'").append(HJDZ).append("'").append(",");
                    String XZZ = object.getString("XZZ");
                    datas.append("XZZ:'").append(XZZ).append("'").append(",");
                    String RESERVE93 = object.getString("RESERVE93");
                    datas.append("RESERVE93:'").append(RESERVE93).append("'").append(",");

                    //查询用户照片信息
                    String photo = getPhotoMsg(fwid,gsm,yhm,mm,url);
                    JSONObject photo_object = JSONObject.fromObject(photo);
                    int photo_code = photo_object.getInt("CODE");
                    if(photo_code==1){
                        String URL = photo_object.getString("URL");  //另外一张表的信息
                        datas.append("URL:'").append(URL).append("'");
                    }else {
                        datas.append("URL:'").append("").append("'");
                    }

                    datas.append("}");
                    if (i != array.size() - 1) {
                        datas.append(",");
                    }
                }
                datas.append("]");

                StringBuilder r = new StringBuilder();
                r.append("{success:true,'msg':'success'");
                r.append(",");
                r.append(datas);
                r.append(",");
                r.append(columModles);
                r.append(",");
                r.append(fields);
                r.append("}");

                actionBase.actionEnd(response, r.toString(), result);

            } else {
                String ret = getCodeString(code);
                StringBuilder r = new StringBuilder();
                r.append("{success:false,'code':'"+ret+"'");
                r.append("}");
                actionBase.actionEnd(response, r.toString(), result);
            }
        }
            return null;
    }

    */
/**
     *
      * @param fwid
     * @param gmsfhm
     * @param yhm
     * @param mm
     * @param url
     * @return
     * @throws Exception
     *//*

    public static String getPeopleForGMSFHM(String fwid, String gmsfhm, String yhm, String mm, String url) throws Exception {
       return null;
    }

    */
/**
     *
     * @param fwid
     * @param xm
     * @param yhm
     * @param mm
     * @param url
     * @return
     * @throws Exception
     *//*

    public static String getPeopleForXM(String fwid, String xm, String yhm, String mm, String url) throws Exception {
        return null;
    }

    */
/**
     * 根据身份证获取照片信息
     * @param fwid
     * @param gmsfhm
     * @param yhm
     * @param mm
     * @param url
     * @return
     * @throws Exception
     *//*

    public static String getPhotoMsg(String fwid, String gmsfhm, String yhm, String mm, String url) throws Exception {
        return null;
    }
}
*/
