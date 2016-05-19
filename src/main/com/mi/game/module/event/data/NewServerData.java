package com.mi.game.module.event.data;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/NewServerActivePrototype.xml" })
public class NewServerData extends EventBaseData {
	private int activeID;
	private String contents;
	private String desc;
	private int param;

	public int getActiveID() {
		return activeID;
	}

	public void setActiveID(int activeID) {
		this.activeID = activeID;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getParam() {
		return param;
	}

	public void setParam(int param) {
		this.param = param;
	}

}
