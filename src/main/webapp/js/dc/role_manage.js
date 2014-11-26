$(function(){
	var rootPath = $("#rootPath").val();
	
	$("#roleAdd_id").click(function(){
		window.location.href=rootPath+"/forward_toRoleNew.action";
	});
	$("#roleSelect_id").click(function(){
		$("#condition").val($("#query_id").serialize());
		$("#query_id").submit();
	});
	
});
function defaultCondition(){
	/**
	 * 根据条件设置默认的查询项
	 */
	var condition = JSON.parse($("#condition").val());
	if(condition.name){
		$("#name_id").val(condition.name);
	}
}
function modify(item){
	var rootPath = $("#rootPath").val();
	window.location.href=rootPath+"/role_toModify.action?roleForm.roleId="+$(item).attr("name");
}
function deleteRole(item){
	confirmBox("请确定是否删除？", function(y){
		if(y=="ok"){
			ajaxDelete(item);
		}
	});
}
function permissionSetup(item){
	var rootPath = $("#rootPath").val();
	window.location.href=rootPath+"/role_toPermissionSetup.action?roleForm.roleId="+$(item).attr("name");
}
function ajaxDelete(item){
	var rootPath = $("#rootPath").val();
	$.ajax({
		url:rootPath+"/role_ajaxDelete.action?roleForm.roleId="+$(item).attr("name"),//请求地址
		type:"POST",//请求方式 ,get/post
		async:true,//同步/异步
		dataType:"json",//请求回来的数据格式
		success:function(data){//请求成功后的回调函数
            var opRet = new OperResult(data);
			if (opRet.isSuccess()) {
				$.jBox.tip("角色删除成功！", "success", {
					closed : function() {
						queryAgain();
					}
				});
			} else {
				errorBox("角色删除失败！");
			}
		},
		error:function(){
			
		}
	});
}