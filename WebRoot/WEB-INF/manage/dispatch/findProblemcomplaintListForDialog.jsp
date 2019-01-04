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
<link type="text/css" rel="stylesheet" href="style/easyui/easyui.css">
<link rel="stylesheet" type="text/css" href="main/plugin/easyui/themes/icon.css">
</head>

<body>
	<div id="body">
	<div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.dispatch.manage"/> &gt; <spring:message code="dispatch.complaintquery"/></div>
    <div class="seh-box">
        <form action="" method="post" id="searchForm" name="searchForm">
        	<input type="hidden"  name="querystate" id="querystate" value="${problemcomplaint.querystate }">
            <table width="100%">
				<tr>
					<td align="right" width="10%"><spring:message code="network.netname"/>：</td>
					<td width="20%">
				 	 	<input id="querynetid" name="querynetid" style="width: 157px;">
	             	</td>
	                <td align="right" width="10%"><spring:message code="area.areaname"/>：</td>
					<td>
						<input class="easyui-combotree inp w200" data-options="" id="queryareacode" name="queryareacode" >
					</td>
					<td align="right"><spring:message code="dispatch.problemtype"/>：</td>		
					<td><select id="queryproblemtype" name="queryproblemtype" class="select" onchange="queryComplaint();">
								<option value=""><spring:message code="page.select" /></option>
								<option value="0" <c:if test="${problemcomplaint.queryproblemtype == '0'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.0" />
								</option>
								<option value="1"<c:if test="${problemcomplaint.queryproblemtype == '1'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.1" />
								</option>
								<option value="2"<c:if test="${problemcomplaint.queryproblemtype == '2'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.2" />
								</option>	
								<option value="3"<c:if test="${problemcomplaint.queryproblemtype == '3'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.3" />
								</option>	
								<option value="4"<c:if test="${problemcomplaint.queryproblemtype == '4'}">selected</c:if>>
									<spring:message code="problemcomplaint.problemtype.4" />
								</option>						
						</select>
					</td>
				</tr>
				<tr>
					<td align="right"><spring:message code="problemcomplaint.complaintcode"/>：</td>
					<td>
						 <input type="text"  class="inp w200" name="queryid" id="queryid" value="${problemcomplaint.queryid }">
					</td>
					<td align="right"><spring:message code="user.username"/>：</td>
					<td>
						 <input type="text"  class="inp" name="queryusername" id="queryusername" value="${problemcomplaint.queryusername }">
					</td>
					<td align="center" colspan="2">
		    			<%-- 
		    			<input type="button" class="btn-sch" value="<spring:message code="page.query"/>" onclick="queryComplaint();"/>
		    			 --%>
		    			<a href="javascript:queryComplaint();" iconCls="icon-search" style="width:100px" class="easyui-linkbutton" ><spring:message code="page.query"/></a>
		    		</td>
				</tr>
				
    	    </table>
   		</form>
    </div>
    <%-- <div class="lst-box">
    	<table id="treetable" class="treetable"  width="100%" border="0" cellspacing="0" cellpadding="0">
    		<tr class="lb-tit">
    			<td width="60"><spring:message code="page.select"/></td>
    			<td><spring:message code="problemcomplaint.complaintcode"/></td>
    			<td><spring:message code="user.username"/></td>
    			<td><spring:message code="user.userid"/></td>  		
	          	<td><spring:message code="dispatch.problemtype"/></td>
	          	<td><spring:message code="dispatch.state"/></td>
	          	<td><spring:message code="problemcomplaint.content" />
        	</tr>
        	<c:forEach items="${problemcomplaint.problemcomplaintlist }" var="dataList">
	        	<tr height="30"class="lb-list">
	        		<td width="" height="30">
	        	    		<input type="radio"  name="_selKey"   value="${dataList.id }+${dataList.userid }+${dataList.user.username }+${dataList.user.netid }+${dataList.user.areacode }+${dataList.problemtype }+${dataList.complaintcode }+${dataList.content }"/>	
	        	    </td>
	        		<td >${dataList.complaintcode}</td>
	        		<td title="${dataList.user.username }">${fn:substring(dataList.user.username, 0, 20)}</td>
	        		<td >${dataList.userid }</td>
	        		<td ><spring:message code="problemcomplaint.problemtype.${dataList.problemtype}"/></td>
	        		<td ><spring:message code="problemcomplaint.state.${dataList.state }"/></td>
	        		<td class="remarkClass" title="${dataList.content }">${fn:substring(dataList.content, 0, 9)}&nbsp;</td>
	        	</tr>
        	</c:forEach>
    	</table>
    </div>
    <div class="page">
    	<cite>
        	<pg:pager
			    url="dispatch/findProblemcomplaintListForDialog"
			    items="${problemcomplaint.pager_count}"
			    index="center"
			    maxPageItems="5"
			    maxIndexPages="5"
			    isOffset="<%= true %>"
			    export="offset,currentPageNumber=pageNumber"
			    scope="request">	
				<pg:param name="index"/>
				<pg:param name="maxPageItems"/>
				<pg:param name="maxIndexPages"/>
				<pg:param name="querynetid" value="${problemcomplaint.queryuserid}"/>
				<pg:param name="queryusername" value="${problemcomplaint.queryusername}"/>
				<pg:param name="querydocumentno" value="${problemcomplaint.queryid}"/>
				<pg:param name="querystate" value="${problemcomplaint.querystate}"/>
				<pg:index>
					<spring:message code="page.total"/>:${problemcomplaint.pager_count}
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
    </div> --%>
    <div id="problemcomplaintlist" style="width:855px;"></div>
