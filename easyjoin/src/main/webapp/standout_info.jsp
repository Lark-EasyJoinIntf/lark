<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
</head>
<body style="margin:0;">
	<input type="hidden" name="provider" value="<%=pcode%>">
	<fieldset style="height:92%;border:1px solid lightblue;">
		<legend>统一映射</legend>
						
		<div id="_ReqHeader" class="easyui-datagrid" style="width: 100%;"
			data-options="title:'请求头列表',rownumbers:true,singleSelect:true,collapsible:true"></div>
								
		<table style="width: 100%; margin-top: 10px; border:0;">
			<thead>
				<tr>
					<td>
						<div id="_StandOutEdit" class="easyui-datagrid"
									style="width: 100%;"
									data-options="title:'标准输出',rownumbers:true,singleSelect:false,
									autoRowHeight:true,striped:true,collapsible:true,
									idField: 'id', treeField: 'text'"></div>
					</td>
				</tr>
			</thead>
		</table>	
	</fieldset>			
	<script type="text/javascript">
		var reqHeaderColArr = [ [ 
		             		   {field : 'id', title : '序号'}, 
		             		   {field : 'paramCode',title : '参数编码'}, 
		             		   {field : 'paramVal',title : '参数值'}
		             		] ];
		$('#_ReqHeader').datagrid({columns : reqHeaderColArr});
		$('#_ReqHeader').datagrid('loadData', parent.headerData);
		
		var ecolArr = [ [ 
			     			{field : 'id',title : 'id',hidden : true}, 
			     			{field : 'parentId',title : '父节点',hidden : true}, 
			     			{field : 'text',title : '描述',editor : 'text'}, 
			     			{field : 'nodeName',title : '请求参数/节点编码',editor : 'text'}, 
			     			{field : 'attrName',title : '标准映射参数',editor : 'text'}, 
			     			{field : 'attrType',title : 'Type属性',editor : 'text'} 
			     		] ];
		$('#_StandOutEdit').treegrid({columns : ecolArr});
		$('#_StandOutEdit').treegrid("loadData", parent.outMap);
	</script>						
</body>
</html>