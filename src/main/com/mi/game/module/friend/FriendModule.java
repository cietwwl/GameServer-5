package com.mi.game.module.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.MailType;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.friend.dao.FriendEntityDAO;
import com.mi.game.module.friend.pojo.FriendEntity;
import com.mi.game.module.friend.pojo.FriendState;
import com.mi.game.module.friend.pojo.PresentInfo;
import com.mi.game.module.friend.protocol.FriendInfo;
import com.mi.game.module.friend.protocol.FriendProtocol;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.login.pojo.PlayerStatusEntity;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.mailBox.pojo.MailBoxEntity;
import com.mi.game.module.mailBox.pojo.MailInfo;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.util.CommonMethod;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.FriendMoudle, clazz = FriendModule.class)
public class FriendModule extends BaseModule {
	FriendEntityDAO friendEntityDAO = FriendEntityDAO.getInstance();
	public static final int APPLYFRIENDSTATUS = 1;
	public static final int ACCEPTFRIENDSTATUS = 2;
	public static final int REFUSEFRIENDSTATUS = 3;
	public static final int BEACCEPTFRIENDSTATUS = 4;
	public static final int BEREFUSEFRIENDSTATUS = 5;
	public static final int BEBREAKFRIENDSTATUS = 6;
	public static final int FIGHTWIN = 7;
	public static final int FIGHTFAILED = 8;
	public static final int LEAVEMESSAGE = 9;
	public static int ACCEPTFRIEND = 0;
	public static int REFUSEFRIEND = 1;
	private final int maxPresentNum = 30;
	private static int maxFriendNum = SysConstants.MaxFriendNum;

	/**
	 * 获取好友实体
	 * */
	public FriendEntity getFriendEntity(String playerID) {
		FriendEntity entity = friendEntityDAO.getEntity(playerID);
		if (entity == null) {
			entity = this.initEntity(playerID);
		}
		return entity;
	}

	
	/**
	 * 获取好友实体
	 * */
	public FriendEntity getFriendEntity(String playerID,String serverID) {
		FriendEntity entity = friendEntityDAO.getEntity(playerID,serverID);
		if (entity == null) {
			entity = this.initEntity(playerID);
		}
		return entity;
	}
	
