package com.mi.game.defines;

public class ErrorIds {
	public final static int SERVER_ERROR = 10000;
	/** 用户名不合法 */
	public final static int UserNameIllegal = 20000;
	/** 密码不合法 */
	public final static int PasswdIllegal = 20001;
	/** 用户已注册 */
	public final static int UserRegistered = 20002;
	/** 用户名/密码错误 */
	public final static int PasswdWrong = 20003;
	/** 银币不足 */
	public final static int NotEnoughSilver = 20004;
	/** 金币不足 */
	public final static int NotEnoughGold = 20005;
	/** 将魂不足 */
	public final static int NotEnoughHeroSoul = 20006;
	/** 已装备相同的武将 */
	public final static int SameHeroAdd = 20007;
	/** 武将位置未解锁 */
	public final static int HeroPostionWrong = 20008;
	/** 该关卡未解锁 */
	public final static int DungeonLocked = 20009;
	/** 超过关卡最大难度 */
	public final static int OverDungeonDifficulty = 20010;
	/** 超过关卡的最大攻打次数 */
	public final static int OverMaxDungeonAttackNum = 20011;
	/** 需完成前一等级的关卡 */
	public final static int FirstDungeonNotFinish = 20012;
	/** 挑战星级错误 */
	public final static int StarLevelError = 20013;
	/** 体力不足 */
	public final static int NotEnoughVitatly = 20014;
	/** 耐力不足 */
	public final static int NotEnoughEnergy = 20015;
	/** 背包已满 */
	public final static int BagFull = 20016;
	/** 装备未找到 */
	public final static int EquipNotFound = 20017;
	/** 武将未找到 */
	public final static int HeroNotFound = 20018;
	/** 装备未装备 */
	public final static int UnEquip = 20019;
	/** 达到目前强化最高等级 */
	public final static int EquipmentStrengLevel = 20020;
	/** 该品质装备不可出售 */
	public final static int EquipQualityNotSell = 20021;
	/** 不可穿戴同一件装备 */
	public final static int DontSameEquip = 20022;
	/** 已装备的装备不可出售 */
	public final static int DontSellEquiped = 20023;
	/** 魂玉不足 */
	public final static int NotEnoughJewelSoul = 20024;
	/** 背包数量错误 */
	public final static int BagNumWrong = 20025;
	/** 英雄背包超过上限 */
	public final static int HeroBagNumWrong = 20026;
	/** 错误的抽取类型 */
	public final static int WrongHeroDrawType = 20027;
	/** 宝物未找到 */
	public final static int TalismanNotFound = 20028;
	/** 道具未找到 */
	public final static int NotFoundItem = 20029;
	/** 道具不可出售 **/
	public final static int ItemDontSell = 20030;
	/** 道具背包为空 */
	public final static int ItemBagEmpty = 20031;
	/** 钱包实体为空 */
	public final static int WalletNull = 20032;
	/** 错误的英雄强化类型 */
	public final static int WrongHeroStrengType = 20033;
	/** 英雄已达到最大进阶等级 */
	public final static int MaxClassLevel = 20034;
	/** 未知的消耗类型 */
	public final static int UnKnowType = 20035;
	/** 宝物背包为空 */
	public final static int TalismanBagEmpty = 20036;
	/** 英雄等级不足 */
	public final static int HeroLevelLow = 20037;
	/** 英雄不可出售 **/
	public final static int HeroDontSell = 20038;
	/** 英雄碎片不足 */
	public final static int HeroShardNotEnoungh = 20039;
	/** 宠物碎片不存在 */
	public final static int PetShardNotFound = 20040;
	/** 宠物碎片不足 */
	public final static int PetSHardNotEnough = 20041;
	/** 宠物背包已满 */
	public final static int PetBagFull = 20042;
	/** 宠物正在训练 */
	public final static int PetIsTained = 20043;
	/** 宠物栏未解锁 */
	public final static int PetFildLocked = 20044;
	/** 宠物未找到 */
	public final static int PetNotFound = 20045;
	/** 宠物已出战 */
	public final static int PetWorked = 20046;
	/** 宠物点数不足 */
	public final static int PetSkillPointNotEnough = 20047;
	/** 宠物技能未找到 */
	public final static int PetSkillNotFound = 20048;
	/** 宠物技能已锁定 */
	public final static int PetSkillLocked = 20049;
	/** 宠物不需要重置 */
	public final static int NotResetPetSkill = 20050;
	/** 重置技能点数异常 */
	public final static int ResetPointWrong = 20051;
	/** 未到道具领取时间 */
	public final static int ArrivalGetItemTime = 20052;
	/** 宠物没有特殊技能 */
	public final static int NoPetSpecialSkill = 20053;
	/** 不可吞噬自身 */
	public final static int NoEatSelf = 20054;
	/** 装备碎片未找到 */
	public final static int EquipmentShardNotFound = 20055;
	/** 装备碎片不足 */
	public final static int EquipmentShardNotEnough = 20056;
	/** 装备背包已满 */
	public final static int EquipmentShardBagFull = 20057;
	/** 错误的精练类型 */
	public final static int WrongRefineType = 20058;
	/** 宝物不可出售 */
	public final static int TalismanNotSell = 20059;
	/** 宝物不可精炼 */
	public final static int TalismanNotRefine = 20060;
	/** 宝物精练数据错误 */
	public final static int TalismanDataWrong = 20061;
	/** 没有可以替换的属性 */
	public final static int NoRefineChangeProperty = 20062;
	/** 名将没有找到 */
	public final static int ManualNotFound = 20063;
	/** 好感度等级上限 */
	public final static int ManualMaxLevel = 20064;
	/** 装备中的装备不可炼化 */
	public final static int EquipedNotSmelt = 20065;
	/** 装备不可炼化 */
	public final static int EquipmentNotSmelt = 20066;
	/** 装备中的宝物不可炼化 */
	public final static int EquipedTalismanNotSmelt = 20067;
	/** 英雄不可炼化 */
	public final static int HeroNotSmelt = 20068;
	/** 宝物不可炼化 */
	public final static int TalismanNotSmelt = 20069;
	/** 宝物背包已满 */
	public final static int TalismanBagFull = 20070;
	/** 装备不可重生 */
	public final static int EquipmentNotRebirth = 20071;
	/** 英雄不可重生 */
	public final static int HeroNotRebirth = 20072;
	/** 法宝不可重生 */
	public final static int TalismanNotRebirth = 20073;
	/** 占星ID错误 */
	public final static int AstralIDWrong = 20074;
	/** 占星次数不足 */
	public final static int AstralNumNotEnough = 20075;
	/** 占星点数不足 */
	public final static int AstralPointNotEnough = 20076;
	/** 占星奖励不存在 */
	public final static int AstralRewardNotFound = 20077;
	/** 占星奖励已领取 */
	public final static int AstralRewardAlreadyGet = 20078;
	/** 已达到今日刷新上限 */
	public final static int AstralRewardFreshMaxNum = 20079;
	/** 奖励已领取不可刷新 */
	public final static int AstralRewardNotRefresh = 20080;
	/** 掉落数据异常 */
	public final static int DropDataError = 20081;
	/** 主角不可强化 */
	public final static int HeroNoStreng = 20082;
	/** 不可移除主角 */
	public final static int HeroNoRemove = 20083;
	/** 用户未注册 */
	public final static int UserNoRegister = 20084;
	/** 随机库已空 */
	public final static int RandomNameListEmpty = 20085;
	/** 名字已被使用 */
	public final static int SameName = 20086;
	/** 不存在该数据 */
	public final static int NoEntity = 20087;
	/** 关卡未找到 */
	public final static int DungeonNotFound = 20088;
	/** 还有剩余次数 不能恢复 */
	public final static int DungeonAttackNumRemain = 20089;
	/** 试炼塔实体为空 */
	public final static int TowerEntityIsNull = 20090;
	/** 武将不可进阶 */
	public final static int HeroCouldNotAdvance = 20091;
	/** 数据库故障 */
	public final static int MongoNotFound = 20092;
	/** 副本大关数据为空 */
	public final static int DungeonActNull = 20093;
	/** 副本大关奖励点数不足 */
	public final static int ActPointNotEnough = 20094;
	/** 大关奖励不存在 */
	public final static int ActRewardNotFound = 20095;
	/** 大关奖励已领取 */
	public final static int ActRewardGeted = 20096;
	/** 精英副本的次数不足 */
	public final static int EliteNumNotEnough = 20097;
	/** 精英副本未开启 */
	public final static int EliteNotFound = 20098;
	/** 副本购买次数超过上限 */
	public final static int ElitePayNumEnough = 20099;
	/** 试练塔挑战ID错误 */
	public final static int TowerAttackIDWrong = 20100;
	/** 试练塔挑战次数不足 */
	public final static int TowerHeartNumNotEnough = 20101;
	/** 试炼塔重置次数不足 */
	public final static int TowerResetNumNOtEnough = 20102;
	/** 试练塔已达到最大层数 请重置 */
	public final static int TowerGetMaxLevel = 20103;
	/** 试练塔不可扫荡 */
	public final static int TowerNotClear = 20104;
	/** 活动副本攻打次数不足 */
	public final static int ActiveDungeonNumNotEnough = 20105;
	/** 活动副本未找到 */
	public final static int ActiveDungeonNotFound = 20106;
	/** 活动副本不在时限内 */
	public final static int ActiveDungeonTimeWrong = 20107;
	/** 活动副本有剩余次数 不可购买 */
	public final static int ActiveDungeonNotReset = 20108;
	/** 活动副本购买次数达到上限 */
	public final static int ActiveDungonMax = 20109;
	/** 试练塔扫荡中 */
	public final static int TowerIsClearing = 20110;
	/** 有剩余次数 无法购买 */
	public final static int HeartNotBuy = 20111;
	/** 天命星数不足 */
	public final static int DestinyStarNotEnough = 20112;
	/** 碎片不足 */
	public final static int ShardNotEnough = 20113;
	/** 不存在该碎片 **/
	public final static int ShardNotFound = 20114;
	/** 试练塔扫存档层数错误 */
	public final static int TowerLevelWrong = 20115;
	/** 用户不存在 */
	public final static int PlayerNotExist = 20116;
	/** 已有该碎片 不可掠夺 */
	public final static int AlreadyHaveShard = 20117;
	/** 不能跟自己聊天 */
	public final static int canNotChatSelf = 20118;
	/** 声望不足 */
	public final static int NotEnoughReputation = 20119;
	/** 竞技场商店道具id错误 */
	public final static int ArenaShopIDWrong = 20120;
	/** 竞技场道具数量不足 */
	public final static int ArenaShopItemNotEnough = 20121;

