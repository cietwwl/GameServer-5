package com.mi.game.module.effect;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.EffectType;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.effect.dao.PlayerEffectDAO;
import com.mi.game.module.effect.pojo.Effect;
import com.mi.game.module.effect.pojo.PlayerEffectEntity;
import com.mi.game.module.effect.protocol.EffectProtocol;
import com.mi.game.module.reward.RewardModule;

@Module(name = ModuleNames.EffectModule, clazz = EffectModule.class)
public class EffectModule extends BaseModule {
	private final static PlayerEffectDAO playerEffectDao = PlayerEffectDAO.getInstance();

	@Override
	public void init() {
	}

	public PlayerEffectEntity initPlayerEffectEntity(String playerID) {
		PlayerEffectEntity playerEffectEntity = new PlayerEffectEntity();
		playerEffectEntity.setKey(playerID);
		return playerEffectEntity;
	}

	public PlayerEffectEntity getPlayerEffectEntity(String playerID) {
		PlayerEffectEntity playerEffectEntity = playerEffectDao.getEntity(playerID);
		if (playerEffectEntity == null) {
			playerEffectEntity = this.initPlayerEffectEntity(playerID);
			this.setPlayerEffectEntity(playerEffectEntity);
			// throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return playerEffectEntity;
	}

	public void setPlayerEffectEntity(PlayerEffectEntity playerEffectEntity) {
		playerEffectDao.save(playerEffectEntity);
	}

	public void syncSetPlayerEffectEntity(Effect effect, String playerID, Integer key) {
		PlayerEffectEntity entity = this.getPlayerEffectEntity(playerID);
		entity.getEffectMap().put(key, effect);
		this.setPlayerEffectEntity(entity);
	}

	public void addEffect(String playerID, int templateID, long endTime, HashMap<String, Object> map) {
		PlayerEffectEntity playerEffectEntity = this.getPlayerEffectEntity(playerID);
		Map<Integer, Effect> effectMap = playerEffectEntity.getEffectMap();
		Effect effect = new Effect(templateID, endTime, map);
		effectMap.put(templateID, effect);
		this.setPlayerEffectEntity(playerEffectEntity);
	}

	/***
	 * 
	 * 根据类型返回剩余时间
	 * 
	 * */
	public long getEndTime(String playerID, Integer effectType) {
		PlayerEffectEntity entity = this.getPlayerEffectEntity(playerID);
		Effect effect = entity.getEffectMap().get(effectType);
		if (effect != null) {
			return effect.getEndTime();
		}
		return 0;
	}

	/**
	 * 增加免战的时间
	 * */
	public void addRefuseBattleBuff(String playerID, int payType, IOMessage ioMessage, EffectProtocol protocol) {
		int pid = 0;
		int num = 0;
		if (payType == 1) {
			pid = 101728;
			num = 1;
		} else {
			pid = KindIDs.GOLDTYPE;
			num = 20;
		}
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, pid, num, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		protocol.setItemMap(itemMap);
		PlayerEffectEntity playerEffectEntity = this.getPlayerEffectEntity(playerID);
		Map<Integer, Effect> effectMap = playerEffectEntity.getEffectMap();
		Effect effect = effectMap.get(EffectType.refuseBattle);
		long nowTime = System.currentTimeMillis();
		if (effect == null) {
			effect = new Effect(EffectType.refuseBattle, nowTime + 4 * DateTimeUtil.ONE_HOUR_TIME_MS, null);
		} else {
			long endTime = effect.getEndTime();
			if (endTime < nowTime) {
				endTime = nowTime + 4 * DateTimeUtil.ONE_HOUR_TIME_MS;
			} else {
				endTime = endTime + 4 * DateTimeUtil.ONE_HOUR_TIME_MS;
			}
			effect.setEndTime(endTime);
		}
		effectMap.put(EffectType.refuseBattle, effect);
		this.setPlayerEffectEntity(playerEffectEntity);
		protocol.setEffect(effect);

		// ////
		// // 元宝消耗记录
		// ///
		if (pid == KindIDs.GOLDTYPE) {
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			analyseModule.goldCostLog(playerID, num, 1, num, "addRefuseBattleBuff", "effect");
		}

	}
}
