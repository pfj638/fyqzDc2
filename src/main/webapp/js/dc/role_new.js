$(function () {
	
	$("#addRole").click(function(){
		var validRet = $("#addForm").valid();
        if (!validRet) {
        	errorBox("请正确填写信息！");
            return;
        }
		addRole();
	});
	
	jQuery.extend(jQuery.validator.messages, {
	    required: "该项为必填项！",
	    remote: "请修正该字段！",
	    email: "邮件格式错误！",
	    url: "请输入合法的网址！",
	    date: "请输入合法的日期！",
	    dateISO: "请输入合法的日期 (ISO)！",
	    number: "请输入合法的数字！",
	    digits: "只能数字！",
	    equalTo: "请再次输入相同的值！",
	    accept: "请输入拥有合法后缀名的字符串！",
	    maxlength: jQuery.validator.format("最长不能超过 {0}位数字!"),
	    minlength: jQuery.validator.format("最少不能少于 {0}位数字!"),
	    rangelength: jQuery.validator.format("有效期格式不正确!"),
	    range: jQuery.validator.format("有效期格式不正确!"),
	    ranges: jQuery.validator.format("请输入大于今年的数字!"),
	    max: jQuery.validator.format("请输入一个最大为{0} 的值!"),
	    min: jQuery.validator.format("请输入一个最小为{0} 的值!")
	});
	
	$("#addForm").validate({
	    onfocusout:function(elment){
	        $(elment).valid();
	    },
	    rules: {
	
	    },
	    messages: {
	
	    },
	    meta: "validate",
	    errorClass: "invalid",
	    errorPlacement: function (error, element) {
	        error.appendTo(element.parent());
	    }
	});
	
	// 案例名称的验证
	jQuery.validator.addMethod("title_validate", function (value, element) {
	    var name = /^[a-zA-Z0-9\u4E00-\u9FFF]{1,50}$/;
	    return this.optional(element) || (name.test(value));
	}, "研讨案例名称不能超过50字符!");
});

function addRole(){
	var data =$('#addForm').serialize();
    var rootPath = $("#rootPath").val();
    $.ajax({
        url: rootPath+"/role_ajaxAdd.action",//请求地址
        type: "POST",//请求方式 ,get/post
        data: data,
        async: true,//同步/异步
        dataType: "json",//请求回来的数据格式
        success: function (result) {//请求成功后的回调函数
        	var opRet = new OperResult(result);
            if (opRet.isSuccess()) {
            	$.jBox.tip("角色新增成功！", "success", {
					closed : function() {
						toRoleManage();
					}
				});
            }else{
            	errorBox("角色新增出现错误！");
            }
        }
    });
}
function toRoleManage(){
	var rootPath = $("#rootPath").val();
	window.location.href = rootPath+"/role_queryRoleByPage.action";
}