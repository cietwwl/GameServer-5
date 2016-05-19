var jsonGameUrl = serverPath;
var queryCMD = 11111; // 查询命令id
var showCMD = 11112; // 查询命令id
var updateCMD = 11113; // 更新命令id
var deleteCMD = 11114; // 删除命令id
var queryLongCMD = 11115; // 查询long的命令
var updateLongCMD = 11116; // 更新实体
var deleteLongCMD = 11117; // 删除实体

var confdataListCMD = 11121; // 查询配置数据列表
var confdataShowCMD = 11122; // 查询配置数据命令id
var confdataUpdateCMD = 11123; // 更新配置数据命令id
var confdataDeleteCMD = 11124; // 删除配置数据命令id
var confdataResetCMD = 11125; // 刷新配置数据

var addItemCMD = 11126; // 增加物品
var addCDKCMD = 11127; // 生成cdk
var postParseJson = {};
var inDex = 1;

/**
 * 控制查询条件显示或者隐藏
 *
 * @param spanObj
 */
function controlQueryinfo(spanObj) {
	if (parseInt($(spanObj).attr("value")) == 1) { // 隐藏查询条件
		$(spanObj).attr("value", 0);
		$(spanObj).html("显示");
		$('#queryinfo_table').addClass('hidden');
		$('#queryinfo_table').removeClass('show');
	} else {
		$(spanObj).attr("value", 1);
		$(spanObj).html("隐藏");
		$('#queryinfo_table').addClass('show');
		$('#queryinfo_table').removeClass('hidden');
	}
}

function controlLeftMenu(menuId) {
	if (parseInt($("#menu_title_" + menuId).attr("value")) == 1) { // 隐藏查询条件
		$("#menu_title_" + menuId).attr("value", 0);
		$("#menu_title_" + menuId).html("展开");
		$("#menu_cnt_" + menuId).hide();
	} else {
		$("#menu_title_" + menuId).attr("value", 1);
		$("#menu_title_" + menuId).html("折叠");
		$("#menu_cnt_" + menuId).show();
	}
}

/**
 * 初始化设置鼠标滑入滑出颜色
 */
$(document).ready(function() {
	setListTableColor();
})

/**
 * 设置列表数据表格颜色
 */
function setListTableColor() {
	$("#list_table tbody tr").mouseover(function() {
		$(this).attr('bgcl', $(this).css("background-color"));
		$(this).css("background-color", "#33aa66");
	})

	$("#list_table tbody tr").mouseout(function() {
		$(this).css("background-color", $(this).attr('bgcl'));
	})
}

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

function longTimeQuery() {
	postParseJson = {};
	postParseJson["type"] = 11130;
	postParseJson["time1"] = $('#time1').val();
	postParseJson["time2"] = $('#time2').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: longTimeQueryOk,
		dataType: "json"
	});
}

function longTimeQueryOk(result) {
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

/**
 * 模拟充值
 */
function analogRecharge() {
	postParseJson = {};
	postParseJson["type"] = 1116;
	postParseJson["uid"] = $('#playerId').val();
	postParseJson["money"] = $('#money').val();
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: analogRechargeOk,
		dataType: "json"
	});
}

function analogRechargeOk(result) {
	if (resultError(result)) {
		return;
	}
	alert('充值成功');
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
		// alert(queryFieldArr[i] + "="+$('#query_'+queryFieldArr[i]).val());
		postParseJson[queryFieldArr[i]] = $('#query_' + queryFieldArr[i]).val();
	}
	// alert(obj2str(postParseJson));
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: queryOk,
		dataType: "json"
	});
}

/**
 * 添加实体
 */
function insertEntity() {
	var idField = $('#idField').val();
	if (idField != "cdk") {
		$('#toedit_' + idField).val("-1");
	}
	submitEditEntity();
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
	$("input", document.forms["editForm"]).each(function() {
		if (this.id.startWith('toedit_')) {
			if (this.id == 'toedit_type') {
				postParseJson[this.id] = $(this).val();
			} else {
				postParseJson[this.id.substring(7)] = $(this).val();
			}
		}
	});
	$("textarea", document.forms["editForm"]).each(function() {
		if (this.id.startWith('toedit_')) {
			if (this.id == 'toedit_type') {
				postParseJson[this.id] = $(this).val();
			} else {
				postParseJson[this.id.substring(7)] = $(this).val();
			}
		}
	});

	// alert(obj2str(postParseJson));
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: submitEditEntityOk,
		dataType: "json"
	});
}


