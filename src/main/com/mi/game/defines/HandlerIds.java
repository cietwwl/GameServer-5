package com.mi.game.defines;

/**
 * @author 刘凯旋 前后端协议ID类 2014年5月29日 下午2:43:47
 */
public final class HandlerIds {
	/**
	 * 注册消息
	 */

	/** 用户注册 */
	public final static int RegisterUser = 100;
	/** 用户密码登陆 */
	public final static int UserLogin = 101;
	/** 登陆服务器 */
	public final static int ServerLogin = 102;
	/** 用户信息注册 */
	public final static int PlayerInfoInit = 103;
	/** 获取随机名字 */
	public final static int getRandomNameList = 104;
	/** 保存用户新手状态 */
	public final static int saveNewPlayerState = 105;
	/** 小米平台校验用户session登录 */
	public final static int VerifyMiuiSession = 106;
	/** 修改用户姓名 */
	public final static int changePlayerName = 107;
	/** 返回用户新手状态和playerID */
	public final static int getPlayerStatus = 108;
	/** 获取服务器列表 */
	public final static int GetServerList = 109;
	// 第三方平台登录
	public final static int platformLogin = 110;
	/** 修改服务器信息 */
	public final static int ChangeServersInfo = 111;
	/** 获取玩家战斗数据 */
	public final static int getBattleInfo = 112;
	/** 获取玩家基本数据 */
	public final static int getBaseInfo = 113;
	/** 获取玩家副本数据 */
	public final static int getDungeonInfo = 114;
	/** 游客登录 */
	public final static int visitorLogin = 115;
	/** 游客注册 */
	public final static int visitorRegister = 116;
	/** 游客获取服务器列表 */
	public final static int getVisitorServerList = 117;
	/** 修改登录密码 */
	public final static int changePassword = 119;
	/** 获取平台uid **/
	public final static int getPlatFormInfo = 120;
	/** 获取体力耐力 */
	public final static int getVitatlyEntity = 121;

	/**
	 * 英雄操作
	 * */

	/** 添加到组队 */
	public final static int HeroAddToTeam = 200;
	/** 阵型变换 */
	public final static int ChangeTroops = 201;
	/** 武将卸下 */
	public final static int DischargeHero = 202;
	/** 武将抽取 */
	public final static int DrawHero = 203;
	/** 英雄强化 */
	public final static int StrengHero = 204;
	/** 英雄进阶 */
	public final static int AdvanceHero = 205;
	/** 英雄出售 */
	public final static int SellHero = 206;
	/** 英雄背包扩展 */
	public final static int ExpandHeroBag = 207;
	/** 英雄碎片合成 */
	public final static int CompoundHero = 208;
	/** 英雄添加经验 */
	public final static int AddHeroExp = 209;
	/** 获取抽奖信息 */
	public final static int getHeroDrawInfo = 210;
	/** 新手引导武将抽取 */
	public final static int newPlayerDraw = 211;
	/** 修改玩家皮肤 */
	public final static int ChangeHeroSkin = 212;
	/** 获取玩家皮肤 */
	public final static int getHeroSkin = 213;
	/**
	 * 副本操作
	 * */

