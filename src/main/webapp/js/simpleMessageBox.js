/**
 * 带有感叹号“！”的提示框<br>
 * 
 * @param message
 */
function infoBox(message) {
	$.jBox.info(message, "<div style=\"text-align:left;\">提示</div>", {
		showIcon : false,
		top : "30%",
		height : 150
	});
}

/**
 * 不带任何图标的提示框<br>
 * 
 * @param message
 */
function alertBox(message) {
	$.jBox.alert(message, "<div style=\"text-align:left;\">提示</div>", {
		showIcon : false,
		top : "30%",
		height : 150
	});
}

/**
 * 带有对勾的提示框(表示成功)<br>
 * 
 * @param message
 */
function successBox(message) {
	$.jBox.success(message, "<div style=\"text-align:left;\">提示</div>", {
		showIcon : false,
		top : "30%",
		height : 150
	});
}

/**
 * 代表错误的提示框<br>
 * 
 * @param message
 */
function errorBox(message) {
	$.jBox.error(message, "<div style=\"text-align:left;\">提示</div>", {
		showIcon : false,
		top : "30%",
		height : 150
	});
}

/**
 * 确认提示框<br>
 * fn 参数为一个方法，方法中有3个参数，按照顺序分别为以下参数：<br>
 * 1、 y 表示所点的按钮的返回值，例如：{'确认':'ok','取消':'cancel'},那么在点击了'确认'按钮以后，此变量将返回“OK”<br>
 * 2、 h 表示窗口内容的jQuery对象<br>
 * 3、 f 表示窗口内容里的form表单键值<br>
 * 
 * @param message
 * @param fn
 */
function confirmBox(message, fn) {
	$.jBox.confirm(message, "<div style=\"text-align:left;\">提示</div>", fn,{
		showIcon : false,
		top : "40%"
	});
}