function showEntity(entityId) {
	postParseJson = {};
	postParseJson["type"] = showCMD;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson[$('#idField').val()] = entityId;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: showEntityOk,
		dataType: "json"
	});
}


function editEntity(entityId) {
	postParseJson = {};
	postParseJson["type"] = showCMD;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson[$('#idField').val()] = entityId;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: editEntityOk,
		dataType: "json"
	});
}


function delEntity(entityId) {
	postParseJson = {};
	postParseJson["type"] = deleteCMD;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson[$('#idField').val()] = entityId;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];

	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: deleteEntityOk,
		dataType: "json"
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
		var string = false;
		if ($('#string') && parseInt($('#string').val()) === 0) {
			string = true;
		}
		if ($('#canedit') && parseInt($('#canedit').val()) === 0) {
			canEdit = false;
		}
		if ($('#candelete') && $('#candelete').val() == 1) {
			canDelete = true;
		}
		for (var i = 0; i < result.entityList.length; i++) {

			var entity = result.entityList[i];
			if (i % 2 == 0) {
				dataHtml += '<tr class="double_tr">';
			} else {
				dataHtml += '<tr class="singe_tr">';
			}
			dataHtml += '<td>';
			dataHtml += ((parseInt($('#page').val()) - 1) * parseInt($('#pagesize').val()) + i + 1);
			dataHtml += '</td>';
			for (var j = 0; j < showFieldArr.length; j++) {

				if (isJson(entity[showFieldArr[j]])) {
					dataHtml += '<td>' + obj2str(entity[showFieldArr[j]]) + '</td>';
				} else if (showFieldArr[j] == "message") {
					if (entity[showFieldArr[j]].length > 30) {
						var ss = entity[showFieldArr[j]].substr(0, 30);
						dataHtml += '<td>' + ss + '....' + '</td>';
					} else {
						dataHtml += '<td>' + entity[showFieldArr[j]] + '</td>';
					}
					dataHtml += '<input id ="hiddenMessage' + entity[idField] + '" type="hidden" value ="' + entity[showFieldArr[j]] + '" />';
				} else {
					dataHtml += '<td>' + entity[showFieldArr[j]] + '</td>';
				}
				if (entity[showFieldArr[j]] == 'manMap') {
					dataHtml += '<td>' + entity[showFieldArr[j]].cardNum + '</td>';
				}
			}
			dataHtml += '<td>';
			if (string) {
				dataHtml += '<a href="#" onclick="showEntity1(\'' + entity[idField] + '\')">查看</a> &nbsp;';
				if (canEdit) {
					dataHtml += '<a href="#" onclick="editEntity1(\'' + entity[idField] + '\')">编辑</a> &nbsp;';
				}
				if (canDelete) {
					dataHtml += '<a href="#" onclick="delEntity1(\'' + entity[idField] + '\')">删除</a>';
				}
			} else {
				// alert('string');
				if ($('#entityManager').val() == "SuggestEntity") {
					dataHtml += '<a href="#" onclick="showHiddenMessge(\'' + entity[idField] + '\')">查看</a> &nbsp;';
					dataHtml += '<a href="#" onclick="changeReadStatus(\'' + entity[idField] + '\',1,' + entity['showStatus'] + ')">已读</a> &nbsp;';
					dataHtml += '<a href="#" onclick="changeReadStatus(\'' + entity[idField] + '\',2,' + entity['showStatus'] + ')">解决</a> &nbsp;';
					dataHtml += '<a href="#" onclick="changeReadStatus(\'' + entity[idField] + '\',3,' + entity['showStatus'] + ')">删除</a> &nbsp;';
				} else {
					dataHtml += '<a href="#" onclick="showEntity(\'' + entity[idField] + '\')">查看</a> &nbsp;';
					if (canEdit) {
						dataHtml += '<a href="#" onclick="editEntity(\'' + entity[idField] + '\')">编辑</a> &nbsp;';
					}
					if (canDelete) {
						dataHtml += '<a href="#" onclick="delEntity(\'' + entity[idField] + '\')">删除</a>';
					}
				}

			}
			dataHtml += '</td>';
			dataHtml += '</tr>';
		}
		$('#list_table_tbody').html(dataHtml);
		setListTableColor();
	}

	showPageInfo();
}

function changeReadStatus(showID, changeType, showStatus) {
	postParseJson = {};
	postParseJson["type"] = updateCMD;
	postParseJson["entity"] = $('#entityManager').val();
	postParseJson[$('#idField').val()] = showID;
	postParseJson["changeType"] = changeType;
	postParseJson["status"] = showStatus;

	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: queryOk,
		dataType: "json"
	});
}

