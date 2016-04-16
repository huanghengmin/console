package com.hzih.stp.utils.udp;

import JACE.ASX.MessageQueue;
import JACE.ASX.TimeValue;
import com.hzih.stp.utils.FileUtil;
import com.inetec.common.config.stp.nodes.TargetFile;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by 钱晓盼 on 15-6-15.
 */
public class UploadService extends Thread {
    private final Logger logger = Logger.getLogger(UploadService.class);
//    private MessageQueue queue;

    private boolean isRunning = false;
    private boolean isStopping = false;
    private TargetFile targetFile;

    public void init(TargetFile targetFile){
//        queue = new MessageQueue();
        this.targetFile = targetFile;
    }


//    public boolean haveMessages() {
//        if(queue == null) {
//            queue = new MessageQueue();
//            return false;
//        }
//        return (!queue.isEmpty());
//    }
//
//    public void offer(String filePath) {
//        try {
//            queue.enqueueTail(new JACE.ASX.MessageBlock(filePath));
//        } catch (InterruptedException e) {
//        }
//    }

    public void isStop( boolean isStopping) {
        this.isStopping = isStopping;

    }

    public void run() {
        isRunning = true;
        while (isRunning) {
//            if(haveMessages()){
//                try {
//                    String filePath = queue.dequeueHead(new TimeValue(3000)).base();
//
//                } catch (Exception e) {
//                    logger.error("", e);
//                }
//            } else {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                }
//            }
            while (isStopping) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            File dir = new File("/data/flag");
            File[] list = dir.listFiles();
            for (File file : list) {
                if(targetFile!=null){
                    smbUpload(file,targetFile);
                } else {
                    File _dir = new File("/data/flag2");
                    if(!_dir.exists()) {
                       _dir.mkdirs();
                    }
                    try {
                        FileUtil.copy(file,_dir);
                    } catch (IOException e) {
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

        }

    }

    private void smbUpload(File file, TargetFile targetFile) {
        NtlmPasswordAuthentication npa =
                    new NtlmPasswordAuthentication(targetFile.getServerAddress(),
                            targetFile.getUserName(),targetFile.getPassword());
        String fileName = file.getName();
        String url = "smb://" + targetFile.getServerAddress() + ":" + targetFile.getPort() + targetFile.getDir();
        if(url.endsWith("/")) {
            url += fileName;
        } else {
            url += "/" + fileName;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            SmbFile smbFile = new SmbFile(url,npa);
            out = new SmbFileOutputStream(smbFile);
            in = new FileInputStream(file);
            FileUtil.copy(in, out);
        } catch (Exception e) {
            logger.error("上传失败",e);
        } finally {
            if(out!=null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if(in!=null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            file.delete();
        }
    }

}