	/** 已提交好友请求,请等待回复 */
	public final static int FriendApplySend = 20122;
	/** 错误的好友申请处理 */
	public final static int FriendIndexWrong = 20123;
	/** 已经是好友 */
	public final static int AlreadyFriend = 20124;
	/** 不是好友 */
	public final static int NoFriend = 20125;
	/** 已赠送耐力 */
	public final static int AlreadyGivePresent = 20126;
	/** 超过每日好友耐力上限 */
	public final static int FriendPresentMax = 20127;
	/** 该好友未增送耐力 */
	public final static int FriendNoGivePresent = 20128;
	/** 上阵武将无法卸下 */
	public final static int TroopsHeroUnDischarge = 20129;
	/** 不可进行好友切磋 */
	public final static int NoFriendFight = 20130;
	/** 超过好友上限 */
	public final static int MaxFriendNum = 20131;
	/** 用户未找到 */
	public final static int NotFoundPlayer = 20132;
	/** 搜索关键字错误 */
	public final static int SearchNameWrong = 20133;
	/** 错误的领取耐力id */
	public final static int EnergyIndexWrong = 20134;
	/** 已领取该任务奖励 */
	public final static int AlreadyGetQuestReward = 20135;
	/** 错误的任务奖励ID */
	public final static int WrongQuestRewardID = 20136;
	/** 任务所需的积分不足 */
	public final static int QuestScoreNotEnough = 20137;
	/** 成就id错误 */
	public final static int AchievementIDNotFound = 20138;
	/** 成就未到达领取条件 */
	public final static int AchievementNoReach = 20139;
	/** 错误的英雄位置 */
	public final static int WrongHeroPostion = 20140;
	/** 挑战时间未到 */
	public final static int BossAttackTimeNoReach = 20141;
	/** boss不可攻击 */
	public final static int BossCouldNotAttack = 20142;
	/** 鼓舞已满 */
	public final static int MaxInspireNum = 20143;
	/** 错误的鼓舞类型 */
	public final static int WrongInspireType = 20144;
	/** 鼓舞冷缺时间未到 */
	public final static int InspireCoolDown = 20145;
	/** 连战冷却时间未到 */
	public final static int ContinueBattleColldown = 20146;
	/** 世界bossjob数据错误 */
	public final static int WorldBossJobDataWrong = 20147;

