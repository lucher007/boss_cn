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
  <link rel="stylesheet" type="text/css" href="style/easyui/easyui.css">
  <style>
   .fb-con table { 
    	width: 100%; border: 0; border-spacing: 0; border-collapse: collapse; 
    }
    .fb-con table tr {
      height: 25px;
    }
  </style>
</head>

<body>
<div id="body">
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.business.manage"/> &gt; <spring:message code="menu.business.resetbinding"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-tit"><spring:message code="menu.business.resetbinding"/></div>
      <div class="fb-con">
    	<div id="userInfo"></div>
        <div class="oldStbInfo">
	    	<table>
	          <tr class="fbc-tit">
	            <td colspan="8" style="font-weight: bold;"><spring:message code="menu.business.resetbinding"/></td>
	          </tr>
	          <tr>
	          	<td height="30" width="10%" align="right"><spring:message code="stb.stbno"/>：</td>
				<td>
					<select id="stbno" name="stbno" class="select">
		                <c:forEach items="${user.user.userstblist}" var="userstb" varStatus="s">
		                  <c:if test="${userstb.incardflag == '0' || userstb.incardflag == '1'}">
		                  	<option value="${userstb.stbno}">${userstb.stbno}</option>
		                  </c:if>
		                </c:forEach>
		            </select>
				</td>
				<td height="30" width="10%" align="right"><spring:message code="userstb.cardid"/>：</td>
				<td>
					<select id="cardid" name="cardid" class="select">
						<option value=""><spring:message code="page.select"/></option>
		                <c:forEach items="${user.user.usercardlist}" var="usercard" varStatus="s">
		                  <c:if test="${usercard.stbno == null || usercard.stbno == ''}">
		                  	<option value="${usercard.cardid}">${usercard.cardid}</option>
		                  </c:if>
		                </c:forEach>
		            </select>
				</div>
	          </tr>
	        </table>
		</div>
      </div>
      <div class="fb-bom">
        <cite>
        	<c:choose>  
               <c:when test="${user.user.state ne '0' && user.user.state ne '3'}">
               		<input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="saveResetBinding();" id="btnfinish">
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
<script type="text/javascript" src="js/common/jquery.easyui.min.js"></script>
<script type="text/javascript">
  
	$(function () {
	      loadUserInfo();
	      var returninfo = '${user.returninfo}';
	      if (returninfo != '') {
	       	$.dialog.tips(returninfo, 2, 'alert.gif');
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
  
  function saveResetBinding() {
        
        var stbno = $("#stbno").val();
        if(stbno == null || stbno == ''){
	   		$.dialog.tips('<spring:message code="userproduct.stbno.empty"/>', 2, 'alert.gif');
	   		$("#stbno").focus();
	   		return false;
	    }
        
        var cardid = $("#cardid").val();
        if(cardid == null || cardid == ''){
	   		$.dialog.tips('<spring:message code="userproduct.cardid.empty"/>', 2, 'alert.gif');
	   		$("#cardid").focus();
	   		return false;
	    }
      
        $.dialog.confirmMultiLanguage('<spring:message code="page.confirm.execution"/>?',
 	             '<spring:message code="page.confirm"/>',
 	             '<spring:message code="page.cancel"/>',
 	             function(){ 
					 $("#addform").attr("action", "user/saveResetBinding?stbno=" + stbno + "&cardid=" + cardid);
			    	 $("#addform").submit();
		         }, 
                 function(){
		});
    }
  
</script>
</body>
</html>
