<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html>
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8">
		<title></title>
		<link type="text/css" rel="stylesheet" href="style/user.css">
		<style type="text/css">
			.fb-con table { width: 100%; }
			.fb-con table tr td:first-child { height: 30px; width: 40%; text-align: right; }
			.inp { width: 150px; }
		</style>
	</head>
	
	<body>
	<div id="body">
		<div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.system.operator"/> &gt; <spring:message code="menu.system.operatorinvoice"/></div>
		<form action="" method="post" id="searchform" name="searchform" autocomplete="off">
		  <input type="hidden" name="operatorid" id="operatorid" value="${Operator.id }"/>
	      <div class="form-box">
    	  <div class="fb-tit"><spring:message code="menu.system.operatorinvoice"/></div>
          <div class="fb-con">
		        <table>
			            <tr>
				            <td>开始发票号：</td>
				            <td>
				            	<input type="text" class="inp" name="startinvoicecode" id="startinvoicecode" value="${operatorinvoice.operatorinvoice.startinvoicecode }" 
				            	onkeyup="checkNum(this)" onkeypress="checkNum(this)" onblur="checkNum(this)" onafterpaste="this.value=this.value.replace(/\D/g,'')"
				            	maxlength="8">
				            	<span class="red">*</span>
				            </td>
						</tr>
					    <tr>
						    <td>结束发票号：</td>
						    <td>
                               <input type="text" class="inp" name="endinvoicecode" id="endinvoicecode" value="${operatorinvoice.operatorinvoice.endinvoicecode }" 
                               onkeyup="checkNum(this)" onkeypress="checkNum(this)" onblur="checkNum(this)" onafterpaste="this.value=this.value.replace(/\D/g,'')"
                               maxlength="8" >
                            	<span class="red">*</span>
                            </td>
					    </tr>
		        </table>
	        </div>
	        <div class="fb-bom">
		        <cite>
          			<input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="updateOperatorinvoice();"/>
		        </cite>
		        <span class="red">${operatorinvoice.returninfo }</span>
		    </div>
      </div>
    </form>
	</div>
	<script type="text/javascript" src="js/common/jquery.js"></script>
    <script type="text/javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
	<script type="text/javascript" src="js/form.js"></script>
	<script type="text/javascript">
	
	function updateOperatorinvoice(){
		//开始发票号
	    $("#startinvoicecode").val($.trim($("#startinvoicecode").val()));
		if($("#startinvoicecode") != undefined && $("#startinvoicecode").val() == ""){
			$.dialog.tips('请输入开始发票号', 1, "alert.gif", function() {
				$("#startinvoicecode").focus();
			});
			return false;
		}
		
		//结束发票号
	    $("#endinvoicecode").val($.trim($("#endinvoicecode").val()));
		if($("#endinvoicecode") != undefined && $("#endinvoicecode").val() == ""){
			$.dialog.tips('请输入结束发票号', 2, "alert.gif", function() {
				$("#endinvoicecode").focus();
			});
			return false;
		}
		
		$("#searchform").attr("action", "operator/updateOperatorinvoice").submit();
	}
	
	
	
	$(function() {
		var returninfo = '${operatorinvoice.returninfo}';
	       if (returninfo != '') {
	        	$.dialog.tips(returninfo, 1, 'alert.gif');
	       }
	});
	
</script>   
</body>
</html>
