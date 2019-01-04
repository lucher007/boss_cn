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
<link rel="stylesheet" type="text/css" href="style/easyui/easyui.css">
<link rel="stylesheet" type="text/css" href="main/plugin/easyui/themes/icon.css">
</head>

<body>
	<div id="body">
	<div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.dispatch.manage"/>  &gt; <spring:message code="dispatch.assign"/></div>
    <form action="" method="post" id="searchForm" name="searchForm">
    <input type="hidden"  name="dispatcherSelected" id="dispatcherSelected"  />
    <input type="hidden" name="ids" id="ids" />
    <div class="seh-box" >
		<table>					        
			<tr>
				<td align="right" width="10%"><spring:message code="network.netname"/>：</td>
				<td width="20%">
					<%-- <select id="querynetid" name="querynetid" class="select" onchange="initArea();">
			 			<option value=""><spring:message code="page.select"/></option>
			 		</select> --%>
			 		<input id="querynetid" name="querynetid" width="120px">
             	</td>
             	<td align="right" width="10%"><spring:message code="area.areaname"/>：</td>
				<td>
					<!-- 
					<input class="easyui-combotree inp w200"  data-options="" id="queryareacode" name="queryareacode" width="120px" onChange= "queryDispatch();" >
					 -->
					<input id="queryareacode" name="queryareacode" width="120px">
				</td>
             	<td align="right" width="120px"><spring:message code="dispatch.dispatchcode"/>：</td>
				<td align="left"><input type="text" class="inp w200"  name="queryid" id="queryid" value="${dispatch.queryid}"></td>
				<td align="center">	
					<%-- 
					<input type="button" class="btn-sch" value="<spring:message code="page.query"/>" onclick="queryDispatch();" />
					 --%>
					<a href="javascript:queryDispatch();" iconCls="icon-search" style="width:80px" class="easyui-linkbutton" ><spring:message code="page.query"/></a>				
				</td>
				
			</tr>
					
			<tr>					
				
				<td align="right"><spring:message code="user.username"/>：</td>
				<td><input type="text" class="inp w200"  name="queryusername" id="queryusername" value="${dispatch.queryusername}"></td>	
				<td align="right" width="120px"><spring:message code="dispatch.problemtype"/>：</td>
				<td colspan="3">
					<select id="queryproblemtype" name="queryproblemtype" class="select" >
						<option value=""><spring:message code="page.select" /></option>
						<option value="0" <c:if test="${dispatch.queryproblemtype == '0'}">selected</c:if>>
							<spring:message code= "dispatch.problemtype.0"/>
						</option>
						<option value="1"<c:if test="${dispatch.queryproblemtype == '1'}">selected</c:if>>
							<spring:message code= "dispatch.problemtype.1"/>
						</option>
						<option value="2"<c:if test="${dispatch.queryproblemtype == '2'}">selected</c:if>>
							<spring:message code= "dispatch.problemtype.2"/>
						</option>
						<option value="3"<c:if test="${dispatch.queryproblemtype == '3'}">selected</c:if>>
							<spring:message code= "dispatch.problemtype.3"/>
						</option>
						<option value="4"<c:if test="${dispatch.queryproblemtype == '4'}">selected</c:if>>
							<spring:message code= "dispatch.problemtype.4"/>
						</option>
					</select>
				</td>
				<td align="center">
					<a href="javascript:selectDispatcher();" style="width:80px" class="easyui-linkbutton"><spring:message code="dispatch.assignselected"/></a>
				</td>		
			</tr>
		</table>
	</div>
    <%-- <div class="lst-box">
    	<table id="treetable" class="treetable"  width="100%" border="0" cellspacing="0" cellpadding="0">
    		<tr class="lb-tit">
    			<td><spring:message code="network.netname"/></td>
    			<td><spring:message code="area.areaname"/></td>
    			<td><spring:message code="dispatch.dispatchcode"/></td>
	          	<td><spring:message code="user.username"/></td>
	          	<td><spring:message code="dispatch.problemtype"/></td>
	          	<td><spring:message code="dispatch.state"/></td>
	          	<td><spring:message code="dispatch.content"/></td>
		        <td><spring:message code="page.operate"/></td>
		         <td style="white-space: nowrap;width: 51px"> 
		        	<input type="checkbox" id="checkall" onclick="checkAll();" style="vertical-align: middle;">
			        <label for="checkall"><spring:message code="page.select.all"/></label>
			    </td>	       
        	</tr>
 
        	<c:forEach items="${dispatch.dispatchlist }" var="dataList">
	        	<tr height="30"class="lb-list">
	        		<td >${dataList.network.netname}</td>
	        		<td >${dataList.area.areaname}</td>
	        	 	<td >${dataList.dispatchcode}</td>
	        		<td >${dataList.user.username}</td>
	        		<td><spring:message code= "dispatch.problemtype.${dataList.problemtype}"/></td>
	        		<td><spring:message code= "dispatch.state.${dataList.state}"/></td>
	        		<td class="remarkClass" title="${dataList.content }">${fn:substring(dataList.content, 0, 9)}&nbsp;</td>
	        		<td><a href="javascript:assignDispatch(${dataList.id });" ><spring:message code="dispatch.assign"/></a></td>
	        		<td>  
	            	    <input type="checkbox" class="checkbox" name="ids" value="${dataList.id}"
		                     onclick="checkbox();"
		                     style="vertical-align: middle;"/>
		            </td>
	        	</tr>
        	</c:forEach>
    	</table>
    </div>
    <div class="page">
    	<cite>
        	<pg:pager
			    url="dispatch/findByList"
			    items="${dispatch.pager_count}"
			    index="center"
			    maxPageItems="10"
			    maxIndexPages="5"
			    isOffset="<%= true %>"
			    export="offset,currentPageNumber=pageNumber"
			    scope="request">	
				<pg:param name="index"/>
				<pg:param name="maxPageItems"/>
				<pg:param name="maxIndexPages"/>
				<pg:param name="queryid" value="${dispatch.queryid}"/>
				<pg:param name="queryproblemtype" value="${dispatch.queryproblemtype}"/>
				<pg:param name="queryusername" value="${dispatch.queryusername}"/>
				<pg:param name="querynetid" value="${dispatch.querynetid}"/>
				<pg:param name="queryareacode" value="${dispatch.queryareacode}"/>
                <pg:param name="querystate" value="${dispatch.querystate}"/>
                <pg:param name="jumping" value="${dispatch.jumping}"/>
				<pg:index>
					<spring:message code="page.total"/>:${dispatch.pager_count}
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
    </form>
    <div id="dispatchlist" style="width:auto"></div>
    </div>
    
