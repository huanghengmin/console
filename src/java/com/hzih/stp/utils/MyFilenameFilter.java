package com.hzih.stp.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by 钱晓盼 on 15-8-28.
 */
public class MyFilenameFilter implements FilenameFilter {

    private String fileName;

    public MyFilenameFilter(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean accept(File file, String s) {
        if(file.getName().indexOf(fileName)>-1){
            return true;
        }
        return false;
    }
}
