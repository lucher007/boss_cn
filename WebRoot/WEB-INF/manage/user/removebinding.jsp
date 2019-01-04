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
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.business.manage"/> &gt; <spring:message code="menu.business.removebinding"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-tit"><spring:message code="menu.business.removebinding"/></div>
      <div class="fb-con">
    	<div id="userInfo"></div>
        <div class="oldStbInfo">
	    	<table>
	          <tr class="fbc-tit">
	            <td colspan="8" style="font-weight: bold;"><spring:message code="menu.business.removebinding"/></td>
	          </tr>
	          <tr>
	          	<td height="30" width="10%" align="right"><spring:message code="stb.stbno"/>：</td>
				<td>
					<select id="stbno" name="stbno" class="select" onchange="initUsercard();">
		                <c:forEach items="${user.user.userstblist}" var="userstb" varStatus="s">
		                  <c:if test="${userstb.incardflag == '0' || userstb.incardflag == '1'}">
		                  	<option value="${userstb.stbno}">${userstb.stbno}</option>
		                  </c:if>
		                </c:forEach>
		            </select>
				</td>
				<td height="30" width="10%" align="right"><spring:message code="userstb.cardid"/>：</td>
				<td>
					<input name="cardid" id="cardid" >
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
	      initUsercard();
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

  
  //查询该机顶盒已经绑定的智能卡
  function initUsercard() {
	  //获取页面的机顶盒信息
	  var stbno = $("#stbno").val();
	  
	  $('#cardid').combobox({    
			url:'user/getBindedUsercardJsonForRemovebinding?stbno='+stbno,
		    valueField:'id',    
		    //limitToList:true,
		    textField:'name',
		    editable:false,
		    async:false,
	        onSelect: function(rec){    
             //initArea(rec.id);//默认加载区域
      		},
	    	onLoadSuccess:function(node, data){
		    	//默认选择第一个
				var val = $(this).combobox("getData");  
                for (var item in val[0]) {  
                    if (item == "id") {  
                        $('#cardid').combobox("select", val[0][item]);  
                    }  
                 }  	
		    }
		}); 
	  
  }
  
  function saveResetBinding() {
	  
        var stbno = $("#stbno").val();
        if(stbno == null || stbno == ''){
	   		$.dialog.tips('<spring:message code="userproduct.stbno.empty"/>', 1, 'alert.gif');
	   		$("#stbno").focus();
	   		return false;
	    }
       
        var cardid = $("#cardid").combobox("getValue");
        if(cardid==null || cardid == ''){
        	$.dialog.tips("<spring:message code="usercard.userstb.nobind"/>", 2, 'alert.gif');
        	return false;
        }
        
        $.dialog.confirmMultiLanguage('<spring:message code="page.confirm.execution"/>?',
            '<spring:message code="page.confirm"/>',
            '<spring:message code="page.cancel"/>',
            function(){ 
				 $("#addform").attr("action", "user/saveRemoveBinding?stbno=" + stbno);
		    	 $("#addform").submit();
	        }, 
            function(){
		    });
    }
  
</script>
</body>
</html>
