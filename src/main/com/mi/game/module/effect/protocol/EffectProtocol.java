package com.mi.game.module.effect.protocol;

import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.effect.pojo.Effect;

public class EffectProtocol extends BaseProtocol{
	private Map<String,Object> itemMap;
	private Effect effect ;

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	
	
}
