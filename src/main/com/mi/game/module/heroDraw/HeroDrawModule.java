package com.mi.game.module.heroDraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.DrawHeroType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.InformationMessageType;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.chat.ChatModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.hero.data.HeroData;
import com.mi.game.module.hero.protocol.HeroInfoProtocol;
import com.mi.game.module.heroDraw.dao.HeroDrawDAO;
import com.mi.game.module.heroDraw.pojo.HeroDrawEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.pojo.PayEntity;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;

@Module(name = ModuleNames.HeroDrawModule, clazz = HeroDrawModule.class)
public class HeroDrawModule extends BaseModule {
	private final HeroDrawDAO heroDrawDAO = HeroDrawDAO.getInstance();
	private int NormalDraw = 10651;
	private int BetterDraw = 10652;
	private int BestDraw = 10653;
	private int SpecialDraw = 10654;
	private int NewPlayerDraw = 10655;
	private int FirstBlue = 10659;
	private int FirstPurple = 106511;
	private int NewPlayerPurpleDraw =106510;
	private int valentineDrop = 1037706;
	private int foolDayDrop = 1037719;
	/**
	 * 获取抽奖的实体
	 * */
	public HeroDrawEntity getEntity(String playerID) {
		HeroDrawEntity entity = heroDrawDAO.getEntity(playerID);
		if (entity == null) {
			entity = initEntity(playerID);
			this.saveDrawEntity(entity);
		}
		return entity;
	}

	/**
	 * 初始化抽奖实体
	 * */
	public HeroDrawEntity initEntity(String playerID) {
		HeroDrawEntity entity = new HeroDrawEntity();
		entity.setLastBestDrawTime(System.currentTimeMillis() + DateTimeUtil.ONE_HOUR_TIME_MS);
		entity.setDrawNum(4);
		entity.setKey(playerID);
		return entity;
	}

	/**
	 * 保存抽奖实体
	 * */
	public void saveDrawEntity(HeroDrawEntity entity) {
		heroDrawDAO.save(entity);
	}

