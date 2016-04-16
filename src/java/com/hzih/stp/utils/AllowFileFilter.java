package com.hzih.stp.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Administrator on 15-5-29.
 */
public class AllowFileFilter implements FileFilter {
    final static Logger logger = Logger.getLogger(AllowFileFilter.class);
    private int level;

    public AllowFileFilter(int level) {
        this.level = level;
    }

    @Override
    public boolean accept(File file) {
        if(file.isFile()){
            int l = makeL(file.getName());
            if(level >= l) {
                return true;
            }
        }
        return false;
    }

    private int makeL(String filePath) {

        if(filePath.endsWith("_cx")) {
            String end = filePath.substring(filePath.lastIndexOf("."));
            return Integer.parseInt(end.substring(1,2));
        }
        return 0;
    }
}
