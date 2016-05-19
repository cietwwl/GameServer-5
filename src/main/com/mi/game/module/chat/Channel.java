package com.mi.game.module.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.core.engine.ModuleManager;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.chat.classes.Message;
import com.mi.game.module.chat.classes.MessageBlock;
import com.mi.game.module.chat.define.EnumChannelType;
import com.mi.game.module.chat.define.EnumMessageAddressType;
import com.mi.game.module.chat.define.EnumMessageType;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.NewPlayerEntity;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.login.pojo.PlayerStatusEntity;
import com.mi.game.module.pet.PetModule;
import com.mi.game.module.pet.pojo.Pet;

/**
 * 频道对象
 * 
 * @author Administrator
 */
public class Channel {
	public final static Logger logger = LoggerFactory.getLogger(Channel.class);

	public int maxBlockCount = 5000; // 最大承载块数

	private String channelName; // 频道名称
	private EnumChannelType channelType;
	private EnumMessageAddressType type; // 频道类型
	private int id; // 频道编号
	private long timestamp; // 最后添加时间戳
	protected int createDateTime; // 创建channel时间
	private List<MessageBlock> msgBlockList; // 消息块列表

	public Channel(EnumChannelType channelType, String channelName, int channelID) {
		this.channelName = channelName;
		this.channelType = channelType;
		this.id = channelID;
		this.createDateTime = DateTimeUtil.getTime();
		this.msgBlockList = new CopyOnWriteArrayList<MessageBlock>(); // 线程安全的列表
	}

	/**
	 * 添加message
	 * 
	 * @param msg
	 */
	public void addMessage(Message msg) {
		onAddMessage(msg);
	}

	public List<Message> getMessages(long fromTimestamp, long toTimeStamp, long[] outNewTimeStamp) {
		return this.getMessages(null, fromTimestamp, toTimeStamp, outNewTimeStamp);
	}

	/**
	 * 返回指定时间点的消息列表
	 * 
	 * @param fromTimestamp
	 * @param toTimeStamp
	 * @return
	 */
	public List<Message> getMessages(String playerID, long fromTimestamp, long toTimeStamp, long[] outNewTimeStamp) {
		// 返回值
		List<Message> lstResult = null;

		if (fromTimestamp == 0 || toTimeStamp > System.currentTimeMillis()) { // 如果是第一次请求，或者结束时间大于当前时间
			outNewTimeStamp[0] = System.currentTimeMillis();
		} else {
			if (toTimeStamp > 0) { // 有结束时间
				if (toTimeStamp > System.currentTimeMillis()) {
					outNewTimeStamp[0] = System.currentTimeMillis();
				} else {
					outNewTimeStamp[0] = toTimeStamp;
				}
			} else {
				outNewTimeStamp[0] = System.currentTimeMillis();
			}
		}
		if (msgBlockList == null || fromTimestamp == 0) { // 如果没有消息，返回 null
			return lstResult;
		}
		String groupId = "";
		if (channelType.equals(EnumChannelType.Hide)) { // 军团频道
			// logger.debug("获取心跳包消息块，playerID："+playerID+",fromTimestamp:"+fromTimestamp+",toTimeStamp:"+toTimeStamp);
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			PlayerEntity sendEntity = loginModule.getPlayerEntity(playerID);
			groupId = sendEntity.getGroupID();
			if (groupId.equals("0")) {
				return lstResult;
			}
		}
		lstResult = new ArrayList<Message>();
		if ((channelType.equals(EnumChannelType.Heartbeat))) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			PlayerStatusEntity playerStatusEntity = loginModule.getPlayerStatusEntity(playerID);
			Map<String, Object> body = new HashMap<>();

			// //////////////////////
			// 开服活动,经验翻倍
			EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
			NewPlayerEntity newPlayerEntity = loginModule.getNewPlayerEntity(playerID);
			if (newPlayerEntity != null) {
				if (newPlayerEntity.getProperId() != EventConstans.GUIDE_PROPERID) {
					int expStatus = eventModule.getNewServerEventReward(EventConstans.EVENT_TYPE_DOUBLEEXP);
					playerStatusEntity.setExpStatus(expStatus);
				}
			}
			/* 宠物每十秒增加一次经验*/
			PetModule petModule = ModuleManager.getModule(ModuleNames.PetModule, PetModule.class); 
			Pet pet=petModule.petTimeExp(playerID);
			if(pet!=null){
				Map<String,Pet> petMap=new HashMap<String,Pet>();
				petMap.put("pet", pet);
				body.put("petInfo", petMap);
			}			
			// ////////////////////
			// 节日活动,活动开启返回活动key
			FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
			playerStatusEntity.setFestivalID(festivalModule.nowFestivalActive());
			
			body.put("playerStatusEntity", playerStatusEntity.responseMap());
			Message msg = new Message(EnumMessageAddressType.Heartbeat, "0", "system", EnumMessageAddressType.Heartbeat, "0", body, EnumMessageType.Infomation, "");
			lstResult.add(msg);
		}

