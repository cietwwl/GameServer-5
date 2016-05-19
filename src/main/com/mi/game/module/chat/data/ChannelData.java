package com.mi.game.module.chat.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.chat.define.EnumChannelType;

@XmlTemplate(template = { "com/mi/template/channel.xml" })
public class ChannelData extends BaseTemplate {
	private int interval = 0;
	private EnumChannelType type;
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public EnumChannelType getType() {
		return type;
	}
	public void setType(int value) {
		type = EnumChannelType.fromInt(value);
	}
}
