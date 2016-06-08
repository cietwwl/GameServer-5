function initAdmin() {
	$("#editAdmin").hide();
	$("#addAdmin").hide();
	postData = {};
	postData["type"] = adminManage;
	postData["adminType"] = "all";
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: initAdminResetOK,
		dataType: "json"
	});
}

function initAdminResetOK(data) {
	if (resultError(data)) {
		return;
	}
	initTree(data);
	var allAdmin = $("#allAdmin");
	html = '<table class="table">';
	html += '<tr onclick="showAddAdmin()"><th>管理员列表</th></tr>';
	html += '<tr><td>用户id</td><td>用户名</td><td>密码</td><td>操作</td></tr>';
	for (var i = 0; i < data.admins.length; i++) {
		admin = data.admins[i];
		html += '<tr><td> ' + admin.userID + ' </td><td>' + admin.username + '</td><td>******</td>'
		html += '<td><a href="#" onclick="editAdmin(' + admin.userID + ')" > 编辑 </a>'
		html += '<a href="#" onclick="delAdmin(' + admin.userID + ')">删除</a></td>'
	}
	html += '</tr></table>';
	allAdmin.html(html);
}

function initTree(data) {
	var setting = {
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
	var zNodes = data.roles;
	console.log(data);
	zTree = $.fn.zTree.init($("#tree"), setting, zNodes);
}

function initEditTree(data) {
	var setting = {
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
	var zNodes = data.roles;
	edit_zTree = $.fn.zTree.init($("#edit_tree"), setting, zNodes);
}

function addAdmin() {
	postData = {};
	postData["type"] = adminManage;
	postData["adminType"] = "add";
	username = $("#username").val();
	pwd = $("#password").val();
	if (username == "" || pwd == "") {
		alert("不能留空");
		return;
	}
	postData["username"] = username;
	postData["password"] = pwd;
	var nodes = getCheckedRole(zTree.getCheckedNodes(true));
	postData["roles"] = nodes;
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: addAdminResetOk,
		dataType: "json"
	});
}

function getCheckedRole(nodes) {
	var ret = [];
	var i = 0;
	for (var temp in nodes) {
		var nodeTemp = nodes[temp];
		if (nodeTemp.pId != null && nodeTemp.children != null) {
			var role = {};
			role.id = nodeTemp.id;
			for (var child in nodeTemp.children) {
				if (nodeTemp.children[child].checked) {
					role[nodeTemp.children[child].id] = "";
				}
			}
			ret[i] = role;
			i++;
		}
	}
	return obj2str(ret);
}

function addAdminResetOk(data) {
	if (resultError(data)) {
		return;
	}
	if (data.result == 0) {
		alert("重复添加");
		return;
	}
	$("#username").val("");
	$("#password").val("");
	zTree.checkAllNodes(false);
	initAdmin();
}

function delAdmin(adminID) {
	postData = {};
	postData["type"] = adminManage;
	postData["adminType"] = "del";
	postData["userID"] = adminID;
	if (confirm("是否确定删除?")) {
		$.ajax({
			type: 'POST',
			url: serverPath,
			data: postData,
			success: initAdmin,
			dataType: "json"
		});
	}
}

function hiddenAddAdmin() {
	$("#addAdmin").hide();
}

function showAddAdmin() {
	$("#addAdmin").show();
}

function hiddenEditAdmin() {
	$("#editAdmin").hide();
}

function editAdmin(adminID) {
	postData["type"] = adminManage;
	postData["adminType"] = "find";
	postData["userID"] = adminID;
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: showAdmin,
		dataType: "json"
	});
}

function showAdmin(data) {
	$("#addAdmin").hide();
	$("#editAdmin").show();
	$("#edit_username").val(data.admin.username);
	$("#edit_userID").val(data.admin.userID);
	$("#edit_password").val(data.admin.password);
	initEditTree(data);
}

function updateAdmin() {
	postData = {};
	postData["type"] = adminManage;
	postData["adminType"] = "update";
	username = $("#edit_username").val();
	pwd = $("#edit_password").val();
	if (username == "" || pwd == "") {
		alert("不能留空");
		return;
	}
	postData["username"] = username;
	postData["password"] = pwd;
	postData["userID"] = $("#edit_userID").val();
	var nodes = getCheckedRole(edit_zTree.getCheckedNodes(true));
	postData["roles"] = nodes;
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: updateAdminResetOk,
		dataType: "json"
	});
}

function updateAdminResetOk(data) {
	if (resultError(data)) {
		return;
	}
	if (data.result == 0) {
		alert("用户名重复");
		return;
	}
	initAdmin();
}