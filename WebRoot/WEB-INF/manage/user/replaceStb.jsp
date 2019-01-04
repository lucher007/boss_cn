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
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.business.manage"/> &gt; <spring:message code="menu.business.stbreplace"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="id" id="id" value="${user.user.id}"/>
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-tit"><spring:message code="menu.business.stbreplace"/></div>
      <div class="fb-con">
    	<div id="userInfo"></div>
        <div id="userStbInfo"></div>
        <div id="replaceStbInfo"></div>
        <div class="oldstbInfo">
		    	<table>
		          <tr class="fbc-tit">
		            <td colspan="8" style="font-weight: bold;"><spring:message code="userbusiness.replacestb.state"/></td>
		          </tr>
		          <tr>
		            <td align="right" colspan="2"><span class="red"><spring:message code="userbusiness.replacestb.state"/>：</span></td>
		            <td colspan="6">
		            	<select id="stbstate" name="stbstate" class="select">
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
        	<c:choose>  
               <c:when test="${user.user.state ne '0' && user.user.state ne '3'}">
               		<input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="unitBusiness();" id="btnfinish">
               </c:when>
            </c:choose>
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
	
	 function updateProduct() {
	    if ($('#userid').val() == '') {
	      $.dialog.tips('<spring:message code="user.userid.empty"/>', 1, 'alert.gif');
	      $('#userid').focus();
	      return;
	    }
	       
	    var hasSelected = false;
	    $('.checkbox').each(function () {
	      if ($(this).attr('checked')) {
	        return hasSelected = true;
	      }
	    });
	    if (!hasSelected) {
	      $.dialog.tips('<spring:message code="user.service.empty"/>', 1, 'alert.gif');
	      return;
	    }
	     
	     $('#addform').attr('action', 'user/update');
	     $("#addform").submit();
	 }
	 
	 function goBack() {
	     $("#addform").attr("action", "user/findByList");
	     $("#addform").submit();
	 }
  
  
	$(function () {
	      loadUserInfo();
	      loadUserStbInfo();
	      loadReplaceStbInfo();
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
  
	 function loadUserStbInfo() {
	   var data = {
	     id: '${user.user.id}',
	     tag: 'userStbInfo'
	   };
	   var url = 'user/getReplaceDeviceInfo .' + data.tag;
	   $('#userStbInfo').load(url, data, function () {
	   });
	 }
  
	 function loadReplaceStbInfo() {
	   var data = {
	     id: '${user.user.id}',
	     tag: 'replaceStbInfo'
	   };
	   var url = 'user/getUserInfo .' + data.tag;
	   $('#replaceStbInfo').load(url, data, function () {
	   });
	 }
  
	 var stbDialog;
	 function selectStb() {
	     var stbno = $("input[type='radio']:checked").val();
		 if(stbno == null || stbno == ''){
		   		$.dialog.tips('<spring:message code="userproduct.stbno.empty"/>', 1, 'alert.gif');
		   		return false;
		 }
	     stbDialog = $.dialog({
		    title: '<spring:message code="stb.stbquery"/>',
		    lock: true,
		    width: 900,
		    height: 500,
		    top: 0,
		    drag: false,
		    resize: false,
		    max: false,
		    min: false,
		    content: 'url:user/findReplaceStbListForDialog?querystate=0&stbno='+stbno
		});
	 }
  
	function closeStbDialog(){
		stbDialog.close();
		loadReplaceStbInfo();
	}
	
	var unitBusinessDialog;
	function unitBusiness() {
	   
	   var stbno = $("input[type='radio']:checked").val();
		 if(stbno == null || stbno == ''){
		   		$.dialog.tips('<spring:message code="userproduct.stbno.empty"/>', 1, 'alert.gif');
		   		return false;
		 }
	   
	   var replacestbid = $("#replacestbid").val();
	   if(replacestbid == null || replacestbid == ''){
	   		$.dialog.tips('<spring:message code="user.replacestb.empty"/>', 1, 'alert.gif');
	   		return false;
	   }
	   
	   unitBusinessDialog = $.dialog({
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
		    content: 'url:user/unitBusiness?businesstype='+$('#businesstype').val()+'&stbno='+stbno+'&stbstate='+$('#stbstate').val()
		});
	 }
	
	function closeBusinessDialog(){
		unitBusinessDialog.close();
		$("#addform").attr("action", "user/businessUnit");
      	$("#addform").submit();
	}
	
</script>
</body>
</html>