	/** 副本开始 */
	public final static int dungeonStart = 300;
	/** 恢复副本次数 */
	public final static int recoverDungeonNum = 301;
	/** 领取大关奖励 */
	public final static int getActReward = 302;
	/** 攻打精英副本 */
	public final static int dungeonEliteStart = 303;
	/** 精英副本恢复 */
	public final static int recoverDungeonEliteNum = 304;
	/** 攻打活动副本 */
	public final static int dungeonActiveStart = 305;
	/** 活动副本恢复 */
	public final static int dungeonActiveRecover = 306;
	/** 连续战斗 */
	public final static int ContinuousFight = 307;
	/** 获取副本排行 */
	public final static int getDungeonTopList = 308;
	/** 清除连续战斗冷却时间 */
	public final static int clearContinuousCooldown = 309;
	/** 获取活动副本数据 */
	public final static int GetActiveDungeonInfoHandler = 310;
	/** 获取精英副本数据 */
	public final static int GetEliteDungeonInfo = 311;
	/** 获取普通副本数据 */
	public final static int getNormalDungeonInfo = 312;
	/** 副本复活英雄 */
	public final static int ResurgenceHero = 313;
	/** 购买限时副本奖励 */
	public final static int BuyActLimitReward = 314;
	/**
	 * 装备操作
	 * */
	/** 穿装备 */
	public final static int DoEquip = 400;
	/** 脱装备 */
	public final static int UnEquip = 401;
	/** 装备强化 */
	public final static int StrengEquip = 402;
	/** 装备出售 */
	public final static int SellEquip = 403;
	/** 一键装备 */
	public final static int AtuoEquip = 404;
	/** 扩大装备背包 */
	public final static int ExpandEquipBag = 405;
	/** 装备碎片合成 */
	public final static int EquipShardCompose = 406;
	/** 装备精练 */
	public final static int EquipmentRefine = 407;
	/** 替换/取消装备精炼 */
	public final static int DoRefine = 408;
	/** 扩大装备碎片背包 */
	public final static int ExpandEquipShardBag = 409;
	/** 出售装备碎片 */
	public final static int SellEquipmentShard = 410;
	/** 一键强化 */
	public final static int AutoStrengEquipment = 411;
	/**
	 * 宝物操作
	 * */
	/** 装备宝物 */
	public final static int EquipTalisman = 500;
	/** 脱下宝物 */
	public final static int UnEquipTalisman = 501;
	/** 宝物强化 */
	public final static int StrengTalisman = 502;
	/** 宝物出售 */
	public final static int SellTalisman = 503;
	/** 宝物精练 */
	public final static int RefineTalisman = 504;
	/** 宝物碎片合成 */
	public final static int TalismanShardCompose = 505;
	/** 宝物掠夺界面 */
	public final static int PlunderTalismanList = 506;
	/** 宝物掠夺 */
	public final static int DoPlunderTalisman = 507;
	/** 新手掠夺 */
	public final static int NewPlayerPlunderShard = 508;
	/** 宝物扩充 */
	public final static int ExpendTalismanBag = 509;
	/** 新手宝物掠夺 */
	public final static int NewPlayerPlunderTalismanList = 510;
	/** 获取宝物碎片 */
	public final static int getTalismanShardList = 511;
	/** 连续掠夺 */
	public final static int ContinuesPlunder = 512;
	/** 判断玩家是否可以掠夺 */
	public final static int checkCanPlunder = 513;
	/**
	 * 背包操作
	 * */

	/** 扩展背包 */
	public final static int ExpandBag = 600;
	/** 道具出售 */
	public final static int ItemSell = 601;
	/** 使用道具 */
	public final static int UseItem = 602;
	/** 增加名将经验 */
	public final static int AddManualExp = 603;
	/**
	 * 宠物操作
	 * */

	/** 宠物合成 */
	public final static int CompoundPet = 700;
	/** 宠物训练 */
	public final static int PetTrain = 701;
	/** 宠物出战 */
	public final static int PetWork = 702;
	/** 宠物领悟技能 */
	public final static int LearnPetSkill = 703;
	/** 宠物解锁 */
	public final static int LockPetSkill = 704;
	/** 技能重置 */
	public final static int ResetPetSkill = 705;
	/** 扩展背包 */
	public final static int ExpandPetBag = 706;
	/** 领取特殊技能奖励 */
	public final static int GetSpecialSkillItem = 707;
	/** 传承 */
	public final static int EatPet = 708;
	/** 宠物 进阶 */
	public final static int PetAdvanced = 709;
	/** 使用经验书道具升级 */
	public final static int FeedPet = 710;
	

	/** 炼化操作 */
	/** 炼化 */
	public final static int Smelt = 800;
	/** 重生 */
	public final static int Rebirth = 801;

