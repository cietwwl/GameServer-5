package com.mi.game.module.pet.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.reward.data.GoodsBean;
@XmlTemplate(template = {"com/mi/template/PetSpecialSkillPrototype.xml"})
public class PetSpecialSkillData extends BaseTemplate{
	private List<GoodsBean> rewardItem = new ArrayList<GoodsBean>();
	private long rewardTime;
	
	public List<GoodsBean> getRewardItem() {
		return rewardItem;
	}
	public void setRewardItem(String rewardItem) {
		if(rewardItem != null){
			List<GoodsBean> goodsList  = new ArrayList<GoodsBean>(); 
			String[] strArr = rewardItem.split(",");
			for(String temp :strArr ){
				String[] costArr = temp.split("=");
				if(costArr != null){
					GoodsBean goodsBean = new GoodsBean(Integer.parseInt(costArr[0]),  Integer.parseInt(costArr[1]));
					goodsList.add(goodsBean);
				}
			}
			this.rewardItem = goodsList;
		}
		
	}
	public long getRewardTime() {
		return rewardTime;
	}
	public void setRewardTime(long rewardTime) {
		this.rewardTime = rewardTime;
	}
	
	
}
