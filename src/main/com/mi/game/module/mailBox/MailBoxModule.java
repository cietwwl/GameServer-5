package com.mi.game.module.mailBox;

import java.util.List;
import java.util.Map;

import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.BattleMsgType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.MailType;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SystemMsgType;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mailBox.dao.MailBoxEntityDAO;
import com.mi.game.module.mailBox.pojo.MailInfo;
import com.mi.game.module.mailBox.pojo.MailBoxEntity;

@Module(name = ModuleNames.MailBoxModule, clazz = MailBoxModule.class)
public class MailBoxModule extends BaseModule {
	private MailBoxEntityDAO mailBoxEntityDAO = MailBoxEntityDAO.getInstance();
	private final String arenaFailed = "竞技场被击败";
	private final String arenaWin = "竞技场防守成功";
	private final String arenaPlunder = "竞技场被掠夺";
	private final String friendApply = "申请好友";
	private final String friendMsg = "好友留言";
	private final String friendAccept = "同意好友请求";
	private final String friendRefuse = "拒绝好友请求";
	private final String friendBreak = "断绝好友关系";
	private final String arenaReward = "竞技场排名奖励";
	private final String arenaLuckyReward = "竞技场幸运排名奖励";
	private final String legionReward = "城池争夺战奖励";
	private final String vip = "VIP";
	private final String testPay = "充值返还福利通知";
	private final String welcomInfo = "入群送好礼";
	private final String systemCompensation = "系统补偿";
	private final int maxMsgNum = 20;
	private final String plunderShardSuccess = "夺宝防守失败";
	private final String plunderShardFailed = "夺宝防守成功";
	
	public void saveMailBoxEntity(MailBoxEntity mailBoxEntity) {
		mailBoxEntityDAO.save(mailBoxEntity);
	}

