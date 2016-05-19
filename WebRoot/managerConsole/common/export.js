function exportDB(data) {
	postData = {};
	postData["type"] = confdataResetCMD;
	postData["confdata"] = "AnalyseModule";
	postData["exportType"] = data;
	$.ajax({
		type : 'POST',
		url : serverPath,
		data : postData,
		success : exportDBResetOK,
		dataType : "json"
	});
}

function exportDBResetOK(data) {
	if (resultError(data)) {
		return;
	}
	alert("开始导出");
}