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
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：集团业务 &gt; <spring:message code="menu.business.cancelproduct"/></div>
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
	    <div id="userproductlist" style="width:auto"></div>
	    <div id="producttools">
	    	<div style="margin-bottom:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"  plain="true" onclick="javascript:unitCancelBusiness();">产品取消</a>
			</div>
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
      <div class="fb-bom">
        <cite>
        	<input type="button" class="btn-save" value="产品取消" onclick="javascript:unitCancelBusiness();" id="btnfinish">
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
		var datas = $("#userproductlist").datagrid("getSelections");
		if(datas == "" || datas == null){//没有选中数据
			$.dialog.tips("请选择要取消的产品！", 1, 'alert.gif');
			return false;
		}
		
		for(var i=0; i<datas.length; i++){
			if(datas[i].remark != null && datas[i].remark != '' && datas[i].remark != '${Operator.id}'){
				$.dialog.tips('不能取消其他操作员办理的业务', 2, 'alert.gif');
				return ;
			}
		}
		
		var userproductid = "";
		for(var i = 0;i < datas.length;i++){
			userproductid = userproductid + datas[i].id + ",";
		}		
		
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
		    content: 'url:usergroup/unitCancelBusiness?businesstype='+$('#businesstype').val()+'&userproductids='+userproductid
		});
		
	    //查询当前订户该产品的授权结束时间
        /* var data = {
				     id: userproductid
				   };
		var url="userproduct/checkedUserproductCancel?rid="+Math.random();
		$.getJSON(url,data,function(jsonMsg){
			if(jsonMsg.flag == "0"){
		   		$.dialog.tips('<spring:message code="userproduct.authorization.started"/>', 1, 'alert.gif');
		   		return false;
			}else{
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
				    content: 'url:usergroup/unitCancelBusiness?businesstype='+$('#businesstype').val()+'&userproductid='+userproductid
				});
			}
		}); */
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
	         onClickRow: onClickRow,
	         idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
	         loadMsg:'loading...',
	         toolbar:'#cardtools',
	         columns: [[
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
	        	 initUserproductlist();
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
    
	function initUserproductlist(){
	    $('#userproductlist').datagrid({
	         title: '<spring:message code="订户产品信息" />',
	         url:'userproduct/userproductJson',
	         queryParams: userproductparamsJson(),
	         pageSize:5,
             singleSelect: false,
             pageList: [5,25,50], 
	         scrollbar:true,
	         pagination: true,
	         fitColumns:true,
	         rownumbers:true,
	         toolbar:'#producttools',
	         idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
	         loadMsg:'loading...',
	         //frozenColumns:[[ {field:'ck',checkbox:true}]],
	         columns: [[
	         	 { field: 'id', title: '全选',checkbox:true,align:"center",width:40,
     				  formatter: function(val, row, index){
     	                    return row.cardid;
     	                }, 
     			 }, 
	             { field: 'terminalid', title: '<spring:message code="user.terminalid"/>',align:"center",width:100},
	             { field: 'terminaltype', title: '<spring:message code="user.terminaltype" />',align:"center",width:100,
	            	 formatter:function(val, row, index){ 
	            		 if(val==0){
		         				return	'<spring:message code="user.terminaltype.0"/>';
		         			}else if(val ==1){
		         				return	'<spring:message code="user.terminaltype.1"/>';
		         			}
           	         },
	             },
	        	 { field: 'productid', title: '<spring:message code="product.productid" />',align:"center",width:100,},
	        	 { field: 'productname', title: '<spring:message code="product.productname" />',align:"center",width:100,},
	        	 { field: 'addtime', title: '<spring:message code="user.buytime" />',align:"center",width:110,
	        		 formatter:function(val, row, index){ 
						 	return val==null?val:val.substring(0,19);
        	         },
	        	 },
	        	 { field: 'starttime', title: '<spring:message code="userproduct.starttime" />',align:"center",width:100,
	        		 formatter:function(val, row, index){ 
						 	return val==null?val:val.substring(0,10);
        	         },
	        	 },
	        	 { field: 'endtime', title: '<spring:message code="userproduct.endtime" />',align:"center",width:100,
	        		 formatter:function(val, row, index){ 
						 	return val==null?val:val.substring(0,10);
        	         },
	        	 },
	        	 { field: 'source', title: '<spring:message code="userproduct.source" />',align:"center",width:100,
	            	 formatter:function(val, row, index){ 
	            		 if(val==0){
		         				return	'<spring:message code="userproduct.source.0"/>';
		         			}else if(val ==1){
		         				return	'<spring:message code="userproduct.source.1"/>';
		         			}
           	         },
	             },
	             { field: 'state', title: '<spring:message code="userproduct.state" />',align:"center",width:100,
	            	 formatter:function(val, row, index){ 
	            		 if(val==0){
		         				return	'<spring:message code="userproduct.state.0"/>';
	         			 }else if(val ==1){
	         				return	'<spring:message code="userproduct.state.1"/>';
	         			 }else if(val ==4){
	         				return	'<spring:message code="userproduct.state.4"/>';
	         			 }
           	         },
	             }/* ,
	             { field: 'id', title: '操作',width:80,align:"center",
					   formatter: function(val, row, index){
						   if(row.state=='1'){
		                    	return '<a class="btn-del" href="javascript:unitCancelBusiness('+row.id+');" ><spring:message code="page.cancel"/></a>';
		                    }
		                }, 
				   } */
	         ]],
	     });
	     var p = $('#userproductlist').datagrid('getPager');
	     $(p).pagination({
	         beforePageText: "<spring:message code='easyui.page.beforePageText'/>",
	         afterPageText: "<spring:message code='easyui.page.afterPageText' arguments='{pages}'/>",
	    	 displayMsg:	"<spring:message code='easyui.page.displayMsg' arguments='{from},{to},{total}'/>"
	     });
	}
 
    //将表单数据转为json
	function userproductparamsJson(){
    	//获取选中的智能卡
		//var selected = $('#usercardlist').datagrid('getSelected');
		
		var json = {
				queryuserid:'${user.user.id}',
				//terminalid:terminalid,
		};
		return json;
	}
    
	//点击表格某一条
	function onClickRow(index, data){
		var terminalid = data.cardid;
		var rows = $('#usercardlist').datagrid("getSelections");
		var terminalids = "";
		for(var i = 0;i < rows.length;i++){
			terminalids =terminalids + rows[i].cardid + ",";
		}
		$('#userproductlist').datagrid('reload',{
			queryuserid:'${user.user.id}',
			terminalids:terminalids,
		});	
	}
	
	//选择智能卡列表
	function queryUsercardlist(){
		var cardid = $('#cardid').val();
		//刷新
		$('#usercardlist').datagrid('clearSelections');
		$('#usercardlist').datagrid('reload',{
			queryuserid:'${user.user.id}',
			querycardid:cardid
		});	
	}
</script>
</body>
</html>
