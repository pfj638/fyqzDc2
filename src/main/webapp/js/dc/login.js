$(function () {
	$("#loginName").focus();
    $("#loginBtn").click(function () {
        login();
    });
    //给表单添加回车登录的功能
    $("form:first").keydown(function (e) {
        if (e.which == 13) {
            login();
        }
    });
    //取消当前window的鼠标按下时候出发的事件
    $(window).keydown(function (e) {
        if (e.which == 13) {
            return false;
        }
    });
    
    jQuery.extend(jQuery.validator.messages, {
        required: "*必填",
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

    $("#login_form_id").validate({
        onfocusout: function (elment) {
            $(elment).valid();
        },
        rules: {

        },
        messages: {

        },
        meta: "validate",
        errorClass: "invalid",
        errorPlacement: function (error, element) {
            error.appendTo($("#msg"));
        }
    });

    jQuery.validator.addMethod("userName_validate", function (value, element) {
        var name = /^[a-zA-Z0-9]{1,10}$/;
        return this.optional(element) || (name.test(value));
    }, "用户名称不能超过50字符!");

    jQuery.validator.addMethod("password_validate", function (value, element) {
        var name = /^[a-zA-Z0-9]{1,10}$/;
        return this.optional(element) || (name.test(value));
    }, "用户密码不能超过10字符!");

});
//实现登录
function login() {
    var urlPath=$('#urlPath').val();
    var validRet = $("#login_form_id").valid();
    if (!validRet) {
        return;
    }
    $.ajax({
        url: urlPath+"/user_ajaxLogin.action",//请求地址
        type: "POST",//请求方式 ,get/post
        data: $("#login_form_id").serialize(),
        async: true,//同步/异步
        dataType: "json",//请求回来的数据格式
        success: function (data) {//请求成功后的回调函数
            var opRet = new OperResult(data);
            if (opRet.isServerRespNotNull() && opRet.isSuccess()) {
                window.location.href = urlPath + "/forward_toMain.action";
            } else {
                infoBox(opRet.getDesc());
            }
        }
    });
}

