package com.mi.game.module.pay.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = {
	"com/mi/template/ChargePrototype.xml"
})
public class PayData extends PayBaseData {
	// 充值金额
	private int rmb;
	// 充值金币
	private Map<Integer, Integer> gold = new HashMap<Integer, Integer>();
	// 首冲金币
	private Map<Integer, Integer> firstGold = new HashMap<Integer, Integer>();
	// 首冲奖励
	private Map<Integer, Integer> firstItem = new HashMap<Integer, Integer>();

	public int getRmb() {
		return rmb;
	}

	public void setRmb(int rmb) {
		this.rmb = rmb;
	}

	public Map<Integer, Integer> getGold() {
		return gold;
	}

	public void setGold(String str) {
		this.gold = setMultiple(str);
	}

	public Map<Integer, Integer> getFirstGold() {
		return firstGold;
	}

	public void setFirstGold(String str) {
		this.firstGold = setMultiple(str);
	}

	public Map<Integer, Integer> getFirstItem() {
		return firstItem;
	}

	public void setFirstItem(String str) {
		this.firstItem = setMultiple(str);
	}
}
