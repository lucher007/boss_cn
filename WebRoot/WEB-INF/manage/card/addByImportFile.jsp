<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
<div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.device.card"/> &gt; <spring:message code="stb.fileimport"/></div>		
<form method="post" id="searchform" name="searchform" enctype="multipart/form-data">
			<div class="form-box">
				<div class="fb-tit"><spring:message code="stb.fileimport"/></div>
				<div class="fb-con">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
				            <td height="30" width="15%" align="right"><spring:message code="provider.model"/>：</td>
				            <td width="25%">
				             <select id="providerid" name="providerid" class="select" style="width: 250px;">
				                <c:forEach items="${card.providerlist}" var="dataMap" varStatus="s">
				                  <option value="${dataMap.id}" <c:if test="${dataMap.id == card.providerid}">selected</c:if>>${dataMap.model}</option>
				                </c:forEach>
				              </select>
				            </td>
				          </tr>
				          <tr>
				          	<td align="right" width="15%"><spring:message code="network.netname"/>：</td>
							<td align="left">
								<select id="netid" name="netid" class="select" style="width: 250px;">
										<c:forEach items="${card.networklist}" var="networkMap" varStatus="s">
											<option value="${networkMap.id}" <c:if test="${networkMap.id == card.netid }">selected</c:if>>${networkMap.netname}</option>
										</c:forEach>
								</select>
							</td>	
				          </tr>
				          <tr>
				          	 <td align="right" width="15%"><spring:message code="server.versiontype"/>：</td>
				             <td width="25%">
				             	<select name="versiontype" id="versiontype" class="select" style="width: 250px;">
					                <!--
					                	<option value="gos_gn" <c:if test="${card.versiontype == 'gos_gn' }">selected</c:if>><spring:message code="server.versiontype.gos_gn"/></option>
					                  -->
					                <option value="gos_pn" <c:if test="${card.versiontype == 'gos_pn' }">selected</c:if>><spring:message code="server.versiontype.gos_pn"/></option>
					            </select>
				             </td>
				          </tr>
				          <tr>
				            <td align="right"><spring:message code="card.incardflag"/>：</td>
				            <td>
				              <select name="incardflag" id="incardflag" class="select" style="width: 250px;">
				              	  <option value="0" <c:if test="${card.incardflag == '0' }">selected</c:if>><spring:message code="card.incardflag.0"/></option>
				              	  <option value="1" <c:if test="${card.incardflag == '1' }">selected</c:if>><spring:message code="card.incardflag.1"/></option>
				              </select>
				            </td>
				        </tr>
						<tr>
							<td width="15%" align="right"><spring:message code="uploadify.filepath"/>：</td>
							<td height="30" width="45%">
								<input type="text" id="txt" name="txt" readonly="readonly" style="width: 230px" class="inp"/> 
								<input type="button" style="width:70px;height:23px;" name="selectbutton" id="selectbutton" value="<spring:message code="page.select" />"  onclick="uploadfile.click()"/> 
								<input type="file" name="uploadfile" id="uploadfile" onchange="txt.value=this.value" style="position:absolute;width:100px;height:23px;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;margin-left:-103px;"/>
							</td>
						</tr>
					</table>
				</div>
				<div class="fb-bom">
					<cite> <span><a href="card/downloadTemplate" class="btn-print"><spring:message code="uploadify.download.template"/></a> </span> <span><input type="button" class="btn-back"
							value="<spring:message code="page.goback"/>" onclick="goBack()"
						/> </span> <span><input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="saveCard();" /> </span> </cite> <span class="red">${card.returninfo}</span>
				</div>
			</div>
		</form>
	</div>
	<script type="text/javascript" src="js/common/jquery.js"></script>
	<script type="text/javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
	<script type="text/javascript" src="js/form.js"></script>
	<script type="text/javascript">
		function saveCard() {
			if ($('#providerid').val()== null || $('#providerid').val() == '') {
			      $.dialog.tips('<spring:message code="card.providerid.empty"/>', 1, 'alert.gif');
			      $('#providerid').focus();
			      return;
		    } 
		    
		    if ($('#netid').val() == null || $('#netid').val() == '') {
		      $.dialog.tips('<spring:message code="card.netid.empty"/>', 1, 'alert.gif');
		      $('#netid').focus();
		      return;
		    }
			
		    $.dialog({
			    title: '<spring:message code="stb.fileimport"/>',
			    lock: true,
			    background: '#000', /* 背景色 */
			    opacity: 0.5,       /* 透明度 */
			    content: '<spring:message code="page.confirm.execution"/>',
			    icon: 'alert.gif',
			    ok: function () {
			    	$("#searchform").attr("action", "card/saveByImportFile"+"?rid="+Math.random());
 					$("#searchform").submit();
			        /* 这里要注意多层锁屏一定要加parent参数 */
			        $.dialog({
			        	lock: true,
			            title: '<spring:message code="page.notice"/>',
			        	content: '<spring:message code="page.wait"/>......', 
			        	max: false,
					    min: false,
					    cancel:false,
			        	icon: 'loading.gif',
			        });
			        return false;
			    },
			    okVal: '<spring:message code="page.confirm"/>',
			    cancel: true,
			    cancelVal:'<spring:message code="page.cancel"/>'
			});

		}

		function goBack() {
			$("#searchform").attr("action", "card/addInit").submit();
		}

		$(function() {
			var returninfo = '${card.returninfo}';
			if (returninfo != '') {
				$.dialog.tips(returninfo, 1, 'alert.gif');
			}
		});

		function initNetwork() {
			
			$.getJSON('network/initNetworkJson?rid='+Math.random(), null, function(networkJson) {
				var options = '';
				for ( var key in networkJson) {
					options += '<option value="' + networkJson[key].id + '">'
							+ networkJson[key].netname + '</option>';
				}
				$('#netid').children().remove();
				$('#netid').append(options);
				$('#netid').val('$card.netid}');
				initServer();
			});
		}
	</script>
</body>
</html>
