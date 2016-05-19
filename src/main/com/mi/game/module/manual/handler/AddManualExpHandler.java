package com.mi.game.module.manual.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.bag.BagModule;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.bag.pojo.BagItem;
import com.mi.game.module.bag.protocol.BagProtocol;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;

@HandlerType(type = HandlerIds.AddManualExp)
public class AddManualExpHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		List<Object> useIDList = new ArrayList<>();
		if(ioMessage.getInputParse("useIDList") != null)
			useIDList = JSON.parseArray(ioMessage.getInputParse("useIDList").toString());
		long targetID = 0;
		if(ioMessage.getInputParse("targetID") != null){
			targetID =Long.parseLong(ioMessage.getInputParse("targetID").toString());
		}
		List<GoodsBean> goodsList = new ArrayList<>();
		int useType = 0;
		if(ioMessage.getInputParse("useType") != null){
			useType = Integer.parseInt(ioMessage.getInputParse("useType").toString());
		}
		if(useType == 0){
			int pid = Integer.parseInt(useIDList.get(0).toString());
			int num = 1;
			goodsList.add(new GoodsBean(pid,num));
		}else{
			BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule,BagModule.class);
			BagEntity bagEntity = bagModule.getBagEntity(playerID, ioMessage);
			Map<Integer,BagItem> bagList = bagEntity.getBagList();
			for(Object id : useIDList){
				int itemID = Integer.parseInt(id.toString());
				BagItem bagItem  = bagList.get(itemID);
				if(bagItem != null){
					int num = bagItem.getNum();
					goodsList.add(new GoodsBean(itemID, num));
				}
			} 
		}
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
		Map<String,GoodsBean> showMap = new HashMap<>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		BagProtocol protocol = new BagProtocol();
		try{
			rewardModule.useGoods(playerID, goodsList, targetID, true, showMap, itemMap, ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		protocol.setShowMap(showMap);
		protocol.setItemMap(itemMap);
		ioMessage.setProtocol(protocol);
	}
}
