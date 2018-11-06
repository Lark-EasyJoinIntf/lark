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
</head>
<body style="margin:0;">
	<input type="hidden" name="provider" value="<%=pcode%>">
	<div id="tb" style="height:auto">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="add_intf()">新增接口</a>
	</div>
	<div id="_IntfList" class="easyui-datagrid" style="width: 100%; height:99.6%;"
		data-options="title:'接口列表',rownumbers:true,singleSelect:true,collapsible:true, toolbar:'#tb'"></div>
		
	<script type="text/javascript">
		var intfColArr = [ [ 
		             		   {field : 'id', title : '序号'},
		             		   {field : 'provider',title : '接口提供者'}, 
		             		   {field : 'intfName',title : '接口名称'}, 
		             		   {field : 'intfCode',title : '接口编码/方法'},
		             		   {field : 'reqType',title : '请求方式'},
		             		   {field : 'reqFormat',title : '请求及应答格式'},
		             		   {field : 'op',title : '操作',
			             			formatter: function(value,row,index){
			             				return " <a href='javascript:void(0)' class='easyui-linkbutton' onclick='parent.intf_test("+JSON.stringify(row)+")'>接口测试</a>";
			             			}
		             		   } 
		             		] ];
		$('#_IntfList').datagrid({
			url : '${ctx }/joy/readIntfList',
			queryParams:{provider: $("[name='provider']").val()}, 
			method : 'post',
			columns : intfColArr,
			onDblClickRow : function(rowIndex, rowData) {
				parent.edit_intf(rowData);
			}
		});
		
		function add_intf() {
			if($("[name='provider']").val()){			
				parent.add_intf($("[name='provider']").val());
			}else{
				alert("请先选择接口提供者！");
			}
		}
	</script>	
</body>
</html>