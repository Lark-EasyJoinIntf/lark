package com.easyjoin.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easyjoin.util.http.HttpSender;
import com.easyjoin.util.http.ParserHelper;

/**
 * 接口配置信息获取工具
 * @author Smile.Li
 * @since 2018年8月22日
 */
public class IntfUtil {
	private static String filePath = EjConfig.get(EjConfig.MapfilePath); //"D:/easyjoin/";
	
	/*public static void main(String[] args) {
		
		JSONObject reqParams = JSONObject.parseObject("{\"userName\":\"Nico.Li\", \"linkList\":[{\"tel\":\"18976546517\"},{\"tel\":\"18198989898\"}]}");
		JSONObject intfConf = JSONObject.parseObject("{\"headerMap\":[{\"paramCode\":\"GH_CODE\", \"paramVal\":\"#ghCode#\"}], \"paramMap\":["
				+"{\"nodeName\":\"root\", \"children\":[{\"nodeName\":\"user_name\", \"attrName\":\"userName\"}, "
				+"{\"nodeName\":\"linkInfo\", \"attrName\":\"linkList\", \"attrType\":\"list\", \"children\":["
				+"{\"nodeName\":\"info\", \"children\":["
				+ "{\"nodeName\":\"telphone\", \"attrName\":\"tel\"}"
				+ "]}]}"
				+ "]}"
				+ "]}");
		JSONObject result = getReqParams(reqParams, "ZHLH", "login");
		System.out.println(result);
	}*/
	
	/**
	 * 接口请求方法
	 * @param reqParams
	 * @param provider
	 * @param intfCode
	 * @return
	 */
	public static JSONObject sendPost(JSONObject reqParams, String provider, String intfCode){
		JSONObject inParam = IntfUtil.getReqParams(reqParams, provider, intfCode);
		String parserType = inParam.getString("contentType");
		return HttpSender.post(inParam, ParserHelper.getParser(parserType));
	}
	
	/**
	 * 转化获取接口请求参数
	 * @param reqParams 请求参数
	 * @param provider 接口提供者
	 * @param intfCode 接口编码/方法
	 * @return 转换后的接口入参对象{_HEADER:{},_IN_MAP:{}, _OUT_MAP:{}, _RS_MAP:{}, _URL:'', _TYPE:'', _INTF:'', contentType:''}
	 */
	public static JSONObject getReqParams(JSONObject reqParams, String provider, String intfCode){
		JSONObject intfConf = getIntfConf(provider, intfCode);
		return IntfUtil.convertParams(reqParams, intfConf);
	}
	
	/**
	 * 
	 * @param reqParams
	 * @param intfConf
	 * @return
	 */
	private static JSONObject convertParams(JSONObject reqParams, JSONObject intfConf){
		JSONObject inParams =  new JSONObject();
		JSONArray headerMap = intfConf.getJSONArray("headerMap");
		if(headerMap != null && headerMap.size()>0){//请求头处理
			JSONObject h = null, header = new JSONObject();
			for(int i=0; i<headerMap.size(); i++){
				h = headerMap.getJSONObject(i);
				String val = h.getString("paramVal");
				if(val != null){
					val = val.trim();
					if(val.startsWith("#") && val.endsWith("#") && reqParams.containsKey(val.replaceAll("#", ""))){
						header.put(h.getString("paramCode"), reqParams.getString(val.replaceAll("#", "")));
					}else{					
						header.put(h.getString("paramCode"), val);
					}
				}else{
					header.put(h.getString("paramCode"), "");
				}
			}
			inParams.put("_HEADER", header);
		}
		JSONArray paramMap = intfConf.getJSONArray("paramMap");
		JSONObject newObj = null;
		for(int i=0; i<paramMap.size(); i++){
			newObj = JSONObject.parseObject(JSONObject.toJSONString(paramMap.getJSONObject(i)));
			setNodeVal(reqParams, intfConf.getString("provider"), newObj);
		}
		
		inParams.put("_IN_MAP", newObj);
		inParams.put("_URL", intfConf.getString("reqUrl"));
		inParams.put("_INTF", intfConf.getString("intfCode"));
		inParams.put("_TYPE", intfConf.getString("reqType"));
		inParams.put("contentType", intfConf.getString("reqFormat"));
		inParams.put("_OUT_MAP", intfConf.getJSONArray("outMap").getJSONObject(0));
		inParams.put("_DATA_MAP", intfConf.getJSONArray("resultMap").getJSONObject(0));
		return inParams;
	}
	
