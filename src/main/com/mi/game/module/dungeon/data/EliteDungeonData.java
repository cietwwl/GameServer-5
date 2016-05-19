package com.mi.game.module.dungeon.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.defines.KindIDs;
import com.mi.game.module.reward.data.GoodsBean;

@XmlTemplate(template = {"com/mi/template/DungeonElitePrototype.xml"})
public class EliteDungeonData extends BaseTemplate{
	private int soulReward;
	private int coinReward;
	private Map<Integer,Integer> dropList; //掉落列表
	private int openAct;
	private List<GoodsBean> addList;
	private int maxStage;
	private int nextOpenID;
	
	public int getNextOpenID() {
		return nextOpenID;
	}
	public void setNextOpenID(int nextOpenID) {
		this.nextOpenID = nextOpenID;
	}
	public int getMaxStage() {
		return maxStage;
	}
	public void setMaxStage(int maxStage) {
		this.maxStage = maxStage;
	}
	public List<GoodsBean> getAddList() {
		if(addList == null){
			addList = new ArrayList<GoodsBean>();
		}
		return addList;
	}
	public void setAddList(List<GoodsBean> addList) {
		this.addList = addList;
	}
	public int getSoulReward() {
		return soulReward;
	}
	public void setSoulReward(int soulReward) {
		this.soulReward = soulReward;
		GoodsBean goodsBean = new GoodsBean(KindIDs.HEROSOUL,soulReward);
		this.getAddList().add(goodsBean);
	}
	public int getCoinReward() {
		return coinReward;
	}
	public void setCoinReward(int coinReward) {
		this.coinReward = coinReward;
		GoodsBean goodsBean = new GoodsBean(KindIDs.SILVERTYPE,coinReward);
		this.getAddList().add(goodsBean);
	}
	public Map<Integer, Integer> getDropList() {
		return dropList;
	}
	public void setDropList(String dropList) {
		if(dropList != null && !dropList.isEmpty()){
			this.dropList = new HashMap<Integer, Integer>();
			String[] tempArr = dropList.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.dropList.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
				}
			}
	}
	}
	public int getOpenAct() {
		return openAct;
	}
	public void setOpenAct(int openAct) {
		this.openAct = openAct;
	}
	
}
