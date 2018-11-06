package com.easyjoin.util.http;

import com.easyjoin.util.SpringUtils;

/**
 * 转换器帮助类
 * @author Smile.Li
 * @since 2018年11月4日
 */
public class ParserHelper {
	
	/**
	 * 获取转换器
	 * @param parserType 转换类型，根据请求及应答方式：xml，json等确定
	 * @return
	 */
	public static DataParser getParser(String parserType){
		return SpringUtils.getBean(parserType.toLowerCase()+"DataParser");
	}
	
}