function showHiddenMessge(showId) {
	var showMessage = $('#hiddenMessage' + showId).val();
	$('#show_message').html(showMessage);
	$.colorbox({
		html: $('#show_entity_cnt').html()
	});
}

function showEntityOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.entity) {
		var listName = $('#listName').val();
		if (result.entity.bagArr) {
			var dataHtml = '';
			var showFieldArr = $('#showEntityField').val().split(",");
			var idField = $('#idField').val();
			$('#list_table_tbody1').html('');
			for (var i = 0; i < result.entity.bagArr.length; i++) {
				var entity = result.entity.bagArr[i];
				if (i % 2 == 0) {
					dataHtml += '<tr class="double_tr">';
				} else {
					dataHtml += '<tr class="singe_tr">';
				}
				dataHtml += '<td>';
				dataHtml += ((parseInt($('#page').val()) - 1) * parseInt($('#pagesize').val()) + i + 1);
				dataHtml += '</td>';
				for (var j = 0; j < showFieldArr.length; j++) {
					dataHtml += '<td>' + entity[showFieldArr[j]] + '</td>';

				}
				dataHtml += '<td>';
				dataHtml += '<a href="#" onclick="showEntity(' + entity[idField] + ')">查看</a> &nbsp;';
				dataHtml += '</td>';

				dataHtml += '</tr>';
			}
			$('#list_table_tbody1').html(dataHtml);
			setListTableColor();
			showPageInfo();
		} else if (result.entity[listName]) {
			var dataHtml = '';
			var showFieldArr = $('#showEntityField').val().split(",");
			var idField = $('#idField').val();
			$('#list_table_tbody1').html('');
			var x = 0;
			for (var i in result.entity[listName]) {
				var entity = result.entity[listName][i];
				if (i % 2 == 0) {
					dataHtml += '<tr class="double_tr">';
				} else {
					dataHtml += '<tr class="singe_tr">';
				}
				dataHtml += '<td>';
				dataHtml += (x += 1);
				dataHtml += '</td>';
				for (var j = 0; j < showFieldArr.length; j++) {
					dataHtml += '<td>' + entity[showFieldArr[j]] + '</td>';
				}
				dataHtml += '<td>';
				dataHtml += '<a href="#" onclick="showEntity(' + entity[idField] + ')">查看</a> &nbsp;';
				dataHtml += '</td>';
				dataHtml += '</tr>';
			}
			$('#list_table_tbody1').html(dataHtml);
			setListTableColor();
			showPageInfo();
		} else {
			var entity = result.entity;
			for (var item in entity) {
				if ($('#show_' + item)) {
					if (item == 'bagArr') {
						for (var i = 0; i < 2; i++) {
							alert('amount:' + entity.bagArr[i].amount);
							alert('uid:' + entity.bagArr[i].uid);
							alert('index:' + entity.bagArr[i].index)
							$('#show_' + uid).html(entity.bagArr[i].uid);
							$('#show_' + index).html(entity.bagArr[i].index);
							$('#show_' + amount).html(entity.bagArr[i].amount);
						}
					}
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
				html: $('#show_entity_cnt').html()
			});
		}
	}
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
		for (var item in entity) {
			if ($('#edit_' + item)) {
				if (item == 'key') {
					$('#edit_' + entity.keyName).html(entity.key);
				} else {
					$('#edit_' + item).html(entity[item]);
				}
			}
			if ($('#toedit_' + item)) {
				if (item == 'key') {
					$('#toedit_' + entity.keyName).val(entity.key);
				} else {
					if (isJson(entity[item])) {
						$('#toedit_' + item).val(obj2str(entity[item]));
					} else {
						$('#toedit_' + item).val(entity[item]);
					}
				}

			}
		}
		$('#edit_entity_cnt').removeClass('hidden');
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
		alert("删除成功");
		submitQuery();
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
		alert("更新成功");
		submitQuery();
	}
}

function showHelpDiv() {
	$.colorbox({
		html: $('#show_help_div').html()
	});
}

/**
 * 显示页数信息
 */
