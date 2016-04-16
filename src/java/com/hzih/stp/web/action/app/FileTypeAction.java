package com.hzih.stp.web.action.app;

import com.google.protobuf.ByteString;
import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.hzih.stp.domain.*;
import com.hzih.stp.entity.TypeBase;
import com.hzih.stp.entity.TypeFile;
import com.hzih.stp.entity.TypeSafe;
import com.hzih.stp.service.*;
import com.hzih.stp.utils.*;
import com.hzih.stp.utils.code.RequestMessage;
import com.hzih.stp.utils.filter.KeywordsFilterFactory;
import com.hzih.stp.utils.filter.KeywordsFilterUtil;
import com.hzih.stp.utils.udp.UdpClientImpl;
import com.hzih.stp.web.SessionUtils;
import com.hzih.stp.web.action.ActionBase;
import com.hzih.stp.web.servlet.SiteContextLoaderServlet;
import com.inetec.common.config.stp.nodes.Plugin;
import com.inetec.common.config.stp.nodes.TargetFile;
import com.inetec.common.config.stp.nodes.Type;
import com.inetec.common.config.stp.nodes.SourceFile;
import com.inetec.common.exception.Ex;
import com.inetec.common.io.IOUtils;
import com.inetec.common.security.FileMd5;
import com.inetec.common.util.OSInfo;
import com.opensymphony.xwork2.ActionSupport;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 文件同步
 */
public class FileTypeAction extends ActionSupport {

