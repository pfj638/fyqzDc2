$(function () {
	$("#checkAgree_id").click(function(){
		$("#checkType").val("true");
		check();
	});
	$("#checkRefuse_id").click(function(){
		$("#checkType").val("false");
		check();
	});
});

function check(){
	var data = $("#checkForm").serialize();
	var rootPath = $("#rootPath").val();
    $.ajax({
        url: rootPath+"/discuss_ajaxCheck.action",//请求地址
        type: "POST",//请求方式 ,get/post
        data: data,
        async: true,//同步/异步
        dataType: "json",//请求回来的数据格式
        success: function (result) {//请求成功后的回调函数
        	var opRet = new OperResult(result);
            if (opRet.isSuccess()) {
            	$.jBox.tip("研讨授权成功！", "success", {
					closed : function() {
						toCheckList();
					}
				});
            }else{
            	errorBox("研讨授权出现错误！");
            }
        }
    });
}

function toCheckList(){
	var rootPath = $("#rootPath").val();
	window.location.href = rootPath+"/discuss_queryCheckDiscuss.action";
}