	/** 占星操作 */

	/** 获取占星信息 */
	public final static int getAstralInfo = 900;
	/** 占星 */
	public final static int DoAstral = 901;
	/** 刷新列表 */
	public final static int RefreshAstralList = 902;
	/** 领取奖励 */
	public final static int GetAstralReward = 903;
	/** 刷新奖励 */
	public final static int RefreshAstralReward = 904;

	/** 试练塔系统 */

	/** 获取试练塔信息 */
	public final static int getTowerEntity = 1000;
	/** 挑战试练塔 */
	public final static int challenegeTower = 1001;
	/** 重置试练塔 */
	public final static int resetTower = 1002;
	/** 扫荡试练塔 */
	public final static int clearTower = 1003;
	/** 取消扫荡 */
	public final static int cancelClear = 1004;
	/** 购买试练塔心数 */
	public final static int buyHeartNum = 1005;
	/** 获取试练塔排行榜 */
	public final static int getTowerTopList = 1006;
	/** 挑战试练塔的隐藏关 */
	public final static int challenegeHideTower = 1007;
	/** 试练塔立即完成 */
	public final static int towerQuicklyComplete = 1008;

	/** 奖励中心操作 */

	/** 获取奖励列表 */
	public final static int getRewardCenterInfo = 1100;
	/** 领取奖励 */
	public final static int getRewardCenterRewrard = 1101;

	/** 天命相关的操作 */
	/** 点击天命 */
	public final static int destinyAdd = 1200;
	/** 获取当前天命星数 */
	public final static int getDestinyNum = 1201;

	/*** 聊天 */
	/** 发送聊天室消息 */
	public final static int CHAT_RECEIVE = 1301;
	/** 用户发送消息 **/
	public final static int CHAT_USER_SEND = 1302;
	/** 修改聊天头像 */
	public final static int ChangeChatPhoto = 1303;

	/** pk相关操作 */
	/** 获取pk列表 */
	public final static int getPkList = 1400;
	/** 获取pk详细信息 */
	public final static int getPkInfo = 1401;
	/** pk 战斗 */
	public final static int pkBattle = 1402;
	/** pk排行 */
	public final static int pkTopList = 1403;
	/** 兑换竞技场道具 */
	public final static int exchangeArenaGoods = 1404;
	/** 初始化竞技场机器人 */
	public final static int initArenaRobot = 1405;
	/** 获取pk详细信息(王翔用) */
	public final static int getPkInfoBak = 1406;

	/** 好友相关 */
	/** 获取好友列表 */
	public final static int getFriendList = 1500;
	/** 获取pk推荐列表 */
	public final static int getRecommendList = 1501;
	/** 增加好友 */
	public final static int addFriend = 1502;
	/** 处理好友请求 */
	public final static int friendHandler = 1503;
	/** 增送好友耐力 */
	public final static int giveFriendEnergy = 1504;
	/** 领取好友耐力 */
	public final static int getFriendEnergy = 1505;
	/** 一键领取好友耐力 */
	public final static int getAllFriendEnergy = 1506;
	/** 留言 */
	public final static int leaveFriendMessage = 1507;
	/** 好友切磋 */
	public final static int friendFight = 1508;
	/** 断绝好友关系 */
	public final static int breakFriendShip = 1509;
	/** 搜索好友 */
	public final static int findPlayerByName = 1510;

	/** 邮箱相关 */
	/** 获取邮箱信息 */
	public final static int getMailBox = 1600;
	/** 为用户添加信息 */
	public final static int addMailInfo = 1601;

	/** 每日任务相关 */
	/** 获取每日任务信息 */
	public final static int getDayTaskInfo = 1700;
	/** 领取每日任务奖励 */
	public final static int getDayTaskReward = 1701;
	/** 获取每日任务信息 */
	public final static int getDayTaskInfoHandler = 1702;

	/** 成就相关 */
	/** 获取成就信息 */
	public final static int getAchievementInfo = 1800;
	/** 领取成就奖励 */
	public final static int getAchievementReward = 1801;

