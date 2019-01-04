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
  <link rel="stylesheet" type="text/css" href="main/plugin/easyui/themes/icon.css">
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
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：集团业务 &gt; <spring:message code="退卡"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="id" id="id" value="${user.user.id}"/>
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-con">
    	<div id="userInfo"></div>
      </div>
      <div id="usercardlist" style="width:auto"></div>
      <div id="cardtools">
		  <div style="margin-bottom:5px">
				 智能卡号：<input type="text"  class="inp w200" name="cardid" id="cardid"  onkeydown="if(event.keyCode==13){queryUsercardlist();}" >
				<span style="display: none">
					<input type="text"  class="inp w200" >
				</span>
		  </div>
	  </div>
      <div class="fb-con">
    	<div class="oldDeviceInfo">
		    	<table>
		          <tr class="fbc-tit">
		            <td colspan="8" style="font-weight: bold;"><spring:message code="userbusiness.rebackdevice.state"/></td>
		          </tr>
		          <tr>
		            <td align="right" colspan="2"><span class="red"><spring:message code="userbusiness.rebackdevice.state"/>：</span></td>
		            <td colspan="6">
		            	<select id="devicestate" name="devicestate" class="select">
		            	 	   <option value="3"><spring:message code="stb.state.3"/></option>
			                   <option value="0"><spring:message code="stb.state.0"/></option>
			            </select>
			            <span class="red">*</span>
		            </td>
		          </tr>
		        </table>
		</div>
      </div>
     
      <div class="fb-bom">
        <cite>
        	<input type="button" class="btn-save" value="<spring:message code="退卡"/>" onclick="unitCancelBusiness();" id="btnfinish">
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
	      initUsercardlist();
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
	   var url = 'usergroup/getUserInfo .' + data.tag;
	   $('#userInfo').load(url, data, function () {
	   });
	 }
  
	
	var unitCancelBusinessDialog;
	function unitCancelBusiness() {
	   
	  //获取所有选择的产品
	  var rowsSelected = $('#usercardlist').datagrid('getSelections');
	    if(rowsSelected == null  || rowsSelected == ''){
       	 $.dialog.tips('<spring:message code="请选择需要回收的智能卡"/>', 2, 'alert.gif');
		      return;
       }
	   //终端号
	    var terminalids = "";
		for(var i = 0;i < rowsSelected.length;i++){
			terminalids = terminalids + rowsSelected[i].cardid + ",";
		}		
	   
	   //var terminalid = rowsSelected.cardid;
	   var terminaltype = '1';
	   //终端类型(0-机顶盒；1-智能卡)
	  
		unitCancelBusinessDialog = $.dialog({
		    title: '<spring:message code="userbusiness.feecount"/>',
		    lock: true,
		    width: 800,
		    height: 400,
		    top: 0,
		    drag: false,
		    resize: false,
		    max: false,
		    min: false,
		    cancel:false,
		    content: 'url:usergroup/unitCancelBusiness?businesstype='+$('#businesstype').val()+'&terminalids='+terminalids+'&terminaltype='+terminaltype+'&devicestate='+$('#devicestate').val()
		});
			
		
	 }
	
	function closeCancelBusinessDialog(){
		unitCancelBusinessDialog.close();
		$("#addform").attr("action", "usergroup/businessUnit");
      	$("#addform").submit();
	}
	
	function initUsercardlist(){
	    $('#usercardlist').datagrid({
	         title: '<spring:message code="订户智能卡信息" />',
	         url:'usercard/usercardJson',
	         queryParams: usercardparamsJson(),
	         pageSize:5,
             singleSelect: false,
             pageList: [5,25,50], 
	         scrollbar:true,
	         pagination: true,
	         fitColumns:true,
	         rownumbers:true,
	         //onClickRow: onClickRow,
	         idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
	         loadMsg:'loading...',
	         toolbar:'#cardtools',
	         columns: [[
				 { field: 'id', title: '全选',checkbox:true,align:"center",width:40,
						  formatter: function(val, row, index){
					           return row.cardid;
					       }, 
				 },   
	             { field: 'cardid', title: '<spring:message code="card.cardid"/>',align:"center",width:100},
	             { field: 'addtime', title: '<spring:message code="user.buytime" />',align:"center",width:100,
	            	 formatter:function(val, row, index){ 
						 	return val==null?val:val.substring(0,19);
           	         },
	             },
	        	 { field: 'state', title: '<spring:message code="usercard.state" />',align:"center",width:100,
	            	 formatter:function(val, row, index){ 
	            		if(val==0){
	         				return	'<spring:message code="usercard.state.0"/>';
	         			}else if(val ==1){
	         				return	'<spring:message code="usercard.state.1"/>';
	         			}else if(val ==2){
	         				return	'<spring:message code="usercard.state.2"/>';
	         			}else if(val ==3){
	         				return	'<spring:message code="usercard.state.3"/>';
	         			}else if(val ==4){
	         				return	'<spring:message code="usercard.state.4"/>';
	         			}
           	        },
	        	 },
	        	 { field: 'mothercardflag', title: '<spring:message code="usercard.mothercardflag" />',align:"center",width:100,
	        		 formatter:function(val, row, index){ 
						 	if(val == '0'){
						 		return '<spring:message code="userstb.mothercardflag.0" />';
						 	}else if(val == '1'){
						 		return '<spring:message code="userstb.mothercardflag.1" />';
						 	}
     	              },
	        	 },
	        	 { field: 'mothercardid', title: '<spring:message code="usercard.mothercardid" />',align:"center",width:100,},
	        	 { field: 'cardflag', title: '高清标识',width:80,align:"center",
					   formatter: function(val, row, index){
						   if(val=='0'){
		                    	return '高清卡';
		                    }else{
		                    	return '标清卡';
		                    } 
		                }, 
		                styler:function(val, row, index){
		                    if(val=='0'){
		                    	return 'color:red;';
		                    }
		                }, 
				   }
	         ]],
	         onLoadSuccess:function(data){  
	        	 //默认选择第一条
	        	 //$('#usercardlist').datagrid("selectRow", 0);
	        	 //加载第一条智能卡的产品信息
	        	 //initUserproductlist();
	         },
	     });
	     var p = $('#usercardlist').datagrid('getPager');
	     $(p).pagination({
	         beforePageText: "<spring:message code='easyui.page.beforePageText'/>",
	         afterPageText: "<spring:message code='easyui.page.afterPageText' arguments='{pages}'/>",
	    	 displayMsg:	"<spring:message code='easyui.page.displayMsg' arguments='{from},{to},{total}'/>"
	     });
	}
 
    //将表单数据转为json
	function usercardparamsJson(){
		var json = {
				queryuserid:'${user.user.id}',
		};
		return json;
	}
    
	//选择智能卡列表
	function queryUsercardlist(){
		var cardid = $('#cardid').val();
		//刷新
		$('#usercardlist').datagrid('reload',{
			queryuserid:'${user.user.id}',
			querycardid:cardid
		});	
	}
	
</script>
</body>
</html>
