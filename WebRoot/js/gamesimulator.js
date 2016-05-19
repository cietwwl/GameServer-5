var uniqueKey = "123456";
var playerID = "";
var playerName = "";
var mainCntDiv = null;

var postParseJson = {};

var jsonGameUrl = "/browserGame/json.do";

// 聊天相关
var chatMsgArr = new Array();
var chatHandler = null;
var chatstartTime = 0;
var chatendTime = 0;
var chatIntervalTime = 10000;

function getLeftMenu() {
	var leftMenuHtml = '';
	leftMenuHtml += '<input type="button" value="加减物品" onclick="start1800()"/> <br/>';
	leftMenuHtml += '<input type="button" value="聊天" onclick="startChat()"/> <br/>';
	leftMenuHtml += '<input type="button" value="商城" onclick="start900()"/> <br/>';
	leftMenuHtml += '<input type="button" value="背包" onclick="post210()"/> <br/>';
	leftMenuHtml += '<input type="button" value="邮箱" onclick="post1900()"/> <br/>';
	leftMenuHtml += '<input type="button" value="英雄" onclick="post406()"/> <br/>';
	leftMenuHtml += '<input type="button" value="任务" onclick="post804()"/> <br/>';
	leftMenuHtml += '<input type="button" value="全服金币排行" onclick="post1603()"/> <br/>';
	leftMenuHtml += '<input type="button" value="全服皇冠排行" onclick="post1604()"/> <br/>';
	leftMenuHtml += '<input type="button" value="全服城防值排行" onclick="post1605()"/> <br/>';
	leftMenuHtml += '<input type="button" value="全服战斗力排行" onclick="post1606()"/> <br/>';
	leftMenuHtml += '<input type="button" value="全服魔石数量排行" onclick="post1607()"/> <br/>';
	leftMenuHtml += '<input type="button" value="全服boss总分排行" onclick="post1608()"/> <br/>';
	leftMenuHtml += '<input type="button" value="全服魅力值排行" onclick="post1609()"/> <br/>';
	leftMenuHtml += '<input type="button" value="全服1v1总分排行" onclick="post1610()"/> <br/>';
	leftMenuHtml += '<input type="button" value="战士战斗力排行" onclick="post1611()"/> <br/>';
	leftMenuHtml += '<input type="button" value="弓手战斗力排行" onclick="post1612()"/> <br/>';
	leftMenuHtml += '<input type="button" value="法师战斗力排行" onclick="post1613()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取金币排行奖励" onclick="post1615()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取皇冠排行奖励" onclick="post1616()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取总战斗力排行奖励" onclick="post1617()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取魅力值排行奖励" onclick="post1618()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取魔石排行奖励" onclick="post1619()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取城防值行奖励" onclick="post1620()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取战士战斗力排行奖励" onclick="post1621()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取弓箭手战斗力排行奖励" onclick="post1622()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取法师战斗力排行奖励" onclick="post1623()"/> <br/>';
	leftMenuHtml += '<input type="button" value="添加好友" onclick="post1614()"/> <br/>';
	leftMenuHtml += '<input type="button" value="查看排行" onclick="post1624()"/> <br/>';
	leftMenuHtml += '<input type="button" value="删除好友" onclick="post2106()"/> <br/>';
	leftMenuHtml += '<input type="button" value="查看好友列表" onclick="post2107()"/> <br/>';
	leftMenuHtml += '<input type="button" value="查看奖励" onclick="post1625()"/> <br/>';
	leftMenuHtml += '<input type="button" value="领取奖励" onclick="post1626()"/> <br/>';
	leftMenuHtml += '<input type="button" value="拒绝请求" onclick="post2108()"/> <br/>';
	leftMenuHtml += '<input type="button" value="t添加全部" onclick="post1624()"/> <br/>';
	leftMenuHtml += '<input type="button" value="推荐好友列表" onclick="post2109()"/> <br/>';
	leftMenuHtml += '<input type="button" value="搜索玩家" onclick="post2110()"/> <br/>';
	leftMenuHtml += '<input type="button" value="搜索玩家" onclick="post2111()"/> <br/>';
	return leftMenuHtml;
}
function post1614add() {
	mainCntDiv.html('');
	var addMenuHtml = '';
	addMenuHtml += '好友id：<input type="text" id="fid" value="0" size="4"/><br/>';
	addMenuHtml += '<input type="button" value="添加" onclick="post1614()"/> &nbsp; ';
	mainCntDiv.html(addMenuHtml);
}