<script type="text/javascript" language="javascript" src="js/common/jquery.js"></script>
<script type="text/javascript" language="javascript" src="js/common/jquery.easyui.min.js"></script>
<script type="text/javascript" language="javascript" src="js/My97DatePicker/WdatePicker.js" defer="defer"></script>
<script type="text/javascript" language="javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
<script type="text/javascript" language="javascript" src="js/plugin/poshytip/jquery.poshytip.min.js"></script>
<script type="text/javascript" language="javascript" src="js/form.js"></script>
<script type="text/javascript" language="javascript" src="js/plugin/treeTable/jquery.treetable.js"></script>

<script type="text/javascript">
	
	$(function () {
      initNetwork();
       var returninfo = '${dispatch.returninfo}';
       if (returninfo != '') {
        	$.dialog.tips(returninfo, 1, 'alert.gif');
       }
    });
    
	function initNetwork(){
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
			     	initDispatchlist();
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
	  if('${dispatch.queryareacode}' != '' && '${dispatch.queryareacode}' != null){
	  	  var node = $('#queryareacode').combotree('tree').tree('find', '${dispatch.queryareacode}');
	  	  if(node != null){
	  	  	$('#queryareacode').combotree('setValue', node.id);
	  	  }
	  }
  }
  
	var selectDispatcherDialog;
	 function selectDispatcher() {
	    /* var checked = false;
	    $('.checkbox').each(function () {
	      if ($(this).attr('checked')) {
	        return checked = true;
	      }
	    });
		if(checked){
	    	selectDispatcherDialog = $.dialog({
		    	title: '<spring:message code="dispatch.dispatcherquery"/>',
		    	lock: true,
		    	width: 900,
		    	height: 500,
		    	top: 0,
		    	drag: false,
		    	resize: false,
		    	max: false,
		    	min: false,
		    	content: 'url:operator/findOperatorListForDialog?queryoperatortype=2' 
				});
		}else{
			$.dialog.tips('<spring:message code="page.select.empty"/>',1,'alert.gif');
		} */
		var rows = $('#dispatchlist').datagrid('getChecked');
		if(rows != ""){
			var ids = "";
			for(var i = 0;i < rows.length;i++){
				ids = ids + rows[i].id + ",";
			}
			$('#ids').val(ids);
			var netid = $('#querynetid').combobox('getValue');
			var areacode = $('#queryareacode').combotree('getValue');
			selectDispatcherDialog = $.dialog({
		    	title: '<spring:message code="dispatch.dispatcherquery"/>',
		    	lock: true,
		    	width: 900,
		    	height: 500,
		    	top: 0,
		    	drag: false,
		    	resize: false,
		    	max: false,
		    	min: false,
		    	content: 'url:operator/findOperatorListForDialog?queryoperatortype=2&querynetid='+netid+'&queryareacode='+areacode
				});
		}else{
			$.dialog.tips('<spring:message code="page.select.empty"/>',1,'alert.gif');
		} 
	 }
	
	 function closeDialog(data){	
		selectDispatcherDialog.close();
		$("#dispatcherSelected").val(data[0]);
		$.dialog.confirmMultiLanguage('<spring:message code="page.confirm.execution"/>?', 
		    '<spring:message code="page.confirm"/>',
			'<spring:message code="page.cancel"/>',
		    function(){ 
				$("#searchForm").attr("action", "dispatch/saveAssignSelected"+"?pager_offset="+'${dispatch.pager_offset}'+"&rid="+Math.random());
				$("#searchForm").submit();
			}, function(){
		});
	}
	
	
	function passSelected(data){
	     	closeDialog();
			alert("after send:"+data);
			$("#dispatcherSelected").val(data);
			$.dialog.confirmMultiLanguage('<spring:message code="page.confirm.execution"/>?', 
			    '<spring:message code="page.confirm"/>',
				'<spring:message code="page.cancel"/>',
			    function(){ 
					$("#searchForm").attr("action", "dispatch/saveAssignSelected"+"?pager_offset="+'${dispatch.pager_offset}'+"&operatorid="+'${Operator.id}'+"&rid="+Math.random());
					$("#searchForm").submit();
				}, function(){
			});
	}
	
	//全选
	function checkAll() {
	    $(':checkbox').attr('checked', !!$('#checkall').attr('checked'));
	}
	
	function checkbox() {
	    var checked = true;
	    $('.checkbox').each(function () {
	      if (!$(this).attr('checked')) {
	        checked = false;
	      }
	    });
	    $('#checkall').attr('checked', checked);
	}
	
		
    //查询操作员
	function queryDispatch(){
	    /* $("#searchForm").attr("action", "dispatch/findByList?jumping=findDispatchListForAssign&querystate=0");
		$("#searchForm").submit(); */
		$('#dispatchlist').datagrid('reload', paramsJson());
	}	
    
    /**
	*编辑
	*/
	function assignDispatch(id){
		$("#searchForm").attr("action", "dispatch/assignInit?id="+id+"&pager_offset="+'${dispatch.pager_offset}'+"&jumping=fromForAssign");
		$("#searchForm").submit();
	}
	
	function initDispatchlist(){
	    $('#dispatchlist').datagrid({
	         title: '<spring:message code="dispatch.dispatchquery" />',
	         url:'dispatch/dispatchJson',
	         queryParams: paramsJson(),
	         pageSize:10,
             singleSelect: false,
             pageList: [10,50,100], 
	         scrollbar:true,
	         pagination: true,
	         fitColumns:true,
	         rownumbers:true,
	         idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
	         loadMsg:'loading...',
	         columns: [[
	             { field: 'id', title: '全选',checkbox:true,align:"center",width:40},
	             { field: 'netid', hidden:'true', title: '<spring:message code="network.netid" />',align:"center",width:80},
	             { field: 'netname', title: '<spring:message code="network.netname" />',align:"center",width:80},
	             { field: 'areacode', hidden:'true', title: '<spring:message code="area.areacode" />',align:"center",width:80},
	             { field: 'areaname', title: '<spring:message code="area.areaname" />',align:"center",width:80},
	             { field: 'dispatchcode', title: '<spring:message code="dispatch.dispatchcode" />',align:"center",width:80},
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
	         	 { field: 'content', title: '<spring:message code="dispatch.content" />',align:"center",width:100,
	         	 	formatter: function (value) {
	                    return "<span title='" + value + "'>" + value + "</span>";
	                }
	         	 },
	         	 { field: 'operate', title: '<spring:message code="page.operate" />',align:"center",width:100,
	         	 	formatter:function(val, row, index){ 
					 	return '<a href="javascript:assignDispatch('+val+');" style="color:green;text-decoration: underline;"><spring:message code="dispatch.assign"/></a>';
       	        	},
	         	 },
	         ]]
	     });
	     var p = $('#dispatchlist').datagrid('getPager');
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
				querystate:"0",
		};
		return json;
	}
	
</script>
</body>
</html>

