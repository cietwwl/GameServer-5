/**
 * 
 */
package com.mi.game.module.chat.define;

/**
 * 消息地址枚举
 * @author Administrator
 */
public enum EnumMessageAddressType {
	/**
	 * 信息
	 */
	Information(1),
	/**
	 * 玩家
	 */
	Person(2),
	/**
	 * 世界
	 */
	World(3),
	/**
	 * 隐藏
	 */
	Hide(4),
	/**
	 * 心跳
	 * */
	Heartbeat(5);
	
	
	
	private final int value;
	private EnumMessageAddressType(int value){
		this.value = value;
	}
	
	public int value(){
		return this.value;
	}
	
	public static EnumMessageAddressType fromInt(int value){
		switch(value){
		case 1:
			return Information;
		case 2:
			return Person;
		case 3:
			return World;
		case 5:
			return Heartbeat;
		default:
			return Hide;
			
		}
	}
}
