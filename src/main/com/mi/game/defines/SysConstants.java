package com.mi.game.defines;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mi.core.util.ConfigUtil;

public class SysConstants {
	/** 统一的cdk服务器ID */
	public static String CDKSERVERID = "0";
	/** 英雄唯一开始ID */
	public static int heroStartID = 100000;
	/** 装备唯一开始ID */
	public static int equipStartID = 1;
	/** 宝物唯一开始ID */
	public static int talismanStartID = 1;
	/** 宠物唯一开始ID */
	public static int petStatID = 1;
	/** 订单唯一开始ID */
	public static int orderStatID = 1;
	/** 战斗日志唯一起始ID */
	public static int reportStartID = 1;
	/** 机器人起始ID */
	public static int robotStartID = 1;
	/** 宝物碎片起始ID */
	public static int talismanShardStartID = 1;
	/** 客服消息起始ID */
	public static int suggestStartID = 1;
	/** 系统奖励起始ID */
	public static int systemRewardStartID = 1;
	/** 游客起始ID */
	public static int visitorStartID = 10000; 
	/** 装备ID实体名称 */
	public static String equipIDEntity = "equipIDEntity";
	/** 宝物ID实体名称 */
	public static String talismanEntity = "talismanEntity";
	/** 宠物ID实体名称 */
	public static String petIDEntity = "petIDEntity";
	/** 英雄ID实体名称 */
	public static String heroIDEntitiy = "heroIDEntity";
	/** 订单ID实体名称 */
	public static String orderIDEntity = "orderIDEntity";
	/** 战斗日志ID实体名称 */
	public static String reportIDEntity = "reportIDEntity";
	/** 活动配置ID实体名称 */
	public static String eventIDEntity = "eventIDEntity";
	/** 机器人ID */
	public static String robotIDEntity = "robotIDEntity";
	/** 宝物碎片ID实体名称 */
	public static String talismanShardEntity = "talismanShardEntity";
	/** 客服消息ID实体名称 */
	public static String suggestIDEntity = "suggestIDEntity";
	/** 游客ID实体名称 */
	public static String visitorIDEntity = "visitorIDEntity";

	/** cdk ID **/
	public static String cdkIDEntity = "cdkIDEntity";
	/** 系统奖励ID */
	public static String systemRewardEntity = "systemRewardIDEntity";
	/** AdminRoleEntity **/
	public static String AdminRoleIDEntity = "AdminRoleIDEntity";
	/** AdminCategoryEntity **/
	public static String AdminCategoryIDEntity = "AdminCategoryIDEntity";
	/** AdminUserEntity **/
	public static String AdminUserIDEntity = "AdminUserIDEntity";
	/** NoticeEntity **/
	public static String NoticeIDEntity = "NoticeIDEntity";
	/** CDKRewardEntity **/
	public static String CDKRewardIDEntity = "CDKRewardIDEntity";
	/** CDKTypeEntity **/
	public static String CDKTypeIDEntity = "CDKTypeIDEntity";
	/** LegionEntity **/
	public static String LegionIDEntity = "LegionEntity";
	/** LegionHistoryEntity **/
	public static String LegionHistoryIDEntity = "LegionHistoryIDEntity";
	/** DeviceIDEntity **/
	public static String DeviceIDEntity = "DeviceIDEntity";
	/** PayOrderEntity **/
	public static String PayOrderIDEntity = "PayOrderIDEntity";
	/**CDKEntity **/
	public static String CDKIDEntity = "CDKIDEntity";
	
