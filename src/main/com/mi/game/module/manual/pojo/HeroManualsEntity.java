package com.mi.game.module.manual.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

/**
 * @author 刘凯旋	
 * 图鉴/名将实体
 * 2014年7月3日 上午10:47:28
 */
@Entity(noClassnameStored = true)
public class HeroManualsEntity extends  BaseEntity{
		/**
	 * 
	 */
	private static final long serialVersionUID = -5961935662823006029L;
	@Indexed(value=IndexDirection.ASC, unique=true)
	private String playerID;
	private Map<Integer, HeroManual> heroList;
	private int point;

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public Map<Integer, HeroManual> getHeroList() {
		if(heroList == null){
			heroList = new HashMap<Integer,HeroManual>();
		}
		return heroList;
	}

	public void setHeroList(Map<Integer, HeroManual> heroList) {
		this.heroList = heroList;
	}
	
	/**
	 * 增加经验
	 * */
	
	public void addManualExp(){
		
	}
	
	@Override
	public Map<String, Object> responseMap(){
		Map<String,Object> data  = new HashMap<>();
		if(heroList != null)
			data.put("heroList", heroList.values());
		return data;
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
