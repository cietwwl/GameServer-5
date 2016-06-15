//后台地址
var server = "";
// 后台通讯地址
var serverPath = server + "/browserGame/json.do";
// 查询命令id
var queryCMD = 11111;
// 查询命令id
var showCMD = 11112;
// 更新命令id
var updateCMD = 11113;
// 删除命令id
var deleteCMD = 11114;
// 增加实体
var adminCreateEntity = 11115;
// 查询配置数据列表
var confdataListCMD = 11121;
// 查询配置数据命令id
var confdataShowCMD = 11122;
// 更新配置数据命令id
var confdataUpdateCMD = 11123;
// 删除配置数据命令id
var confdataDeleteCMD = 11124;
// 刷新配置数据
var confdataResetCMD = 11125;
// 增加物品
var addItemCMD = 11126;
// 生成cdk
var addCDKCMD = 11127;
// 发放系统奖励
var sendSystemReward = 11128;
// 发放平台奖励
var platformReward = 11132;
// 时间转换
var timeConvert = 11130;
// session
var adminSession = 10001;
// login
var adminLogin = 10002;
// roleManage
var roleManage = 10003;
// userManage
var adminManage = 10004;
// roleManage
var categoryManage = 10005;
// remoteTemplate
var remoteTemplateCMD = 11133;
// updateRebot
var updateTemplateAndRebotCMD=11134;
// addMail
var addMailCMD = 11138;


// 请求数据
var postData = {};

var zTree = {};
var edit_zTree = {};
var login_type = 0;

var admin = {};

var htmlpath = getHtmlPath();

function resultError(result) {
	if (result.code > 0) {
		if (responseCodeArr) {
			alert('错误编码：' + result.code + ",错误信息：" + responseCodeArr[result.code]);
		} else {
			alert('错误编码：' + result.code);
		}
		return true;
	}
	return false;
}

function getHtmlPath() {
	var arr = window.location.pathname.split("managerConsole/");
	return arr[1];
}

function obj2str(o) {
	var r = [];
	if (typeof o == "string")
		return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	if (typeof o == "undefined")
		return "undefined";
	if (typeof o == "object") {
		if (o === null)
			return "null";
		else if (!o.sort) {
			for ( var i in o)
				r.push("\"" + i + "\":" + obj2str(o[i]))
			r = "{" + r.join() + "}"
		} else {
			for (var i = 0; i < o.length; i++)
				r.push(obj2str(o[i]))
			r = "[" + r.join() + "]"
		}
		return r;
	}
	return o.toString();
}

function sessionCheck(type) {
	if (type != "" && type != undefined) {
		login_type = type;
	}
	postData = {};
	postData["type"] = adminSession;
	postData["permission"] = htmlpath;
	admin = $.cookies.get("admin");
	if (admin != null) {
		postData["username"] = admin["username"];
		postData["password"] = admin["password"];
	}
	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postData,
		success : sessionCheckResetOK,
		dataType : "json"
	});
}

function sessionCheckResetOK(data) {
	if (resultError(data)) {
		return;
	}
	if (data.result != 1) {
		toUrl = "login/login.html";
		if (data.result == 2) {
			msg = "权限不足";
			self.location.href = "../desc.html?" + login_type + "," + msg;
			return;
		}
		if (data.result == 3) {
			msg = "用户不存在";
			self.location.href = "login/login.html?" + login_type + "," + msg;
			return;
		}
		if (login_type == 1 && data.result == 4) {
			msg = " 可能以下原因:\n 1.登录超时\n 2.没有打开cookie\n 3.低版本ie浏览器";
			parent.location.href = "login/login.html?" + login_type + "," + msg;
			return;
		}
		if (login_type == 2 && data.result == 4) {
			msg = " 可能以下原因:\n 1.登录超时\n 2.没有打开cookie\n 3.低版本ie浏览器";
			parent.location.href = "../login/login.html?" + login_type + "," + msg;
			return;
		}
		if (login_type == 1) {
			toUrl = "login/login.html";
		}
		if (login_type == 2 && data.result == 0) {
			toUrl = "../login/login.html";
		}
		parent.location.href = toUrl;
		return;
	}

	if (data.result && data.result == 1) {
		if (login_type == 1) {
			initTree(data);
			login_type = 2;
		}
	}
}