	/** 月卡未到期，不能购买 */
	public final static int MONTH_CARD_CANNOT_BUY = 20148;
	/** 您没有购买月卡或月卡已过期 */
	public final static int MONTH_CARD_NOT_BUY = 20149;
	/** 月卡奖励已领取 */
	public final static int MONTH_CARD_HAD_REWARD = 20150;

	/** 隐藏关不存在 */
	public final static int HideLevelNoExist = 20151;
	/** 今天已免费抽取过了 */
	public final static int TimeLimitDrawHero_HadReward = 20152;

	/** 领取奖励的ID错误 */
	public final static int RewardIDWrong = 20153;

	/** 没有碎片 不可抢夺 */
	public final static int NoTalismanShard = 20154;
	/** 支付类型错误 */
	public final static int PAY_TYPE_ERROR = 20155;
	/** 支付金额错误 */
	public final static int PAY_MONEY_ERROR = 20156;
	/** 没有该模板 */
	public final static int NOTEMPLATE = 20157;
	/** 不可作为强化材料 */
	public final static int NoStrengHero = 20158;
	/** 错误的强化类型 */
	public final static int WrongStrengType = 20159;
	/** 对方好友已满 */
	public final static int TargerFriendNumFull = 20160;
	/** 请求数据错误 */
	public final static int RequestParseError = 20161;
	/** 重复登录 */
	public final static int ConfirmLogin = 20162;
	/** 连续攻打15级开放 */
	public final static int ContinuousFightLevel = 20163;
	/** 道具不足 */
	public final static int ItemNotEnough = 20164;
	/** 武将等级不能超过主角等级 */
	public final static int HeroLevelNoExceedPlayerLevel = 20165;
	/** 试练塔不可立即完成 */
	public final static int TowerNoQuickComplete = 20166;
	/** 装备不可精炼 */
	public final static int EquipmentNoRefine = 20167;
	/** 玩家不能Pk自己 */
	public final static int PlayerNotPkSelf = 20168;
	/** 已上阵该英雄 */
	public final static int HeroAlreadyInTroops = 20169;
	/** 装备背包为空 */
	public final static int EquipmentBagIsEmpty = 20170;
	/** 错误的客服消息类型 */
	public final static int WrongSuggestType = 20171;
	/** 宝物不可强化 */
	public final static int TalismanNotIntensify = 20172;
	/** 装备精炼次数错误 */
	public final static int EquipmentRefineNum = 20173;
	/** 错误的名将等级 */
	public final static int ManualWrongLevel = 20174;
	/** 保护中不可抢夺 */
	public final static int InTalismanProtect = 20175;
	/** 进阶的英雄不可炼化 */
	public final static int AdvanceHeroNotSmelt = 20176;
	/** 名字长度不合法 */
	public final static int NameLength = 20177;
	/** 名字含非法关键字 */
	public final static int NameNoLegal = 20178;
	/** 不在抢夺时间内 */
	public final static int NoInPlunderTime = 20179;
	/** 竞技场发送奖励中 不可攻击 */
	public final static int AreanRewardStaus = 20180;
	/** 不能加自己为好友 **/
	public final static int NotAddFriendMyself = 20181;
	/** 参数错误 */
	public final static int ParamWrong = 20182;
	/** 复活英雄的次数错误 */
	public final static int ResurgenceNumWrong = 20183;
	/** 玩家不在线 */
	public final static int PlayerNoOnline = 20184;
	/** uid为空或session为空 */
	public final static int UIDEmpty = 20185;
	/** session验证错误 */
	public final static int SessionWrong = 20186;
	/** 不可越级挑战 */
	public final static int ArenaRankWrong = 20187;
	/** 已领取该奖励 */
	public final static int AlreadyGetReward = 20188;
	/** 不在挂机状态 */
	public final static int NoInFarmState = 20189;
	/** 没有挂机奖励可以领取 */
	public final static int NoFarmReward = 20190;
	/** 已经在挂机中 */
	public final static int NoStartFarm = 20191;
	/** 奖励不能为空 */
	public final static int RewardNotEmpty = 20192;
	/** 限时奖励兑换时间已过 */
	public final static int ActLimitTimeOut = 20193;
	/** 不可兑换限时奖励 */
	public final static int NoChangeLimitReward = 20194;
	/** 聊天长度不合法 */
	public final static int ChatMessageLengthError = 20195;
	/** 主角位置不可替换 */
	public final static int HeroPostionNoChange = 20196;
	/** 情人节兑换钻石金币不足 */
	public final static int NotEnoughValentineGold = 20197;
	/** 设备ID未注册 */
	public final static int PhoneIDNoRegister = 20198;
	/** 设备ID已绑定 */
	public final static int PhoneIDBind = 20199;
	/** 邮箱格式错误 */
	public final static int EmailFormatWrong = 20200;
	/** 邮箱已注册 */
	public final static int EamilRegistered = 20201;
	/** 原始密码错误 */
	public final static int OrginalPasswordWrong = 20202;
	/** 碎片不足 不能合成 */
	public final static int shardNotCompose = 20203;
	/** 留言长度不合法 */
	public final static int leaveMessageLength = 20204;
	/** 没有当前等级的强化数据 */
	public final static int noEquipmentUpData = 20205;
	/** 奖励未领取,不可挂机 */
	public final static int rewardNotGet = 20206;
	/** 挂机翻倍vip等级不足 */
	public final static int FARMVIPLEVELNOENOUGH = 20207;
	/** 已经领取过孙悟空 */
	public final static int ALREADYGETSUNWUKONG = 20208;
	/** 正在解救孙武空 */
	public final static int RESUCEINGSUNWUKONG = 20209;
	/** 未到解救时间 */
	public final static int NOTREACHRESUCETIME = 20210;
	/** 已购买限时礼包 */
	public final static int ALREADYBUYPACKAGE = 20211;
	/** 主角等级小于3级不可强化护法 */
	public final static int LEADLEVELSTRENGEHERONOENOUGH = 20212;
	/** 已领取过筋斗云 */
	public final static int ALREADYGETCLOUD = 20213;
	/** 需要先解救孙悟空 */
	public final static int NEEDRESUCEMONKY = 20214;
	/** 获得筋斗云的等级不足 */
	public final static int GETCLOUDLEVELNOTENOUGH = 20215;
	/** 账号不存在 */
	public final static int ACCOUNTNOTEXITS = 20216;
	/** 未达到任务领取条件 */
	public final static int NOREACHTASKGOAL = 20217;
	/** 愚人节兑换魔盒金币不足 */
	public final static int NotEnoughFoolDayGold = 20218;
	
