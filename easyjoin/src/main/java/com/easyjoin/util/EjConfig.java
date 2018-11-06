package com.easyjoin.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author Smile.Li
 * @since 2018年11月5日
 */
public class EjConfig {
	
	private static Properties props = new Properties();
	
	static {
		try {
			InputStream in = EjConfig.class.getClassLoader().getResourceAsStream("easyjoin.properties");
			props.load(in);
			in.close();
		} catch (Exception ex) {
			props = null;
		}
	}
	
	/**
	 * 通过key获取支付配置文件中的值
	 * @param key
	 * @return
	 */
	public static String get(String key){
		if(props != null && props.containsKey(key)){
			return props.getProperty(key);
		}
		return null;
	}
	
	/**
	 * 接口映射文件地址
	 */
	public static final String MapfilePath = "mapfile.path";
}
