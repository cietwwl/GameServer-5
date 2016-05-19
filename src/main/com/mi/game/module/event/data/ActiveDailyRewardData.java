package com.mi.game.module.event.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/ActiveDailyRewardPrototype.xml" })
public class ActiveDailyRewardData extends EventBaseData {
	
	private Map<Integer, Integer> itemID;
	private Map<Integer, Integer> VIPItemID;
	private String itemIDValue;
	private String VIPItemIDValue;
	private int vipRequest;
	
	public int getVipRequest() {
		return vipRequest;
	}

	public void setVipRequest(int vipRequest) {
		this.vipRequest = vipRequest;
	}

	public void setItemIDValue(String itemIDValue) {
		this.itemIDValue = itemIDValue;
	}

	public void setVIPItemIDValue(String vIPItemIDValue) {
		VIPItemIDValue = vIPItemIDValue;
	}

	public String getItemIDValue() {
		return itemIDValue;
	}	

	public String getVIPItemIDValue() {
		return VIPItemIDValue;
	}	

	public Map<Integer, Integer> getItemID() {
		return itemID;
	}

	public void setItemID(String str) {
		this.itemIDValue=str;
		if (str != null && !str.isEmpty()) {
			String[] arr = str.split(",");
			this.itemID = new HashMap<Integer, Integer>();
			for (int i = 0; i < arr.length; i++) {				
				String[] tempArr = arr[i].split("=");
				itemID.put(Integer.parseInt(tempArr[0]),Integer.parseInt(tempArr[1]));
			}
		}
	}

	public Map<Integer, Integer> getVIPItemID() {
		return VIPItemID;
	}

	public void setVIPItemID(String str) {
		this.VIPItemIDValue=str;
		if (str != null && !str.isEmpty()) {
			String[] arr = str.split(",");
			this.VIPItemID = new HashMap<Integer, Integer>();
			for (int i = 0; i < arr.length; i++) {
				String[] tempArr = arr[i].split("=");
				VIPItemID.put(Integer.parseInt(tempArr[0]),Integer.parseInt(tempArr[1]));
			}
		}
	}	
	
	public int getRewardValue(int type,int key) {
		if(type==0){
			return itemID.get(key);
		}
		return VIPItemID.get(key);
	}
}
