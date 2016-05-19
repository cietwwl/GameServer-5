/**
 * 
 */
package com.mi.game.module.welfare.data;


import org.apache.commons.lang.StringUtils;





import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.reward.data.GoodsBean;

@XmlTemplate(template = { "com/mi/template/MonkeyActivePrototype.xml" })
public class MonkeyActiveData extends BaseTemplate{
	private String time;
	private GoodsBean reward;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public GoodsBean getReward() {
		return reward;
	}
	public void setReward(String reward) {
		if(!StringUtils.isBlank(reward)){
			String[] arr = reward.split("=");
			this.reward = new GoodsBean(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]));
		}
	}
	
	
	
}