function postData() {
	var parsekey = "";
	var parseValue = "";
	postParseJson = {};
	postParseJson["type"] = $("#cmdId").val();
	postParseJson["playerID"] = $("#playerId").val();
	postParseJson["uniqueKey"] = uniqueKey;

	for ( var i = 1; i < 6; i++) {
		parsekey = $("#parseKey" + i).val();
		parseValue = $("#parseValue" + i).val();
		if (parsekey != "" && parseValue != "") {
			postParseJson[parsekey] = parseValue;
		}
	}

	gamePost(postParseJson, resultError);
}

/**
 * 游戏数据POST提交
 * 
 * @param postParseJson
 * @param success
 * @return
 */
function gamePost(postParseJson, success) {
	if ($("#result_div")) {
		$("#result_div")
				.append(
						'<br/><font color="#ff0000">------------------ 请求数据 ---------------------</font><br/>');
		if (window.JSON) {
			$("#result_div").append(JSON.stringify(postParseJson));
		} else {
			$("#result_div").append(obj2str(postParseJson));
		}
	}

	// alert("ticket:"+ticket+",post ticket:"+postParseJson["ticket"]);
	$.ajax({
		type : 'POST',
		url : jsonGameUrl,
		data : postParseJson,
		success : success,
		dataType : "json"
	});
}

// 处理公共返回数据
function resultCommon(result) {
	var wallet = result.wallet;

	if (result.itemMap && result.itemMap.WalletEntity) {
		wallet = result.itemMap.WalletEntity;
	}

	if (wallet) { // 如果有钱包数据
		if (wallet.gold) {
			$("#user_gold").html('' + wallet.gold);
		}
		if (wallet.gold) {
			$("#user_diamond").html('' + wallet.diamond);
		}
		if (wallet.gold) {
			$("#user_elixir").html('' + wallet.elixir);
		}
	}
}

function resultError(result) {
	if ($("#result_div")) {
		$("#result_div")
				.append(
						'<br/><font color="#0000ff">------------------ 返回数据 ---------------------</font><br/>');
		if (window.JSON) {
			$("#result_div").append(JSON.stringify(result));
		} else {
			$("#result_div").append(obj2str(result));
		}
	}
	if (result.code > 0) {
		alert('错误，code:' + result.code);
		return true;
	}

	return false;
}

function clearResult() {
	if ($("#result_div")) {
		$("#result_div").html("");
	}
}

function login() {
	postParseJson = {};
	postParseJson["type"] = 101;
	postParseJson["playerName"] = $("#playerName").val();
	postParseJson["password"] = $("#password").val();

	gamePost(postParseJson, loginResult);
}

function register() {
	postParseJson = {};
	postParseJson["type"] = 100;
	postParseJson["playerName"] = $("#playerName").val();
	postParseJson["password"] = $("#password").val();

	gamePost(postParseJson, result103);
}

function loginResult(result) {
	if (resultError(result)) {
		return;
	}
	// uniqueKey = result.uniqueKey;
	playerID = result.playerID;
	playerName = result.playerName;
	post103();
}

function post103() {
	postParseJson = {};
	postParseJson["type"] = 103;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result103);
}

function result103(result) {
	if (resultError(result)) {
		return;
	}
	showIndex(result);
}

function controlCommandWindow(obj) {
	if ($("#simulator_div").css("display") == "none") {
		$("#simulator_div").css("display", "block");
		obj.value = "隐藏测试命令";
	} else {
		$("#simulator_div").css("display", "none");
		obj.value = "显示测试命令";
	}
}

/**
 * 显示主页面
 * 
 * @param result
 * @returns
 */
