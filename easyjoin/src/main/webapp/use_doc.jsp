<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<% String pcode = request.getParameter("code"); 
String ctx = request.getContextPath();
request.setAttribute("ctx", ctx);
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="static/default/easyui.css">
<link rel="stylesheet" type="text/css" href="static/icon.css">
<script type="text/javascript" src="static/jquery.min.js"></script>
<script type="text/javascript" src="static/jquery.easyui.min.js"></script>
<script type="text/javascript" src="static/easyui-lang-zh_CN.js"></script>
<style type="text/css">
	h1{margin: 15px; 5px;}
	h2{margin-left:10px;}
	h4,p{margin-left:30px;}
	h5{margin-left:50px;}
	.ptext{margin-left:50px;}
</style>
</head>
<body style="margin:0;">
<h1 style="line-height:60px;">
	<img alt="" src="${ctx }/static/images/logo.png" style="float:left;">Easy Join
	<SPAN STYLE="FONT-SIZE:16PX; COLOR: #009fff;">为您解决接口对接的烦恼</<SPAN>
</h1>
<br/>
<div style="background:#ccc;">
	<h2>项目简介</h2>
	<div>
		<p>
			1、面对几十上百的参数的接口时，你是否感到眼花缭乱、发愁、头疼？
		</p>
		<p>
			2、在公司现有成熟的业务上突然需要接入外部的数据接口，是否感觉参数、返回等的转换繁琐且费事？
		</p>
		<p>
			3、当你接入同一个业务，却要面对多套外部接口，且返回五花八门？
		</p>
		<p>
			4、当你的入参格式是JSON，但对方的入参格式确实XML或其他，是不是感觉天都要塌下来了？
		</p>
		<p>
			5、当外部接口参数改变时，是否觉得变更麻烦？
		</p>

		<p>
			本项目就是为了一站式解决这些问题应势而生，为你解决这些烦恼。项目源于工作中遇到的实际问题，解放了接口对接的烦恼，为同一业务接入多渠道接口等场景提供统一输入、标准输出的接入解决方案，让您更专注于业务及本地逻辑！
		</p>
		<p>
			场景1：普通的单渠道接入
		</p>
		<p>
			场景2：同一业务接入多渠道接口（不同渠道提供的接口不同，业务相同）
		</p>
		<p>
			场景3：。。。
		</p>
	</div>
	<h2>主调接口</h2>
	<div>
		<h4>包名：com.easyjoin.util</h4>
		<h4>IntfUtil.sendPost(JSONObject reqParams, String provider, String intfCode)</h4>
	</div>	
	<div>
		<h5>
			reqParams：请求参数<br/>
			provider：   接口提供者编码<br/>
			intfCode：   接口编码或接口路径<br/>
		</h5>
	</div>
	<h2>界面化操作</h2>
	<div>
		<h4>1.项目集成</h4>
		<div>
			<p class="ptext">(1)将项目下载到本地，在本地以war包的形式引入业务项目。项目基于Spring MVC框架搭建，业务工程需要满足Spring的要求。</p>
			<p class="ptext">(2)easy-join-config.xml文件可以使用业务项目本身的文件，将 easy-join-servlet.xml配置到web.xml或包含进业务系统的servlet文件即可</p>
			<p class="ptext">(3)easyjoin.properties下配置映射文件保存路径</p>
		</div>
		<h4>2.新增提供者</h4>
		<div>
			<p class="ptext">(1)在界面左上角点新增，弹出提供者新增界面。配置提供者的编码、描述、请求地址（接口外部访问的域名或IP等）</p>
			<p class="ptext">(2)请求头配置：为该接口提供者所有接口的统一请求参数，加入Header请求头。如果存在请求头参数则需要配置，参数值可为固定的也可为变量（#变量名#）</p>
			<p class="ptext">(3)标准输出配置：为该接口提供者所有接口的统一输出，一般为请求接口返回的状态及描述信息。可映射多个节点，用"|"分隔</p>
			<p class="ptext">(4)示例：</p>
			<p class="ptext"><img alt="" src="static/images/provider.png"></p>
		</div>
		<h4>3.新增提供者所提供的接口</h4>
		<div>
			<p class="ptext">(1)双击提供者列表，右侧和下侧会刷新该提供者的信息，然后点击右侧新增接口，弹出接口新增界面，填写接口的基本信息</p>
			<p class="ptext">(2)接口入参映射配置：根据提供者提供的接口文档，按其结构将接口所需的参数名与本地业务统一的参数名映射。依据type属性，可配置为：不填值-必传 项；fit-固定值；auto-自动序列填充；list-列表；false-可选（根据入参是否存在决定是否生成在最终请求报文中）；</p>
			<p class="ptext">(3)结果数据节点映射配置：（该配置不包括状态信息）根据返回的请求结果，按业务方的数据结构，将本地业务统一的节点与返回结果映射。依据type属性，可配置为：list-列表；</p>
			<p class="ptext">(4)示例：</p>
			<p class="ptext"><img alt="" src="static/images/intf.png"></p>
		</div>
		<h4>4.接口测试</h4>
		<div>
			<p class="ptext">(1)点击接口列表的"接口测试"按钮，进入请求入参填写、请求结果展示界面</p>
			<p class="ptext">(2)依据参数对照，将必填参数赋值</p>
			<p class="ptext">(3)点击发送请求，等待请求结果</p>
			<p class="ptext">(4)示例：</p>
			<p class="ptext"><img alt="" src="static/images/test.png"></p>
		</div>
		<h4>5.接口使用</h4>
		<div>
			<p class="ptext">(1)定义转换器：转换器实现com.easyjoin.util.http.DataParser接口。目前默认实现了Xml和json的转换器XmlDataParser和JsonDataParser</p>
			<p class="ptext">(1)调用主调接口IntfUtil.sendPost(JSONObject reqParams, String provider, String intfCode)</p>
			<p class="ptext">(2)示例：</p>
			<p class="ptext">&nbsp;&nbsp;JSONObject reqParams = JSONObject.parseObject(reqParamsStr);</p>
			<p class="ptext">&nbsp;&nbsp;JSONObject result = IntfUtil.sendPost(reqParams, provider, intfCode);</p>
		</div>
		<h4>6.扩展-转换器</h4>
		<div>
			<p class="ptext">转换器接口：com.easyjoin.util.http.DataParser接口。包含参数转换方法和结果转换方法</p>
			<p class="ptext">默认实现了输入输出一致的xml与json格式的转换器，如果客户有自己的格式，可以自定义实现，如输入是json格式，输出是xml格式</p>
			<p class="ptext">转换器自定义</p>
			<p class="ptext">(1)定义自己的格式类型，如JSONXML，表示入参是JSON格式，返回是XML格式</p>
			<p class="ptext">(2)实现com.easyjoin.util.http.DataParser接口，实现的类名规则是JsonXmlDataParser，加上@Component("jsonxmlDataParser")注解</p>
			<p class="ptext">(3)配置接口信息，请求及应答格式选择您自定义的格式类型即可</p>
		</div>
	</div>
	<!-- <h2>名词解释</h2>
	<div>
		<h4>1.提供者</h4>
		<div>
			<p class="ptext"></p>
			<p class="ptext"></p>
		</div>
	</div> -->
</div>
<jsp:include page="myinfo.jsp"></jsp:include>
</body>
</html>