	public MailBoxEntity getMailBoxEntity(String playerID) {
		MailBoxEntity entity = mailBoxEntityDAO.getEntity(playerID);
		if (entity == null) {
			entity = this.initBoxEntity(playerID);
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		loginModule.changePlayerRewardNewsEntity(playerID, false);
		return entity;
	}

	public MailBoxEntity initBoxEntity(String playerID) {
		MailBoxEntity entity = new MailBoxEntity();
		entity.setKey(playerID);
		return entity;
	}

	public String getMsg(int mailType, int status, String name, Map<String, Object> paramMap) {
		String msg = null;
		switch (mailType) {
		case MailType.BATTLETYPE:
			msg = this.getBattleMsg(status, name, paramMap);
			break;
		case MailType.SYSTEMTYPE:
			msg = this.getSystemMas(status, paramMap);
			break;
		}
		return msg;
	}

	private String getBattleMsg(int status, String name, Map<String, Object> paramMap) {
		String msg = null;
		switch (status) {
		case BattleMsgType.BEATTACKWIN:
			msg = "玩家[00FFFF]" + name + "[-]在竞技场中挑战你,但被你轻松击败了";
			break;
		case BattleMsgType.BEATTACKFAILED:
			msg = "玩家[00FFFF]" + name + "[-]在竞技场中轻松击败了你,你的竞技场排名降至[00ff00]" + paramMap.get("rank") + "[-]";
			break;
		case BattleMsgType.BEATTACKFAILEDNOLOW:
			msg = "玩家[00FFFF]" + name + "[-]在竞技场中轻松击败了你,你的竞技场排名没有改变";
			break;
		case BattleMsgType.BEATTACKPLUNDER:
			msg = "玩家[00FFFF]" + name + "[-]在竞技场中轻松击败了你,你被掠夺了[00ff00]" + paramMap.get("silver") + "[-]铜板,排名降至[00ff00]" + paramMap.get("rank") + "[-]";
			break;
		case BattleMsgType.PLUNDERSHARDSUCCESS:
			msg = "战事不利,你的"+paramMap.get("color")+paramMap.get("shardName")+"碎片[-]被[00FFFF]"+name+"[-]抢走了";
			break;
		case BattleMsgType.PLUNDERSHARDFAILED:
			msg = "防守成功,[00FFFF]"+name+"[-]不自量力想要抢夺你的"+paramMap.get("color")+paramMap.get("shardName")+"碎片[-]被你轻松击败了";
			break;
		}
		return msg;
	}

	private String getSystemMas(int status, Map<String, Object> paramMap) {
		String msg = null;
		switch (status) {
		case SystemMsgType.ARENAREWARD:
			msg = "恭喜您在[00ff00]" + DateTimeUtil.getStringDate("yyyy年MM月dd日") + "[-]的竞技场中取得了第[00ff00]" + paramMap.get("rank") + "[-]名的成绩,获得奖励[00ff00]"
					+ paramMap.get("reputation") + "[-]声望[00ff00]" + paramMap.get("silver") + "[-]铜板";
			break;
		case SystemMsgType.ARENALUCKYREWARD:
			msg = "恭喜您在竞技场中获得了幸运排名[00ff00]" + paramMap.get("rank") + "[-]获得幸运奖励[00ff00]" + paramMap.get("gold") + "[-]元宝";
			break;
		case SystemMsgType.LEGIONREWARD:
			msg = "您所在的军团在上一轮城池争夺战中成功占领了城池[00ff00]" + paramMap.get("city") + "[-],您获得了" + paramMap.get("legionReward") + "奖励!";
			break;
		case SystemMsgType.VIP:
			msg = "恭喜您成为了VIP[00ff00]" + paramMap.get("vip") + "[-]玩家,逐鹿中原指日可待!";
			break;
		case SystemMsgType.TestPay:
			msg = "在本次测试过程中，小伙伴们的充值金额将会被记录，公测后三倍返还，多充多赚哟~";
			break;
		case SystemMsgType.WelcomeInfo:
			msg = "欢迎来到《天天西游2》的世界，我们开通了游戏客服专用QQ群426615277，加群后可以获得豪华大礼包，同时有漂亮的客服MM为您解答问题，赶紧加入吧!";
			break;
		}
		return msg;
	}

	private String getTitle(int mailType, int status) {
		String title = null;
		switch (mailType) {
		case MailType.BATTLETYPE:
			title = getBattleTitle(status);
			break;
		case MailType.FRIENDTYPE:
			title = getFriendTitle(status);
			break;
		case MailType.SYSTEMTYPE:
			title = getSystemTitle(status);
		}
		return title;
	}

	private String getBattleTitle(int status) {
		String title = null;
		switch (status) {
		case BattleMsgType.BEATTACKWIN:
			title = arenaWin;
			break;
		case BattleMsgType.BEATTACKFAILED:
			title = arenaFailed;
			break;
		case BattleMsgType.BEATTACKFAILEDNOLOW:
			title = arenaFailed;
			break;
		case BattleMsgType.BEATTACKPLUNDER:
			title = arenaPlunder;
			break;
		case BattleMsgType.PLUNDERSHARDSUCCESS:
			title = plunderShardSuccess;
			break;
		case BattleMsgType.PLUNDERSHARDFAILED:
			title = plunderShardFailed;
			break;
		}
		return title;
	}

	private String getFriendTitle(int status) {
		String title = null;
		switch (status) {
		case FriendModule.BEACCEPTFRIENDSTATUS:
			title = friendAccept;
			break;
		case FriendModule.BEREFUSEFRIENDSTATUS:
			title = friendRefuse;
			break;
		case FriendModule.BEBREAKFRIENDSTATUS:
			title = friendBreak;
			break;
		case FriendModule.LEAVEMESSAGE:
			title = friendMsg;
			break;
		default:
			title = friendApply;
			break;
		}
		return title;
	}

	private String getSystemTitle(int status) {
		String title = null;
		switch (status) {
		case SystemMsgType.ARENAREWARD:
			title = arenaReward;
			break;
		case SystemMsgType.ARENALUCKYREWARD:
			title = arenaLuckyReward;
			break;
		case SystemMsgType.LEGIONREWARD:
			title = legionReward;
			break;
		case SystemMsgType.VIP:
			title = vip;
			break;
		case SystemMsgType.TestPay:
			title = testPay;
			break;
		case SystemMsgType.WelcomeInfo:
			title = welcomInfo;
			break;
		case SystemMsgType.SystemCompensation:
			title = systemCompensation;
			break;
		}
		return title;
	}

	public void addMail(String playerID, String name, String sendPlayerID, String msg, int mailType, int status, Map<String, Object> msgParam) {
		if (name == null && mailType != MailType.SYSTEMTYPE) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(sendPlayerID);
			name = playerEntity.getNickName();
		}
		if (msg == null) {
			msg = this.getMsg(mailType, status, name, msgParam);
		}
		String title = this.getTitle(mailType, status);
		MailInfo mailInfo = new MailInfo(sendPlayerID, name, msg, mailType, status);
		if (msgParam != null && msgParam.get("reportID") != null) {
			mailInfo.setReportID(Long.parseLong(msgParam.get("reportID").toString()));
		}
		MailBoxEntity entity = this.getMailBoxEntity(playerID);
		List<MailInfo> mailList = entity.getMailList();
		if (mailType == MailType.FRIENDTYPE && status == FriendModule.APPLYFRIENDSTATUS) {
			for (MailInfo mInfo : mailList) {
				if (mInfo.getMailType() == MailType.FRIENDTYPE) {
					if (mInfo.getSendPlayerID().equals(sendPlayerID) && mInfo.getStatus() == FriendModule.APPLYFRIENDSTATUS) {
						throw new IllegalArgumentException(ErrorIds.FriendApplySend + "");
					}
				}
			}
		}
		mailInfo.setTitle(title);
		mailInfo.setCreateTime(System.currentTimeMillis());
		mailInfo.setIndex(entity.addCounter());
		mailList.add(0, mailInfo);
		int size = mailList.size();
		if (size > maxMsgNum) {
			mailList.remove(maxMsgNum);
		}
		this.saveMailBoxEntity(entity);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		loginModule.changePlayerRewardNewsEntity(playerID, true);
	}
	
