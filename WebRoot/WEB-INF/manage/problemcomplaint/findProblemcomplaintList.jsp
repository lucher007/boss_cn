<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<base href="<%=basePath%>" />
<meta charset="utf-8">
<title></title>
<link type="text/css" rel="stylesheet" href="style/user.css">
<link type="text/css" rel="stylesheet" href="style/easyui/easyui.css">
<link type="text/css" rel="stylesheet" href="js/plugin/poshytip/tip-yellowsimple/tip-yellowsimple.css">
<link rel="stylesheet" type="text/css" href="main/plugin/easyui/themes/icon.css">
</head>
<body>
	<div id="body">
		<form action="" method="post" id="searchForm" name="searchForm">
		<div class="cur-pos">
			<spring:message code="page.currentlocation" />
			：
			<spring:message code="menu.dispatch.manage" />
			&gt;
			<spring:message code="problemcomplaint.problemcomplaintquery" />
		</div>
		<div class="seh-box">
				<table width="100%">
					<tr>
						<td align="right" width="10%"><spring:message code="network.netname"/>：</td>
						<td width="20%">
							<!-- <select id="querynetid" name="querynetid" class="select" onchange="initArea();">
					 	 	</select> -->
					 	 	<input id="querynetid" name="querynetid" style="width: 157px;">
		             	</td>
		                <td align="right" width="10%"><spring:message code="area.areaname"/>：</td>
						<td>
							<!-- 
							<input class="easyui-combotree inp w200" data-options="" id="queryareacode" name="queryareacode">
							 -->
							<input id="queryareacode" name="queryareacode" style="width: 157px;">
						</td>
						<td align="right" width="120px"><spring:message code="problemcomplaint.type" />：</td>
						<td><select id="querytype" name="querytype" class="select">
								<option value="">
									<spring:message code="page.select" />
								</option>
								<option value="0" <c:if test="${problemcomplaint.querytype == '0'}">selected</c:if>>
									<spring:message code="problemcomplaint.type.0" />
								</option>
								<option value="1" <c:if test="${problemcomplaint.querytype == '1'}">selected</c:if>>
									<spring:message code="problemcomplaint.type.1" />
								</option>
						</select>
						</td>
						<td align="center">
							<%-- 
							<input type="button" class="btn-sch" value="<spring:message code="page.query"/>" onclick="queryProblemcomplaint();" />
							 --%>
							<a href="javascript:queryProblemcomplaint();" iconCls="icon-search" class="easyui-linkbutton" ><spring:message code="page.query"/></a>
						</td>
					</tr>
					<tr>
						<td align="right"><spring:message code="user.username" />：</td>
						<td>
							<input type="text" class="inp w200" name="queryusername" id="queryusername" value="${problemcomplaint.queryusername}">
						</td>
						<td align="right" width="120px"><spring:message code="problemcomplaint.problemtype" />：</td>
						<td><select id="queryproblemtype" name="queryproblemtype" class="select">
								<option value=""><spring:message code="page.select" /></option>
								<option value="1" <c:if test="${problemcomplaint.queryproblemtype == '1'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.1" />
								</option>
								<option value="2" <c:if test="${problemcomplaint.queryproblemtype == '2'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.2" />
								</option>
								<option value="3" <c:if test="${problemcomplaint.queryproblemtype == '3'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.3" />
								</option>
								<option value="4" <c:if test="${problemcomplaint.queryproblemtype == '4'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.4" />
								</option>
								<option value="0" <c:if test="${problemcomplaint.queryproblemtype == '0'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.0" />
								</option>
						</select>
						<td align="right" width="120px"><spring:message code="problemcomplaint.state" />：</td>
						<td><select id="querystate" name="querystate" class="select">
								<option value=""><spring:message code="page.select" /></option>
								<option value="0" <c:if test="${problemcomplaint.querystate == '0'}">selected</c:if>>
									<spring:message code="problemcomplaint.state.0" />
								</option>
								<option value="1" <c:if test="${problemcomplaint.querystate == '1'}">selected</c:if>>
									<spring:message code="problemcomplaint.state.1" />
								</option>
								<option value="3" <c:if test="${problemcomplaint.querystate == '3'}">selected</c:if>>
									<spring:message code="problemcomplaint.state.3" />
								</option>
								<option value="4" <c:if test="${problemcomplaint.querystate == '4'}">selected</c:if>>
									<spring:message code="problemcomplaint.state.4" />
								</option>
						</select>
						</td>
						<td align="center">
							<%-- 
							<input type="button" class="btn-add" value="<spring:message code="page.add"/>" onclick="addProblemcomplaint();" />
							 --%>
							<a href="javascript:addProblemcomplaint();" iconCls="icon-add" class="easyui-linkbutton" ><spring:message code="page.add"/></a>
						</td>
					</tr>
				</table>
		</div>
		<%-- <div class="lst-box">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr class="lb-tit">
					<!-- 
					<td>
						<spring:message code="network.netname" />
					</td>
					 -->
					<td>
						<spring:message code="problemcomplaint.complaintcode" />
					</td>
					<td><spring:message code="user.username" />
					</td>
					<td>联系方式
					</td>
					<td><spring:message code="problemcomplaint.type" />
					</td>
					<td><spring:message code="problemcomplaint.problemtype" />
					</td>
					<td><spring:message code="problemcomplaint.content" />
					</td>
					<td><spring:message code="problemcomplaint.state" />
					</td>
					<td><spring:message code="problemcomplaint.addtime" />
					</td>
					<td><spring:message code="problemcomplaint.attachment" /></td>
					<td><spring:message code="page.operate" />
					</td>
				</tr>
				<c:forEach items="${problemcomplaint.problemcomplaintlist }" var="dataList">
					<tr height="30" class="lb-list">
						<!-- <td>${dataList.network.netname }</td> -->
						<td>${dataList.complaintcode}</td>
						<td>${dataList.user.username }</td>
						<td>${dataList.telephone }</td>
						<td><spring:message code="problemcomplaint.type.${dataList.type}" />
						</td>
						<td><spring:message code="problemcomplaint.problemtype.${dataList.problemtype}" />
						</td>
						<td class="remarkClass" title="${dataList.content }">${fn:substring(dataList.content, 0, 9)}&nbsp;</td>
						<td><spring:message code="problemcomplaint.state.${dataList.state }" />
						</td>
						<td>${fn:substring(dataList.addtime, 0, 19)}</td>
						<td>
							<a class="btn-look" href="javascript:queryAttachment(${dataList.id });" ><spring:message code="page.look"/></a>
						</td>
						<td>
							<a class="btn-edit" href="javascript:updateProblemcomplaint('${dataList.id}');"><spring:message code="page.update" /></a> 
							<a class="btn-del" href="javascript:deleteProblemcomplaint(${dataList.id });"><spring:message code="page.delete" /></a>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div class="page">
			<cite> <pg:pager url="problemcomplaint/findByList" items="${problemcomplaint.pager_count}" index="center" maxPageItems="10"
					maxIndexPages="5" isOffset="<%= true %>" export="offset,currentPageNumber=pageNumber" scope="request"
				>
					<pg:param name="index" />
					<pg:param name="maxPageItems" />
					<pg:param name="maxIndexPages" />
					<pg:param name="querynetid" value="${problemcomplaint.querynetid}" />
					<pg:param name="queryareacode" value="${problemcomplaint.queryareacode}" />
					<pg:param name="querytype" value="${problemcomplaint.querytype}" />
					<pg:param name="queryusername" value="${problemcomplaint.queryusername}" />
					<pg:param name="querystate" value="${problemcomplaint.querystate}" />
					<pg:param name="queryproblemtype" value="${problemcomplaint.queryproblemtype}" />
					<pg:index>
				<spring:message code="page.total"/>:${problemcomplaint.pager_count}
					<pg:first unless="current">
							<a href="<%=pageUrl%>"><spring:message code="pape.firstpage"/></a>
						</pg:first>
						<pg:prev export="prevPageUrl=pageUrl">
							<a href="<%=prevPageUrl%>"><spring:message code="page.prevpage"/></a>
						</pg:prev>
						<pg:pages>
							<%
								if (pageNumber == currentPageNumber) {
							%><span style="font:bold 16px arial;"><%=pageNumber%></span>
							<%
								} else {
							%><a href="<%=pageUrl%>"><%=pageNumber%></a>
							<%
								}
							%>
						</pg:pages>
						<pg:next export="nextPageUrl=pageUrl">
							<a href="<%=nextPageUrl%>"><spring:message code="page.nextpage"/></a>
						</pg:next>
						<pg:last>
							<a href="<%=pageUrl%>"><spring:message code="page.lastpage"/></a>
						</pg:last>
					</pg:index>
				</pg:pager> </cite>
		</div> --%>
		
		</form>
		<div id="problemcomplaintlist" style="width:auto"></div>
	</div>
	<script type="text/javascript"  src="js/common/jquery.js"></script>
	<script type="text/javascript"  src="js/My97DatePicker/WdatePicker.js" defer="defer"></script>
	<script type="text/javascript"  src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
	<script type="text/javascript" src="js/plugin/poshytip/jquery.poshytip.min.js"></script>
	<script type="text/javascript" src="js/form.js"></script>
	<script type="text/javascript" language="javascript" src="js/common/jquery.easyui.min.js"></script>
	<script type="text/javascript">
		//查询操作员
		function queryProblemcomplaint() {
			/* $("#searchForm").attr("action", "problemcomplaint/findByList");
			$("#searchForm").submit(); */
			$('#problemcomplaintlist').datagrid('reload',paramsJson());
		}

		//添加
		function addProblemcomplaint() {
			$("#searchForm").attr("action", "problemcomplaint/addInit");
			$("#searchForm").submit();
		}

		/**
		 *编辑
		 */
		function updateProblemcomplaint(id) {
			$("#searchForm").attr( "action", "problemcomplaint/updateInit?id=" + id + "&pager_offset="+'${problemcomplaint.pager_offset}');
			$("#searchForm").submit();
		}

		/**
		 *删除
		 */
		function deleteProblemcomplaint(id) {
			$.dialog.confirmMultiLanguage(
					'<spring:message code="page.confirm.execution"/>?',
					'<spring:message code="page.confirm"/>',
					'<spring:message code="page.cancel"/>', function() {
						$("#searchForm").attr(
								"action",
								"problemcomplaint/delete?id=" + id
										+ "&pager_offset="
										+ '${problemcomplaint.pager_offset}'
										+ "&rid=" + Math.random());
						$("#searchForm").submit();
					}, function() {
					});

		}

		$(function() {
		    initNetwork();
			var returninfo = '${problemcomplaint.returninfo}';
			if (returninfo != '') {
				$.dialog.tips(returninfo, 1, 'alert.gif');
			}
		});

		//全选
		function checkAll() {
			$(':checkbox').attr('checked', !!$('#checkall').attr('checked'));
		}

		function checkbox() {
			var checked = true;
			$('.checkbox').each(function() {
				if (!$(this).attr('checked')) {
					checked = false;
				}
			});
			$('#checkall').attr('checked', checked);
		}
		
	function initNetwork(){
		/* 
		var data = {
			    t: new Date().getTime()
		     };
		
    	$.getJSON('network/initNetworkJson', data, function (networkJson) {
		     var options = '<option value=""><spring:message code="page.select"/></option>';
		     for (var key in networkJson) {
		        options += '<option value="' + networkJson[key].id + '">' + networkJson[key].netname + '</option>';
		     }
		     $('#querynetid').children().remove();
		     $('#querynetid').append(options);
		     $('#querynetid').val('${problemcomplaint.querynetid}');
		     initArea();
	   }); */
	   
	   $('#querynetid').combobox({    
			url:'user/initNetworkJson?rid='+Math.random(),
		    valueField:'id',    
		    //limitToList:true,
		    editable:false,
		    textField:'netname',
	        onSelect: function(rec){ 
	           	initArea(rec.id);
        	},
		    onLoadSuccess:function(data){
			    	//初始化营业厅列表框的默认选择值
			    	if('${user.netid}' != '' && '${user.netid}' != null){
					  	  $(this).combobox('select',parseInt('${Statreport.netid}'));
					}else{//默认选择第一个
						var val = $('#querynetid').combobox("getData");
	                    for (var item in val[0]) {  
	                        if (item == "id") {  
	                            $(this).combobox("select", val[0][item]);  
	                        }  
	                     }  	
					} 
			    }
		});  
    }
    
  var flag = true;
  function initArea(netid) {
	  /* $('#queryareacode').combotree({   
		    url:'area/getAreaTreeJson?querynetid='+$('#querynetid').val()+'&rid='+Math.random(),
		    //数据执行之后才加载
		    onLoadSuccess:function(node, data){
		    	initAreaValue();
		    },
		    //选择之前运行
		    onBeforeSelect : function(node) {
		         //返回树对象  
		        var tree = $(this).tree;  
		        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中  
		        var isLeaf = tree('isLeaf', node.target);  
		        if (!isLeaf) {  
		            //清除选中  
		           //$("#queryareacode").treegrid("unselect");
		        }  
			    //if (node.children != undefined) {
			    // 	$("#areacode").treegrid("unselect");
			    //}
			},
		onChange:function(){
			//queryProblemcomplaint();		
		}
		    
	  });  */
	  
	   $('#queryareacode').combotree({   
			    url:'user/getAreaTreeJson?querynetid='+netid+'&rid='+Math.random(),
			    //数据执行之后才加载
			    onLoadSuccess:function(node, data){
			    	if('${Operator.areacode}' != '' && '${Operator.areacode}' != null){
					  	  var node = $('#queryareacode').combotree('tree').tree('find', '${Operator.areacode}');
					  	  if(node != null){
					  	  	$('#queryareacode').combotree('setValue', node.id);
					  	  }else{
					  	  	$('#queryareacode').combotree('setValue', '');
					  	  }
				     }else{
				     	$('#queryareacode').combotree('setValue', '');
				     }
				     if(flag){
				     	initProblemcomplaintlist();
				     	flag = false;
				     }
			    },
			    //选择之前运行
			    onBeforeSelect : function(node) {
			         //返回树对象  
			        var tree = $(this).tree;  
			        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中  
			        var isLeaf = tree('isLeaf', node.target);  
			        if (!isLeaf) {  
			            //清除选中  
			           //$("#queryareacode").treegrid("unselect");
			        }  
				    //if (node.children != undefined) {
				    // 	$("#areacode").treegrid("unselect");
				    //}
				},
			    //绑定onchanger事件
			    onChange:function(){  
	                //queryUser();
	            }
		  }); 
  }
		
		
  //初始化区域列表框的默认选择值
  function initAreaValue(){
	  if('${problemcomplaint.queryareacode}' != '' && '${problemcomplaint.queryareacode}' != null){
	  	  var node = $('#queryareacode').combotree('tree').tree('find', '${problemcomplaint.queryareacode}');
	  	  if(node != null){
	  	  	$('#queryareacode').combotree('setValue', node.id);
	  	  }
	  }
  }
  
  function queryAttachment(id){
 		$("#searchForm").attr("action", "problemcomplaint/queryDetail?id="+id+"&pager_offset="+'${problemcomplaint.pager_offset}');
 		$("#searchForm").submit();
  }
  
  function initProblemcomplaintlist(){
	    $('#problemcomplaintlist').datagrid({
	         title: '<spring:message code="problemcomplaint.problemcomplaintquery" />',
	         url:'problemcomplaint/problemcomplaintJson',
	         queryParams: paramsJson(),
	         singleSelect: true,
	         scrollbar:true,
	         pagination: true,
	         fitColumns:true,
	         rownumbers:true,
	         showFooter: false,
	         idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
	         loadMsg:'loading...',
	         columns: [[
	             { field: 'complaintcode', title: '<spring:message code="problemcomplaint.complaintcode" />',align:"center",width:100},
	             { field: 'username', title: '<spring:message code="user.username" />',align:"center",width:100},
	             { field: 'telephone', title: '<spring:message code="联系方式" />',align:"center",width:100},
	             { field: 'address', title: '<spring:message code="user.address" />',align:"center",width:100,
	             	formatter: function (val) {
	                    if(val != null){
	                    	return "<span title='" + val + "'>" + val + "</span>";
	                    }else{
	                    	return val;
	                    }
	                    
	                }
	             },
	        	 { field: 'type', title: '<spring:message code="problemcomplaint.type" />',align:"center",width:40,
	        	 	formatter:function(val, row, index){ 
	        	 		if(val == 0){
	        	 			return '<spring:message code="problemcomplaint.type.0" />';
	        	 		}else if(val == 1){
	        	 			return '<spring:message code="problemcomplaint.type.1" />';
	        	 		}
       	        	},
	        	 },
	        	 { field: 'problemtype', title: '<spring:message code="problemcomplaint.problemtype" />',align:"center",width:80,
	        	 	formatter:function(val, row, index){ 
	        	 		if(val == 0){
	        	 			return '<spring:message code="problemcomplaint.problemtype.0" />';
	        	 		}else if(val == 1){
	        	 			return '<spring:message code="problemcomplaint.problemtype.1" />';
	        	 		}else if(val == 2){
	        	 			return '<spring:message code="problemcomplaint.problemtype.2" />';
	        	 		}else if(val == 3){
	        	 			return '<spring:message code="problemcomplaint.problemtype.3" />';
	        	 		}else if(val == 4){
	        	 			return '<spring:message code="problemcomplaint.problemtype.4" />';
	        	 		}
       	        	},
	        	 },
	         	 { field: 'content', title: '<spring:message code="problemcomplaint.content" />',align:"center",width:100,
	         	 	formatter: function (value) {
	                    return "<span title='" + value + "'>" + value + "</span>";
	                }
	         	 },
	         	 { field: 'state', title: '<spring:message code="problemcomplaint.state" />',align:"center",width:60,
	         	 	formatter:function(val, row, index){ 
	        	 		if(val == 0){
	        	 			return '<spring:message code="problemcomplaint.state.0" />';
	        	 		}else if(val == 1){
	        	 			return '<spring:message code="problemcomplaint.state.1" />';
	        	 		}else if(val == 3){
	        	 			return '<spring:message code="problemcomplaint.state.3" />';
	        	 		}else if(val == 4){
	        	 			return '<spring:message code="problemcomplaint.state.4" />';
	        	 		}
       	        	},
	         	 },
	         	 { field: 'addtime', title: '<spring:message code="problemcomplaint.addtime" />',align:"center",width:100,
	         	 	formatter:function(val, row, index){ 
        	 			return val.substring(0,19);
       	        	},
	         	 },
	         	 { field: 'attachment', title: '<spring:message code="problemcomplaint.attachment" />',align:"center",width:60,
	         	 	formatter:function(val, row, index){ 
					 	return '<a class="btn-look" href="javascript:queryAttachment('+val+');"><spring:message code="page.look" /></a>';
       	        	},
	         	 },
	         	 { field: 'operate', title: '<spring:message code="page.operate" />',align:"center",width:100,
	         	 	formatter:function(val, row, index){ 
					 	return '<a class="btn-edit" href="javascript:updateProblemcomplaint('+val+');"><spring:message code="page.update" /></a>' +
					 	'<a class="btn-del" href="javascript:deleteProblemcomplaint('+val+');"><spring:message code="page.delete" /></a>';
       	        	},
	         	 },
	         ]]
	     });
	     var p = $('#problemcomplaintlist').datagrid('getPager');
	     $(p).pagination({
	         beforePageText: "<spring:message code='easyui.page.beforePageText'/>",
	         afterPageText: "<spring:message code='easyui.page.afterPageText' arguments='{pages}'/>",
	    	 displayMsg:	"<spring:message code='easyui.page.displayMsg' arguments='{from},{to},{total}'/>"
	     });
	}
	
	//将表单数据转为json
	function paramsJson(){
		var json = {
				querynetid:$("#querynetid").combobox("getValue"),
				queryareacode:$("#queryareacode").combotree("getValue"),
				querytype:$("#querytype").val(),
				queryusername:$("#queryusername").val(),
				queryproblemtype:$("#queryproblemtype").val(),
				querystate:$("#querystate").val(),
		};
		return json;
	}
</script>
</body>
</html>