    private final static Logger logger = Logger.getLogger(FileTypeAction.class);
    private LogService logService;
    private DeleteStatusService deleteStatusService;
    private AccountService accountService;
    private RoleService roleService;
    private XmlOperatorService xmlOperatorService;
    private ContentFilterService contentFilterService;
    private SecurityLevelService securityLevelService;
    private TypeBase typeBase;
    private TypeSafe typeSafe;
    private TypeFile typeFile;
    private String appName;
    private String plugin;
    private boolean privated;
    private String appDesc;
    private String appType;
    private String channel;
    private String channelport;
    private int speed;
    private Integer start;
    private Integer limit;
    private String[] fileNames;
    private String filePath;
    private String fileName;

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result = base.actionBegin(request);
        String msg = null;
        try{
            String interval = request.getParameter("interval");
            if(interval != null){
                typeFile.setInterval(interval);
            }
            typeBase = setTypeBase();
            xmlOperatorService.saveFileType(typeBase,typeFile);
            if(!typeBase.getPrivated()){
                xmlOperatorService.updateTypeAppSend(typeBase.getAppName(), StringContext.INSERT_APP);
            }

            DeleteStatus deleteStatus = new DeleteStatus();
            deleteStatus.setAppName(appName);

            if("1".equals(plugin)) {
                deleteStatus.setPlugin("external");
            } else if("2".equals(plugin)){
                deleteStatus.setPlugin("internal");
            } else {
                deleteStatus.setPlugin(plugin);
            }
            AccountSecurity as = accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId());
            deleteStatus.setFlagType(Integer.parseInt(as.getInfoLevel()));
            deleteStatus.setDeleteType(0);
            deleteStatusService.insertOrUpdate(deleteStatus);

            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","文件同步新增应用成功", 1);
            msg = "保存成功,点击[确定]返回列表";
        } catch (Exception e){
            logger.error("文件同步",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"文件同步","文件同步新增应用失败", 0);
            msg = "保存失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        typeBase = null;
        typeFile = null;
        base.actionEnd(response, json, result);
        return null;
    }

    private TypeBase setTypeBase() {
        TypeBase  typeBase =  new TypeBase();
        typeBase.setAppDesc(appDesc);
        typeBase.setAppName(appName);
        typeBase.setAppType(appType);
        typeBase.setActive(false);
        typeBase.setAllow(false);
        typeBase.setFilter(false);
        typeBase.setVirusScan(false);
        typeBase.setPlugin(plugin);
        typeBase.setPrivated(privated);
        typeBase.setChannel(channel);
        typeBase.setChannelport(channelport);
        typeBase.setSpeed(speed);
        return typeBase;
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result = base.actionBegin(request);
        String msg = null;
        try{
            String interval = request.getParameter("interval");
            if(interval != null){
                typeFile.setInterval(interval);
            }
            xmlOperatorService.updateFileType(typeBase, typeFile);
            if(!typeBase.getPrivated()){
                xmlOperatorService.updateTypeAppSend(typeBase.getAppName(),StringContext.UPDATE_APP);
            }
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","文件同步修改应用成功", 1);
            msg = "修改成功,点击[确定]返回列表";
        } catch (Exception e){
            logger.error("文件同步",e);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"文件同步","文件同步修改应用失败", 0);
            msg = "修改失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        typeBase = null;
        typeFile = null;
        base.actionEnd(response,json,result);
        return null;
    }

    public String delete() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result = base.actionBegin(request);
        String msg = null;
        try{
            int deleteType = Integer.parseInt(request.getParameter("deleteType"));
//            DeleteStatus deleteStatus = new DeleteStatus();
//            deleteStatus.setAppName(appName);
//            deleteStatus.setPlugin(plugin);
            deleteStatusService.updateToAllowDelete(appName);
            logger.info("删除应用成功!");
            msg = "删除应用成功";
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","配置用户删除文件同步应用"+appName+"成功,等待授权用户审批是否能删除!", 1);
        } catch (Exception e){
            logger.error("文件同步",e);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","配置用户删除文件同步应用"+appName+"失败,下次继续删除!", 0);
            msg = "删除提交失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        typeBase = null;
        typeFile = null;
        base.actionEnd(response,json,result);
        return null;
    }

    public String deletePolicy() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result = base.actionBegin(request);
        String msg = null;
        try{
            int deleteType = Integer.parseInt(request.getParameter("deleteType"));
//            DeleteStatus deleteStatus = new DeleteStatus();
//            deleteStatus.setAppName(appName);
//            deleteStatus.setPlugin(plugin);
//            deleteStatusService.updateToAllowDelete(appName);
            xmlOperatorService.deleteExternalTypeByName(appName,1);
            logger.info("删除策略成功!");
            msg = "删除策略成功";
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"采集策略","删除采集策略"+appName+"成功!", 1);
        } catch (Exception e){
            logger.error("采集策略",e);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"采集策略","删除采集策略"+appName+"失败,下次继续删除!", 0);
            msg = "删除提交失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        typeBase = null;
        typeFile = null;
        base.actionEnd(response,json,result);
        return null;
    }

    public String addDeleteStatus() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        request.getCharacterEncoding();
        String json =  null;
        try{
            String appNames = request.getParameter("appNames");
            String[] names = appNames.split(",");
            for (String appName : names) {
                DeleteStatus deleteStatus = new DeleteStatus();
                deleteStatus.setAppName(appName);
                deleteStatus.setPlugin("internal");
                deleteStatus.setFlagType(0);
                deleteStatus.setDeleteType(0);
                deleteStatusService.insertOrUpdate(deleteStatus);
            }
            json = "{success:true}";
        } catch (Exception e){
            logger.error("自动更新应用状态",e);
            json = "{success:false}";

        }
        actionBase.actionEnd(response, json);
        return null;
    }

    public String testConnect() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result = base.actionBegin(request);
        String msg = null;
        boolean isOk = false;
        try{
            String interval = request.getParameter("interval");
            if("ftp".equals(typeFile.getProtocol())||"ftpdown".equals(typeFile.getProtocol())){
                FTPClient client = null;
                try{
                    client = getFTPClient(typeFile.getServerAddress(), Integer.valueOf(typeFile.getPort()), typeFile.getUserName(), typeFile.getPassword(),typeFile.getCharset());
                    if(client!=null){
                        disconnect(client);
                        isOk = true;
                        logger.info("测试成功!");
                        msg = "测试成功,点击[确定]返回";
                        logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","测试所选"+typeFile.getProtocol()+"文件服务是否可以连通成功!", 1);
                    } else {
                        logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "文件同步", "测试所选" + typeFile.getProtocol() + "文件服务是否可以连通失败", 0);
                        msg = "测试失败";
                    }
                } catch (Exception e) {
                    logger.error("[FTP同步测试失败]",e);
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "文件同步", "测试所选" + typeFile.getProtocol() + "文件服务是否可以连通失败", 0);
                    msg = "测试失败,"+e.getMessage();
                }
            }else if("smb".equals(typeFile.getProtocol())||"smbdown".equals(typeFile.getProtocol())){
                SmbFile smbFile = getSmbFile(typeFile.getServerAddress(),Integer.valueOf(typeFile.getPort()),typeFile.getUserName(),typeFile.getPassword(),typeFile.getDir());
                if(smbFile!=null){
                    isOk = true;
                    if(typeFile.getBackUp() != null && typeFile.getBackUp() && typeFile.getBackUpDir() != null ){
                        SmbFile smbFile_backup = getSmbFile(typeFile.getServerAddress(),Integer.valueOf(typeFile.getPort()),typeFile.getUserName(),typeFile.getPassword(),typeFile.getBackUpDir());
                        if(smbFile_backup != null){
                            isOk = true;
                        }else{
                            isOk = false;
                        }
                    }
                    if(isOk){
                        logger.info("测试成功!");
                        msg = "测试成功,点击[确定]返回";
                        logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","测试所选"+typeFile.getProtocol()+"文件服务是否可以连通成功!", 1);
                    }else{
                        logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","测试所选"+typeFile.getProtocol()+"文件服务是否可以连通失败", 0);
                        msg = "测试失败";
                    }
                } else {
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","测试所选"+typeFile.getProtocol()+"文件服务是否可以连通失败", 0);
                    msg = "测试失败";
                }
            }else if("ftps".equals(typeFile.getProtocol())){
                logger.info("ftps未开通");
                msg = "测试失败,ftps服务未开通!";
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","测试所选"+typeFile.getProtocol()+"文件服务是否可以连通失败", 0);
            }else if("WebDAV".equals(typeFile.getProtocol())){
                String url = "http://"+typeFile.getServerAddress() + ":" + typeFile.getPort() + "/" +typeFile.getDir();
                List<DavResource> sourceDavResource = null;
                try {
                    Sardine sardine = SardineFactory.begin(typeFile.getUserName(),typeFile.getPassword());
                    if(sardine.exists(url)){
                        sourceDavResource = sardine.list(url);
                    }
                }catch (IOException e){
                    logger.error("***遍历服务器目录出错，连接失败或服务器目录设置出错!!!***",e);
                    msg = e.getMessage();
                }
                if(sourceDavResource!=null) {
                    isOk = true;
                    logger.info("测试成功!");
                    msg = "测试成功,点击[确定]返回";
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","测试所选"+typeFile.getProtocol()+"文件服务是否可以连通成功!", 1);
                } else {
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","测试所选"+typeFile.getProtocol()+"文件服务是否可以连通失败", 0);
                }
            } else {
                msg = "没有该通信协议的实现";
            }
        } catch (Exception e){
            logger.error("文件同步",e);
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件同步","测试所选"+typeFile.getProtocol()+"文件服务是否可以连通失败", 0);
            msg = "测试失败"+e.getMessage();
        }
        typeBase = null;
        typeFile = null;
        String json = "{success:true,msg:'"+msg+"',flag:"+isOk+"}";
        base.actionEnd(response,json,result);
        return null;
    }

    private FTPClient getFTPClient(String hostname, int port, String username, String password,String charset) throws IOException {
        FTPClient client = new FTPClient();
        client.connect(hostname, port);
        client.setControlEncoding(charset);
        if(FTPReply.isPositiveCompletion(client.getReplyCode())){
            if(client.login(username, password)){
                client.enterLocalPassiveMode();
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                return client;
            }
        }
        return null;
    }

    private void disconnect(FTPClient client) throws IOException{
        if(client.isConnected()){
            client.disconnect();
        }
    }

    private SmbFile getSmbFile(String hostname, int port, String username, String password,String dir){
        String url = "smb://" + username + ":" + password + "@" + hostname + ":" + port + dir;
        SmbFile smbFile = null;
        try {
            smbFile = new SmbFile(url);
            smbFile.connect();
            return smbFile;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            logger.error("[SMB同步测试失败]"+e.getMessage(),e);
        }
        return null;
    }

    public String queryByNameType() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = null;
        try{
            json = xmlOperatorService.queryByNameType(appName, appType);
            logger.info("读取配置文件的应用"+appName+"信息成功!");
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "业务运行监控", "读取配置文件的应用"+appName+"信息成功!", 1);
        }catch (Ex ex){
            logger.error("业务运行监控",ex);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"业务运行监控", "读取配置文件的应用"+appName+"信息失败!", 0);
        }
		base.actionEnd(response, json ,result);
		return null;
	}

    public String selectClient() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = "{success:true,total:0,rows:[]}";
        String dir = request.getParameter("dir");
        boolean isRemote = Boolean.parseBoolean(request.getParameter("isRemote"));
        try{
            int flagType = Integer.parseInt(accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId()).getInfoLevel());
            json = listClient(isRemote, dir, flagType, start, limit);
            logger.info("读取目录"+dir+"下文件列表成功!");
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "文件标记", "读取目录/data/flag/"+(dir==null?"":dir)+"下文件列表成功!", 1);
        }catch (Exception ex){
            logger.error("客户端读取文件列表",ex);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"文件标记", "读取目录/data/flag/"+(dir==null?"":dir)+"下文件列表失败", 0);
        }
		base.actionEnd(response, json ,result);
		return null;
	}

    public String selectResource() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = "{success:true,total:0,rows:[]}";
        String dir = null;
        boolean isRemote = Boolean.parseBoolean(request.getParameter("isRemote"));
        try{
            AccountSecurity accountSecurity = accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId());
            String appName = accountSecurity.getAppName();
            Type app = xmlOperatorService.readExternalTypeByAppName(appName);
            SourceFile sourceFile = xmlOperatorService.getSourceFile(appName);
            dir = sourceFile.getBackUpDir();
            File file;
            if(OSInfo.getOSInfo().isLinux()){
                file = new File("/data/resource/" + dir);
            } else {
                file = new File(System.getProperty("ichange.home")+"/data/resource/" + dir);
            }
            if(!file.exists()) {
                file.mkdirs();
            }
            if(!app.isActive()){
                logger.warn("策略"+appName+"未启用");
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(), "资源采集", "策略" + appName + "未启用,采集资源失败", 0);
            } else {
                try{
                    if(isRemote) {
                        NtlmPasswordAuthentication npa =
                                new NtlmPasswordAuthentication(sourceFile.getServerAddress(),
                                        sourceFile.getUserName(),sourceFile.getPassword());
                        String remoteDir = sourceFile.getDir();
                        if(!remoteDir.startsWith("/")) {
                            remoteDir = "/"+remoteDir + "/";
                        }
                        if(!remoteDir.endsWith("/")){
                            remoteDir += "/";
                        }
                        String url = "smb://" + sourceFile.getServerAddress() + ":" + sourceFile.getPort()  + remoteDir;
                        SmbFile smbFile = new SmbFile(url,npa);
                        SmbFile[] smbList = smbFile.listFiles(new SmbSendFileFilter(sourceFile));
                        for (SmbFile s : smbList){
                            File f = new File(file.getPath() + "/" + s.getName());
                            if(f.exists()) {    //TODO: need to check md5
                                continue;
                            }
                            InputStream in = new SmbFileInputStream(s);
                            OutputStream out = new FileOutputStream(f); //
                            FileUtil.copy(in,out);
                            out.close();
                            in.close();
                            if(sourceFile.isDeletefile()) {
                                s.delete();
                            }
                        }
                        logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源采集", "采集资源" + appName + "成功!", 1);
                    }
                } catch (Exception e){
                    logger.error("采集资源失败",e);
                    logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源采集", "采集资源" + appName + "失败", 0);
                }
            }
            File[] list = file.listFiles(new MyFilenameFilter(fileName));
            json = "{success:true,total:"+list.length+",rows:[";
            int index = 0;
            for(int i = 0 ; i < list.length ; i ++) {
                if(i==start&&index<limit){
                    start ++;
                    index ++;
                    String filePath ="/" + dir + "/"+ list[i].getName();
                    json += "{filePath:'"+filePath+"'},";
                }
            }
            json += "]}";
