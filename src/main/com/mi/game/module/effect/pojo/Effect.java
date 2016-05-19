package com.mi.game.module.effect.pojo;

import java.util.HashMap;
import java.util.Map;

public class Effect {
	/**
	 * 
	 */
	private long endTime; //单位毫秒
	private int  templateID;
	private Map<String,Object> map = new HashMap<String, Object>();
	public Effect(){}
	public Effect(int templateID, long endTime,HashMap<String, Object> map){
		this.endTime = endTime;
		this.templateID = templateID;
		this.putAll(map);
	}
	
	public void putAll(HashMap<String, Object> map){
		this.map = map;
	}
	
	public void put(String key,Object value){
		map.put(key, value);
	}
	
	public Object get(String key){
		return map.get(key);
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getTemplateID() {
		return templateID;
	}
	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "templateID:"+templateID+",endTime:"+endTime+",map:"+map;
	}
	
}
