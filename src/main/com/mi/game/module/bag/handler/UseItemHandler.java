package com.mi.game.module.bag.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.bag.protocol.BagProtocol;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;

/**
 * @author 刘凯旋	
 * 使用道具
 * 2014年7月4日 下午2:18:39
 */
@HandlerType(type = HandlerIds.UseItem)
public class UseItemHandler extends BaseHandler{
	@Override
	public void execute (IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		List<GoodsBean> goodsList  = new ArrayList<GoodsBean>();
//		if(ioMessage.getInputParse("goodsList") != null){
//			goodsList = JSON.parseArray(ioMessage.getInputParse("goodsList").toString(), GoodsBean.class);
//			//goodsList = JSON.parseArray("}", GoodsBean.class);
//		}
//		long targetID = 0;
//		if(ioMessage.getInputParse("targetID") != null){
//			targetID =Long.parseLong(ioMessage.getInputParse("targetID").toString());
//		}
		int pid = 0;
		if(ioMessage.getInputParse("pid") != null){
			pid =Integer.parseInt(ioMessage.getInputParse("pid").toString());
		}
		int num = 0;
		if(ioMessage.getInputParse("num") != null){
			num = Integer.parseInt(ioMessage.getInputParse("num").toString());
		}
		GoodsBean goodsBean = new GoodsBean(pid,num);
		goodsList.add(goodsBean);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Map<String,GoodsBean> showMap = new HashMap<>();
		BagProtocol protocol = new BagProtocol();
		try{
			rewardModule.useGoods(playerID, goodsList, 0, true, showMap, itemMap, ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		protocol.setShowMap(showMap);
		protocol.setItemMap(itemMap);
		ioMessage.setProtocol(protocol);
	}
}