//            dir = sourceFile.getBackUpDir();
//            json = listResourceDir(isRemote,sourceFile, start, limit,SessionUtils.getAccount(request).getUserName(),appName);
            logger.info("读取目录"+dir+"下文件列表成功!");
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源采集", "打开资源列表失败成功!", 1);
        }catch (Exception ex){
            logger.error("资源采集",ex);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源采集", "打开资源列表失败", 0);
            json = "{success:true,total:0,rows:[]}";
        }
        base.actionEnd(response, json ,result);
		return null;
	}

    public String selectResourceByRemote() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=GBK");
		String json = "{success:true,total:0,rows:[]}";
        String dir = null;
        try{
            String permission = request.getParameter("permission");
            SourceFile sourceFile = xmlOperatorService.getSourceFile(appName);
            dir = sourceFile.getBackUpDir();
            File file;
            if(OSInfo.getOSInfo().isLinux()){
                file = new File("/data/resource/" + dir);
            } else {
                file = new File(System.getProperty("ichange.home")+"/data/resource/" + dir);
            }
            if(!file.exists()) {
                file.mkdirs();
            }
            File[] list = file.listFiles(new MyFilenameFilter(fileName));
            json = "{success:true,total:"+list.length+",rows:[";
            int index = 0;
            for(int i = 0 ; i < list.length ; i ++) {
                if(i==start&&index<limit){
                    start ++;
                    index ++;
                    String filePath ="/" + dir + "/"+ list[i].getName();
                    json += "{filePath:'"+filePath+"',permission:'"+permission+"'},";
                }
            }
            json += "]}";
//            dir = sourceFile.getBackUpDir();
//            json = listResourceDir(isRemote,sourceFile, start, limit,SessionUtils.getAccount(request).getUserName(),appName);
//            logger.info("读取目录"+dir+"下文件列表成功!");
//            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源采集", "读取目录" + (dir == null ? "" : dir) + "下文件列表成功!");
            response.getWriter().write(json);
            response.getWriter().close();
        }catch (Exception e){
            logger.error("远程资源访问",e);
//            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"资源采集", "读取目录"+(dir==null?"":dir)+"下文件列表失败!");
            json = "{success:true,total:0,rows:[]}";
            response.getWriter().write(e.getMessage());
            response.getWriter().close();
        }
		return null;
	}

    public String selectResourceExternal() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = "{success:true,total:0,rows:[]}";

        String dir = null;
        try{
            AccountSecurity accountSecurity = accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId());
            String appName = accountSecurity.getAppName();
            String permission = null;
            Set<Role> roleSet = SessionUtils.getAccount(request).getRoles();
            for (Role role : roleSet){
                long roleTypeId = role.getRoleType();
                RoleType roleType = roleService.getRoleType(roleTypeId);
                permission = roleType.getPermission();
            }
            String[][] params = new String[][] {
                        { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                        { "appName", appName },
                        { "permission", permission },
                        { "fileName", fileName },
                        { "start", String.valueOf(start)},
                        { "limit", String.valueOf(limit)}
                };
            ServiceResponse serviceResponse = ServiceUtil.callConsole("FileTypeAction_selectResourceByRemote.action", params);
            if (serviceResponse != null && serviceResponse.getCode() == 200){
                logger.info("响应成功" + serviceResponse.getData());
                json = serviceResponse.getData();
            } else {
                logger.info("查询失败:前置机("+(serviceResponse != null?"(响应错误码"+serviceResponse.getCode():"响应错误)"));
            }
            logger.info("读取目录"+dir+"下文件列表成功!");
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源访问", "访问资源列表成功!", 1);
        }catch (Exception ex){
            logger.error("资源访问",ex);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源访问", "访问资源列表失败", 0);
            json = "{success:true,total:0,rows:[]}";
        }
        base.actionEnd(response, json ,result);
		return null;
	}

    private String listResourceDir(boolean isRemote, SourceFile sourceFile, Integer start, Integer limit,String userName,String appName) throws Exception{
        String dir = sourceFile.getBackUpDir();
        File file;
        if(OSInfo.getOSInfo().isLinux()){
            file = new File("/data/resource/" + dir);
        } else {
            file = new File(System.getProperty("ichange.home")+"/data/resource/" + dir);
        }
        if(!file.exists()) {
            file.mkdirs();
        }
        if(isRemote) {
            NtlmPasswordAuthentication npa =
                    new NtlmPasswordAuthentication(sourceFile.getServerAddress(),
                            sourceFile.getUserName(),sourceFile.getPassword());
            String remoteDir = sourceFile.getDir();
            if(!remoteDir.startsWith("/")) {
                remoteDir = "/"+remoteDir + "/";
            }
            if(!remoteDir.endsWith("/")){
                remoteDir += "/";
            }
            String url = "smb://" + sourceFile.getServerAddress() + ":" + sourceFile.getPort()  + remoteDir;
            SmbFile smbFile = new SmbFile(url,npa);
            SmbFile[] smbList = smbFile.listFiles(new SmbSendFileFilter(sourceFile));
            for (SmbFile s : smbList){
                File f = new File(file.getPath() + "/" + s.getName());
                if(f.exists()) {    //TODO: need to check md5
                    continue;
                }
                InputStream in = new SmbFileInputStream(s);
                OutputStream out = new FileOutputStream(f); //
                FileUtil.copy(in,out);
                out.close();
                in.close();
            }
            logService.newManagerLog("INFO", userName, "资源采集", "采集资源"+appName+"成功!", 1);
        }
        File[] list = file.listFiles();
        String json = "{success:true,total:"+list.length+",rows:[";
        int index = 0;
        for(int i = 0 ; i < list.length ; i ++) {
            if(i==start&&index<limit){
                start ++;
                index ++;
                String filePath ="/" + dir + "/"+ list[i].getName();
                json += "{filePath:'"+filePath+"'},";
            }
        }
        json += "]}";
        return json;
    }

    public String checkResource() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
        String msg = null;

        try{
            AccountSecurity accountSecurity = accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId());
            String appName = accountSecurity.getAppName();
            Type app = xmlOperatorService.readExternalTypeByAppName(appName);
            if(app.isActive()){
                msg = "000";
            } else {
                msg = "策略" + appName + "未启用";
            }