function showPageInfo() {
	var pageinfohtml = '当前第' + $('#page').val() + '页，共' + $('#totalpage').val() + '页,共' + $('#total').val() + '条记录 &nbsp;';

	if (parseInt($('#page').val()) > 1) {
		pageinfohtml += '<input type="button" value="首页" onclick="firstPage()"/> &nbsp;';
		pageinfohtml += '<input type="button" value="上一页" onclick="berforePage()"/> &nbsp;';
	}
	if (parseInt($('#page').val()) < parseInt($('#totalpage').val())) {
		pageinfohtml += '<input type="button" value="下一页" onclick="nextPage()"/> &nbsp;';
		pageinfohtml += '<input type="button" value="末页" onclick="endPage()"/> &nbsp;';
	}

	pageinfohtml += ' &nbsp; 跳转到第<input type="text" size="2" id="jumppage"/>页  &nbsp;';
	pageinfohtml += '<input type="button" value="跳转" onclick="jumpPage()"/>';
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
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: dtsOK,
		dataType: "json"
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

function updateVersion() {
	postParseJson = {};
	postParseJson["type"] = 1117;
	postParseJson["ver"] = $('#new_version').val();

	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: updateVersionOK,
		dataType: "json"
	});
}

function updateVersionOK(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		$('#cur_version_span').html(result.ver);
		if (result.update == 1) {
			alert("更新成功");
		}
	}
}

function exptoexcel() {
	// 指定表的id值 并获取该对象
	var curTbl = document.getElementById("list_table");
	// 新建excel 的ActiveX对象
	var oXL = new ActiveXObject("Excel.Application");
	// 添加一个Workbook
	var oWB = oXL.Workbooks.Add();
	// 打开一个sheet
	var oSheet = oWB.ActiveSheet;
	// copy curTbl的text到sheet
	var sel = document.body.createTextRange();
	sel.moveToElementText(curTbl);
	sel.select();
	sel.execCommand("Copy");
	oSheet.Paste();
	oXL.Visible = true;
}

// 配置数据管理
/**
 * 显示配置数据列表
 */

function confdataList() {
	postParseJson = {};
	postParseJson["type"] = confdataListCMD;
	postParseJson["confdata"] = $('#confdata').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	// alert(obj2str(postParseJson));
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: confdataListOk,
		dataType: "json"
	});
}

function confdataListOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.confList) {
		var dataHtml = '';
		var showFieldArr = $('#showField').val().split(",");
		$('#list_table_tbody').html('');
		var canEdit = true;
		var canDelete = false;
		if ($('#canedit') && parseInt($('#canedit').val()) === 0) {
			canEdit = false;
		}
		if ($('#candelete') && $('#candelete').val() == 1) {
			canDelete = true;
		}
		for (var i = 0; i < result.confList.length; i++) {
			var entity = result.confList[i];
			if (entity == null) {
				continue;
			}
			if (i % 2 == 0) {
				dataHtml += '<tr class="double_tr">';
			} else {
				dataHtml += '<tr class="singe_tr">';
			}
			dataHtml += '<td>';
			dataHtml += "" + (i + 1);
			dataHtml += '</td>';
			for (var j = 0; j < showFieldArr.length; j++) {
				dataHtml += '<td>' + entity[showFieldArr[j]] + '</td>';
			}

			dataHtml += '<td>';
			dataHtml += '<a href="#" onclick="showConfdata(\'' + entity["fileName"] + '\')">查看</a> &nbsp;';
			if (canEdit) {
				dataHtml += '<a href="#" onclick="editConfdata(\'' + entity["fileName"] + '\')">编辑</a> &nbsp;';
			}
			if (canDelete) {
				dataHtml += '<a href="#" onclick="delConfdata(' + entity["fileName"] + ')">删除</a>';
			}
			dataHtml += '</td>';
			dataHtml += '</tr>';
		}
		$('#list_table_tbody').html(dataHtml);
		setListTableColor();
	}
}

function showConfdata(dataId) {
	postParseJson = {};
	postParseJson["type"] = confdataShowCMD;
	postParseJson["fileName"] = dataId;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: showConfdataOk,
		dataType: "json"
	});
}

function showConfdataOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.content) {
		$('#show_content').text(result.content);
		$.colorbox({
			html: $('#show_entity_cnt').html()
		});
	}
}

function editConfdata(dataId) {
	postParseJson = {};
	postParseJson["type"] = confdataShowCMD;
	postParseJson["fileName"] = dataId;
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	// alert(postParseJson["confdata"]);
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: editConfdataOk,
		dataType: "json"
	});
}

function editConfdataOk(result) {
	
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	
	if (result.content) {
		$('#edit_fileName').html(result.fileName);
			$('#toedit_fileName').val(result.fileName);
			$('#toedit_content').text(result.content);

		$('#edit_entity_cnt').removeClass('hidden');
	}
}

