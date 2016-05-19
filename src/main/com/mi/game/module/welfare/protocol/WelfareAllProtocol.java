package com.mi.game.module.welfare.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.welfare.pojo.WelfareEntity;
import com.mi.game.module.welfare.pojo.WelfareLevelEntity;
import com.mi.game.module.welfare.pojo.WelfareLoginEntity;
import com.mi.game.module.welfare.pojo.WelfareMonthEntity;
import com.mi.game.module.welfare.pojo.WelfareOnlineEntity;
import com.mi.game.module.welfare.pojo.WelfareRescueSunEntity;
import com.mi.game.module.welfare.pojo.WelfareSignEntity;

public class WelfareAllProtocol extends BaseProtocol {

	private WelfareEntity welfareEntity;
	private WelfareLoginEntity loginEntity;
	private WelfareLevelEntity levelEntity;
	private WelfareOnlineEntity onlineEntity;
	private WelfareSignEntity signEntity;
	private WelfareMonthEntity monthEntity;
	private WelfareRescueSunEntity rescueSunEntity;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (welfareEntity != null) {
			response.put("welfare", welfareEntity.getWelfareList());
		}
		if (loginEntity != null) {
			response.put("login", loginEntity.responseMap());
		}
		if (levelEntity != null) {
			response.put("level", levelEntity.responseMap());
		}
		if (onlineEntity != null) {
			response.put("online", onlineEntity.responseMap());
		}
		if (signEntity != null) {
			response.put("sign", signEntity.responseMap());
		}
		if (monthEntity != null) {
			response.put("month", monthEntity.responseMap());
		}
		if(rescueSunEntity != null){
			response.put("rescueSun", rescueSunEntity.responseMap());
		}
		return response;
	}
	
	public WelfareRescueSunEntity getRescueSunEntity() {
		return rescueSunEntity;
	}

	public void setRescueSunEntity(WelfareRescueSunEntity rescueSunEntity) {
		this.rescueSunEntity = rescueSunEntity;
	}

	public WelfareEntity getWelfareEntity() {
		return welfareEntity;
	}

	public void setWelfareEntity(WelfareEntity welfareEntity) {
		this.welfareEntity = welfareEntity;
	}

	public WelfareLoginEntity getLoginEntity() {
		return loginEntity;
	}

	public void setLoginEntity(WelfareLoginEntity loginEntity) {
		this.loginEntity = loginEntity;
	}

	public WelfareLevelEntity getLevelEntity() {
		return levelEntity;
	}

	public void setLevelEntity(WelfareLevelEntity levelEntity) {
		this.levelEntity = levelEntity;
	}

	public WelfareOnlineEntity getOnlineEntity() {
		return onlineEntity;
	}

	public void setOnlineEntity(WelfareOnlineEntity onlineEntity) {
		this.onlineEntity = onlineEntity;
	}

	public WelfareSignEntity getSignEntity() {
		return signEntity;
	}

	public void setSignEntity(WelfareSignEntity signEntity) {
		this.signEntity = signEntity;
	}

	public WelfareMonthEntity getMonthEntity() {
		return monthEntity;
	}

	public void setMonthEntity(WelfareMonthEntity monthEntity) {
		this.monthEntity = monthEntity;
	}

}
