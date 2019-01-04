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
      height: 25px;
    }
  </style>
</head>

<body>
<div id="body">
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：集团业务 &gt; <spring:message code="menu.business.accountrecharge"/></div>
  <form action="" method="post" id="addform" name="addform">
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
        <div>
	    	<table>
	          <tr class="fbc-tit">
	            <td colspan="8" style="font-weight: bold;"><spring:message code="menu.business.accountrecharge"/></td>
	          </tr>
	          <tr>
	            <td align="right"><spring:message code="userbusiness.accountrecharge.rechargemoney"/>：</td>
	            <td colspan="3">
	            	<input type="text" class="inp" style="width:150px;" name="rechargemoney" id="rechargemoney" value="" onpaste="return false" maxlength="10"
	            	onkeypress="onkeypressCheck(this);" onkeyup="onkeyupCheck(this);" onblur="onkeyblurCheck(this);">
	            </td>
	          </tr>
	        </table>
		</div>
      </div>
      <div class="fb-bom">
        <cite>
        	<c:choose>  
               <c:when test="${user.user.state ne '0' && user.user.state ne '3'}">
               		<input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="saveRechargeAccount();" id="btnfinish">
               </c:when>
            </c:choose>
        </cite>
        <span class="red">${user.returninfo }</span>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript" src="js/common/jquery.js"></script>
<script type="text/javascript" src="js/common/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/form.js"></script>
<script type="text/javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
<script type="text/javascript">
    
    function saveRechargeAccount() {
        if ($('#rechargemoney').val() == '') {
		      $.dialog.tips('<spring:message code="userbusiness.accountrecharge.rechargemoney.empty"/>', 1, 'alert.gif');
		      $('#rechargemoney').focus();
		      return;
	    }
        
        $.dialog.confirmMultiLanguage('<spring:message code="page.confirm.execution"/>?',
 	             '<spring:message code="page.confirm"/>',
 	             '<spring:message code="page.cancel"/>',
 	             function(){ 
					 $("#addform").attr("action", "usergroup/saveRechargeAccount");
			    	 $("#addform").submit();
		         }, 
                 function(){
		});
    }
    
    
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
	
	var unitBusinessDialog;
	function unitBusiness() {
	  
	   var address = $("#address").val();
	   if(address == null || address == ''){
	   		$.dialog.tips('<spring:message code="user.newaddress.empty"/>', 1, 'alert.gif');
	   		$("#address").focus();
	   		return false;
	   }
	  
	   unitBusinessDialog = $.dialog({
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
		    content: 'url:usergroup/unitBusiness?businesstype='+$('#businesstype').val()+'&address='+$('#address').val()
		});
	 }
	
	function closeBusinessDialog(){
		unitBusinessDialog.close();
		loadUserInfo();
	}
	
	
	/////////////////////对必要的输入框进行数字合法验证//////////////////////////
	function onkeypressCheck(obj){
		if (!obj.value.match(/^[1-9]?\d*?\.?\d*?$/)) {
			obj.value = obj.t_value;
		} else {
			obj.t_value = obj.value;
		}
		if (obj.value.match(/^(?:[1-9]?\d+(?:\.\d+)?)?$/)) {
			obj.o_value = obj.value;
		}
		if(obj.value.match(/^\d+\.\d{3}?$/)){
		   obj.value = obj.value.substr(0,obj.value.length-1);
		}
		//updateAccountmoney();
	}
	
	function onkeyupCheck(obj){
		if (!obj.value.match(/^[1-9]?\d*?\.?\d*?$/)) {
				obj.value = obj.t_value;
			} else {
				obj.t_value = obj.value;
			}
			if (obj.value.match(/^(?:[1-9]?\d+(?:\.\d+)?)?$/)) {
				obj.o_value = obj.value;
			}
			if (obj.value.match(/^\.$/)) {
				obj.value = "";
			}
			if (obj.value.match(/^\-$/)) {
				obj.value = "";
			}
			if (obj.value.match(/^00+/)) {
				obj.value = "";
			}
			if (obj.value.match(/^0\.00/)) {
				obj.value = "";
			}
			if (obj.value.match(/^0[1-9]/)) {
				obj.value = "";
			}
			if(obj.value.match(/^\d+\.\d{3}?$/)){
				obj.value = obj.value.substr(0,obj.value.length-1);
			} 
			if(obj.value == 'undefined'){
				obj.value='';
			}
			
			//updateAccountmoney();
	}
	
	function onkeyblurCheck(obj){
		if(obj.value==0){
			//obj.value='';
		}
		if(obj.value==''){
			obj.value = 0;
		}
		if (!obj.value.match(/^(?:[1-9]?\d+(?:\.\d+)?|\.\d*?)?$/)) {
			obj.value = obj.o_value;
		}else {
			if (obj.value.match(/^\.\d+$/)) {
				obj.value = 0 + obj.value;
			}
			obj.o_value = obj.value;
		}
		if(!obj.value.match(/^\d+(\.\d{3})?$/)){
			obj.value = obj.value.substr(0,obj.value.indexOf(".")+3);
		} 
		//updateAccountmoney();
	}
	
	function initUsercardlist(){
	    $('#usercardlist').datagrid({
	         title: '<spring:message code="订户智能卡信息" />',
	         url:'usercard/usercardJson',
	         queryParams: usercardparamsJson(),
	         pageSize:5,
             singleSelect: true,
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
