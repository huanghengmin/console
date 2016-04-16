package com.hzih.stp.utils.filter;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-8-9
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public interface KeywordsFilterUtil {

    public byte[] filter(byte[] data, String keywords) throws Exception;

    public InputStream filter(InputStream inputStream, String keywords) throws Exception;

}
