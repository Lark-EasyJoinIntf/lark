package com.easyjoin.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author Smile.Li
 * @since 2018年11月5日
 */
public class Utils {

	/**
	 * 根据参数及参数值替换字符串中的变量
	 * @param oriStr
	 * @param keys
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String replaceParams(String oriStr, List<String> keys, JSONObject params) throws Exception{
		String sql = oriStr;
		for(String key : keys){
			if(params.containsKey(key)){
				Object val = params.get(key);
				if(val != null){
					if(val instanceof String){
						sql = sql.replaceAll(":"+key, "'"+val.toString()+"'");
					}else{
						sql = sql.replaceAll(":"+key, val.toString());
					}
				}
			}else{
				throw new Exception("缺失参数："+key);
			}
		}
		return sql;
	}
	
	/**
	 * 从字符串中匹配出满足表达式的字符串列表
	 * @param oriStr
	 * @param regex
	 * @return
	 */
	public static List<String> matchs(String oriStr, String regex){
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(oriStr);
		List<String> matches = new ArrayList<String>();
		while (match.find()){
			matches.add(match.group(0).replaceAll(":", "").trim());
		}
		return matches;
	}
	
   /**
    * 转换元到分，输入单位是元，输出是分
    */
   public static long getPenny(BigDecimal dollarRmb) {
	   if (dollarRmb == null) 
		   return 0;
	   else 
		   return dollarRmb.multiply(new BigDecimal(100)).longValue();
   }
   
   
   
   public static String properties(String key)
   {
		Properties prop =  new  Properties(); 
    	InputStream in =  Object.class.getResourceAsStream( "/abframe.properties" );
    	try
		{
			prop.load(in);
		} catch (IOException e)
		{
			e.printStackTrace();
		}    
        String param = prop.getProperty(key).trim();
        return param;
   }
	
	/**
	    * 请求时间戳验证5分钟内有效
	    * @param timestamp
	    * @return 超时返回false
	    */
	public static boolean checkTimestamp(long timestamp){
		if ((System.currentTimeMillis() - timestamp) / 1000 > 300) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 */
	public static JSONObject getWebInput(HttpServletRequest request){
		JSONObject paramsJson = new JSONObject();
		Enumeration<?> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			String value = request.getParameter(key);
			paramsJson.put(key, value);
		}
		return paramsJson;
	}
	
