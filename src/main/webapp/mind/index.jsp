<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
		<title>My Mind</title>
		<link rel="icon" href="<%=path %>/icons/favicon.ico" />
		<script src="<%=path %>/js/mind/my-mind.js"></script>
		<script src="https://cdn.firebase.com/v0/firebase.js"></script>
		<script src="https://cdn.firebase.com/v0/firebase-simple-login.js"></script>
		<link rel="stylesheet" href="<%=path %>/css/mind/font.css" />
		<link rel="stylesheet" href="<%=path %>/css/mind/style.css" />
		<link rel="stylesheet" href="<%=path %>/css/mind/print.css" media="print" />
  </head>
  
  <body>

   	<div id = "associatePort">
	<div id="associateTo" style="display:none;"></div>
	<div id="associateFrom" style="display:none;"></div>
    <ul id="port">
    
   <textarea id="commentBox" style="display:none;position:absolute;z-index:1;" >
  	</textarea>
  	<div id="evaluateIndex" tabindex="-1" style="border:1px solid #000;width:310px;display:none;position:absolute;z-index:1;">
  		<p><input type="button" value="新增" onclick="addRow()"/></p>
  		<table id="evaluateTable" border=1>
  		<tr>
  			<td>序号</td>
  			<td>名称</td>
  			<td>单位</td>
  			<td>类型</td>
  			<td>操作</td>
  		</tr>
  		</table>
  	</div>
		<div id="tip">Press ‘Tab’ to Insert Child, ‘Enter’ to Insert Sibling Node. For more tips/news, follow <a href="https://twitter.com/my_mind_app" target="_blank">@my_mind_app</a>.</div>
	</ul>
   </div>

		<div class="ui">
			<h3>My Mind</h3>
			<p>
				<button data-command="New" title="New"><img src="<%=path %>/icons/new.png" alt="New" /></button>
				<button data-command="Load" title="Open"><img src="<%=path %>/icons/open.png" alt="Open" /></button>
				<button data-command="Save" title="Save"><img src="<%=path %>/icons/save.png" alt="Save" /></button>
				<button data-command="SaveAs" title="Save as"><img src="<%=path %>/icons/save-as.png" alt="Save as" /></button>
			</p>

			<p>
				<span>Layout</span>
				<select id="layout">
					<option value="">(Inherit)</option>
				</select>
			</p>
			<p>
				<span>Shape</span>
				<select id="shape">
					<option value="">(Automatic)</option>
				</select>
			</p>
			<p>
				<span>Value</span>
				<select id="value">
					<option value="">(None)</option>
					<option value="num">Number</option>
					<optgroup label="Formula">
						<option value="sum">Sum</option>
						<option value="avg">Average</option>
						<option value="min">Minimum</option>
						<option value="max">Maximum</option>
					</optgroup>
				</select>
			</p>
			<p>
				<span>Status</span>
				<select id="status">
					<option value="">None</option>
					<option value="yes">Yes</option>
					<option value="no">No</option>
					<option value="computed">Autocompute</option>
				</select>
			</p>

			<p>
				<span>Color</span>
				<select id="colorType">
					<option value="edge">Edge</option>
					<option value="node">Node</option>
					<option value="font">Font</option>
				</select>
				<span id="color">
					<a data-color="" title="Inherit" href="#"></a>
					<a data-color="#000" title="Black" href="#"></a>
					<a data-color="#e33" title="Red" href="#"></a>
					<a data-color="#3e3" title="Green" href="#"></a>
					<a data-color="#33e" title="Blue" href="#"></a>
					<a data-color="#dd3" title="Yellow" href="#"></a>
					<a data-color="#3dd" title="Cyan" href="#"></a>
					<a data-color="#d3d" title="Magenta" href="#"></a>
					<a data-color="#fa3" title="Orange" href="#"></a>
				</span>
			</p>
			<p>
				<span>Tag</span>
				<span id="tag">
					<a data-tag="" title="none" href="#">无</a>
					<img data-tag="progress_start" title="未启动" src="<%=path%>/icons/progress_start.png"></img>
					<img data-tag="progress_1o" title="完成1/8" src="<%=path%>/icons/progress_1o.png"></img>
					<img data-tag="progress_1q" title="完成1/4" src="<%=path%>/icons/progress_1q.png"></img>
					<img data-tag="progress_3o" title="完成3/8" src="<%=path%>/icons/progress_3o.png"></img>
					<img data-tag="progress_half" title="完成1/2" src="<%=path%>/icons/progress_half.png"></img>
					<img data-tag="progress_5o" title="完成5/8" src="<%=path%>/icons/progress_5o.png"></img>
					<img data-tag="progress_3q" title="完成3/4" src="<%=path%>/icons/progress_3q.png"></img>
					<img data-tag="progress_7o" title="完成7/8" src="<%=path%>/icons/progress_7o.png"></img>
					<img data-tag="progress_done" title="任务完成" src="<%=path%>/icons/progress_done.png"></img>
				</span>
			</p>
			<a id="github" target="_blank" href="https://github.com/ondras/my-mind" title="GitHub project page"><img src="<%=path %>/icons/github.png" alt="GitHub project page" /></a>
			<button id="toggle" title="Toggle UI"></button>
			<button data-command="Help" title="Help"><img src="<%=path %>/icons/help.png" alt="Help" /></button>

			<div id="throbber"></div>

		</div>


		<div id="io" class="ui">
			<h3></h3>
			<p>
				<span>Storage</span>
				<select id="backend"></select>
			</p>
			
			<div id="file">
				<p data-for="save">
					<span>Format</span>
					<select class="format"></select>
				</p>
				<p data-for="save load">
					<button class="go"></button><button class="cancel">Cancel</button>
				</p>
			</div>
			
			<div id="database">
				<p data-for="save">
					<button class="go"></button><button class="cancel">Cancel</button>
				</p>
			</div>

			<div id="image">
				<p data-for="save">
					<button class="go"></button><button class="cancel">Cancel</button>
				</p>
			</div>

			<div id="local">
				<p data-for="load">
					<span>Saved maps</span>
					<select class="list"></select>
				</p>
				<p data-for="save load">
					<button class="go"></button><button class="cancel">Cancel</button>
				</p>
				<p data-for="load">
					<button class="remove">Delete</button>
				</p>
			</div>

			<div id="firebase">
				<p data-for="save load">
					<span>Server</span>
					<input type="text" class="server" />
				</p>
				<p data-for="save load">
					<span>Auth</span>
					<select class="auth">
						<option value="">(None)</option>
						<option value="facebook">Facebook</option>
						<option value="twitter">Twitter</option>
						<option value="github">GitHub</option>
						<option value="persona">Persona</option>
					</select>
				</p>
				<p data-for="load">
					<span>Saved maps</span>
					<select class="list"></select>
				</p>
				<p data-for="save load">
					<button class="go"></button><button class="cancel">Cancel</button>
				</p>
				<p data-for="load">
					<button class="remove">Delete</button>
				</p>
			</div>

			<div id="webdav">
				<p data-for="save load">
					<span>URL</span>
					<input type="text" class="url" />
				</p>
				<p data-for="save load">
					<button class="go"></button><button class="cancel">Cancel</button>
				</p>
			</div>

			<div id="gdrive">
				<p data-for="save">
					<span>Format</span>
					<select class="format"></select>
				</p>
				<p data-for="save load">
					<button class="go"></button><button class="cancel">Cancel</button>
				</p>
			</div>
		</div>

		<div id="help" class="ui">
			<h3>Help</h3>

			<p><span>Navigation</span></p>
			<table class="navigation"></table>

			<p><span>Manipulation</span></p>
			<table class="manipulation"></table>

			<p><span>Editing</span></p>
			<table class="editing"></table>

			<p><span>Other</span></p>
			<table class="other"></table>
		</div>
		
		<div id="menu">
			<button data-command="InsertChild"></button>
			<button data-command="InsertSibling"></button>
			<button data-command="InsertAssociate"></button>
			<button data-command="Delete"></button>
			<span></span>
			<button data-command="Edit"></button>
			<button data-command="Evaluate"></button>
			<button data-command="Value"></button>
			<button data-command="Comment"></button>
			<span></span>
			<button data-command="Undo"></button>
			<button data-command="Redo"></button>
			<button data-command="Center"></button>
		</div>

		<script>
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
			})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
			ga('create', 'UA-383250-18', 'my-mind.github.io');
			ga('send', 'pageview');
		</script>

		<script>
			window.onload = function() {
				MM.App.init();
				MM.App.io.restore();
			}
		</script>
<!--
TODO:
  shortterm:

  longterm:
    - firebase realtime
    - (custom) icons

  bugs:

  only as a request:
	- firebase multiserver
    - l11n
    - custom css
-->
  </body>
</html>
