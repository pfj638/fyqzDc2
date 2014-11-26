$(function(){
	queryPermission();
	
	$("#setupPermission_id").click(function(){
		setupPermission();
	});
});

function queryPermission(){
	var rootPath = $("#rootPath").val();
	$.ajax({
		url:rootPath+"/permission_ajaxQueryAll.action",//请求地址
		type:"POST",//请求方式 ,get/post
		async:true,//同步/异步
		dataType:"json",//请求回来的数据格式
		success:function(data){//请求成功后的回调函数
            var opRet = new OperResult(data);
			if (opRet.isSuccess()) {
				drawPermission(opRet.getData());
				defaultSelect();
			} else {
				errorBox("角色信息获取失败！");
			}
		},
		error:function(){
			
		}
	});
}

function drawPermission(data){
	var table = $("#permissionList_id");
	while(table[0].rows.length > 1){
		table[0].deleteRow(1);
	}
	data.forEach(function(item,index){
		var tr = $("<tr></tr>");
		
		$("<td>").append($("<input>",{
			type : "checkbox",
			name : "roleForm.permission",
			value : item.value
		})).appendTo(tr);
		$("<td>",{
			text : item.name
		}).appendTo(tr);
		$("<td>",{
			text : item.permissionDesc
		}).appendTo(tr);
		table.append(tr);
	});
}

function defaultSelect(){
	var table = $("#permissionList_id");
	var rolePermission = $("#rolePermissionInfo").val();
	var checkboxs = $(table).find("input[name='roleForm.permission']");
	checkboxs.each(function(index,item){
		if((parseInt(rolePermission) & parseInt(item.value)) == item.value){
			item.checked = true;
		}
	});
}

function setupPermission(){
	var rootPath = $("#rootPath").val();
	var permission = 0;
	var table = $("#permissionList_id");
	var checkboxs = $(table).find("input:checked");
	checkboxs.each(function(index,item){
		permission += parseInt(item.value);
	});
	$("<input>",{
		type : "hidden",
		value : permission,
		name : "roleForm.permission"
	}).appendTo($("#setupForm"));
	var data = $("#setupForm").serialize();
	$.ajax({
		url:rootPath+"/role_ajaxSetupPermission.action",//请求地址
		type:"POST",//请求方式 ,get/post
		data:data,
		async:true,//同步/异步
		dataType:"json",//请求回来的数据格式
		success:function(data){//请求成功后的回调函数
            var opRet = new OperResult(data);
			if (opRet.isSuccess()) {
				$.jBox.tip("角色权限设置成功！", "success", {
					closed : function() {
						toRoleManage();
					}
				});
			} else {
				errorBox("角色权限设置失败！");
			}
		},
		error:function(){
			
		}
	});
}
function toRoleManage(){
	var rootPath = $("#rootPath").val();
	window.location.href = rootPath+"/role_queryRoleByPage.action";
}