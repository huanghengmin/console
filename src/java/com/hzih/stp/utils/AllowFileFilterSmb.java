package com.hzih.stp.utils;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileFilter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Administrator on 15-5-29.
 */
public class AllowFileFilterSmb implements SmbFileFilter {
    final static Logger logger = Logger.getLogger(AllowFileFilterSmb.class);
    private int level;

    public AllowFileFilterSmb(int level) {
        this.level = level;
    }

    private int makeL(String filePath) {
        if(filePath.endsWith("_cx")) {
            String end = filePath.substring(filePath.lastIndexOf("."));
            return Integer.parseInt(end.substring(1,2));
        }
        return 0;
    }

    @Override
    public boolean accept(SmbFile file) throws SmbException {
        if(file.isFile()){
            int l = makeL(file.getName());
            if(level >= l) {
                return true;
            }
        }
        return false;
    }
}