	public static Map<String, Object> getWebInput2(HttpServletRequest request){
		Map<String, Object> paramsJson = new HashMap<String, Object>();
		Enumeration<?> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			String value = request.getParameter(key);
			paramsJson.put(key, value);
		}
		return paramsJson;
	}
	
	public static JSONObject getWebInputByStream(HttpServletRequest request){
		String input="{}";
		try {
			input = inStream2String(request.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JSONObject.parseObject(input);
	}
	
	
	/**
	 * 正则验证，满足正则则返回true，否则false
	 * @param value
	 * @return
	 */
	public static boolean regxMache(String value){
		String regex=
				".*\\s*(select|from|where|update|and|or|delete|insert|truncate|char|into|in|substr|union"
						+ "|ascii|declare|exec|count|master|drop|execute|create|alter|like|script"
						+ "|\\(|\\)|=|>|<|\\*|;|\\'|\")\\s+.*";
		if(value.toLowerCase().matches(regex)){

			return true;
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getParamVals(Map params){
		String vals = "";
		Iterator<?> keys = params.keySet().iterator();
		while(keys.hasNext()){
			vals += params.get(keys.next())+",";
		}
		return vals;
	}
	/**
	 * 将字符串首字母转化成大写
	 * 
	 * @param s
	 * @return
	 */
	public static String firstCharUpperCase(String s) {
		if (s == null || "".equals(s))
			return ("");
		return s.substring(0, 1).toUpperCase(Locale.CHINESE) + s.substring(1);
	}
	
	/**
	 * 将字符串首字母转化成小写
	 * 
	 * @param s
	 * @return
	 */
	public static String firstCharLowerCase(String s) {
		if (s == null || "".equals(s))
			return ("");
		return s.substring(0, 1).toLowerCase(Locale.CHINESE) + s.substring(1);
	}
	
	public static String trimTokenAndNextFirstCharUpperCase(String value, String token)
	{
		if (value == null || "".equals(value))
			return value;

		String[] ss = StringUtils.split(value, token);

		ss[0] = ss[0].toLowerCase();
		for (int i = 1; i < ss.length; i++)
			ss[i] = firstCharUpperCase(ss[i]);

		return StringUtils.join(ss);
	}
	
	public static <T> T[] concatArray(T[] first, T[] second) {  
		T[] result = Arrays.copyOf(first, first.length + second.length);  
		System.arraycopy(second, 0, result, first.length, second.length);  
		return result;  
	}  
	
	/**
	 * 复制对象属性
	 * @param src 源对象
	 * @param target 目标对象
	 */
	public static void copyProperties(Object src, Object target){
		Class<?> targetClazz = target.getClass();
		Class<?> srcClazz = src.getClass();
		Method[] srcMethods =  Utils.concatArray(srcClazz.getDeclaredMethods(), srcClazz.getMethods());
		String srcMethodName = null;
		Object val = null;
		Method execMethod = null;
		for(Method srcm : srcMethods){
			srcMethodName = srcm.getName();
			if(srcMethodName.startsWith("get") && !"getClass".equals(srcMethodName)){
				try {
					val = srcm.invoke(src);
					if(Utils.isValid(val)){
						try{
							execMethod = targetClazz.getDeclaredMethod(srcMethodName.replaceFirst("g", "s"), val.getClass());
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
							execMethod = targetClazz.getMethod(srcMethodName.replaceFirst("g", "s"), val.getClass());
						}
						execMethod.invoke(target, val);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 把JSONObject对象转换成实体对象
	 * 
	 * @param joObject
	 * @param entity
	 */
	public static void toEntity(JSONObject joObject, Object entity) {
		Class<?> clazz = entity.getClass();
		Field[] fields = Utils.concatArray(clazz.getDeclaredFields(), clazz.getFields());
		Method method = null;
		String name = null;
		String methodStr = null;
		Object obj = null;
		Class<?> typeClz = null;
		for (int i = 0; i < fields.length; i++) {
			try {
				name = fields[i].getName();
				methodStr = "set" + firstCharUpperCase(name);
				obj = joObject.get(name);

				if (isValid(obj)) {
					typeClz = fields[i].getType();
					if("Date".equals(typeClz.getSimpleName())){
						if(obj.toString().indexOf("-")>-1){
							obj = DateUtil.format(obj.toString());
						}else{
							long datemill = Long.parseLong(obj.toString());
							obj = new Date(datemill);
						}
					}
					if("String".equals(typeClz.getSimpleName()) 
						&& ("JSONObject".equals(obj.getClass().getSimpleName())||"JSONArray".equals(obj.getClass().getSimpleName())) ){
						obj = obj.toString();
					}
					if("Integer".equals(typeClz.getSimpleName()) ){
						obj = Integer.valueOf(obj.toString());
					}
					if("Long".equals(typeClz.getSimpleName()) ){
						obj = Long.valueOf(obj.toString());
					}
					if("Double".equals(typeClz.getSimpleName()) ){
						obj = Double.valueOf(obj.toString());
					}
					if("BigDecimal".equals(typeClz.getSimpleName()) ){
						obj = new BigDecimal(obj.toString());
					}
					try{
						method = clazz.getDeclaredMethod(methodStr, new Class[] { typeClz });
					} catch (NoSuchMethodException e) {
						method = clazz.getMethod(methodStr, new Class[] { typeClz });
					}
					method.invoke(entity, new Object[] { obj });
				}
			} catch (NoSuchMethodException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (InvocationTargetException e) {
				continue;
			}
		}
	}
	
	public static void toEntity(Map<String, Object> joObject, Object entity) {
		Class<?> clazz = entity.getClass();
		Field[] fields = Utils.concatArray(clazz.getDeclaredFields(), clazz.getFields());
		Method method = null;
		String name = null;
		String methodStr = null;
		Object obj = null;
		Class<?> typeClz = null;
		for (int i = 0; i < fields.length; i++) {
			try {
				name = fields[i].getName();
				methodStr = "set" + firstCharUpperCase(name);
				obj = joObject.get(name);

				if (obj != null) {
					typeClz = fields[i].getType();
					if("Date".equals(typeClz.getSimpleName())){
						if(obj.toString().indexOf("-")>-1){
							obj = DateUtil.format(obj.toString());
						}else{
							long datemill = Long.parseLong(obj.toString());
							obj = new Date(datemill);
						}
					}
					if("String".equals(typeClz.getSimpleName()) 
						&& ("JSONObject".equals(obj.getClass().getSimpleName())||"JSONArray".equals(obj.getClass().getSimpleName())) ){
						obj = obj.toString();
					}
					if("Integer".equals(typeClz.getSimpleName()) ){
						obj = Integer.valueOf(obj.toString());
					}
					if("Long".equals(typeClz.getSimpleName()) ){
						obj = Long.valueOf(obj.toString());
					}
					if("Double".equals(typeClz.getSimpleName()) ){
						obj = Double.valueOf(obj.toString());
					}
					if("BigDecimal".equals(typeClz.getSimpleName()) ){
						obj = new BigDecimal(obj.toString());
					}
					try{
						method = clazz.getDeclaredMethod(methodStr, new Class[] { typeClz });
					} catch (NoSuchMethodException e) {
						method = clazz.getMethod(methodStr, new Class[] { typeClz });
					}
					method.invoke(entity, new Object[] { obj });
				}
			} catch (NoSuchMethodException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (InvocationTargetException e) {
				continue;
			}
		}
	}
	/**
	 * 将Map转换为JSON
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject mapToJSONOBject(Map<String, Object> params) {
		JSONObject json = new JSONObject();
		if (params == null) {
			return json;
		}
		if (params.containsKey("object")) {
			Object obj = params.get("object");
			if (obj instanceof Map) {
				JSONObject jobj = mapToJSONOBject((Map<String, Object>) obj);
				params.put("object", jobj);
			}
		}
		Iterator<String> it = params.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			json.put(key, params.get(key));
		}
		return json;
	}
	
	public static Map<String, Object> jsonToMap(JSONObject json) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (json == null) {
			return map;
		}
		Iterator<String> it = json.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			map.put(key, json.get(key));
		}
		return map;
	}
	
	public static void entityToJSON(JSONObject json, Object entity) {
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				String name = fields[i].getName();
				String methodStr = "get" + firstCharUpperCase(name);
				Method method = clazz.getDeclaredMethod(methodStr);
				Object obj = method.invoke(entity);
				json.put(name, obj);
			} catch (NoSuchMethodException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (InvocationTargetException e) {
				continue;
			}
		}
	}
	
	public static void entityToMap(Map<String, Object> json, Object entity) {
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				String name = fields[i].getName();
				String methodStr = "get" + firstCharUpperCase(name);
				Method method = clazz.getDeclaredMethod(methodStr);
				Object obj = method.invoke(entity);
				json.put(name, obj);
			} catch (NoSuchMethodException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (InvocationTargetException e) {
				continue;
			}
		}
	}
	
	
	

	
	/**
	 * 按字母顺序获取参数拼接串
	 * @param params
	 * @return
	 */
	public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }
	
	public static String inStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return new String(baos.toByteArray(), "UTF-8");
    }
	
	/**
	 * 判断值是否存在（不等于null且不为空值）
	 * @param value
	 * @return
	 */
	public static boolean isValid(String value){
		return (value != null && !value.equals("")) ? true : false ;
	}
	
	/**
	 * 判断列表是否存在（不等于null且至少含有一个值）
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> boolean isValid(List<T> list){
		return (list != null && list.size()>0) ? true : false ;
	}
	
	public static boolean isValid(Object val){
		return (val != null && !val.equals("")) ? true : false ;
	}
	
	public static boolean isValid(int val){
		return (val > 0) ? true : false ;
	}
	/**
	 * 将对象转化为String
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj){
		return (obj != null ? obj.toString() : "");
	}
	
	
	
	/**
	 * 将实体转换为JSONObject
	 * @param entity
	 */
	public static JSONObject entityToJSONObject(Object entity) {
		JSONObject json = new JSONObject();
		if(entity != null){
			Class<?> clazz = entity.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				try {
					String name = fields[i].getName();
					String methodStr = "get" + firstCharUpperCase(name);
					Method method = clazz.getDeclaredMethod(methodStr);
					Object obj = method.invoke(entity);
					if(obj != null && obj instanceof String){
						obj = obj.toString().replaceAll("(\n|\r|(\r\n)|(\u0085)|(\u2028)|(\u2029))", "#rn#");
					}
					json.put(name, obj);
				} catch (NoSuchMethodException e) {
					continue;
				} catch (IllegalAccessException e) {
					continue;
				} catch (IllegalArgumentException e) {
					continue;
				} catch (InvocationTargetException e) {
					continue;
				}
			}
		}
		return json;
	}
}