//            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源采集", "删除成功!");
        }catch (Exception ex){
            logger.error("资源采集",ex);
//            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"资源采集", "删除失败!");
            msg = "资源采集失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
		return null;
    }

    public String deleteResource() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
        String msg = null;

        try{
//            AccountSecurity accountSecurity = accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId());
//            String appName = accountSecurity.getAppName();
//            SourceFile sourceFile = xmlOperatorService.getSourceFile(appName);
//            dir = sourceFile.getBackUpDir();
//            listResourceDir();
            String permission = null;
            Set<Role> roleSet = SessionUtils.getAccount(request).getRoles();
            for (Role role : roleSet){
                long roleTypeId = role.getRoleType();
                RoleType roleType = roleService.getRoleType(roleTypeId);
                permission = roleType.getPermission();
            }
            boolean isAllowDelete = false;
            if(permission!=null&&permission.indexOf("delete")>-1){
                isAllowDelete = true;
            }
            if(!isAllowDelete) {
                msg = "删除失败,没有删除权限";
                logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源采集", msg, 0);
            } else {
                String dir;
                if(OSInfo.getOSInfo().isLinux()){
                    dir = "/data/resource";
                } else {
                    dir = System.getProperty("ichange.home")+"/data/resource";
                }
                for (String f : fileNames) {
                    File file = new File(dir + f);
                    file.delete();
                    logger.info("删除文件" + file.getPath());
                }
                msg = "删除成功";
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源采集", msg, 1);
            }
        }catch (Exception ex){
            logger.error("资源采集",ex);
            msg = "删除失败";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源采集", "删除失败" + ex.getMessage(), 0);
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
		return null;
    }

    public String deleteResourceByRemote() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
        try{
            String dir;
            if(OSInfo.getOSInfo().isLinux()){
                dir = "/data/resource";
            } else {
                dir = System.getProperty("ichange.home")+"/data/resource";
            }
            String[] fileNames = request.getParameter("fileName").split(",");
            for (String f : fileNames) {
                File file = new File(dir + f);
                file.delete();
                logger.info("删除文件" + file.getPath());
            }
            response.getWriter().write("ok");
            response.getWriter().close();
//            msg = "删除成功";
//            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源采集", msg);
        }catch (Exception e){
            logger.error("资源采集",e);
//            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"资源采集", "删除失败!");
//            msg = "删除失败";
            response.getWriter().write(e.getMessage());
            response.getWriter().close();
        }

		return null;
    }

    public String deleteResourceExternal() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
        String msg = null;

        try{
            String permission = null;
            Set<Role> roleSet = SessionUtils.getAccount(request).getRoles();
            for (Role role : roleSet){
                long roleTypeId = role.getRoleType();
                RoleType roleType = roleService.getRoleType(roleTypeId);
                permission = roleType.getPermission();
            }
            boolean isAllowDelete = false;
            if(permission!=null&&permission.indexOf("delete")>-1){
                isAllowDelete = true;
            }
            if(!isAllowDelete) {
                msg = "没有删除权限";
                logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源访问", msg, 0);
            } else {
                String fieNameStr = "";
                for (int i= 0;i< fileNames.length;i++) {
                    String fieName = fileNames[i];
                    if(i<fileNames.length -1){
                        fieNameStr += fieName + ",";
                    } else {
                        fieNameStr += fieName;
                    }
                }
                String[][] params = new String[][] {
                        { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                        { "fileName", fieNameStr }
                };
                ServiceResponse serviceResponse = ServiceUtil.callConsole("FileTypeAction_deleteResourceByRemote.action", params);
                if (serviceResponse != null && serviceResponse.getCode() == 200){
                    logger.info("响应成功" + serviceResponse.getData());
                    msg = "删除成功";
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源访问", msg, 1);
                } else {
                    logger.info("响应失败" + serviceResponse.getData());
                    msg = "删除失败:前置机("+(serviceResponse != null?"(响应错误码"+serviceResponse.getCode():"响应错误)");
                    logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源访问", msg, 0);
                }
            }
        }catch (Exception ex){
            logger.error("资源访问",ex);
            msg = "删除失败";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"资源访问", msg + ":" + ex.getMessage(), 0);
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
		return null;
    }

    public String reNameResourceByRemote() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        try{
            String dir;
            if(OSInfo.getOSInfo().isLinux()){
                dir = "/data/resource";
            } else {
                dir = System.getProperty("ichange.home")+"/data/resource";
            }
            String newPath = request.getParameter("newPath");
            File file = new File(dir + filePath);
            File newFile = new File(dir + newPath);
            file.renameTo(newFile);
            logger.info("重命名文件" + file.getPath() + "成为" + newFile.getPath());
            response.getWriter().write("ok");
            response.getWriter().close();
//            msg = "删除成功";
//            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源采集", msg);
        }catch (Exception e){
            logger.error("资源采集",e);
//            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"资源采集", "删除失败!");
//            msg = "删除失败";
            response.getWriter().write(e.getMessage());
            response.getWriter().close();
        }

        return null;
    }

    public String reNameResourceExternal() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
        String msg = null;
        String newPath = request.getParameter("newPath");

        try{
            String permission = null;
            Set<Role> roleSet = SessionUtils.getAccount(request).getRoles();
            for (Role role : roleSet){
                long roleTypeId = role.getRoleType();
                RoleType roleType = roleService.getRoleType(roleTypeId);
                permission = roleType.getPermission();
            }
            boolean isAllowRename = false;
            if(permission!=null&&permission.indexOf("rename")>-1){
                isAllowRename = true;
            }
            if(!isAllowRename) {
                msg = "没有重命名权限";
                logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源访问", msg, 0);
            } else {

                String[][] params = new String[][] {
                        { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                        { "filePath", filePath },
                        { "newPath", newPath }
                };
                ServiceResponse serviceResponse = ServiceUtil.callConsole("FileTypeAction_reNameResourceByRemote.action", params);
                if (serviceResponse != null && serviceResponse.getCode() == 200){
                    logger.info("响应成功" + serviceResponse.getData());
                    msg = "重命名成功";
                    logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源访问", "重命名" + filePath + "成为" + newPath + "成功!", 1);
                } else {
                    logger.info("响应失败" + serviceResponse.getData());
                    msg = "重命名失败:前置机("+(serviceResponse != null?"(响应错误码"+serviceResponse.getCode():"响应错误)");
                    logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源访问", msg, 0);
                }
            }
        }catch (Exception ex){
            logger.error("资源访问",ex);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "资源访问", "重命名" + filePath + "成为" + newPath + "失败:" + ex.getMessage(), 0);
            msg = "重命名失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
		return null;
    }

    private String listClient(boolean isRemote, String dir, int level, int start, int limit) throws Exception {
        File file;
        if(OSInfo.getOSInfo().isLinux()){
            file = new File("/data/flag");
        } else {
            file = new File("F:/stp/data/flag");
        }
        if(!file.exists()){
            file.mkdirs();
        }

        if(isRemote) {
//            List<Type> types = xmlOperatorService.readType("external",Type.s_app_file);
//            SourceFile sourceFile = null;
//            for (Type t : types) {
//                if(level == t.getInfoLevel()){
//                    sourceFile = xmlOperatorService.getSourceFile(t.getTypeName());
//                }
//            }
            Configuration config = new Configuration(StringContext.EXTERNALXML);
            SourceFile sourceFile = config.getSourceFile("flag", Plugin.s_source_plugin);
            if(sourceFile == null) {
                return "{success:true,total:0,rows[]}";
            }
//          SourceFile sourceFile = xmlOperatorService.getSourceFile(appName);
            NtlmPasswordAuthentication npa =
                    new NtlmPasswordAuthentication(sourceFile.getServerAddress(),
                            sourceFile.getUserName(),sourceFile.getPassword());
            String url;
            if(dir!=null){
                if(!dir.startsWith("/")) {
                    dir = "/"+dir + "/";
                }
                if(!dir.endsWith("/")){
                    dir += "/";
                }
                url = "smb://" + sourceFile.getServerAddress() + ":" + sourceFile.getPort() + sourceFile.getDir() + dir;
            } else {
                dir = "/";
                url = "smb://" + sourceFile.getServerAddress() + ":" + sourceFile.getPort() + sourceFile.getDir() + dir;
            }
            SmbFile smbFile = new SmbFile(url,npa);
            SmbFile[] smbList = smbFile.listFiles(new SmbSendFileFilter(sourceFile));


            for (SmbFile s : smbList){
                File f = new File(file.getPath() + "/" + s.getName());
                if(f.exists()) {    //TODO: need to check md5
                    continue;
                }
                InputStream in = new SmbFileInputStream(s);
                OutputStream out = new FileOutputStream(f); //
                FileUtil.copy(in,out);
                out.close();
                in.close();
            }
        }

        File[] list = file.listFiles(new AllowFileFilter(level));
        String json = "{success:true,total:"+list.length+",rows:[";
        int index = 0;
        for(int i = 0 ; i < list.length ; i ++) {
            if(i==start&&index<limit){
                start ++;
                index ++;
                String filePath = list[i].getPath();
                if(!OSInfo.getOSInfo().isLinux()) {
                    filePath = filePath.replace("\\","/");
                }
                int l = makeL(filePath);
                json +="{filePath:'"+filePath+"',level:"+l+",accountLevel:"+level+",flag:1},";
            }
        }
        json += "]}";
        return json;
    }


    private String listClientTarget(boolean isRemote, String dir, int level, int start, int limit) throws Exception {

//        List<Type> types = xmlOperatorService.readType("internal",Type.s_app_file);
        Configuration config = new Configuration(StringContext.INTERNALXML);
        TargetFile targetFile = config.getTargetFile("flag", Plugin.s_target_plugin);
//        TargetFile targetFile = null;
//        for (Type t : types) {
//            if(level == t.getInfoLevel()){
//                targetFile = xmlOperatorService.getTargetFile(t.getTypeName());
//            }
//        }

        if(targetFile != null) {
            NtlmPasswordAuthentication npa =
                    new NtlmPasswordAuthentication(targetFile.getServerAddress(),
                            targetFile.getUserName(),targetFile.getPassword());
            String url = "smb://" + targetFile.getServerAddress() + ":" + targetFile.getPort() + targetFile.getDir();
            if(!url.endsWith("/")){
                url += "/";
            }
            SmbFile smbFile = new SmbFile(url,npa);
            SmbFile[] list = smbFile.listFiles(new AllowFileFilterSmb(level));

            String json = "{success:true,total:"+list.length+",rows:[";
            int index = 0;
            for(int i = 0 ; i < list.length ; i ++) {
                if(i==start&&index<limit){
                    start ++;
                    index ++;
                    String filePath = list[i].getPath();
                    if(!OSInfo.getOSInfo().isLinux()) {
                        filePath = filePath.replace("\\","/");
                    }
                    int l = makeL(filePath);
                    json +="{filePath:'"+filePath+"',level:"+l+",accountLevel:"+level+",flag:1},";
                }
            }
            json += "]}";
            return json;
        } else {
            File file;
            if(OSInfo.getOSInfo().isLinux()){
                file = new File("/data/flag2");
            } else {
                file = new File("F:/stp/data/flag");
            }
            if(!file.exists()) {
                file.mkdirs();
            }
            File[] list = file.listFiles(new AllowFileFilter(level));
            String json = "{success:true,total:"+list.length+",rows:[";
            int index = 0;
            for(int i = 0 ; i < list.length ; i ++) {
                if(i==start&&index<limit){
                    start ++;
                    index ++;
                    String filePath = list[i].getPath();
                    if(!OSInfo.getOSInfo().isLinux()) {
                        filePath = filePath.replace("\\","/");
                    }
                    int l = makeL(filePath);
                    json +="{filePath:'"+filePath+"',level:"+l+",accountLevel:"+level+",flag:1},";
                }
            }
            json += "]}";
            return json;
        }
    }

    private int makeL(String filePath) {
        if(filePath.endsWith("_cx")) {
            String end = filePath.substring(filePath.lastIndexOf("."));
            return Integer.parseInt(end.substring(1,2));
        }
        return 0;
    }

    public String updateClient() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = "{success:true,msg:''}";
        boolean isFilter = Boolean.parseBoolean(request.getParameter("isFilter"));
        boolean isVirusScan = Boolean.parseBoolean(request.getParameter("isVirusScan"));
        boolean isDelete = Boolean.parseBoolean(request.getParameter("isDelete"));
        int infoLevel = Integer.parseInt(request.getParameter("infoLevel"));
        String filePath = request.getParameter("filePath");
        int accountLevel = Integer.parseInt(request.getParameter("accountLevel"));
        InputStream is = null;
        FileOutputStream out = null;
        try{

            if(infoLevel > accountLevel){
                json = "{success:true,msg:'权限不足'}";
            } else {

                if(isVirusScan) {

                }
                if(isFilter) {
                    InputStream in = new FileInputStream(filePath);
                    KeywordsFilterUtil keywordsFilterUtil = KeywordsFilterFactory.getKeywordsFilterUtil(filePath);
                    String keyWords = contentFilterService.getAllToString();
                    try {
                        is = keywordsFilterUtil.filter(in,keyWords);
                    } catch (Exception e) {
                        logger.error("过滤错误",e);
                    } finally {
                        if(in!=null){
                            try {
                                in.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                } else {
                    is = new FileInputStream(filePath);
                }
                InputStream is2 = null;
                if(infoLevel>0) {
                    out = new FileOutputStream(filePath + "." + infoLevel + "_cx");
                    String security = securityLevelService.findByLevelInfo(infoLevel).getSecurityFlag();
                    is2 = SecurityUtils.encrypt(security, infoLevel, is);
                    is.close();
                    org.apache.commons.io.IOUtils.copy(is2, out);
                    out.close();
                    if(isDelete){
                        new File(filePath).delete();
                    }
                } else {
                    out = new FileOutputStream(filePath);
                    org.apache.commons.io.IOUtils.copy(is,out);
                }
                json = "{success:true,msg:'标记成功'}";
            }
        } catch (Exception ex){
            logger.error("客户端标记文件失败",ex);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"文件标记", "标记文件"+filePath+"失败", 0);
        } finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            try{
                if(isDelete) {
                    boolean isSuccess = new File(filePath).delete();
                    if(isSuccess){
                        logger.info("删除" + filePath + "成功");
                    } else {
                        logger.info("删除" + filePath + "失败");
                    }
                }
            } catch (Exception e) {
                logger.error("删除"+filePath+"失败",e);
            }
        }
        base.actionEnd(response, json ,result);
		return null;
	}

    public String sendToTarget() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = "{success:true,msg:''}";
