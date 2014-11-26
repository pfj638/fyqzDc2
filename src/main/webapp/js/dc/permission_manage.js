$(function(){
	var rootPath = $("#rootPath").val();
	
	$("#permissionAdd_id").click(function(){
		window.location.href=rootPath+"/forward_toPermissionNew.action";
	});
	$("#permissionSelect_id").click(function(){
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
	window.location.href=rootPath+"/permission_toModify.action?permissionForm.permissionId="+$(item).attr("name");
}
function deletePermission(item){
	confirmBox("请确定是否删除？", function(y){
		if(y=="ok"){
			ajaxDelete(item);
		}
	});
}
function roleSetup(item){
	var rootPath = $("#rootPath").val();
	window.location.href=rootPath+"/permission_toPermissionSetup.action?permissionForm.permissionId="+$(item).attr("name");
}
function ajaxDelete(item){
	var rootPath = $("#rootPath").val();
	$.ajax({
		url:rootPath+"/permission_ajaxDelete.action?permissionForm.permissionId="+$(item).attr("name"),//请求地址
		type:"POST",//请求方式 ,get/post
		async:true,//同步/异步
		dataType:"json",//请求回来的数据格式
		success:function(data){//请求成功后的回调函数
            var opRet = new OperResult(data);
			if (opRet.isSuccess()) {
				$.jBox.tip("权限删除成功！", "success", {
					closed : function() {
						queryAgain();
					}
				});
			} else {
				errorBox("权限删除失败！");
			}
		},
		error:function(){
			
		}
	});
}