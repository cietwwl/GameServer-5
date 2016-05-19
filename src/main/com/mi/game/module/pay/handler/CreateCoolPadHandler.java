package com.mi.game.module.pay.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.util.ConfigUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.data.PayData;
import com.mi.game.module.pay.pojo.CoolPadOrder;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.protocol.PayProtocol;
import com.mi.game.module.pay.servlet.BasePay;

@HandlerType(type = HandlerIds.COOLPAD_CREATE_ORDER)
public class CreateCoolPadHandler extends BaseHandler {
	private static String SERVER_ID = ConfigUtil.getString("server.id");
	private static String appId = ConfigUtil.getString("coolpad.appID");
	private static String appKey = ConfigUtil.getString("coolpad.appKey");
	private static String priKey = ConfigUtil.getString("coolpad.rsaPrivateKey");

	@Override
	public void execute(IOMessage ioMessage) {
		PayProtocol protocol = new PayProtocol();
		ioMessage.setProtocol(protocol);

		String playerID = ioMessage.getPlayerId();
		// 充值类型
		int type = 0;
		if (ioMessage.getInputParse("t") != null) {
			type = Integer.parseInt(ioMessage.getInputParse("t").toString());
		}

		// 其他
		String parse = null;
		if (ioMessage.getInputParse("parse") != null) {
			parse = ioMessage.getInputParse("parse").toString();
		}

		PayData payData = TemplateManager.getTemplateData(type, PayData.class);
		if (payData == null) {
			protocol.setCode(ErrorIds.PAY_TYPE_ERROR);
			return;
		}

		PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);

		PayOrderEntity orderEntity = payModule.createOrder(playerID, type, payData.getRmb(), parse);

		String storeOrder = SERVER_ID + "_" + orderEntity.getOrderID();

		float price = Float.parseFloat(payData.getRmb() + "");
		CoolPadOrder coolPadOrder = new CoolPadOrder(appId, type, storeOrder, playerID, price, appKey);

		Map<String, String> requestBody = null;
		try {
			requestBody = CoolPadOrder.wrapCreateOrder(coolPadOrder, priKey);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("生成酷派订单数据错误!");
			protocol.setCode(ErrorIds.SERVER_ERROR);
			return;
		}
		String response = null;
		try {
			StringBuffer sb = new StringBuffer();
			Set<Entry<String, String>> entrys = requestBody.entrySet();
			for (Entry<String, String> entry : entrys) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			String temp = sb.toString();
			String requestStr = temp.substring(0, temp.length() - 1);
			response = BasePay.sendPost("http://pay.coolyun.com:6988/payapi/order", requestStr);
		} catch (Exception e) {
			logger.debug("获取酷派订单返回数据错误!");
			protocol.setCode(ErrorIds.SERVER_ERROR);
			return;
		}
		try {
			response = URLDecoder.decode(response, "utf-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String, String> result = getResponseMap(response);
		if (result.containsKey("transdata")) {
			JSONObject json = JSON.parseObject(result.get("transdata"));
			String transid = json.getString("transid");
			coolPadOrder.setOutOrderNo(transid);
			coolPadOrder.setCporderid(orderEntity.getOrderID());
		}
		PayProtocol payProtocol = new PayProtocol();
		payProtocol.setCoolPadOrder(coolPadOrder);
		ioMessage.setOutputResult(payProtocol);

	}

	private Map<String, String> getResponseMap(String str) {
		Map<String, String> result = new HashMap<String, String>();
		String[] temp = str.split("&");
		for (String xx : temp) {
			String[] oo = xx.split("=");
			result.put(oo[0], oo[1]);
		}
		return result;
	}
}