	/** 世界boss */

	/** 获取世界boss消息 */
	public final static int getBossInfo = 1900;
	/** 鼓舞 */
	public final static int inspire = 1901;
	/** 挑战世界Boss */
	public final static int challengeBoss = 1902;
	/** 复活 */
	public final static int worldBossRevive = 1903;
	/** 获取前10名 */
	public final static int getWorldBossTopTen = 1904;
	/** 获取显示列表 */
	public final static int getWorldBossShowList = 1905;

	/** 战斗日志 **/

	/** 获取战斗日志 **/
	public final static int getBattleReportHandler = 2000;
	/** 增加免战时间 */
	public final static int addRefuseBattleHandler = 2100;

	/** 客服系统 */
	/** 提交信息 */
	public final static int postSuggestHandler = 2200;
	/** 查询未提交的信息 */
	public final static int getUnreadInfo = 2201;

	/** 挂机系统 */
	/** 开始挂机 */
	public final static int startFarmHandler = 2300;
	/** 取消挂机 */
	public final static int cancelFarmHandler = 2301;
	/** 领取挂机奖励 */
	public final static int getFarmReward = 2302;
	/** 获取挂机状态 */
	public final static int getFarmHandler = 2303;

	/** 合并服务器 */
	public final static int mergeServer = 2500;

	/** 主线任务 */

	/** 获取任务实体 */
	public final static int getTaskEntity = 2400;
	/** 获取任务奖励 */
	public final static int getTaskReward = 2401;
	/**
	 * 军团相关操作
	 **/

	/** 创建军团 create **/
	public final static int LEGION_CREATE = 3000;
	/** 获取军团列表 getlist **/
	public final static int LEGION_GETLIST = 3001;
	/** 修改军团资料 update **/
	public final static int LEGION_UPDATE = 3002;
	/** 成员管理 manage **/
	public final static int LEGION_MANAGE = 3003;
	/** 加入退出 joinout **/
	public final static int LEGION_JOINOUT = 3004;
	/** 获取申请列表 applys **/
	public final static int LEGION_APPLYS = 3005;
	/** 获取军团信息 info **/
	public final static int LEGION_INFO = 3006;
	/** 获取军团成员列表 members **/
	public final static int LEGION_MEMBERS = 3007;
	/** 军团大厅 hall **/
	public final static int LEGION_HALL = 3008;
	/** 军团商店 shop **/
	public final static int LEGION_SHOP = 3009;
	/** 军团关公殿 gg **/
	public final static int LEGION_GG = 3010;
	/** 军团动态 history **/
	public final static int LEGION_HISTORY = 3011;
	/** 检查是否加入军团 **/
	public final static int LEGION_ISJOIN = 3012;
	/** 修复工会id repair **/
	public final static int LEGION_REPAIR = 3013;

	/**
	 * 活动相关操作
	 */

