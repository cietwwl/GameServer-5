package com.mi.game.module.tower.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.reward.data.GoodsBean;
/**
 * @author 刘凯旋	
 *
 * 2014年10月27日 下午9:24:58
 */
@XmlTemplate(template ={"com/mi/template/TowerPrototype.xml"})
public class TowerData extends BaseTemplate{
	private Map<Integer,Integer> specialReward;
	private int soulReward;
	private int coinReward;
	private List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
	private int nextID;
	
	public int getNextID() {
		return nextID;
	}
	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
	public List<GoodsBean> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsBean> goodsList) {
		this.goodsList = goodsList;
	}
	public int getSoulReward() {
		return soulReward;
	}
	public int getCoinReward() {
		return coinReward;
	}
	public void setSoulReward(int soulReward) {
		this.soulReward = soulReward;
	}
	public void setCoinReward(int coinReward) {
		this.coinReward = coinReward;
	}
	public Map<Integer, Integer> getSpecialReward() {
		return specialReward;
	}
	public void setSpecialReward(String specialReward) {
		if(specialReward != null && !specialReward.isEmpty()){
			this.specialReward = new HashMap<Integer, Integer>();
			String[] tempArr = specialReward.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.specialReward.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
					GoodsBean goodsBean = new GoodsBean(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
					goodsList.add(goodsBean);
				}
			}
		}
	}
	
}