	/** 宠物技能锁定数量已满 */
	public final static int PET_SKILL_LOCK_EMP = 20220;
	/** 宠物已满级 */
	public final static int PET_EXP_EMP = 20221;
	/** 被传承宠物不能比传承宠物等级高 */
	public final static int PET_EAT_EMP = 20222;
	/** 已达到最高阶 */
	public final static int PET_IS_MAXADV = 20223;
	/** 进阶宠物与宠物碎片不是同一种 */
	public final static int PET_IS_SHARD = 20224;
	/** 宠物已经存在 */
	public final static int PETEXIST = 20225;
	/** 技能已满级 */
	public final static int PET_KILL_EMP = 20226;

	/******** 比武相关 **********/
	/** 自己没有足够的耐力值 **/
	public final static int PK_NOT_ENOUGH_ENERGY_SELF = 20310;
	/** 自己比武次数达到上限 **/
	public final static int PK_NUM_REACH_LIMIT_SELF = 20311;
	/** 兑换物品数量错误 **/
	public final static int PK_EXCHANGE_NUM_ERROR = 20312;
	/** 比武兑换奖励荣誉值不足 **/
	public final static int PK_EXCHANGE_REWARD_NOTENOUGH = 20313;
	/** 积分远超其他玩家 **/
	public final static int PK_POINT_TOO_BIG = 20314;
	/** 达到每周兑换上限 **/
	public final static int PK_TOP_WEEK = 20315;
	/** 达到每天兑换上限 **/
	public final static int PK_TOP_DAY = 20316;
	/** 达到领取上限 **/
	public final static int PK_TOP_HISTORY = 20317;
	/** 比武发送奖励job数据错误 */
	public final static int PKSENDREWARDJJOBDATAWRONG = 20318;
	/** 没有合适的比武对象 **/
	public final static int PK_HAVE_NO_MATCH = 20319;
	 
