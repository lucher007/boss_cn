<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
    .fb-con table tr {
      height: 30px;
    }
    .service table tr td {
      height: 30px;
      width: 12.5%;
    }
  </style>
</head>

<body>
<div id="body">
  <div class="cur-pos"><spring:message code="page.currentlocation"/>：<spring:message code="集团业务"/> &gt; <spring:message code="集团订户开户"/></div>
  <form method="post" id="addForm" name="addForm">
    <div class="form-box">
      <div class="fb-con">
        <div id="openUser" class="easyui-panel" title="<spring:message code="user.useradd"/>" style="width:100%">
        	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
		          <tr>
		            <td height="30" width="15%" align="right"><spring:message code="network.netname"/>：</td>
		            <td width="35%">
			             <input id="netid" name="netid" style="width: 200px;">
			              <span class="red">*</span>
		            </td>
		            <td height="30" width="15%" align="right"><spring:message code="area.areacode"/>：</td>
					<td width="35%" >
						<input id="areacode" name="areacode" style="width:200px;" >
						<span class="red">*</span>
					</td>
		          </tr>
		          <tr>
		            <td height="30" width="15%" align="right">订户类型：</td>
		            <td width="15%" style="font-weight: bold;">
		            	集团订户
		            	 <input type="hidden" readonly="readonly" id="usertype" name="usertype" value="1">
		            </td>
		           
		          	<td align="right">订户级别：</td>
		            <td>
		            	<input id="userlevelid" name="userlevelid" style="width:200px;" >
		            	<span class="red">*</span>
		            </td>
		          </tr>
		          <tr>
		            <td align="right"><spring:message code="user.username"/>：</td>
		          	<td>
						<input type="text" class="inp" name="username"  id="username" 
						maxlength="50" style="width:200px;"
						value="${user.username }">
					</td>
					<td align="right">联系电话：</td>
		            <td >
		            	<input type="text" class="inp" name="telephone" id="telephone" 
		            	maxlength="15" style="width:200px;"
		            	onkeyup="checkNum(this)" onkeypress="checkNum(this)" onblur="checkNum(this)"
		            	onafterpaste="this.value=this.value.replace(/\D/g,'')" 
		            	value="${user.telephone }">
		            </td>
					<%-- <td align="right"><spring:message code="user.mobile"/>：</td>
		            <td >
		            	<input type="text" class="inp" name="mobile" id="mobile" 
		            	maxlength="15" style="width:200px;"
		            	onkeyup="checkNum(this)" onkeypress="checkNum(this)" onblur="checkNum(this)"
		            	onafterpaste="this.value=this.value.replace(/\D/g,'')" 
		            	value="${user.mobile }">
		            </td> --%>
		          </tr>
		          <tr>
		            <td align="right"><spring:message code="user.documenttype"/>：</td>
		            <td >
		            	<input type="text" class="inp" maxlength="30" name="documenttype" id="documenttype" style="width:200px;" value="${user.documenttype }">
		            </td>
		            <td align="right"><spring:message code="user.documentno"/>：</td>
		          	<td>
						<input type="text" class="inp" name="documentno" id="documentno"  
						maxlength="50" style="width:200px;"
						value="${user.documentno }">
					</td>
		          </tr>
		          <tr>
		            <td align="right"><spring:message code="user.email"/>：</td>
		          	<td>
						<input type="text" class="inp" maxlength="50" name="email" id="email" style="width:200px;" value="${user.email }">
					</td>
					<td align="right"><spring:message code="user.password"/>：</td>
		          	<td>
						<input type="text" class="inp" name="password" id="password" 
						maxlength="6" style="width:200px;"
						value="${user.password }">
					</td>
		          </tr>
		          <tr>
		            <td align="right"><spring:message code="user.address"/>：</td>
		            <td>
		            	<input type="text" class="inp" style="width:400px;" name="address" id="address" 
		            	maxlength="100" 
		                value="${user.address }">
		            </td>
		            <td align="right"><spring:message code="systempara.remark"/>：</td>
		          	<td>
						<input type="text" class="inp" name="remark" id="remark" 
						maxlength="100" style="width:200px;"
						value="${user.remark }">
					</td>
		          </tr>
		        </table>
        </div>
        <!-- 
        <div style="margin:10px 0;"></div>
        <div id="openUser" class="easyui-panel" title="<spring:message code="购买中的智能卡"/>" style="width:100%" 
        	data-options="tools:'#tt'">
        	<table title="<spring:message code="package.content"/>" id="tg"></table>
        </div>
        <div id="tt">
			<a href="javascript:void(0)" class="icon-add" onclick="javascript:openWindow('card');"></a>
		</div>
      </div>
       -->
      <!--智能卡卡窗口-->			
	<div id="card" class="easyui-window" title="<spring:message code="package.service.card"/>" closed="true" style="width:400px;height:200px;">
		<form style="padding:10px 20px 10px 40px;">
			<table width="100%">
					<tr height="30px">
						<td align="right" width="120px"><spring:message code="package.card.cardtype"/>：</td>
						<td>
							 <select id="mothercardflag" name="mothercardflag" class="select">
								 <option value="0"><spring:message code="母卡"/></option>
				                 <option value="1"><spring:message code="子卡"/></option>
				             </select>
						</td>
					</tr>
					<tr height="30px">
						<td align="right" width="120px"><spring:message code="高标清标识"/>：</td>
						<td>
							 <select id="cardflag" name="cardflag" class="select">
								 <option value="0"><spring:message code="高清"/></option>
				                 <option value="1"><spring:message code="标清"/></option>
				             </select>
						</td>
					</tr>
					<tr height="30px">
						<td align="right"><spring:message code="开始卡号"/>：</td>
						<td>
							<input type="text" class="inp w200" name="startcardid" id="startcardid"
								maxlength="16" onkeyup="checkNum(this)" onkeypress="checkNum(this)" onblur="checkNum(this)" onafterpaste="this.value=this.value.replace(/\D/g,'')"> <span class="red">*</span>
						</td>
					</tr>
					<tr height="30px">
						<td align="right"><spring:message code="结束卡号"/>：</td>
						<td>
							<input type="text" class="inp w200" name="endcardid" id="endcardid"
								maxlength="16" onkeyup="checkNum(this)" onkeypress="checkNum(this)" onblur="checkNum(this)" onafterpaste="this.value=this.value.replace(/\D/g,'')"> <span class="red">*</span>
						</td>
					</tr>
			</table>
			<div style="margin:20px 0;"></div>
			<div style="padding:5px;text-align:center;">
				<a onclick="saveCard(this);" style="width:60px" class="easyui-linkbutton"><spring:message code="page.save"/></a>
				<a onclick="closeWindow(this);" style="width:60px" class="easyui-linkbutton"><spring:message code="page.cancel"/></a>
			</div>
		</form>
	</div>
      
      
      <div class="fb-bom">
        <cite>
          <c:if test="${user.id == null || user.id == ''}">
          	<input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="saveUser();"/>
          </c:if>
          <c:if test="${user.id != null}">
          	<input type="button" class="btn-next" value="<spring:message code="page.next"/>" onclick="nextStep();"/>
          </c:if>
          <!-- 
          <input type="button" class="btn-save" value="<spring:message code="page.save"/>" onclick="saveUser();"/>
          -->
        </cite>
        <span class="red">${user.returninfo}</span>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript" src="js/common/jquery.js"></script>
