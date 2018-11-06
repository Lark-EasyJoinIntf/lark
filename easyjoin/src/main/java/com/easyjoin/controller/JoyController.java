package com.easyjoin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easyjoin.util.EjConfig;
import com.easyjoin.util.FileUtil;
import com.easyjoin.util.IdCreatorUtil;
import com.easyjoin.util.IntfUtil;
import com.easyjoin.util.Utils;
import com.easyjoin.util.mapper.JsonMapper;

/**
 * @author Lixc
 * @version: 1.0
 * @since Sun, 08 Jul 2018 02:23:04 GMT
 */
@Controller
@RequestMapping(value = "/joy")
public class JoyController {
	private String filePath = EjConfig.get(EjConfig.MapfilePath);
	
	private String getFilePath(HttpServletRequest request){
		if(filePath != null && filePath.trim().length()>0){
			return filePath+"/files/";
		}else{
			return request.getRealPath("/files/");
		}
	}

	@RequestMapping(value = {"saveProvider"}, method = RequestMethod.POST)
	public String saveProvider(HttpServletRequest request, HttpServletResponse response){
		JSONObject result = new JSONObject();
		JSONObject data = Utils.getWebInput(request);
		String path = getFilePath(request);
		String oldDataStr = FileUtil.readFile(path + "provider.json");
		if(oldDataStr == null || "".equals(oldDataStr.trim())){
			oldDataStr = "{\"rows\":[]}";
		}
		
		boolean isNew = true;
		JSONObject same = null;
		JSONObject oldData = JSONObject.parseObject(oldDataStr);
		for(int i=0; i<oldData.getJSONArray("rows").size(); i++){
			JSONObject ddd = oldData.getJSONArray("rows").getJSONObject(i);
			if(ddd.getString("code").equals(data.getString("code"))){
				isNew = false;
				same = ddd;
				break;
			}
		}
		if(isNew){
			data.put("id", IdCreatorUtil.ConfirmIntId());
			data.put("headerMap", JSONObject.parseObject(data.getString("headerMap")));
			data.put("outMap", JSONArray.parseArray(data.getString("outMap")));
			oldData.getJSONArray("rows").add(data);
			FileUtil.saveFile(path+"provider.json", oldData.toJSONString());
			result.put("success", true);
			result.put("msg", "新增成功");
		}else{
			if(null != data.getString("id") && !"".equals(data.getString("id"))){
				same.put("provider", data.getString("provider"));
				same.put("remark", data.getString("remark"));
				same.put("reqUrl", data.getString("reqUrl"));
				same.put("headerMap", JSONObject.parseObject(data.getString("headerMap")));
				same.put("outMap", JSONArray.parseArray(data.getString("outMap")));
				FileUtil.saveFile(path+"provider.json", oldData.toJSONString());
				result.put("success", true);
				result.put("msg", "编辑成功");
			}else{
				result.put("success", false);
				result.put("msg", "编码已经存在");
			}
		}
		return JsonMapper.write(response, result);
	}
	
	@RequestMapping(value = {"readProvider"}, method = RequestMethod.POST)
	public String readProvider(HttpServletRequest request, HttpServletResponse response){
		String path = getFilePath(request);
		String oldDataStr = FileUtil.readFile(path + "provider.json");
		JSONObject data = JSONObject.parseObject(oldDataStr);
		JSONObject param = Utils.getWebInput(request);
		if(param.containsKey("provider")){
			JSONArray newArr = new JSONArray();
			String inp = param.getString("provider");
			JSONArray arr = data.getJSONArray("rows");
			for(int i=0; i<arr.size(); i++){
				String code = arr.getJSONObject(i).getString("code");
				String provider = arr.getJSONObject(i).getString("provider");
				if(inp.equals(code) || provider.indexOf(inp)>-1){
					newArr.add(arr.getJSONObject(i));
				}
			}
			data.put("rows", newArr);
		}
		return JsonMapper.write(response, data);
	}
	
	@RequestMapping(value = {"saveIntf"}, method = RequestMethod.POST)
	public String saveIntf(HttpServletRequest request, HttpServletResponse response){
		JSONObject result = new JSONObject();
		JSONObject data = Utils.getWebInput(request);
		String provider = data.getString("provider");
		String intfCode = data.getString("intfCode");
		String path = getFilePath(request);
		FileUtil.creatDir(path+provider);
		String oldDataStr = FileUtil.readFile(path+provider+"/"+provider+".json");
		if(oldDataStr == null || "".equals(oldDataStr.trim())){
			oldDataStr = "{\"rows\":[]}";
		}
		boolean isNew = true;
		JSONObject same = null;
		JSONObject oldData = JSONObject.parseObject(oldDataStr);
		for(int i=0; i<oldData.getJSONArray("rows").size(); i++){
			JSONObject da = oldData.getJSONArray("rows").getJSONObject(i);
			if(da.getString("intfCode").equals(intfCode)){
				isNew = false;
				same = da;
				break;
			}
		}
		if(isNew){
			data.put("id", IdCreatorUtil.ConfirmIntId());
			data.put("paramMap", JSONArray.parseArray(data.getString("paramMap"))); 
			data.put("resultMap", JSONArray.parseArray(data.getString("resultMap"))); 
			oldData.getJSONArray("rows").add(data);
			FileUtil.saveFile(path+provider+"/"+provider+".json", oldData.toJSONString());
			result.put("success", true);
			result.put("msg", "新增成功");
		}else{
			if(null != data.getString("id") && !"".equals(data.getString("id"))){
				same.put("intfName", data.getString("intfName"));
				same.put("reqType", data.getString("reqType"));
				same.put("reqFormat", data.getString("reqFormat"));
				same.put("paramMap", JSONArray.parseArray(data.getString("paramMap"))); 
				same.put("resultMap", JSONArray.parseArray(data.getString("resultMap"))); 
				FileUtil.saveFile(path+provider+"/"+provider+".json", oldData.toJSONString());
				result.put("success", true);
				result.put("msg", "编辑成功");
			}else{
				result.put("success", false);
				result.put("msg", "编码已经存在");
			}
		}
		return JsonMapper.write(response, result);
	}
	
	@RequestMapping(value = {"readIntfList"}, method = RequestMethod.POST)
	public String readIntfList(HttpServletRequest request, HttpServletResponse response){
		JSONObject params = Utils.getWebInput(request);
		String provider = params.getString("provider");
		String path = getFilePath(request);
		String oldDataStr = "{\"rows\":[]}";
		if(provider!=null && !"".equals(provider)){
			String dataStr = FileUtil.readFile(path+provider+"/"+provider+".json");
			if(dataStr != null && !"".equals(dataStr)){
				oldDataStr = dataStr;
			}
		}
		return JsonMapper.write(response, JSONObject.parseObject(oldDataStr));
	}
	
	@RequestMapping(value = {"testIntf"}, method = RequestMethod.POST)
	public String testIntf(HttpServletRequest request, HttpServletResponse response){
		JSONObject params = Utils.getWebInput(request);
		String provider = params.getString("provider");
		String intfCode = params.getString("intfCode");
		String reqParamsStr = params.getString("reqParams");
		JSONObject result = new JSONObject();
		if(reqParamsStr != null && reqParamsStr.trim().length()>0){
			JSONObject reqParams = JSONObject.parseObject(reqParamsStr);
			result = IntfUtil.sendPost(reqParams, provider, intfCode);
		}else{
			result.put("msg", "参数为JSON格式");
		}
		return JsonMapper.write(response, result);
	}
	
}
