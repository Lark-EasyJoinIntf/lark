package com.easyjoin.util.http;

import com.alibaba.fastjson.JSONObject;

/**
 * 请求结果转换接口
 * @author Smile.Li
 * @since 2018年9月19日
 */
public interface DataParser {
	
	/**
	 * 请求参数转换
	 * @param injson
	 * @return
	 */
	public String paramParse(JSONObject injson);
	
	/**
	 * 结果转换方法
	 * @param rs 几千块请求返回结果字符串
	 * @param _DATA_MAP 数据节点映射
	 * @param _OUT_MAP 标准输出映射
	 * @return
	 */
	public JSONObject resultParse(String rs, JSONObject _DATA_MAP, JSONObject _OUT_MAP);
}
