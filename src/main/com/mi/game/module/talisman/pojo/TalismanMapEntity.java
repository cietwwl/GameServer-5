package com.mi.game.module.talisman.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

/**
 * @author 刘凯旋
 *
 *         2014年6月23日 下午1:57:46
 */

@Entity(noClassnameStored = true)
public class TalismanMapEntity extends BaseEntity{


	@Indexed(value=IndexDirection.ASC, unique=true)
	private String playerID;

	private static final long serialVersionUID = 2006212458430534465L;

	private Map<String, TalismanEntity> talismanMap = new HashMap<String, TalismanEntity>();

	private int maxTalismanNum;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("talismanMap", talismanMap);
		data.put("maxTalismanNum", maxTalismanNum);
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		switch (t) {
		case 11111:
			data.put("playerID", playerID);
			data.put("talismanMapSize", talismanMap.size());
			data.put("maxTalismanNum", maxTalismanNum);
			break;
		}
		return data;
	}

	public int getMaxTalismanNum() {
		return maxTalismanNum;
	}

	public void setMaxTalismanNum(int maxTalismanNum) {
		this.maxTalismanNum = maxTalismanNum;
	}

	public Map<String, TalismanEntity> getTalismanMap() {
		if (talismanMap == null) {
			talismanMap = new HashMap<String, TalismanEntity>();
		}
		return talismanMap;
	}

	public void setTalismanMap(Map<String, TalismanEntity> talismanMap) {
		this.talismanMap = talismanMap;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		playerID = key.toString();
	}

}
