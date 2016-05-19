package com.mi.game.module.pay.handler;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

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
import com.mi.game.module.pay.pojo.JinliOrder;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.protocol.PayProtocol;
import com.mi.game.module.pay.servlet.BasePay;

/**
 * 金立平台创建支付订单
 * 
 * @author 李强 <br/>
 *
 */
@HandlerType(type = HandlerIds.JINLI_CREATE_ORDER, order = 2)
public class CreateJinliOrderHandler extends BaseHandler {

	// 服务器id
	private static String SERVER_ID = ConfigUtil.getString("server.id");
	// apiKey
	private static String apiKey = ConfigUtil.getString("jinli.apiKey");
	// rsaPrivateKey
	private static String rsaPublicKey = ConfigUtil.getString("jinli.rsaPrivateKey");

	// 网游类型接入时固定值
	private static final String DELIVER_TYPE = "1";

	// 成功响应状态码
	private static final String CREATE_SUCCESS_RESPONSE_CODE = "200010000";

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

		String outOrderNo = SERVER_ID + "-" + orderEntity.getOrderID();

		// 测试地址
// String notifyURL =
// "http://182.92.97.248:8001/pay/MiGamePay.do?platForm=jinli";

		// 正式地址
// String notifyURL =
// "http://pay-android.millergame.net/pay/MiGamePay.do?platForm=jinli";
		
		
		// 需支付金额
		BigDecimal totalFee = new BigDecimal(payData.getRmb());
		// 商户总金额
		BigDecimal dealPrice = new BigDecimal(payData.getRmb());
		// 创建金立平台支付订单
		JinliOrder jinliOrder = new JinliOrder(outOrderNo, playerID, payData.getName(), apiKey, totalFee, dealPrice, new Date(), null, null);

		// 设置自己订单号
		jinliOrder.setOrderID(orderEntity.getOrderID());

		String requestBody = null;
		try {
			requestBody = JinliOrder.wrapCreateOrder(jinliOrder, rsaPublicKey, DELIVER_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("生成金立订单数据错误!");
			protocol.setCode(ErrorIds.SERVER_ERROR);
			return;
		}

		String response = null;
		try {
			response = BasePay.sendPost("https://pay.gionee.com/order/create", requestBody);
		} catch (Exception e) {
			logger.debug("获取金立订单返回数据错误!");
			protocol.setCode(ErrorIds.SERVER_ERROR);
			return;
		}

		JSONObject json = JSON.parseObject(response);

		if (CREATE_SUCCESS_RESPONSE_CODE.equals(json.getString("status"))) {
			String orderNo = json.getString("order_no");
			jinliOrder.setOutOrderNo(orderNo);
			if (StringUtils.isBlank(orderNo)) {
				logger.debug("获取金立订单id错误!");
				protocol.setCode(ErrorIds.SERVER_ERROR);
				return;
			}
		}
		protocol.setJinliOrder(jinliOrder);
		ioMessage.setOutputResult(protocol);
	}
}
