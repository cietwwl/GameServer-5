// 商城模板数据
var ShopPropertyTemplate;

// 初始化模板数据
function initTemplate(){
	initTemplateData("/Resources/Config/ShopProperty.xml",initShopPropertyTemplateSecc)
}

function initShopPropertyTemplateSecc(xmlData){
	ShopPropertyTemplate = xml2json(xmlData,"").root.Shop;
}

function initTemplateData(templateUrl,seccessFuc) {	
	$.ajax({
        url: templateUrl,
        dataType: 'xml',
        type: 'GET',
        timeout: 2000, 
        success:seccessFuc
	});
}
