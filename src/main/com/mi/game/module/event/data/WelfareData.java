package com.mi.game.module.event.data;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/BenefitPrototype.xml" })
public class WelfareData extends EventBaseData {
	private int param;
	private int target;
	private String contents;
	private String desc;

	public int getParam() {
		return param;
	}

	public void setParam(int param) {
		this.param = param;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
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
}
