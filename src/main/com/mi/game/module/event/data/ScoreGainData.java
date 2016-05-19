package com.mi.game.module.event.data;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = {
	"com/mi/template/ScoreGainPrototype.xml"
})
public class ScoreGainData extends EventBaseData {
	private int activeReward;

	public int getActiveReward() {
		return activeReward;
	}

	public void setActiveReward(int activeReward) {
		this.activeReward = activeReward;
	}
}
