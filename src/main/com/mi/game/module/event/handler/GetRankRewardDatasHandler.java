package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;

/**
 * 竞技排行,获取奖励详情列表
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 6:21:57 PM
 */
@HandlerType(type = HandlerIds.EVENT_RANK_REWARD_TEMPLATE_LIST)
public class GetRankRewardDatasHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		// String playerID = ioMessage.getPlayerId();
		// String rewardID = (String) ioMessage.getInputParse("rewardID");
		// EventModule eventModule = ModuleManager.getModule(
		// ModuleNames.EventModule, EventModule.class);
		// RankRewardProtocol protocol = new RankRewardProtocol();
		// eventModule.getSportsRankRewardGoods(protocol);
		// ioMessage.setProtocol(protocol);

		EventModule eventModule = ModuleManager.getModule(
				ModuleNames.EventModule, EventModule.class);
		// 发送每日充值未领取奖励
		// eventModule.sendPayEveryDayRewardForAll();
		// 发送每日消费未领取奖励
		// eventModule.sendDailyExpenseRewardForAll();
		// 发送竞技排名奖励
		eventModule.sendSportsRankReward();
		ioMessage.setProtocol(new BaseProtocol());
	}
}
