package com.mi.game.module.chat.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.chat.Channel;
import com.mi.game.module.chat.ChatModule;
import com.mi.game.module.chat.classes.Message;
import com.mi.game.module.chat.define.EnumChannelType;
import com.mi.game.module.chat.protocol.ChatRoomResponse;

@HandlerType(type=HandlerIds.CHAT_RECEIVE ,order=2)
public class ReceiveMessageHandler extends BaseHandler {
	public static Logger logger = LoggerFactory.getLogger(ReceiveMessageHandler.class);
	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		int channelID  = Integer.parseInt(ioMessage.getInputParse("channelID").toString());
		long startTime = Long.parseLong(ioMessage.getInputParse("startTime").toString());
		long endTime   =  Long.parseLong(ioMessage.getInputParse("endTime").toString());
		ChatModule module = ModuleManager.getModule(ModuleNames.ChatModule,ChatModule.class);
		EnumChannelType type = EnumChannelType.fromInt(channelID);
		Channel channel = module.getChannel(type);
		long[] arr = new long[1];
		List<Message> lst =  channel.getMessages(playerID,startTime, endTime, arr);

		ChatRoomResponse chatRoomResponse=new ChatRoomResponse();
		chatRoomResponse.channelType=type;
		if(arr[0]!= 0 ){
			chatRoomResponse.starTime= arr[0] + "";
		}else
		{
			chatRoomResponse.starTime=DateTimeUtil.getMillTime() + "";
		}
		if(lst==null)
			lst=new ArrayList<Message>();
		chatRoomResponse.msgList=lst;
		if(type.equals( EnumChannelType.Heartbeat)){
			chatRoomResponse.setSystemTime(System.currentTimeMillis());
		}
		ioMessage.getTransmitter().setOutputResult(chatRoomResponse);
	}
}



