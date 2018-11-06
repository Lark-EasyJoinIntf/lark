package com.easyjoin.util.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.easyjoin.util.xml.XmlUtil;

/**
 * 
 * @author Smile.Li
 * @since 2018年11月5日
 */
public class HttpSender {
    
	/**
	 * 
	 * @param url
	 * @param params
	 * @param contentType json,url,file
	 * @return
	 */
    @SuppressWarnings({ "deprecation", "resource" })
	public static String postSend(String url, String params, String contentType){
    	HttpClient client = new DefaultHttpClient();
    	HttpPost postMethod = new HttpPost(url);
    	String body = null;
        try {
        	if("json".equals(contentType)){
        		postMethod.addHeader("Content-type","application/json; charset=utf-8");  
        		postMethod.setHeader("Accept", "application/json");  
        	} if("file".equals(contentType)) {
        		postMethod.addHeader("Content-type","multipart/form-data; charset=utf-8");
        	} else{
        		postMethod.addHeader("Content-type","application/x-www-form-urlencoded; charset=utf-8");  
        	}
			postMethod.setEntity(new StringEntity(params, Charset.forName("UTF-8"))); 
			HttpResponse response = client.execute(postMethod);
			int statusCode = response.getStatusLine().getStatusCode();  
			if (statusCode != HttpStatus.SC_OK) {  
				
			}  
			//Read the response body   
			InputStream is = response.getEntity().getContent();
			body = inStream2String(is);
			System.out.println(body);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return body;
    }
    
    
    /**
	 * 
	 * @param url
	 * @param params
	 * @param contentType json,url,file
	 * @return
	 */
    @SuppressWarnings({ "deprecation", "resource" })
	public static InputStream postSendForInputStream(String url, String params, String contentType){
    	HttpClient client = new DefaultHttpClient();
    	HttpPost postMethod = new HttpPost(url);
        try {
        	if("json".equals(contentType)){
        		postMethod.addHeader("Content-type","application/json; charset=utf-8");  
        		postMethod.setHeader("Accept", "application/json");  
        	} if("file".equals(contentType)) {
        		postMethod.addHeader("Content-type","multipart/form-data; charset=utf-8");
        	} else{
        		postMethod.addHeader("Content-type","application/x-www-form-urlencoded; charset=utf-8");  
        	}
			postMethod.setEntity(new StringEntity(params, Charset.forName("UTF-8"))); 
			HttpResponse response = client.execute(postMethod);
			int statusCode = response.getStatusLine().getStatusCode();  
			if (statusCode != HttpStatus.SC_OK) {  
				
			}  
			//Read the response body   
			InputStream is = response.getEntity().getContent();
			return is;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    
    
    
	@SuppressWarnings({ "deprecation", "resource" })
	public static String postSend(String url, Map<String, String> paramsMap) {
		HttpClient client = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(url);
		String body = null;
		try {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			Iterator<String> it = paramsMap.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				params.add(new BasicNameValuePair(key, paramsMap.get(key)));
			}
			HttpEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");//指定编码方式，否则中文时会乱码
			postMethod.setEntity(formEntity);
			HttpResponse response = client.execute(postMethod);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {

			}
			// Read the response body
			InputStream is = response.getEntity().getContent();
			body = inStream2String(is);
			System.out.println(body);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}
	
	@SuppressWarnings({ "deprecation", "resource" })
	public static String getSend(String url, Map<String, String> paramsMap) {
		HttpClient client = new DefaultHttpClient();
		String body = null;
		try {
			StringBuffer sb = new StringBuffer(512);
			Iterator<String> it = paramsMap.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				sb.append(key);
				sb.append("=");
				sb.append(paramsMap.get(key));
				sb.append("&");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
	        }
			HttpGet httpget = new HttpGet(url + (sb.length()>0 ? "?"+sb.toString():""));
			httpget.addHeader("Content-type","application/x-www-form-urlencoded; charset=utf-8");  
			HttpResponse response = client.execute(httpget);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {

			}
			// Read the response body
			InputStream is = response.getEntity().getContent();
			body = inStream2String(is);
			System.out.println(body);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}
	
	// 将输入流转换成字符串
    private static String inStream2String(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return new String(baos.toByteArray(), "UTF-8");
    }
    
    final public static String CNT_TYPE_JSON = "JSON";
	final public static String CNT_TYPE_XML = "XML";
	
	@Deprecated
    @SuppressWarnings({ "deprecation", "resource" })
	public static JSONObject post(JSONObject json){
		String _TYPE = json.getString("_TYPE");
		String url = json.getString("_URL");
		if("METHOD".equalsIgnoreCase(_TYPE)){//方法形式的请求
			url += json.getString("_INTF");
		}
		HttpPost request = new HttpPost(url);
		if(json != null && json.containsKey("_HEADER") && json.getJSONObject("_HEADER")!=null){//设置请求头
			JSONObject header = json.getJSONObject("_HEADER");
			Iterator<String> it = header.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				request.addHeader(key, header.getString(key));
			}
			json.remove("_HEADER");
		}
		String contentType = json.getString("contentType");
		if(CNT_TYPE_JSON.contains(contentType.toUpperCase())){
			request.setHeader("Accept", "application/json");
			request.setEntity(new StringEntity(json.toString(), Charset.forName("UTF-8")));
			request.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
		}else if(CNT_TYPE_XML.contains(contentType.toUpperCase())){//设置CONTENT_TYPE等，将json对象转换为xml请求参数格式
			request.addHeader("Accept", "text/xml");
			//request.addHeader("Content-Type", "text/xml; charset=GBK");
			String xml = XmlUtil.jsonParseXml(json.getJSONObject("_IN_MAP"), false);
			request.setEntity(new StringEntity(xml, Charset.forName("GBK")));
		}
        
		JSONObject result = new JSONObject();
		result.put("code", 0);
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); 
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000); 
		try {
			HttpResponse response = client.execute(request);
			String rs = EntityUtils.toString(response.getEntity());
	        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	        	if(CNT_TYPE_JSON.contains(contentType.toUpperCase())){
	        		result = JSONObject.parseObject(rs);
	        	}else if(CNT_TYPE_XML.contains(contentType.toUpperCase())){
	        		result = XmlUtil.xmlParseJson(rs, json.getJSONObject("_DATA_MAP"), json.getJSONObject("_OUT_MAP"));
	        	}
			}else{
				result.put("msg", rs);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
		} finally {
			if(client != null) {
				client.getConnectionManager().shutdown(); 
			}
		}
        return result;
	}
    
