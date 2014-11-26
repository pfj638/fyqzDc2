$(function () {
	$("#totalShow_id").text($("#totalCount").val());
	$("#indexShow_id").text($("#pageIndex").val());
	
	$("#sizeSelect_id").change(function(){
		$("#pageSize").val($("#sizeSelect_id").val());
		queryAgain();
	});
	
	$("#indexSelect_id").change(function(){
		$("#pageIndex").val($("#indexSelect_id").val());
		queryAgain();
	});
	
	var select = $("#sizeSelect_id");
	var options = select[0].getElementsByTagName("option");
	$(options).each(function(index,item){
		if($(item).val() == $("#pageSize").val()){
			$(item).attr("selected",true);
		}
	});
	
	var indexSelect = $("#indexSelect_id");
	for(var i=1;i<=$("#pageCount").val();i++){
		var option = $("<option>",{
			value : i,
			text : i
		}).appendTo(indexSelect);
		if(i == $("#pageIndex").val()){
			option.attr("selected",true);
		}
	}
	
});

function beforePage(){
	var pageIndex = $("#pageIndex").val();
	if(pageIndex > 1){
		pageIndex--;
		$("#pageIndex").val(pageIndex);
	}
	queryAgain();
}

function nextPage(){
	var pageIndex = $("#pageIndex").val();
	var pageCount = $("#pageCount").val();
	if(pageIndex < pageCount){
		pageIndex++;
		$("#pageIndex").val(pageIndex);
	}
	queryAgain();
}

function queryAgain(){
	$("#pageInfo_id").submit();
}