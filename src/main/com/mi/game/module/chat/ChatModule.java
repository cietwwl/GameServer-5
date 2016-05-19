package com.mi.game.module.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.job.BaseJob;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.InformationMessageType;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.chat.classes.Message;
import com.mi.game.module.chat.data.ChannelData;
import com.mi.game.module.chat.data.JobData;
import com.mi.game.module.chat.define.EnumChannelType;
import com.mi.game.module.chat.define.EnumMessageAddressType;
import com.mi.game.module.chat.define.EnumMessageType;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;

@Module(clazz=ChatModule.class,name=ModuleNames.ChatModule)
public class ChatModule extends BaseModule {
	public final static Logger log = LoggerFactory.getLogger(ChatModule.class);
	/**
	 * 频道集合
	 */
	private Map<EnumChannelType, Channel> mapChannels = null;	//频道集合
	private Scheduler scheduler = null;


	@Override
	public void init() {
		// TODO 自动生成的方法存根
		super.init();
		startup();
	}

	@SuppressWarnings("unused")
	private void ttt(){

//		//初始化频道,根据配置
//		Channel c1 = new Channel(EnumChannelType.Information, "system", 1);
//		Channel c2 = new Channel(EnumChannelType.World, "world", 1);
//
//		this.addChannel(c1);
//		this.addChannel(c2);
//
//		long fromTime = DateTimeUtil.getNanoTime();
//
//		Message msg = new Message(EnumMessageAddressType.World, "1", "11", EnumMessageAddressType.World, 
//				"1", "hello channel1", EnumMessageType.Infomation, "");
//		c1.addMessage(msg);
//		msg = new Message(EnumMessageAddressType.World, "1", "11", EnumMessageAddressType.World, 
//				"1", "hello channel2", EnumMessageType.Infomation, "");
//		c1.addMessage(msg);
//		msg = new Message(EnumMessageAddressType.World, "1", "11", EnumMessageAddressType.World, 
//				"1", "hello channel3", EnumMessageType.Infomation, "");
//		c1.addMessage(msg);
//		msg = new Message(EnumMessageAddressType.World, "1", "11", EnumMessageAddressType.World, 
//				"1", "hello channel4", EnumMessageType.Infomation, "");
//		c2.addMessage(msg);
//
//		Long newTime = null;
//		Long[] arr = new Long[1];
//		List<MessageBlock> lst = c1.getMessages(fromTime, DateTimeUtil.getNanoTime(), arr);
//		log.debug("channel1 message count : "+lst.size());
//		lst = c2.getMessages(fromTime, DateTimeUtil.getNanoTime(), arr);
//		log.debug("channel2 message count : "+lst.size());
	}

	//聊天引擎启动
	private void startup(){
		this.mapChannels = new HashMap<EnumChannelType, Channel>();
		initChannels();				//initial channels
		try{
			initScheduler();			//initial scheduler
		}catch(SchedulerException ex){
			log.debug("chat module error line 81. des : " + ex.getMessage());
		}
		
		log.debug("chat engine started.");
	}

