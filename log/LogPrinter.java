package com.xtc.common.log;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 日志打印
 * <b>此类可以灵活的控制log输出,解决了因不同的人开发的时候打印不同的log造成程序log打印非常混乱,只需做一些简单的配置
 * 就可屏蔽log输出和打印自己想要的log输出</b>
 * <p><br>1.在配置文件中配置"类全名"=true/false;"包名"=true/false
 * <br>2.父包和子包配置有冲突就以子包为准</p>
 * <p>和assets文件夹中的log.properties文件配合控制log的打印输出
 * 在文件中配置类的全名和log打印模式(true表为debug模式,打印log;false表示非debug模式,即不打印log)
 * 在文件中配置类的log打印模式是永久生效的,也可直接调用debugAll()或者debug()方法来动态改变文件的配置
 * 不过调用这两个方法改变的打印模式只在内存中生效,不会写入配置文件中</p>
 *
 * Created by lhd on 2016/3/22.
 */
public class LogPrinter implements ILog {

    private enum LEVEL {
        verbose, debug, info, warn, error
    }

    private String pckName;

    private String className;

    private String simpleClassName;

    private String methodName;

    private int lineNumber;

    private boolean isDebug = true;

    /**
     * 日志保存模式,false不保存文件,true则自动保存文件
     */
    private boolean saveFile = true;

    private boolean i = false;

    private boolean d = false;

    private boolean w = false;

    private boolean v = false;

    private boolean e = true;

    public String saveUrl = Environment.getExternalStorageDirectory() + "/xtci3data/logs/watch/";

    public String testUrl = Environment.getExternalStorageDirectory() + "/xtci3data/logs/test/";

    private Properties properties = new Properties();

    private String propertiesName = "log.properties";

    public LogPrinter(Context context) {
        initProperties(context);
        initVar();
        pckName = context.getPackageName();
    }

