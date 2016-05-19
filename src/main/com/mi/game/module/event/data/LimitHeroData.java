package com.mi.game.module.event.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/LimitHeroPrototype.xml" })
public class LimitHeroData extends EventBaseData {

	private int dropID;
	private int SPdropID;
	private int[] contects;
	private Map<Integer, Integer> rank1Reward;
	private Map<Integer, Integer> rank2Reward;
	private Map<Integer, Integer> rank3Reward;
	private Map<Integer, Integer> rank4Reward;

	public int getDropID() {
		return dropID;
	}

	public void setDropID(int dropID) {
		this.dropID = dropID;
	}

	public int getSPdropID() {
		return SPdropID;
	}

	public void setSPdropID(int sPdropID) {
		SPdropID = sPdropID;
	}

	public int[] getContects() {
		return contects;
	}

	public void setContects(String str) {
		if (str != null && !str.isEmpty()) {
			String[] arr = str.split(",");
			contects = new int[arr.length];
			for (int i = 0; i < arr.length; i++) {
				contects[i] = Integer.parseInt(arr[i]);
			}
		}
	}

	public Map<Integer, Integer> getRank1Reward() {
		return rank1Reward;
	}

	public void setRank1Reward(String str) {
		this.rank1Reward = setMultiple(str);
	}

	public Map<Integer, Integer> getRank2Reward() {
		return rank2Reward;
	}

	public void setRank2Reward(String str) {
		this.rank2Reward = setMultiple(str);
	}

	public Map<Integer, Integer> getRank3Reward() {
		return rank3Reward;
	}

	public void setRank3Reward(String str) {
		this.rank3Reward = setMultiple(str);
	}

	public Map<Integer, Integer> getRank4Reward() {
		return rank4Reward;
	}

	public void setRank4Reward(String str) {
		this.rank4Reward = setMultiple(str);
	}

}
