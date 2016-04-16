package com.hzih.stp.service;

import com.hzih.stp.domain.Iptables;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-30
 * Time: 下午3:32
 * To change this template use File | Settings | File Templates.
 */
public interface IptablesService {

    public String selectSSH() throws Exception;

    public String selectWebmin() throws Exception;

    public String insertSSH(String oldIp,String newIp) throws Exception;

    public String insertWebmin(String oldIp,String newIp) throws Exception;

    public String deleteSSH(String ip) throws Exception;

    public String deleteWebmin(String ip) throws Exception;

    public String selectFromSh(String clientIp,String serverIp,String port) throws Exception;

    public String updateFromSh(Iptables old ,Iptables iptables) throws Exception;

    public String deleteFromSh(Iptables iptables) throws Exception;

    public String restart() throws Exception;

    public String restartWebmin() throws Exception;

    public String select(String clientIp,String serverIp,String port,int start, int limit) throws Exception;

    public String insert(Iptables iptables) throws Exception;

    public String update(Iptables iptables) throws Exception;

    public String delete(String ids) throws Exception;
}