function isJson(obj) {
	var isjson = typeof (obj) == "object";
	return isjson;
}

function initTree(data) {
	var setting = {
		data : {
			simpleData : {
				enable : true
			}
		}
	};
	var zNodes = data.roles;
	console.log(data);
	zTree = $.fn.zTree.init($("#tree"), setting, zNodes);
}

function remoteTemplateDataList() {
	postParseJson = {};
	postParseJson["type"] = remoteTemplateCMD;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	postParseJson["cdnUrl"] = $('#cdnUrl').val();
//	alert(obj2str(postParseJson));
	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postParseJson,
		success : remoteTemplateDataOk,
		dataType : "json"
	});
}

function remoteTemplateDataOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		var dataHtml = '';
		var showFieldArr = $('#showField').val().split(",");
		$('#list_table_tbody').html('');
		for (var i = 0; i < result.confList.length; i++) {
			var entity = result.confList[i];
			if (entity == null) {
				continue;
			}
			dataHtml += '<tr>';
			for (var j = 0; j < showFieldArr.length; j++) {
				dataHtml += '<td>' + entity[showFieldArr[j]] + '</td>';
			}
			dataHtml += '</tr>';
		}
		$('#list_table_tbody').html(dataHtml);
	}
}

function remoteRebot() {
	postParseJson = {};
	postParseJson["type"] = updateTemplateAndRebotCMD;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	postParseJson["cdnUrl"] = $('#cdnUrl').val();
	// alert(obj2str(postParseJson));
	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postParseJson,
		success : remoteRebotOk,
		dataType : "json"
	});
}

function remoteRebotOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		// confdataList();
	}
	alert("可能以下原因:\n1.没有需要更新的配置文件\n2.更新失败,请检查文件编码或内容!\n3.CDN不可用或地址错误!");
}

function longTimeQuery() {
	postParseJson = {};
	postParseJson["type"] = timeConvert;
	postParseJson["time1"] = $('#time1').val();
	postParseJson["time2"] = $('#time2').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postParseJson,
		success : longTimeQueryOk,
		dataType : "json"
	});
}

function longTimeQueryOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	$('#nowTime').val(new Date().getTime());
	if (result.time1Long) {
		$('#time1Long').val(result.time1Long);
	}
	if (result.time2Long) {
		$('#time2Long').val(result.time2Long);
	}
}

function addCDKReq() {
	postParseJson = {};
	postParseJson["type"] = addCDKCMD;
	postParseJson["cdkType"] = $('#add_cdkType').val();
	postParseJson["platFrom"] = $('#add_platFrom').val();
	postParseJson["rewardID"] = $('#add_rewardID').val();
	postParseJson["num"] = $('#add_num').val();
	if (postParseJson["cdkType"] == "" || postParseJson["platFrom"] == "" || postParseJson["rewardID"] == "" || postParseJson["num"] == "") {
		alert("类型,平台,奖励,数量不能为空!");
		return;
	}

	postParseJson["startTime"] = $('#add_startTime').val();
	postParseJson["endTime"] = $('#add_endTime').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];

	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postParseJson,
		success : addCDKReqOk,
		dataType : "json"
	});
}

function addCDKReqOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		var htm = "<h2>cdk下载地址:<a href='" + server + result.url + "'>" + result.url + "</a></h2>";
		$('#cdkView').html(htm);
		alert("cdk已生成!");
	}
}

function sendReward(result) {
	postParseJson = {};
	postParseJson["type"] = sendSystemReward;
	postParseJson["sendType"] = result;
	postParseJson["rewardKey"] = $('#add_rewardKey').val();
	postParseJson["playerID"] = $('#add_playerID').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];

	if (postParseJson["rewardKey"] == "") {
		alert("奖励key不能为空");
		return;
	}
	if (result == 1) {
		if (postParseJson["playerID"] == "") {
			alert("玩家ID不能为空");
			return;
		}
	}
	if (confirm("是否确定发放奖励?")) {
		$.ajax({
			type : 'POST',
			url : serverPath,
			data : postParseJson,
			success : addRewardOK,
			dataType : "json"
		});
	}
}

