<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<% 	String pcode = request.getParameter("code"); 
	String ctx = request.getContextPath();
	request.setAttribute("ctx", ctx);
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="static/default/easyui.css">
<link rel="stylesheet" type="text/css" href="static/icon.css">
<script type="text/javascript" src="static/jquery.min.js"></script>
<script type="text/javascript" src="static/jquery.easyui.min.js"></script>
<script type="text/javascript" src="static/jQuery.Form.js"></script>
<script type="text/javascript" src="static/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="static/Utils.js"></script>

<style type="text/css">
.ddd {
	cursor: pointer;
}

.btns {padding:10px;}
.btns span{border:1px solid lightblue;padding:2px 10px;}

</style>
</head>
<body>
<form id="_SaveIntf">
	<input type="hidden" name="id">
	<input type="hidden" name="provider" value="<%=pcode%>">
	<div id="_edit_btns"
		style="position: absolute; z-index: 100000; display: none;">
		<img style="width: 22px; cursor: pointer;" src="static/images/add.png"
			title="添加节点"> <img style="width: 22px; cursor: pointer;"
			src="static/images/accept.png" title="保存节点更新"> <img
			style="width: 20px; cursor: pointer;"
			src="static/images/cancel_del.png" title="取消编辑"> <img
			style="width: 22px; cursor: pointer;" src="static/images/delete.png"
			title="删除节点">
	</div>
	<div>
		<div>
			<span>接口配置</span>
			<div>
				<span
					style="width: 92%; background: #fff; height: 1px; float: left; margin: 10px 10px;"></span>
				<!-- <span id="_templete_selor" style="float: right; margin-right: 20px;"><a
					href="#">选择模版</a></span> -->
			</div>
			<!-- <div style="width: 160px; display: none; position: absolute; background: #fff; margin-top: 2px; right: 4px; border: 1px solid #666; z-index: 100000;">
				<ul>
					<li>fdaskfdjaskf</li>
					<li>fdaskfdjaskf</li>
				</ul>
			</div> -->
		</div>
		<table align="center" style="background: #e4e4e4; width: 99%;">
			<tr>
				<td>接口名称<input name="intfName"></td>
				<td>接口编码/方法<input name="intfCode"></td>
				<td>编码/方法<select id="reqType" class="easyui-combobox" name="reqType" style="width:200px;">
					    <option value="METHOD">方法</option>
					    <option value="CODE">编码</option>
					</select></td>
				<td>请求及应答格式<select id="reqFormat" class="easyui-combobox" name="reqFormat" style="width:200px;">
					    <option value="json">JSON格式</option>
					    <option value="xml">XML格式</option>
					</select></td>
			</tr>
			<tr>
				<td colspan="2" valign="top" width="50%">
					<input type="hidden" name="paramMap">
					<fieldset>
						<legend>入参映射</legend>
						<div style="width: 100%; height: 100%;">
							<div id="_inparams_map" class="easyui-datagrid"
								style="width: 100%; height: 100%"
								data-options="title:'',rownumbers:true,singleSelect:false,
								autoRowHeight:true,striped:true,animate: true, collapsible: true,
								idField: 'id', treeField: 'text'"></div>
						</div>
					</fieldset>
				</td>
				<td colspan="2" valign="top" width="50%">
					<input type="hidden" name="resultMap">
					<fieldset>
						<legend>结果数据节点映射</legend>
						<div style="width: 100%; height: 100%;">
							<div id="_result_map" class="easyui-datagrid"
								style="width: 100%; height: 100%"
								data-options="title:'',rownumbers:true,singleSelect:false,
								autoRowHeight:true,striped:true,
								idField: 'id', treeField: 'text'"></div>
						</div>
					</fieldset>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<div class="btns">
						<span id="_SaveIntfBtn">保存</span>
						<!-- <span>同时保存为模版</span>
						<span>预览</span> -->
						<span onclick="parent.close()">关闭</span>
					</div>
				</td>
			</tr>
		</table>
	</div>