function showIndex(result) {
	gameSimulatorDiv.html('');
	var divhtml = '';
	divhtml += '<div id="user_info_div" class="user_info_css">';
	divhtml += '欢迎您：' + playerName + '&nbsp;';
	divhtml += '金币：<span id="user_gold">' + result.wallet.gold
			+ '</span>&nbsp;';
	divhtml += '钻石：<span id="user_diamond">' + result.wallet.diamond
			+ '</span>&nbsp;';
	divhtml += '粮草：<span id="user_elixir">' + result.wallet.elixir
			+ '</span>&nbsp;';
	divhtml += '</div>';
	divhtml += '<div>';
	divhtml += '  <div id="left_div" class="left_cnt_css">';
	divhtml += getLeftMenu();
	divhtml += '  </div>';
	divhtml += '  <div id="main_cnt_div" class="main_cnt_css">main';
	divhtml += '  </div>';
	divhtml += '</div>';
	gameSimulatorDiv.html(divhtml);

	mainCntDiv = $('#main_cnt_div');
}

function startChat() {
	mainCntDiv.html('');
	var mainCntHtml = '';

	mainCntHtml += '<div>';
	mainCntHtml += '<input type="text" id="chatMsg" size="40"/>';
	mainCntHtml += '<input type="button" value="发送" onclick="post1102()"/>';
	mainCntHtml += '<input type="button" value="停收聊天消息" onclick="stopReceiverChatMsg()"/>';
	mainCntHtml += '</div>';
	mainCntHtml += '<div id="chatCntDiv">aaaaaa';
	mainCntHtml += '</div>';
	mainCntDiv.html(mainCntHtml);

	if (chatHandler == null) {
		receiverChatMsg();
		chatHandler = window.setInterval("receiverChatMsg()", chatIntervalTime);
	}
}

function post1102() {
	postParseJson = {};
	postParseJson["type"] = 1102;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	// {\"body\":\"aaaaaaa\",\"receiverDateTime\":1395129811564,\"receiverType\":3,\"receiverID\":\"0\",\"senderID\":\"13\",\"senderName\":\"whw01\",\"senderType\":2,\"strParams\":\"-1\",\"type\":2,\"sendNum\":\"1\"}

	var message = {};
	message["body"] = $('#chatMsg').val();
	message["senderID"] = playerID;
	message["senderName"] = playerName;
	message["senderType"] = 2;
	message["receiverID"] = 0;
	message["receiverType"] = 3;
	message["strParams"] = "-1";
	message["type"] = "2";

	postParseJson["message"] = obj2str(message);
	gamePost(postParseJson, resultCommon);
}

function stopReceiverChatMsg() {
	window.clearInterval(chatHandler);
	chatHandler = null;
}

// type=1101&data={"channelID":3,"startTime":1395130821516,"endTime":1395130824530,"playerID":"13","uniqueKey":1395129755,"sendNum":118,"lastSendTime":0}
function receiverChatMsg() {
	postParseJson = {};
	postParseJson["type"] = 1101;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["channelID"] = 3;
	postParseJson["startTime"] = chatstartTime;
	postParseJson["endTime"] = chatendTime;

	gamePost(postParseJson, receiverChatMsgOk);
}

function receiverChatMsgOk(result) {
	if (resultError(result)) {
		return;
	}
	chatstartTime = parseInt(result.starTime);
	chatendTime = chatstartTime + chatIntervalTime;
	if (result.msgList && result.msgList.length > 0) {
		for ( var i = 0; i < result.msgList.length; i++) {
			chatMsgArr.push(result.msgList[i]);
		}
		if (chatMsgArr.length > 20) {
			chatMsgArr.pop();
		}
	}

	freshChatCnt();
}

