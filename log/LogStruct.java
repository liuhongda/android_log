package com.xtc.common.log;

import android.os.Process;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lhd on 2016/3/22.
 */
public class LogStruct {

    String level;
    String time;
    int pid;
    int tid;
    String pckName;
    String tag;
    String text;

    LogStruct(String level, String tag, String text, String pckName) {
        String formatDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        this.time = formatDate;
        this.pid = Process.myPid();
        this.tid = Process.myTid();
        this.pckName = pckName;
        this.level = level;
        this.tag = tag;
        this.text = text;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("level", level);
            jsonObject.put("time", time);
            jsonObject.put("pid", pid);
            jsonObject.put("tid", tid);
            jsonObject.put("pckName", pckName);
            jsonObject.put("tag", tag);
            jsonObject.put("text", text);
        } catch (JSONException e) {
            LogUtil.e("LogStruct", e);
        }
        return jsonObject.toString();
    }
}
