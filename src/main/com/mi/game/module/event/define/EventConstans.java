package com.mi.game.module.event.define;

public class EventConstans {

	// 活动排序
	public static int[] EVENT_SORT = {
			10952,10951,10957,109529,109523,109525,109526,109527,109528,10954,109515,109516,109511,10955,109520,109522,109521,109512,109513
	};

	/** 吃烧鸡 **/
	public static final int EVENT_TYPE_CHICKEN = 10951;
	/** 神秘商店 **/
	public static final int EVENT_TYPE_SHOP = 10954;
	/** 神秘商人 **/
	public static final int EVENT_TYPE_TRADER = 10955;
	/** vip福利 **/
	public static final int EVENT_TYPE_VIPDAILY = 10957;
	/** vip成长 **/
	public static final int EVENT_TYPE_VIPGROW = 10952;
	/** 首冲 **/
	public static final int EVENT_TYPE_FIRSTCHARGE = 10958;
	/** 福利活动 **/
	public static final int EVENT_TYPE_WELFARE = 10953;
	/** 月卡 **/
	public static final int EVENT_TYPE_MONETH_CARD = 109520;
	/** 皇陵探宝 **/
	public static final int EVENT_TYPE_EXPLORE = 109511;
	/** 限时武将 **/
	public static final int EVENT_TYPE_TIME_LIMIT = 109515;
	/** 充值送礼 **/
	public static final int EVENT_TYPE_PAY = 109512;
	/** 消费送礼 **/
	public static final int EVENT_TYPE_EXPENSE = 109513;
	/** 充值抽奖 **/
	public static final int EVENT_TYPE_DRAW_PAY = 109521;
	/** 积分抽奖 **/
	public static final int EVENT_TYPE_DRAW_INTEGRAL = 109522;
	/** 道具兑换 **/
	public static final int EVENT_TYPE_EXCHANGE = 109516;
	/** 冲级活动 **/
	public static final int EVENT_TYPE_LEVELUP = 109523;
	/** 冲级之王 **/
	public static final int EVENT_TYPE_UPKING = 109524;
	/** 战神之王 **/
	public static final int EVENT_TYPE_WARGOD = 109525;
	/** 公会之王 **/
	public static final int EVENT_TYPE_LEGIONKING = 109526;
	/** 决战精英 **/
	public static final int EVENT_TYPE_PKELITE = 109527;
	/** 斩妖除魔 **/
	public static final int EVENT_TYPE_KILLMONSTER = 109528;
	/** 双倍经验 **/
	public static final int EVENT_TYPE_DOUBLEEXP = 109529;
	/** 充值福利 **/
	public static final int EVENT_TYPE_PAYWELFARE = 109530;	
	/** 限时抢购活动 **/
	public static final int EVENT_TYPE_LIMIT_SHOP = 109531;
	/** vip折扣店活动 **/
	public static final int EVENT_TYPE_VIP_DIGO = 109532;
	/** 每日充值 活动 **/
	public static final int EVENT_TYPE_PAY_EVERYDAY = 109533;
	/** 每日消费 活动 **/
	public static final int EVENT_TYPE_CONSUMED_DAILY = 109534;
	/** 每日登录-充值 **/
	public static final int EVENT_TYPE_DAILY_LOGIN = 109535;
	/** 竞技排行 **/
	public static final int EVENT_TYPE_SPORTS_RANK = 109536;
	/** 全民福利 **/
	public static final int EVENT_TYPE_UNIVERSAL_VERFARE = 109537;
	/** 财神送礼 **/
	public static final int EVENT_TYPE_FORTUNA_GIFTS = 109538;
	/** 幸运抽奖 **/
	public static final int EVENT_TYPE_LUCKY_DRAW = 109539;
	/** 吃烧鸡时间 **/
	public static String[] CHICKEN_TIME = {
			"12-14","18-20"
	};
	/** 吃烧鸡恢复体力值 **/
	public static int CHICKEN_VITALITY = 50;
	/** 年月日时间格式 **/
	public static String YMD = "yyyy-MM-dd";
	/** 年月日时分秒时间格式 **/
	public static String YMDHMS = "yyyy-MM-dd HH:mm:ss";

	/** 神秘商店 pid 10721 **/
	public static int MYSTERY_SHOP = 10721;
	/** 神秘商人pid 10722 **/
	public static int MYSTERY_TRADER = 10722;

