<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<base href="<%=basePath%>" />
<meta charset="utf-8">
<title></title>
<link type="text/css" rel="stylesheet" href="style/user.css">
<link type="text/css" rel="stylesheet" href="js/plugin/poshytip/tip-yellowsimple/tip-yellowsimple.css">
<link type="text/css" rel="stylesheet" href="js/plugin/treeTable/css/jquery.treetable.css">
<style>
	html { overflow-x:hidden; }
    .fb-con table { 
    	width: 100%; border: 0; border-spacing: 0; border-collapse: collapse; 
    }
    .fb-con table tr {
      height: 30px;
    }
    .service table tr td {
      height: 30px;
      width: 12.5%;
    }
   
  </style>
</head>

<body style="padding:0px; width:100%; ">
	<div class="form-box">
	    <div class="fb-con">
	      	<table>
	      	  <tr class="fbc-tit">
	            <td colspan="10" style="font-weight: bold;"><spring:message code="menu.business.businessquery"/></td>
	          </tr>
	          <tr class="lb-tit">
	          		<td><spring:message code="user.accountlog.type"/></td>
	          		<td><spring:message code="user.accountlog.money"/></td>
	          		<td><spring:message code="user.accountlog.addtime"/></td>
	          		<td><spring:message code="userbusiness.source"/></td>
	          		<td><spring:message code="business.type"/></td>
	          		<td><spring:message code="operator.operatorcode"/></td>
	         </tr>
	         <c:forEach items="${useraccountlog.useraccountloglist }" var="dataList">
		        	<tr height="30"class="lb-list">
    					<td ><spring:message code="user.accountlog.type.${dataList.type }"/></td>
		        		<td >${dataList.money }</td>
		        		<td >${fn:substring(dataList.addtime, 0, 19)}</td>
		        		<td ><spring:message code="userbusiness.source.${dataList.source }"/></td>
		        		<td ><spring:message code="business.type.${dataList.businesstypekey }"/></td>
		        		<td >${dataList.operator.operatorcode }</td>
		        	</tr>
	        	</c:forEach>
	        </table>
		</div>
	    <div class="page">
	    	<cite>
	        	<pg:pager
				    url="useraccountlog/findUseraccountlogByList"
				    items="${useraccountlog.pager_count}"
				    index="center"
				    maxPageItems="${useraccountlog.pager_openset}"
				    maxIndexPages="5"
				    isOffset="<%= true %>"
				    export="offset,currentPageNumber=pageNumber"
				    scope="request">	
					<pg:param name="index"/>
					<pg:param name="maxPageItems"/>
					<pg:param name="maxIndexPages"/>
					<pg:param name="userid" value="${useraccountlog.userid}"/>
					<pg:index>
						<spring:message code="page.total"/>:${useraccountlog.pager_count}
						<pg:first unless="current">
							<a href="<%=pageUrl %>"><spring:message code="pape.firstpage"/></a>
						</pg:first>
						<pg:prev export="prevPageUrl=pageUrl">
					  		<a href="<%= prevPageUrl %>"><spring:message code="page.prevpage"/></a>
						</pg:prev>
						<pg:pages>
		   					<%if (pageNumber == currentPageNumber) { 
					    	%><span style="font:bold 16px arial;"><%= pageNumber %></span><%
					  		} else {
					    	%><a href="<%= pageUrl %>"><%= pageNumber %></a><%
					   		}
							%>  
						</pg:pages>
						<pg:next export="nextPageUrl=pageUrl">
					  		<a href="<%= nextPageUrl %>"><spring:message code="page.nextpage"/></a>
						</pg:next>
						<pg:last>
							<a href="<%=pageUrl %>"><spring:message code="page.lastpage"/></a>
						</pg:last>
					</pg:index>
				</pg:pager>
	    	</cite>
	    </div>
  </div>
<script type="text/javascript" language="javascript" src="js/common/jquery.js"></script>
<script type="text/javascript" language="javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
<script type="text/javascript">
	
</script>
</body>
</html>

