package com.mi.game.module.mailBox.protocol;


import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.mailBox.pojo.MailBoxEntity;

public class MailBoxProtocol extends BaseProtocol{
	private MailBoxEntity entity;

	@Override
	public Map<String,Object> responseMap(int type){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(type){
			case HandlerIds.getMailBox:
				if(entity != null){
					data.put("mailEntity", entity.responseMap());
				}
		}
		return data;
	}
	
	
	public MailBoxEntity getEntity() {
		return entity;
	}

	public void setEntity(MailBoxEntity entity) {
		this.entity = entity;
	}
	
	
}