function freshChatCnt() {
	var chatCntDivObj = $('#chatCntDiv');

	if (chatCntDivObj) {
		if (chatMsgArr.length > 0) {
			chatCntDivObj.html('');

			var mainCntHtml = '';

			for ( var index in chatMsgArr) {
				mainCntHtml += '<span>';
				if (chatMsgArr[index].senderID == playerID) {
					mainCntHtml += '[世界]<font color="dd6600">我</font>:<font color="ff0000">';
				} else {
					mainCntHtml += '[世界]<font color="773322">'
							+ chatMsgArr[index].senderName
							+ '</font>:<font color="0000ff">';
				}
				mainCntHtml += chatMsgArr[index].body;
				mainCntHtml += '</font></span><br/>';
			}

			chatCntDivObj.html(mainCntHtml);
		}
	}
}

function start900() {
	mainCntDiv.html('');
	var mainCntHtml = '';

	for ( var i = 0; i < ShopPropertyTemplate.length; i++) {
		mainCntHtml += '<div class="goods_item_css">';
		mainCntHtml += 'PID：' + ShopPropertyTemplate[i].pid + '<br/>';
		mainCntHtml += '货币类型：' + ShopPropertyTemplate[i].priceType + '<br/>';
		mainCntHtml += '价格：' + ShopPropertyTemplate[i].price + '<br/>';
		mainCntHtml += '购买数量：<input type="text" id="num'
				+ ShopPropertyTemplate[i].pid + '" value="1" size="4"/><br/>';
		mainCntHtml += '<input type="button" value="购买" onclick="post900('
				+ ShopPropertyTemplate[i].pid + ')"/><br/>';
		mainCntHtml += '</div>';
	}
	mainCntDiv.html(mainCntHtml);
}

function post900(pid) {
	postParseJson = {};
	postParseJson["type"] = 900;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["pid"] = pid;
	postParseJson["num"] = $('#num' + pid).val();

	gamePost(postParseJson, result900);
}

function result900(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);
}

function start1800() {
	mainCntDiv.html('');
	var mainCntHtml = '添加物品<br/>';
	var mainCntHtml = '常用Pid:2001001-金币 2001002-粮草 2001012-钻石<br/>';

	mainCntHtml += 'pid：<input type="text" id="pid" value="2001001" size="10"/><br/>';
	mainCntHtml += '数量：<input type="text" id="num" value="1" size="4"/><br/>';
	mainCntHtml += '<input type="button" value="添加" onclick="post1800()"/> &nbsp; ';
	mainCntHtml += '<input type="button" value="扣除" onclick="post1801()"/><br/>';
	mainCntDiv.html(mainCntHtml);
}

function post1800() {
	postParseJson = {};
	postParseJson["type"] = 1800;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["pid"] = $('#pid').val();
	postParseJson["num"] = $('#num').val();

	gamePost(postParseJson, result1800);
}

function post1801() {
	postParseJson = {};
	postParseJson["type"] = 1801;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["pid"] = $('#pid').val();
	postParseJson["num"] = $('#num').val();

	gamePost(postParseJson, result1800);
}

function result1800(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);
}

function post210() {
	postParseJson = {};
	postParseJson["type"] = 210;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result210);
}

function result210(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);
	refreshBag(result);
}

function refreshBag(result) {
	mainCntDiv.html('');
	var mainCntHtml = '';
	if (result.bag) {
		for ( var i = 0; i < result.bag.bagArr.length; i++) {
			mainCntHtml += '<div id="bag_item_' + i
					+ '" class="goods_item_css">';
			mainCntHtml += setbagItem(result.bag.bagArr[i], i);
			mainCntHtml += '</div>';
		}
	}

	mainCntDiv.html(mainCntHtml);
}

function setbagItem(bagItem, index) {
	var mainCntHtml = '';
	if (index) {
		bagItem.index = index;
	}
	mainCntHtml += 'PID：' + bagItem.itemData.templateID + '<br/>';
	mainCntHtml += '位置：' + bagItem.index + '<br/>';
	mainCntHtml += '数量：' + bagItem.amount + '<br/>';
	if (bagItem.itemData.templateID > 0) {
		mainCntHtml += '卖出数量：<input type="text" id="sellnum' + bagItem.index
				+ '" value="1" size="4"/><br/>';
		mainCntHtml += '<input type="button" value="卖出" onclick="post209('
				+ bagItem.index + ')"/><br/>';
		mainCntHtml += '<input type="button" value="使用" onclick="post207('
				+ bagItem.index + ')"/>';
	}
	return mainCntHtml;
}

