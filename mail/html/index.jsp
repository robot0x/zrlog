<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<form id="ajaxemailServiceMsg" class="form-horizontal" role="form" action="${url}/admin/plugin/install">
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 登陆名 </label>

		<div class="col-sm-9">
			<input type="text" id="form-field-1" class="col-xs-10 col-sm-5" value="forum4j@163.com" name="mail_from">
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> SMTP服务器 </label>

		<div class="col-sm-9">
			<input type="text" id="form-field-1" class="col-xs-10 col-sm-5" value="smtp.163.com" name="mail_smtpServer">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 密码  </label>

		<div class="col-sm-9">
			<input type="password" id="form-field-1" class="col-xs-10 col-sm-5" value="wozcc94" name="mail_password">
		</div>
	</div>
	
	 
	<div class="space-4"></div>

	<div class="clearfix form-actions">
		<div class="col-md-offset-3 col-md-9">
			<button class="btn btn-info" type="button" id="emailServiceMsg">
				<i class="icon-ok bigger-110"></i>
				提交
			</button>

			&nbsp; &nbsp; &nbsp;
			<button class="btn" type="reset">
				<i class="icon-undo bigger-110"></i>
				重置
			</button>
		</div>
	</div>

</form>