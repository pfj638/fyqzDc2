//记录鼠标是否按下
var isClick = false;
//按下鼠标时候的坐标
var defaultX;
var defaultY;
//移动时候的坐标
var mouseX;
var mouseY;
//移动层距离上边和左边的距离

$(function(){
	//按下鼠标
	$(".moveDiv").mousedown(function(e){
		isClick = true;
		defaultX = e.pageX;
		defaultY = e.pageY;
		DivTop = $(".moveTxt").css("top");
		DivLeft = $(".moveTxt").css("left");
		DivTop = parseFloat(String(DivTop).substring(0,String(DivTop).indexOf("px")));
		DivLeft = parseFloat(String(DivLeft).substring(0,String(DivLeft).indexOf("px")));
	});
	//移动鼠标
	$(".moveDiv").mousemove(function(e){
		mouseX = e.pageX;
		mouseY = e.pageY;
		if(isClick && mouseX > 0 && mouseY > 0){
			var newTop = parseFloat(mouseY - defaultY);
			var newLeft = parseFloat(mouseX - defaultX);
			$(".moveTxt").css({"top":newTop+DivTop});
			$(".moveTxt").css({"left":newLeft+DivLeft});
		}
	});
	//松开鼠标
	$(".moveDiv").mouseup(function(e){
		isClick = false;
	});
});