	/**
     * 
     * @param param 
     * @param paramParser 参数转换接口
     * @param rsp 结果转换接口
     * @return
     */
    public static JSONObject post(PostParam  param, DataParser rsp){
    	return post(param.get_TYPE(), param.get_URL(), param.get_INTF(), param.getContentType(), 
    			param.get_HEADER(), param.get_IN_MAP(), param.get_DATA_MAP(), param.get_OUT_MAP(), rsp);
    }
    /**
     * 
     * @param param 包含_TYPE, _URL, _INTF, contentType, _IN_MAP, _DATA_MAP, _OUT_MAP, _HEADER
     * @param paramParser 参数转换接口
     * @param rsp 结果转换接口
     * @return
     */
    public static JSONObject post(JSONObject param, DataParser rsp){
    	String _TYPE = param.getString("_TYPE");
		String _URL = param.getString("_URL");
		String _INTF =  param.getString("_INTF");
		String contentType = param.getString("contentType");
		JSONObject header = param.containsKey("_HEADER") ? param.getJSONObject("_HEADER") : null;
		JSONObject _IN_MAP = param.getJSONObject("_IN_MAP");
		JSONObject _DATA_MAP = param.getJSONObject("_DATA_MAP");
		JSONObject _OUT_MAP = param.getJSONObject("_OUT_MAP");
    	return post( _TYPE, _URL, _INTF, contentType, header, _IN_MAP, _DATA_MAP, _OUT_MAP, rsp);
    }
    
    private static JSONObject post(String _TYPE,String _URL,String _INTF,String contentType,
    		JSONObject _HEADER,JSONObject _IN_MAP,JSONObject _DATA_MAP,JSONObject _OUT_MAP, DataParser dp){
		String url = _URL;
		if("METHOD".equalsIgnoreCase(_TYPE)){//方法形式的请求
			url += _INTF;
		}
		HttpPost request = new HttpPost(url);
		if(_INTF != null){//设置请求头
			Iterator<String> it = _HEADER.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				request.addHeader(key, _HEADER.getString(key));
			}
		}

		if(CNT_TYPE_JSON.contains(contentType.toUpperCase())){
			request.setHeader("Accept", "application/json");
			request.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
		}else if(CNT_TYPE_XML.contains(contentType.toUpperCase())){//设置CONTENT_TYPE等，将json对象转换为xml请求参数格式
			request.addHeader("Accept", "text/xml");
		}
		request.setEntity(new StringEntity(dp.paramParse(_IN_MAP), Charset.forName("GBK")));
		
		JSONObject result = new JSONObject();
		result.put("code", 0);
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); 
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000); 
		try {
			HttpResponse response = client.execute(request);
			String rs = EntityUtils.toString(response.getEntity());
	        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	        	result = dp.resultParse(rs, _DATA_MAP, _OUT_MAP); 
			}else{
				result.put("msg", rs);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
		} finally {
			if(client != null) {
				client.getConnectionManager().shutdown(); 
			}
		}
        return result;
    }
 
    public static class PostParam{
    	private String _TYPE;
    	private String _URL;
    	private String _INTF;
    	private String contentType;
    	private JSONObject _HEADER;
    	private JSONObject _IN_MAP;
    	private JSONObject _DATA_MAP;
    	private JSONObject _OUT_MAP;
    	
		public String get_TYPE() {
			return _TYPE;
		}
		public void set_TYPE(String _TYPE) {
			this._TYPE = _TYPE;
		}
		public String get_URL() {
			return _URL;
		}
		public void set_URL(String _URL) {
			this._URL = _URL;
		}
		public String get_INTF() {
			return _INTF;
		}
		public void set_INTF(String _INTF) {
			this._INTF = _INTF;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public JSONObject get_HEADER() {
			return _HEADER;
		}
		public void set_HEADER(JSONObject _HEADER) {
			this._HEADER = _HEADER;
		}
		public JSONObject get_IN_MAP() {
			return _IN_MAP;
		}
		public void set_IN_MAP(JSONObject _IN_MAP) {
			this._IN_MAP = _IN_MAP;
		}
		public JSONObject get_DATA_MAP() {
			return _DATA_MAP;
		}
		public void set_DATA_MAP(JSONObject _DATA_MAP) {
			this._DATA_MAP = _DATA_MAP;
		}
		public JSONObject get_OUT_MAP() {
			return _OUT_MAP;
		}
		public void set_OUT_MAP(JSONObject _OUT_MAP) {
			this._OUT_MAP = _OUT_MAP;
		}
    }
}

