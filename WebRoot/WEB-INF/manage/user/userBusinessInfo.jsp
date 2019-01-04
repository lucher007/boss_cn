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
    .service table tr td {
      height: 30px;
      width: 12.5%;
    }
  </style>
</head>

<body>
<div id="body">
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.business.manage"/> &gt; <spring:message code="user.businessinfoquery"/></div>
  <form action="" method="post" id="updateform" name="updateform">
    <input type="hidden" name="id" id="id" value="${user.user.id}"/>
    <input type="hidden" name="querynetid" id="querynetid" value="${user.querynetid }"/>
    <input type="hidden" name="queryareacode" id="queryareacode" value="${user.queryareacode }"/>
    <input type="hidden" name="queryusercode" id="queryusercode" value="${user.queryusercode }"/>
    <input type="hidden" name="queryusername" id="queryusername" value="${user.queryusername }"/>
    <input type="hidden" name="querydocumentno" id="querydocumentno" value="${user.querydocumentno }"/>
    <input type="hidden" name="querystbno" id="querystbno" value="${user.querystbno }"/>
    <input type="hidden" name="querycardid" id="querycardid" value="${user.querycardid }"/>
    <input type="hidden" name="querytelephone" id="querytelephone" value="${user.querytelephone }"/>
    <input type="hidden" name="querystate" id="querystate" value="${user.querystate }"/>
    <input type="hidden" name="queryaddress" id="queryaddress" value="${user.queryaddress }"/>
    <input type="hidden" name="pager_offset" id="pager_offset" value="${user.pager_offset }"/>
    <div class="form-box">
<!--       <div class="fb-tit"><spring:message code="user.businessinfoquery"/></div> -->
      <div class="fb-con">
    	<table>
          <tr class="fbc-tit">
            <td colspan="7" style="font-weight: bold;"><spring:message code="user.userinfo"/></td>
            <td>
		         <input type="button" class="btn-back" value="<spring:message code="page.goback"/>" onclick="goBack()" >
            </td>
          </tr>
          <tr>
            <td width="10%" align="right" class="line-r"><spring:message code="network.netname"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r" title="${user.user.network.netname}">${fn:substring(user.user.network.netname, 0, 20)}</td>
            <td width="10%" align="right" class="line-r"><spring:message code="area.areacode"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r">${user.user.area.areaname}(${user.user.area.areacode})</td>
            <td width="10%" align="right" class="line-r"><spring:message code="user.usercode"/>：</td>
            <td width="15%" style="font-weight: bold;">${user.user.usercode}</td>
            <td width="10%" align="right" class="line-r"><spring:message code="user.username"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r" title="${user.user.username}">${fn:substring(user.user.username, 0, 20)}</td>
          </tr>
          <tr>
            <%-- <td width="10%" align="right" class="line-r"><spring:message code="user.mobile"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r">${user.user.mobile}</td> --%>
            <td width="10%" align="right" class="line-r">联系电话：</td>
            <td width="15%" style="font-weight: bold;" class="line-r">${user.user.telephone}</td>
            <td width="10%" align="right" class="line-r"><spring:message code="user.documenttype"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r" title="${user.user.documenttype}">${fn:substring(user.user.documenttype, 0, 20)}</td>
            <td width="10%" align="right" class="line-r"><spring:message code="user.documentno"/>：</td>
            <td width="15%" style="font-weight: bold;" title="${user.user.documentno}">${fn:substring(user.user.documentno, 0, 20)}</td>
          	<td width="10%" align="right" class="line-r"><spring:message code="订户级别"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r" title="${user.user.userlevel.levelname}">${fn:substring(user.user.userlevel.levelname, 0, 70)}</td>
          </tr>
          <tr>
            <td width="10%" align="right" class="line-r"><spring:message code="user.email"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r" title="${user.user.email}">${fn:substring(user.user.email, 0, 20)}</td>
            <td width="10%" align="right" class="line-r"><spring:message code="订户类型"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r">
            	<c:if test="${user.user.usertype=='0'}">个人订户</c:if>
                <c:if test="${user.user.usertype=='1'}">集团订户</c:if>
            </td>
            <td width="10%" align="right" class="line-r"><spring:message code="user.account"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r">${user.user.account}</td>
            <td width="10%" align="right" class="line-r"><spring:message code="user.state"/>：</td>
            <td width="15%" style="font-weight: bold;">
				  <c:choose>  
		               <c:when test="${user.user.state == '0' || user.user.state == '3'}">
		               		<span class="red"><spring:message code="user.state.${user.user.state}"/></span>
		               </c:when>
		               <c:otherwise>
		               		<spring:message code="user.state.${user.user.state}"/>
		               </c:otherwise>
	               </c:choose>
            </td>
          </tr>
          <tr>
            <td width="10%" align="right" class="line-r"><spring:message code="user.address"/>：</td>
            <td width="15%" style="font-weight: bold;" class="line-r" colspan="3" title="${user.user.address}">${fn:substring(user.user.address, 0, 70)}</td>
          	<td width="10%" align="right" class="line-r"><spring:message code="systempara.remark"/>：</td>
            <td width="15%" style="font-weight: bold;color: red;" class="line-r" title="${user.user.remark}">${fn:substring(user.user.remark, 0, 70)}</td>
          </tr>
        </table>
        <!--
    	<table>
          <tr class="fbc-tit">
            <td colspan="8" style="font-weight: bold;"><spring:message code="user.stbinfo"/></td>
          </tr>
          <tr class="lb-tit">
          		<td><spring:message code="stb.stbno"/></td>
	          	<td><spring:message code="user.buytime"/></td>
	          	<td><spring:message code="userstb.state"/></td>
	          	<td><spring:message code="userstb.incardflag"/></td>
	          	<td><spring:message code="userstb.mothercardflag"/></td>
	          	<td><spring:message code="userstb.mothercardid"/></td>
         </tr>
         <c:forEach items="${user.user.userstblist }" var="dataList">
	        	<tr height="30"class="lb-list">
	        		<td >${dataList.stbno }</td>
	        		<td >${fn:substring(dataList.addtime, 0, 19)}</td>
	        		<td ><spring:message code="userstb.state.${dataList.state }"/></td>
	        		<td ><spring:message code="userstb.incardflag.${dataList.incardflag }"/></td>
	        		<td >
		        		<c:if test="${dataList.incardflag=='2'}">
		        			<spring:message code="userstb.mothercardflag.${dataList.mothercardflag }"/>
		        		</c:if>
	        		</td>
	        		<td >${dataList.mothercardid }</td>
	        	</tr>
        	</c:forEach>
        </table>
          -->
    	<table>
          <tr class="fbc-tit">
            <td colspan="9" style="font-weight: bold;"><spring:message code="user.cardinfo"/></td>
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
	        		    <a class="btn-edit" href="javascript:updateSignflagInit('${dataList.cardid}','${dataList.cardflag}');"><spring:message code="page.update"/></a>
	        		</td>
	        	</tr>
        	</c:forEach>
        </table>
        <div>
        	<iframe id="main" scrolling="auto" frameborder="0" style="width:100%;height:230px;" src="userproduct/findByList?userid=${user.user.id }"></iframe>
        </div>
        <!-- 改变智能卡标识弹出框 -->
        <div class="pop-box" id="sign-div">
			<table width="400" border="0" cellpadding="0" cellspacing="0">
	          <tr>
	            <td height="30" width="30%" align="right">智能卡标识：</td>
	            <td width="60%">
	            	<select id="signcardflag" name="signcardflag" class="select">
		                <option value="1" ><spring:message code="usercard.remarkcardflag.1"/></option>
		                <option value="0" ><spring:message code="usercard.remarkcardflag.0"/></option>
		            </select>
	            </td>
	          </tr>
	          
			</table>
		</div>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript" src="js/common/jquery.js"></script>
