package com.hzih.stp.utils.udp;

import com.hzih.stp.utils.code.ClientMessageCodecFactory;
import com.hzih.stp.utils.code.RequestMessage;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-29
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public class UdpClientImpl {
    private static final Logger logger = Logger.getLogger(UdpClientImpl.class);
    private InetSocketAddress target;
    private InetSocketAddress source;
    private String appName;
    private IoSession session;
    private NioDatagramAcceptor acceptor;
    private long times;

    private boolean isRun = false;

    public UdpClientImpl(String appName, InetSocketAddress target,InetSocketAddress source) {
        this.target = target;
        this.source = source;
        this.appName = appName;
        acceptor = new NioDatagramAcceptor();
        acceptor.setHandler(new ClientHandler(appName));
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("codec", new ProtocolCodecFilter(new ClientMessageCodecFactory()));
        DatagramSessionConfig ccfg = acceptor.getSessionConfig();
        ccfg.setReuseAddress(true);
        ccfg.setBroadcast(false);
        ccfg.setSendBufferSize(6553600);
        logger.info("send from " + source.toString() + " to " + target.toString());
    }

    private boolean sendBuf(RequestMessage message) {
        try{
            session.write(message);
            System.setProperty("networkok_1", String.valueOf(true));
        } catch (Exception e) {
            logger.error("send error ",e);
        }
        return Boolean.valueOf(System.getProperty("networkok_1"));
    }

    public void close() {
        acceptor.dispose();
        acceptor.unbind();
    }
    public void destroy(){
        acceptor.dispose();
    }

    public InetSocketAddress getInetSocketAddress() {
        return target;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void start() {
        try {
            acceptor.bind(source);
        } catch (IOException e) {
        }
    }

    public void sendMessage(RequestMessage message) {
        if(session == null || session.isClosing()){
            session = acceptor.newSession(target,source);
        }
        session.write(message);
    }



}
