<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="include/menu.jsp"/>
<script type="text/javascript" src="admin/js/set_update.js"></script>
<script src="assets/js/jquery-ui-1.10.3.full.min.js"></script>
<script src="assets/js/switchery.min.js"></script>
<script src="assets/js/jquery.smartWizard.js"></script>
<script src="admin/js/do_upgrade.js"></script>
<div class="row">
<div class="col-xs-12">
	<div class="x_panel">
	<div class="x_content">
		<h2>
    		更新向导
    	</h2>
<div id="wizard_verticle" class="form_wizard wizard_horizontal">
  <ul class="list-unstyled wizard_steps anchor">
    <li>
      <a href="${currentPage}#step-11" class="selected" isdone="1" rel="1">
        <span class="step_no">1</span>
      </a>
    </li>
    <li>
      <a href="${currentPage}#step-22" class="disabled" isdone="0" rel="2">
        <span class="step_no">2</span>
      </a>
    </li>
    <li>
      <a href="${currentPage}#step-33" class="disabled" isdone="0" rel="3">
        <span class="step_no">3</span>
      </a>
    </li>
  </ul>
<div class="stepContainer" style="min-height: 600px;"><div id="step-11" class="content" style="display: block;">
    <h2 class="StepTitle">Step 1 Content</h2>
    <h3>变更日志</h3>
    <div id="changeLog">
        ${lastVersion.version.changeLog}
    </div>
  </div><div id="step-22" style="min-height: 600px;" class="content" style="display: none;">
    <h2 class="StepTitle">Step 2 Content</h2>
    <h3 id="processbar-title" ></h3>
    <div data-percent="0%" id="progress" class="progress progress-striped" style="display: none;">
        <div style="width: 0%;" id="progress2" class="progress-bar progress-bar-success"></div>
    </div>
  </div><div id="step-33" style="min-height: 600px;" class="content" style="display: none;">
    <h2 class="StepTitle">Step 3 Content</h2>
    <h3>正在执行更新。。。</h3>
  </div>
</div>
</div>
</div>
</div>
</div>
</div>

<jsp:include page="include/footer.jsp"/>