	public static int update_entity_error;
	public static int delete_entity_error;
	public static int user_data_error;
	

	/*********** 军团相关 *********/
	/*** 等级不足 **/
	public final static int LEGION_LEVELINSUFFICIENT = 25000;
	/*** 银币不足 **/
	public final static int LEGION_SILVERINSUFFICIENT = 25001;
	/*** 金币不足 **/
	public final static int LEGION_GOLDINSUFFICIENT = 25002;
	/*** 军团名不能空 **/
	public final static int LEGION_NAMENOTNULL = 25003;
	/*** 军团名已存在 **/
	public final static int LEGION_NAMEISEXIST = 25004;
	/*** 军团名长度不合法 **/
	public final static int LEGION_NAMELENGTHERROR = 25005;
	/*** 军团宣言不能为空 **/
	public final static int LEGION_DECLARATIONNOTNULL = 25006;
	/*** 军团宣言长度不合法 **/
	public final static int LEGION_DECLARATIONLENGTHERROR = 25007;
	/*** 你已加入一个军团 **/
	public final static int LEGION_ISHAVE = 25008;
	/*** 加入/创建军团冷却中 **/
	public final static int LEGION_JOINCOOLING = 25009;
	/*** 没有加入一个军团 **/
	public final static int LEGION_NOTJOIN = 25010;
	/*** 军团id不能为空 **/
	public final static int LEGION_ID_ISNOTNULL = 25011;
	/*** 军团不存在 **/
	public final static int LEGION_ISNOTHING = 25012;
	/*** 军团公告不能为空 **/
	public final static int LEGION_NOTICENOTNULL = 25013;
	/*** 军团公告长度不合法 **/
	public final static int LEGION_NOTICELENGTHERROR = 25014;
	/*** 军团权限不足 **/
	public final static int LEGION_NOTMANAGE = 25015;
	/*** 军团参数错误 **/
	public final static int LEGION_PARAMEERROR = 25016;
	/*** 申请加入军团个数不能超过3个 **/
	public final static int LEGION_JOINNUMERROR = 25017;
	/*** 玩家不存在 **/
	public final static int LEGION_PLAYERNOTEXIST = 25018;
	/*** 军团长无法退出 **/
	public final static int LEGION_LEGATUSNOTOUT = 25019;
	/*** 没有申请加入该军团 **/
	public final static int LEGION_NOTAPPLYJOIN = 25020;
	/*** 已经申请加入该军团 **/
	public final static int LEGION_ISAPPLYJOIN = 25021;
	/*** 原密码不能为空 **/
	public final static int LEGION_OLDPASSISNULL = 25022;
	/*** 新密码不能为空 **/
	public final static int LEGION_NEWPASSISNULL = 25023;
	/*** 新密码长度错误 **/
	public final static int LEGION_NEWPASSLENGTHERROR = 25024;
	/*** 密码错误 **/
	public final static int LEGION_PASSERROR = 25025;
	/*** 军团用户已满 **/
	public final static int LEGION_MEMBERISFULL = 25026;
	/*** 用户已经加入其他军团或取消申请 **/
	public final static int LEGION_MEMBERJOINOTHERORCANCAL = 25027;
	/** 超过副军团长允许的最大数量 **/
	public final static int LEGION_LEGATUSISFULL = 25028;
	/** 军团建设方式不能为空 **/
	public final static int LEGION_BUILDTYPENULL = 25029;
	/** 当天已经建设 **/
	public final static int LEGION_ISBUILD = 25030;
	/** 军团贡献不足 **/
	public final static int LEGION_DEVOTESUFFICIENT = 25031;
	/** 建筑已到达最高级 **/
	public final static int LEGION_BUILDLEVELMAX = 25032;
	/** 参拜次数已用完 **/
	public final static int LEGION_VISITISMAX = 25033;
	/** 没有申请人 **/
	public final static int LEGION_APPLYSISNULL = 25034;
	/** 没有发现此物品 */
	public final static int LEGION_NOTFOUNDITEM = 25035;
	/** 购买物品id不能为空 **/
	public final static int LEGION_ITEMISNULL = 25036;
	/** 已经购买完此物品 **/
	public final static int LEGION_ITEMISBUY = 25037;
	/** 物品已经卖完 **/
	public final static int LEGION_ITEMSELLOUT = 25038;
	/** 军团长不能被踢出 **/
	public final static int LEGION_NOKILLLEGATUS = 25039;
	/** 副军团长不能被踢出 **/
	public final static int LEGION_NOKILLLEGATUS2 = 25040;
	/** 不在参拜时间内 **/
	public final static int LEGION_VISTER_TIME_ERROR = 25041;
	/** 升级条件不足 **/
	public final static int LEGION_UPGRADE_MISS = 25042;
	/** 还有其它军团成员 **/
	public final static int LEGION_HAS_MEMBER = 25043;