	private static void setNodeVal(JSONObject reqParams, String provider, JSONObject json){
		String nodeName = json.getString("nodeName");
		String attrName = json.getString("attrName");
		String attrType = json.getString("attrType");
		String keyName = (attrName != null && attrName.length()>0 ? attrName : nodeName);
		if("auto".equalsIgnoreCase(attrType)){
			json.put("value", IdCreatorUtil.ConfirmDateSerializeId(provider, 4));
		}else if("fit".equalsIgnoreCase(attrType)){
			json.put("value", attrName);
		}else{
			if(json.containsKey("children") && !"".equals(json.get("children"))){//存在子节点
				if("list".equalsIgnoreCase(attrType)){
					if(reqParams.containsKey(keyName)){
						JSONArray subArr = json.getJSONArray("children");
						JSONObject tempJson = JSONObject.parseObject(JSONObject.toJSONString(subArr.get(0)));
						JSONArray dataArr = reqParams.getJSONArray(keyName);
						setNodeVal(dataArr.getJSONObject(0), provider, subArr.getJSONObject(0));
						
						for(int i=1; i<dataArr.size(); i++){
							JSONObject node = JSONObject.parseObject(JSONObject.toJSONString(tempJson.clone()));
							setNodeVal(dataArr.getJSONObject(i), provider, node);
							subArr.add(node);
						}
					}
				}else{
					JSONArray subArr = json.getJSONArray("children");
					for(int i=0; i<subArr.size(); i++){
						JSONObject subJson = subArr.getJSONObject(i);
						setNodeVal(reqParams, provider, subJson);
					}
				}
			}else{
				if(reqParams.containsKey(keyName)){
					json.put("value", reqParams.getString(keyName));
				}
			}
		}
	}
	/**
	 * 通过provider编码和intfCode接口编码获取接口配置数据
	 * @param provider 接口提供者编码
	 * @param intfCode 接口编码
	 * @return
	 */
	public static JSONObject getIntfConf(String provider, String intfCode){
		//通过provider获取标准输出及请求头
		JSONObject provData = IntfUtil.getProvider(provider);
		//通过provider和intfCode获取接口数据
		JSONObject intfData = IntfUtil.getIntf(provider, intfCode);
		if(provData != null && intfData != null){
			JSONObject headerMap = provData.getJSONObject("headerMap");
			if(headerMap!=null && headerMap.containsKey("rows"))
				intfData.put("headerMap", headerMap.getJSONArray("rows"));
			else
				intfData.put("headerMap", new JSONArray());
			intfData.put("outMap", provData.getJSONArray("outMap"));
		}
		intfData.put("reqUrl", provData.getString("reqUrl"));
		return intfData;
	}
	
	private static JSONObject getProvider(String provider){
		String dataStr = FileUtil.readFile(filePath+"/files/provider.json");
		JSONObject datas = JSONObject.parseObject(dataStr);
		JSONArray rows = datas.getJSONArray("rows");
		JSONObject row = null;
		for(int i=0; i<rows.size(); i++){
			row = rows.getJSONObject(i);
			if(provider!=null && provider.equals(row.getString("code"))){
				break;
			}else{
				row = null;
			}
		}
		return row;
	}
	
	private static JSONObject getIntf(String provider, String intfCode){
		String dataStr = FileUtil.readFile(filePath+"/files/"+provider+"/"+provider+".json");
		JSONObject datas = JSONObject.parseObject(dataStr);
		JSONArray rows = datas.getJSONArray("rows");
		JSONObject row = null;
		for(int i=0; i<rows.size(); i++){
			row = rows.getJSONObject(i);
			if(intfCode!=null && intfCode.equals(row.getString("intfCode"))){
				break;
			}else{
				row = null;
			}
		}
		return row;
	}
}
