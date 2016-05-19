package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.List;

import com.mi.game.module.reward.data.GoodsBean;

public class Exchange {
	// 需要物品列表 key=pid ,value=num
	private List<GoodsBean> wantList = new ArrayList<GoodsBean>();
	// 兑换结果
	private GoodsBean result;
	// 兑换次数
	private int num;
	// 最大兑换次数
	private int maxNum;
	// 刷新次数
	private int refrushNum;
	// 兑换价格
	private int gold;
	private int pid;

	public List<GoodsBean> getWantList() {
		return wantList;
	}

	public void setWantList(List<GoodsBean> wantList) {
		this.wantList = wantList;
	}

	public GoodsBean getResult() {
		return result;
	}

	public void setResult(GoodsBean result) {
		this.result = result;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getRefrushNum() {
		return refrushNum;
	}

	public void setRefrushNum(int refrushNum) {
		this.refrushNum = refrushNum;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	/**
	 * 增加刷新次数
	 */
	public void addRefrushNum() {
		refrushNum++;
	}

	/**
	 * 是否可以兑换
	 * 
	 * @return
	 */
	public boolean checkExchange() {
		if (num < maxNum) {
			num++;
			return true;
		}
		return false;
	}
}
