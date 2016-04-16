package com.hzih.stp.utils.code;

import com.google.protobuf.ByteString;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2009-4-10
 * Time: 20:43:16
 * To change this template use File | Settings | File Templates.
 */
public class RequestMessageDecoder implements MessageDecoder {
    public static final Logger logger = Logger.getLogger(RequestMessageDecoder.class);
    public static final String Str_SessionBuffer = "SessionBuffer";
    public static short ProtocolTypeOnly = '\013';
    public static short ProtocolTypeStart = '\014';
    public static final short ProtocolType = '\015';
    public static final short ProtocolTypeEnd = '\016';
    public static final byte Version = '\017';
    private byte version;
    private short protocolType;
    private short protocolTypeEnd;
    private short protocolTypeOnly;
    private short protocolTypeStart;


    public RequestMessageDecoder() {
        this.version = Version;
        this.protocolType = ProtocolType;
        this.protocolTypeStart = ProtocolTypeStart;
        this.protocolTypeOnly = ProtocolTypeOnly;
        this.protocolTypeEnd = ProtocolTypeEnd;
    }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        if (in.remaining() < 17) {
            return MessageDecoderResult.NEED_DATA;
        }
        if (session.containsAttribute(Str_SessionBuffer)) {
            IoBuffer buffer = (IoBuffer) session.getAttribute(Str_SessionBuffer);
            if (buffer.remaining() > 0) {
                in.put(buffer.array());
            }
            buffer.free();
            session.removeAttribute(Str_SessionBuffer);
        }
        //get version
        byte ver = in.get();
        //get protocol type
        short type = in.getShort();
        byte code = in.get();
        long fileSize = in.getLong();
        //get message length
        byte nameLen = in.get();
        if (nameLen > 0) {
            byte[] buff = new byte[nameLen];
            in.get(buff);
        }
        int md5Len = in.getInt();
        if (md5Len > 0) {
            byte[] buff = new byte[md5Len];
            in.get(buff);
        }
        int len = in.getInt();
        if (in.remaining() < len)
            return MessageDecoderResult.NEED_DATA;
        if (ver == this.version
                && (type == this.protocolTypeStart
                || type == this.protocolType
                || type == this.protocolTypeOnly
                || type == this.protocolTypeEnd)) {
            return MessageDecoderResult.OK;
        }
        return MessageDecoderResult.NOT_OK;
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        RequestMessage tMsg = new RequestMessage();
        tMsg.setVersion(in.get());
        tMsg.setProtocolType(in.getShort());
        tMsg.setFileCode(in.get());
        tMsg.setFileSize(in.getLong());
        byte nameLen = in.get();
        if (nameLen > 0) {
            byte[] buff = new byte[nameLen];
            in.get(buff);
            tMsg.setNameLen(nameLen);
            tMsg.setNameBody(ByteString.copyFrom(buff));
//            out.write(tMsg);
        }
        int md5Len = in.getInt();
        if (md5Len > 0) {
            byte[] buff = new byte[md5Len];
            in.get(buff);
            tMsg.setMd5Len(md5Len);
            tMsg.setMd5Body(ByteString.copyFrom(buff));
//            out.write(tMsg);
        }
        int msgLen = in.getInt();
        if (msgLen > 0) {
            byte[] buff = new byte[msgLen];
            in.get(buff);
            tMsg.setMsgLen(msgLen);
            tMsg.setMsgBody(ByteString.copyFrom(buff));
            out.write(tMsg);
        }
        if (in.remaining() > 0) {
            int len = in.remaining();
            IoBuffer buffer = IoBuffer.allocate(len);
            buffer.put(in.get(new byte[len]));
            session.setAttribute(Str_SessionBuffer, buffer);
            logger.warn("RequestMessageDecoder recv duble packet.length:" + len);
        }
        in.free();
        return MessageDecoderResult.OK;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }
}