<script type="text/javascript" src="js/form.js"></script>
<script type="text/javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
<script type="text/javascript">

 //判断是否为数字
  function checkNumberChar(ob) {
    if (/^\d+$/.test(ob)) {
      return true;
    } else {
      return false;
    }
  }

  function goBack() {
      $("#updateform").attr("action", "user/businessUnit?businesstype=queryUser");
      $("#updateform").submit();
  }
  
  
 $(function () {
       var returninfo = '${user.returninfo}';
       if (returninfo != '') {
        	$.dialog.tips(returninfo, 1, 'alert.gif');
       }
  });
  
  //高清，标清修改
	var updateSigncardflagDialog;
	function updateSignflagInit(cardid, cardflag) {
	     updateSigncardflagDialog = $.dialog({
		    title: '智能卡标识',
		    lock: true,
		    width: 400,
		    height:100,
		    top: 200,
		    drag: false,
		    resize: false,
		    max: false,
		    min: false,
		    content: $("#sign-div").html(),
		   	ok: function(){
		   		   updateSigncardflag(cardid, cardflag);
		   		},
		   	okVal:'<spring:message code="page.confirm"/>',
		   	cancel:function(){/* updateMothercardflagDialog.close(); */},
		   	cancelVal:'<spring:message code="page.cancel"/>'
	   });
	 }
	 
	 function updateSigncardflag(cardid, cardflag){
	     var signcardflag = $("#signcardflag").val();
	     if(cardflag == signcardflag){//选择同样的卡标志类型，不做处理
	     	return true;
	     }
	     $("#updateform").attr("action", "user/updatesigncard?" + "cardid=" + cardid + "&cardflag=" + signcardflag);
		 $("#updateform").submit();
    }
  
</script>
</body>
</html>
