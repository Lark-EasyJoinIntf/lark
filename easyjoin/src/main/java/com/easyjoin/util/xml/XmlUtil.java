package com.easyjoin.util.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easyjoin.util.IdCreatorUtil;

/**
 * 根据xml模板填充数据，依赖xml-apis<br/>
 * 	&lt;dependency><br/>
		&lt;groupId>xml-apis&lt;/groupId><br/>
		&lt;artifactId>xml-apis&lt;/artifactId><br/>
		&lt;version>1.4.01&lt;/version><br/>
	&lt;/dependency><br/>
 * @author lxc
 * @since 2017-12-27
 */
public class XmlUtil {
	private final static Logger logger = LoggerFactory.getLogger(XmlUtil.class);
	private final static String tempPath = "";
	
	private static DocumentBuilder docBuilder;
	static{
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载模板文件，优先加载自定义的文件夹目录的文件，不存在则去加载工程文件夹xmltemplete下的文件
	 * @param direc 自定义的文件夹目录
	 * @param filename 模板文件名
	 * @return
	 */
	public static InputStream getTempleteIs(String direc, String filename){
		File file = null;
		if(direc == null){
			file = new File(filename);
		}else{
			file = new File(direc+File.separator+filename);
		}
		InputStream is = null;
		if(file.exists()){
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				logger.error(filename+"不存在！准备加载xmltemplete下的文件");
				is = XmlUtil.class.getResourceAsStream(direc+File.separator+filename);
			}
		}else{
			is = XmlUtil.class.getResourceAsStream(direc+File.separator+filename);
		}
		return is;
	}
	
	/**
	 * 加载xml模板文件,先加载配置文件属性（temp.filepath）文件夹下的文件，没有则加载工程文件夹xmltemplete下的文件
	 * @param filename 文件路径
	 * @return Document对象
	 */
	public static Document loadXmlFile(String filename){
		Document doc = null;
		InputStream is = null;
		try {
			is = getTempleteIs(tempPath, filename);
			if(is != null){
				doc = docBuilder.parse(is);
			}else{
				logger.error(filename+"不存在！");
			}
		}catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return doc;
	}
	
	/**
	 * 通过模板及数据解析为凭证xml<br>
	 * 先加载配置文件属性（temp.filepath）文件夹下的文件，没有则加载工程文件夹xmltemplete下的文件<br>
	 * 模板中fit属性的含义：是否固定值或属性<br>
	 * 	-如果为true，则表示该节点及值都是固定的必须存在；<br>
	 *  -如果为false，则表示该属性或值是可缺失的，在数据中存在该节点名称对应的键的话表示该节点存在;<br>
	 *  -其他值就视同没有fit属性。
	 * @param filename 文件路径
	 * @param vals 填充的值
	 * @return Document对象
	 */
	public static Document parse(String filename, JSONObject vals){
		Document doc = loadXmlFile(filename);
		parse(doc, vals);
		return doc;
	}
	
	/**
	 * 通过模板及数据解析为凭证xml<br>
	 * 模板中fit属性的含义：是否固定值或属性<br>
	 * 	-如果为true，则表示该节点及值都是固定的必须存在；<br>
	 *  -如果为false，则表示该属性或值是可缺失的，在数据中存在该节点名称对应的键的话表示该节点存在;<br>
	 *  -其他值就视同没有fit属性。
	 * @param doc Document对象
	 * @param vals 填充的值
	 */
	public static void parse(Document doc, JSONObject vals){
		Node innode = doc.getElementsByTagName(doc.getDocumentElement().getNodeName()).item(0);
		parse(doc, innode, vals);
	}
	
	/**
	 * 将doc对象保存为xml文件
	 * @param filename 文件路径
	 * @param doc 
	 * @return true / false
	 */
	public static boolean saveXml(String filename, Document doc){
		boolean issuccess = false;
		File file = new File(filename);
		TransformerFactory tFactory = TransformerFactory.newInstance();  
		try {
			Transformer transformer = tFactory.newTransformer();			
			DOMSource source = new DOMSource(doc);  		
			FileOutputStream outStream = new FileOutputStream(file);
			StreamResult result = new StreamResult(outStream);  
			transformer.transform(source, result);
			outStream.flush();
			outStream.close();
			issuccess = true;
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
		}  catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return issuccess;
	}
	
