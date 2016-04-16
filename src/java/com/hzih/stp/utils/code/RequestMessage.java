package com.hzih.stp.utils.code;

import com.google.protobuf.ByteString;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2009-4-10
 * Time: 20:42:22
 * To change this template use File | Settings | File Templates.
 */
public class RequestMessage implements Serializable,Cloneable {
//    public static int FileNameLength = 19;
    public static short ProtocolTypeOnly = '\013';
    public static short ProtocolTypeStart = '\014';
    public static final short ProtocolType = '\015';
    public static final short ProtocolTypeEnd = '\016';
    public static final byte Version = '\017';
    private byte version;
    private short protocolType;
    private byte fileCode;    //文件顺序编号 1--100,
    private long fileSize;
    private int msgLen;
    private ByteString msgBody;
    private byte nameLen;
    private ByteString nameBody;
    private int md5Len;
    private ByteString md5Body;

    public RequestMessage() {
        version = Version;
//        protocolType = ProtoclType;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public short getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(short protocolType) {
        this.protocolType = protocolType;
    }

    public byte getFileCode() {
        return fileCode;
    }

    public void setFileCode(byte fileCode) {
        this.fileCode = fileCode;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }

    public ByteString getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(ByteString msgBody) {
        this.msgBody = msgBody;
        this.msgLen = msgBody.size();
    }

    public int getMd5Len() {
        return md5Len;
    }

    public void setMd5Len(int md5Len) {
        this.md5Len = md5Len;
    }

    public ByteString getMd5Body() {
        return md5Body;
    }

    public void setMd5Body(ByteString md5Body) {
        this.md5Body = md5Body;
        this.md5Len = md5Body.size();
    }

    public void clean() {
        msgLen = 0;
        msgBody = null;
        nameLen = 0;
        nameBody = null;
        md5Len = 0;
        md5Body = null;
    }

    public byte getNameLen() {
        return nameLen;
    }

    public void setNameLen(byte nameLen) {
        this.nameLen = nameLen;
    }

    public ByteString getNameBody() {
        return nameBody;
    }

    public void setNameBody(ByteString nameBody) {
        this.nameBody = nameBody;
        this.nameLen = (byte) nameBody.size();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        RequestMessage o = (RequestMessage) super.clone();
        return o;
    }
}
