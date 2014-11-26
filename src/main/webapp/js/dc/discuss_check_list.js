
function checkDetail(item){
	var rootPath = $("#rootPath").val();
	var discussId = $(item).attr("name");
	window.location.href = rootPath+"/discuss_detail.action?discussForm.discussId="+discussId;
}