	/********************/
	/** 吃烧鸡 chicken **/
	public final static int EVENT_CHICKEN = 4000;
	/** 登录礼包 login **/
	public final static int WELFARE_LOGIN = 4001;
	/** 等级礼包 level **/
	public final static int WELFARE_LEVEL = 4002;
	/** 签到礼包 sign **/
	public final static int WELFARE_SIGN = 4003;
	/** 持续在线礼包 online **/
	public final static int WELFARE_ONLINE = 4004;
	/** 成长计划 grow **/
	public final static int EVENT_GROW = 4005;
	/** 福利活动 weal **/
	public final static int EVENT_WEAL = 4006;
	/** 神秘商店 shop **/
	public final static int EVENT_SHOP = 4007;
	/** vip 福利 vip **/
	public final static int EVENT_VIP = 4008;
	/** 皇陵探宝 explore **/
	public final static int EVENT_EXPLORE = 4009;
	/** 充值送礼 pay **/
	public final static int EVENT_PAY = 4010;
	/** 消费送礼 expense **/
	public final static int EVENT_EXPENSE = 4011;
	/** 多买多送 buyMore **/
	public final static int EVENT_BUYMORE = 4012;
	/** 限时热卖 time limit **/
	public final static int EVENT_TIMELIMIT = 4013;
	/** 收集物品兑换道具 exchange **/
	public final static int EVENT_EXCHANGE = 4014;
	/** 邀请好友送礼 invite **/
	public final static int EVENT_INVITE = 4015;
	/** 邀请码功能 inviteCode **/
	public final static int EVENT_INVITECODE = 4016;
	/** 推广员下属关系绑定 Promoter **/
	public final static int EVENT_PROMOTER = 4017;
	/** 获取全部福利 **/
	public final static int WELFARE_ALL = 4018;
	/** 获取全部活动 **/
	public final static int EVENT_ALL = 4019;
	/** 神秘商人 trader **/
	public final static int EVENT_TRADER = 4020;
	/** 月卡 购买 **/
	public final static int EVENT_MONTH_CARD_BUY = 4021;
	/** 月卡 奖励领取 **/
	public final static int EVENT_MONTH_CARD_REWARD = 4022;
	/** 限时抽武将 */
	public final static int TimeLimitDrawHero = 4023;
	/** 限时武将积分排行榜 */
	public final static int TimeLimitScoreList = 4024;
	/** 充值抽奖 **/
	public final static int EVENT_DRAW_PAY = 4025;
	/** 积分抽奖 **/
	public final static int EVENT_DRAW_INTEGRAL = 4026;
	/** 冲级活动 **/
	public final static int EVENT_LEVELUP = 4027;
	/** 冲级之王 **/
	public final static int EVENT_UPKING = 4028;
	/** 战神之王 **/
	public final static int EVENT_WARGOD = 4029;
	/** 公会之王 **/
	public final static int EVENT_LEGIONKING = 4030;
	/** 决战精英 **/
	public final static int EVENT_PKELITE = 4031;
	/** 斩妖除魔 **/
	public final static int EVENT_KILLMONSTER = 4032;
	/** 双倍经验 **/
	public final static int EVENT_DOUBLEEXP = 4033;
	/** 月签到礼包 month **/
	public final static int WELFARE_MONTH = 4034;
	/** 新春鞭炮 **/
	public final static int FESTIVAL_FIRECRACKER = 4035;
	/** 充值加码 **/
	public final static int FESTIVAL_PAYMORE = 4036;
	/** 节日活动 **/
	public final static int FESTIVAL_ACTIVE = 4037;
	/** 情人节兑换钻石 */
	public final static int FESTIVAL_EXCHANGEDIAMOND = 4038;
	/** 战神之王战斗力排行榜 */
	public final static int EVENT_WORGODRANK = 4039;
	/** 解救孙悟空 */
	public final static int WELFARE_RESUCESUNWUKONG = 4040;
	/** 愚人节兑换魔盒 */
	public final static int FESTIVAL_EXCHANGEFOOLDAYMAGICBOX = 4041;
	/** 为五一点赞 **/
	public final static int FESTIVAL_LABORDAY_NICE = 4042;
	/** 五一充值加码 **/
	public final static int FESTIVAL_LABORDAY_PAYMORE = 4043;
	/** 全民福利 **/
	public final static int EVENT_UNIVERSAL_VERFARE = 4044;
	/** 财神送礼 **/
	public final static int EVENT_FORTUNA_GIFTS = 4045;
	/** 幸运抽奖 **/
	public final static int EVENT_LUCKY_DRAW = 4046;
	
	/** vip折扣店 **/
	public final static int EVENT_VIP_DIGO_SHOP = 4047;
	/** 每日充值 领取充值奖励 **/
	public final static int EVENT_PAY_EVERY_DAY_GET_REWARD = 4048;
	/** 每日充值 奖励模版列表 **/
	public final static int EVENT_PAY_EVERY_DAY_INIT = 4049;
	/** 每日充值 发送昨天未领取的奖励 **/
	public final static int EVENT_PAY_EVERY_DAY = 4050;

