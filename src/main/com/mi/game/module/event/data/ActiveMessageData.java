package com.mi.game.module.event.data;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/ActiveMessagePrototype.xml" })
public class ActiveMessageData extends EventBaseData {

	private String title;
	private String contents;
	private int activeID;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getActiveID() {
		return activeID;
	}

	public void setActiveID(int activeID) {
		this.activeID = activeID;
	}

}
