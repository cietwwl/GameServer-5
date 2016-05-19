package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.pojo.PayEntity;
import com.mi.game.module.vip.VipModule;

public class PlayerEntityManager extends BaseEntityManager<PlayerEntity> {

	public PlayerEntityManager() {
		this.dao = PlayerEntitiyDAO.getInstance();
	}

	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		queryInfo.setOrder("-playerID");
		String playerID = (String) ioMessage.getInputParse("playerID");
		String nickName = (String) ioMessage.getInputParse("nickName");
		String level = (String) ioMessage.getInputParse("level");
		String vipLevel = (String) ioMessage.getInputParse("vipLevel");
		if (StringUtils.isNotBlank(playerID)) {
			queryInfo.addQueryCondition("playerID", playerID);
		}
		if (StringUtils.isNotBlank(nickName)) {
			queryInfo.addQueryCondition("nickName", QueryType.LIKE, nickName);
		}
		if (StringUtils.isNotBlank(level)) {
			queryInfo.addQueryCondition("level", Integer.parseInt(level));
		}
		if (StringUtils.isNotBlank(vipLevel)) {
			queryInfo.addQueryCondition("vipLevel", Integer.parseInt(vipLevel));
		}
		return dao.queryPage(queryInfo);
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {

		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String playerID = (String) ioMessage.getInputParse("playerID");
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		PlayerEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String nickName = (String) ioMessage.getInputParse("nickName");
		String sex = (String) ioMessage.getInputParse("sex");
		String level = (String) ioMessage.getInputParse("level");
		String vipLevel = (String) ioMessage.getInputParse("vipLevel");
		if (StringUtils.isNotBlank(nickName)) {
			entity.setNickName(nickName);
		}
		if (StringUtils.isNotBlank(sex)) {
			entity.setSex(Integer.parseInt(sex));
		}
		if (StringUtils.isNotBlank(level)) {
			HeroEntity heroEntity = heroModule.getHeroEntity(playerID);
			long leadID = heroEntity.getLeadID();
			Hero hero = heroEntity.getHeroMap().get(leadID + "");
			hero.setLevel(Integer.parseInt(level));
			heroModule.saveHeroEntity(heroEntity);
			entity.setLevel(Integer.parseInt(level));
		}
		if (StringUtils.isNotBlank(vipLevel)) {
			int vipLevelInt = Integer.parseInt(vipLevel.trim());
			PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
			VipModule vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
			int payTotal = vipModule.getVipDataByLevel(vipLevelInt).getPrice();
			PayEntity payEntity = payModule.getPayEntity(playerID);
			if (payEntity == null) {
				payEntity = new PayEntity();
				payEntity.setPlayerID(playerID);
			}
			payEntity.setPayTotal(payTotal);
			payModule.savePayEntity(payEntity);
			entity.setVipLevel(vipLevelInt);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
