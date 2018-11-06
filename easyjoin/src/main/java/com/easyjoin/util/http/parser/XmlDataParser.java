package com.easyjoin.util.http.parser;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.easyjoin.util.http.DataParser;
import com.easyjoin.util.xml.XmlUtil;

/**
 * 
 * @author Smile.Li
 * @since 2018年11月5日
 */
@Component("xmlDataParser")
public class XmlDataParser implements DataParser {

	@Override
	public String paramParse(JSONObject injson) {
		return XmlUtil.jsonParseXml(injson, false);
	}
	
	@Override
	public JSONObject resultParse(String rs, JSONObject _DATA_MAP, JSONObject _OUT_MAP) {
		return XmlUtil.xmlParseJson(rs, _DATA_MAP, _OUT_MAP);
	}

}
