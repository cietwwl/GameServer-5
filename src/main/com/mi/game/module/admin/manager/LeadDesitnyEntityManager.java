package com.mi.game.module.admin.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.lead.dao.DestinyEntityDAO;
import com.mi.game.module.lead.pojo.HeroPrototype;
import com.mi.game.module.lead.pojo.LeadDesitnyEntity;

public class LeadDesitnyEntityManager extends BaseEntityManager<LeadDesitnyEntity> {
	public LeadDesitnyEntityManager() {
		this.dao = DestinyEntityDAO.getInstance();
	}

	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String playerID = (String) ioMessage.getInputParse("playerID");
		if (StringUtils.isNotBlank(playerID)) {
			queryInfo.addQueryCondition("playerID", playerID);
		}
		return dao.queryPage(queryInfo);
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String playerID = (String) ioMessage.getInputParse("playerID");
		LeadDesitnyEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String destinyID = (String) ioMessage.getInputParse("destinyID");
		if (StringUtils.isNotBlank(destinyID)) {
			entity.setDestinyID(Integer.parseInt(destinyID));
		}
		String point = (String) ioMessage.getInputParse("point");
		if (StringUtils.isNotBlank(point)) {
			entity.setPoint(Integer.parseInt(point));
		}
		String destinyNum = (String) ioMessage.getInputParse("destinyNum");
		if (StringUtils.isNotBlank(destinyNum)) {
			entity.setDestinyNum(Integer.parseInt(destinyNum));
		}
		String nextSilver = (String) ioMessage.getInputParse("nextSilver");
		if (StringUtils.isNotBlank(nextSilver)) {
			entity.setNextSilver(Integer.parseInt(nextSilver));
		}
		String nextPoint = (String) ioMessage.getInputParse("nextPoint");
		if (StringUtils.isNotBlank(nextPoint)) {
			entity.setNextPoint(Integer.parseInt(nextPoint));
		}
		String prototype = (String) ioMessage.getInputParse("prototype");
		if (StringUtils.isNotBlank(prototype)) {
			JSONObject temp = JSONObject.parseObject(prototype);
			Map<Integer, HeroPrototype> tempPro = new HashMap<Integer, HeroPrototype>();
			Set<Entry<String, Object>> set = temp.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				HeroPrototype hero = JSON.parseObject(value, HeroPrototype.class);
				tempPro.put(Integer.parseInt(key), hero);
			}
			entity.setPrototype(tempPro);
		}
		String nextPrototype = (String) ioMessage.getInputParse("nextPrototype");
		if (StringUtils.isNotBlank(nextPrototype)) {
			List<HeroPrototype> temp = JSON.parseArray(nextPrototype, HeroPrototype.class);
			entity.setNextPrototype(temp);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
