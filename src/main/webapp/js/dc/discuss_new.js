var ALLPARTICIPATE = [];
var ALLSELECT = [];
var participate = function(){
		this.id = null;
		this.loginName = null;
		this.jobTitle = null;
		this.type = null;
		this.discussRole = null;
		this.professionalTitle = null;
}
$(function () {

    $("#startDate_id").focus(function () {
        WdatePicker({
            doubleCalendar: false,
            minDate: "%y-%M-#{%d} 00:00:00",
            dateFmt: "yyyy-MM-dd HH:mm:ss",
            highLineWeekDay: true,
            readOnly:true,
            onpicked: function () {
                var strDate = $dp.cal.getP('y') + "-" + $dp.cal.getP('M') + "-" + $dp.cal.getP('d') + " " + $dp.cal.getP('H') +
                    ":" + $dp.cal.getP('m') + ":" + $dp.cal.getP('s');// 得到字符串的时间
            }
        });
    });
    
    $("#addDiscuss").click(function () {
    	var validRet = $("#addForm").valid();
        if (!validRet) {
        	errorBox("您有选项未正确填写！");
            return;
        }
    	ALLPARTICIPATE.forEach(function(item,index){
    		var value = $("#participate"+index).val();
    		var expertNumber = 0;
    		var viewNumber = 0;
    		if(value == "1"){
    			var expertList = '<input type="hidden" value="' +
    			ALLPARTICIPATE[index].id + '" name="discussForm.expert" />';
    			$("#addForm").append(expertList);
    			expertNumber++;
    		}else if(value == "2"){
    			var viewList = '<input type="hidden" value="' +
    			ALLPARTICIPATE[index].id + '" name="discussForm.host" />';
    			$("#addForm").append(viewList);
    		}else if(value == "3"){
    			var viewList = '<input type="hidden" value="' +
    			ALLPARTICIPATE[index].id + '" name="discussForm.view" />';
    			$("#addForm").append(viewList);
    			viewNumber++;
    		}
    	});
        add();
    });
    
    $("#userSelect_id").click(function(){
    	ALLPARTICIPATE.length = 0;
    	ALLSELECT.forEach(function(item,index){
    		ALLPARTICIPATE.push(item);
    	});
    	document.getElementById("tab_3").style.display="none";
    	showSelected(ALLPARTICIPATE);
    });
    
    $("#selectAll_check").click(function(){
    	var checkboxs = $("#userList_id").find($("input[type=checkbox]"));
    	if($("#selectAll_check")[0].checked){
    		ALLSELECT.length = 0;
    		$.each(checkboxs,function(index,item){
    			if(index > 0){
    				item.checked = true;
    				checkbox(item);
    			}
    		});
    	}else{
    		$.each(checkboxs,function(index,item){
    			if(index > 0){
    				item.checked = false;
    				checkbox(item);
    			}
    		});
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

    jQuery.validator.addMethod("description_validate", function (value, element) {

        return this.optional(element) || (value.length<500);
    }, "案例描述不能超过500字符!");
});
function show(){
	document.getElementById("tab_3").style.display="block";
	queryUser();
}
function hiden_tab(){
	document.getElementById("tab_3").style.display="none";
	ALLSELECT.length = 0;
	ALLPARTICIPATE.forEach(function(item,index){
		ALLSELECT.push(item);
	});
}
//为新增人员查找用户
function queryUser() {
	var rootPath = $("#rootPath").val();
    $.ajax({
        url: rootPath+"/user_ajaxQueryAllUser.action",//请求地址
        type: "POST",//请求方式 ,get/post
        async: true,//同步/异步
        dataType: "json",//请求回来的数据格式
        success: function (result) {//请求成功后的回调函数
        	var table = $("#userList_id");
        	while(table[0].rows.length > 1){
        		table[0].deleteRow(1);
        	}
        	var opRet = new OperResult(result);
            if (opRet.isSuccess()) {
            	var data = opRet.getData();
            	console.debug(data);
            	$.each(data, (function (i, item) {
            		table.append(showQuery(item));
            	}));
            }
            defalutSelect();
        }
    });
}

/*返回用户列表tr*/
function showQuery(item){
	var tr = $("<tr></tr>");
	$("<td>",{}).append($("<input>",{
		type : 'checkbox',
		'onclick' : 'checkbox(this)',
		name : 'users',
		value : item.id
	})).appendTo(tr);
	$("<td>",{
		text : item.loginName
	}).appendTo(tr);
	$("<td>",{
		text : item.jobTitle
	}).appendTo(tr);
	$("<td>",{
		text : item.professionalTitle.name
	}).appendTo(tr);
	$("<td>",{
		text : item.type.name
	}).appendTo(tr);
	return tr;
}

function showSelected(data){
	var table = $("#participateList_id");
	while(table[0].rows.length > 1){
		table[0].deleteRow(1);
	}
	
	var select = $("<select>",{
		"class" : "text width_1",  
		size : "1", 
		style : "width:87px"
	});
	$("<option>",{
		text : "研讨人员",
		value : 1
	}).appendTo(select);
	$("<option>",{
		text : "主持人",
		value : 2
	}).appendTo(select);
	$("<option>",{
		text : "观摩人员",
		value : 3
	}).appendTo(select);
	data.forEach(function(item,index){
		var tr = $("<tr></tr>");
		$("<td>",{
			text : item.loginName
		}).appendTo(tr);
		$("<td>",{
			text : item.jobTitle
		}).appendTo(tr);
		$("<td>",{
			text : item.type
		}).appendTo(tr);
		$("<td>",{
			text : item.professionalTitle
		}).appendTo(tr);
		$("<td>").append(select.clone().attr("id","participate"+index)).appendTo(tr);
		table.append(tr);
	});
	
	if(table[0].rows.length == 1){
		table.append("<tr><td></td><td></td><td></td><td></td><td></td></tr>");
	}

}

//默认选中
function defalutSelect() {
    var box = document.getElementsByName("users");
    $.each(box, (function (i, item) {
        if (checkSelect(item)) {
            item.checked = true;
        }
    }));
}
//根据用户id检查用户是否被选中
function checkSelect(box) {
    for (var i = 0; i < ALLSELECT.length; i++) {
        if (box.value == ALLSELECT[i].id) {
            return true;
        }
    }
    return false;
}
function checkbox(item) {
    var user = new participate();
    user.id=item.value;
    user.loginName = $(item).parent().next().text();
    user.jobTitle = $(item).parent().next().next().text();
    user.professionalTitle = $(item).parent().next().next().next().text();
    user.type = $(item).parent().next().next().next().next().text();
    if (item.checked) {
        ALLSELECT.push(user);
    } else {
        for (var i = 0; i < ALLSELECT.length; i++) {
            if (item.value == ALLSELECT[i].id) {
                ALLSELECT.splice(i, 1);
            }
        }
    }
}

function add(){
	var data =$('#addForm').serialize();
    var rootPath = $("#rootPath").val();
    $.ajax({
        url: rootPath+"/discuss_ajaxAdd.action",//请求地址
        type: "POST",//请求方式 ,get/post
        data: data,
        async: true,//同步/异步
        dataType: "json",//请求回来的数据格式
        success: function (result) {//请求成功后的回调函数
        	var opRet = new OperResult(result);
            if (opRet.isSuccess()) {
            	$.jBox.tip("研讨申请成功！", "success", {
					closed : function() {
						toInitList();
					}
				});
            }else{
            	errorBox("研讨申请出现错误！");
            }
        },
        error: function(){
        	alert("sffd");
        }
    });
}

function toInitList(){
	var rootPath = $("#rootPath").val();
	window.location.href = rootPath+"/bms/discuss_discussInitList.action";
}