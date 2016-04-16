package com.hzih.stp.utils.udp;

import com.hzih.stp.utils.code.ServerMessageCodecFactory;
import com.inetec.common.config.stp.nodes.TargetFile;
import com.inetec.common.config.stp.nodes.Type;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-12-9
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public class UdpServer implements Runnable {
    private static final Logger logger = Logger.getLogger(UdpServer.class);
    private NioDatagramAcceptor acceptor;
    private InetSocketAddress localAddress;
    private boolean isRunning = false;
    private boolean isStart = true;
    private String appName;
    private UdpServerHandler serverHandler;

    /**
     *
     * @throws java.io.IOException
     */
    public UdpServer() {

    }


    public void init() {
        this.acceptor = new NioDatagramAcceptor();//创建一个UDP的接收器

        serverHandler = new UdpServerHandler();
        acceptor.setHandler(serverHandler);//设置接收器的处理程序
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("codec", new ProtocolCodecFilter(new ServerMessageCodecFactory()));
        DatagramSessionConfig dcfg = acceptor.getSessionConfig();//建立连接的配置文件
        dcfg.setReuseAddress(true);//设置每一个非主监听连接的端口可以重用
        dcfg.setBroadcast(false);
        dcfg.setReceiveBufferSize(6553600);
        dcfg.setSendBufferSize(6553600);
        dcfg.setMaxReadBufferSize(6553600);
        dcfg.setMinReadBufferSize(65536);
    }
    private boolean isOk = false;

    public void startUp(InetSocketAddress localAddress, String dir, TargetFile targetFile){
        this.localAddress = localAddress;
        this.serverHandler.init(dir,targetFile);
        this.isStart = true;
        try {
            if(acceptor.isDisposed()){
                init();
                this.serverHandler.init(dir,targetFile);
            }
            acceptor.bind(localAddress);//绑定端口
            logger.info("启动端口" + localAddress.toString() + "的UDP监听...");
            isOk = true;
        } catch (IOException e) {
            logger.error("启动端口"+localAddress.toString()+"的UDP监听错误..",e);
        }
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            if(!isStart) {
                isOk = false;
                acceptor.dispose();
                acceptor.unbind();
                logger.info("关闭端口" + localAddress.toString() + "的UDP监听...");
                isStart = true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

    }

    public boolean isOk() {
        return isOk;
    }

    public void closeUp() {
        this.isOk = false;
        this.isStart = false;

    }

}
