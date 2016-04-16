package com.hzih.stp.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.stp.dao.IptablesDao;
import com.hzih.stp.domain.Iptables;
import com.hzih.stp.service.IptablesService;
import com.hzih.stp.utils.StringContext;
import com.hzih.stp.utils.StringUtils;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-30
 * Time: 下午3:34
 * To change this template use File | Settings | File Templates.
 */
public class IptablesServiceImpl implements IptablesService {
    private static final Logger logger = Logger.getLogger(IptablesServiceImpl.class);
    private IptablesDao iptablesDao;
    //todo
//    private static String iptables_file =  StringContext.systemPath + "/others/firewall.sh";
    private static String iptables_file = StringUtils.getEtc() + "/init.d/firewall.sh";
    private static String ssh_allow_file = StringUtils.getEtc() + "/hosts.allow";
    private static String webmin_allow_file = StringUtils.getEtc() + "/webmin/miniserv.conf";

    @Override
    public String selectSSH() throws Exception {
        File file = new File(ssh_allow_file);
        String json = "{success:true,rows:[";
        int count = 0;
        if(file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String allowString = null;
            try{
                while ((allowString = reader.readLine()) != null){
                    if(allowString.trim().startsWith("sshd")){
                        String [] ips = allowString.split("\\:")[1].split(" ");
                        if(ips != null){
                            for(int i = 0 ; i < ips.length ; i ++){
                                if(ips[i] != null && ips[i].length() > 0){
                                    json += "{ip:'" + ips[i] + "'},";
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){
                throw e;
            }finally {
                reader.close();
            }
        }
        json += "],total:" + count + "}";
        return json;
    }

    @Override
    public String selectWebmin() throws Exception {
        File file = new File(webmin_allow_file);
        String json = "{success:true,rows:[";
        int count = 0;
        if(file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String allowString = null;
            try{
                while ((allowString = reader.readLine()) != null){
                    if(allowString.trim().startsWith("allow")){
                        String [] ips = allowString.split("=")[1].split(" ");
                        if(ips != null){
                            for(int i = 0 ; i < ips.length ; i ++){
                                if(ips[i] != null && ips[i].length() > 0){
                                    json += "{ip:'" + ips[i] + "'},";
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){
                throw e;
            }finally {
                reader.close();
            }
        }
        json += "],total:" + count + "}";
        return json;
    }

    @Override
    public String insertSSH(String oldIp,String newIp) throws Exception {
        //若存在相关规则,则删除
        String json = "{success:false,msg:'更新失败,点击[确定]返回列表!'}";
        OSInfo osInfo = OSInfo.getOSInfo();
        if(osInfo.isLinux()){
            Proc proc = new Proc();
            String del_command = null,add_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/ssh_delete.sh ";
            add_command = "sh " +StringContext.systemPath + "/others/firewall/ssh_save.sh ";
            if(oldIp != null && oldIp.length() > 0){
                del_command = del_command + " " + oldIp;
                proc.exec(del_command);
                logger.info(del_command);
            }
            if(newIp != null && newIp.length() > 0){
                add_command = add_command + " " + newIp;
                proc.exec(add_command);
                logger.info(add_command);
            }
            json ="{success:true,msg:'更新成功,点击[确定]返回列表!'}";
        }else{
            String del_command = null,add_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/ssh_delete.sh ";
            add_command = "sh " +StringContext.systemPath + "/others/firewall/ssh_save.sh ";
            if(oldIp != null && oldIp.length() > 0){
                del_command = del_command + " " + oldIp;
                logger.info(del_command);
            }
            if(newIp != null && newIp.length() > 0){
                add_command = add_command + " " + newIp;
                logger.info(add_command);
            }
            json ="{success:true,msg:'更新成功,点击[确定]返回列表!'}";
        }
        return json;
    }

    @Override
    public String insertWebmin(String oldIp,String newIp) throws Exception {
        //若存在相关规则,则删除
        String json = "{success:false,msg:'更新失败,点击[确定]返回列表!'}";
        OSInfo osInfo = OSInfo.getOSInfo();
        if(osInfo.isLinux()){
            Proc proc = new Proc();
            String del_command = null,add_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/webmin_delete.sh ";
            add_command = "sh " +StringContext.systemPath + "/others/firewall/webmin_save.sh ";
            if(oldIp != null && oldIp.length() > 0){
                del_command = del_command + " " + oldIp;
                proc.exec(del_command);
                logger.info(del_command);
            }
            if(newIp != null && newIp.length() > 0){
                add_command = add_command + " " + newIp;
                proc.exec(add_command);
                logger.info(add_command);
            }
            json ="{success:true,msg:'更新成功,点击[确定]返回列表!'}";
        }else{
            String del_command = null,add_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/webmin_delete.sh ";
            add_command = "sh " +StringContext.systemPath + "/others/firewall/webmin_save.sh ";
            if(oldIp != null && oldIp.length() > 0){
                del_command = del_command + " " + oldIp;
            }
            if(newIp != null && newIp.length() > 0){
                add_command = add_command + " " + newIp;
            }
            logger.info(del_command);
            logger.info(add_command);
        }
        return json;
    }

    @Override
    public String deleteSSH(String ip) throws Exception {
        //若存在相关规则,则删除
        String json = "{success:false,msg:'更新失败,点击[确定]返回列表!'}";
        OSInfo osInfo = OSInfo.getOSInfo();
        if(osInfo.isLinux()){
            Proc proc = new Proc();
            String del_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/ssh_delete.sh ";
            if(ip != null && ip.length() > 0){
                del_command = del_command + " " + ip;
                proc.exec(del_command);
                logger.info(del_command);
            }
            json ="{success:true,msg:'删除成功,点击[确定]返回列表!'}";
        }else{
            String del_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/ssh_delete.sh ";
            if(ip != null && ip.length() > 0){
                del_command = del_command + " " + ip;
                logger.info(del_command);
            }
            json ="{success:true,msg:'删除成功,点击[确定]返回列表!'}";
        }
        return json;
    }

    @Override
    public String deleteWebmin(String ip) throws Exception {
        //若存在相关规则,则删除
        String json = "{success:false,msg:'更新失败,点击[确定]返回列表!'}";
        OSInfo osInfo = OSInfo.getOSInfo();
        if(osInfo.isLinux()){
            Proc proc = new Proc();
            String del_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/webmin_delete.sh ";
            if(ip != null && ip.length() > 0){
                del_command = del_command + " " + ip;
                proc.exec(del_command);
                logger.info(del_command);
            }
            json ="{success:true,msg:'更新成功,点击[确定]返回列表!'}";
        }else{
            String del_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/webmin_delete.sh ";
            if(ip != null && ip.length() > 0){
                del_command = del_command + " " + ip;
                logger.info(del_command);
            }
            json ="{success:true,msg:'更新成功,点击[确定]返回列表!'}";
        }
        return json;
    }

    /**
     * add iptables into firewall.sh
     * iptables -A INPUT -s clientIp -d serverIp --dport port -j ACCEPT
     * @param clientIp
     * @param serverIp
     * @param port
     * @return
     * @throws Exception
     */
    @Override
    public String selectFromSh(String clientIp, String serverIp, String port) throws Exception {
        File file = new File(iptables_file);
        String json = "{success:true,rows:[";
        int count = 0;
        if(file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            try{
                while ((tempString = reader.readLine()) != null){
                    if(tempString.startsWith("iptables -A INPUT") && !tempString.startsWith("iptables -A INPUT -p")){
                        String [] strs = tempString.split(" ");
                        String r_clinetIp = null , r_serverIp = null , r_port = null;
                        boolean isExist = false;
                        for(int i = 0 ; i < strs.length ; i++){
                            if(strs[i].equals("-s")){
                                r_clinetIp = strs[i+1];
                                if(r_clinetIp.startsWith("127.0.0.1")){
                                    isExist = false;
                                    break;
                                }
                                i++;
                                isExist = true;
                            }else if(strs[i].equals("-d")){
                                r_serverIp = strs[i+1];
                                i++;
                                isExist = true;
                            }else if(strs[i].equals("--dport")){
                                r_port = strs[i+1];
                                i++;
                                isExist = true;
                            }
                        }
                        if(clientIp != null && clientIp.length() > 0){
                            if (!clientIp.equals(r_clinetIp)){
                                isExist = false;
                            }
                        }
                        if(serverIp != null && serverIp.length() > 0){
                            if(!serverIp.equals(r_serverIp)){
                                isExist = false;
                            }
                        }
                        if(port != null && port.length() > 0){
                            if(!port.equals(r_port)){
                                isExist = false;
                            }
                        }
                        if(isExist){
                            count ++;
                            json += "{clientIp:'"+isBlank(r_clinetIp)+"',serverIp:'"+isBlank(r_serverIp)+
                                    "',port:'"+isBlank(r_port)+
                                    "'},";
                        }
                    }
                }
            } catch (Exception e){
                throw e;
            }finally {
                reader.close();
            }
        }
        json += "],total:" + count + "}";
        return json;
    }

    /**
     *
     * @param old
     * @param iptables
     * @return
     * @throws Exception
     */
    @Override
    public String updateFromSh(Iptables old, Iptables iptables) throws Exception {
        //若存在相关规则,则删除
        String json = "{success:false,msg:'更新失败,点击[确定]返回列表!'}";
        OSInfo osInfo = OSInfo.getOSInfo();
        if(osInfo.isLinux()){
            Proc proc = new Proc();
            String del_command = null,add_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/delete.sh ";
            add_command = "sh " +StringContext.systemPath + "/others/firewall/save.sh ";
            if(old != null && old.getClientIp() != null && old.getClientIp().length() > 0){
                del_command = del_command + " " + old.getClientIp();
            }
            if(old != null && old.getServerIp() != null && old.getServerIp().length() > 0 ){
                del_command = del_command + " " + old.getServerIp();
            }
            if(old != null && old.getPort() != 0){
                del_command = del_command + iptables.getPort();
            }
            if(iptables.getClientIp() != null && iptables.getClientIp().length() > 0){
                add_command = add_command + " " + iptables.getClientIp();
            }
            if(iptables.getServerIp() != null && iptables.getServerIp().length() > 0 ){
                add_command = add_command + " " + iptables.getServerIp();
            }
            if(iptables.getPort() != 0){
                add_command = add_command + " " + iptables.getPort();
            }
            proc.exec(del_command);
            logger.info(del_command);
            proc.exec(add_command);
            logger.info(add_command);
            json ="{success:true,msg:'更新成功,点击[确定]返回列表!'}";
        }
        return json;
    }

    @Override
    public String deleteFromSh(Iptables iptables) throws Exception {
        //若存在相关规则,则删除
        String json = "{success:false,msg:'删除失败,点击[确定]返回列表!'}";
        OSInfo osInfo = OSInfo.getOSInfo();
        if(osInfo.isLinux()){
            Proc proc = new Proc();
            String del_command = null;
            del_command = "sh " +StringContext.systemPath + "/others/firewall/delete.sh ";
            if(iptables.getClientIp() != null && iptables.getClientIp().length() > 0){
                del_command = del_command + " " + iptables.getClientIp();
            }
            if(iptables.getServerIp() != null && iptables.getServerIp().length() > 0 ){
                del_command = del_command + " " + iptables.getServerIp();
            }
            if(iptables.getPort() != 0){
                del_command = del_command +" " +  iptables.getPort();
            }
            proc.exec(del_command);
            logger.info(del_command);
            json ="{success:true,msg:'删除成功,点击[确定]返回列表!'}";
        }
        return json;
    }

    @Override
    public String restart() throws Exception {
        OSInfo osInfo = OSInfo.getOSInfo();
        String json = "{success:false,msg:'应用失败,点击[确定]返回列表!'}";
        if(osInfo.isLinux()){
            Proc proc = new Proc();
            String command = "sh " + iptables_file + " ";
            proc.exec(command);
            logger.info(command);
            json ="{success:true,msg:'重新应用规则成功,点击[确定]返回列表!'}";
        }
        return  json;
    }

    @Override
    public String restartWebmin() throws Exception {
        OSInfo osInfo = OSInfo.getOSInfo();
        String json = "{success:false,msg:'应用失败,点击[确定]返回列表!'}";
        if(osInfo.isLinux()){
            Proc proc = new Proc();
            String command = "service webmin restart";
            proc.exec(command);
            logger.info(command);
            json ="{success:true,msg:'重新应用规则成功,点击[确定]返回列表!'}";
        }
        return  json;
    }

    @Override
    public String select(String clientIp, String serverIp, String port, int start, int limit) throws Exception {
        PageResult ps = iptablesDao.listByPage(clientIp, serverIp, port, start, limit);
        List<Iptables> iptablesList = ps.getResults();
        int total = ps.getAllResultsAmount();
        String json = "{success:true,total:"+total + ",rows:[";
        for (Iptables i : iptablesList) {
            json += "{id:"+isBlank(i.getId())+",clientIp:'"+isBlank(i.getClientIp())+"',serverIp:'"+isBlank(i.getServerIp())+
                    "',port:'"+isBlank(i.getPort())+"',isActive:'"+(i.isActive())+
                    "'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public String insert(Iptables iptables) throws Exception {
        iptablesDao.create(iptables);
        return "{success:true,msg:'新增成功,点击[确定]返回列表!'}";
    }

    @Override
    public String update(Iptables iptables) throws Exception {
        Iptables old = (Iptables)iptablesDao.getById(iptables.getId());
        old.setClientIp(iptables.getClientIp());
        old.setServerIp(iptables.getServerIp());
        old.setPort(iptables.getPort());
        old.setActive(iptables.isActive());
        iptablesDao.update(old);
        return "{success:true,msg:'更新成功,点击[确定]返回列表!'}";

    }

    @Override
    public String delete(String ids) throws Exception {
        if(ids != null){
            String [] str_id = ids.split(",");
            for(int i = 0 ; i < str_id.length ; i++){
                long id = Long.parseLong(str_id[i]);
                Iptables old = (Iptables) iptablesDao.getById(id);
                if(old != null){
                    iptablesDao.delete(id);
                }

            }
        }
        return "{success:true,msg:'删除成功,点击[确定]返回列表!'}";
    }

    private String isBlank(Object o){
        if(o == null){
            return "";
        }else{
            String v = o.toString();
            if(v != null && v.length() > 0){
                return v;
            }else {
                return "";
            }
        }
    }

    public IptablesDao getIptablesDao() {
        return iptablesDao;
    }

    public void setIptablesDao(IptablesDao iptablesDao) {
        this.iptablesDao = iptablesDao;
    }
}