function post209(index) {
	postParseJson = {};
	postParseJson["type"] = 209;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["index"] = index;
	postParseJson["amount"] = $('#sellnum' + index).val();

	gamePost(postParseJson, result209);
}

function result209(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);

	if (result.bagItem) {
		$('#bag_item_' + result.bagItem.index).html(setbagItem(result.bagItem));
	}
}

function post207(index) {
	postParseJson = {};
	postParseJson["type"] = 207;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["index"] = index;
	postParseJson["amount"] = 1;

	gamePost(postParseJson, result207);
}

function result207(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);
}

function post1900() {
	postParseJson = {};
	postParseJson["type"] = 1900;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1900);
}

function result1900(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);
	refreshMsgBox(result);
}

function refreshMsgBox(result) {
	mainCntDiv.html('');
	var mainCntHtml = '';
	if (result.msgBox) {
		for ( var i = 0; i < result.msgBox.itemList.length; i++) {
			mainCntHtml += '<div id="msg_item_'
					+ result.msgBox.itemList[i].index
					+ '" class="goods_item_css">';
			mainCntHtml += setMsgItem(result.msgBox.itemList[i]);
			mainCntHtml += '</div>';
		}
	}

	mainCntDiv.html(mainCntHtml);
}

function setMsgItem(msgItem) {
	var mainCntHtml = '';

	mainCntHtml += '标题：' + msgItem.title + '<br/>';
	mainCntHtml += '类型：' + msgItem.type + '<br/>';
	mainCntHtml += '状态：' + msgItem.stat + '<br/>';
	if (msgItem.goodsList && msgItem.goodsList.length > 0 && msgItem.stat < 2) {
		mainCntHtml += '<input type="button" value="提取物品" onclick="post1901('
				+ msgItem.index + ')"/><br/>';
	}
	mainCntHtml += '<input type="button" value="阅读邮件" onclick="post1902('
			+ msgItem.index + ')"/>';
	mainCntHtml += '<input type="button" value="删除" onclick="post1903('
			+ msgItem.index + ')"/>';
	return mainCntHtml;
}

function post1901(index) {
	postParseJson = {};
	postParseJson["type"] = 1901;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["index"] = index;

	gamePost(postParseJson, result1901);
}

function post1902(index) {
	postParseJson = {};
	postParseJson["type"] = 1902;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["index"] = index;

	gamePost(postParseJson, result1901);
}

function post1903(index) {
	postParseJson = {};
	postParseJson["type"] = 1903;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["index"] = index;

	gamePost(postParseJson, result1901);
}

function result1901(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);
	post1900();
}

function post1603() {
	postParseJson = {};
	postParseJson["type"] = 1603;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}

function result1603(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);

}
function post1604() {
	postParseJson = {};
	postParseJson["type"] = 1604;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}