	/**
	 * 初始化调度器
	 * @throws SchedulerException
	 */
	private void initScheduler() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		List<JobData> jobs = TemplateManager.getKindTemplateList(KindIDs.Jobs);
		for(JobData element : jobs){
			BaseJob job = null;
			try {
				job = element.createObject();
				job.addJobToScheduler(scheduler);
			} catch (Exception e) {
				log.debug("create job error ,"+e.getMessage());
			}
		}
		scheduler.start();
	}

	/**
	 * 初始化频道集合
	 */
	private void initChannels(){
		List<ChannelData> lst = TemplateManager.getKindTemplateList(KindIDs.ChatChannel);
		Channel channel = null;
		for(ChannelData element : lst){
			channel = new Channel(element.getType(), element.getName(), element.getPid());
			this.addChannel(channel);
		}

		log.debug("initial channels length : " + lst.size());
	}

	/**
	 * 添加一个频道
	 * @param channel
	 */
	public void addChannel(Channel channel){
		try{
			Channel old = null;
			if(mapChannels.containsKey(channel.getChannelType())){
				old = mapChannels.get(channel.getChannelType());
				mapChannels.remove(channel.getChannelType());
			}
			if(old!=null)
				old = null;
			synchronized (mapChannels) {
				mapChannels.put(channel.getChannelType(), channel);
			}
		}catch(Exception ex){
			log.debug("ChatModule error channelID : " + channel.getChannelName());
		}
	}

	/**
	 * 添加一个频道
	 * @param type
	 * @param channelName
	 * @param channelID
	 */
	public void addChannel(EnumChannelType type, String channelName,int channelID){

		Channel channel = new Channel(type, channelName, channelID);
		mapChannels.put(type, channel);
	}

	/**
	 * 返回一个频道
	 * @param type
	 * @return
	 */
	public Channel getChannel(EnumChannelType type){
		if(mapChannels.containsKey(type))
			return mapChannels.get(type);
		return null;
	}

	/**
	 * 根据编号返回频道
	 * @param channelID
	 * @return
	 */
	public Channel getChannel(int channelID){
		for(Entry<EnumChannelType, Channel> element : mapChannels.entrySet()){
			if(element.getValue().getId()==channelID)
				return element.getValue();
		}

		return null;
	}

	/**
	 * 移除一个频道
	 * @param type
	 * @return
	 */
	public Channel removeChannel(EnumChannelType type){
		Channel channel = null;
		if(mapChannels.containsKey(type)){
			synchronized (mapChannels) {
				channel = mapChannels.remove(type);	
			}
		}
		if(channel!=null)
			return channel;
		else
			return  null;
	}

	/**
	 * 发送系统消息,默认地址information
	 * @param body
	 */
	public void sendInformationMessage(String body){
		Message msg = new Message(EnumMessageAddressType.Information, "0", "system", EnumMessageAddressType.Information, "0", body, EnumMessageType.Infomation, "");
		Channel channel = mapChannels.get(EnumChannelType.Information);
		channel.addMessage(msg);
	}
	
	public void sendInformationMessage(String body,String strParams){
		Message msg = new Message(EnumMessageAddressType.Information, "0", "system", EnumMessageAddressType.Information, "0", body, EnumMessageType.Infomation, strParams);
		Channel channel = mapChannels.get(EnumChannelType.Information);
		channel.addMessage(msg);
	}
	
	public void sendWorldMessage(String body){
		Message msg = new Message(EnumMessageAddressType.World, "0", "system", EnumMessageAddressType.World, "0", body, EnumMessageType.Infomation, "");
		Channel channel = mapChannels.get(EnumChannelType.World);
		channel.addMessage(msg);
	}
	
	/**
	 * 发送系统消息
	 * @param body
	 */
	public void sendInformationMessage(Channel channel, Message msg){
		if(channel==null){
			//log.debug("chat module sendinformation error channel is null.");
			return;
		}
		channel.addMessage(msg);
	}
	
	public void sendInformationMessage(EnumChannelType channelType,EnumMessageType messageType, String body){
		EnumMessageAddressType addressType = EnumMessageAddressType.fromInt(channelType.value());
		Message msg = new Message(addressType, "0", "system", addressType, "0", body, messageType, "");
		Channel channel = mapChannels.get(channelType);
		sendInformationMessage(channel, msg);
	}
	
	
	/**
	 * 删除过期时间消息
	 * 
	 */
	public void clearMessage(long timeStamp){
		List<ChannelData> lst = TemplateManager.getKindTemplateList(KindIDs.ChatChannel);
		Channel channel = null;
		for(ChannelData element : lst){
			EnumChannelType channelType = element.getType();
			channel = mapChannels.get(channelType);
			long delTime = DateTimeUtil.getMillTime() - SysConstants.MsgDelTime/10;
			channel.delMessage(delTime);
		}	
	}
	
	/**
	 * @param
	 *  playerID String 玩家ID
	 *  templateID int  模板ID
	 *  messageType int 消息类型
	 *  names String[] 显示的物品名称  
	 * */
	
	public void addInformation(String playerID,int messageType,int param,Object... names){
		String message = "" ;
		LoginModule  loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		String nickName = playerEntity.getNickName();
		switch(messageType){
			case InformationMessageType.advanceHero:
				message = "恭喜"+ nickName + "将护法[d514f4]"+ names[0]+"进阶到了[-]+[d514f4]"+ param+ "[-]，战斗力大大增加，简直太厉害了";
			break;
			case InformationMessageType.tenBestDraw:
				String nameString = "";
				for(Object name : names){
					nameString += "[d514f4]" + name + "[-],";
				}
				message = "恭喜"+nickName + "在商城★天神十连抽中获得了"+nameString + "笑傲西游指日可待";
			break;
			case InformationMessageType.bestDraw:
				message = "恭喜"+nickName + "在商城★天神抽卡中获得了[d514f4]"+names[0] + "[-], 笑傲西游指日可待";
			break;
			case InformationMessageType.betterDraw:
				message = "恭喜"+nickName + "在商城★仙将抽卡中获得了[d514f4]"+names[0] + "[-], 笑傲西游指日可待";
			break;
			case InformationMessageType.normalDraw:
				message = "恭喜"+nickName + "在商城★灵兵抽卡中获得了[d514f4]"+names[0] + "[-], 笑傲西游指日可待";
			break;
			case InformationMessageType.worldBossActive:
				message = "【[d514f4]"+nickName+"[-]】霸气侧漏，在[d514f4]决战混世天魔[-]活动中，对混世天魔造成了[ff0000]巨量伤害[-]！善财童子送上大礼一份!";
				break;
		}
		this.sendInformationMessage(message);
	}
	
}
