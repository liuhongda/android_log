package com.xtc.common.log;

import android.content.Context;

/**
 * 日志打印工具
 *
 * Created by lhd on 2016/3/22.
 */
public class LogUtil {

    private static ILog logger;

    /**
     * 初始化log工具
     * @param context
     * @param configFileName log配置文件名,null表示使用默认
     * @param saveLog 是否保存log到文件中
     * @param logUrl 文件保存路径,null表示使用默认
     */
    public static void init(Context context, String configFileName, boolean saveLog, String logUrl) {
        if (logger == null) {
            LogPrinter myLogPrinter = new LogPrinter(context.getApplicationContext());
            myLogPrinter.setLogConfigFileName(configFileName);
            myLogPrinter.setSaveMode(saveLog);
            myLogPrinter.setSaveUrl(logUrl);
            logger = myLogPrinter;
        }
    }

    public static void init(ILog iLog) {
        logger = iLog;
    }

    public static void setDebug(boolean isDebug) {
        if (logger == null) {
            return;
        }
        logger.setDebug(isDebug);
    }

    public static void setSaveLevel(boolean v, boolean d, boolean i, boolean w, boolean e) {
        if (logger == null) {
            return;
        }
        logger.setSaveLevel(v, d, i, w, e);
    }

    public static void v(String text) {
        if (logger == null) {
            return;
        }
        logger.v(new Throwable().getStackTrace(), text);
    }

    public static void d(String text) {
        if (logger == null) {
            return;
        }
        logger.d(new Throwable().getStackTrace(), text);
    }

    public static void i(String text) {
        if (logger == null) {
            return;
        }
        logger.i(new Throwable().getStackTrace(), text);
    }

    public static void w(String text) {
        if (logger == null) {
            return;
        }
        logger.w(new Throwable().getStackTrace(), text);
    }

    public static void e(String text) {
        if (logger == null) {
            return;
        }
        logger.e(new Throwable().getStackTrace(), text);
    }

    public static void e(Throwable throwable) {
        if (logger == null) {
            return;
        }
        logger.e(new Throwable().getStackTrace(), throwable);
    }

    public static void v(String tag, String text) {
        if (logger == null) {
            return;
        }
        logger.v(tag, text);
    }

    public static void d(String tag, String text) {
        if (logger == null) {
            return;
        }
        logger.d(tag, text);
    }

    public static void i(String tag, String text) {
        if (logger == null) {
            return;
        }
        logger.i(tag, text);
    }

    public static void w(String tag, String text) {
        if (logger == null) {
            return;
        }
        logger.w(tag, text);
    }

    public static void e(String tag, String text) {
        if (logger == null) {
            return;
        }
        logger.e(tag, text);
    }

    public static void e(String tag, Throwable throwable) {
        if (logger == null) {
            return;
        }
        logger.e(tag, throwable);
    }
}