	/**
	 *  将doc对象转为字符串
	 * @param doc
	 * @return
	 */
	public static String parseToString(Document doc){
		DOMSource domSource = new DOMSource(doc);  
        StringWriter writer = new StringWriter();  
        StreamResult result = new StreamResult(writer);  
        TransformerFactory tf = TransformerFactory.newInstance();  
		try {
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
			transformer.transform(domSource, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} 
		return writer.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static void parse(Document doc, Node innode, JSONObject vals){
		NodeList nodeList = innode.getChildNodes();
		if(nodeList.getLength()==0){
			if(innode.hasAttributes()){
				String fit = innode.getAttributes().getNamedItem("fit").getNodeValue();
				if("false".equals(fit)){
					Object val = vals.get(innode.getNodeName());
					setValue(doc, innode, val);
				}
			}else{
				Object val = vals.get(innode.getNodeName());
				setValue(doc, innode, val);
			}
		}
		for(int i=0; i<nodeList.getLength(); i++){
			boolean needReplace = true;//节点是否需要替换值
			Node node = nodeList.item(i);
			
			String nodeName = node.getNodeName();
			if(isTextNode(nodeName)){
				continue;
			}
			if(node.hasAttributes()){
				String fit = node.getAttributes().getNamedItem("fit").getNodeValue();
				if("true".equals(fit)){
					needReplace = false;
				}else if("false".equals(fit)){
					if(vals.containsKey(nodeName)){
						Object val = vals.get(nodeName);
						setValue(doc, node, val);
					}else{
						innode.removeChild(node);
					}
				}else{
					Object val = vals.get(nodeName);
					setValue(doc, node, val);
				}
				node.getAttributes().removeNamedItem("fit");//生成的最终凭证xml将fit属性去掉
			}else if(node.hasChildNodes() && 
					(!isTextNode(node.getFirstChild().getNodeName()) || node.getChildNodes().getLength()>1)){
				needReplace = false;
				Object items = vals.get(nodeName);
				if(items != null && items instanceof List){
					Node subnode = node.getFirstChild();
					if(isTextNode(subnode.getNodeName())){
						subnode = subnode.getNextSibling();
					}
					parseItem(doc, node, subnode, (List<JSONObject>)items);
				}else if(items != null && items instanceof JSONObject){
					Node subnode = node.getFirstChild();
					if(isTextNode(subnode.getNodeName())){
						subnode = subnode.getNextSibling();
					}
					parseItem(doc, node, subnode, (JSONObject)items);
				}else{
					if(!isTextNode(nodeName))
						innode.removeChild(node);
				}
			}
			if(needReplace){
				Object val = vals.get(nodeName);
				setValue(doc, node, val);
			}
		}
	}
	
	private static void setValue(Document doc, Node pnode, Object val){
		if(val instanceof Node){
			Node node = doc.importNode((Node)val, true);
			if(pnode.getNodeName().endsWith(node.getNodeName())){
				pnode.getParentNode().replaceChild(node, pnode);
			}else{
				pnode.appendChild(node);
			}
		}else{
			pnode.setTextContent(val==null?"":val.toString());
		}
	}
	
	public static void parseItem(Document doc, Node pnode, Node innode, List<JSONObject> items){
		Node textNode = null;
		if(!isTextNode(pnode.getFirstChild().getNodeName())){
			pnode.removeChild(pnode.getFirstChild());//将加载的模板中的第一个item移除
		}else{
			pnode.removeChild(pnode.getFirstChild().getNextSibling());//将加载的模板中的第一个item移除
		}
		textNode = pnode.getFirstChild().cloneNode(true);
		for(int i=0; i<items.size(); i++){
			Node node = innode.cloneNode(true);//克隆item
			JSONObject item = items.get(i);
			item.put("detailindex", i+1);//item序号自动生成
			parse(doc, node, item);
			pnode.appendChild(node);//将item添加到doc中
			pnode.appendChild(textNode);
		}
	}
	
	public static void parseItem(Document doc, Node pnode, Node innode, JSONObject item){
		NodeList nodeList = pnode.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if(!isTextNode(node.getNodeName())){
				if(node.getChildNodes().getLength()>1){
					JSONObject vals = item.getJSONObject(innode.getNodeName());
					parse(doc, node, vals);
				}else{
					parse(doc, node, item);
				}
			}
		}
	}
	
	public static boolean isTextNode(String nodeName){
		if(nodeName == null || "#text".equals(nodeName) || "#comment".equals(nodeName)){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param xmlStr 根元素必须唯一
	 * @return
	 */
	public static Node strAsNode(String xmlStr){
		try {
			ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
			return docBuilder.parse(tInputStringStream).getDocumentElement();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过两个模板文件合并、填充相应的值后输出xml格式的字符串。当模板中节点带有fit属性，<br/> 
	 *  fit=true时，为固定值（不会被替换掉）；fit=false时，为该节点可变（入参中包含该节点的替换参数则存在该节点，否则该节点会被删除）。<br/> 
	 *  参数替换优先顺序：<br/> 
	 *    当模板中节点带有name属性，则以name的值作为key在入参中获取填充值，进行该节点的填充；<br/> 
	 *    否则寻找节点内容是否以#key#包含，有则以key值在入参中获取值填充；<br/>
	 *    前两者都不存在的情况下，则以节点元素名称为key在入参中获取值填充。<br/>
	 *  当节点存在type属性时，type目前支持auto、list类型，为auto时，该元素值自动填充；<br/> 
	 *    为list时，该元素下面为列表型模版，以name属性值为key，从入参中取出数组列表，再以该节点的子节点为模版循环生成多个该模版节点元素<br/>
	 *  
	 *  模版实例：<br/>
	 *  &lt;?xml version="1.0" encoding="UTF-8"?><br/>
		&lt;root><br/>
			&lt;head><br/>
				&lt;account name="user_name">&lt;/account><br/>
				&lt;password name="user_pw">&lt;/password><br/>
			&lt;/head><br/>
			&lt;base type="list" name="manList"><br/>	
				&lt;man><br/>
					&lt;tel name="telephone">&lt;/tel><br/>
				&lt;/man><br/>
			&lt;/base><br/>
		&lt;/root><br/>
	 * @param toFileDocName 基本节点模板路径
	 * @param fromFileDocName 导入节点模板路径
	 * @param vals 填充的值对象集合
	 * @return
	 */
	public static String parseXml(String toFileDocName, String fromFileDocName, JSONObject vals){
		Document toDoc = XmlUtil.loadXmlFile(toFileDocName);
		Document fromDoc = XmlUtil.loadXmlFile(fromFileDocName);
		XmlUtil.importNode(toDoc, fromDoc, toDoc.getDocumentElement().getNodeName());
		parseXml2(toDoc, null, vals);
		return parseToString(toDoc); 
	}
	
	/**
	 * 通过两个模板文件合并、填充相应的值后输出xml格式的字符串。当模板中节点带有fit属性，<br/> 
	 *  fit=true时，为固定值（不会被替换掉）；fit=false时，为该节点可变（入参中包含该节点的替换参数则存在该节点，否则该节点会被删除）。<br/> 
	 *  参数替换优先顺序：<br/> 
	 *    当模板中节点带有name属性，则以name的值作为key在入参中获取填充值，进行该节点的填充；<br/> 
	 *    否则寻找节点内容是否以#key#包含，有则以key值在入参中获取值填充；<br/>
	 *    前两者都不存在的情况下，则以节点元素名称为key在入参中获取值填充。<br/>
	 *  当节点存在type属性时，type目前支持auto、list类型，为auto时，该元素值自动填充；<br/> 
	 *    为list时，该元素下面为列表型模版，以name属性值为key，从入参中取出数组列表，再以该节点的子节点为模版循环生成多个该模版节点元素<br/>
	 *  
	 *  模版实例：<br/>
	 *  &lt;?xml version="1.0" encoding="UTF-8"?><br/>
		&lt;root><br/>
			&lt;head><br/>
				&lt;account name="user_name">&lt;/account><br/>
				&lt;password name="user_pw">&lt;/password><br/>
			&lt;/head><br/>
			&lt;base type="list" name="manList"><br/>	
				&lt;man><br/>
					&lt;tel name="telephone">&lt;/tel><br/>
				&lt;/man><br/>
			&lt;/base><br/>
		&lt;/root><br/>
	 * @param toDoc 基本节点Document对象
	 * @param fromDoc 导入节点Document对象
	 * @param vals 填充的值对象集合
	 * @return
	 */
	public static String parseXml(Document toDoc, Document fromDoc, JSONObject vals){
		XmlUtil.importNode(toDoc, fromDoc, toDoc.getDocumentElement().getNodeName());
		parseXml2(toDoc, null, vals);
		return parseToString(toDoc); 
	}
	
	/**
	 * 将模板填充后输出xml格式的字符串。当模板中节点带有fit属性，<br/> 
	 *  fit=true时，为固定值（不会被替换掉）；fit=false时，为该节点可变（入参中包含该节点的替换参数则存在该节点，否则该节点会被删除）。<br/> 
	 *  参数替换优先顺序：<br/> 
	 *    当模板中节点带有name属性，则以name的值作为key在入参中获取填充值，进行该节点的填充；<br/> 
	 *    否则寻找节点内容是否以#key#包含，有则以key值在入参中获取值填充；<br/>
	 *    前两者都不存在的情况下，则以节点元素名称为key在入参中获取值填充。<br/>
	 *  当节点存在type属性时，type目前支持auto、list类型，为auto时，该元素值自动填充；<br/> 
	 *    为list时，该元素下面为列表型模版，以name属性值为key，从入参中取出数组列表，再以该节点的子节点为模版循环生成多个该模版节点元素<br/>
	 *  
	 *  模版实例：<br/>
	 *  &lt;?xml version="1.0" encoding="UTF-8"?><br/>
		&lt;root><br/>
			&lt;head><br/>
				&lt;account name="user_name">&lt;/account><br/>
				&lt;password name="user_pw">&lt;/password><br/>
			&lt;/head><br/>
			&lt;base type="list" name="manList"><br/>	
				&lt;man><br/>
					&lt;tel name="telephone">&lt;/tel><br/>
				&lt;/man><br/>
			&lt;/base><br/>
		&lt;/root><br/>
	 * @param xmlFile 模板文件路径
	 * @param vals 填充的值对象集合
	 * @return
	 */
	public static String parseXml(String xmlFile, JSONObject vals){
		Document doc = XmlUtil.loadXmlFile(xmlFile);
		parseXml2(doc, null, vals);
		return parseToString(doc); 
	}
	
	/**
	 * 将模板填充后输出xml格式的字符串。当模板中节点带有fit属性，<br/> 
	 *  fit=true时，为固定值（不会被替换掉）；fit=false时，为该节点可变（入参中包含该节点的替换参数则存在该节点，否则该节点会被删除）。<br/> 
	 *  参数替换优先顺序：当模板中节点带有name属性，则以name的值作为key在入参中获取填充值，进行该节点的填充；<br/> 
	 *  否则寻找节点内容是否以#key#包含，有则以key值在入参中获取值填充；<br/>
	 *  前两者都不存在的情况下，则以节点元素名称为key在入参中获取值填充。<br/>
	 *  当节点存在type属性时，type目前支持auto、list类型，为auto时，该元素值自动填充；为list时，该元素下面为列表型模版
	 *  
	 * @param doc 模板Document对象
	 * @param innode 最初调用时，为null；递归调用时，传入当前node
	 * @param vals 填充的值对象集合
	 * @return
	 */
	private static void parseXml2(Document doc, Node innode, JSONObject vals){
		NodeList list = innode==null?doc.getChildNodes():innode.getChildNodes();
		for(int i=0; i<list.getLength(); i++){
			Node node = list.item(i);
			if(!isTextNode(node.getNodeName())){//非文本节点
				String text = node.getTextContent();//获取节点文本
				String type = "";//type属性
				String name = "";//name属性,其值作为key
				String fit  = "";//fit属性
				if(node.hasAttributes()){//节点配置了属性
					NamedNodeMap map = node.getAttributes();
					type = (map.getNamedItem("type") == null ? "": map.getNamedItem("type").getNodeValue());//type属性
					fit  = (map.getNamedItem("fit") == null ? "": map.getNamedItem("fit").getNodeValue());//fit属性
					name = (map.getNamedItem("name") == null ? "": map.getNamedItem("name").getNodeValue());//name属性,其值作为key
				}
				if("".equals(name)){
					if(text!= null && text.startsWith("#") && text.endsWith("#")){//优先配置的变量
						name = text.replaceAll("#","").trim();
					}else{//最终才是节点名称
						name = node.getNodeName();
					}
				}
				
				if("auto".equals(type)){//序列自增模版
					setValue(doc, node, IdCreatorUtil.ConfirmDateSerializeId("FH", 6));
				}else if("list".equals(type)){//同类列表模版
					List<Node> nodeList = new ArrayList<Node>();
					JSONArray subValList = vals.getJSONArray(name);
					NodeList subList = node.getChildNodes();
					Node tempNode = null;
					for(int j=0; j<subValList.size(); j++){
						JSONObject item = subValList.getJSONObject(j);
						for(int n=0; n<subList.getLength(); n++){
							Node subNode = subList.item(n);
							if(!isTextNode(subNode.getNodeName())){//非文本节点
								Node newNode = subNode.cloneNode(true);
								parseXml2(doc, newNode, item);
								nodeList.add(newNode);
								tempNode = subNode;
							}
						}
					}
					if(tempNode != null)
						node.removeChild(tempNode);
					for(Node newNode : nodeList){
						node.appendChild(newNode);
					}
				}else{
					if(vals.containsKey(name)){//入参中包含了节点名称键值对，则直接填充
						setValue(doc, node, vals.get(name));
					}else if(node.hasChildNodes() && node.getChildNodes().getLength()>1){//递归
						parseXml2(doc, node, vals);
					}else if("false".equals(fit)){//属性值不为true时，表示当入参中不包含该参数，则将该节点删除
						innode.removeChild(node);
					}
				}
			}
		}
	}
	
	/**
	 * 将xml格式的字符串加载为Document对象
	 * @param xml Xml格式的字符串
	 * @return Document
	 */
	public static Document loadXml(String xml){
		try {
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			Document doc = docBuilder.parse(is);
			is.close();
			return doc;
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将Xml格式的数据串转换成JSON对象，未设置templete时，直接转，设置了映射模版则对比映射模版进行转换<br/>
	 * @param templeteFile 映射模版，节点主要属性有name、type，存在name时最终会以name的值进行映射转换，type=list时，节点将转为JSONArray
	 * @param xml Xml格式数据字符串
	 * @return JSONObject
	 */
	public static JSONObject xmlToJson(String templeteFile, String xml){
		Document templete = (templeteFile!=null && templeteFile.length()>0 ? loadXmlFile(templeteFile) : null);
		Document doc = loadXml(xml);
		JSONObject json = new JSONObject();
		xmlToJson(json, templete, doc, null);
		return json;
	}
	
	/**
	 * 递归转换xml为json对象
	 * @param json
	 * @param templete
	 * @param doc
	 * @param innode
	 */
	private static void xmlToJson(JSONObject json, Document templete, Document doc, Node innode){
		NodeList list = innode==null?doc.getChildNodes():innode.getChildNodes();
		for(int i=0; i<list.getLength(); i++){
			Node node = list.item(i);
			if(!isTextNode(node.getNodeName())){//非文本节点
				String nodeName = node.getNodeName();
				//通过数据节点名称定位模版中相应的节点，然后确定最终的json key
				String type = "";//type属性
				Node tn = null; 
				NodeList tempNodeList = null;
				if(templete != null){
					tempNodeList = templete.getElementsByTagName(nodeName);
					for(int j=0; j<tempNodeList.getLength(); j++){
						tn = tempNodeList.item(j);
						if(!isTextNode(tn.getNodeName()) && isSamePath(tn, node)){//isSamePath为解决不同节点下父节点名相同的情况
							break;
						}
					}
					if(tn != null && tn.hasAttributes()){//节点配置了属性
						NamedNodeMap map = tn.getAttributes();
						type = (map.getNamedItem("type") == null ? "": map.getNamedItem("type").getNodeValue());//type属性
						nodeName = (map.getNamedItem("name") == null ? nodeName: map.getNamedItem("name").getNodeValue());//name属性,其值作为key
					}
				}
				if(node.hasChildNodes() && node.getChildNodes().getLength()>1){//数据节点存在子节点
					if("list".equals(type) || checkIsListNode(node)){//节点为list格式
						JSONArray subArr = new JSONArray();
						json.put(nodeName, subArr);
						tempNodeList = node.getChildNodes();
						for(int j=0; j<tempNodeList.getLength(); j++){
							tn = tempNodeList.item(j);
							if(!isTextNode(tn.getNodeName())){
								JSONObject subJson = new JSONObject();
								xmlToJson(subJson, templete, doc, tn);
								subArr.add(subJson);
							}
						}
					}else{//否则为json
						JSONObject subJson = new JSONObject();
						json.put(nodeName, subJson);
						xmlToJson(subJson, templete, doc, node);
					}
				}else{
					json.put(nodeName, node.getTextContent().replaceAll("(\t\n|\t|\n|\n\t)", ""));
				}
			}
		}
	}
	
	/**
	 * 检查数据节点是否为list节点，只要子节点有两个为同名节点就判断为list节点
	 * @param node
	 * @return boolean
	 */
	private static boolean checkIsListNode(Node node){
		int count = 0;
		NodeList childList = node.getChildNodes();
		Node cn = null, oldCn = null;
		for(int j=0; j<childList.getLength(); j++){
			cn = childList.item(j);
			if(!isTextNode(cn.getNodeName())){
				if(oldCn==null){
					count++;
				}else if(oldCn.getNodeName().equals(cn.getNodeName())){
					count++;
					break;
				}
				oldCn = cn;
			}
		}
		return count>1;//只要有两个节点名相同就可判定该节点为list节点
	}
	/**
	 * 通过节点路径比较两个节点是否是同一位置的节点
	 * @param node1
	 * @param node2
	 * @return boolean
	 */
	private static boolean isSamePath(Node node1, Node node2){
		return getNodePath(node1).equals(getNodePath(node2));
	}
	/**
	 * 获取节点路径
	 * @param node
	 * @return 根节点->子节点...
	 */
	public static String getNodePath(Node node){
		StringBuffer sb = new StringBuffer(128);
		sb.insert(0, node.getNodeName());
		Node pnode = node.getParentNode();
		while(pnode != null && (pnode.getNodeName() != null || !"#document".equals(pnode.getNodeName()))){
			if(!"#document".equals(pnode.getNodeName()))
				sb.insert(0, "->").insert(0, pnode.getNodeName());
			pnode = pnode.getParentNode();
		}
		return sb.toString();
	}
	/**
	 * 导入xml节点
	 * @param toDoc 基本节点Document对象
	 * @param fromDoc 导入节点Document对象
	 * @param container 导入到哪个节点下
	 */
	public static void importNode(Document toDoc, Document fromDoc, String container){
		Node innode = fromDoc.getElementsByTagName(fromDoc.getDocumentElement().getNodeName()).item(0);
		Node newnode = toDoc.importNode(innode, true);
		toDoc.getElementsByTagName(container).item(0).appendChild(newnode);
	}

	/**
	 * 将Json格式入参转换为Xml格式入参
	 * @param json
	 * @param debug 默认false，true时xml包含所有节点属性
	 * @return
	 */
	public static String jsonParseXml(JSONObject json, boolean debug){
		Document doc = docBuilder.newDocument();
		
		loopByJson(json, doc, null, debug);
		return parseToString(doc);
	}
	private static void loopByJson(JSONObject json, Document doc, Element pele, boolean debug){
		logger.info("入参JSON="+json.toJSONString());
		String nodeName = json.getString("nodeName");
		String attrType = json.getString("attrType");
		if("false".equalsIgnoreCase(attrType)){
			if(!json.containsKey("value") || (json.containsKey("value") && "".equals(json.get("value"))))
				return;
		}
		Element ele = doc.createElement(nodeName);
		if(pele == null)
			doc.appendChild(ele);
		else
			pele.appendChild(ele);
		if(json.containsKey("value")){
			ele.setTextContent(json.getString("value"));
		}
		if(debug){
			if(json.containsKey("attrName"))
				ele.setAttribute("name", json.getString("attrName"));
			if(json.containsKey("attrType"))
				ele.setAttribute("type", json.getString("attrType"));
			if(json.containsKey("text"))
				ele.setAttribute("remark", json.getString("text"));
		}
		if(json.containsKey("children") && !"".equals(json.get("children")) && json.getJSONArray("children").size()>0){
			JSONArray subArr = json.getJSONArray("children");
			for(int i=0; i<subArr.size(); i++){
				loopByJson(subArr.getJSONObject(i), doc, ele, debug);
			}
		}
	}
	
	/**
	 * 将Xml格式结果转换为Json格式输出
	 * @param xmlStr 请求返回xml
	 * @param dataMap 数据节点映射
	 * @param outMap 标准输出映射
	 * @return
	 */
	public static JSONObject xmlParseJson(String xmlStr, JSONObject dataMap, JSONObject outMap){
		Document doc = loadXml(xmlStr);
		JSONObject result = new JSONObject();
		//应答状态
		JSONObject json = null;
		String nodeName = null;
		String attrName = null;
		NodeList nodeList = null;
		if(outMap.containsKey("children") && !"".equals(outMap.get("children"))){//处理标准输出的返回状态信息
			JSONArray status = outMap.getJSONArray("children");
			for(int i=0; i<status.size(); i++){
				json = status.getJSONObject(i);
				nodeName = json.getString("nodeName");
				attrName = json.getString("attrName");
				if(attrName!=null && attrName.length()>0){
					String[] attrNames = attrName.split("\\|");
					for(String name : attrNames){
						nodeList = doc.getElementsByTagName(name);
						if(nodeList != null && nodeList.getLength()>0){
							break;
						}
					}
				}
				if(nodeList != null && nodeList.getLength()>0)
					result.put(nodeName, nodeList.item(0).getTextContent());
			}
		}
		//应答体处理
		nodeName = dataMap.getString("nodeName");
		attrName = dataMap.getString("attrName");
		String attrType = dataMap.getString("attrType");
		nodeList = doc.getElementsByTagName(attrName);//通过节点名获取返回数据节点
		if(nodeList != null && nodeList.getLength()>0){
			if(dataMap.containsKey("children") && !"".equals(dataMap.get("children"))){
				JSONArray subMapArr = dataMap.getJSONArray("children");
				if("list".equalsIgnoreCase(attrType)){
					nodeList = nodeList.item(0).getChildNodes();
					JSONArray subArr = new JSONArray();
					NodeList subNodes = null; Node node = null;
					for(int i=0; i<nodeList.getLength(); i++){
						JSONObject subJson = new JSONObject();
						if(!isTextNode(nodeList.item(i).getNodeName())){
							subNodes = nodeList.item(i).getChildNodes();
							for(int j=0; j<subNodes.getLength(); j++){
								node = subNodes.item(j);
								if(!isTextNode(node.getNodeName())){
									//获取key
									String key = null;//node.getNodeName();
									for(int n=0; n<subMapArr.size(); n++){
										attrName = subMapArr.getJSONObject(n).getString("attrName");
										if(attrName.equals(node.getNodeName())){
											key = subMapArr.getJSONObject(n).getString("nodeName");
											break;
										}
									}
									if(key != null)
										subJson.put(key, node.getTextContent().replaceAll("(\r\n|\r|\n|\n\r|\t)", ""));
								}
							}
							subArr.add(subJson);
						}
					}
					result.put(nodeName, subArr);
				}else{
					
				}
			}else{
				result.put(nodeName, nodeList.item(0).getTextContent());
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		JSONObject ppp = new JSONObject();
		ppp.put("nodeName", "root");
		JSONArray subArr = new JSONArray();
		ppp.put("children", subArr);
		JSONObject subppp = new JSONObject();
		subppp.put("nodeName", "name");
		subppp.put("value", "Nico.li");
		subppp.put("text", "用户名");
		subArr.add(subppp);
		subppp = new JSONObject();
		subppp.put("nodeName", "pwd");
		subppp.put("value", "123456");
		subArr.add(subppp);
		
		System.out.println(XmlUtil.jsonParseXml(ppp, true));
		//构造数据
		/*JSONObject headitem = new JSONObject();
		headitem.put("TRANS_TYPE", "V7014");
		headitem.put("SERIALDECIMAL", "1234567890");
		headitem.put("SESSIONID", "1234567890");
				
		JSONObject baseitem = new JSONObject();
		baseitem.put("C_PLATE_NO", "湘A45678");
		
		JSONObject basePart = new JSONObject();
		basePart.put("BASE_PART", baseitem);
		
		JSONObject item = new JSONObject();
		item.put("HEAD", headitem);
		item.put("BODY", basePart);
		
		
		
		String filename = "E:/桌面文件夹/ERP/保险业务系统/zhlh_base.xml";
		Document doc = XmlUtil.loadXmlFile(filename);
		Document doc2 = XmlUtil.loadXmlFile("E:/桌面文件夹/ERP/保险业务系统/zhlhInitTemp.xml");
		doc.importNode(doc2, false);
		XmlUtil.parse(doc, item);
		String xmlstr = XmlUtil.parseToString(doc);
		item.put("BODY", xmlstr);*/
		
		
		/*JSONObject json = new JSONObject();
		json.put("trans_type", "V7801");
		json.put("serialno", "12345678");
		json.put("sessionid", "12345678");
		json.put("partnercode", "HNDA");
		json.put("partnersubcode", "HNDA");
		json.put("C_PLATE_NO", "湘A54U81");
		
		String filename = "E:/桌面文件夹/ERP/保险业务系统/zhlh_base.xml";
		Document doc = XmlUtil.loadXmlFile(filename);
		Document doc2 = XmlUtil.loadXmlFile("E:/桌面文件夹/ERP/保险业务系统/zhlhInitTemp.xml");
		System.out.println(XmlUtil.parseXml(doc, doc2, json));*/
		
		/*JSONObject vals = new JSONObject();
		vals.put("user_name", "Nico.li");
		vals.put("user_pw", "123456");
		vals.put("clientName", "Smile.li");
		vals.put("telephone", "18073150620");
		URL url = XmlUtil.class.getClassLoader().getResource("demoxml.xml");
		String result = XmlUtil.parseXml(url.getPath(), vals);
		System.out.println(result);*/
		
		/*JSONObject vals = new JSONObject();
		JSONArray manList = new JSONArray(); 
		vals = new JSONObject();
		vals.put("manList", manList);
		vals.put("user_name", "Nico.li");
		vals.put("user_pw", "123456");
		vals.put("telephone", "15111079566");
		
		JSONObject man = new JSONObject();
		man.put("telephone", "15323150620");
		man.put("man_name", "AAAA");
		manList.add(man);
		man = new JSONObject();
		man.put("telephone", "15323150621");
		man.put("man_name", "CCCC");
		manList.add(man);
		
		URL url = XmlUtil.class.getClassLoader().getResource("demoxml.xml");
		String result = parseXml(url.getPath(), vals);
		System.out.println(result);
		JSONObject json = XmlUtil.xmlToJson(url.getPath(), result);
		
		System.out.println(json);*/
		
	}
}
