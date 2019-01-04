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
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.business.manage"/> &gt; <spring:message code="menu.business.cancelproduct"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="id" id="id" value="${user.user.id}"/>
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-tit"><spring:message code="menu.business.cancelproduct"/></div>
      <div class="fb-con">
    	<div id="userInfo"></div>
    	<table>
	      	  <tr class="fbc-tit">
	            <td colspan="12" style="font-weight: bold;"><spring:message code="user.productinfo"/></td>
	          </tr>
	          <tr class="lb-tit">
	          		<td><spring:message code="user.terminalid"/></td>
	          		<td><spring:message code="user.terminaltype"/></td>
	          		<td><spring:message code="product.productid"/></td>
		          	<td><spring:message code="product.productname"/></td>
		          	<td><spring:message code="userproduct.type"/></td>
		          	<td><spring:message code="user.buytime"/></td>
		          	<td><spring:message code="userproduct.starttime"/></td>
		          	<td><spring:message code="userproduct.endtime"/></td>
		          	<td><spring:message code="userproduct.totalmoney"/></td>
		          	<td><spring:message code="userproduct.source"/></td>
		          	<td><spring:message code="userproduct.state"/></td>
		          	<td><spring:message code="page.operate"/></td>
	         </tr>
	         <c:forEach items="${user.user.userproductlist }" var="dataList">
		        	<tr height="30"class="lb-list">
		        		<td >${dataList.terminalid }</td>
		        		<td ><spring:message code="user.terminaltype.${dataList.terminaltype }"/></td>
	        			<td >${dataList.productid }</td>
	        			<td >${dataList.productname }</td>
		        		<td ><spring:message code="userproduct.type.${dataList.type }"/></td>
		        		<td >${fn:substring(dataList.addtime, 0, 19)}</td>
		        		<td >${fn:substring(dataList.starttime, 0, 19)}</td>
		        		<td >${fn:substring(dataList.endtime, 0, 19)}</td>
		        		<td >${dataList.totalmoney}</td>
		        		<td ><spring:message code="userproduct.source.${dataList.source }"/></td>
		        		<td ><spring:message code="userproduct.state.${dataList.state }"/></td>
		        		<td >
		        			<c:if test="${dataList.state eq '1' }">
		        				<a class="btn-del" href="javascript:unitCancelBusiness(${dataList.id },'${dataList.remark}');" ><spring:message code="page.cancel"/></a>
		        			</c:if>
		        		</td>
		        	</tr>
	        	</c:forEach>
	        </table>
      </div>
      <div class="fb-bom">
      <!-- 
        <cite>
        	<input type="button" class="btn-save" value="<spring:message code="page.refresh"/>" onclick="authorizeRefresh();" id="btnfinish">
        </cite>
       -->
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
	      loadUserproductInfo();
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
  
	 function loadUserproductInfo() {
	   var data = {
	     id: '${user.user.id}',
	     tag: 'userproductInfo'
	   };
	   var url = 'user/getUserproductInfo .' + data.tag;
	   $('#userproductInfo').load(url, data, function () {
	   });
	 }
	
	var unitCancelBusinessDialog;
	function unitCancelBusiness(userproductid,operatorid) {
	   
		if(operatorid != null && operatorid != '' && operatorid != '${Operator.id}'){
			$.dialog.tips('不能取消其他操作员办理的业务', 2, 'alert.gif');
			return ;
		}
		
	   //查询当前订户该产品的授权结束时间
        var data = {
				     id: userproductid
				   };
		var url="userproduct/checkedUserproductCancel?rid="+Math.random();
		$.getJSON(url,data,function(jsonMsg){
			if(jsonMsg.flag == "0"){
		   		$.dialog.tips('<spring:message code="userproduct.authorization.started"/>', 1, 'alert.gif');
		   		return false;
			}else{
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
				    content: 'url:user/unitCancelBusiness?businesstype='+$('#businesstype').val()+'&userproductid='+userproductid
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