function post1605() {
	postParseJson = {};
	postParseJson["type"] = 1605;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1606() {
	postParseJson = {};
	postParseJson["type"] = 1606;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1607() {
	postParseJson = {};
	postParseJson["type"] = 1607;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}

function post1608() {
	postParseJson = {};
	postParseJson["type"] = 1608;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}

function post1609() {
	postParseJson = {};
	postParseJson["type"] = 1609;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}

function post1610() {
	postParseJson = {};
	postParseJson["type"] = 1610;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}

function post1611() {
	postParseJson = {};
	postParseJson["type"] = 1611;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1612() {
	postParseJson = {};
	postParseJson["type"] = 1612;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1613() {
	postParseJson = {};
	postParseJson["type"] = 1613;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1615() {
	postParseJson = {};
	postParseJson["type"] = 1615;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1616() {
	postParseJson = {};
	postParseJson["type"] = 1616;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1617() {
	postParseJson = {};
	postParseJson["type"] = 1617;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1618() {
	postParseJson = {};
	postParseJson["type"] = 1618;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1619() {
	postParseJson = {};
	postParseJson["type"] = 1619;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1620() {
	postParseJson = {};
	postParseJson["type"] = 1620;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1621() {
	postParseJson = {};
	postParseJson["type"] = 1621;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1622() {
	postParseJson = {};
	postParseJson["type"] = 1622;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1623() {
	postParseJson = {};
	postParseJson["type"] = 1623;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1624() {
	postParseJson = {};
	postParseJson["type"] = 1624;
	postParseJson["category"] = 5;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1625() {
	postParseJson = {};
	postParseJson["type"] = 1625;
	postParseJson["category"] = 4;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post1626() {
	postParseJson = {};
	postParseJson["type"] = 1626;
	postParseJson["category"] = 4;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}
function post2105() {
	postParseJson = {};
	postParseJson["type"] = 2105;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result1603);
}

function post1614() {
	postParseJson = {};
	postParseJson["type"] = 1614;
	//postParseJson["name"] = "lw05";
	postParseJson["playerID"] = playerID;
	postParseJson["friendID"] = 4;
	postParseJson["category"] = 0;
	postParseJson["addType"] = 0;
	postParseJson["friendType"] = 0;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["fid"] = $('#fid').val();
	gamePost(postParseJson, result1603);
}
function post2106() {
	postParseJson = {};
	postParseJson["type"] = 1614;
	postParseJson["addType"] = 0;
	postParseJson["playerID"] = playerID;
	postParseJson["friendID"] = 158;
	postParseJson["category"] = 2;
	postParseJson["friendType"] = 0;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["fid"] = $('#fid').val();
	gamePost(postParseJson, result1603);
}
function post2107() {
	postParseJson = {};
	postParseJson["type"] = 1630;
	// postParseJson["addType"]=0;
	postParseJson["playerID"] = playerID;
	// postParseJson["friendID"]=170;
	postParseJson["category"] = 1;
	postParseJson["friendType"] = 0;
	postParseJson["uniqueKey"] = uniqueKey;
	// postParseJson["fid"]=$('#fid').val();
	gamePost(postParseJson, result1603);
}
function post2108() {
	postParseJson = {};
	postParseJson["type"] = 1614;
	postParseJson["addType"] = 0;
	postParseJson["playerID"] = playerID;
	postParseJson["friendID"] = 4;
	postParseJson["category"] = 3;
	postParseJson["friendType"] = 0;
	postParseJson["uniqueKey"] = uniqueKey;
	// postParseJson["fid"]=$('#fid').val();
	gamePost(postParseJson, result1603);
}
function post2109() {
	postParseJson = {};
	postParseJson["type"] = 1630;
	// postParseJson["name"]="lw05";
	postParseJson["playerID"] = playerID;
	// postParseJson["friendID"]=170;
	postParseJson["category"] = 2;
	postParseJson["friendType"] = 0;
	postParseJson["uniqueKey"] = uniqueKey;
	// postParseJson["fid"]=$('#fid').val();
	gamePost(postParseJson, result1603);
}
function post2110() {
	postParseJson = {};
	postParseJson["playerID"] = playerID;
	postParseJson["type"] = 1631;
	postParseJson["nickName"] = "lw05";
	postParseJson["friendType"] = 0;
	postParseJson["uniqueKey"] = uniqueKey;
	// postParseJson["fid"]=$('#fid').val();
	gamePost(postParseJson, result1603);
}
function post2111() {
	postParseJson = {};
	postParseJson["playerID"] = playerID;
	postParseJson["type"] = 1614;
	postParseJson["category"] = 4;
	postParseJson["friendID"] = 158;
	postParseJson["friendType"] = 0;
	postParseJson["uniqueKey"] = uniqueKey;
	// postParseJson["fid"]=$('#fid').val();
	gamePost(postParseJson, result1603);
}

function post406() {
	postParseJson = {};
	postParseJson["type"] = 406;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result406);
}

function result406(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);
	refreshHeros(result);
}

function refreshHeros(result) {
	mainCntDiv.html('');
	var mainCntHtml = '';
	if (result.heros) {
		for ( var i = 0; i < result.heros.length; i++) {
			mainCntHtml += '<div id="hero_item_' + result.heros[i].heroID
					+ '" class="goods_item_css">';
			mainCntHtml += setHeroItem(result.heros[i]);
			mainCntHtml += '</div>';
		}
	}

	mainCntDiv.html(mainCntHtml);
}

function setHeroItem(heroItem) {
	var mainCntHtml = '';
	mainCntHtml += '职业：' + heroItem.job + '<br/>';
	mainCntHtml += '等级：' + heroItem.level + '<br/>';
	mainCntHtml += '经验：' + heroItem.currentExp + '<br/>';
	mainCntHtml += '战斗力：' + heroItem.fightValue + '<br/>';

	return mainCntHtml;
}

function post804() {
	postParseJson = {};
	postParseJson["type"] = 804;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;

	gamePost(postParseJson, result804);
}

function result804(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);
	refreshTask(result);
}

function refreshTask(result) {
	mainCntDiv.html('');
	var mainCntHtml = '';
	if (result.taskEntity) {
		if (result.taskEntity.mainTask) {
			mainCntHtml += '<div style="width:600px;">';
			mainCntHtml += '<h1>-------------------主线任务-------------------</h1>';
			for ( var i = 0; i < result.taskEntity.mainTask.length; i++) {
				mainCntHtml += setTaskItem(result.taskEntity.mainTask[i]);
			}
			mainCntHtml += '</div>';
		}
		if (result.taskEntity.dayTask) {
			mainCntHtml += '<div style="width:600px;clear:both;">';
			mainCntHtml += '<h1>----------------日任务----------------</h1>';
			for ( var i = 0; i < result.taskEntity.dayTask.length; i++) {
				mainCntHtml += setTaskItem(result.taskEntity.dayTask[i]);
			}
			mainCntHtml += '</div>';
		}
		if (result.taskEntity.weekTask) {
			mainCntHtml += '<div style="width:600px;clear:both;">';
			mainCntHtml += '<h1>----------------周任务----------------</h1>';
			for ( var i = 0; i < result.taskEntity.weekTask.length; i++) {
				mainCntHtml += setTaskItem(result.taskEntity.weekTask[i]);
			}
			mainCntHtml += '</div>';
		}
		if (result.taskEntity.monthTask) {
			mainCntHtml += '<div style="width:600px;clear:both;">';
			mainCntHtml += '<h1>----------------月任务----------------</h1>';
			for ( var i = 0; i < result.taskEntity.monthTask.length; i++) {
				mainCntHtml += setTaskItem(result.taskEntity.monthTask[i]);
			}
			mainCntHtml += '</div>';
		}
	}

	mainCntDiv.html(mainCntHtml);
}

function setTaskItem(taskItem) {
	var mainCntHtml = '';
	mainCntHtml += '<div id="task_item_' + taskItem.taskId
			+ '" class="goods_item_css">';
	mainCntHtml += 'taskId：' + taskItem.taskId + '<br/>';
	if (taskItem.stat == 0) {
		mainCntHtml += '状态：未完成<br/>';
	} else if (taskItem.stat == 1) {
		mainCntHtml += '状态：可领取奖励<br/>';
		mainCntHtml += '<input type="button" value="领取奖励" onclick="post805('
				+ taskItem.taskId + ')"/><br/>';
	} else if (taskItem.stat == 2) {
		mainCntHtml += '状态：已领取奖励<br/>';

	} else if (taskItem.stat == 3) {
		mainCntHtml += '状态：放弃<br/>';
	} else {
		mainCntHtml += '状态：未知<br/>';
	}
	mainCntHtml += '</div>';
	return mainCntHtml;
}

function post805(taskId) {
	postParseJson = {};
	postParseJson["type"] = 805;
	postParseJson["playerID"] = playerID;
	postParseJson["uniqueKey"] = uniqueKey;
	postParseJson["taskId"] = taskId;

	gamePost(postParseJson, result805);
}

function result805(result) {
	if (resultError(result)) {
		return;
	}

	resultCommon(result);
	post804();
}