package com.hzih.stp.domain;


/**
 * 防火墙
 *
 * @author collin.code@gmail.com
 * @hibernate.class table="account"
 */
public class Iptables {
    /**
     * @hibernate.id column="id" generator-class="increment" type="long"
     *               length="11"
     */
    Long id;
    /**
     * 客户端IP
     *
     * @hibernate.property column="clientIp" type="string" length="20"
     */
    String clientIp;
    /**
     * 服务端IP
     *
     * @hibernate.property column="serverIp" type="string" length="20"
     */
    String serverIp;
    /**
     * 访问端口
     *
     * @hibernate.property column="port" type="int" length="11"
     */
    int port;
    /**
     * 是否启用
     *
     * @hibernate.property column="port" type="boolean" length="2"
     */
    boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    public void setActive(String active){
        if(active != null && active.length() > 0){
            isActive = Boolean.valueOf(active);
        }
    }
}
