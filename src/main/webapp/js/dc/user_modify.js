$(function(){
	showSelects();
	$("#modifyUser").click(function(){
		modifyUser();
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
            	defaultSelect();
            }else{
            	errorBox("研讨申请出现错误！");
            }
        }
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
	
	$("#modifyForm").validate({
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
}

function drawOption(selectId,data){
	var select = $("#"+selectId);
	select.empty();
	data.forEach(function(item,index){
		if(item){
			$("<option>",{
				value : item.id,
				text : item.name
			}).appendTo(select);
		}
	});
}

function defaultSelect(){
	var userInfo = $("#userInfo").val();
	var data = JSON.parse(userInfo);
	console.debug(data);
	var sex = $("#sex_id");
	selectedOption(sex,data.sex);
	var year = $("#year_id");
	selectedOption(year,data.year);
	var professionalTitle = $("#professionalTitle_id");
	selectedOption(professionalTitle,data.professionalTitle.id);
	var userType = $("#userType_id");
	selectedOption(userType,data.type.id);
	var grade = $("#grade_id");
	selectedOption(grade,data.grade.id);
}

function selectedOption(selector,data){
	var options = $(selector).find("option");
	options.each(function(index,item){
		if(item.value == data){
			item.selected = true;
		}
	});
	//$(selector).find("option[value="+data+"]").attr("selected",true);
}

function modifyUser(){
	var data =$('#modifyForm').serialize();
    var rootPath = $("#rootPath").val();
    $.ajax({
        url: rootPath+"/user_ajaxModify.action",//请求地址
        type: "POST",//请求方式 ,get/post
        data: data,
        async: true,//同步/异步
        dataType: "json",//请求回来的数据格式
        success: function (result) {//请求成功后的回调函数
        	var opRet = new OperResult(result);
            if (opRet.isSuccess()) {
            	$.jBox.tip("用户信息修改成功！", "success", {
					closed : function() {
						toUserManager();
					}
				});
            }else{
            	errorBox("用户休息修改出现错误！");
            }
        }
    });
}


function toUserManager(){
	var rootPath = $("#rootPath").val();
	window.location.href = rootPath+"/user_queryUserByPage.action";
}