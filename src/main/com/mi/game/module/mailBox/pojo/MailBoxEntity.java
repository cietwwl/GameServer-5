package com.mi.game.module.mailBox.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class MailBoxEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2206590803876481971L;
	@Indexed(value=IndexDirection.ASC, unique=true)
	private String playerID;
	private List<MailInfo> mailList;
	private int counter;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		if(mailList != null){
			data.put("mailList",mailList);
		}
		return data;
	}
	
	public int addCounter(){
		counter += 1;
		return counter;
	}
	public MailInfo getMainInfo(int index){
		if(mailList != null){
			for(MailInfo item:mailList){
				if(item.getIndex() == index){
					return item;
				}
			}
		}
		return null;
	}
	
	public void removeMsgBoxItem(int index){
		if(mailList != null){
			for(MailInfo item:mailList){
				if(item.getIndex() == index){
					mailList.remove(item);
					break;
				}
			}
		}
	}
	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public List<MailInfo> getMailList() {
		if(mailList == null){
			mailList = new ArrayList<>();
		}
		return mailList;
	}

	public void setMailList(List<MailInfo> mailList) {
		this.mailList = mailList;
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