	/** 每日消费 **/
	public final static int EVENT_EXPENSE_DAILY_INIT = 4051;
	/** 每日消费,领取消费奖励 **/
	public final static int EVENT_EXPENSE_DAILY_GET_REWARD = 4052;

	/** 竞技排行 查询活动奖励信息模版列表 **/
	public final static int EVENT_RANK_REWARD_TEMPLATE_LIST = 4053;

	/**
	 * 每日登录
	 */
	/** 每日登录充值 初始化信息 **/
	public final static int EVENT_DAILY_LOGIN_PAY_INIT = 4054;
	/** 领取登录奖励 **/
	public final static int EVENT_DAILY_LOGIN_GET_LOGINREWARD = 4055;
	/** 领取充值奖励 **/
	public final static int EVENT_DAILY_LOGIN_GET_PAYREWARD = 4056;


		/**
	 * 限时购购买
	 * **/
	/** 购买物品 **/
	public final static int EVENT_LIMIT_SHOP_BUY = 4059;
	/** 商品列表 **/
	public final static int EVENT_LIMIT_SHOP_GOODS_LIST = 4060;
	
	
	/** 充值送礼 **/
	public final static int EVENT_PAY_DATA = 4100;
	/** 消费送礼 **/
	public final static int EVENT_EXPENSE_DATA = 4101;
	/** 充值抽奖 **/
	public final static int EVENT_DRAW_PAY_DATA = 4102;
	/** 全民福利配置文件数据 **/
	public final static int EVENT_UNIVERSAL_VERFARE_DATA = 4103;
	/** 幸运抽奖配置文件数据 **/
	public final static int EVENT_LUCKY_DRAW_DATA = 4104;
	/**
	 * vip相关
	 */
	/** 获取vip信息 **/
	public final static int VIP_GETINFO = 5000;
	/** 购买vip 次数 **/
	public final static int VIP_PERMISSION = 5001;

	/**
	 * 师徒关系相关
	 */
	/** 拜师 **/
	public final static int RELATION_PUPIL = 6000;
	/** 收徒 **/
	public final static int RELATION_MASTER = 6001;
	/** 拜师管理 **/
	public final static int RELATION_PUPIL_MANAGE = 6002;
	/** 收徒管理 **/
	public final static int RELATION_MASTER_MANAGE = 6003;
	/** 师傅列表 */
	public final static int RELATION_MASTER_LIST = 6004;
	/** 徒弟列表 */
	public final static int RELATION_PUPIL_LIST = 6005;

	/**
	 * 比武模块
	 */
	/** 初始化可参加比武玩家数据 **/
	public final static int PK_INIT_PLAYER = 2600;
	/** 查询玩家比武积分等信息 **/
	public final static int PK_GET_PK_INFO = 2601;
	/** 寻找比武对手 **/
	public final static int PK_FIND_PLAYER = 2602;
	/** 新增比武记录 **/
	public final static int PK_CREATE_INFO = 2603;
	/** 重置比武积分等数据 **/
	public final static int PK_CLEAR_POINTS = 2604;
	/** 计算比武排名,发放奖励 **/
	public final static int PK_SEND_REWARD = 2605;
	/** 检查是否可以进行比武 **/
	public final static int PK_SENK_PK_ASK = 2606;
	/** 比武查询复仇信息 **/
	public final static int PK_GET_REVENGE_INFO = 2607;
	/** 兑换奖品 **/
	public final static int PK_EXCHANGE_PRIZE = 2608;
	/** 查询积分排行榜单(前十名) **/
	public final static int PK_RANK_TOP_TEN = 2609;

	/**
	 * 公告模块
	 */
	public final static int NOTICE_INFO = 7000;
	/**
	 * cdk相关
	 */
	/** cdk使用 **/
	public final static int CDK_USE = 7500;
	/** 获取cdk类型 **/
	public final static int CDK_TYPE = 7501;
	/** 获取bug用户列表 **/
	public final static int CDK_BUG = 7502;

