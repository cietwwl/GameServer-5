package com.mi.game.module.legion.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/LegionDevotePrototype.xml" })
public class LegionDevoteData extends LegionBaseData {
	// 建设消耗
	private Map<Integer, Integer> devoteRequire;
	// 建设获得
	private Map<Integer, Integer> devoteReward;

	/**
	 * 获取消耗数量
	 * 
	 * @return
	 */
	public Integer getRequireValue(Integer key) {
		return devoteRequire.get(key);
	}
	/**
	 * 获取奖励数量
	 * 
	 * @return
	 */
	public Integer getRewardValue(Integer key) {
		return devoteReward.get(key);
	}

	public Map<Integer, Integer> getDevoteRequire() {
		return devoteRequire;
	}

	public void setDevoteRequire(String str) {
		this.devoteRequire = setMultiple(str);
	}

	public Map<Integer, Integer> getDevoteReward() {
		return devoteReward;
	}

	public void setDevoteReward(String str) {
		this.devoteReward = setMultiple(str);
	}

	@Override
	public String toString() {
		return "LegionDevoteData [devoteRequire=" + devoteRequire + ", devoteReward=" + devoteReward + "]";
	}

}
