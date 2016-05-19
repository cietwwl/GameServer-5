package com.mi.game.module.chat.define;

/**
 * 频道枚举
 * 
 * @author Administrator
 */
public enum EnumChannelType {
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
	 */
	Heartbeat(5);
	
	private int value = 0;
	private EnumChannelType(int value) {
		this.value = value;
	}

	public static EnumChannelType fromInt(int value) {
		switch (value) {
		case 1:
			return Information;
		case 2:
			return Person;
		case 3:
			return World;
		case 4:
			return Hide;
		case 5:
			return Heartbeat;
		default:
			return Hide;
		}
	}

	public int value() {
		return value;
	}
}
