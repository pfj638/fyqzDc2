$(function(){
	load();// 每5秒钟刷新一下文档列表
	getKnowledgeDoces();
	$("#uploadBtn").click(function(){
		if($.trim($("#file").val())=="")
		{
			infoBox("请选择文档！");
			return ;
		}
		$.jBox.tip("正在上传，请稍后...");
		$("#uploadForm").ajaxSubmit({
			url : $("#rootPath").val()+"/bms/discuss_ajaxuploadDocument.action?discussManagerForm.discussId="+$("#discussId").val(),
			type : "post",
			dataType : "json",
			async : true,
			success : function(data, t) {
				var opRet = new OperResult(data);
				 if (opRet.isSuccess()) {
						$.jBox.tip("上传成功！", "success", {
							closed : function() {
								getKnowledgeDoces();
							}
						});
		            }else{
		            	$.jBox.tip("上传失败！", "error");
		            }
			},
			error : function() {
				errorBox("出錯了");
			}
		});
	});
});
/**
 * 加载当前节点所有文档
 */
function getKnowledgeDoces()
{
	var rootPath = $("#rootPath").val();
	$.ajax({
		url:rootPath+"/bms/discuss_ajaxfindDocumentByDiscussId.action?discussManagerForm.discussId="+$("#discussId").val(),// 请求地址
		type:"POST",// 请求方式 ,get/post
		async:true,// 同步/异步
		dataType:"json",// 请求回来的数据格式
		success:function(data){// 请求成功后的回调函数	
			var dl = $("#discussUpload");
			var opRet = new OperResult(data);
            if (opRet.isSuccess()) {
            	var data1 = opRet.getData();
            	dl.find('tr').each(function(){ 
        	    	var demo=$(this).attr("id"); 	
        	    	if(demo=="dd"){
        	    		$(this).remove();
        	    	}
        	    });
            	$.each(data1, (function (i, item) {
            		var str="<tr  id=\"dd\">";
            		    str+="<td><a href=\"javascript:kdocDownload('"+item.id+"');\" style='color:blue'  >"+item.name+"</a></td>";
            			str+="<td><a href=\"javascript:deleteDocumentById('"+item.id+"');\" style='color:blue' >删除</a></td>";
            			str+="</tr>";
		              dl.append(str);
            	}));
            }
		}
	});
}

/**
 * 加载当前节点所有文档
 */
function deleteDocumentById(documentId)
{
	var rootPath = $("#rootPath").val();
	$.ajax({
		url:rootPath+"/bms/discuss_ajaxdeleteDocumentById.action?discussManagerForm.documentId="+documentId,// 请求地址
		type:"POST",// 请求方式 ,get/post
		async:true,// 同步/异步
		dataType:"json",// 请求回来的数据格式
		success:function(data){// 请求成功后的回调函数
			var opRet = new OperResult(data);
			 if (opRet.isSuccess()) {
					$.jBox.tip("删除成功！", "success", {
						closed : function() {
							getKnowledgeDoces();
						}
					});
	            }else{
	            	$.jBox.tip("删除失败！", "error");
	            }
		}
	});
}
function kdocDownload(id)
{
	var rootPath = $("#rootPath").val();
	$.ajax({
		url:rootPath+"/bms/discuss_ajaxHasFile.action?discussManagerForm.documentId="+id,// 请求地址
		type:"get",// 请求方式 ,get/post
		async:true,// 同步/异步
		dataType:"json",// 请求回来的数据格式
		success:function(data){// 请求成功后的回调函数
			var opRet = new OperResult(data);
			 if (opRet.isSuccess()) {
					window.location.href=rootPath+"/bms/discuss_downloadFile.action?discussManagerForm.documentId="+id;
	            }else{
	            	$.jBox.tip("该文件不存在！", "error");
	            }
	    } 
	});
}

function save(){
	return true;
}
/**
 * 初始化，按照每5秒的间隔重新请求后台，刷新页面
 */
function load(){
	 window.setInterval("getKnowledgeDoces()",5000);
}
//初始化提交
function initDiscussAdd() {
	$("#discussAddForm").submit();
}
