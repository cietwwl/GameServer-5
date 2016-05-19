/**
 * 
 */
package com.mi.game.module.chat.define;

/**
 * 消息类型枚举
 * @author Administrator
 */
public enum EnumMessageType {
	/**
	 * 系统命令消息
	 */
	Command(1),
	/**
	 * 系统一般消息
	 */
	Infomation(2);
	
	private int value = 0;
	private EnumMessageType(int value){
		this.value = value;
	}
	
	public int value(){
		return this.value;
	}
	
	public static EnumMessageType fromInt(int value){
		switch(value){
		case 1:
			return Command;
		case 2:
		default:
			return Infomation;
				
		}
	}
}
