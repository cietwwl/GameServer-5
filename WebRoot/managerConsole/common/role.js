function initRole() {
	$("#editRole").hide();
	postData = {};
	postData["type"] = roleManage;
	postData["roleType"] = "all";
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: initRoleResetOK,
		dataType: "json"
	});
}

function initRoleResetOK(data) {
	if (resultError(data)) {
		return;
	}
	var allRole = $("#allRole");
	html = '<table class="table">';
	html += '<tr onclick="showAddRole()"><th>权限列表</th></tr>';
	html += '<tr><td>权限id</td><td>权限类型</td><td>权限名称</td><td>权限url</td><td>操作</td></tr>';
	for (var i = 0; i < data.roles.length; i++) {
		role = data.roles[i];
		html += '<tr><td> ' + role.roleID + ' </td><td>' + role.categoryID + '</td><td> ' + role.roleName + '</td><td>' + role.roleUrl + '</td> '
		html += '<td><a href="#" onclick="editRole(' + role.roleID + ')" > 编辑 </a> <a href="#" onclick="delRole(' + role.roleID + ')">删除</a></td>'
	}
	html += '</tr></table>';
	allRole.html(html);
	showCategory();
}

function showCategory() {
	postData = {};
	postData["type"] = categoryManage;
	postData["categoryType"] = "all";
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: showCategoryResetOK,
		dataType: "json"
	});
}

function showCategoryResetOK(data) {
	if (resultError(data)) {
		return;
	}
	html = '';
	for (var i = 0; i < data.categorys.length; i++) {
		category = data.categorys[i];
		html += '<option value="' + category.categoryID + '">' + category.categoryName + '</option>';
	}
	$("#roleCategory").html(html);
}

function editShowCategory() {
	postData = {};
	postData["type"] = categoryManage;
	postData["categoryType"] = "all";
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: editShowCategoryResetOK,
		dataType: "json"
	});
}

function editShowCategoryResetOK(data) {
	if (resultError(data)) {
		return;
	}
	html = '';
	var cate = $("#edit_category").val();
	for (var i = 0; i < data.categorys.length; i++) {
		category = data.categorys[i];
		if (cate == category.categoryID) {
			html += '<option value="' + category.categoryID + '" selected="selected">' + category.categoryName + '</option>';
		} else {
			html += '<option value="' + category.categoryID + '">' + category.categoryName + '</option>';
		}
	}
	$("#edit_roleCategory").html(html);
}


function addRole() {
	postData = {};
	postData["type"] = roleManage;
	postData["roleType"] = "add";
	roleCategory = $("#roleCategory").val();
	roleName = $("#roleName").val();
	roleUrl = $("#roleUrl").val();
	if (roleCategory == "" || roleName == "" || roleUrl == "") {
		alert("不能留空");
		return;
	}
	postData["roleCategory"] = roleCategory;
	postData["roleName"] = roleName;
	postData["roleUrl"] = roleUrl;
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: addRoleResetOK,
		dataType: "json"
	});
}

function addRoleResetOK(data) {
	if (resultError(data)) {
		return;
	}
	if (data.result == 0) {
		alert("重复添加");
		return;
	}
	$("#roleCategory").val("");
	$("#roleName").val("");
	$("#roleUrl").val("");
	initRole();
}

function editRole(roleID) {
	postData = {};
	postData["type"] = roleManage;
	postData["roleType"] = "find";
	postData["roleID"] = roleID;
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: showRole,
		dataType: "json"
	});
}

function delRole(roleID) {
	postData = {};
	postData["type"] = roleManage;
	postData["roleType"] = "del";
	postData["roleID"] = roleID;
	if (confirm("是否确定删除?")) {
		$.ajax({
			type: 'POST',
			url: serverPath,
			data: postData,
			success: initRole,
			dataType: "json"
		});
	}
}

function showRole(data) {
	$("#editRole").show();
	$("#edit_category").val(data.role.categoryID);
	$("#edit_roleName").val(data.role.roleName);
	$("#edit_roleUrl").val(data.role.roleUrl);
	$("#edit_roleID").val(data.role.roleID);
	editShowCategory();
}

function hideEditRole() {
	$("#editRole").hide();
}

function hiddenAddRole() {
	$("#addRole").hide();
}

function showAddRole() {
	$("#addRole").show();
}

function updateRole() {
	postData = {};
	postData["type"] = roleManage;
	postData["roleType"] = "update";
	roleID = $("#edit_roleID").val();
	roleCategory = $("#edit_roleCategory").val();
	roleName = $("#edit_roleName").val();
	roleUrl = $("#edit_roleUrl").val();
	if (roleCategory == "" || roleName == "" || roleUrl == "") {
		alert("不能留空");
		return;
	}
	postData["roleID"] = roleID;
	postData["roleCategory"] = roleCategory;
	postData["roleName"] = roleName;
	postData["roleUrl"] = roleUrl;
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: updateRoleResetOK,
		dataType: "json"
	});
}

function updateRoleResetOK(data) {
	if (resultError(data)) {
		return;
	}
	alert("更新成功");
	$("#edit_category").val("");
	$("#edit_roleID").val("");
	$("#edit_roleName").val("");
	$("#edit_roleUrl").val("");
	initRole();
}