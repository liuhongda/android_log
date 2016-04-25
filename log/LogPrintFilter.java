package com.xtc.common.log;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 日志过滤
 *
 * Created by lhd on 2016/3/22.
 */
public class LogPrintFilter {

    public static boolean checkPrint(Properties properties, String className) {
        String key = new String(className);
        int i = -1;
        if ((i = key.indexOf("$")) != -1) {
            key = key.substring(0, i);
        }

        return match(properties, key);
    }

    // 默认匹配
    private static boolean match(Properties properties, String className) {
        int matchFullClassName = matchFullClassName(properties, className);
        if (matchFullClassName != -1) {
            return matchFullClassName == 1 ? true : false;
        }

        int matchFullPackageName = matchFullPackageName(properties, className);
        if (matchFullPackageName != -1) {
            return matchFullPackageName == 1 ? true : false;
        }

        int matchParentPackageName = matchParentPackageName(properties, className);
        if (matchParentPackageName != -1) {
            return matchParentPackageName == 1 ? true : false;
        }
        // 如果都是匹配不到就是没有配置过滤条件，没配置就默认不过滤
        return true;
    }

    //完整类名匹配
    private static int matchFullClassName(Properties properties, String className) {
        int match = 1;
        String result = properties.getProperty(className);
        if (result != null) {
            boolean b = Boolean.parseBoolean(result);
            if (b) {
                match = 1; // 匹配
            } else {
                match = 0; // 不匹配
            }
        } else {
            match = -1; // 不存
        }
        return match;
    }

    //完整包名匹配
    private static int matchFullPackageName(Properties properties, String className) {
        int match = 1;
        String packageName = null;
        try {
            packageName = Class.forName(className).getPackage().getName();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String pckResult = properties.getProperty(packageName);
        if (pckResult != null) {
            boolean b = Boolean.parseBoolean(pckResult);
            if (b) {
                match = 1; // 匹配
            } else {
                match = 0; // 不匹配
            }
        } else {
            match = -1; // 不存
        }
        return match;
    }

    //最小父包名匹配
    private static int matchParentPackageName(Properties properties, String className) {
        int match = 1;
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String k = entry.getKey().toString();
            if (className.startsWith(k)) {
                if (map.get(className) != null) {
                    if (map.get(className).length() < k.length())
                        map.put(className, k);
                } else {
                    map.put(className, k);
                }
            }
        }
        if (map.size() > 0) {
            boolean b = Boolean.parseBoolean(properties.getProperty(map.get(className)));
            map.clear();
            if (b) {
                match = 1; // 匹配
            } else {
                match = 0; // 不匹配
            }
        } else {
            match = -1;
        }
        return match;
    }
}
