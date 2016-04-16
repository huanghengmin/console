package com.hzih.stp.utils.code;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2009-4-10
 * Time: 20:43:16
 * To change this template use File | Settings | File Templates.
 */
public class RequestMessageEncoder implements MessageEncoder {
    public RequestMessageEncoder() {
    }

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
            throws Exception {
        RequestMessage mess = (RequestMessage) message;
        IoBuffer buff = IoBuffer.allocate(mess.getMsgLen() + mess.getNameLen() + mess.getMd5Len() + 7 + 1 + 1 + 8 + 4);
//        buff.setAutoExpand(true);
        buff.put(mess.getVersion());
        buff.putShort(mess.getProtocolType());
        buff.put(mess.getFileCode());
        buff.putLong(mess.getFileSize());
        buff.put(mess.getNameLen());
        if(mess.getNameBody()!=null) {
            buff.put(mess.getNameBody().toByteArray());
        }

        buff.putInt(mess.getMd5Len());
        if(mess.getMd5Body()!=null){
            buff.put(mess.getMd5Body().toByteArray());
        }

        buff.putInt(mess.getMsgLen());
        if (mess.getMsgBody() != null) {
            buff.put(mess.getMsgBody().toByteArray());
        }
        buff.flip();
        mess.clean();
        out.write(buff);
//        out.flush();
    }
}