	public void draw(String playerID, int drawID, Map<String, Object> itemMap, Map<String, GoodsBean> showMap, IOMessage ioMessage) {
		int heroPid = this.drawHero(drawID);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, heroPid, 1, null, true, showMap, itemMap, ioMessage);
	}

	/**
	 * 抽取武将
	 * */
	public void drawHero(String playerID, int drawType, HeroInfoProtocol protocol, IOMessage ioMessage) {
		HeroDrawEntity entity = this.getEntity(playerID);
		int bestNum = entity.getDrawNum();
//		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
//		HeroEntity heroEntity = heroModule.getHeroEntity(playerID, ioMessage);
		long nowTime = System.currentTimeMillis();
//		Map<String, Hero> heroMap = heroEntity.getHeroMap();
//		int size = heroMap.size();
//		int maxHeroNum = heroEntity.getMaxHeroNum();
//		if (size > maxHeroNum) {
//			protocol.setCode(ErrorIds.HeroBagNumWrong);
//			return;
//		}
		Map<String, Object> itemMap = new HashMap<>();
		int pid = 0;
		int currency = 0;
		int drawID = 0;
		int messageType = 0;
		switch (drawType) {
		case DrawHeroType.NormalDraw:
			pid = DrawHeroType.RecruitItem;
			currency = 1;
			drawID = NormalDraw;
			int normalNum = entity.getNormalDrawNum();
			if (normalNum < SysConstants.newNormalDrawNum) {
				normalNum += 1;
				entity.setNormalDrawNum(normalNum);
				drawID = SysConstants.newNormalDrawDropID;
			}
			messageType = InformationMessageType.normalDraw;
			break;
		case DrawHeroType.BetterDraw:
			pid = KindIDs.GOLDTYPE;
			long betterDrawTime = entity.getLastBetterDrawTime();
			if (betterDrawTime <= nowTime) {
				currency = 0;
				entity.setLastBetterDrawTime(nowTime + 24 * DateTimeUtil.ONE_HOUR_TIME_MS);
			} else {
				currency = DrawHeroType.BetterDrawGold;
			}
			drawID = BetterDraw;
			messageType = InformationMessageType.betterDraw;
			break;
		case DrawHeroType.BestDraw:
			pid = KindIDs.GOLDTYPE;
			long bestDrawTime = entity.getLastBestDrawTime();
			if (bestDrawTime <= nowTime) {
				currency = 0;
				entity.setLastBestDrawTime(nowTime + 48 * DateTimeUtil.ONE_HOUR_TIME_MS);
			} else {
				currency = DrawHeroType.BestDrawGold;
			}
			messageType = InformationMessageType.bestDraw;
			drawID = BestDraw;
			break;
		case DrawHeroType.TenBestDraw:
			pid = KindIDs.GOLDTYPE;
			currency = DrawHeroType.TenBestDrawGold;
			drawID = BestDraw;
			messageType = InformationMessageType.tenBestDraw;
			break;
		case DrawHeroType.newDraw:
			if (entity.getNewPlayerBatterDraw() == 0) {
				drawID = NewPlayerDraw;
				entity.setNewPlayerBatterDraw(1);
			} else {
				drawID = BetterDraw;
			}
			pid = KindIDs.GOLDTYPE;
			if (entity.getLastBetterDrawTime() <= nowTime) {
				currency = 0;
				entity.setLastBetterDrawTime(nowTime + 24 * DateTimeUtil.ONE_HOUR_TIME_MS);
			} else {
				currency = DrawHeroType.BetterDrawGold;
			}
			messageType = InformationMessageType.betterDraw;
			break;
		case DrawHeroType.firstBlue:
			if (entity.getFirstBule() == 0) {
				drawID = FirstBlue;
				entity.setFirstBule(1);
			} else {
				drawID = BetterDraw;
			}
			pid = KindIDs.GOLDTYPE;
			currency = DrawHeroType.BetterDrawGold;
			messageType = InformationMessageType.betterDraw;
			break;
		case DrawHeroType.firstPurple:
			if (entity.getFirstPurple() == 0) {
				drawID = FirstPurple;
				entity.setFirstPurple(1);
			} else {
				drawID = BestDraw;
			}
			pid = KindIDs.GOLDTYPE;
			currency = DrawHeroType.BestDrawGold;
			messageType = InformationMessageType.bestDraw;
			break;
		case DrawHeroType.newPlayerPurpleDraw:
			pid = KindIDs.GOLDTYPE;
			long bestTime = entity.getLastBestDrawTime();
			if (bestTime <= nowTime) {
				currency = 0;
				entity.setLastBestDrawTime(nowTime + 48 * DateTimeUtil.ONE_HOUR_TIME_MS);
			} else {
				currency = DrawHeroType.BestDrawGold;
			}
			messageType = InformationMessageType.bestDraw;
			if (entity.getNewPlayerBestDraw() == 0) {
				drawID = NewPlayerPurpleDraw;
				entity.setNewPlayerBestDraw(1);
			} else {
				drawID = BestDraw;
			}
			break;
		default:
			logger.error("错误的抽取类型");
			protocol.setCode(ErrorIds.WrongHeroDrawType);
			return;
		}
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, pid, currency, 0, false, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}

		// ////
		// // 元宝消耗记录
		// ///
		if (pid == KindIDs.GOLDTYPE && currency != 0) {
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			analyseModule.goldCostLog(playerID, currency, 1, currency, "drawHero" + messageType, "heroDraw");
		}

		List<GoodsBean> addHeroList = new ArrayList<GoodsBean>();
		int drawNum = 0;

		if (drawType != DrawHeroType.TenBestDraw) {
			drawNum = 1;
		} else {
			drawNum = 10;
		}
		int heroPid = 0;
		
		if(currency >= DrawHeroType.BestDrawGold){
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.BESTDRAWHERO, 0, null);
		}
		if (BestDraw == drawID) {
			PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
			PayEntity payEntity = payModule.getPayEntity(playerID);
			if (payEntity == null) {
				drawID = SysConstants.newBestDrawDropID;
			}
		}

		for (int i = 0; i < drawNum; i++) {
			GoodsBean heroBean = new GoodsBean();
			heroBean.setNum(1);
			if (drawID == BestDraw || drawID == SysConstants.newBestDrawDropID) {
				if (bestNum == 0) {
					heroPid = this.drawHero(SpecialDraw);
					bestNum = 9;
				} else {
					bestNum -= 1;
					heroPid = this.drawHero(drawID);
				}
			} else {
				heroPid = this.drawHero(drawID);
			}
			heroBean.setPid(heroPid);
			addHeroList.add(heroBean);
		}
		entity.setDrawNum(bestNum);

		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
		if (festivalModule.isInValentine()) {
			if (drawType == DrawHeroType.TenBestDraw) {
				GoodsBean goodsBean = DropModule.doDrop(valentineDrop);
				rewardModule.addGoods(playerID, goodsBean.getPid(), goodsBean.getNum(), 0, true, null, itemMap, ioMessage);
				festivalModule.addValentineDrawGold(playerID, goodsBean.getNum());
				protocol.setValentineGold(goodsBean.getNum());
			}
		}
		if(festivalModule.isInFoolDay()){
			if (drawType == DrawHeroType.TenBestDraw) {
				GoodsBean goodsBean = DropModule.doDrop(foolDayDrop);
				rewardModule.addGoods(playerID, goodsBean.getPid(), goodsBean.getNum(), 0, true, null, itemMap, ioMessage);
				festivalModule.addFoolDayDrawGold(playerID, goodsBean.getNum());
				protocol.setValentineGold(goodsBean.getNum());
			}
		}

		this.saveDrawEntity(entity);
		Map<String, GoodsBean> showMap = new HashMap<String, GoodsBean>();
		rewardModule.addGoods(playerID, addHeroList, true, showMap, itemMap, ioMessage);
		List<String> names = new ArrayList<>();
		int index = 0;
		for (GoodsBean goodsBean : addHeroList) {
			int templateID = goodsBean.getPid();
			HeroData data = TemplateManager.getTemplateData(templateID, HeroData.class);
			int quality = data.getQuality();
			if (quality >= 5) {
				String name = data.getName();
				names.add(name);
				index++;
			}
		}
		if (index > 0) {
			ChatModule chatModule = ModuleManager.getModule(ModuleNames.ChatModule, ChatModule.class);
			chatModule.addInformation(playerID, messageType, 0, names.toArray());
		}
		protocol.setHeroDrawEntity(entity);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(showMap);
	}

	/**
	 * 抽取武将
	 * */
	private int drawHero(int drawID) {
		GoodsBean heroBean = DropModule.doDrop(drawID);
		return heroBean.getPid();
	}

	/**
	 * 新手抽取蓝将
	 * */
	public void newPlayerDraw() {

	}
}
