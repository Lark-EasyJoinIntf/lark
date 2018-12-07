package com.lark.comm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统一获取log对象
 */
public class LogUtil {

    public static Logger getLogger(Class<?> clz){
        return LoggerFactory.getLogger(clz);
    }
}