	/** 副本起始ID */
	public static int dungeonStartID = 10101;
	/** 大关起始ID */
	public static int dungeonActStartID = 10111;
	/** 精英关起始ID */
	public static int dungeonEliteStartID = 10321;
	/** 大关ID */
	public static int actID = 1;
	/** 最大的体力 */
	public static int maxVitatly = 150;
	/** 初始能量 */
	public static int initEnergy = 20;
	/** 耐力恢复时间 */
	public static long recoverEnergyTime = 900000;
	/** 体力恢复时间 */
	public static long recoverVitatlyTime = 360000;
	/** 初始化背包格子数 */
	public static int bagInitNum = 30;
	/** 初始化英雄背包格子数 */
	public static int heroBagInitNum = 100;
	/** 初始化宠物背包格子数 */
	public static int petBagInitNum = 20;
	/** 初始化碎片数 */
	public static int fragmentNum = 50;
	/** 背包扩充个数 */
	public static int bagSellAddNum = 5;
	/** 背包扩充价格基数 */
	public static int bagSellAddPrice = 25;
	/** 洗炼初始洗炼石 */
	public static int refineStartStone = 1;
	/** 洗练石ID */
	public static int refineStoneID = 10176;
	/** 洗练消耗的洗炼石 */
	public final static int refineStone = 1;
	/** 洗炼消耗的银币 */
	public final static int refineSilver = 3000;
	/** 洗炼消耗的金币 */
	public final static int refineGold = 5;
	/** 重置宠物技能消耗的金币 */
	public final static int resetPetSkillGold = 40;
	/** 男主角 */
	public final static int maleHero = 10431;
	/** 女主角 */
	public final static int femaleHero = 10432;
	/** 强攻令 */
	public final static int addDungeonItem = 101729;
	/** 精英副本最大付费数 */
	public final static int elitePayNum = 3;
	/** 精英副本免费次数 */
	public final static int eliteFreeNum = 3;
	/** 聊天清除的时间 */
	public final static int MsgDelTime = 600000;
	/** vip强化等级上限 行为id **/
	public final static String STRENG_MAX = "103968";
	/** vip强化等级下限 行为id **/
	public final static String STRENG_MIN = "103967";
	/** 经验书马副本id */
	public static final int BOOK_HORSE_ID = 10512;
	/** 经验书马活动id */
	public static final int BOOK_HORSE_ACTIVE_ID = 10812;
	/** 摇钱树副本id */
	public static final int MONEY_TREE_ID = 10513;
	/** 摇钱树活动id */
	public static final int MONEY_TREE_ACTIVE_ID = 10811;
	/** 竞技场奖励翻倍活动id **/
	public static final int ARENA_ACTIVE_ID = 10814;
	/** 精彩活动限时抽奖 需要的金币 */
	public static final int TimeLimitDrawHero_GOLD = 280;
	/** 男主角ID */
	public static final int MaleHeroID = 1042101;
	/** 女主角ID */
	public static final int FemaleHeroID = 1042201;
	/** 生命 */
	public static final int Hp = 100011;
	/** 攻击 */
	public static final int Attack = 100012;
	/** 物防 */
	public static final int PDef = 100013;
	/** 魔防 */
	public static final int MDef = 100014;
	/** 最终伤害 */
	public static final int FinalAttack = 100015;
	/** 最终免伤 */
	public static final int FinalDef = 100016;
	/** 加值属性集合 */
	public static final int[] addProp = new int[] { 100011, 100012, 100013, 100014 };
	/** 生命百分比 */
	public static final int HpPer = 100081;
	/** 攻击百分比 */
	public static final int AttackPer = 100082;
	/** 物防百分比 */
	public static final int PDefPer = 100083;
	/** 魔防百分比 */
	public static final int MDefPer = 100084;
	/** 最大好友数 */
	public static final int MaxFriendNum = 100;
	/** 经验银宝 */
	public static final int ExpSilverBook = 101637;
	/** 经验银马 */
	public static final int ExpSilverHourse = 101635;
	/** 复活的基础银币消耗 */
	public static final int resurgenceBaseSilver = 100;
	/** 竞技场奖励时间 **/
	public static String[] ARENA_TIME = { "22-23"};
	/** 聊天屏蔽关键字 */
	public static String[] chatShieldedKeywordArray = ConfigUtil.getStringArray("chatShieldedKeywordArray");
	/** 屏蔽关键字转换后的内容 */
	public final static String chatShieldedKeywordToString = ConfigUtil.getString("chatShieldedKeywordToString", "***");

	static {
		// 聊天屏蔽关键字数组排序
		Arrays.sort(chatShieldedKeywordArray);
		List<String> tmpList = Arrays.asList(chatShieldedKeywordArray);
		Collections.reverse(tmpList);
		chatShieldedKeywordArray = tmpList.toArray(chatShieldedKeywordArray);
	}
	/** 世界boss一档伤害奖励 */
	public static int worldBossFristGrade = 109912;
	/** 世界boss二档伤害奖励 */
	public static int worldBossSecondGrade = 109913;
	/** 世界boss三档伤害奖励 */
	public static int worldBossThirdGrade = 109914;
	/** 空掉落 */
	public static int emptyPid = 9999;
	/** 最大强化等级 */
	public static int maxStrengLevel = 500;
	/** 伐树令 */
	public static int TreeItem = 101730;
	/** 刷新战斗力的人数 */
	public static int FightValueFreshNum = 1000;
	/** 青铜宝箱掉落 */
	public static int copperBoxDropID = 103730;
	/** 白银宝箱掉落 */
	public static int silverBoxDropID = 103731;
	/** 黄金宝箱掉落 */
	public static int goldBoxDropID = 103732;
	/** 新手青铜宝箱掉落 */
	public static int newCopperBoxDropID = 1037707;
	/** 新手白银宝箱掉落 */
	public static int newSilverBoxDropID = 1037708;
	/** 新手黄金宝箱掉落 */
	public static int newGoldBoxDropID  =1037709 ;
	/** 新手良将抽卡包 */
	public static int newNormalDrawDropID = 1065017 ;
	/** 新手神将抽卡包 */
	public static int newBestDrawDropID = 1065018 ;
	/** 新手宝箱次数 */
	public static int newOpenBoxNum = 5;
	/** 新手良将次数 */
	public static int newNormalDrawNum = 50;
	
	/** 比武模块 **/
	/** 每天比武次数上限 **/
	public static final int PK_MAX_NUM = 15;
	/** 比武初始积分数 **/
	public static final int PK_INIT_POINTS = 1000;
	/** 比武每天消耗耐力值上限 **/
	public static final int PK_MAX_ENERGY = 30;
}