		for (int i = 0; i < msgBlockList.size(); i++) {
			MessageBlock msgBlock = msgBlockList.get(i);

			if (msgBlock.getTimestamp() >= fromTimestamp && msgBlock.getTimestamp() < toTimeStamp) { // 如果消息快在这个时间段
				String receiverID = msgBlock.getContent().getReceiverID(); // 消息接收者
				Message message = msgBlock.getContent();

				if (channelType.equals(EnumChannelType.Hide)) { // 军团频道
					if (groupId == message.getSenderGroupID()) { // 如果是本军团的消息
						lstResult.add(message);
					}
				} else if (channelType.equals(EnumChannelType.Person)) { // 私聊频道
					if (playerID.equals(message.getSenderID()) || playerID.equals(message.getReceiverID())) { // 如果发送者是自己或者接收者是自己
						lstResult.add(message);
					}
				} else {
					if (receiverID == null || receiverID.equals("") || "0".equals(receiverID)) { // 如果没有接收者
						lstResult.add(message);
					} else {
						if (playerID == null || playerID.equals("") || "0".equals(playerID)) { // 如果没有指定获取者
							lstResult.add(message);
						} else {
							if (playerID.equals(receiverID)) { // 如果消息接受者等于获取者
								lstResult.add(message);
								msgBlockList.remove(i); // 推送了，直接删除
								i--;
							}
						}
					}
				}
			}
		}

		return lstResult;
	}

	/**
	 * 移除消息,移除小于指定时间戳的元素
	 * 
	 * */
	public void delMessage(long timeStamp) {
		onDelMessage(timeStamp);
	}

	public void onDelMessage(long timeStamp) {
		for (int i = 0; i < msgBlockList.size(); i++) {
			MessageBlock msgBlock = msgBlockList.get(i);
			if (msgBlock.getTimestamp() < timeStamp) {
				msgBlockList.remove(i);
				i--;
			}
		}
	}

	protected void onAddMessage(Message msg) {
		long now = DateTimeUtil.getNanoTime();
		long nowTime = DateTimeUtil.getMillTime();
		if (now > this.timestamp) {
			if (this.createBlock(nowTime, msg)) {
				this.timestamp = now; // 设置最后添加时间
			}
		} else {
			logger.debug("add message error.");
		}
	}

	/**
	 * 创建频道消息快
	 * 
	 * @param now
	 *            时间
	 * @param msg
	 *            内容
	 * @return 是否创建成功
	 */
	private boolean createBlock(long now, Message msg) {
		try {
			if (channelType.equals(EnumChannelType.Heartbeat)) {
				// logger.debug("添加消息块，时间戳：" +
				// now+",ReceiverID:"+msg.getReceiverID());
			}
			MessageBlock block = new MessageBlock(now, msg);
			msgBlockList.add(block);

			while (msgBlockList.size() > maxBlockCount) { // 如果超出承载量
				msgBlockList.remove(0);
			}
			return true;
		} catch (Exception ex) {
			logger.debug("create block error.");
			return false;
		}
	}

	public String getChannelName() {
		return channelName;
	}

	public EnumMessageAddressType getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public EnumChannelType getChannelType() {
		return channelType;
	}
}
