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
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.business.manage"/> &gt; <spring:message code="menu.authorize.authorizerefresh"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="id" id="id" value="${user.user.id}"/>
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-tit"><spring:message code="menu.authorize.authorizerefresh"/></div>
      <div class="fb-con">
    	<div id="userInfo"></div>
    	<table>
	      	  <tr class="fbc-tit">
	            <td colspan="12" style="font-weight: bold;"><spring:message code="user.productinfo"/></td>
	          </tr>
	          <tr class="lb-tit">
	          		<td style="white-space: nowrap;width: 51px"> 
			        	<input type="checkbox" id="checkall" onclick="checkAll();" style="vertical-align: middle;">
				        <label for="checkall"><spring:message code="page.select.all"/></label>
				    </td>
	          		<td><spring:message code="user.terminalid"/></td>
	          		<td><spring:message code="user.terminaltype"/></td>
	          		<td><spring:message code="product.productid"/></td>
		          	<td><spring:message code="product.productname"/></td>
		          	<td><spring:message code="user.buytime"/></td>
		          	<td><spring:message code="userproduct.type"/></td>
		          	<td><spring:message code="userproduct.starttime"/></td>
		          	<td><spring:message code="userproduct.endtime"/></td>
		          	<td><spring:message code="userproduct.source"/></td>
		          	<td><spring:message code="userproduct.state"/></td>
	         </tr>
	         <c:forEach items="${user.user.userproductlist }" var="dataList">
		        	<tr height="30"class="lb-list">
		        		<td>  
		            	    <input type="checkbox" class="checkbox" name="ids" value="${dataList.id}"
			                     onclick="checkbox();"
			                     style="vertical-align: middle;"/>
			            </td>
		        		<td >${dataList.terminalid }</td>
		        		<td ><spring:message code="user.terminaltype.${dataList.terminaltype }"/></td>
	        			<td >${dataList.productid }</td>
	        			<td >${dataList.productname }</td>
		        		<td >${fn:substring(dataList.addtime, 0, 19)}</td>
		        		<td ><spring:message code="userproduct.type.${dataList.type }"/></td>
		        		<td >${fn:substring(dataList.starttime, 0, 19)}</td>
		        		<td >${fn:substring(dataList.endtime, 0, 19)}</td>
		        		<td ><spring:message code="userproduct.source.${dataList.source }"/></td>
		        		<td ><spring:message code="userproduct.state.${dataList.state }"/></td>
		        	</tr>
	        	</c:forEach>
	        </table>
      </div>
      <div class="fb-bom">
        <cite>
        	<c:choose>  
               <c:when test="${user.user.state ne '0' && user.user.state ne '3'}">
               		<input type="button" class="btn-save" value="<spring:message code="page.refresh"/>" onclick="authorizeRefresh();" id="btnfinish">
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
	
	
	function authorizeRefresh() {
	    var checkboxlength = $("input[name='ids']:checked").length;
	    if(checkboxlength == null || checkboxlength < 1){
	   		$.dialog.tips('<spring:message code="page.select.empty"/>', 1, 'alert.gif');
	   		return false;
	    }
        $.dialog.confirmMultiLanguage('<spring:message code="page.confirm.execution"/>?',
 	             '<spring:message code="page.confirm"/>',
 	             '<spring:message code="page.cancel"/>',
 	             function(){ 
					 $("#addform").attr("action", "user/saveAuthorizeRefresh");
			    	 $("#addform").submit();
		         }, 
                 function(){
		});
    }
	
	
	//全选按钮
	function checkAll() {
	    $(':checkbox').attr('checked', !!$('#checkall').attr('checked'));
	}
	
	//选择某一项，判断全选按钮是否选择
	function checkbox() {
	    var checked = true;
	    $('.checkbox').each(function () {
	      if (!$(this).attr('checked')) {
	        checked = false;
	      }
	    });
	    $('#checkall').attr('checked', checked);
	}
</script>
</body>
</html>