	/** 已经吃过烧鸡 **/
	public static int CHICKEN_ISEAT = 25044;
	/** 不在活动时间 **/
	public static int EVENT_NOINTIME = 25045;
	/** 已经领取 **/
	public static int REWARD_ISGET = 25046;
	/** 等级不足 **/
	public static int REWARD_LEVELWRONG = 25047;
	/** 神秘商人,神秘商店类型不能为空 **/
	public static int MYSTERY_TYPE_NOTNULL = 25048;
	/** 神秘物品id 不能为空 **/
	public static int MYSTERY_ID_NOTNULL = 25049;
	/** 重复购买 **/
	public static int MYSTERY_BUYAGAIN = 25050;
	/** 物品不存在 **/
	public static int MYSTERY_NOTHING = 25051;
	/** 刷新次数不足 **/
	public static int MYSTERY_REFRESH_ZERO = 25052;
	/** 神秘商人不存在 **/
	public static int MYSTERY_TRADER_NOTHING = 25053;
	/** vip 等级不足 **/
	public static int REWARD_VIPLEVEL_ERROR = 25054;
	/** 购买后才可领取 **/
	public static int REWARD_VIPGROW_NOTBUY = 25055;
	/** 皇陵探宝类型不能为空 **/
	public static int EXPLORE_TYPE_NOTNULL = 25056;
	/** 皇陵探宝次数不足 **/
	public static int EXPLORE_NUM_ERROR = 25057;
	/** 奖励物品类型不能为空 **/
	public static int REWARD_ID_NOTNULL = 25058;
	/** 奖励物品已领取 **/
	public static int REWARD_ITEM_ISHAS = 25059;
	/** 领取条件不足 **/
	public static int REWARD_NOTENOUGH = 25060;
	/** 奖励不存在 **/
	public static int REWARD_NOTFOUND = 25061;
	/** 抽奖积分不足 **/
	public static int DRAW_INTEGRAL_NOTENOUGH = 25062;
	/** 抽奖类型不能为空 **/
	public static int DRAW_TYPE_NOTNULL = 25063;
	/** 充值抽奖金额不足 **/
	public static int DRAW_PAY_NOTENOUGH = 25064;
	/** 重复充值抽奖 **/
	public static int DRAW_PAY_AGAIN = 25065;
	/** 领取每日首冲奖励类型错误 **/
	public static int DRAW_PAY_TYPE_ERROR = 25066;
	/** 道具兑换pid不能为空 **/
	public static int EXCHANGE_PID_NOTNULL = 25067;
	/** 没有发现兑换道具 **/
	public static int EXCHANGE_PID_NOTFOUND = 25068;
	/** 道具兑换类型错误 **/
	public static int EXCHANGE_TYPE_ERROR = 25069;
	/** 兑换次数不足 **/
	public static int EXCHANGE_NUM_ENOUGH = 25070;

