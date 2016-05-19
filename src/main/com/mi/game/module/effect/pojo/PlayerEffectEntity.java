package com.mi.game.module.effect.pojo;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class PlayerEffectEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3486826204432364065L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private Map<Integer,Effect> effectMap;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<>();
		if(effectMap != null)
			data.put("effectMap",effectMap.values());
		return data;
	}
	
	public  Map<Integer, Effect> getEffectMap() {
		if(effectMap == null){
			effectMap = new HashMap<Integer, Effect>();
		}
		return effectMap;
	}

	public void setEffectMap(Map<Integer, Effect> effectMap) {
		this.effectMap = effectMap;
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
	public void setKey(Object arg0) {
		// TODO 自动生成的方法存根
		playerID = arg0.toString();
	}	
}
