package com.mi.game.module.dungeon.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.reward.data.GoodsBean;

/**
 * @author 刘凯旋	
 * 大关卡模板类
 * 2014年6月4日 上午11:45:34
 */
@XmlTemplate(template={"com/mi/template/DungeonActPrototype.xml"})
public class ActData extends BaseTemplate{
	private Map<Integer,List<GoodsBean>> reward;
	private List<Integer> includeDungeon ;
	private int openLevel;
	private int maxStar;
	
	public Map<Integer, List<GoodsBean>> getReward() {
		return reward;
	}
	public void setReward(String reward) {
		if(reward != null && !reward.isEmpty()){
			this.reward = new HashMap<Integer, List<GoodsBean>>();
			String[] strArr  = reward.split(";");
			for(String temp : strArr){
				String[] rewardArr = temp.split("\\|");
				int key = Integer.parseInt(rewardArr[0]);
				if(key != -1){
					String rewardInfo = rewardArr[1];
					String[] rewardInfoArr = rewardInfo.split(",");
					List<GoodsBean> rewardList = new ArrayList<GoodsBean>();
					for(String rewardFinal : rewardInfoArr){
						String[] rewardFinalTemp = rewardFinal.split("=");
						GoodsBean goodsBean = new GoodsBean(Integer.parseInt(rewardFinalTemp[0]),Integer.parseInt(rewardFinalTemp[1]));
						rewardList.add(goodsBean);
					}
					this.reward.put(Integer.parseInt(rewardArr[0]), rewardList);
				}
			
			}
		}
	
	}
	public List<Integer> getIncludeDungeon() {
		return includeDungeon;
	}
	public void setIncludeDungeon(String  includeDungeon) {
		if(includeDungeon != null && !includeDungeon.isEmpty()){
			this.includeDungeon = new ArrayList<Integer>();;
			String[] stringArr = includeDungeon.split(",");
			for(String temp : stringArr){
				this.includeDungeon.add(Integer.parseInt(temp));
			}
		}
		
	
	}
	public int getOpenLevel() {
		return openLevel;
	}
	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}
	public int getMaxStar() {
		return maxStar;
	}
	public void setMaxStar(int maxStar) {
		this.maxStar = maxStar;
	}
	
 }
