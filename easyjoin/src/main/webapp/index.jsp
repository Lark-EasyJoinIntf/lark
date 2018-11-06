<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% 
String ctx = request.getContextPath();
request.setAttribute("ctx", ctx);
%>
<html>
<head>
<link rel="shortcut icon" href="${ctx }/static/images/logo16.png"/>  
<link rel="stylesheet" type="text/css" href="${ctx }/static/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx }/static/icon.css">
<script type="text/javascript" src="${ctx }/static/jquery.min.js"></script>
<script type="text/javascript" src="${ctx }/static/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx }/static/jQuery.Form.js"></script>
<script type="text/javascript" src="${ctx }/static/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx }/static/datagrid-filter.js"></script>
<script type="text/javascript" src="${ctx }/static/Utils.js"></script>

<style type="text/css">
	iframe{width:100%; height:100%; border: 0; }
	.join_div{position: absolute; width: 100%; height: 100%; z-index: 1000; top: 0px; left: 0px; background: #ccc; display:none;}
	.btns {padding:10px;}
	.btns span{border:1px solid lightblue;padding:2px 10px;}
</style>
</head>
<body>
	<table style="width: 100%; height: 100%;">
		<tr style="height: 40px;">
			<td colspan="2">
				<div>
					<img alt="" src="${ctx }/static/images/logo.png" style="float:left;">
					<h2 style="float:left;">Easy Join</h2>
					<h4 style="float:right;"><a href="use_doc.jsp?code=" target="_blank">使用说明</a></h4>
				</div>
				<!-- <div id="_QryContaner" style="margin: 10px 0; ">
					接口提供者：<input name="provider">
					<button id="_search">搜索</button>
				</div> -->
			</td>
		</tr>
		<tr>
			<td valign="top" style="width:50%;" id="tab1">
				<div id="tb" style="height:auto">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="add_provider()">新增</a>
				</div>
				<div id="_Provider" class="easyui-datagrid" style="width: 100%; height:40%; margin: 0 10px 0 0;"
					data-options="title:'提供者列表',rownumbers:true,singleSelect:true,collapsible:true, toolbar:'#tb'"></div>
				<div style="margin:10px 0 0 0;height:60%;">
					<iframe id="_StandOut" src="standout_info.jsp?code="></iframe>		
				</div>
			</td>
			<td valign="top" style="width:50%;">
				<iframe id="_Intfs" src="intf_list.jsp?code="></iframe>	<!-- 接口列表 -->
			</td>
		</tr>
	</table>
	
	<jsp:include page="myinfo.jsp"></jsp:include>
	
	
	<!-- 编辑提供者 -->
	<div id="providerWin" class="easyui-window" title="提供者编辑" style="width:100%;height:100%;"
	    data-options="iconCls:'icon-save',modal:true, closed:true">
	    <form id="_SaveProvider">
	    <input type="hidden" name="id">
	    <table align="center" style="background: #e4e4e4; width: 100%;">
			<tr>
				<td>提供者编码<input name="code"></td>
				<td>提供者<input name="provider"></td>
				<td>请求地址<input name="reqUrl" style="width:300px;"></td>
				<td>描述<input name="remark" style="width: 50%;"></td>
			</tr>
			<tr>
				<td colspan="2" valign="top" width="50%" height="400">
					<input type="hidden" name="headerMap">
					<fieldset>
						<legend>请求头</legend>
						<div id="_ReqHeader" class="easyui-datagrid" style="width: 100%; margin: 0 10px 0 0;"
							data-options="rownumbers:true,singleSelect:true,collapsible:true"></div>
					</fieldset>
				</td>
				<td colspan="2" valign="top" width="50%">
					<div id="_edit_btns" style="position: absolute; z-index: 100000; display: none;">
						<img style="width: 22px; cursor: pointer;" src="static/images/add.png"
							title="添加节点"> <img style="width: 22px; cursor: pointer;"
							src="static/images/accept.png" title="保存节点更新"> <img
							style="width: 20px; cursor: pointer;"
							src="static/images/cancel_del.png" title="取消编辑"> <img
							style="width: 22px; cursor: pointer;" src="static/images/delete.png"
							title="删除节点">
					</div>
					<input type="hidden" name="outMap">
					<fieldset>
						<legend>标准输出</legend>
						<div id="_StandOutEdit" class="easyui-datagrid"
								style="width: 100%; height: 100%"
								data-options="title:'',rownumbers:true,singleSelect:false,
								autoRowHeight:true,striped:true,
								idField: 'id', treeField: 'text'"></div>
					</fieldset>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<div class="btns">
			    		<span id="_SaveProviderBtn">保存</span>
			    		<span onclick="$('#providerWin').window('close');">关闭</span>
			    	</div>
				</td>
			</tr>
	    </table>
	    </form>
	</div>
	<!-- 编辑接口 -->
	<div id="_edit_intf" class="join_div">
		<iframe></iframe>
	</div>
	
	<div id="intfTestWin" class="easyui-window" title="接口测试" style="width:100%;"
	    data-options="iconCls:'icon-save',modal:true, closed:true">
	    <form id="_TestIntf">
	    	<table align="top" style="background: #e4e4e4; width: 100%; height:95%;">
			<tr>
				<td>接口名称：<input name="intfName" readonly="readonly"></td>
				<td>接口编码/方法名：<input name="intfCode" readonly="readonly"></td>
				<td>请求方式：<input name="reqType" readonly="readonly" style="width:60px"></td>
				<td>请求格式：<input name="reqFormat" readonly="readonly" style="width:100px"></td>
				<td>接口提供者：<input name="provider" readonly="readonly" style="width:100px"></td>
			</tr>
			</table>
			<!-- <tr>
				<td>入参说明：</td><td colspan="2">请求参数：</td><td colspan="2">应答结果：</td>
			</tr>
			<tr>
				<td width="25%">
					<textarea id="_InParamDesc" rows="37" style="width:100%;background:#e4e4e4;border:0;" readonly="readonly"></textarea>
				</td>
				<td colspan="2" width="35%">
					<textarea name="reqParams" rows="37" style="width:100%;"></textarea>
				</td>
				<td colspan="2" width="40%">
					<textarea name="results" rows="37" style="width:100%;"></textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2" width="40%" align="right">
					<div class="btns"><span onclick="$('#intfTestWin').window('close');">关闭</span></div>
				</td>
				<td colspan="3" width="60%"><div class="btns"><span id="_TestIntfBtn">发送请求</span></div></td>
			</tr>
			</table> --> 
			<jsp:include page="intf_test.jsp"></jsp:include>
	    </form>
	</div>

	<script type="text/javascript">
	
		function add_provider(){
			$('#providerWin').window('open');
		}
		function edit_provider(row){
			$('#providerWin').window('open');
			$('[name="id"]').val(row.id)
			$('[name="code"]').val(row.code)
			$('[name="provider"]').val(row.provider)
			$('[name="remark"]').val(row.remark)
			$('[name="reqUrl"]').val(row.reqUrl)
			var headerMap = row.headerMap;
	    	var outMap = row.outMap;
	    	$('#_ReqHeader').datagrid("loadData", headerMap);
	    	$('#_StandOutEdit').treegrid("loadData", outMap);
		}
		
		function add_intf(code) {
			if(window.intfData){
				window.intfData.id = "";
			}
			$("#_edit_intf").show();	
			$("#_edit_intf iframe").attr("src", "add_intf.jsp?code="+code);
		}
		
		window.intfData = null;
		function edit_intf(row){
			$("#_edit_intf").show();	
			$("#_edit_intf iframe").attr("src", "add_intf.jsp?code="+row.provider);
			window.intfData = row;
		}
		
		function close(code){
			$("#_edit_intf").hide();
			if(code){				
				$("#_Intfs").attr("src", "intf_list.jsp?code="+code);
			}
		}
		
		function intf_test(row){
			$('#intfTestWin').window('open');
			$('[name="intfName"]').val(row.intfName)
			$('[name="intfCode"]').val(row.intfCode)
			$('[name="provider"]').val(row.provider)
			$('[name="reqType"]').val(row.reqType)
			$('[name="reqFormat"]').val(row.reqFormat)
			if(row.paramMap && row.paramMap.length>0){
				/* $('#_InParamDesc').val(JSON.stringify(getNodeDesc(row.paramMap[0]), null, 2))*/
				$('[name="reqParams"]').val(JSON.stringify(nodeToJson(row.paramMap[0], row.intfCode), null, 4));  
				
				var tt = $("#_prams_input");	
				tt.html('');
				var json = nodeToJson2(row.paramMap[0]);
				for(prop in json){
					var val = (prop == 'method' ? row.intfCode : '');
					var row;
					if(json[prop] instanceof Array){//数组
						var tds0 = '', tds = '';
						var arr0 = json[prop][0];
						for(sprop in arr0){
							tds0 += '<td>'+arr0[sprop]+'('+sprop+')</td>'; 
							tds += '<td><input name="'+sprop+'" style="width:100%;"></td>';
						}
						row = '<tr class="row" pnode="'+prop+'"><td colspan="2">('+prop+')：<div style="float:right;width:80%;"><table style="width:100%;"><tr>'+tds0+
						'<td width="10px;"><img onclick="add_row(this)" src="static/images/add.png"></td></tr><tr>'+tds+
						'<td></td></tr></table></div></td></tr>';
					}else{
						row = '<tr class="row"><td>'+json[prop]+'('+prop+')：</td><td style="width:50%;"><input name="'+prop+'" value="'+val+'" style="width:100%;"></td></tr>';
					}
					tt.append(row);
				}
			}
		}
		
		function add_row(obj){
			var ptr = $(obj).parent().parent();
			var next = ptr.next().clone();
			var del = $('<img src="static/images/delete.png">');
			next.find('td:last').append(del);
			ptr.parent().append(next);
			del.click(function(){
				$(this).parent().parent().remove();
			});
		}
		
		var providerColArr = [ [ 
		             		   {field : 'id', title : '序号'},
		             		   {field : 'code', title : '提供者编码'}, 
		             		   {field : 'provider',title : '提供者'}, 
		             		   {field : 'reqUrl',title : '请求地址'}, 
		             		   {field : 'remark',title : '描述'}, 
		             		   {field : 'op',title : '操作',
		             			formatter: function(value,row,index){
		             				return"<a href='javascript:void(0)' class='easyui-linkbutton' onclick='edit_provider("+JSON.stringify(row)+")'>编辑</a> ";
		             			}
		             		   } 
		             		] ];
		var reqHeaderColArr = [ [ 
			             		   {field : 'id', title : '序号',hidden : true}, 
			             		   {field : 'paramCode',title : '参数编码',editor : 'text'}, 
			             		   {field : 'paramVal',title : '参数值',editor : 'text'}
			             		] ];
		
		var ecolArr = [ [ 
		     			{field : 'id',title : 'id',hidden : true}, 
		     			{field : 'parentId',title : '父节点',hidden : true}, 
		     			{field : 'text',title : '描述',editor : 'text'}, 
		     			{field : 'nodeName',title : '请求参数/节点编码',editor : 'text'}, 
		     			{field : 'attrName',title : '标准映射参数',editor : 'text'}, 
		     			{field : 'attrType',title : 'Type属性',editor : 'text'} 
		     		] ];

		var data1 = {rows : [ 
		    {"id" : '111',"text" : '根节点',"nodeName" : 'root',"attrName" : '',"attrType" : '',"parentId" : '0'} 
		]};
		var data2 = {rows : [ 
		    {"id" : '111',"paramCode" : '',"paramVal" : ''} 
		]};  
		
		window.headerData = null;
		window.outMap = null;
		$(function(){
			$('#_search').click(function(){
				var provider = $('#_QryContaner').find('[name=provider]').val();
				$('#_Provider').datagrid("reload", {provider : provider});
			});
			$('#_Provider').datagrid({
				url : '${ctx }/joy/readProvider',
				method : 'post',
				columns : providerColArr,
				onDblClickRow : function(index, row) {
					$("#tab1").css("width", "50%");
					$("#_Intfs").attr("src", "intf_list.jsp?code="+row.code);
					$("#_StandOut").attr("src", "standout_info.jsp?code="+row.code);
					window.headerData = row.headerMap;
					window.outMap = row.outMap;
				}
			});
			
			var gridUtil0 = new GridUtil($('#_ReqHeader'), reqHeaderColArr, data2, 1);
			var gridUtil = new GridUtil($('#_StandOutEdit'), ecolArr, data1);
			
			var editBtns = $('#_edit_btns');
			editBtns.find('img:first').click(function() {
				if ('_StandOutEdit' == GridUtil.currTab)
					gridUtil.addRow();
				if ('_ReqHeader' == GridUtil.currTab)
					gridUtil0.addRow();
			});
			editBtns.find('img').eq(1).click(function() {
				if ('_StandOutEdit' == GridUtil.currTab)
					gridUtil.save(gridUtil.getCurrId());
				if ('_ReqHeader' == GridUtil.currTab)
					gridUtil0.save(gridUtil.getCurrId());
			});
			editBtns.find('img').eq(2).click(function() {
				if ('_StandOutEdit' == GridUtil.currTab)
					gridUtil.cancelEdit(gridUtil.getCurrId());
				if ('_ReqHeader' == GridUtil.currTab)
					gridUtil0.cancelEdit(gridUtil.getCurrId());
			});
			editBtns.find('img:last').click(function() {
				if (confirm("确认删除该节点？")) {
					if ('_StandOutEdit' == GridUtil.currTab)
						gridUtil.delRow();
					if ('_ReqHeader' == GridUtil.currTab)
						gridUtil0.delRow();
				}
			});
			
			
			$('#_SaveProviderBtn').on('click',function(){//保存信息
				if(!$('[name="code"]').val()){
					alert('提供者编码不能为空');
					return ;
				}
				var headerMap = $('#_ReqHeader').datagrid('getData');
		    	var outMap = $('#_StandOutEdit').treegrid('getData');
		    	$('[name="headerMap"]').val(JSON.stringify(headerMap));
		    	$('[name="outMap"]').val(JSON.stringify(outMap));
		    	var url = "${ctx}/joy/saveProvider"; 
		    	$('#_SaveProvider').ajaxSubmit({
					type: "post",  //提交方式  
					dataType: "json", //数据类型  
		            url : url,
		            success : function(jqXhr) {
		                if(jqXhr.success == true){
		                	$('#providerWin').window('close');
		                	$('#_Provider').datagrid("reload",{});
		                }
		                alert(jqXhr.msg);
		            }
		        });
		    	
		    });
			
			
			$('#_TestIntfBtn').on('click',function(){//接口测试
				$("[name='results']").val('');
				var params = {};
				$("#_prams_input .row").each(function(obj){
					if($(this).attr("pnode")){
						var arr = new Array();
						$(this).find("table tr").each(function(i, sub){
							if(i>0){
								var sub = {};
								$(this).find("input").each(function(ip){
									sub[$(this).attr("name")] = $(this).val();
								});
								arr.push(sub);
							}
						});
						params[$(this).attr("pnode")] = arr;
					}else{
						var o = $(this).find("input");
						params[o.attr("name")] = o.val();
					}
				});
				$('[name="reqParams"]').val(JSON.stringify(params, null, 4));  
		    	var url = "${ctx}/joy/testIntf"; 
		    	$('#_TestIntf').ajaxSubmit({
					type: "post",  //提交方式  
					dataType: "json", //数据类型  
		            url : url,
		            success : function(jqXhr) {
		                $("[name='results']").val(JSON.stringify(jqXhr, null, 4))
		            }
		        });
		    	
		    });
		});
		
	</script>
</body>

</html>
