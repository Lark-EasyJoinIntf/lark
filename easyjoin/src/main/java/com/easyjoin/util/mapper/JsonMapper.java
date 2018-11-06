package com.easyjoin.util.mapper;

import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * 
 * @author Smile.Li
 * @since 2018年11月5日
 */
public class JsonMapper extends ObjectMapper {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);
	private static JsonMapper mapper;

	public JsonMapper() {
		this(JsonInclude.Include.NON_EMPTY);
	}

	public JsonMapper(JsonInclude.Include include) {
		if (include != null) {
			setSerializationInclusion(include);
		}

		enableSimple();

		disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			public void serialize(Object value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				jgen.writeString("");
			}
		});
		registerModule(new SimpleModule().addSerializer(String.class,
				new JsonSerializer<String>() {
					@Override
					public void serialize(String value, JsonGenerator jgen,
							SerializerProvider provider) throws IOException,
							JsonProcessingException {
						jgen.writeString(StringEscapeUtils.unescapeHtml4(value));
					}
				}));
		setTimeZone(TimeZone.getDefault());
	}

	public static JsonMapper getInstance() {
		if (mapper == null) {
			mapper = new JsonMapper().enableSimple();
		}
		return mapper;
	}

	public static JsonMapper nonDefaultMapper() {
		if (mapper == null) {
			mapper = new JsonMapper(JsonInclude.Include.NON_DEFAULT);
		}
		return mapper;
	}

	public String toJson(Object object) {
		try {
			return writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
		}
		return null;
	}

	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString))
			return null;
		try {
			return readValue(jsonString, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
		}
		return null;
	}

	public <T> T fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString))
			return null;
		try {
			return readValue(jsonString, javaType);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
		}
		return null;
	}

	public JavaType createCollectionType(Class<?> collectionClass,
			Class<?>[] elementClasses) {
		return getTypeFactory().constructParametricType(collectionClass,
				elementClasses);
	}

	public <T> T update(String jsonString, T object) {
		try {
			return readerForUpdating(object).readValue(jsonString);
		} catch (JsonProcessingException e) {
			logger.warn("update json string:" + jsonString + " to object:"
					+ object + " error.", e);
		} catch (IOException e) {
			logger.warn("update json string:" + jsonString + " to object:"
					+ object + " error.", e);
		}
		return null;
	}

	public String toJsonP(String functionName, Object object) {
		return toJson(new JSONPObject(functionName, object));
	}

	public JsonMapper enableEnumUseToString() {
		enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		return this;
	}

	public JsonMapper enableJaxbAnnotation() {
		JaxbAnnotationModule module = new JaxbAnnotationModule();
		registerModule(module);
		return this;
	}

	public JsonMapper enableSimple() {
		configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return this;
	}

	public ObjectMapper getMapper() {
		return this;
	}

	public static JSONObject toJsonObject(Object object) {
		return JSONObject.parseObject(getInstance().toJson(object));
	}
	
	public static String toJsonString(Object object) {
		return getInstance().toJson(object);
	}
	
	public static Object fromJsonString(String jsonString, Class<?> clazz) {
		return getInstance().fromJson(jsonString, clazz);
	}

	public static String write(HttpServletResponse response, Object object) {
		boolean isJson = true;
		if(object instanceof List || object instanceof JSONArray){
			isJson = false;
		}
		return renderString(response, JsonMapper.toJsonString(object),
				"application/json",isJson);
	}

	public static String renderString(HttpServletResponse response, String string,
			String type, boolean isJson) {
		try {
			response.reset();
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			if(isJson){
				JSONObject json = JSONObject.parseObject(string);
				if(!json.containsKey("rows")){
					json.put("rows", new JSONArray());
				}
				response.getWriter().print(json.toJSONString());
			}else{
				response.getWriter().print(string);
			}
			return null;
		} catch (IOException e) {
		}
		return null;
	}
	
}