    private void initProperties(Context context) {
        try {
            InputStream is = context.getAssets().open(propertiesName);
            properties.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVar() {
        if (properties != null) {
            String saveMode = properties.getProperty("saveFile");
            if (saveMode != null) {
                saveFile = Boolean.parseBoolean(saveMode);
            }
            String iMode = properties.getProperty("i");
            if (iMode != null) {
                i = Boolean.parseBoolean(iMode);
            }
            String dMode = properties.getProperty("d");
            if (dMode != null) {
                d = Boolean.parseBoolean(dMode);
            }
            String wMode = properties.getProperty("w");
            if (wMode != null) {
                w = Boolean.parseBoolean(wMode);
            }
            String vMode = properties.getProperty("v");
            if (vMode != null) {
                v = Boolean.parseBoolean(vMode);
            }
            String eMode = properties.getProperty("e");
            if (eMode != null) {
                e = Boolean.parseBoolean(eMode);
            }
        }
    }

    public void setLogConfigFileName(String configFileName) {
        if (TextUtils.isEmpty(configFileName)) {
            return;
        }
        propertiesName = configFileName;
    }

    public void setSaveUrl(String saveUrl) {
        if (TextUtils.isEmpty(saveUrl)) {
            return;
        }
        this.saveUrl = saveUrl;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    /**
     * 日志保存模式,false不保存文件,true则自动保存文件
     */
    public void setSaveMode(boolean saveFile) {
        this.saveFile = saveFile;
    }

    @Override
    public void setSaveLevel(boolean v, boolean d, boolean i, boolean w, boolean e) {
        this.v = v;
        this.d = d;
        this.i = i;
        this.w = w;
        this.e = e;
    }

    private void initLogMember(StackTraceElement[] sElements) {
        className = sElements[1].getClassName();
        int i = className.lastIndexOf(".");
        if ((i != -1) && (i + 1 < className.length() - 1)) {
            simpleClassName = className.substring(i + 1, className.length());
        } else {
            simpleClassName = className;
        }
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    private synchronized void log(LEVEL level, StackTraceElement[] sElements, String msg) {
        if (!isDebug) {
            //Log.i("LogUtil", "DEBUG = false");
            return;
        }
        initLogMember(sElements);
        if (check()) {
            print(level, msg);
        } else {
            // do nothing
        }
    }

    private void print(LEVEL level, String msg) {
        String tag = formatTag();

        switch (level) {
            case verbose:
                Log.v(tag, msg);
                break;
            case debug:
                Log.d(tag, msg);
                break;
            case info:
                Log.i(tag, msg);
                break;
            case warn:
                Log.w(tag, msg);
                break;
            case error:
                Log.e(tag, msg);
                break;
            default:
                break;
        }
    }

    private void saveLogToFile(LEVEL level, String tag, String msg) {
        if (!isDebug) {
            return;
        }
        String formatDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        saveLogToFile(level, saveUrl, formatDate + ".txt", false, tag, msg);
    }

    /**
     * 把日志保存到sdcard文件夹中
     *
     * @param level
     * @param dir
     * @param fileName
     * @param msg
     */
    private void saveLogToFile(LEVEL level, String dir, String fileName, boolean clear, String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            tag = formatTag();
        }
        LogStruct logStruct = new LogStruct(level.name(), tag, msg, pckName);
        LogSaver.saveLog(dir, fileName, clear, logStruct);
    }

    private boolean check() {

        if (properties == null) {
            LogUtil.e("LogPrinter", "properties is null");
            return false;
        }

        return LogPrintFilter.checkPrint(properties, className);
    }

    private String formatTag() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(simpleClassName);
        strBuf.append(".");
        strBuf.append(methodName);
        strBuf.append(":");
        strBuf.append(lineNumber);
        return strBuf.toString();
    }

    @Override
    public void test(StackTraceElement[] sElements, String msg) {
        log(LEVEL.error, sElements, msg);
        String formatDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        saveLogToFile(LEVEL.debug, testUrl, formatDate + ".txt", true, null, msg);
    }

    @Override
    public void v(StackTraceElement[] sElements, String msg) {
        log(LEVEL.verbose, sElements, msg);
        if (saveFile && v) {
            saveLogToFile(LEVEL.verbose, null, msg);
        }
    }

    @Override
    public void d(StackTraceElement[] sElements, String msg) {
        log(LEVEL.debug, sElements, msg);
        if (saveFile && d) {
            saveLogToFile(LEVEL.debug, null, msg);
        }
    }

    @Override
    public void i(StackTraceElement[] sElements, String msg) {
        log(LEVEL.info, sElements, msg);
        if (saveFile && i) {
            saveLogToFile(LEVEL.info, null, msg);
        }
    }

    @Override
    public void w(StackTraceElement[] sElements, String msg) {
        log(LEVEL.warn, sElements, msg);
        if (saveFile && w) {
            saveLogToFile(LEVEL.warn, null, msg);
        }
    }

    @Override
    public void e(StackTraceElement[] sElements, String msg) {
        log(LEVEL.error, sElements, msg);
        if (saveFile && e) {
            saveLogToFile(LEVEL.error, null, msg);
        }
    }

    @Override
    public void e(StackTraceElement[] sElements, Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        log(LEVEL.error, sElements, stringWriter.toString());
        if (saveFile && e) {
            saveLogToFile(LEVEL.error, null, stringWriter.toString());
        }
    }

    @Override
    public void e(String tag, String text) {
        Log.e(tag, text);
        if (saveFile && e) {
            saveLogToFile(LEVEL.error, tag, text);
        }
    }

    @Override
    public void e(String tag, Throwable throwable) {
        Log.e(tag, throwable.toString());
        if (saveFile && e) {
            saveLogToFile(LEVEL.error, tag, throwable.toString());
        }
    }

    @Override
    public void w(String tag, String text) {
        Log.w(tag, text);
        if (saveFile && w) {
            saveLogToFile(LEVEL.warn, tag, text);
        }
    }

    @Override
    public void i(String tag, String text) {
        Log.i(tag, text);
        if (saveFile && i) {
            saveLogToFile(LEVEL.warn, tag, text);
        }
    }

    @Override
    public void d(String tag, String text) {
        Log.d(tag, text);
        if (saveFile && d) {
            saveLogToFile(LEVEL.warn, tag, text);
        }
    }

    @Override
    public void v(String tag, String text) {
        Log.v(tag, text);
        if (saveFile && v) {
            saveLogToFile(LEVEL.warn, tag, text);
        }
    }
}
