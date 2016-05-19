package com.mi.game.module.pay.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.util.ConfigUtil;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.data.PayData;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.protocol.PayProtocol;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.VivoSignUtils;

/**
 * vivo平台创建支付订单
 * 
 * @author 李强 <br/>
 *
 */
@HandlerType(type = HandlerIds.VIVO_CREATE_ORDER, order = 2)
public class CreateViVoOrderHandler extends BaseHandler {

	// 服务器id
	private static String SERVER_ID = ConfigUtil.getString("server.id");
	private static String storeId = ConfigUtil.getString("vivo.CPID");
	private static String appId = ConfigUtil.getString("vivo.appid");
	private static String CPKEY = ConfigUtil.getString("vivo.CPKEY");

	private static final String version = "3.0.1";

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
		String cpOrderNumber = storeOrder;
		// 测试地址
//		String notifyUrl = "http://182.92.97.248:8001/pay/MiGamePay.do?platForm=vivo";

		// 正式地址
		String notifyUrl =
		 "http://pay-android.millergame.net/pay/MiGamePay.do?platForm=vivo";

		String orderTime = DateTimeUtil.getStringDate(new Date(), "yyyyMMddHHmmss");

		Map<String, String> requestMap = new HashMap<String, String>();

		requestMap.put("version", version);
		requestMap.put("cpId", storeId);
		requestMap.put("appId", appId);
		requestMap.put("extInfo", storeOrder);
		requestMap.put("cpOrderNumber", cpOrderNumber);
		requestMap.put("notifyUrl", notifyUrl);
		requestMap.put("orderTime", orderTime);
		requestMap.put("orderAmount", payData.getRmb() * 100 + "");
		requestMap.put("orderTitle", payData.getName());
		requestMap.put("orderDesc", payData.getName());

		String requestBody = VivoSignUtils.buildReq(requestMap, CPKEY);

		String response = null;
		try {
			response = BasePay.sendPost("https://pay.vivo.com.cn/vcoin/trade",
					requestBody);
		} catch (Exception e) {
			logger.debug("获取vivo订单返回数据错误!");
			protocol.setCode(ErrorIds.SERVER_ERROR);
			return;
		}

		JSONObject json = JSON.parseObject(response);
		Map<String, Object> vivoData = new HashMap<String, Object>();

		if ("200".equals(json.getString("respCode"))) {
			String vivoOrder = json.getString("orderNumber");
			String signature = json.getString("accessKey");
			System.out.println("vivoOrder:" + vivoOrder);
			System.out.println("signature:" + signature);
			vivoData.put("orderID", orderEntity.getOrderID());
			vivoData.put("vivoOrder", vivoOrder);
			vivoData.put("signature", signature);
			if (StringUtils.isBlank(vivoOrder)) {
				logger.debug("获取vivo订单id错误!");
				protocol.setCode(ErrorIds.SERVER_ERROR);
				return;
			}
		}
		protocol.setVivoOrder(vivoData);
		ioMessage.setOutputResult(protocol);
	}

}