//        boolean isFilter = Boolean.parseBoolean(request.getParameter("isFilter"));
//        boolean isVirusScan = Boolean.parseBoolean(request.getParameter("isVirusScan"));
        boolean isDelete = Boolean.parseBoolean(request.getParameter("isDelete"));
//        int infoLevel = Integer.parseInt(request.getParameter("infoLevel"));
        String filePath = request.getParameter("filePath");
//        int accountLevel = Integer.parseInt(request.getParameter("accountLevel"));
        InputStream is = null;
        FileOutputStream out = null;
        UdpClientImpl client = null;
        try{
                File file = new File(filePath);
                String md5 = FileMd5.getFileMD5String(file);
//                String md5 = "ddddddd";

                InetSocketAddress remote = ServiceUtil.addressMap.get("t");
                InetSocketAddress local = ServiceUtil.addressMap.get("s");

                client = new UdpClientImpl(SessionUtils.getAccount(request).getUserName(),remote,local);
                client.start();

                is = new FileInputStream(file);

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
                    message.setFileCode((byte) 0);
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
                json = "{success:true,msg:'发送成功'}";
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件标记", "标记发送"+filePath+"成功!", 1);

        } catch (Exception ex){
            logger.error("客户端发送文件"+filePath+"失败",ex);
            json = "{success:true,msg:'发送失败'}";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "文件标记", "标记发送" + filePath + "失败", 0);
        } finally {
            if(client != null){
                client.close();
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            try{
                if(isDelete) {
                    boolean isSuccess = new File(filePath).delete();
                    if(isSuccess){
                        logger.info("删除" + filePath + "成功");
                    } else {
                        logger.info("删除" + filePath + "失败");
                    }
                }
            } catch (Exception e) {
                logger.error("删除" + filePath + "失败", e);
            }
        }
        base.actionEnd(response, json ,result);
		return null;
	}

    public String sendToTarget_old() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = "{success:true,msg:''}";
