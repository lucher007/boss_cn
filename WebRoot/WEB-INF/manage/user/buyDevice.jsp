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
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="menu.business.manage"/> &gt; <spring:message code="menu.business.devicebuy"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="id" id="id" value="${user.user.id}"/>
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-con">
    	<div id="userInfo"></div>
    	<!--
    	<div id="userstbInfo"></div>
        <div id="buyingStbInfo"></div>
          -->
      </div>
	      <div id="usercardlist" style="width:98%"></div>
		  <div id="cardtools">
				  <div style="margin-bottom:5px">
						 智能卡号：<input type="text"  class="inp w200" name="cardid" id="cardid"  onkeydown="if(event.keyCode==13){queryUsercardlist();}" >
						<span style="display: none">
							<input type="text"  class="inp w200" >
						</span>
				  </div>
		  </div>
		  <div id="userproductlist" style="width:100%"></div>
	  <div class="fb-con">
        <div id="buyingCardInfo"></div>
        <div id="buyingProductInfo"></div>
        
        <!-- 改变子母卡属性弹出框 -->
        <div class="pop-box" id="pop-div">
			<table width="400" border="0" cellpadding="0" cellspacing="0">
	          <tr>
	            <td height="30" width="30%" align="right"><spring:message code="userstb.mothercardflag"/>：</td>
	            <td width="60%">
	            	<select id="mothercardflag" name="mothercardflag" class="select" onchange="onchangeMothercardflag();">
		                <option value="1" ><spring:message code="userstb.mothercardflag.1"/></option>
		                <option value="0" ><spring:message code="userstb.mothercardflag.0"/></option>
		            </select>
	            </td>
	          </tr>
	          <tr id="mothercardidTr">
	            <td height="30" width="30%" align="right"><spring:message code="userstb.mothercardid"/>：</td>
	            <td width="60%">
	            	<select id="mothercardid" name="mothercardid" class="select">
			        </select>
	            </td>
	          </tr>
			</table>
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
      </div>
     <!-- 其他费用收取 -->
      <div style="margin:10px 0;"></div>
      <table id="buyingotherfeeData" style="width:auto;height: 150px;"></table>
      <div id="otherfeetools">
		<div style="margin-bottom:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"  plain="true" onclick="javascript:openWindow('otherfee');">其他费用收取</a>
			
			<%--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"     plain="true" onclick="javascript:addOtherfee();">其他费用收取</a>--%>
		</div>
	  </div>
	   <!--其他费用收取窗口-->		
	  <div id="otherfee" class="easyui-window" title="<spring:message code="其他费用收取信息"/>" closed="true" style="width:450px;height:250px;">
		<div style="padding:10px 20px 10px 40px;">
			<table width="100%">
				 <tr height="30px">
		            <td align="right"><spring:message code="service.servicetype"/>：</td>
					<td align="left">
						 <input id="typekey" name="typekey" style="width: 157px;">
						 <span class="red">*</span>
					</td>
		        </tr>
		        <tr height="30px">
		            <td align="right"><spring:message code="statreport.business.price"/>：</td>
		            <td align="left">
		            	<input type="text" class="easyui-numberbox" data-options="precision:2"  style="width:150px;" name="price" id="price"  onpaste="return false" maxlength="10">
		            </td>
		        </tr>
		        <tr height="30px">
		            <td align="right">备注：</td>
		            <td align="left">
		            	<input type="text"  class="inp" style="width:250px;" name="remark" id="remark"  maxlength="30" >
		            </td>
		        </tr>
			</table>
			<div style="margin:20px 0;"></div>
			<div style="padding:5px;text-align:center;">
				<a onclick="saveOtherfee(this);" style="width:60px" class="easyui-linkbutton"><spring:message code="page.save"/></a>
				<a onclick="closeWindow(this);" style="width:60px" class="easyui-linkbutton"><spring:message code="page.cancel"/></a>
			</div>
		</div>
	  </div>
      <div class="fb-bom">
        <cite>
        	<c:choose>  
               <c:when test="${user.user.state ne '0' && user.user.state ne '3'}">
               		 <input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="unitBusiness();" id="btnfinish">
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
	
	 function updateProduct() {
	    if ($('#userid').val() == '') {
	      $.dialog.tips('<spring:message code="user.userid.empty"/>', 1, 'alert.gif');
	      $('#userid').focus();
	      return;
	    }
	       
	    var hasSelected = false;
	    $('.checkbox').each(function () {
	      if ($(this).attr('checked')) {
	        return hasSelected = true;
	      }
	    });
	    if (!hasSelected) {
	      $.dialog.tips('<spring:message code="user.service.empty"/>', 1, 'alert.gif');
	      return;
	    }
	     
	     $('#addform').attr('action', 'user/update');
	     $("#addform").submit();
	 }
	 
	 function goBack() {
	     $("#addform").attr("action", "user/findByList");
	     $("#addform").submit();
	 }
  
  
	$(function () {
	      loadUserInfo();
	      initUsercardlist();
	      //loadUserstbInfo();
	      //loadBuyingStbInfo();
	      loadBuyingCardInfo();
	      loadBuyingProductInfo();
	      initBuyingOtherfeeDatagrid();
	      initOtherbusinesstype();
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
	   var url = 'user/getUserInfo .' + data.tag;
	   $('#userInfo').load(url, data, function () {
	   });
	 }
	 
	 function loadUserstbInfo() {
		   var data = {
		     id: '${user.user.id}',
		     tag: 'userstbInfo'
		   };
		   var url = 'user/getUserstbInfo .' + data.tag;
		   		$('#userstbInfo').load(url, data, function () {
		   });
	 }
  
	 function loadBuyingStbInfo() {
	  
	   var data = {
	     id: '${user.user.id}',
	     tag: 'buyingStbInfo'
	   };
	   
	   var url = 'user/getBuyingStbInfo .' + data.tag;
	   $('#buyingStbInfo').load(url, data, function () {
	   });
	 }
  
	 function loadBuyingCardInfo() {
	   var data = {
	     id: '${user.user.id}',
	     tag: 'buyingCardInfo'
	   };
	   var url = 'user/getBuyingCardInfo .' + data.tag;
	   $('#buyingCardInfo').load(url, data, function () {
	   });
	 }
	 
	 function loadBuyingProductInfo() {
	   var data = {
	     id: '${user.user.id}',
	     tag: 'buyingProductInfo'
	   };
	   var url = 'user/getBuyingProductInfo .' + data.tag;
	   $('#buyingProductInfo').load(url, data, function () {
	   });
	 }
  
	 var stbDialog;
	 function addStb() {
	    stbDialog = $.dialog({
		    title: '<spring:message code="stb.stbquery"/>',
		    lock: true,
		    width: 900,
		    height: 500,
		    top: 0,
		    drag: false,
		    resize: false,
		    max: false,
		    min: false,
		    content: 'url:user/findStbListForDialog?querystate=0'
		});
	 }
  
	function closeStbDialog(){
		stbDialog.close();
		loadBuyingStbInfo();
		loadBuyingCardInfo();
	}
  
    //删除购买中的机顶盒
	function deleteBuyingStb(stbno){
	    var data = "stbno="+ stbno;
		var url="user/deleteBuyingStb?rid="+Math.random();
		$.getJSON(url,data,function(jsonMsg){
			if(jsonMsg.flag=="0"){
			    $.dialog.tips(jsonMsg.error, 1, 'alert.gif');
			}else if(jsonMsg.flag=="1"){
				loadBuyingStbInfo();
				loadBuyingCardInfo();
				loadBuyingProductInfo();
			}
		});
	}	
	
	var cardDialog;
	function addCard(stbno) {
	   cardDialog = $.dialog({
		    title: '<spring:message code="card.cardquery"/>',
		    lock: true,
		    width: 900,
		    height: 500,
		    top: 0,
		    drag: false,
		    resize: false,
		    max: false,
		    min: false,
		    content: 'url:user/findCardListForDialog?querystate=0'+'&stbno='+stbno
	   });
	}
     
    function closeCardDialog(){
		cardDialog.close();
		loadUserstbInfo();
		loadBuyingStbInfo();
		loadBuyingCardInfo();
	}
	
	//删除购买中的智能卡
	function deleteBuyingCard(cardid){
	    var data = "cardid="+ cardid;
		var url="user/deleteBuyingCard?rid="+Math.random();
		$.getJSON(url,data,function(jsonMsg){
			if(jsonMsg.flag=="0"){
			    $.dialog.tips(jsonMsg.error, 1, 'alert.gif');
			}else if(jsonMsg.flag=="1"){
				loadUserstbInfo();
				loadBuyingStbInfo();
				loadBuyingCardInfo();
				loadBuyingProductInfo();
			}
		});
	}	
	
	var productDialog;
	function addProduct(cardid,stbno,mothercardflag,mothercardid) {
		
	   //var cardid = $("input[type='radio']:checked").val();
	   //if(cardid == null || cardid == ''){
	   //		$.dialog.tips('<spring:message code="userproduct.cardid.empty"/>', 1, 'alert.gif');
	   //		return false;
	   //}
	  
	   productDialog = $.dialog({
		    title: '<spring:message code="product.productquery"/>',
		    lock: true,
		    width: 900,
		    height: 500,
		    top: 0,
		    drag: false,
		    resize: false,
		    max: false,
		    min: false,
		    content: 'url:user/findProductListForDialog?cardid='+cardid+'&stbno='+stbno+'&mothercardflag='+mothercardflag+'&mothercardid='+mothercardid
	   });
	}
     
    function closeProductDialog(){
		productDialog.close();
		loadBuyingProductInfo();
	}
	
	//删除购买中的产品
	function deleteBuyingProduct(terminalid,productid){
	    var data = {
	    			terminalid: terminalid,
				    productid: productid
				   };
		var url="user/deleteBuyingProduct?rid="+Math.random();
		$.getJSON(url,data,function(jsonMsg){
			if(jsonMsg.flag=="0"){
				$.dialog.tips(jsonMsg.error, 1, 'alert.gif');
			}else if(jsonMsg.flag=="1"){
				loadBuyingProductInfo();
			}
		});
	}	
	
	var unitBusinessDialog;
	function unitBusiness() {
	   if($('.stbflag').length==0 && $('.cardflag').length==0 && $('.productflag').length==0){
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
		    content: 'url:user/unitBusiness?businesstype='+$('#businesstype').val()
		});
	 }
	
	function closeBusinessDialog(){
		//unitBusinessDialog.close();
		//loadUserInfo();
		//loadBuyingStbInfo();
		//loadBuyingCardInfo();
		//loadBuyingProductInfo();
		$("#addform").attr("action", "user/businessUnit");
      	$("#addform").submit();
	}
	
	var updateMothercardflagDialog;
	function updateMothercardflagInit(terminaltype,terminalid) {	   
	    var data = {
		    terminaltype: terminaltype,
		    terminalid: terminalid,
		    t: new Date().getTime()
		  };
		  
		 $.getJSON('userproduct/initMothercardListJson', data, function (mothercardListJson) {
		       var options = '<option value=""><spring:message code="page.select"/></option>';
		       for (var key in mothercardListJson) {
		            options += '<option value="' + key + '">' + mothercardListJson[key] + '</option>';
		  	   }
		       $('#mothercardid').children().remove();
		       $('#mothercardid').append(options);
	      });
		  
	     updateMothercardflagDialog = $.dialog({
		    title: '<spring:message code="userstb.mothercardflag"/>',
		    lock: true,
		    width: 400,
		    height:100,
		    top: 200,
		    drag: false,
		    resize: false,
		    max: false,
		    min: false,
		    content: $("#pop-div").html(),
		   	ok: function(){
		   		   if($('#mothercardflag').val() == '1' && $('#mothercardid').val() == '') {
				      	$.dialog.tips('<spring:message code="usercard.mothercardid.empty"/>', 1, 'alert.gif');
				      	$('#mothercardid').focus();
				      return false;
				   }
		   		   updateMothercardflag(terminaltype,terminalid);
		   		},
		   	okVal:'<spring:message code="page.confirm"/>',
		   	cancel:function(){/* updateMothercardflagDialog.close(); */},
		   	cancelVal:'<spring:message code="page.cancel"/>'
	   });
	 }
	 
	 
	 
	 
	//高清，标清修改
	var updateSigncardflagDialog;
	function updateSignflagInit(cardid) {
	     updateSigncardflagDialog = $.dialog({
		    title: '智能卡标识',
		    lock: true,
		    width: 400,
		    height:100,
		    top: 200,
		    drag: false,
		    resize: false,
		    max: false,
		    min: false,
		    content: $("#sign-div").html(),
		   	ok: function(){
		   		   updateSigncardflag(cardid);
		   		},
		   	okVal:'<spring:message code="page.confirm"/>',
		   	cancel:function(){/* updateMothercardflagDialog.close(); */},
		   	cancelVal:'<spring:message code="page.cancel"/>'
	   });
	 }
	 function updateSigncardflag(cardid){
		var data = {
			cardflag:$("#signcardflag").val(),
		    cardid:cardid,
		    t: new Date().getTime()
		  };
		 $.getJSON('userproduct/signcardListJson', data, function (msg) {
		 	if(msg.flag == 1){
		 		closeUpdateSigncardflagDialog();
		 	}
	     });
    }
    function closeUpdateSigncardflagDialog(){
		loadBuyingStbInfo();
		loadBuyingCardInfo();
		loadBuyingProductInfo();
		updateSigncardflagDialog.close();
	}
	
	
	function onchangeMothercardflag(){
		if($("#mothercardflag").val()=="1"){
		    $("#mothercardidTr").show();
		}else{
		    $("#mothercardidTr").hide();
		}
	}
	
	function updateMothercardflag(terminaltype,terminalid){
		var data = {
				     terminaltype: terminaltype,
				       terminalid: terminalid,
				     mothercardflag: $('#mothercardflag').val(),
				     mothercardid: $('#mothercardid').val()
				   };
		var url="userproduct/updateMothercardflag?rid="+Math.random();
		$.getJSON(url,data,function(jsonMsg){
		    
			if(jsonMsg.flag=="0"){
			    $.dialog.tips(jsonMsg.error, 2, 'alert.gif');
			}else if(jsonMsg.flag=="1"){
				closeUpdateMothercardflagDialog();
			}
		});
    }
	
	function closeUpdateMothercardflagDialog(){
		loadBuyingStbInfo();
		loadBuyingCardInfo();
		loadBuyingProductInfo();
		updateMothercardflagDialog.close();
	}
	
	//------------------------------------------------------
	 
	//初始化其他业务类型
	function initOtherbusinesstype(){
	   $('#typekey').combobox({    
			url:'businesstype/initOtherbusinesstypeJson',
		    valueField:'id',    
		    //limitToList:true,
		    editable:false,
		    textField:'name',
	        onSelect: function(rec){ 
	        	$('#price').numberbox('setValue',rec.price);
        	},
		    onLoadSuccess:function(data){
			    	//初始化列表框的默认选择值
					var val = $('#typekey').combobox("getData");
                    for (var item in val[0]) {  
                        if (item == "id") {  
                            $(this).combobox("select", val[0][item]);  
                        }  
                     }  	
			    }
		});  
    }
 

     //开启弹出框
    function openWindow(service_name){
	    //如果是弹出其他费用收取框，先初始化业务类型数据
		$('#'+service_name).window('open');
		$('#'+service_name).window("resize",{top:$(document).scrollTop() + ($(window).height()-250) * 0.5});//居中显示
	}
   
    //关闭弹出框
	function  closeWindow(obj){
		$(obj).parents('.easyui-window').window('close');
	}
    
	//初始化其他费用收取
	function initBuyingOtherfeeDatagrid(){
      $('#buyingotherfeeData').datagrid({
           title: '其他费用收取',
           //queryParams: paramsJson(),
           url:'usergroup/getBuyingOtherfeeJson',
           pagination: false,
           singleSelect: true,
           scrollbar:true,
           rownumbers:true,
           toolbar:'#otherfeetools',
           //checkOnSelect: false,// 如果为true，当用户点击行的时候该复选框就会被选中或取消选中。 如果为false，当用户仅在点击该复选框的时候才会呗选中或取消。
           //pageSize: 10,
           //pageList: [10,30,50], 
           fitColumns:true,
           idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
           loadMsg:'正在加载数据......',
           columns: [[
			   { field: 'typekey', title: '费用KEY',width:50,align:"center",},
			   { field: 'typename', title: '费用名称',width:50,align:"center",},
			   { field: 'price', title: '金额',width:50,align:"center",},
			   { field: 'remark', title: '备注',width:50,align:"center",},
			   { field: 'id', title: '<spring:message code="page.operate" />',align:"center",width:50,
  	 					formatter:function(val, row, index){ 
  	 						return '<a class="btn-del" href="javascript:deleteBuyingOtherfee(\''+row.typekey+'\');" ><spring:message code="page.delete"/></a>';
            	       }
       			},
           ]]
       });
	}
	
	//其他费用收取添加
    function saveOtherfee(obj){
		
    	if ($('#typekey').combobox('getValue') == '') {
		      //$.dialog.tips('请选择业务类型', 1, 'alert.gif');
		      $('#typekey').next('span').find('input').focus();
		      return;
	    }
		
    	var typekey = $('#typekey').combobox('getValue');
		var data = {
				typekey:typekey,
				price: $('#price').numberbox('getValue'),
				remark:$('#remark').val(),
			  };
			var url="usergroup/addBuyingOtherfee?rid="+Math.random();
			$.getJSON(url,data,function(jsonMsg){
				$(obj).parents('.easyui-window').window('close');
				if(jsonMsg.flag=="0"){
				    $.dialog.tips(jsonMsg.error, 3, 'alert.gif');
				}else if(jsonMsg.flag=="1"){
					//刷新收取的其他收费
					$('#buyingotherfeeData').datagrid('reload');
				}
			});
	}
	
     //删除购买中的其他费用
	function deleteBuyingOtherfee(typekey){
    	 
		var data = {
				typekey:typekey,
			  };
	   
		var url="usergroup/deleteBuyingOtherfee?rid="+Math.random();
		$.getJSON(url,data,function(jsonMsg){
			if(jsonMsg.flag=="0"){
			    $.dialog.tips(jsonMsg.error, 1, 'alert.gif');
			}else if(jsonMsg.flag=="1"){
				//刷新收取的其他收费
				$('#buyingotherfeeData').datagrid('reload');
			}
		});
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
	        	 $('#usercardlist').datagrid("selectRow", 0);
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
             singleSelect: true,
             pageList: [5,25,50], 
	         scrollbar:true,
	         pagination: true,
	         fitColumns:true,
	         rownumbers:true,
	         idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
	         loadMsg:'loading...',
	         columns: [[
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
	        	 { field: 'addtime', title: '<spring:message code="user.buytime" />',align:"center",width:100,
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
	             }
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
		var selected = $('#usercardlist').datagrid('getSelected');
    	var terminalid = selected['cardid'];
		var json = {
				queryuserid:'${user.user.id}',
				terminalid:terminalid,
		};
		return json;
	}
    
	//点击表格某一条
	function onClickRow(index, data){
		var terminalid = data.cardid;
		$('#userproductlist').datagrid('reload',{
			queryuserid:'${user.user.id}',
			terminalid:terminalid,
		});	
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
