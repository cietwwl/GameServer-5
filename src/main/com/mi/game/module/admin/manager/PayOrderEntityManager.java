package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.dao.PayOrderEntityDao;
import com.mi.game.module.pay.pojo.PayOrderEntity;

public class PayOrderEntityManager extends BaseEntityManager<PayOrderEntity> {

	public PayOrderEntityManager() {
		this.dao = PayOrderEntityDao.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String oid = (String) ioMessage.getInputParse("oid");
		PayOrderEntity orderEntity = dao.getEntity(oid);
		if (orderEntity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String orderID = (String) ioMessage.getInputParse("orderID");
		String playerID = (String) ioMessage.getInputParse("playerID");
		String payType = (String) ioMessage.getInputParse("payType");
		String payMoney = (String) ioMessage.getInputParse("payMoney");
		String state = (String) ioMessage.getInputParse("state");
		String itemMap = (String) ioMessage.getInputParse("itemMap");
		if (StringUtils.isNotBlank(orderID)) {
			orderEntity.setOrderID(orderID);
		}
		if (StringUtils.isNotBlank(playerID)) {
			orderEntity.setPlayerID(playerID);
		}
		if (StringUtils.isNotBlank(payType)) {
			orderEntity.setPayType(Integer.parseInt(payType));
		}
		if (StringUtils.isNotBlank(payMoney)) {
			orderEntity.setPayMoney(Integer.parseInt(payMoney));
		}
		if (StringUtils.isNotBlank(state)) {
			int orderState = Integer.parseInt(state);
			if (orderEntity.getState() == 0 && orderState == 1 && orderEntity.getCallbackTime() == 0) {
				// 模拟处理充值
				PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, "test", "test", null);
				orderEntity.setCallbackTime(-1);
			}
			orderEntity.setState(orderState);
		}
		if (StringUtils.isNotBlank(itemMap)) {
			JSONObject temp = (JSONObject) JSON.parse(itemMap);
			if (!temp.isEmpty()) {
				orderEntity.setItemMap(temp);
			}
		}
		dao.save(orderEntity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String clientTime = (String) ioMessage.getInputParse("clientTime");
		String orderID = (String) ioMessage.getInputParse("orderID");
		String playerID = (String) ioMessage.getInputParse("playerID");
		String payType = (String) ioMessage.getInputParse("payType");
		String payMoney = (String) ioMessage.getInputParse("payMoney");
		String state = (String) ioMessage.getInputParse("state");
		if (StringUtils.isNotBlank(clientTime)) {
			queryInfo.addQueryCondition("clientTime", Long.parseLong(clientTime));
		}
		if (StringUtils.isNotBlank(orderID)) {
			queryInfo.addQueryCondition("orderID", orderID);
		}
		if (StringUtils.isNotBlank(playerID)) {
			queryInfo.addQueryCondition("playerID", playerID);
		}
		if (StringUtils.isNotBlank(payType)) {
			queryInfo.addQueryCondition("type", Integer.parseInt(payType));
		}
		if (StringUtils.isNotBlank(payMoney)) {
			queryInfo.addQueryCondition("payMoney", Integer.parseInt(payMoney));
		}
		if (StringUtils.isNotBlank(state)) {
			queryInfo.addQueryCondition("state", Integer.parseInt(state));
		}
		return dao.queryPage(queryInfo);
	}
}
