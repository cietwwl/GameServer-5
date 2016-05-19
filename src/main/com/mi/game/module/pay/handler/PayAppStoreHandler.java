package com.mi.game.module.pay.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.dao.AppstoreReceiptEntityDao;
import com.mi.game.module.pay.define.PayConstants;
import com.mi.game.module.pay.pojo.AppstoreReceiptEntity;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.protocol.PayProtocol;
import com.mi.game.util.IOS_Verify;
import com.mi.game.util.Logs;
import com.mi.game.util.Utilities;

/**
 * appstore订单处理
 */
@HandlerType(type = HandlerIds.PAY_APPSTORE, order = 2)
public class PayAppStoreHandler extends BaseHandler {

	PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);

	@Override
	public void execute(IOMessage ioMessage) {
		PayProtocol protocol = new PayProtocol();
		ioMessage.setOutputResult(protocol);
		if (ioMessage.getInputParse("orderID") != null) {
			String orderID = ioMessage.getInputParse("orderID").toString();
			String receipt = ioMessage.getInputParse("receipt").toString();

			// receipt md5 只能使用一次
			String receiptMD5 = Utilities.encrypt(receipt);

			AppstoreReceiptEntity entity = AppstoreReceiptEntityDao.getInstance().getEntity(receiptMD5);
			if (entity != null) {
				return;
			}

			String verifyState = IOS_Verify.getEnvironment(receipt);
			String verifyResult = IOS_Verify.buyAppVerify(receipt, verifyState);
			if (verifyResult == null) {
				protocol.setCode(ErrorIds.ORDER_APPSTORE_NORESULT);
				return;
			}
			JSONObject json = JSON.parseObject(verifyResult);

			String states = json.getString("status");

			if (!"0".equals(states)) {
				Logs.pay.error("订单：" + orderID + " appstore验证失败!");
				protocol.setCode(ErrorIds.ORDER_NOTDISPOSE);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderID);
			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderID + "未找到");
				protocol.setCode(ErrorIds.ORDER_NOTFOUND);
				return;
			}
			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderID + " 不是初始状态");
				protocol.setCode(ErrorIds.ORDER_DISPOSED);
				return;
			}
			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderID + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PayConstants.PLATFORM_APPSTORE);
				orderEntity.setAppOrderID(IOS_Verify.getTransactionId(receipt));
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PayConstants.PLATFORM_APPSTORE, null);

				// 支付完成,保存appstore Receipt
				entity = new AppstoreReceiptEntity();
				entity.setReceiptMD5(receiptMD5);
				AppstoreReceiptEntityDao.getInstance().save(entity);
			}
		}
	}
}
