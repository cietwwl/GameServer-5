function initCategory() {
	$("#editCategory").hide();
	postData = {};
	postData["type"] = categoryManage;
	postData["categoryType"] = "all";
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: initCategoryResetOK,
		dataType: "json"
	});
}

function initCategoryResetOK(data) {
	if (resultError(data)) {
		return;
	}
	var allRole = $("#allCategory");
	html = '<table class="table">';
	html += '<tr onclick="showAddCategory()"><th>类型列表</th></tr>';
	html += '<tr><td>类型id</td><td>类型名称</td><td>类型描述</td><td>操作</td></tr>';
	for (var i = 0; i < data.categorys.length; i++) {
		category = data.categorys[i];
		html += '<tr><td> ' + category.categoryID + ' </td><td>' + category.categoryName + '</td><td> ' + category.categoryDesc + '</td> '
		html += '<td><a href="#" onclick="editCategory(' + category.categoryID + ')" > 编辑 </a>';
		html += '<a href="#" onclick="delCategory(' + category.categoryID + ')">删除</a></td>'
	}
	html += '</tr></table>';
	allRole.html(html);
}

function addCategory() {
	postData = {};
	postData["type"] = categoryManage;
	postData["categoryType"] = "add";
	categoryName = $("#categoryName").val();
	categoryDesc = $("#categoryDesc").val();
	if (categoryName == "" || categoryDesc == "") {
		alert("不能留空");
		return;
	}
	postData["categoryName"] = categoryName;
	postData["categoryDesc"] = categoryDesc;
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: addCategoryResetOK,
		dataType: "json"
	});
}

function addCategoryResetOK(data) {
	if (resultError(data)) {
		return;
	}
	if (data.result == 0) {
		alert("重复添加");
		return;
	}
	$("#categoryName").val("");
	$("#categoryDesc").val("");
	initCategory();
}

function editCategory(categoryID) {
	postData = {};
	postData["type"] = categoryManage;
	postData["categoryType"] = "find";
	postData["categoryID"] = categoryID;
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: showCategory,
		dataType: "json"
	});
}

function delCategory(categoryID) {
	postData = {};
	postData["type"] = categoryManage;
	postData["categoryType"] = "del";
	postData["categoryID"] = categoryID;
	if (confirm("是否确定删除?")) {
		$.ajax({
			type: 'POST',
			url: serverPath,
			data: postData,
			success: initCategory,
			dataType: "json"
		});
	}
}

function showCategory(data) {
	$("#editCategory").show();
	$("#edit_categoryName").val(data.category.categoryName);
	$("#edit_categoryDesc").val(data.category.categoryDesc);
	$("#edit_categoryID").val(data.category.categoryID);
}

function hideEditCategory() {
	$("#editCategory").hide();
}

function hiddenAddCategory() {
	$("#addCategory").hide();
}

function showAddCategory() {
	$("#addCategory").show();
}

function updateCategory() {
	postData = {};
	postData["type"] = categoryManage;
	postData["categoryType"] = "update";
	categoryID = $("#edit_categoryID").val();
	categoryName = $("#edit_categoryName").val();
	categoryDesc = $("#edit_categoryDesc").val();
	if (categoryID == "" || categoryDesc == "" || categoryName == "") {
		alert("不能留空");
		return;
	}
	postData["categoryID"] = categoryID;
	postData["categoryName"] = categoryName;
	postData["categoryDesc"] = categoryDesc;
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: updateCategoryResetOK,
		dataType: "json"
	});
}

function updateCategoryResetOK(data) {
	if (resultError(data)) {
		return;
	}
	alert("更新成功");
	$("#edit_categoryID").val("");
	$("#edit_categoryName").val("");
	$("#edit_categoryDesc").val("");
	initCategory();
}