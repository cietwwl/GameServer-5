package com.mi.game.module.hero.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

/**
 * @author 刘凯旋	
 * 玩家出战实体
 * 2014年5月29日 下午2:39:34
 */
@Entity(noClassnameStored = true)
public class HeroTroopsEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4475787495009684846L;
	@Indexed(value = IndexDirection.ASC, unique = true)	
	private String playerID;
	private List<Long> troops;
	
	public List<Long> getTroops() {
		return troops;
	}
	
	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("troops", troops);
		return data;
	}

	public void setTroops(List<Long> troops) {
		this.troops = troops;
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
