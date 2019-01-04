<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
    .fb-con table tr {
      height: 30px;
    }
  </style>
</head>

<body>
<div id="body">
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.system.store"/> &gt; <spring:message code="store.storeupdate"/></div>
  <form action="" method="post" id="updateform" name="updateform">
    <input type="hidden" name="id" id="id" value="${store.store.id}"/>
    <input type="hidden" name="querynetid" id="querynetid" value="${store.querynetid }"/>
    <input type="hidden" name="querystate" id="querystate" value="${store.querystate }"/>
    <input type="hidden" name="pager_offset" id="pager_offset" value="${store.pager_offset }"/>
    <div class="form-box">
      <div class="fb-tit"><spring:message code="store.storeupdate"/></div>
      <div class="fb-con">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td height="30" width="15%" align="right"><spring:message code="network.netname"/>：</td>
            <td width="25%">
              <select id="netid" name="netid" class="select">
                <c:forEach items="${store.networkmap}" var="dataMap" varStatus="s">
                  <option value="${dataMap.key}" <c:if test="${store.store.netid == dataMap.key }">selected</c:if>>${dataMap.value}</option>
                </c:forEach>
              </select>
            </td>
            <td width="15%" align="right"><spring:message code="store.storecode"/>：</td>
            <td width="25%">
               <input type="text" class="inp" name="storecode" id="storecode" 
               maxlength="5" 
            	onkeyup="checkNum(this)" onkeypress="checkNum(this)" onblur="checkNum(this)"
               value="${store.store.storecode }"> <span
                class="red">*</span>
            </td>
          </tr>

          <tr>
            <td align="right"><spring:message code="store.storename"/>：</td>
            <td>
                <input type="text" class="inp" name="storename" id="storename" 
	              maxlength="30"
                value="${store.store.storename }">
            </td>
            <td align="right"><spring:message code="store.state"/>：</td>
            <td>
              <select name="state" id="state" class="select">
              	  <option value="1" <c:if test="${store.store.state == '1' }">selected</c:if>><spring:message code="store.state.1"/></option>
                  <option value="0" <c:if test="${store.store.state == '0' }">selected</c:if>><spring:message code="store.state.0"/></option>
              </select>
            </td>
           
          </tr>
          
          <tr>
            <td align="right"><spring:message code="store.address"/>：</td>
            <td>
                <input type="text" class="inp" style="width: 250PX;" name="address" id="address" 
                maxlength="50" 
                value="${store.store.address }">
            </td>
            <td align="right"></td>
            <td>
            </td>
          </tr>
        </table>
      </div>
      <div class="fb-bom">
        <cite>
            <input type="button" class="btn-back" value="<spring:message code="page.goback"/>" onclick="goBack()" >
            <input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="updateStore();" id="btnfinish">
        </cite>
        <span class="red">${store.returninfo }</span>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript" src="js/common/jquery.js"></script>
<script type="text/javascript" src="js/form.js"></script>
<script type="text/javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
<script type="text/javascript">

  function updateStore() {
      if ($('#storecode').val() == '') {
          $.dialog.tips('<spring:message code="store.storecode.empty"/>', 1, 'alert.gif');
          $('#storecode').focus();
          return;
      }

      $('#updateform').attr('action', 'store/update');
      $("#updateform").submit();
  }
  
  function goBack() {
      $("#updateform").attr("action", "store/findByList");
      $("#updateform").submit();
  }
  
  
  $(function () {
      var returninfo = '${store.returninfo}';
      if (returninfo != '') {
          $.dialog.tips(returninfo, 1, 'alert.gif');
      }
  });
</script>
</body>
</html>
