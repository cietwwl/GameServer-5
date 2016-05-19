package com.mi.game.module.astral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.astral.dao.AstralDAO;
import com.mi.game.module.astral.pojo.AstralEntity;
import com.mi.game.module.astral.pojo.AstralNeedInfo;
import com.mi.game.module.astral.pojo.AstralReward;
import com.mi.game.module.astral.protocol.AstralProtocol;
import com.mi.game.module.bag.BagModule;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.bag.pojo.BagItem;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.reward.RewardModule;

/**
 * @author 刘凯旋 占星 2014年7月7日 下午1:51:55
 */
@Module(name = ModuleNames.AstralModule, clazz = AstralModule.class)
public class AstralModule extends BaseModule {

	private static AnalyseModule analyseModule;
	private static final int normalAbsralID = 1;
	private static final int advanceAbstalID = 2;
	private static final AstralDAO astralDAO = AstralDAO.getInstance();
	private static final int astralItemID = 12345;
	private static final int freshDrawGold = 10;
	private static final int freshRewardGold = 50;

	@Override
	public void init() {
		analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
	}

	/**
	 * 获取实体
	 * */
	public AstralEntity getEntity(String playerID) {
		// 自动刷新
		return astralDAO.getEntity(playerID);
	}

	/**
	 * 保存实体
	 * */
	public void saveEntity(AstralEntity astralEntity) {
		astralDAO.save(astralEntity);
	}

	/**
	 * 初始化占星实体
	 * */
	public void initAstralEntity(String playerID, int type) {

		AstralEntity entity = new AstralEntity();
		entity.setKey(playerID);
		entity.setPoint(16);
	}

	/**
	 * 刷新占星抽取列表
	 * */
	public void refreshDrawList(String playerID, IOMessage ioMessage, AstralProtocol protocol) {
		AstralEntity entity = this.getEntity(playerID);
		int freeNum = entity.getFreeNum();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule, BagModule.class);
		BagEntity bagEntity = bagModule.getBagEntity(playerID, ioMessage);
		Map<Integer, BagItem> bagList = bagEntity.getBagList();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		if (freeNum < 1) {
			int pid = 0;
			int num = 0;
			if (bagList.containsKey(astralItemID)) {
				pid = astralItemID;
				num = 1;
				protocol.setItemDelNum(1);
			} else {
				pid = KindIDs.GOLDTYPE;
				num = freshDrawGold;

				// ////
				// // 元宝消耗记录
				// ///
				analyseModule.goldCostLog(playerID, num, 1, freshRewardGold, "refreshDrawList", "astral");
			}
			int code = rewardModule.useGoods(playerID, pid, num, 0, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			protocol.setItemMap(itemMap);
		} else {
			entity.setFreeNum(freeNum - 1);
		}
		List<Integer> drawList = this.getDrawList();
		protocol.setDrawList(drawList);
		protocol.setFreeNum(entity.getFreeNum());
		entity.setDrawList(drawList);
		this.saveEntity(entity);
	}

	private List<Integer> getDrawList() {
		List<Integer> drawList = new ArrayList<>(5);
		return drawList;
	}

	/** 刷新占星需求列表 */
	private List<AstralNeedInfo> getNeedList() {
		List<AstralNeedInfo> needList = new ArrayList<AstralNeedInfo>(4);
		return needList;
	}