<script type="text/javascript" src="js/form.js"></script>
<script type="text/javascript" src="js/plugin/lhgdialog/lhgdialog.js?self=true&skin=iblue"></script>
<script type="text/javascript" src="js/common/jquery.easyui.min.js"></script>
<script type="text/javascript">

  //判断是否为数字
  function checkNumberChar(ob) {
    if (/^\d+$/.test(ob)) {
      return true;
    } else {
      return false;
    }
  }
  
  function checkAll() {
    $(':checkbox').attr('checked', !!$('#checkall').attr('checked'));
  }

  function checkbox() {
    var checked = true;
    $('.checkbox').each(function () {
      if (!$(this).attr('checked')) {
        return checked = false;
      }
    });
    $('#checkall').attr('checked', checked);
  }
  
  //
  function changeNetwork(){
	$("#addForm").attr("action", "usergroup/addInit");
	$("#addForm").submit();
  }	
  
  function nextStep(){
	$("#addForm").attr("action", "usergroup/businessUnit?businesstype=buyDevice");
	$("#addForm").submit();
  }	
  
  function saveUser() {
        if ($('#netid').combobox('getValue') == '') {
	      $.dialog.tips('<spring:message code="user.netid.empty"/>', 1, 'alert.gif');
	      $('#netid').focus();
	      return;
	    }
	    if ($('#areacode').combotree('getValue') == '') {
	      $.dialog.tips('<spring:message code="user.areacode.empty"/>', 1, 'alert.gif');
	      $('#areacode').focus();
	      return;
	    }
	    if ($('#userlevelid').combobox('getValue') == '') {
	      $.dialog.tips('订户级别不能为空', 1, 'alert.gif');
	      $('#netid').focus();
	      return;
	    }
	    $("#addForm").attr("action", "usergroup/save");
	    $("#addForm").submit();
  }
  
  function goBack() {
      $("#addForm").attr("action", "usergroup/findByList");
      $("#addForm").submit();
  }
  
  $(function () {
       initNetwork();
       initUserlevel();
       var returninfo = '${user.returninfo}';
       if (returninfo != '') {
        	$.dialog.tips(returninfo, 1, 'alert.gif');
       }
  });
  
  function initNetwork(){
		   $('#netid').combobox({    
				url:'usergroup/initNetworkJson?rid='+Math.random(),
			    valueField:'id',   
			    editable:false,
			    //limitToList:true,
			    textField:'netname',
		        onSelect: function(rec){    
	               initArea(rec.id);//默认加载区域
	        	},
			    onLoadSuccess:function(node, data){
				    	//初始化营业厅列表框的默认选择值
				    	
				    	if('${Operator.netid}' != '' && '${Operator.netid}' != null){
						  	  $(this).combobox('select',parseInt('${Operator.netid}'));
						}else{//默认选择第一个
							var val = $('#netid').combobox("getData");  
		                    for (var item in val[0]) {  
		                        if (item == "id") {  
		                            $(this).combobox("select", val[0][item]);  
		                        }  
		                     }  	
						} 
	                    //initArea($("#querynetid").combobox("getValue")); //默认加载区域
		    			//initStore('${operator.netid}');//默认加载营业厅 
				    }
			});  
		   
	    }
  
	    function initArea(netid) {
		  $('#areacode').combotree({   
			    url:'usergroup/getAreaTreeJson?querynetid='+netid+'&rid='+Math.random(),
			    //数据执行之后才加载
			    onLoadSuccess:function(node, data){
			    	if('${user.areacode}' != '' && '${user.areacode}' != null){
					  	  var node = $('#areacode').combotree('tree').tree('find', '${user.areacode}');
					  	  if(node != null){
					  	  	$('#areacode').combotree('setValue', node.id);
					  	  }else{
					  	  	$('#areacode').combotree('setValue', '');
					  	  }
				     }else{
				     	$('#areacode').combotree('setValue', '');
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
			           //$("#areacode").treegrid("unselect");
			           return false;
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
	  
	  function initUserlevel(){
		   $('#userlevelid').combobox({    
				url:'usergroup/initUserlevelJson?rid='+Math.random(),
			    valueField:'id',   
			    editable:false,
			    //limitToList:true,
			    textField:'name',
		        onSelect: function(rec){    
	        	},
			    onLoadSuccess:function(node, data){
				    	//初始化默认选择值
						var val = $('#userlevelid').combobox("getData");  
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
			
			//智能卡先要判断网络
			if(service_name == 'card'){
				if ($('#netid').combobox('getValue') == '') {
		 		      $.dialog.tips('<spring:message code="card.netid.empty"/>', 1, 'alert.gif');
		 		      $('#netid').focus();
		 		      return;
	 		    }
			}
			$('#'+service_name).window('open');
		}
	   
	    //关闭弹出框
		function  closeWindow(obj){
			$(obj).parents('.easyui-window').window('close');
		}
	    
		function saveCard(obj){
			alert($('#netid').combobox('getValue'));
			alert($('#mothercardflag').val());
			alert($('#cardflag').val());
			alert($('#startcardid').val());
			alert($('#endcardid').val());
			var data = {
					netid:$('#netid').combobox('getValue'),
					mothercardflag:$('#mothercardflag').val(),
					cardflag: $('#cardflag').val(),
					startcardid: $('#startcardid').val(),
					endcardid: $('#endcardid').val(),
				  };
				var url="usergroup/addBuyingCard?rid="+Math.random();
				$.getJSON(url,data,function(jsonMsg){
					if(jsonMsg.flag=="0"){
					    $.dialog.tips(jsonMsg.error, 1, 'alert.gif');
					}else if(jsonMsg.flag=="1"){
						parent.closeCardDialog();
					}
				});
		}
</script>
</body>
</html>
