$(function(){
	queryRole();
	
	$("#setupRole_id").click(function(){
		setupRole();
	});
});

function queryRole(){
	var rootPath = $("#rootPath").val();
	$.ajax({
		url:rootPath+"/role_ajaxQueryAll.action",//请求地址
		type:"POST",//请求方式 ,get/post
		async:true,//同步/异步
		dataType:"json",//请求回来的数据格式
		success:function(data){//请求成功后的回调函数
            var opRet = new OperResult(data);
			if (opRet.isSuccess()) {
				drawRole(opRet.getData());
				defaultSelect();
			} else {
				errorBox("角色信息获取失败！");
			}
		},
		error:function(){
			
		}
	});
}

function drawRole(data){
	var table = $("#roleList_id");
	while(table[0].rows.length > 1){
		table[0].deleteRow(1);
	}
	data.forEach(function(item,index){
		var tr = $("<tr></tr>");
		
		$("<td>").append($("<input>",{
			type : "radio",
			name : "userForm.role",
			value : item.id
		})).appendTo(tr);
		$("<td>",{
			text : item.name
		}).appendTo(tr);
		$("<td>",{
			text : item.roleDesc
		}).appendTo(tr);
		table.append(tr);
	});
}

function defaultSelect(){
	var table = $("#roleList_id");
	var userRole = $("#userRoleInfo").val();
	$(table).find("input[value="+userRole+"]").attr("checked",true);
}

function setupRole(){
	var rootPath = $("#rootPath").val();
	var data = $("#setupForm").serialize();
	$.ajax({
		url:rootPath+"/user_ajaxSetupRole.action",//请求地址
		type:"POST",//请求方式 ,get/post
		data:data,
		async:true,//同步/异步
		dataType:"json",//请求回来的数据格式
		success:function(data){//请求成功后的回调函数
            var opRet = new OperResult(data);
			if (opRet.isSuccess()) {
				$.jBox.tip("用户角色设置成功！", "success", {
					closed : function() {
						toUserManage();
					}
				});
			} else {
				errorBox("用户角色设置失败！");
			}
		},
		error:function(){
			
		}
	});
}
function toUserManage(){
	var rootPath = $("#rootPath").val();
	window.location.href = rootPath+"/user_queryUserByPage.action";
}