</div>
    
<script type="text/javascript" language="javascript" src="js/common/jquery.js"></script>
<script type="text/javascript" language="javascript" src="js/My97DatePicker/WdatePicker.js" defer="defer"></script>
<script type="text/javascript" language="javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
<script type="text/javascript" language="javascript" src="js/plugin/poshytip/jquery.poshytip.min.js"></script>
<script type="text/javascript" language="javascript" src="js/form.js"></script>
<script type="text/javascript" language="javascript" src="js/plugin/treeTable/jquery.treetable.js"></script>
<script type="text/javascript" language="javascript" src="js/common/jquery.easyui.min.js"></script>
<script type="text/javascript">
	
    //查询操作员
	function queryComplaint(){
		/* $("#searchForm").attr("action", "dispatch/findProblemcomplaintListForDialog");
		$("#searchForm").submit(); */
		$('#problemcomplaintlist').datagrid('reload', paramsJson());
	}	
	
	$(function () {
		initNetwork();
       var returninfo = '${stb.returninfo}';
       if (returninfo != '') {
        	$.dialog.tips(returninfo, 1, 'alert.gif');
       }
    });
    
  $(".lb-list").click(function(){
	var data =$(this).find("input[type=radio]").val();
    var dataArray = data.split("+");
	parent.closeDialog(dataArray);
  });
  
  function initNetwork(){
	    /* var data = {
			    t: new Date().getTime()
		     };
	    
    	$.getJSON('network/initNetworkJson', data, function (networkJson) {
		     var options = '<option value=""><spring:message code="page.select"/></option>';
		     for (var key in networkJson) {
		        options += '<option value="' + networkJson[key].id + '">' + networkJson[key].netname + '</option>';
		     }
		     $('#querynetid').children().remove();
		     $('#querynetid').append(options);
		     $('#querynetid').val('${dispatch.querynetid}');
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
				//queryDispatch();		
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
  
  function initProblemcomplaintlist(){
	    $('#problemcomplaintlist').datagrid({
	         title: '<spring:message code="dispatch.dispatchquery" />',
	         url:'problemcomplaint/problemcomplaintJson',
	         queryParams: paramsJson(),
	         singleSelect: true,
	         scrollbar:true,
	         pagination: true,
	         fitColumns:true,
	         rownumbers:true,
	         showFooter: false,
	         onClickRow: onClickRowcard,
	         idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
	         loadMsg:'loading...',
	         columns: [[
	         	 { field: 'id', title: '全选',checkbox:true,align:"center",width:40},
	             { field: 'complaintcode', title: '<spring:message code="problemcomplaint.complaintcode" />',align:"center",width:80},
	             { field: 'username', title: '<spring:message code="user.username" />',align:"center",width:100},
	        	 { field: 'usercode', title: '<spring:message code="user.usercode" />',align:"center",width:100},
	        	 { field: 'usernetid', hidden: 'true', title: '',align:"center",width:100},
	        	 { field: 'problemtype', title: '<spring:message code="dispatch.problemtype" />',align:"center",width:80,
	        	 	formatter:function(val, row, index){ 
	        	 		if(val == 0){
	        	 			return '<spring:message code="dispatch.problemtype.0" />';
	        	 		}else if(val == 1){
	        	 			return '<spring:message code="dispatch.problemtype.1" />';
	        	 		}else if(val == 2){
	        	 			return '<spring:message code="dispatch.problemtype.2" />';
	        	 		}else if(val == 3){
	        	 			return '<spring:message code="dispatch.problemtype.3" />';
	        	 		}else if(val == 4){
	        	 			return '<spring:message code="dispatch.problemtype.4" />';
	        	 		}
       	        	},
	        	 },
	        	 { field: 'state', title: '<spring:message code="dispatch.state" />',align:"center",width:60,
	        	 	formatter:function(val, row, index){ 
	        	 		if(val == 0){
	        	 			return '<spring:message code="dispatch.state.0" />';
	        	 		}else if(val == 1){
	        	 			return '<spring:message code="dispatch.state.1" />';
	        	 		}else if(val == 2){
	        	 			return '<spring:message code="dispatch.state.2" />';
	        	 		}else if(val == 3){
	        	 			return '<spring:message code="dispatch.state.3" />';
	        	 		}else if(val == 4){
	        	 			return '<spring:message code="dispatch.state.4" />';
	        	 		}else if(val == 5){
	        	 			return '<spring:message code="dispatch.state.5" />';
	        	 		}
       	        	},
	        	 },
	         	 { field: 'content', title: '<spring:message code="problemcomplaint.content" />',align:"center",width:100,
	         	 	formatter: function (value) {
	                    return "<span title='" + value + "'>" + value + "</span>";
	                }
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
				queryid:$("#queryid").val(),
				queryusername:$("#queryusername").val(),
				queryproblemtype:$("#queryproblemtype").val(),
				querystate:$("#querystate").val(),
		};
		return json;
	}
	
	//点击表格某一条
	function onClickRowcard(index, data){
		var dataArray = new Array(data.operate,data.usercode,data.username,data.usernetid,data.userareacode,
		data.problemtype,data.complaintcode,data.content);
		parent.closeDialog(dataArray);
	}
  
</script>
</body>
</html>