	/** 已经领取 **/
	public static int WELFARE_REWARD_ISGET = 25080;
	/** 没有发现奖励 **/
	public static int WELFARE_REWARD_NOTFOUND = 25081;
	/** 等级不足 **/
	public static int WELFARE_REWARD_LEVELWRONG = 25082;
	/** 奖励id不能为空 **/
	public static int WELFARE_REWARDID_NOTNULL = 25083;
	/** 登录签到天数不足 **/
	public static int WELFARE_REWARDID_NODAY = 25084;

	/** 用户没有加入vip */
	public static final int VIP_NOT_FOUND = 25085;
	/* * 没有发现行为id */
	public static final int VIP_NOT_PERMISSION = 25086;
	/* * 购买次数不足 */
	public static final int VIP_PERMISSION_NOT_ENOUGH = 25087;

	/** 商城类型为空 **/
	public static final int STORE_TYPE_NULL = 25088;
	/** 物品不存在 **/
	public static final int STORE_ITEM_NOTFOUND = 25089;
	/** 物品购买数量为空 **/
	public static final int STORE_ITEMNUM_NULL = 25090;
	/** 重复购买 **/
	public static final int STORE_BUY_AGAIN = 25091;
	/** 可购买数量不足 **/
	public static final int STORE_BUY_NUM_ERROR = 25092;
	/** vip等级不足 **/
	public static final int STORE_VIPLEVEL_ERROR = 25093;

