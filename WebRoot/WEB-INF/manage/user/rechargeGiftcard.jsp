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
      height: 25px;
    }
  </style>
</head>

<body>
<div id="body">
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.business.manage"/> &gt; <spring:message code="menu.business.giftcardrecharge"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-tit"><spring:message code="menu.business.giftcardrecharge"/></div>
      <div class="fb-con">
    	<div id="userInfo"></div>
    	<table>
          <tr class="fbc-tit">
            <td colspan="6" style="font-weight: bold;"><spring:message code="user.stbinfo"/></td>
          </tr>
          <tr class="lb-tit">
          		<td><spring:message code="stb.stbno"/></td>
	          	<td><spring:message code="user.buytime"/></td>
	          	<td><spring:message code="userstb.state"/></td>
	          	<td><spring:message code="userstb.mothercardflag"/></td>
	          	<td><spring:message code="userstb.mothercardid"/></td>
         </tr>
         <c:forEach items="${user.user.userstblist }" var="dataList">
	        	<tr height="30"class="lb-list">
	        		<td >${dataList.stbno }</td>
	        		<td >${fn:substring(dataList.addtime, 0, 19)}</td>
	        		<td ><spring:message code="userstb.state.${dataList.state }"/></td>
	        		<td >
		        		<c:if test="${dataList.incardflag=='2'}">
		        			<spring:message code="userstb.mothercardflag.${dataList.mothercardflag }"/>
		        		</c:if>
	        		</td>
	        		<td >${dataList.mothercardid }</td>
	        	</tr>
        	</c:forEach>
        </table>
    	<table>
          <tr class="fbc-tit">
            <td colspan="6" style="font-weight: bold;"><spring:message code="user.cardinfo"/></td>
          </tr>
          <tr class="lb-tit">
          		<td><spring:message code="card.cardid"/></td>
	          	<td><spring:message code="user.buytime"/></td>
	          	<td><spring:message code="usercard.state"/></td>
	          	<td><spring:message code="usercard.mothercardflag"/></td>
	          	<td><spring:message code="usercard.mothercardid"/></td>
	          	<td>高清/标清</td>
         </tr>
         <c:forEach items="${user.user.usercardlist }" var="dataList">
	        	<tr height="30"class="lb-list">
	        		<td >${dataList.cardid }</td>
	        		<td >${fn:substring(dataList.addtime, 0, 19)}</td>
	        		<td ><spring:message code="usercard.state.${dataList.state }"/></td>
	        		<td >
		        		<c:if test="${dataList.mothercardflag != null && dataList.mothercardflag != '' }">
		        			<spring:message code="userstb.mothercardflag.${dataList.mothercardflag }"/>
		        		</c:if>
	        		</td>
	        		<td >${dataList.mothercardid }</td>
	        		<td >
	        			<c:choose>  
						    <c:when test="${dataList.cardflag eq '0'}" >   
		        				<span class="red">高清</span>
						    </c:when> 
						    <c:otherwise>
						    	标清
						    </c:otherwise>  
					    </c:choose>
	        		</td>
	        	</tr>
        	</c:forEach>
        </table>
        <div>
	    	<table>
	          <tr class="fbc-tit">
	            <td colspan="8" style="font-weight: bold;"><spring:message code="menu.business.accountrecharge"/></td>
	          </tr>
	          <tr>
	            <td align="right"><spring:message code="giftcard.giftcardno"/>：</td>
	            <td colspan="3">
	            	<input type="text" class="inp" style="width:150px;" name="giftcardno" id="giftcardno" value="${user.giftcardno}" autocomplete="off" ><span class="red">*</span>
	            </td>
	            <td align="right"><spring:message code="giftcard.password"/>：</td>
	            <td colspan="3">
                    <input type="password" class="inp" name="password" id="password" maxlength="6" autocomplete="off" ><span class="red">*</span>
	            </td>
	          </tr>
	        </table>
		</div>
      </div>
      <div class="fb-bom">
        <cite>
        	<c:choose>  
               <c:when test="${user.user.state ne '0' && user.user.state ne '3'}">
               		<input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="saveRechargeGiftcard();" id="btnfinish">
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
    
    function saveRechargeGiftcard() {
        $("#giftcardno").val($.trim($("#giftcardno").val()));
        if ($('#giftcardno').val() == '') {
		      $.dialog.tips('<spring:message code="giftcard.giftcardno.empty"/>', 1, 'alert.gif');
		      $('#giftcardno').focus();
		      return;
	    }
	    $("#password").val($.trim($("#password").val()));
        if ($('#password').val() == '') {
		      $.dialog.tips('<spring:message code="giftcard.password.empty"/>', 1, 'alert.gif');
		      $('#password').focus();
		      return;
	    }
        
        $.dialog.confirmMultiLanguage('<spring:message code="page.confirm.execution"/>?',
 	             '<spring:message code="page.confirm"/>',
 	             '<spring:message code="page.cancel"/>',
 	             function(){ 
					 $("#addform").attr("action", "user/saveRechargeGiftcard");
			    	 $("#addform").submit();
		         }, 
                 function(){
		});
    }
    
    
	$(function () {
	      loadUserInfo();
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
	
	var unitBusinessDialog;
	function unitBusiness() {
	  
	   var address = $("#address").val();
	   if(address == null || address == ''){
	   		$.dialog.tips('<spring:message code="user.newaddress.empty"/>', 1, 'alert.gif');
	   		$("#address").focus();
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
		    content: 'url:user/unitBusiness?businesstype='+$('#businesstype').val()+'&address='+$('#address').val()
		});
	 }
	
	function closeBusinessDialog(){
		unitBusinessDialog.close();
		loadUserInfo();
	}
	
	
	/////////////////////对必要的输入框进行数字合法验证//////////////////////////
	function onkeypressCheck(obj){
		if (!obj.value.match(/^[1-9]?\d*?\.?\d*?$/)) {
			obj.value = obj.t_value;
		} else {
			obj.t_value = obj.value;
		}
		if (obj.value.match(/^(?:[1-9]?\d+(?:\.\d+)?)?$/)) {
			obj.o_value = obj.value;
		}
		if(obj.value.match(/^\d+\.\d{3}?$/)){
		   obj.value = obj.value.substr(0,obj.value.length-1);
		}
		updateAccountmoney();
	}
	
	function onkeyupCheck(obj){
		if (!obj.value.match(/^[1-9]?\d*?\.?\d*?$/)) {
				obj.value = obj.t_value;
			} else {
				obj.t_value = obj.value;
			}
			if (obj.value.match(/^(?:[1-9]?\d+(?:\.\d+)?)?$/)) {
				obj.o_value = obj.value;
			}
			if (obj.value.match(/^\.$/)) {
				obj.value = "";
			}
			if (obj.value.match(/^\-$/)) {
				obj.value = "";
			}
			if (obj.value.match(/^00+/)) {
				obj.value = "";
			}
			if (obj.value.match(/^0\.00/)) {
				obj.value = "";
			}
			if (obj.value.match(/^0[1-9]/)) {
				obj.value = "";
			}
			if(obj.value.match(/^\d+\.\d{3}?$/)){
				obj.value = obj.value.substr(0,obj.value.length-1);
			} 
			if(obj.value == 'undefined'){
				obj.value='';
			}
			
			updateAccountmoney();
	}
	
	function onkeyblurCheck(obj){
		if(obj.value==0){
			//obj.value='';
		}
		if(obj.value==''){
			obj.value = 0;
		}
		if (!obj.value.match(/^(?:[1-9]?\d+(?:\.\d+)?|\.\d*?)?$/)) {
			obj.value = obj.o_value;
		}else {
			if (obj.value.match(/^\.\d+$/)) {
				obj.value = 0 + obj.value;
			}
			obj.o_value = obj.value;
		}
		if(!obj.value.match(/^\d+(\.\d{3})?$/)){
			obj.value = obj.value.substr(0,obj.value.indexOf(".")+3);
		} 
		updateAccountmoney();
	}
</script>
</body>
</html>