	/**
	 * 占星
	 * */
	public void doAstral(String playerID, int starID, AstralProtocol protocol, IOMessage ioMessage) {
		AstralEntity entity = this.getEntity(playerID);
		int nowNum = entity.getNowNum();
		int point = entity.getPoint();
		if (nowNum < 1) {
			logger.error("占星次数不足");
			protocol.setCode(ErrorIds.AstralNumNotEnough);
			return;
		}
		List<Integer> drawList = entity.getDrawList();
		if (!drawList.contains(starID)) {
			logger.error("占星ID错误");
			protocol.setCode(ErrorIds.AstralIDWrong);
			return;
		}
		boolean isAdd = false;
		boolean update = true;
		int addPoint = 1;
		List<AstralNeedInfo> needList = entity.getNeedList();
		for (AstralNeedInfo needInfo : needList) {
			if (needInfo.getPid() == starID && needInfo.getState() == 0 && !isAdd) {
				needInfo.setState(1);
				isAdd = true;
			}
			if (needInfo.getState() == 0) {
				update = false;
			}
		}
		if (update) {
			entity.setNeedList(this.getNeedList());
			addPoint = entity.getAddPoint();
			int times = entity.getPointTimes();
			// entity.setAddPoint(this.get0);
			// entity.setTimes(times + 1);
		}
		entity.setDrawList(this.getDrawList());
		entity.setNowNum(nowNum - 1);
		entity.setPoint(point + addPoint);
		this.saveEntity(entity);
		protocol.setAstralEntity(entity);
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.intefaceDrawIntegral(playerID, 10894);
	}

	/**
	 * 获取奖励
	 * */
	public void getAstralReward(String playerID, int rewardPoint, AstralProtocol protocol) {
		AstralEntity entity = this.getEntity(playerID);
		int point = entity.getPoint();
		if (point < rewardPoint) {
			logger.error("点数不足");
			protocol.setCode(ErrorIds.AstralPointNotEnough);
			return;
		}
		Map<Integer, AstralReward> rewardList = entity.getRewardList();
		AstralReward reward = rewardList.get(rewardPoint);
		if (reward == null) {
			logger.error("不存在该奖励");
			protocol.setCode(ErrorIds.AstralRewardNotFound);
			return;
		}
		if (reward.getState() == 1) {
			logger.error("奖励已领取");
			protocol.setCode(ErrorIds.AstralRewardAlreadyGet);
			return;
		}
		reward.setState(1);
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.addGoods(playerID, reward.getPid(), reward.getNum(), null, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		protocol.setAstralReward(reward);
		protocol.setItemMap(itemMap);
	}

	/**
	 * 刷新奖励
	 * */
	public void refreshAstralReward(String playerID, AstralProtocol protocol) {
		AstralEntity entity = this.getEntity(playerID);
		int refeshNum = entity.getRefreshRewardNum();
		Map<Integer, AstralReward> rewardList = entity.getRewardList();
		for (Entry<Integer, AstralReward> entry : rewardList.entrySet()) {
			AstralReward reward = entry.getValue();
			if (reward.getState() == 1) {
				logger.error("奖励已领取,不可刷新");
				protocol.setCode(ErrorIds.AstralRewardNotRefresh);
				return;
			}
		}
		if (refeshNum >= entity.getMaxRefreshRewardNum()) {
			logger.error("已达到今日刷新上限");
			protocol.setCode(ErrorIds.AstralRewardFreshMaxNum);
			return;
		}
		int gold = (refeshNum + 1) * freshRewardGold;
		Map<String, Object> itemMap = new HashMap<String, Object>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, null);

		if (code != 0) {
			protocol.setCode(code);
			return;
		}

		Map<Integer, AstralReward> freshRewardList = this.initAstralRewar();
		entity.setRefreshRewardNum(refeshNum + 1);
		entity.setRewardList(freshRewardList);
		protocol.setItemMap(itemMap);
		protocol.setAstralRewardList(this.map2List(freshRewardList));

		// ////
		// // 元宝消耗记录
		// ///
		analyseModule.goldCostLog(playerID, gold, refeshNum + 1, freshRewardGold, "refreshAstralReward", "astral");

	}

	private List<AstralReward> map2List(Map<Integer, AstralReward> freshRewardList) {
		List<AstralReward> beanList = new ArrayList<>();
		for (AstralReward bean : freshRewardList.values()) {
			beanList.add(bean);
		}
		return beanList;
	}

	/**
	 * 初始化奖励列表
	 * */
	private Map<Integer, AstralReward> initAstralRewar() {
		Map<Integer, AstralReward> map = new HashMap<>();
		return map;
	}
}
