package com.mi.game.module.wallet.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

@Entity(noClassnameStored = true)
public class WalletEntity extends BaseEntity {
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	// 金币
	private long gold;
	// 银币
	private long silver;
	// 将魂
	private long heroSoul;
	// 魂玉
	private long jewelSoul;
	// 声望
	private long reputation;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public long getReputation() {
		return reputation;
	}

	public void setReputation(long reputation) {
		this.reputation = reputation;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

	public long getSilver() {
		return silver;
	}

	public void setSilver(long silver) {
		this.silver = silver;
	}

	public long getHeroSoul() {
		return heroSoul;
	}

	public void setHeroSoul(long heroSoul) {
		this.heroSoul = heroSoul;
	}

	public long getJewelSoul() {
		return jewelSoul;
	}

	public void setJewelSoul(long jewelSoul) {
		this.jewelSoul = jewelSoul;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gold", gold);
		data.put("silver", silver);
		data.put("heroSoul", heroSoul);
		data.put("jewelSoul", jewelSoul);
		data.put("reputation", reputation);
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerID", playerID);
		data.put("gold", gold);
		data.put("silver", silver);
		data.put("heroSoul", heroSoul);
		data.put("jewelSoul", jewelSoul);
		data.put("reputation", reputation);
		return data;
	}

	private static final long serialVersionUID = -8872304825158517032L;

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