	/**
	 * 获取更新后的好友实体
	 * */
	public FriendEntity getUpdateFriendEntiy(String playerID) {
		FriendEntity friendEntity = this.getFriendEntity(playerID);
		long nowTime = System.currentTimeMillis();
		if (!DateTimeUtil.isSameDay(nowTime, friendEntity.getUpdateTime())) {
			Map<String, FriendState> friendList = friendEntity.getFriendList();
			for (Entry<String, FriendState> entry : friendList.entrySet()) {
				FriendState state = entry.getValue();
				state.setAttackNum(0);
			}
			friendEntity.setPresentNum(0);
			friendEntity.setSendPresent(new ArrayList<String>());
			friendEntity.setUpdateTime(nowTime);
			this.saveFriendEntity(friendEntity);
		}
		if (friendEntity.getPresentList().isEmpty()) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			loginModule.changePlayerRewardFriendEntity(playerID, false);
		}
		return friendEntity;
	}

	/**
	 * 保存好友实体
	 * */
	public void saveFriendEntity(FriendEntity entity) {
		this.friendEntityDAO.save(entity);
	}

	public FriendEntity initEntity(String playerID) {
		FriendEntity entity = new FriendEntity();
		entity.setKey(playerID);
		return entity;
	}

	/**
	 * 获取好友列表
	 * */
	public void getFriendList(String playerID, FriendProtocol protocol) {
		FriendEntity entity = getUpdateFriendEntiy(playerID);
		Map<String, FriendState> list = entity.getFriendList();
		List<Object> searchList = new ArrayList<>();
		List<FriendInfo> friendList = new ArrayList<>();
		if (!list.isEmpty()) {
			for (String str : list.keySet()) {
				searchList.add(str);
			}
		} else {
			protocol.setFriendList(friendList);
			return;
		}
		long nowTime = System.currentTimeMillis();

		List<PlayerEntity> playerList = PlayerEntitiyDAO.getInstance().getEntityInList(searchList);
		List<String> presentList = entity.getSendPresent();
		for (PlayerEntity playerEntity : playerList) {
			String friendID = playerEntity.getKey().toString();
			FriendState state = list.get(friendID);
			FriendInfo friendInfo = new FriendInfo();
			if (playerEntity.getOffLineTime() > nowTime) {
				friendInfo.setOnline(1);
			}
			if (presentList.contains(friendID)) {
				friendInfo.setPresent(1);
			}
			friendInfo.setBeAttackNum(playerEntity.getBeFriendAttackNum());
			friendInfo.setFriendID(friendID);
			friendInfo.setAttackNum(state.getAttackNum());
			friendInfo.setLevel(playerEntity.getLevel());
			friendInfo.setFightValue(playerEntity.getFightValue());
			friendInfo.setName(playerEntity.getNickName());
			friendList.add(friendInfo);
		}
		protocol.setPresentNum(30 - entity.getPresentNum());
		protocol.setPresentList(entity.getPresentList());
		protocol.setFriendList(friendList);
	}

	/**
	 * 获取推荐列表
	 * */
	public void getRecommendFriendList(String playerID, FriendProtocol protocol) {
		FriendEntity entity = getFriendEntity(playerID);
		List<FriendInfo> friendList = new ArrayList<>();
		Map<String, FriendState> list = entity.getFriendList();
		List<Object> searchList = new ArrayList<>();
		if (!list.isEmpty()) {
			for (String str : list.keySet()) {
				searchList.add(str);
			}
		}
		long nowTime = System.currentTimeMillis();
		List<PlayerEntity> playerList = PlayerEntitiyDAO.getInstance().getRecommendList(searchList);
		for (PlayerEntity playerEntity : playerList) {
			FriendInfo friendInfo = new FriendInfo();
			friendInfo.setFriendID(playerEntity.getKey().toString());
			if (playerEntity.getOffLineTime() > nowTime) {
				friendInfo.setOnline(1);
			}
			friendInfo.setLevel(playerEntity.getLevel());
			friendInfo.setFightValue(playerEntity.getFightValue());
			friendInfo.setName(playerEntity.getNickName());
			friendList.add(friendInfo);
		}
		protocol.setFriendList(friendList);
	}

	/**
	 * 申请好友
	 * */
	public void applyFriend(String playerID, String friendID, String message, FriendProtocol protocol) {
		if (playerID.equals(friendID)) {
			throw new IllegalArgumentException(ErrorIds.NotAddFriendMyself + "");
		}
		if (!Utilities.checkLenght(message,0,40) ) {
			logger.error(" 留言长度不合法");
			throw new IllegalArgumentException(ErrorIds.leaveMessageLength + "");
		}
		FriendEntity entity = this.getFriendEntity(playerID);
		Map<String, FriendState> friendList = entity.getFriendList();
		if (friendList.containsKey(friendID)) {
			throw new IllegalArgumentException(ErrorIds.AlreadyFriend + "");
		}
		FriendEntity friendEntity = this.getFriendEntity(friendID);
		if (friendEntity.getFriendList().size() >= maxFriendNum) {
			throw new IllegalArgumentException(ErrorIds.TargerFriendNumFull + "");
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		String name = playerEntity.getNickName();
		MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		mailBoxModule.addMail(friendID, name, playerID, CommonMethod.chatShieldedKeyword(message), MailType.FRIENDTYPE, APPLYFRIENDSTATUS, null);
	}

	/**
	 * 处理好友请求
	 * */
	public void friendMsgHandler(String playerID, String friendID, int handlerType, int index, FriendProtocol protocol) {
		if (playerID.equals(friendID)) {
			throw new IllegalArgumentException(ErrorIds.NotAddFriendMyself + "");
		}
		MailBoxModule boxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		MailBoxEntity mailBoxEntity = boxModule.getMailBoxEntity(playerID);
		MailInfo mailInfo = mailBoxEntity.getMainInfo(index);
		FriendEntity friendEntity = this.getFriendEntity(playerID);
		Map<String, FriendState> friends = friendEntity.getFriendList();
		if (friends.containsKey(friendID)) {
			mailInfo.setStatus(ACCEPTFRIENDSTATUS);
			protocol.setMailInfo(mailInfo);
			return;
		}
		if (mailInfo == null || mailInfo.getMailType() != MailType.FRIENDTYPE || mailInfo.getStatus() != APPLYFRIENDSTATUS) {
			throw new IllegalArgumentException(ErrorIds.FriendIndexWrong + "");
		}
		friendID = mailInfo.getSendPlayerID();
		int friendStatus = 0;
		if (handlerType == ACCEPTFRIEND) {
			friendStatus = BEACCEPTFRIENDSTATUS;
			mailInfo.setStatus(ACCEPTFRIENDSTATUS);
			MailBoxEntity friendMailBoxEntity = boxModule.getMailBoxEntity(friendID);
			List<MailInfo> mailList = friendMailBoxEntity.getMailList();
			boolean isSave = false;
			for(MailInfo mail : mailList){
				if(mail.getMailType() == MailType.FRIENDTYPE && mail.getStatus() == APPLYFRIENDSTATUS && mail.getSendPlayerID().equals(playerID)){
					mail.setStatus(ACCEPTFRIENDSTATUS);
					isSave = true;
					break;
				}
			}
			if(isSave){
				boxModule.saveMailBoxEntity(friendMailBoxEntity);
			}
			this.addFriend(playerID, friendID);
			this.addFriend(friendID, playerID);
		} else {
			friendStatus = BEREFUSEFRIENDSTATUS;
			mailInfo.setStatus(REFUSEFRIENDSTATUS);
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		boxModule.addMail(friendID, playerEntity.getNickName(), playerID, null, MailType.FRIENDTYPE, friendStatus, null);
		boxModule.saveMailBoxEntity(mailBoxEntity);
		loginModule.changePlayerRewardNewsEntity(playerID, true);
		protocol.setMailInfo(mailInfo);
	}

	/**
	 * 添加好友
	 * */
	private void addFriend(String playerID, String friendID) {
		FriendEntity friendEntity = this.getFriendEntity(playerID);
		Map<String, FriendState> friends = friendEntity.getFriendList();
		if (friends.size() >= maxFriendNum) {
			throw new IllegalArgumentException(ErrorIds.MaxFriendNum + "");
		}
		FriendState friendState = new FriendState();
		friends.put(friendID, friendState);
		this.saveFriendEntity(friendEntity);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerStatusEntity playerStatusEntity = loginModule.changePlayerRewardNewsEntity(friendID, true);
		loginModule.savePlayerStatusEntity(playerStatusEntity);
		AchievementModule achievementModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		achievementModule.refreshAchievement(playerID, ActionType.FRIENDNUM, friends.size());
	}

	/***
	 * 增送耐力
	 * */
	public void giveEnergy(String playerID, String friendID, FriendProtocol protocol) {
		FriendEntity friendEntity = this.getUpdateFriendEntiy(playerID);
		Map<String, FriendState> friends = friendEntity.getFriendList();
		FriendState friendState = friends.get(friendID);
		List<String> presentList = friendEntity.getSendPresent();
		if (friendState == null) {
			throw new IllegalArgumentException(ErrorIds.NoFriend + "");
		}
		if (presentList.contains(friendID)) {
			throw new IllegalArgumentException(ErrorIds.AlreadyGivePresent + "");
		}
		presentList.add(friendID);
		friendEntity.setSendPresent(presentList);
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		dayTaskModule.addScore(playerID, ActionType.GIVEENERGY, 1);
		this.addPresent(friendID, playerID);
		this.saveFriendEntity(friendEntity);
		protocol.setPresent(1);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.SENDENERGY, 0, null);
	}

	private void addPresent(String playerID, String friendID) {
		FriendEntity presentEntity = this.getUpdateFriendEntiy(playerID);
		if (presentEntity.getPresentNum() < maxPresentNum) {
			long index = presentEntity.addIndex();
			List<PresentInfo> presentList = presentEntity.getPresentList();
			PresentInfo presentInfo = new PresentInfo();
			presentInfo.setCreateTime(System.currentTimeMillis());
			presentInfo.setIndex(index);
			presentInfo.setFriendID(friendID);
			presentList.add(presentInfo);
			this.saveFriendEntity(presentEntity);
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			loginModule.changePlayerRewardFriendEntity(playerID, true);
		}
	}

	/**
	 * 领取耐力
	 * */
	public void getEnergy(String playerID, long index, FriendProtocol protocol) {
		FriendEntity myFriendEntity = this.getUpdateFriendEntiy(playerID);
		int num = myFriendEntity.getPresentNum();
		if (num >= maxPresentNum) {
			throw new IllegalArgumentException(ErrorIds.FriendPresentMax + "");
		}
		List<PresentInfo> presentList = myFriendEntity.getPresentList();
		String friendID = null;
		boolean isFind = false;
		for (int i = 0; i < presentList.size(); i++) {
			PresentInfo presentInfo = presentList.get(i);
			if (index == presentInfo.getIndex()) {
				friendID = presentInfo.getFriendID();
				isFind = true;
				presentList.remove(i);
				break;
			}
		}
		if (!isFind) {
			throw new IllegalArgumentException(ErrorIds.EnergyIndexWrong + "");
		}
		if (presentList.isEmpty()) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			loginModule.changePlayerRewardFriendEntity(playerID, false);
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Map<String,GoodsBean> showMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, KindIDs.ENERGY, 1, null, true, showMap, itemMap, null);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(showMap);
		List<String> sendPresentList = myFriendEntity.getSendPresent();
		if (!sendPresentList.contains(friendID)) {
			this.addPresent(friendID, playerID);
			sendPresentList.add(friendID);
			myFriendEntity.setSendPresent(sendPresentList);
			DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
			dayTaskModule.addScore(playerID, ActionType.GIVEENERGY, 1);
		}
		myFriendEntity.setPresentNum(myFriendEntity.getPresentNum() + 1);
		protocol.setPresentNum(30 - myFriendEntity.getPresentNum());
		this.saveFriendEntity(myFriendEntity);
	}

	/**
	 * 一键领取耐力
	 * */
	public void getAllEnergy(String playerID, FriendProtocol protocol) {
		FriendEntity myFriendEntity = this.getUpdateFriendEntiy(playerID);
		List<PresentInfo> presList = myFriendEntity.getPresentList();
		int num = myFriendEntity.getPresentNum();
		int size = presList.size();
		int energy = 0;
		if(size == 0){
			return ;
		}
		List<String> sendPresentList = myFriendEntity.getSendPresent();
		int sendNum = 0;
		if (size + num > maxPresentNum) {
			int i = 0;
			for (Iterator<PresentInfo> iter = presList.iterator(); iter.hasNext();) {
				PresentInfo presentInfo = iter.next();
				if (i >= maxPresentNum - num) {
					break;
				}
				String friendID = presentInfo.getFriendID();
				if (!sendPresentList.contains(friendID)) {
					sendPresentList.add(friendID);
					addPresent(presentInfo.getFriendID(), playerID);
					sendNum ++;
				}
				iter.remove();
				i++;
			}
			energy = i;
		} else {
			for (PresentInfo presentInfo : presList) {
				String friendID = presentInfo.getFriendID();
				if (!sendPresentList.contains(friendID)) {
					sendPresentList.add(friendID);
					addPresent(presentInfo.getFriendID(), playerID);
					sendNum ++;
				}
			}
			presList.clear();
			energy = size;
		}
		if(sendNum != 0){
			DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
			dayTaskModule.addScore(playerID, ActionType.GIVEENERGY, sendNum);
		}
		myFriendEntity.setSendPresent(sendPresentList);
		myFriendEntity.setPresentNum(num + energy);
		protocol.setPresentList(presList);
		Map<String, Object> itemMap = new HashMap<>();
		Map<String,GoodsBean> showMap = new HashMap<>(); 
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, KindIDs.ENERGY, energy, null, true, showMap, itemMap, null);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		loginModule.changePlayerRewardFriendEntity(playerID, false);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(showMap);
		protocol.setPresentNum(30 - myFriendEntity.getPresentNum());
		this.saveFriendEntity(myFriendEntity);
	}

	/**
	 * 好友切磋
	 * */
	public void friendFight(String playerID, String friendID, boolean win, IOMessage ioMessage,FriendProtocol protocol) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getUpdatePlayerEntity(playerID, ioMessage);
		PlayerEntity friendPlayerEntity = loginModule.getUpdatePlayerEntity(friendID, ioMessage);
		int attackNum = playerEntity.getAttackFriendNum();
		int beAttackNum = friendPlayerEntity.getBeFriendAttackNum();
		FriendEntity friendEntity = this.getUpdateFriendEntiy(playerID);
		Map<String, FriendState> friendMap = friendEntity.getFriendList();
		if (!friendMap.containsKey(friendID)) {
			throw new IllegalArgumentException(ErrorIds.NoFriend + "");
		}

		FriendState friendState = friendMap.get(friendID);
		int friendAttackNum = friendState.getAttackNum();
		if (attackNum < 1 || beAttackNum < 1 || friendAttackNum >= 5) {
			throw new IllegalArgumentException(ErrorIds.NoFriendFight + "");
		}
		playerEntity.setAttackFriendNum(attackNum - 1);
		friendPlayerEntity.setBeFriendAttackNum(beAttackNum - 1);
		friendState.setAttackNum(friendAttackNum + 1);
		List<BaseEntity> entityList = new ArrayList<>();
		entityList.add(playerEntity);
		entityList.add(friendPlayerEntity);
		entityList.add(friendEntity);
		friendEntityDAO.save(entityList);
		protocol.setMyAllAtkNum(playerEntity.getAttackFriendNum());
		protocol.setFriendAllAtkNum(friendPlayerEntity.getBeFriendAttackNum());
		protocol.setFriendAtkNum(friendState.getAttackNum());
		protocol.setFriendID(friendID);
		// MailBoxModule mailBoxModule =
		// ModuleManager.getModule(ModuleNames.MailBoxModule,MailBoxModule.class);
		// int status = 0;
		// if(win){
		// status = FIGHTFAILED;
		// }else{
		// status = FIGHTWIN;
		// }
		// mailBoxModule.addMail(friendID, playerEntity.getNickName(), playerID,
		// null, MailType.FRIENDTYPE, status,null);

	}

	/**
	 * 留言
	 * */
	public void leaveMessage(String playerID, String friendID, String message) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (!Utilities.checkLenght(message,2,80) ) {
			logger.error(" 留言长度不合法");
			throw new IllegalArgumentException(ErrorIds.leaveMessageLength + "");
		}
		MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		mailBoxModule.addMail(friendID, playerEntity.getNickName(), playerID, CommonMethod.chatShieldedKeyword(message), MailType.FRIENDTYPE, LEAVEMESSAGE, null);
	}

	/**
	 * 断绝好友关系
	 * */
	public void breakFriendship(String playerID, String friendID, FriendProtocol protocol) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		String nickName = playerEntity.getNickName();
		MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		mailBoxModule.addMail(friendID, nickName, playerID, null, MailType.FRIENDTYPE, BEBREAKFRIENDSTATUS, null);
		FriendEntity myFriendEntity = this.getFriendEntity(playerID);
		Map<String, FriendState> myFriendMap = myFriendEntity.getFriendList();
		if (myFriendMap.containsKey(friendID)) {
			myFriendMap.remove(friendID);
		} else {
			throw new IllegalArgumentException(ErrorIds.NoFriend + "");
		}
		List<PresentInfo> myPresInfos = myFriendEntity.getPresentList();
		int mySize = myPresInfos.size();
		List<Long> myPresentRemoveList = new ArrayList<>();
		for (int i = 0; i < mySize; i++) {
			PresentInfo presentInfo = myPresInfos.get(i);
			if (presentInfo.getFriendID().equals(friendID)) {
				myPresInfos.remove(i);
				myPresentRemoveList.add(presentInfo.getIndex());
			}
		}
		FriendEntity friendEntity = this.getFriendEntity(friendID);
		Map<String, FriendState> friendMap = friendEntity.getFriendList();
		if (friendMap.containsKey(playerID)) {
			friendMap.remove(playerID);
		}
		List<PresentInfo> friendPresInfos = friendEntity.getPresentList();
		// int friendSize = friendPresInfos.size();
		// List<Integer> removeList = new ArrayList<>();
		// for(int i = 0; i < friendSize ; i++){
		// PresentInfo presentInfo = friendPresInfos.get(i);
		// if(presentInfo.getFriendID().equals(playerID)){
		// removeList.add(i);
		// }
		// }
		for (Iterator<PresentInfo> iter = friendPresInfos.iterator(); iter.hasNext();) {
			PresentInfo presentInfo = iter.next();
			if (presentInfo.getFriendID().equals(playerID)) {
				iter.remove();
			}
		}
		protocol.setMyPresentRemoveList(myPresentRemoveList);
		this.saveFriendEntity(myFriendEntity);
		this.saveFriendEntity(friendEntity);
	}

	/**
	 * 搜索好友
	 * */
	public void findPlayerByName(String playerName, FriendProtocol protocol) {
		//if(!Utilities.IsUserName(playerName)){
		//	throw new IllegalArgumentException(ErrorIds.UserNameIllegal + "");
		//}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		List<PlayerEntity> playerList = loginModule.getPlayerEntityLikeName(playerName);
		List<FriendInfo> friendList = new ArrayList<>();
		if (playerList == null || playerList.isEmpty()) {
			throw new IllegalArgumentException(ErrorIds.NotFoundPlayer + "");
		}
		for (PlayerEntity playerEntity : playerList) {
			String friendID = playerEntity.getKey().toString();
			FriendInfo friendInfo = new FriendInfo();
			friendInfo.setFriendID(friendID);
			friendInfo.setLevel(playerEntity.getLevel());
			friendInfo.setFightValue(playerEntity.getFightValue());
			friendInfo.setName(playerEntity.getNickName());
			friendList.add(friendInfo);
		}
		protocol.setFriendList(friendList);
	}

	/**
	 * 是否好友关系
	 * 
	 * @param playerID
	 * @param friendID
	 * @return
	 */
	public boolean isFriend(String playerID, String friendID) {
		if (playerID.equals(friendID)) {
			return false;
		}
		FriendEntity entity = this.getFriendEntity(playerID);
		Map<String, FriendState> friendList = entity.getFriendList();
		if (friendList.containsKey(friendID)) {
			return true;
		}
		return false;
	}
}
