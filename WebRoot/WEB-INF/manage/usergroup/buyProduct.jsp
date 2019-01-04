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
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="集团业务"/> &gt; <spring:message code="menu.business.productbuy"/></div>
  <form action="" method="post" id="addform" name="addform">
    <input type="hidden" name="id" id="id" value="${user.user.id}"/>
    <input type="hidden" name="businesstype" id="businesstype" value="${user.businesstype }"/>
    <div class="form-box">
      <div class="fb-con">
    	<div id="userInfo"></div>
      </div>
      <div id="usercardlist" style="width:98%"></div>
	  <div id="usercardtools">
			  <div style="margin-bottom:5px">
					 智能卡号：<input type="text"  class="inp w200" name="usercardid" id="usercardid"  onkeydown="if(event.keyCode==13){queryUsercardlist();}" >
					<span style="display: none">
						<input type="text"  class="inp w200" >
					</span>
			  </div>
	  </div>
	  <div id="userproductlist" style="width:100%"></div>
      <!-- 智能卡购买 -->
      <div style="margin:10px 0;"></div>
      <div id="usercardData" style="width:auto;height: 200px;"></div>
      <div id="cardtools">
		<div style="margin-bottom:5px">
			购买类型： <select id="buytype" name="buytype" class="select" onchange="changeBuytype();">
					 	 <option value="0">全部卡号购买</option>
		             	 <option value="1">未授权卡号购买</option>
		             	 <option value="2">单个卡号购买</option>
		             </select>
			<span id = "singlebuy">
				 智能卡号：
				 <input type="text"  class="inp w200" name="cardid" id="cardid"  onkeydown="if(event.keyCode==13){querySinglebuy();}" >
			</span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"  plain="true" onclick="javascript:addProduct();">购买产品</a>
			<span style="display: none">
				<input type="text"  class="inp w200" >
			</span>
		</div>
	  </div>
	  
	  <!-- 产品购买 -->
      <div style="margin:10px 0;"></div>
      <div id="buyingproductData" style="width:auto;height: 200px;"></div>
     
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
	
	$(function () {
	      loadUserInfo();
	      initUsercardlist();
	      initUserCardDatagrid();
	      initBuyingProductDatagrid();
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
	   var url = 'usergroup/getUserInfo .' + data.tag;
	   $('#userInfo').load(url, data, function () {
	   });
	 }
  
	//初始化订户的智能卡列表
	function initUserCardDatagrid(){
		var buytype = $('#buytype').val();
		
		if(buytype == '0' || buytype == '1'){
			$("#singlebuy").hide();
		}else{
			$("#singlebuy").show();
		}
		
      $('#usercardData').datagrid({
           title: '订户的智能卡信息',
           //queryParams: paramsJson(),
           url:'usergroup/getUserCardJson',
           pagination: false,
           singleSelect: true,
           scrollbar:true,
           rownumbers:true,
           toolbar:'#cardtools',
           //checkOnSelect: false,// 如果为true，当用户点击行的时候该复选框就会被选中或取消选中。 如果为false，当用户仅在点击该复选框的时候才会呗选中或取消。
           //pageSize: 10,
           //pageList: [10,30,50], 
           fitColumns:true,
           idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
           loadMsg:'正在加载数据......',
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
				{ field: 'cardflag', title: '高清标识',width:50,align:"center",
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
           ]]
       });
	}
	 
	//选择购买方式，刷新智能卡列表
	function changeBuytype(){
		
		var buytype = $('#buytype').val();
		
		if(buytype == '0' || buytype == '1'){
			$("#singlebuy").hide();
		}else{
			$("#singlebuy").show();
		}
		
		$('#usercardData').datagrid('reload',{
			    buytype:buytype,
		});	
	}
	
	//选择购买方式，刷新智能卡列表
	function querySinglebuy(){
		var buytype = $('#buytype').val();
		var cardid = $('#cardid').val();
		$('#usercardData').datagrid('reload',{
			    buytype:buytype,
			    cardid:cardid
		});	
	}
	
	//初始化购买中的产品列表
	function initBuyingProductDatagrid(){
      $('#buyingproductData').datagrid({
           title: '购买中的产品信息',
           //queryParams: paramsJson(),
           url:'usergroup/getBuyingProductJson',
           pagination: false,
           singleSelect: true,
           scrollbar:true,
           rownumbers:true,
           toolbar:'#producttools',
           //checkOnSelect: false,// 如果为true，当用户点击行的时候该复选框就会被选中或取消选中。 如果为false，当用户仅在点击该复选框的时候才会呗选中或取消。
           //pageSize: 10,
           //pageList: [10,30,50], 
           fitColumns:true,
           idField:'id',   //idField 属性已设置，数据网格（DataGrid）的选择集合将在不同的页面保持。
           loadMsg:'正在加载数据......',
           columns: [[
			   { field: 'cardid', title: '智能卡号',width:50,align:"center",},
			   { field: 'productid', title: '产品ID',width:20,align:"center",},
			   { field: 'productname', title: '产品名称',width:50,align:"center",},
			   { field: 'starttime', title: '开始时间',width:50,align:"center",
				    formatter:function(val, row, index){ 
					 	return val==null?val:val.substring(0,10);
          	        	},
			   },
			   { field: 'endtime', title: '结束时间',width:50,align:"center",
				   formatter:function(val, row, index){ 
					 	return val==null?val:val.substring(0,10);
         	        	},
			   },
			   { field: 'price', title: '单价',width:50,align:"center",},
			   { field: 'totalmoney', title: '金额',width:50,align:"center",},
			   { field: 'id', title: '<spring:message code="page.operate" />',align:"center",width:50,
  	 					formatter:function(val, row, index){ 
  	 						return '<a class="btn-del" href="javascript:deleteBuyingProduct('+row.id+','+row.productid+');" ><spring:message code="page.delete"/></a>';
            	       }
       			},
           ]]
       });
	}
	
	//购买产品列表
	var productDialog;
	function addProduct() {
	   var cardids="";
	   var cardidArr = [];
	   
	   var buytype = $('#buytype').val();
	   if(buytype != "2" || buytype == "1"){ //不是单个购买
		   var rows = $('#usercardData').datagrid("getRows");
		   if(rows ==null || rows =='' || rows.length<0){
			   $.dialog.tips('没有智能卡购买产品', 1, 'alert.gif');
			   return;
		   }
		   for(var i=0; i<rows.length; i++){
			   cardidArr.push(rows[i].cardid);
		   }
			
		   cardids = cardidArr.join(',');
	   }else{ //单个智能卡购买
		   var selected = $('#usercardData').datagrid("getSelected");
	       if(selected == null){
	    	   $.dialog.tips('请选择需要购买产品的智能卡', 1, 'alert.gif');
			   return;
	       }
	       cardids = selected.cardid;
	   }
	   
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
		    content: 'url:usergroup/findProductListForDialog?cardids='+cardids+'&mothercardflag=0'
	   });
	}
     
    function closeProductDialog(){
		productDialog.close();
		//刷新购买中的产品
		$('#buyingproductData').datagrid('reload');	
	}
    
	//删除购买中的产品
	function deleteBuyingProduct(terminalid,productid){
	    var data = {
	    			terminalid: terminalid,
				    productid: productid
				   };
		var url="usergroup/deleteBuyingProduct?rid="+Math.random();
		$.getJSON(url,data,function(jsonMsg){
			if(jsonMsg.flag=="0"){
				$.dialog.tips(jsonMsg.error, 1, 'alert.gif');
			}else if(jsonMsg.flag=="1"){
				//刷新购买中的智能卡
				$('#buyingproductData').datagrid('reload');	
			}
		});
	}
	
	
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
	
	
	//保存设备购买
	var unitBusinessDialog;
	function unitBusiness() {
		
	   var rows = $('#buyingproductData').datagrid("getRows");
		   
	   if(rows ==null || rows =='' || rows.length<0){
		   $.dialog.tips('请先购买产品', 1, 'alert.gif');
		   return;
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
		    content: 'url:usergroup/unitBusiness?businesstype='+$('#businesstype').val()
		});
	 }
	
	function closeBusinessDialog(){
		//unitBusinessDialog.close();
		//loadUserInfo();
		//loadBuyingStbInfo();
		//loadBuyingCardInfo();
		//loadBuyingProductInfo();
		$("#addform").attr("action", "usergroup/businessUnit");
      	$("#addform").submit();
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
	         toolbar:'#usercardtools',
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
		var usercardid = $('#usercardid').val();
		//刷新
		$('#usercardlist').datagrid('reload',{
			queryuserid:'${user.user.id}',
			querycardid:usercardid
		});	
	}
</script>
</body>
</html>
