package com.mi.game.module.hero.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.pojo.HeroSkinEntity;
import com.mi.game.module.hero.protocol.HeroInfoProtocol;
@HandlerType(type = HandlerIds.ChangeHeroSkin)
public class ChangeHeroSkin extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int charID = 0;
		int skinType = 0;
		if(ioMessage.getInputParse("charID") != null){
			charID = Integer.parseInt(ioMessage.getInputParse("charID").toString());
		}
		int skinID = 0;
		if(ioMessage.getInputParse("skinID") != null){
			skinID = Integer.parseInt(ioMessage.getInputParse("skinID").toString());
		}
		if(ioMessage.getInputParse("skinType") != null){
			skinType = Integer.parseInt(ioMessage.getInputParse("skinType").toString());
		}
		HeroInfoProtocol protocol = new HeroInfoProtocol();
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		if(skinType == 1){
			HeroSkinEntity heroSkinEntity = heroModule.addHeroSkin(playerID, charID, skinID, ioMessage);
			protocol.setHeroSkinEntity(heroSkinEntity);
		}else{
			HeroSkinEntity heroSkinEntity = heroModule.changeHeroSkin(playerID, charID, skinID, ioMessage);
			protocol.setHeroSkinEntity(heroSkinEntity);
		}
		ioMessage.setProtocol(protocol);
	}
}
