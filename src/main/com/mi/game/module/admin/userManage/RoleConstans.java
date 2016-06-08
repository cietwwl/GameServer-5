package com.mi.game.module.admin.userManage;

public class RoleConstans {

	private static String[] category = {
			"系统管理","cdk管理","玩家管理","财务管理","权限管理","游戏服信息","统计分析"
	};
	private static String[] systemConfig = {
			"活动管理#system/EventEntity.html",
			"公告管理#system/NoticeEntity.html",
			"添加物品#system/addItem.html",
			"发送邮件#system/addMail.html",
			"客服管理#system/SuggestManager.html",
			"时间翻译#system/TimeConvert.html",
			"奖励列表#reward/RewardEntity.html",
			"奖励发放#reward/SendReward.html",
			"平台奖励#reward/SendPlatformReward.html",
			"数据导出#export/export.html",
			"修改xml#system/TemplateManage.html",
			"远程更新#system/RemoteTemplateManage.html"
	};
	private static String[] cdkManage = {
			"cdk奖励#cdk/CDKRewardEntity.html",
			"cdk类型#cdk/CDKTypeEntity.html",
			"cdk管理#cdk/CDKEntity.html",
			"cdk生成#cdk/makeCDK.html"
	};
	private static String[] playerManage = {
			"英雄皮肤#player/HeroSkinEntity.html",
			"试练管理#player/TowerEntity.html",
			"副本星数#player/LeadDesitnynEntity.html",
			"新手引导#player/NewPlayerEntity.html",
			"玩家管理#player/PlayerEntity.html",
			"体力耐力#player/VitatlyEntity.html",
			"背包管理#player/BagEntity.html",
			"钱包管理#player/WalletEntity.html",
			"装备管理#player/EquipmentMapEntity.html",
			"英雄管理#player/HeroEntity.html",
			"宝物管理#player/TalismanMapEntity.html",
			"宝物碎片#player/TalismanShard.html",
			"大关管理#player/DungeonActMapEntity.html",
			"小关管理#player/DungeonMapEntity.html",
			"军团管理#legion/LegionEntity.html",
			"成员管理#legion/LegionMemberEntity.html",
			"等级礼包#welfare/WelfareLevelEntity.html",
			"登录礼包#welfare/WelfareLoginEntity.html",
			"在线礼包#welfare/WelfareOnlineEntity.html",
			"签到礼包#welfare/WelfareSignEntity.html"
	};
	
	private static String[] caiwuManage = {
		"订单管理#pay/PayOrderEntity.html"
	};
	
	private static String[] roleManage = {
			"大类管理#role/category.html",
			"小类管理#role/role.html",
			"账号管理#role/admin.html"
	};
	
	private static String[] analyManage = {
		"统计管理#http://xxx/base/index.html#/show/xxx",
		"米乐统计#http://xxx/analy/index.html#/show/xxx",
		"米乐统计.#http://xxx/analy_/index.html#/show/xxx",
		"登录统计#http://xxx/login/index.html#/show/loginServer"
	};
	
	private static String[] serverInfoManage = {				
		"游戏服信息#server/serverInfo.html"
	};

	public static String[] categorys() {
		return category;
	}

	public static String[] systemConfigs() {
		return systemConfig;
	}

	public static String[] cdkManages() {
		return cdkManage;
	}

	public static String[] playerManages() {
		return playerManage;
	}

	public static String[] caiwuManages() {
		return caiwuManage;
	}

	public static String[] roleManages() {
		return roleManage;
	}
	public static String[] analyManages() {
		return analyManage;
	}
	
	public static String[] serverInfoManages() {
		return serverInfoManage;
	}
}