//        boolean isFilter = Boolean.parseBoolean(request.getParameter("isFilter"));
//        boolean isVirusScan = Boolean.parseBoolean(request.getParameter("isVirusScan"));
        boolean isDelete = Boolean.parseBoolean(request.getParameter("isDelete"));
        int infoLevel = Integer.parseInt(request.getParameter("infoLevel"));
        String filePath = request.getParameter("filePath");
        int accountLevel = Integer.parseInt(request.getParameter("accountLevel"));
        InputStream is = null;
        FileOutputStream out = null;
        UdpClientImpl client = null;
        try{
            if(infoLevel > accountLevel){
                json = "{success:true,msg:'权限不足'}";
                logService.newManagerLog("WARN", SessionUtils.getAccount(request).getUserName(),"文件标记", "标记发送"+filePath+"失败,权限不足!", 0);
            } else {
                File file = new File(filePath);
                String md5 = FileMd5.getFileMD5String(file);
//                String md5 = "ddddddd";

                InetSocketAddress remote = ServiceUtil.addressMap.get("t"+accountLevel);
                InetSocketAddress local = ServiceUtil.addressMap.get("s"+accountLevel);

                client = new UdpClientImpl(SessionUtils.getAccount(request).getUserName(),remote,local);
                client.start();

                is = new FileInputStream(file);

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
                    message.setFileCode((byte) 0);
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
                json = "{success:true,msg:'发送成功'}";
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(),"文件标记", "标记发送"+filePath+"成功!", 1);
            }
        } catch (Exception ex){
            logger.error("客户端发送文件"+filePath+"失败",ex);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(), "文件标记", "标记发送" + filePath + "失败", 0);
        } finally {
            if(client != null){
                client.close();
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            try{
                if(isDelete) {
                    boolean isSuccess = new File(filePath).delete();
                    if(isSuccess){
                        logger.info("删除" + filePath + "成功");
                    } else {
                        logger.info("删除" + filePath + "失败");
                    }
                }
            } catch (Exception e) {
                logger.error("删除" + filePath + "失败", e);
            }
        }
        base.actionEnd(response, json ,result);
		return null;
	}

    public String selectClientDown() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = "{success:true,total:0,rows:[]}";
        String dir = request.getParameter("dir");
        try{
//            if(dir != null && new File(dir).isDirectory()) {
                int flagType = Integer.parseInt(accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId()).getInfoLevel());
                json = listClientTarget(true, dir, flagType, start, limit);
                logger.info("读取目录"+dir+"下文件列表成功!");
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "文件标记", "读取目录"+dir+"下文件列表成功!", 1);
//            } else {
//                if(dir == null) {
//                    json = "{success:false,msg:'没有指定目录'}";
//                } else {
//                    json = "{success:false,msg:'目录不存在'}";
//                }
//            }
        }catch (Exception ex){
            logger.error("客户端读取文件列表",ex);
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"文件标记", "读取目录"+dir+"下文件列表失败", 0);
        }
		base.actionEnd(response, json ,result);
		return null;
	}

    public String downByRemote() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        InputStream in = null;
        try{
            String dir;
            if(OSInfo.getOSInfo().isLinux()){
                dir = "/data/resource";
            } else {
                dir = System.getProperty("ichange.home")+"/data/resource";
            }
            File file = new File(dir + filePath);
            in = new FileInputStream(file);
            response.reset();
            response.setBufferSize(5*1024*1024);
            FileUtil.copy(in, response);
//            msg = "删除成功";
//            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源采集", msg);
        }catch (Exception e){
            logger.error("资源远程下载",e);
//            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"资源采集", "删除失败!");
//            msg = "删除失败";
            response.getWriter().write(e.getMessage());
            response.getWriter().close();
        }

        return null;
    }

    public String downloadExternal() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false}";

        InputStream in = null;
        try {
            String Agent = request.getHeader("User-Agent");
            StringTokenizer st = new StringTokenizer(Agent,";");
//            st.nextToken();
            //得到用户的浏览器名  MSIE  Firefox
            String userBrowser = st.nextToken();
            String name = filePath.substring(filePath.lastIndexOf("/")+1);
            String[][] params = new String[][] {
                    { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                    { "filePath", filePath }
            };
            ServiceResponse serviceResponse = ServiceUtil.callConsole2("FileTypeAction_downByRemote.action", params);
            if (serviceResponse != null && serviceResponse.getCode() == 200){
                in = serviceResponse.getInputStream();
            } else {
                String msg = "前置机("+(serviceResponse != null?"(响应错误码"+serviceResponse.getCode():"响应错误)");
                logger.info("下载失败:");
                logService.newManagerLog("ERROE", SessionUtils.getAccount(request).getUserName(), "文件下载", "用户下载文件" + filePath + "失败:" + msg, 0);
            }
            if(in!=null) {
                FileUtil.downType(response,name,userBrowser);
                response = FileUtil.copy(in, response);
                logger.info("下载" + filePath + "成功!");
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "文件下载", "用户下载文件" + filePath + "成功", 1);
            }
        } catch (Exception e) {
            logger.error("文件下载", e);
            logService.newManagerLog("ERROE", SessionUtils.getAccount(request).getUserName(), "文件下载", "用户下载文件" + filePath + "失败", 0);
        }
