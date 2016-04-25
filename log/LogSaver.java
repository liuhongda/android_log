package com.xtc.common.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 日志保存
 *
 * Created by lhd on 2016/3/22.
 */
public class LogSaver {

    public static final String TAG = LogSaver.class.getSimpleName();

    public static void saveLog(String dir, String fileName, boolean clear, LogStruct logStruct) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }

        if (clear) {
            File[] fs = file.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    if (f.getName().equals(fileName)) {
                        continue;
                    }
                    f.delete();
                }
            }
        }

        File logFile = new File(dir, fileName);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                LogUtil.e(TAG, e.toString());
            }
        }
        writeToFile(logFile, logStruct, true);
    }

    private static void writeToFile(File logFile, LogStruct logStruct, boolean append) {
        String line = logStruct.toString() + "\r\n";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logFile, append); // 内容追加方式append
            fos.write(line.getBytes());
        } catch (IOException e) {
            LogUtil.e(TAG, "save log failed:" + e.toString());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LogUtil.e(TAG, "close FileOutputStream failed after save log:" + e.toString());
                }
            }
        }
    }
}
