<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div style="width:100%;height:600px;overflow-y:auto; ">
<table align="top" style="background: #e4e4e4; width: 100%; height:95%;">
	<tr>
		<td>输入后请求：</td><td><input type="checkbox" id="">按以下参数请求</td><td>应答结果：</td>
	</tr>
	<tr>
		<td width="40%" style="background: #fff;" valign="top">
			<table id="_prams_input"></table>
		</td>
		<td width="30%" valign="top">
			<textarea name="reqParams" rows="37" style="width:100%;height:99%;"></textarea>
		</td>
		<td valign="top">
			<textarea name="results" rows="37" style="width:100%;height:99%;"></textarea>
		</td>
	</tr>
</table>
</div>
<div class="btns" align="center">
	<span onclick="$('#intfTestWin').window('close');">关闭</span>
	<span id="_TestIntfBtn">发送请求</span>
</div>