//        actionBase.actionEnd(response,json);
        return null;
    }

    public String download() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false}";
        String filePath = request.getParameter("filePath");
        int level = Integer.parseInt(request.getParameter("level"));
        int accountLevel = Integer.parseInt(request.getParameter("accountLevel"));
        FileOutputStream out = null;
        InputStream in = null;
        try {
            String Agent = request.getHeader("User-Agent");
            StringTokenizer st = new StringTokenizer(Agent,";");
//            st.nextToken();
            //得到用户的浏览器名  MSIE  Firefox
            String userBrowser = st.nextToken();
            String name;

//            List<Type> types = xmlOperatorService.readType("internal",Type.s_app_file);
//            TargetFile targetFile = null;
//            for (Type t : types) {
//                if(accountLevel == t.getInfoLevel()){
//                    targetFile = xmlOperatorService.getTargetFile(t.getTypeName());
//                }
//            }
            Configuration config = new Configuration(StringContext.INTERNALXML);
            TargetFile targetFile = config.getTargetFile("flag", Plugin.s_target_plugin);
            File file;
            if(targetFile != null) {
                NtlmPasswordAuthentication npa =
                        new NtlmPasswordAuthentication(targetFile.getServerAddress(),
                                targetFile.getUserName(),targetFile.getPassword());
//                String url = "smb://" + targetFile.getServerAddress() + ":" + targetFile.getPort() + targetFile.getDir();
//                if(!url.endsWith("/")){
//                    url += "/";
//                }
                SmbFile smbFile = new SmbFile(filePath,npa);
                InputStream sin = new SmbFileInputStream(smbFile);
                String tmpFile = System.getProperty("ichange.home")+ "/tomcat/temp/"+smbFile.getName();
                OutputStream sout = new FileOutputStream(tmpFile);
                FileUtil.copy(sin,sout);
                file = new File(tmpFile);
            } else {
                file = new File(filePath);
            }
            if(level>0) {
                InputStream is = new FileInputStream(file);
                String security = securityLevelService.findByLevelInfo(level).getSecurityFlag();
                in = SecurityUtils.decrypt(security, level, is);
                is.close();
                name = getDownLoadFileName(file.getName());
            } else {
                in = new FileInputStream(filePath);
                name = file.getName();
            }
            FileUtil.downType(response,name,userBrowser);
            response = FileUtil.copy(in, response);
            logger.info("下载" + filePath + "成功!");
            if(targetFile != null) {
                file.delete();
            }
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "文件下载", "用户下载文件" + filePath + "成功", 1);
            json = "{success:true}";
        } catch (Exception e) {
            logger.error("文件下载", e);
            logService.newManagerLog("ERROE", SessionUtils.getAccount(request).getUserName(), "文件下载","用户下载文件"+filePath+"失败", 0);
        }