	public void addMail(String playerID, String msg, String title) {
		MailInfo mailInfo = new MailInfo(null, null, msg, SystemMsgType.SystemCompensation, 0);
		MailBoxEntity entity = this.getMailBoxEntity(playerID);
		List<MailInfo> mailList = entity.getMailList();
		mailInfo.setTitle(title);
		mailInfo.setCreateTime(System.currentTimeMillis());
		mailInfo.setIndex(entity.addCounter());
		mailList.add(0, mailInfo);
		int size = mailList.size();
		if (size > maxMsgNum) {
			mailList.remove(maxMsgNum);
		}
		this.saveMailBoxEntity(entity);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		loginModule.changePlayerRewardNewsEntity(playerID, true);
	}

	/**
	 * 活动奖励发放邮件
	 * 
	 * @param playerID
	 * @param title
	 * @param msg
	 */
	public void sendEventRewardMail(String playerID, String title, String msg) {
		MailInfo mailInfo = new MailInfo(null, null, msg, MailType.SYSTEMTYPE, 0);
		MailBoxEntity entity = this.getMailBoxEntity(playerID);
		List<MailInfo> mailList = entity.getMailList();
		mailInfo.setTitle(title);
		mailInfo.setCreateTime(System.currentTimeMillis());
		mailInfo.setIndex(entity.addCounter());
		mailList.add(0, mailInfo);
		int size = mailList.size();
		if (size > maxMsgNum) {
			mailList.remove(maxMsgNum);
		}
		this.saveMailBoxEntity(entity);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		loginModule.changePlayerRewardNewsEntity(playerID, true);
	}
	
	public void addTestSystemMail(String playerID){
		
	}
}