	/** 充值id为空 **/
	public static int PAYID_ISNULL = 25094;
	/** 没有发现充值id **/
	public static int PAYID_NOTFOUND = 25095;
	/** 没有发现订单 **/
	public static int ORDER_NOTFOUND = 25096;
	/** 订单未处理 **/
	public static int ORDER_NOTDISPOSE = 25097;

	/** CDK为空 **/
	public static final int CDK_IS_NULL = 25098;
	/** CDK不存在 **/
	public static final int CDK_NOT_FOUND = 25099;
	/** CDK类型已失效 **/
	public static final int CDK_TYPE_ERROR = 25100;
	/** CDK已被使用过,或可用次数不足 **/
	public static final int CDK_IS_USED = 25101;
	/** CDK不在使用期限内 **/
	public static final int CDK_TIME_ERROR = 25102;
	/** CDK使用次数已满 **/
	public static final int CDK_USE_FULL = 25103;
	/** CDK奖励物品已失效 **/
	public static final int CDK_REWARD_ERROR = 25104;
	/** 已使用过cdk **/
	public static final int CDK_USED = 25105;
	/** 试练塔可购买挑战次数不足 */
	public static final int TowerNotBuyHeartNum = 25106;

	/** 放鞭炮类型不能为空 **/
	public static final int FirecrackerTypeNotNull = 25107;
	/** 红鞭炮次数不足 **/
	public static final int FIRECRACKER_REDNUM_SUFFICIENT = 25108;
	/** 金鞭炮次数不足 **/
	public static final int FIRECRACKER_GOLDNUM_SUFFICIENT = 25109;
	/** 红鞭炮次数不足10次 **/
	public static final int FIRECRACKER_REDNUM_SUFFICIENT_10 = 25110;
	/** 金鞭炮次数不足 10次 **/
	public static final int FIRECRACKER_GOLDNUM_SUFFICIENT_10 = 25111;
	/** 充值加码领取类型错误 **/
	public static final int PayMoreTypeNotNull = 25112;
	/** 充值加码已领取 **/
	public static final int PayMoreTypeHas = 25113;
	/** 充值总数不足无法领取 **/
	public static final int PayMoreTotalLess = 25114;	

	/**
	 * vip折扣店
	 */
	/** 重复购买 **/
	public static final int VIP_DIGO_HAD_BUY = 25200;
	/** vip等级不符合要求 **/
	public static final int VIP_DIGO_VIPLEVEL_LIMIT = 25201;

	/** 订单已处理 **/
	public static int ORDER_DISPOSED = 25115;

	/** appstore 未响应 **/
	public static int ORDER_APPSTORE_NORESULT = 25116;
	
	/**
	 * 今日免费次数已用完
	 */
	public static int EVENT_LUCKY_DRAW_OVERPLUS = 25117;
	
	/**
	 * 今日已经抽完
	 */
	public static int EVENT_LUCKY_DRAW_MAXDRAW = 25118;
	
	/**
	 * 今日已拜完，请明天再来
	 */
	public static int EVENT_FORTUNA_GIFTS_NUM = 25119;
	
	/**
	 * 正在拜财神，请稍后再来
	 */
	public static int EVENT_FORTUNA_GIFTS_TIME = 25120;
	
	/** 五一点赞类型不能为空 **/
	public static final int FiftyOneTypeNotNull = 25121;
	/** 铜板次数不足 **/
	public static final int FIFTY_ONE_REDNUM_SUFFICIENT = 25122;
	/** 元宝次数不足 **/
	public static final int FIFTY_ONE_GOLDNUM_SUFFICIENT = 25123;
	/** 铜板次数不足10次 **/
	public static final int FIFTY_ONE_REDNUM_SUFFICIENT_10 = 25124;
	/** 元宝次数不足 10次 **/
	public static final int FIFTY_ONE_GOLDNUM_SUFFICIENT_10 = 25125;
	
}
