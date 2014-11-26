$(function(){
	showSelects();
	var rootPath = $("#rootPath").val();
	
	$("#addUser_id").click(function(){
		window.location.href=rootPath+"/forward_toUserNew.action";
	});
	$("#userSelect_id").click(function(){
		$("#condition").val($("#query_id").serialize());
		$("#query_id").submit();
	});
	
});

/**
 * 加载用户类型、用户职称、用户班级、用户届数的字典表
 */
function showSelects(){
    var rootPath = $("#rootPath").val();
    $.ajax({
        url: rootPath+"/user_ajaxQueryInfo.action",//请求地址
        type: "POST",//请求方式 ,get/post
        async: true,//同步/异步
        dataType: "json",//请求回来的数据格式
        success: function (result) {//请求成功后的回调函数
        	var opRet = new OperResult(result);
            if (opRet.isSuccess()) {
            	drawOption("grade_id", opRet.getData().grade);
            	drawOption("userType_id", opRet.getData().userType);
            	drawOption("professionalTitle_id", opRet.getData().professionalTitle);
            	defaultCondition();
            }else{
            	errorBox("用户字典信息加载错误！");
            }
        }
    });
}

function drawOption(selectId,data){
	var select = $("#"+selectId);
	data.forEach(function(item,index){
		if(item){
			$("<option>",{
				value : item.id,
				text : item.name
			}).appendTo(select);
		}
	});
}

function defaultCondition(){
	/**
	 * 根据条件设置默认的查询项
	 */
	var condition = JSON.parse($("#condition").val());
	if(condition.realName){
		$("#realName_id").val(condition.realName);
	}
	$("#sex_id").find("option[value="+condition.sex+"]").attr("selected",true);
	$("#userType_id").find("option[value="+condition.userType+"]").attr("selected",true);
	$("#professionalTitle_id").find("option[value="+condition.professionalTitle+"]").attr("selected",true);
}
function modify(item){
	var rootPath = $("#rootPath").val();
	window.location.href=rootPath+"/user_toModify.action?userForm.userId="+$(item).attr("name");
}
function deleteUser(item){
	confirmBox("请确定是否删除？", function(y){
		if(y=="ok"){
			ajaxDelete(item);
		}
	});
}
function roleSetup(item){
	var rootPath = $("#rootPath").val();
	window.location.href=rootPath+"/user_toRoleSetup.action?userForm.userId="+$(item).attr("name");
}

function ajaxDelete(item){
	var rootPath = $("#rootPath").val();
	$.ajax({
		url:rootPath+"/user_ajaxDelete.action?userForm.userId="+$(item).attr("name"),//请求地址
		type:"POST",//请求方式 ,get/post
		async:true,//同步/异步
		dataType:"json",//请求回来的数据格式
		success:function(data){//请求成功后的回调函数
            var opRet = new OperResult(data);
			if (opRet.isSuccess()) {
				$.jBox.tip("用户删除成功！", "success", {
					closed : function() {
						queryAgain();
					}
				});
			} else {
				errorBox("用户删除失败！");
			}
		},
		error:function(){
			
		}
	});
}
