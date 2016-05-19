package com.mi.game.module.pet.handler;

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
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pet.PetModule;
import com.mi.game.module.pet.protocol.PetInfoProtocol;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;

@HandlerType(type = HandlerIds.FeedPet)
public class FeedHandler extends BaseHandler {
    @Override
	public void execute(IOMessage ioMessage) {
    	String playerID = ioMessage.getPlayerId();
		long petID = 0;		
		if(ioMessage.getInputParse("petID") != null){
			petID = Long.parseLong(ioMessage.getInputParse("petID").toString());
		}
		List<Object> expList=new ArrayList<Object>();
		if(ioMessage.getInputParse("expList")!=null){
			expList=JSON.parseArray(ioMessage.getInputParse("expList").toString());
		}	
										
		List<GoodsBean> goodsList = new ArrayList<>();		
		int useType = 0;
		if(ioMessage.getInputParse("useType") != null){
			useType = Integer.parseInt(ioMessage.getInputParse("useType").toString());
		}
		if(useType == 0){
			int pid = Integer.parseInt(expList.get(0).toString());
			int num = 1;
			goodsList.add(new GoodsBean(pid,num));						
		}else{		
			BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule,BagModule.class);
			BagEntity bagEntity = bagModule.getBagEntity(playerID, ioMessage);
			Map<Integer,BagItem> bagList = bagEntity.getBagList();
			for(Object id : expList){
				int itemID = Integer.parseInt(id.toString());
				BagItem bagItem  = bagList.get(itemID);
				if(bagItem != null){
					int num = bagItem.getNum();	
					goodsList.add(new GoodsBean(itemID, num));					
				}
			}
		}
		
		PetInfoProtocol protocol = new PetInfoProtocol();
		PetModule petModule = ModuleManager.getModule(ModuleNames.PetModule,PetModule.class);
		petModule.feedPet(playerID,useType, petID,goodsList, protocol,ioMessage);
		ioMessage.setOutputResult(protocol);
	}
}
