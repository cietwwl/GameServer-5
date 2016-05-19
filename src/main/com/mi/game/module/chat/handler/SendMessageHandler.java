package com.mi.game.module.chat.handler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.chat.Channel;
import com.mi.game.module.chat.ChatModule;
import com.mi.game.module.chat.classes.Message;
import com.mi.game.module.chat.define.EnumChannelType;
import com.mi.game.module.chat.define.EnumMessageAddressType;
import com.mi.game.module.chat.define.EnumMessageType;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.util.CommonMethod;
import com.mi.game.util.Utilities;

@HandlerType(type = HandlerIds.CHAT_USER_SEND, order = 2)
public class SendMessageHandler extends BaseHandler {
	public static final Logger log = LoggerFactory
			.getLogger(SendMessageHandler.class);

	@Override
	public void execute(IOMessage ioMessage) {
		// TODO 自动生成的方法存根
		String playerID = ioMessage.getPlayerId();
		String str = ioMessage.getInputParse("message").toString();
		
		JSONObject msg = JSON.parseObject(str);
		BaseProtocol protocol = new BaseProtocol();
		ioMessage.getTransmitter().setOutputResult(protocol);
		if (msg == null) {
			log.debug("playerID:" + playerID + " message is null");
			protocol.setCode(1);
			return;
		}

		ChatModule module = ModuleManager.getModule( ModuleNames.ChatModule, ChatModule.class);
		LoginModule loginModule = ModuleManager.getModule( ModuleNames.LoginModule, LoginModule.class);
		
		PlayerEntity sendEntity = loginModule.getPlayerEntity(playerID);
		
		if(sendEntity == null){
			protocol.setCode(1);
			return;
		}
		// 消息内容
		String body = msg.getString("body");
		// 屏蔽聊天关键字
		if(!Utilities.checkLenght(body,2,80) ){
			throw new IllegalArgumentException(ErrorIds.ChatMessageLengthError + "");
		}
		body = CommonMethod.chatShieldedKeyword(body);
		
		// 消息地址类型-世界
		EnumMessageAddressType receiveType = EnumMessageAddressType
				.fromInt(Integer.parseInt(msg.getString("receiverType")));
		// 接受者 ID
		String receiverID = msg.getString("receiverID");
		// 发送者 ID
	//	String senderID = msg.getString("senderID");
		// 发送者消息地址类型-个人
		EnumMessageAddressType senderType = EnumMessageAddressType
				.fromInt(Integer.parseInt(msg.getString("senderType")));
		// 发送者姓名
	//	String senderName = msg.getString("senderName");
		// 接收者姓名
		String receiverName = msg.getString("receiverName");
		
		// 参数-1
		String strParams = msg.getString("strParams");
		// 消息类型=information
		EnumMessageType msgType = EnumMessageType.fromInt(Integer
				.parseInt(msg.getString("type")));

		// 获取接受者的频道类型
		EnumChannelType channelType = EnumChannelType.fromInt(receiveType.value());
		
		if(channelType.equals(EnumChannelType.Person)){ // 如果是私聊,查找私聊对象
			PlayerEntity receiverPlayer = null;
			if(StringUtils.isNotBlank(receiverName)){
				receiverPlayer = loginModule.getPlayerEntityByName(receiverName);
				if(receiverPlayer == null){
					logger.error("玩家不在线");
					protocol.setCode(ErrorIds.PlayerNoOnline );
					return;
				}
				long offTime  = receiverPlayer.getOffLineTime();
				if(offTime < System.currentTimeMillis()){
					logger.error("玩家不在线");
					protocol.setCode(ErrorIds.PlayerNoOnline );
					return ;
				}
			}
			if(receiverPlayer == null && StringUtils.isNotBlank(receiverID)){
				receiverPlayer = loginModule.getPlayerEntity(receiverID);
			}
			if(receiverPlayer == null){
				protocol.setCode(ErrorIds.PlayerNotExist);
				return;
			}
			receiverID = receiverPlayer.getKey().toString();
			if(playerID.equals(receiverID)){
				protocol.setCode(ErrorIds.canNotChatSelf);
				return;
			}
		}
		
		// 服务器消息-返回消息
		Message smsg = new Message(senderType, sendEntity, receiveType, receiverID, body, msgType, strParams);
		
		if(msg.containsKey("parse")){
			smsg.setParse(msg.getString("parse"));
		}
		
		Channel channel = module.getChannel(channelType);
		// 保存消息
		channel.addMessage(smsg);
	}
}