function sendPlatformReward() {
	postParseJson = {};
	postParseJson["type"] = platformReward;
	postParseJson["rewardKey"] = $('#add_rewardKey').val();
	postParseJson["platform"] = $('#add_platform').val();
	postParseJson["title"] = $('#add_title').val();
	postParseJson["msg"] = $('#add_msg').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];

	if (confirm("是否确定发放奖励?")) {
		$.ajax({
			type : 'POST',
			url : serverPath,
			data : postParseJson,
			success : addRewardOK,
			dataType : "json"
		});
	}
}

function addRewardOK(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.result && result.result == 1) {
		alert("添加奖励成功");
	}
}

function addItemReq() {
	postParseJson = {};
	postParseJson["type"] = addItemCMD;
	postParseJson["playerID"] = $('#add_playerID').val();
	postParseJson["itemParam"] = $('#add_itemParam').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	console.log(postParseJson);
	if (confirm("是否确定增加?")) {
		$.ajax({
			type : 'POST',
			url : serverPath,
			data : postParseJson,
			success : addItemReqOk,
			dataType : "json"
		});
	}
}

function addItemReqOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.result && result.result == 1) {
		alert("添加物品成功");
	}
}


/**
 * 发送邮件
 */
function sendMailReq() {
	postParseJson = {};
	postParseJson["type"] = addMailCMD;
	postParseJson["mailTitle"] = $('#send_mailTitle').val();
	postParseJson["mailMessage"] = $('#send_mailMsg').val();
	// 获取id列表
	var playerInfoListTB = $('#list_table_tbody');
	//console.log(playerInfoListTB[0].children);
	var idList = "";
	for(var i = 0; i < playerInfoListTB[0].children.length; i++){
		//console.log(playerInfoListTB[0].children[i].children[1].innerHTML);
		idList += playerInfoListTB[0].children[i].children[1].innerHTML;
	}
	postParseJson["idList"] = idList;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	console.log(postParseJson);
	if (confirm("是否确定增加?")) {
		$.ajax({
			type : 'POST',
			url : serverPath,
			data : postParseJson,
			success : sendMailReqOk,
			dataType : "json"
		});
	}
}

/**
 * 发送邮件成功反馈
 * @param result
 */
function sendMailReqOk(result){
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.result && result.result == 1) {
		alert("邮件发送成功");
	}
}

function controlQueryinfo(spanObj) {
	if (parseInt($(spanObj).attr("value")) == 1) { // 隐藏查询条件
		$(spanObj).attr("value", 0);
		$(spanObj).html("显示");
		$('#queryinfo_table').hide();
	} else {
		$(spanObj).attr("value", 1);
		$(spanObj).html("隐藏");
		$('#queryinfo_table').show();
	}
}

function confdataReset() {
	postParseJson = {};
	postParseJson["type"] = confdataResetCMD;
	postParseJson["confdata"] = $('#confdata').val();
	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postParseJson,
		success : confdataResetOk,
		dataType : "json"
	});
}

function confdataResetOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		alert("刷新配置数据成功");
	}
}

/**
 * 添加实体
 */
function insertEntity() {
	var idField = $('#idField').val();
	postParseJson = {};
	postParseJson["type"] = adminCreateEntity;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	var inputs = $(":input[id^='toedit_']");
	for (var i = 0; i < inputs.length; i++) {
		index = inputs[i].id.replace("toedit_", "");
		if (idField != index) {
			postParseJson[index] = inputs[i].value;
		} else {
			postParseJson[index] = -1;
		}
	}
	if (confirm("是否确定增加?")) {
		$.ajax({
			type : 'POST',
			url : serverPath,
			data : postParseJson,
			success : submitEditEntityOk,
			dataType : "json"
		});
	}
}