//        actionBase.actionEnd(response,json);
        return null;
    }

    private String getDownLoadFileName(String name) {
        return name.substring(0,name.lastIndexOf("."));
    }

    public String listen() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        String dir = request.getParameter("dir");

        boolean isSuccess = false;
        try{
            /*int flagType = Integer.parseInt(accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId()).getInfoLevel());
            List<Type> types = xmlOperatorService.readType("internal",Type.s_app_file);
            TargetFile targetFile = null;
            for (Type t : types) {
                if(flagType == t.getInfoLevel()){
                    targetFile = xmlOperatorService.getTargetFile(t.getTypeName());
                }
            }
            if(targetFile == null) {
                if(dir != null ) {
                    File d = new File(dir);
                    if(d.exists()) {
                        if(d.isFile()) {
                            msg = "不是一个目录";
                        } else {
                            InetSocketAddress target = ServiceUtil.addressMap.get("t"+flagType);
                            boolean isOk = startUdpServer(flagType,target,dir,targetFile);
                            if(isOk){
                                msg = "启动接收成功";
                            } else {
                                msg = "不能重复启动";
                            }
                            isSuccess = true;
                        }
                    } else {
                        msg = "目录不存在";

                    }
                } else {
                    msg = "目录不能为空";
                }
            } else {
                InetSocketAddress target = ServiceUtil.addressMap.get("t" + flagType);
                boolean isOk = startUdpServer(flagType,target,dir,targetFile);
                if(isOk){
                    msg = "启动接收成功";
                } else {
                    msg = "不能重复启动";
                }
                isSuccess = true;
            }*/
            SiteContextLoaderServlet.uploadService.isStop(false);
            isSuccess = true;
            if(!isSuccess){
                msg = "启动接收失败";
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "文件浏览","启动接收失败", 0);
            } else {
                msg = "启动接收成功";
                logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "文件浏览","启动接收成功", 1);
            }

        }catch (Exception ex){
            logger.error("文件浏览启动接收失败",ex);
            msg = "启动接收失败 "+ex.getMessage();
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"文件浏览", "启动接收失败:" + ex.getMessage(), 0);
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
		return null;
	}

    public String listenStop() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        boolean isSuccess = false;
        try{
//            int flagType = Integer.parseInt(accountService.selectAccountSecurity(SessionUtils.getAccount(request).getId()).getInfoLevel());
//            InetSocketAddress target = ServiceUtil.addressMap.get("t"+flagType);
//            boolean isOk = stopUdpServer(flagType);
            SiteContextLoaderServlet.uploadService.isStop(true);
            boolean isOk = false;
            if(!isOk){
                msg = "停止接收成功";
            } else {
                msg = "停止接收失败";
            }
            logService.newManagerLog("INFO", SessionUtils.getAccount(request).getUserName(), "文件浏览",msg, 1);

        }catch (Exception ex){
            logger.error("文件浏览停止接收失败",ex);
            msg = "停止接收失败";
            logService.newManagerLog("ERROR", SessionUtils.getAccount(request).getUserName(),"文件浏览", "停止接收失败:" + ex.getMessage(), 0);
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
		return null;
	}

    /*private boolean startUdpServer(int flagType, InetSocketAddress target, String dir, TargetFile targetFile) {
        boolean isOK = false;
        switch (flagType) {
            case 0:
                if(isUdpServer0){
                    if(!udpServer0.isOk()) {
                        udpServer0.startUp(target,dir,targetFile);
                    }
                } else {
                    udpServer0.init();
                    new Thread(udpServer0).start();
                    isUdpServer0 = true;
                    udpServer0.startUp(target,dir, targetFile);
                }
                isOK = udpServer0.isOk();
                break;
            case 1:
                if(isUdpServer1){
                    if(!udpServer1.isOk()) {
                        udpServer1.startUp(target,dir, targetFile);
                    }
                } else {
                    udpServer1.init();
                    new Thread(udpServer1).start();
                    isUdpServer1 = true;
                    udpServer1.startUp(target,dir, targetFile);
                }
                isOK = udpServer1.isOk();
                break;
            case 2:
                if(isUdpServer2){
                    if(!udpServer2.isOk()) {
                        udpServer2.startUp(target,dir, targetFile);
                    }
                } else {
                    udpServer2.init();
                    new Thread(udpServer2).start();
                    isUdpServer2 = true;
                    udpServer2.startUp(target,dir, targetFile);
                }
                isOK = udpServer2.isOk();
                break;
            case 3:
                if(isUdpServer3){
                    if(!udpServer3.isOk()) {
                        udpServer3.startUp(target,dir, targetFile);
                    }
                } else {
                    udpServer3.init();
                    new Thread(udpServer3).start();
                    isUdpServer3 = true;
                    udpServer3.startUp(target,dir, targetFile);
                }
                isOK = udpServer3.isOk();
                break;
            case 4:
                if(isUdpServer4){
                    if(!udpServer4.isOk()) {
                        udpServer4.startUp(target,dir, targetFile);
                    }
                } else {
                    udpServer4.init();
                    new Thread(udpServer4).start();
                    isUdpServer4 = true;
                    udpServer4.startUp(target,dir, targetFile);
                }
                isOK = udpServer4.isOk();
                break;
            case 5:
                if(isUdpServer5){
                    if(!udpServer5.isOk()) {
                        udpServer5.startUp(target,dir, targetFile);
                    }
                } else {
                    udpServer5.init();
                    new Thread(udpServer5).start();
                    isUdpServer5 = true;
                    udpServer5.startUp(target,dir, targetFile);
                }
                isOK = udpServer5.isOk();
                break;
        }
        return isOK;
    }*/

    /*private boolean stopUdpServer(int flagType) {
        boolean isOk = false;
        switch (flagType) {
            case 0:
                udpServer0.closeUp();
                isOk = udpServer0.isOk();
                break;
            case 1:
                udpServer1.closeUp();
                isOk = udpServer1.isOk();
                break;
            case 2:
                udpServer2.closeUp();
                isOk = udpServer2.isOk();
                break;
            case 3:
                udpServer3.closeUp();
                isOk = udpServer3.isOk();
                break;
            case 4:
                udpServer4.closeUp();
                isOk = udpServer4.isOk();
                break;
            case 5:
                udpServer5.closeUp();
                isOk = udpServer5.isOk();
                break;
        }
        return isOk;
    }*/


//    public static UdpServer udpServer0 = new UdpServer();
//    public static boolean isUdpServer0 = false;
//    public static UdpServer udpServer1 = new UdpServer();
//    public static boolean isUdpServer1 = false;
//    public static UdpServer udpServer2 = new UdpServer();
//    public static boolean isUdpServer2 = false;
//    public static UdpServer udpServer3 = new UdpServer();
//    public static boolean isUdpServer3 = false;
//    public static UdpServer udpServer4 = new UdpServer();
//    public static boolean isUdpServer4 = false;
//    public static UdpServer udpServer5 = new UdpServer();
//    public static boolean isUdpServer5 = false;


    public void setSecurityLevelService(SecurityLevelService securityLevelService) {
        this.securityLevelService = securityLevelService;
    }

    public void setContentFilterService(ContentFilterService contentFilterService) {
        this.contentFilterService = contentFilterService;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public TypeBase getTypeBase() {
        return typeBase;
    }

    public void setTypeBase(TypeBase typeBase) {
        this.typeBase = typeBase;
    }

    public TypeSafe getTypeSafe() {
        return typeSafe;
    }

    public void setTypeSafe(TypeSafe typeSafe) {
        this.typeSafe = typeSafe;
    }

    public TypeFile getTypeFile() {
        return typeFile;
    }

    public void setTypeFile(TypeFile typeFile) {
        this.typeFile = typeFile;
    }

    public DeleteStatusService getDeleteStatusService() {
        return deleteStatusService;
    }

    public void setDeleteStatusService(DeleteStatusService deleteStatusService) {
        this.deleteStatusService = deleteStatusService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public XmlOperatorService getXmlOperatorService() {
        return xmlOperatorService;
    }

    public void setXmlOperatorService(XmlOperatorService xmlOperatorService) {
        this.xmlOperatorService = xmlOperatorService;
    }

    public boolean isPrivated() {
        return privated;
    }

    public void setPrivated(boolean privated) {
        this.privated = privated;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelport() {
        return channelport;
    }

    public void setChannelport(String channelport) {
        this.channelport = channelport;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
