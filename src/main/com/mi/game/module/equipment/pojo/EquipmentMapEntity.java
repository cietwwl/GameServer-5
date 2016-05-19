package com.mi.game.module.equipment.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class EquipmentMapEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5540441202961864352L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private Map<String, Equipment> equipMap = new HashMap<String, Equipment>(); // 装备列表
	private Map<Integer, EquipmentShard> shardList = new HashMap<Integer, EquipmentShard>(); // 碎片列表
	private int maxEquipNum; // 最大装备数
	private int maxFragmentNum; // 装备碎片数

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		if(equipMap != null){
			Map<String,Map<String,Object>> returnEquipMap = new HashMap<>();
			for(Entry<String,Equipment> entry : equipMap.entrySet()){
				returnEquipMap.put(entry.getKey(), entry.getValue().responseMap());
			}
			data.put("equipMap", returnEquipMap);
		}
		
		data.put("maxEquipNum", maxEquipNum);
		data.put("maxFragmentNum", maxFragmentNum);
		if (shardList != null) {
			data.put("shardList", shardList.values());
		}
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerID", playerID);
		data.put("equipMapSize", equipMap.size());
		data.put("maxEquipNum", maxEquipNum);
		data.put("maxFragmentNum", maxFragmentNum);
		data.put("shardListSize", shardList.values().size());
		return data;
	}

	public Map<Integer, EquipmentShard> getShardList() {
		if (shardList == null) {
			shardList = new HashMap<>();
		}
		return shardList;
	}

	public void setShardList(Map<Integer, EquipmentShard> shardList) {
		this.shardList = shardList;
	}

	public int getMaxFragmentNum() {
		return maxFragmentNum;
	}

	public void setMaxFragmentNum(int maxFragmentNum) {
		this.maxFragmentNum = maxFragmentNum;
	}

	public int getMaxEquipNum() {
		return maxEquipNum;
	}

	public void setMaxEquipNum(int maxEquipNum) {
		this.maxEquipNum = maxEquipNum;
	}

	public Map<String, Equipment> getEquipMap() {
		if (equipMap == null) {
			equipMap = new HashMap<>();
		}
		return equipMap;
	}

	public void setEquipMap(Map<String, Equipment> equipMap) {
		this.equipMap = equipMap;
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
		this.playerID = key.toString();
	}

}
