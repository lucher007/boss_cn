<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- 发票打印表格 -->
<div class="printInfo">
    	<table border=0 cellSpacing=0 cellPadding=0 width="100%" style="border-collapse:collapse;bordercolor:#333333;font-size:9pt;">
        <c:forEach items="${userbusinessdetail.userbusinessdetaillist }" var="dataList">
		        <c:if test="${dataList.totalmoney ne '0.00' }">
		        	<tr height="10"class="lb-list">
		        		<td width="20%" align="left">${dataList.feename }</td>
		        		<td width="10%" align="left">${dataList.totalmoney }</td>
		        		<td width="70%" align="left">
			        		<c:choose> 
			        			 <c:when test="${dataList.businesstypekey=='buyproduct'}" >   
			        				${dataList.cardid }&nbsp;&nbsp;&nbsp;&nbsp;${dataList.content }
							    </c:when> 
							    <c:otherwise>
							    	${dataList.remark }
							    </c:otherwise>   
			        		</c:choose>
		        		</td>
		        		
		        	</tr>
		        </c:if>
	         </c:forEach>
        </table>
</div>

<!-- 发票模板 -->
<div class="invoiceTmpInfo">
		<spring:message code="print.template"/>:
		<select id="template_value" name="template_value" class="select">
			<c:forEach items="${printtemplate.templateMap}" var="templateMap" varStatus="s">
				<option value='${templateMap.value}'>${templateMap.key}</option>
			</c:forEach>
		</select> 
</div>