/**
 * 提交更新实体数据
 */
function submitEditEntity() {
	postParseJson = {};
	postParseJson["type"] = updateCMD;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	var inputs = $(":input[id^='toedit_']");
	for (var i = 0; i < inputs.length; i++) {
		index = inputs[i].id.replace("toedit_", "");
		postParseJson[index] = inputs[i].value;
	}
	if (confirm("是否确定修改?")) {
		$.ajax({
			type : 'POST',
			url : serverPath,
			data : postParseJson,
			success : submitEditEntityOk,
			dataType : "json"
		});
	}
}

function submitEditEntityOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.result && result.result == 1) {
		$.colorbox.close();
		submitQuery();
	}
}

/**
 * 提交查询
 */
function submitQuery(page) {
	postParseJson = {};
	postParseJson["type"] = queryCMD;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	if (page) {
		postParseJson["page"] = page;
		$('#page').val(page);
	} else {
		postParseJson["page"] = $('#page').val();
	}
	var pageSize = $('#pagesize').val();
	if (pageSize && pageSize.length > 0) {
		postParseJson["pagesize"] = pageSize;
	}
	var queryFieldArr = $('#queryField').val().split(",");

	for (var i = 0; i < queryFieldArr.length; i++) {
		postParseJson[queryFieldArr[i]] = $('#query_' + queryFieldArr[i]).val();
	}
	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postParseJson,
		success : queryOk,
		dataType : "json"
	});
}

function queryOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.totalpage || result.totalpage === 0) {
		$('#totalpage').val(result.totalpage);
	}
	if (result.pagesize) {
		$('#pagesize').val(result.pagesize);
	}
	if (result.total || result.total === 0) {
		$('#total').val(result.total);
	}
	// alert(obj2str(result));
	if (result.entityList) {
		var dataHtml = '';
		var showFieldArr = $('#showField').val().split(",");
		var idField = $('#idField').val();
		$('#list_table_tbody').html('');
		var canEdit = true;
		var canDelete = false;
		if ($('#canedit') && parseInt($('#canedit').val()) === 0) {
			canEdit = false;
		}
		if ($('#candelete') && $('#candelete').val() == 1) {
			canDelete = true;
		}
		for (var i = 0; i < result.entityList.length; i++) {
			var entity = result.entityList[i];
			dataHtml += '<tr><td>' + ((parseInt($('#page').val()) - 1) * parseInt($('#pagesize').val()) + i + 1) + '</td>';
			for (var j = 0; j < showFieldArr.length; j++) {
				if (isJson(entity[showFieldArr[j]])) {
					dataHtml += '<td>' + obj2str(entity[showFieldArr[j]]) + '</td>';
				} else {
					dataHtml += '<td>' + entity[showFieldArr[j]] + '</td>';
				}
			}
			dataHtml += '<td><a href="#" onclick="showEntity(\'' + entity[idField] + '\')">查看</a> &nbsp;';
			if (canEdit) {
				dataHtml += '<a href="#" onclick="editEntity(\'' + entity[idField] + '\')">编辑</a> &nbsp;';
			}
			if (canDelete) {
				dataHtml += '<a href="#" onclick="delEntity(\'' + entity[idField] + '\')">删除</a>';
			}
			dataHtml += '</td></tr>';
		}
		$('#list_table_tbody').html(dataHtml);
	}
	showPageInfo();
}

function editEntity(entityId) {
	postParseJson = {};
	postParseJson["type"] = showCMD;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson[$('#idField').val()] = entityId;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postParseJson,
		success : editEntityOk,
		dataType : "json"
	});
}

function editEntityOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.entity) {
		var entity = result.entity;
		for ( var item in entity) {
			if ($('#edit_' + item)) {
				if (item == 'key') {
					$('#edit_' + entity.keyName).html(entity.key);
				} else {
					$('#edit_' + item).html(entity[item]);
				}
			}
			if ($('#toedit_' + item)) {
				if (item == 'key') {
					$('#toedit_' + entity.keyName).attr("value", entity.key);
					// 兼容textarea
					$('#toedit_' + entity.keyName).html(entity.key);
				} else {
					if (isJson(entity[item])) {
						$('#toedit_' + item).attr("value", obj2str(entity[item]));
						// 兼容textarea
						$('#toedit_' + item).html(obj2str(entity[item]));
					} else {
						$('#toedit_' + item).attr("value", entity[item]);
						// 兼容textarea
						$('#toedit_' + item).html(entity[item]);
					}
				}
			}
		}
		$.colorbox({
			html : $('#edit_entity_cnt').html()
		});
	}
}

function delEntity(entityId) {
	postParseJson = {};
	postParseJson["type"] = deleteCMD;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson[$('#idField').val()] = entityId;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	if (confirm("是否确定删除?")) {
		$.ajax({
			type : 'POST',
			url : serverPath,
			data : postParseJson,
			success : deleteEntityOk,
			dataType : "json"
		});
	}
}

function deleteEntityOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.result && result.result == 1) {
		submitQuery();
	}
}

function showEntity(entityId) {
	postParseJson = {};
	postParseJson["type"] = showCMD;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson[$('#idField').val()] = entityId;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postParseJson,
		success : showEntityOk,
		dataType : "json"
	});
}

function showEntityOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.entity) {
		var listName = $('#listName').val();
		var entity = result.entity;
		for ( var item in entity) {
			if ($('#show_' + item)) {
				if (item == 'key') {
					$('#show_' + item).html(entity.key);
				} else {
					if (isJson(entity[item])) {
						$('#show_' + item).html(obj2str(entity[item]));
					} else {
						$('#show_' + item).html(entity[item] + "");
					}
				}
			}
		}
		$.colorbox({
			html : $('#show_entity_cnt').html()
		});
	}
}

/**
 * 显示页数信息
 */
function showPageInfo() {
	var pageinfohtml = '<pre>当前第' + $('#page').val() + '页，共' + $('#totalpage').val() + '页,共' + $('#total').val() + '条记录 &nbsp;';

	if (parseInt($('#page').val()) > 1) {
		pageinfohtml += '<input type="button" value="首页" onclick="firstPage()" class="btn btn-default"/> &nbsp;';
		pageinfohtml += '<input type="button" value="上一页" onclick="berforePage()" class="btn btn-default"/> &nbsp;';
	}
	if (parseInt($('#page').val()) < parseInt($('#totalpage').val())) {
		pageinfohtml += '<input type="button" value="下一页" onclick="nextPage()" class="btn btn-default"/> &nbsp;';
		pageinfohtml += '<input type="button" value="末页" onclick="endPage()" class="btn btn-default"/> &nbsp;';
	}

	pageinfohtml += ' &nbsp; 跳转到第&nbsp;<input type="text" size=2 id="jumppage"/>&nbsp;页  &nbsp;';
	pageinfohtml += '<input type="button" value="跳转" onclick="jumpPage()" class="btn btn-default"/>';
	pageinfohtml += '</pre>';
	$('#pageinfo').html(pageinfohtml);
}

function firstPage() {
	$('#page').val(1);
	submitQuery();
}

function berforePage() {
	$('#page').val(parseInt($('#page').val()) - 1);
	submitQuery();
}

function nextPage() {
	$('#page').val(parseInt($('#page').val()) + 1);
	submitQuery();
}

function endPage() {
	$('#page').val($('#totalpage').val());
	submitQuery();
}

function jumpPage() {
	$('#page').val($('#jumppage').val());
	submitQuery();
}

function searchSuggest(status) {
	$('#query_status').val(status);
	submitQuery(1);
}

function dts() {
	postParseJson = {};
	postParseJson["type"] = 1118;
	postParseJson["dt"] = $('#dt').val();
	postParseJson["path"] = $('#path').val();
	postParseJson["classes"] = $('#classes').val();

	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postParseJson,
		success : dtsOK,
		dataType : "json"
	});
}

function dtsOK(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		if (result.result == 1) {
			alert("执行成功");
		}
	}
}