	/**
	 * 统计相关
	 */
	/** 统计数据收集 **/
	public final static int ANALY_INFO = 7777;
	/** 客户端统计 **/
	public final static int CLIENT_ANALY_INFO = 7776;
	/** 设备统计 **/
	public final static int CLIENT_ANALY_DEVICE = 7778;

	/**
	 * 充值相关
	 */
	/** 充值金币 **/
	public final static int PAY_GOLD = 8000;
	/** 获取首冲信息 **/
	public final static int PAY_INFO = 8001;
	/** 创建订单 **/
	public final static int PAY_CREATE_ORDER = 8002;
	/** 获取订单 **/
	public final static int PAY_GET_ORDER = 8003;
	/** appstore订单验证 **/
	public final static int PAY_APPSTORE = 8004;
	/** 金立平台订单创建 **/
	public final static int JINLI_CREATE_ORDER = 8005;
	/** VIVO平台订单创建 **/
	public final static int VIVO_CREATE_ORDER = 8006;
	/** COOLPAD平台订单创建 */
	public final static int COOLPAD_CREATE_ORDER = 8007;
	/** 腾讯平台支付,修改订单状态 **/
	public final static int PAY_TENCENT = 8008;
	/** 支付统计 **/
	public final static int PAY_ANALY = 8888;

	/**
	 * 商城相关
	 **/
	/** 商城购买道具 **/
	public final static int STORE_BUY = 9000;
	/** 获取商城购买信息 **/
	public final static int STORE_INFO = 9001;

	// ------------------ 管理后台 命令 10000 开始
	// session验证
	public final static int adminSession = 10001;
	public final static int SendMessage = 10100;
	// 用户登录
	public final static int adminLogin = 10002;
	// 权限管理
	public final static int adminRoleManage = 10003;
	// 用户管理
	public final static int adminManage = 10004;
	// 用户权限分类管理
	public final static int adminCategoryManage = 10005;

	public final static int UpdateInitialTask = 10101;
	/** 查询列表 */
	public final static int adminQueryList = 11111;
	/** 显示实体 */
	public final static int adminQueryEntity = 11112;
	/** 更新实体 */
	public final static int adminUpdateEntity = 11113;
	/** 删除实体 */
	public final static int adminDeleteEntity = 11114;
	/** 增加实体 */
	public final static int adminCreateEntity = 11115;
	/** 更新long实体 */
	public final static int adminUpdateLongEntity = 11116;
	/** 删除long实体 */
	public final static int adminDeleteLongEntity = 11117;
	// 查询配置数据列表
	public final static int confdataListCMD = 11121;
	// 查询配置数据命令id
	public final static int confdataShowCMD = 11122;
	// 更新配置数据命令id
	public final static int confdataUpdateCMD = 11123;
	// 删除配置数据命令id
	public final static int confdataDeleteCMD = 11124;
	// 刷新配置数据
	public final static int confdataResetCMD = 11125;
	// 增加物品
	public final static int addItemCMD = 11126;
	// 生成cdk
	public final static int createCDKCMD = 11127;
	// 发放系统奖励
	public final static int sendSystemReward = 11128;
	// 时间转换
	public final static int longTimeQuery = 11130;
	/** 修改服务器状态 */
	public final static int changeServerStatus = 11131;
	/** 按平台发放奖励 */
	public final static int sendPlatformReward = 11132;
	/** 远程获取更新配置文件 */
	public final static int remoteUpdateTemplate = 11133;
	/** 更新配置文件切重启服务器 */
	public final static int updateTemplateAndRebot = 11134;
	/** 更新白名单配置文件 */
	public final static int updateOpenPlayerList = 11135;
	/** 获取充值信息文件 */
	public final static int getPayInfoFile = 11136;
	/** 停号删除英雄 */
	public final static int clearHero = 11137;
}
