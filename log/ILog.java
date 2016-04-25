package com.xtc.common.log;

/**
 * Created by lhd on 2016/3/22.
 */
public interface ILog {

    void v(String tag, String text);
    void d(String tag, String text);
    void i(String tag, String text);
    void w(String tag, String text);
    void e(String tag, String text);
    void e(String tag, Throwable throwable);

    void v(StackTraceElement[] sElements, String text);
    void d(StackTraceElement[] sElements, String text);
    void i(StackTraceElement[] sElements, String text);
    void w(StackTraceElement[] sElements, String text);
    void e(StackTraceElement[] sElements, String text);
    void e(StackTraceElement[] sElements, Throwable throwable);

    void test(StackTraceElement[] sElements, String text);

    void setDebug(boolean debug);

    void setSaveLevel(boolean v, boolean d, boolean i, boolean w, boolean e);
}