</form>
	<script type="text/javascript">
		var attrTypeData = [{id:'fit',name:'固定值'},{id:'auto',name:'随机值'},{id:'list',name:'列表'},{id:'false',name:'非必须'}]
		var inColArr = [ [ 
			{field : 'id',title : 'id',hidden : true}, 
			{field : 'parentId',title : '父节点',hidden : true}, 
			{field : 'text',title : '描述', width: 200,editor : 'text'}, 
			{field : 'nodeName',title : '请求参数/节点编码', width: 150,editor : 'text'}, 
			{field : 'attrName',title : '标准映射参数', width: 120,editor : 'text'}, 
			{field : 'attrType',title : 'Type属性', width: 80,
				editor : {type : 'combobox', options : {valueField : 'id', textField : 'name', data : attrTypeData}}
			}
		] ];
		
		var outColArr = [ [ 
		     			{field : 'id',title : 'id',hidden : true}, 
		     			{field : 'parentId',title : '父节点',hidden : true}, 
		     			{field : 'text',title : '描述', width: 200,editor : 'text'}, 
		     			{field : 'nodeName',title : '标准映射参数', width: 150,editor : 'text'}, 
		     			{field : 'attrName',title : '结果节点编码', width: 120,editor : 'text'}, 
		     			{field : 'attrType',title : 'Type属性', width: 80,
		     				editor : {type : 'combobox', options : {valueField : 'id', textField : 'name', data : attrTypeData}}
		     			}
		     		] ];
		

		var data1 = {rows : [ 
			{"id" : '111',"text" : '根节点',"nodeName" : 'root',"attrName" : '',"attrType" : '',"parentId" : '0'} 
		]};

		var data2 = {rows : [ 
		    {"id" : '111',"text" : '数据节点',"nodeName" : 'rows',"attrName" : '',"attrType" : '',"parentId" : '0'} 
		]};
		
		$(function() {
			$('#_SaveIntfBtn').on('click',function(){//保存信息
				if(!$('[name="intfCode"]').val()){
					alert('接口编码/方法不能为空');
					return ;
				}
		    	var url = "${ctx}/joy/saveIntf"; 
		    	var paramMap = $('#_inparams_map').treegrid('getData');
		    	var resultMap = $('#_result_map').treegrid('getData');
		    	$('[name="paramMap"]').val(JSON.stringify(paramMap));
		    	$('[name="resultMap"]').val(JSON.stringify(resultMap));
		    	$('#_SaveIntf').ajaxSubmit({
					type: "post",  //提交方式  
					dataType: "json", //数据类型  
		            url : url,
		            success : function(jqXhr) {
		                if(jqXhr.success == true){
		                	parent.close($("[name='provider']").val());
		                }
		                alert(jqXhr.msg);
		            }
		        });
		    	
		    });
			
			if(parent.intfData){
				data1.rows = parent.intfData.paramMap;
				data2.rows = parent.intfData.resultMap;
				$('[name="id"]').val(parent.intfData.id);
				$('[name="intfName"]').val(parent.intfData.intfName);
				$('[name="intfCode"]').val(parent.intfData.intfCode);
				$('#reqType').combobox('setValue', parent.intfData.reqType);
				$('#reqFormat').combobox('setValue', parent.intfData.reqFormat);
			}
			var gridUtil1 = new GridUtil($('#_inparams_map'), inColArr, data1);
			var gridUtil2 = new GridUtil($('#_result_map'), outColArr, data2);

			var editBtns = $('#_edit_btns');
			editBtns.find('img:first').click(function() {
				if ('_inparams_map' == GridUtil.currTab)
					gridUtil1.addRow();
				if ('_result_map' == GridUtil.currTab)
					gridUtil2.addRow();
			});
			editBtns.find('img').eq(1).click(function() {
				if ('_inparams_map' == GridUtil.currTab)
					gridUtil1.save(gridUtil1.getCurrId());
				if ('_result_map' == GridUtil.currTab)
					gridUtil2.save(gridUtil2.getCurrId());
			});
			editBtns.find('img').eq(2).click(function() {
				if ('_inparams_map' == GridUtil.currTab)
					gridUtil1.cancelEdit(gridUtil1.getCurrId());
				if ('_result_map' == GridUtil.currTab)
					gridUtil2.cancelEdit(gridUtil2.getCurrId());
			});
			editBtns.find('img:last').click(function() {
				if (confirm("确认删除该节点？")) {
					if ('_inparams_map' == GridUtil.currTab)
						gridUtil1.delRow();
					if ('_result_map' == GridUtil.currTab)
						gridUtil2.delRow();
				}
			});

			$('#_templete_selor').click(function() {
				var sel = $(this).parent().next();
				if (sel.css("display") == 'none') {
					sel.show();
					sel.css({top : $(this).offset().top + 18});
				} else {
					sel.hide();
				}
			});
		});
		
	</script>
</body>

</html>
