var logout_type;

function checkAdminLogin() {
	postData = {};
	postData["type"] = adminLogin;
	postData["loginType"] = "login";
	postData["username"] = $("#username").val();
	postData["password"] = $("#password").val();
	admin["username"] = $("#username").val();
	admin["password"] = $("#password").val();
	$.ajax({
		type: 'POST',
		url: serverPath,
		data: postData,
		success: checkAdminLoginResetOK,
		dataType: "json"
	});
}

function logout() {
	postData = {};
	postData["type"] = adminLogin;
	postData["loginType"] = "logout";
	if (confirm("确定退出?")) {
		logout_type = 1;
		$.cookies.del("admin");
		if (logout_type == 1) {
			location.href = "login/login.html?安全退出";
			return;
		}
	}
}

function checkAdminLoginResetOK(data) {
	if (resultError(data)) {
		return;
	}
	if (data.result == 1) {
		$.cookies.set("admin", admin);
		location.href = "../index.html";
		return;
	} else {
		alert("帐号密码错误!");
		location.href = "../login/login.html";
		return;
	}
}