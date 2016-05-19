package com.mi.game.module.heroDraw.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class HeroDrawEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4956975976682386736L;
	@Indexed(value = IndexDirection.ASC, unique = true)	
	private String playerID;
	private int drawNum;
	private int allDrawNum;
	private long lastBetterDrawTime;
	private long lastBestDrawTime;
	private int newPlayerBatterDraw;
	private int firstBule;
	private int firstPurple;
	private int normalDrawNum;
	private int newPlayerBestDraw;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("drawNum",drawNum);
		data.put("lastBetterDrawTime", lastBetterDrawTime);
		data.put("lastBestDrawTime", lastBestDrawTime);
		data.put("firstBule", firstBule);
		data.put("firstPurple", firstPurple);
		return data;
	}

	
	public int getNewPlayerBestDraw() {
		return newPlayerBestDraw;
	}

	public void setNewPlayerBestDraw(int newPlayerBestDraw) {
		this.newPlayerBestDraw = newPlayerBestDraw;
	}

	public int getNormalDrawNum() {
		return normalDrawNum;
	}

	public void setNormalDrawNum(int normalDrawNum) {
		this.normalDrawNum = normalDrawNum;
	}

	public int getFirstBule() {
		return firstBule;
	}

	public void setFirstBule(int firstBule) {
		this.firstBule = firstBule;
	}

	public int getFirstPurple() {
		return firstPurple;
	}

	public void setFirstPurple(int firstPurple) {
		this.firstPurple = firstPurple;
	}



	public int getNewPlayerBatterDraw() {
		return newPlayerBatterDraw;
	}

	public void setNewPlayerBatterDraw(int newPlayerBatterDraw) {
		this.newPlayerBatterDraw = newPlayerBatterDraw;
	}

	public long getLastBetterDrawTime() {
		return lastBetterDrawTime;
	}

	public void setLastBetterDrawTime(long lastBetterDrawTime) {
		this.lastBetterDrawTime = lastBetterDrawTime;
	}


	public long getLastBestDrawTime() {
		return lastBestDrawTime;
	}


	public void setLastBestDrawTime(long lastBestDrawTime) {
		this.lastBestDrawTime = lastBestDrawTime;
	}


	public int getAllDrawNum() {
		return allDrawNum;
	}


	public void setAllDrawNum(int allDrawNum) {
		this.allDrawNum = allDrawNum;
	}


	public int getDrawNum() {
		return drawNum;
	}

	public void setDrawNum(int drawNum) {
		this.drawNum = drawNum;
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