function submitEditConfdata() {
	postParseJson = {};
	postParseJson["type"] = confdataUpdateCMD;
	postParseJson["confdata"] = $('#confdata').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];
	$("input", document.forms["editForm"]).each(function() {
		if (this.id.startWith('toedit_')) {
			if (this.id == 'toedit_type') {
				postParseJson[this.id] = $(this).val();
			} else {
				postParseJson[this.id.substring(7)] = $(this).val();
			}
		}
	});
	$("textarea", document.forms["editForm"]).each(function() {
		if (this.id.startWith('toedit_')) {
			if (this.id == 'toedit_type') {
				postParseJson[this.id] = $(this).val();
			} else {
				postParseJson[this.id.substring(7)] = $(this).val();
			}
		}
	});

	// alert(obj2str(postParseJson));
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: submitEditConfdataOk,
		dataType: "json"
	});
}

function submitEditConfdataOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result == 2) {
		msg = "权限不足";
		self.location.href = "../desc.html?" + login_type + "," + msg;
		return;
	}
	if (result.result && result.result == 1) {
		alert("更新成功");
		confdataList();
	}
}

function delConfdata(dataId) {
	postParseJson = {};
	postParseJson["type"] = confdataDeleteCMD;
	postParseJson["confdata"] = $('#confdata').val();
	postParseJson[$('#idField').val()] = dataId;

	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: delConfdataOk,
		dataType: "json"
	});
}

function delConfdataOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		alert("删除成功");
		confdataList();
	}
}

function confdataReset() {
	postParseJson = {};
	postParseJson["type"] = confdataResetCMD;
	postParseJson["confdata"] = $('#confdata').val();

	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: confdataResetOk,
		dataType: "json"
	});
}

function confdataResetOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		alert("刷新配置数据成功");
		// confdataList();
	}
}

function addItemReq() {
	postParseJson = {};
	postParseJson["type"] = addItemCMD;
	postParseJson["playerID"] = $('#add_playerID').val();
	postParseJson["itemParam"] = $('#add_itemParam').val();
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: addItemReqOk,
		dataType: "json"
	});
}

function addItemReqOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		alert("添加物品成功");
		// confdataList();
	}
}

function addCDKReq() {
	postParseJson = {};
	postParseJson["type"] = addCDKCMD;
	postParseJson["cdkType"] = $('#add_cdkType').val();
	postParseJson["platFrom"] = $('#add_platFrom').val();
	postParseJson["rewardID"] = $('#add_rewardID').val();
	postParseJson["num"] = $('#add_num').val();
	postParseJson["startTime"] = $('#add_startTime').val();
	postParseJson["endTime"] = $('#add_endTime').val();
	postParseJson["permission"] = htmlpath;
	postParseJson["username"] = admin["username"];

	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: addCDKReqOk,
		dataType: "json"
	});
}

function addCDKReqOk(result) {
	if (resultError(result)) {
		return;
	}
	if (result.result && result.result == 1) {
		var htm = "<h2>cdk下载地址:<a href='" + result.url + "'>" + result.url + "</a></h2>";
		$('#cdkView').html(htm);
		alert("cdk已生成!");
	}
}

function sendSysMsg() {
	postParseJson = {};
	postParseJson["type"] = 10100;
	postParseJson["playerID"] = $('#playerID').val();
	postParseJson["title"] = $('#title').val();
	postParseJson["content"] = $('#content').val();
	postParseJson["goods"] = $('#goods').val();
	postParseJson["expireDay"] = $('#expireDay').val();
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: sendSysMsgOK,
		dataType: "json"
	});
}

function sendSysMsgOK(result) {
	if (resultError(result)) {
		return;
	}
	alert("发送成功");
}

function updateInitialTask() {
	postParseJson = {};
	postParseJson["type"] = 10101;
	postParseJson["mainTaskId"] = $('#mainTaskId').val();
	postParseJson["monthTaskIds"] = $('#monthTaskIds').val();
	postParseJson["weekTaskIds"] = $('#weekTaskIds').val();
	postParseJson["dayTaskIds"] = $('#dayTaskIds').val();
	$.ajax({
		type: 'POST',
		url: jsonGameUrl,
		data: postParseJson,
		success: updateInitialTaskOK,
		dataType: "json"
	});
}

function updateInitialTaskOK(result) {
	if (resultError(result)) {
		return;
	}
	if (result.mainTaskId) {
		$('#mainTaskId').val(result.mainTaskId);
	}
	if (result.monthTaskIds) {
		$('#monthTaskIds').val(result.monthTaskIds);
	}
	if (result.weekTaskIds) {
		$('#weekTaskIds').val(result.weekTaskIds);
	}
	if (result.dayTaskIds) {
		$('#dayTaskIds').val(result.dayTaskIds);
	}
}

function isJson(obj) {
	var isjson = typeof(obj) == "object";
	return isjson;
}