<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!doctype html>
<html>
<head>
  <base href="<%=basePath%>">
  <meta charset="utf-8">
  <title></title>
  <link type="text/css" rel="stylesheet" href="style/user.css">
  <style>
   .fb-con table { 
    	width: 100%; border: 0; border-spacing: 0; border-collapse: collapse; 
    }
    .fb-con table tr {
      height: 30px;
    }
  </style>
</head>

<body>
<div id="body">
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.business.manage"/> &gt; <spring:message code="menu.business.rebackdevice"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="id" id="id" value="${user.user.id}"/>
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-tit"><spring:message code="menu.business.rebackdevice"/></div>
      <div class="fb-con">
    	<div id="userInfo"></div>
    	<div id="userCardInfo"></div>
    	<div class="oldDeviceInfo">
		    	<table>
		          <tr class="fbc-tit">
		            <td colspan="8" style="font-weight: bold;"><spring:message code="userbusiness.rebackdevice.state"/></td>
		          </tr>
		          <tr>
		            <td align="right" colspan="2"><span class="red"><spring:message code="userbusiness.rebackdevice.state"/>：</span></td>
		            <td colspan="6">
		            	<select id="devicestate" name="devicestate" class="select">
		            	 	   <option value="3"><spring:message code="stb.state.3"/></option>
			                   <option value="0"><spring:message code="stb.state.0"/></option>
			            </select>
			            <span class="red">*</span>
		            </td>
		          </tr>
		        </table>
		</div>
      </div>
     
      <div class="fb-bom">
        <cite>
        	<input type="button" class="btn-save" value="<spring:message code="business.type.rebackdevice"/>" onclick="unitCancelBusiness();" id="btnfinish">
        </cite>
        <span class="red">${user.returninfo }</span>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript" src="js/common/jquery.js"></script>
<script type="text/javascript" src="js/form.js"></script>
<script type="text/javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
<script type="text/javascript">
  
	$(function () {
	      loadUserInfo();
	      loadUserCardInfo();
	      var returninfo = '${user.returninfo}';
	      if (returninfo != '') {
	       	$.dialog.tips(returninfo, 1, 'alert.gif');
	      }
	 });
  
	 function loadUserInfo() {
	   var data = {
	     id: '${user.user.id}',
	     tag: 'userInfo'
	   };
	   var url = 'user/getUserInfo .' + data.tag;
	   $('#userInfo').load(url, data, function () {
	   });
	 }
  
	 function loadUserCardInfo() {
	   var data = {
	     id: '${user.user.id}',
	     tag: 'userRebackTerminalInfo'
	   };
	   var url = 'user/getUserCardInfo .' + data.tag;
	   $('#userCardInfo').load(url, data, function () {
	   });
	 }
	
	var unitCancelBusinessDialog;
	function unitCancelBusiness() {
	   
	   //终端号
	   var terminalid = $("input[type='radio']:checked").val();
	   if(terminalid == null || terminalid == ''){
	   		$.dialog.tips('<spring:message code="user.terminalid.empty"/>', 1, 'alert.gif');
	   		return false;
	   }
	   
	   //终端类型
	   var terminaltype=  $("#type_"+terminalid).val();
	   
	   
	   
	   //判断如果回收的终端有副机，先请回收副机
	   var data = {
			   terminalid: terminalid,
			   terminaltype: terminaltype
	   };
		var url="user/validRebakDevice?rid="+Math.random();
		$.getJSON(url,data,function(jsonMsg){
			if(jsonMsg.flag=="0"){
			    $.dialog.tips(jsonMsg.error, 2, 'alert.gif');
			    return false;
			}else if(jsonMsg.flag=="1"){
				unitCancelBusinessDialog = $.dialog({
				    title: '<spring:message code="userbusiness.feecount"/>',
				    lock: true,
				    width: 800,
				    height: 400,
				    top: 0,
				    drag: false,
				    resize: false,
				    max: false,
				    min: false,
				    cancel:false,
				    content: 'url:user/unitCancelBusiness?businesstype='+$('#businesstype').val()+'&terminalid='+terminalid+'&terminaltype='+terminaltype+'&devicestate='+$('#devicestate').val()
				});
			}
		});
	 }
	
	function closeCancelBusinessDialog(){
		unitCancelBusinessDialog.close();
		$("#addform").attr("action", "user/businessUnit");
      	$("#addform").submit();
	}
	
</script>
</body>
</html>
