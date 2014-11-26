
	 //启动案例研讨
	   function initDiscussStart(a){
		    var rootPath=$("#rootPath").val();
		   	  if(confirm("确认要启动该研讨案例吗？")){	
			     	$.ajax({
					url:rootPath+"/bms/discuss_ajaxaddStartDiscuss.action?discussManagerForm.discussId="+a,// 请求地址
					type:"POST",// 请求方式 ,get/post
					async:true,// 同步/异步
					dataType:"json",// 请求回来的数据格式
					success:function(data){// 请求成功后的回调函数	
						var opRet = new OperResult(data);
			            if (opRet.isSuccess()) {
								$.jBox.tip("启动成功！", "success", {
									closed : function() {
										 $("#discussForm").submit();
									}
								});
								
				            }else{
				            	$.jBox.tip("启动失败！", "error");
				            }
					}
				});
		     }
	     }
	    