	/** 神秘商店,商人解锁等级 **/
	public static int MYSTERY_UNLOCKLEVEL = 16;

	/** 神秘商店物品数量 **/
	public static int MYSTERY_SHOP_NUM = 8;
	/** 神秘商人物品数量 **/
	public static int MYSTERY_TRADER_NUM = 6;
	
	/** 拜财神次数 **/
	public static int FORTUNA_GIFTS_NUM = 3;
	
	/** 幸运抽奖免费次数 **/
	public static int LUCKY_DRAW_NUM = 2;

	/**
	 * 行为id
	 */
	/** 神秘商店刷新累计上限 103966 **/
	public static String VIP_MYSTERY_SHOP_REFRESH_MAX = "103966";
	/** 神秘商店vip刷新次数 103965 **/
	public static String VIP_MYSTERY_SHOP_REFRESH = "103965";
	/** 神秘商人vip刷新次数 103984 **/
	public static String VIP_MYSTERY_TRADER_REFRESH = "103984";

	/**
	 * 神秘商店类型
	 */
	/** 获取免费次数 **/
	public static final String MYSTERY_TYPE_RREE_GET = "get";
	/** 购买物品 **/
	public static final String MYSTERY_TYPE_BUY = "buy";
	/** 刷新 **/
	public static final String MYSTERY_TYPE_REFRESH = "refresh";
	/** 金币刷新 **/
	public static final String MYSTERY_TYPE_GOLD_REFRESH = "goldRefresh";
	/** 令牌刷新 **/
	public static final String MYSTERY_TYPE_ITEM_REFRESH = "itemRefresh";
	/** 增加神秘商人存在时间 **/
	public static final String MYSTERY_ADD_EXISITIME = "addExist";
	/** 刷新令id **/
	public static final int MYSTERY_ITEM_REFRESH = 101731;
	/** 神秘商人每次出现时间 **/
	public static final long MYSTERY_TRADER_EXISTTIME = 2 * 3600 * 1000;

	/**
	 * vip 成长礼包
	 */
	/** 购买成长礼包 **/
	public static final String VIP_GROW_TYPE_BUY = "buy";
	/** 领取成长礼包 **/
	public static final String VIP_GROW_TYPE_GET = "get";
	/** 购买礼包金币 **/
	public static final int VIP_GROW_BUY_GOLD = 1000;
	/** 购买礼包所需vip等级 **/
	public static final int VIP_GROW_BUY_LEVEL = 2;

	/**
	 * 福利活动
	 */
	/** 财神送礼活动增加fb次数 **/
	public static final int MAMMON_ACTIVE_NUM = 2;
	/** 经验书马活动增加fb次数 **/
	public static final int EXP_ACTIVE_NUM = 2;

	/**
	 * 皇陵探宝
	 */
	/** 免费探宝 **/
	public static final String EXPLORE_FREE = "free";
	/** 金币探宝 **/
	public static final String EXPLORE_GOLD = "gold";
	/** 五连抽 **/
	public static final String EXPLORE_FIVE = "five";
	/** 皇陵探宝金币价格 **/
	public static final int EXPLORE_PRICE = 20;
	/** 皇陵探宝每日免费次数 **/
	public static final int EXPLORE_FREENUM = 5;
	/** 皇陵探宝金币总次数 **/
	public static final int EXPLORE_GOLDNUM = 1000;
	/** 皇陵探宝掉落配置id **/
	public static final int EXPLORE_DROP_ID = 10831;

	/** 积分抽奖掉落配置id */
	public static final int INTEGRAL_DROP_ID = 10901;
	/** 领取每日首冲奖励 **/
	public static final String DRAW_TYPE_FIRST = "first";
	/** 每日首冲奖励银币数量 **/
	public static final int DRAW_FIRST_TOTAL = 500000;
	/** 刷新道具兑换公式 **/
	public static final String EXCHANGE_TYPE = "refresh";

	/** 经验双倍判断引导id **/
	public static final int GUIDE_PROPERID = 10741;

	/**
	 * vip折扣商店
	 */
	/** 初始化商店商品信息 **/
	public static final String VIP_AGIO_SHOP_INIT = "init";
	/** 购买商品 **/
	public static final String VIP_AGIO_SHOP_BUY = "buy";
	/** 购买历史 **/
	public static final String VIP_AGIO_SHOP_BUY_HISTORY = "history";
}
