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
import com.mi.game.module.legion.dao.LegionMemberEntityDAO;
import com.mi.game.module.legion.pojo.LegionMemberEntity;

public class LegionMemberEntityManager extends BaseEntityManager<LegionMemberEntity> {
	public LegionMemberEntityManager() {
		this.dao = LegionMemberEntityDAO.getInstance();
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
		LegionMemberEntity legionMemberEntity = dao.getEntity(playerID);
		if (legionMemberEntity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String devote = (String) ioMessage.getInputParse("devote");
		if (StringUtils.isNotBlank(devote)) {
			legionMemberEntity.setDevote(Integer.valueOf(devote));
		}
		String maxDevote = (String) ioMessage.getInputParse("maxDevote");
		if (StringUtils.isNotBlank(maxDevote)) {
			legionMemberEntity.setMaxDevote(Integer.valueOf(maxDevote));
		}
		String buyItem = (String) ioMessage.getInputParse("buyItem");
		if (StringUtils.isNotBlank(buyItem)) {
			JSONObject json = JSON.parseObject(buyItem);
			Map<String, Integer> temp = new HashMap<String, Integer>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				temp.put(key, Integer.parseInt(value));
			}
			legionMemberEntity.setBuyItem(temp);
		}
		String buyTime = (String) ioMessage.getInputParse("buyTime");
		if (StringUtils.isNotBlank(buyTime)) {
			legionMemberEntity.setBuyTime(buyTime);
		}
		String visit = (String) ioMessage.getInputParse("visit");
		if (StringUtils.isNotBlank(visit)) {
			legionMemberEntity.setVisit(Integer.valueOf(visit));
		}
		String visitTime = (String) ioMessage.getInputParse("visitTime");
		if (StringUtils.isNotBlank(visitTime)) {
			legionMemberEntity.setVisitTime(visitTime);
		}
		String build = (String) ioMessage.getInputParse("build");
		if (StringUtils.isNotBlank(build)) {
			legionMemberEntity.setBuild(build);
		}
		String buildTime = (String) ioMessage.getInputParse("buildTime");
		if (StringUtils.isNotBlank(buildTime)) {
			legionMemberEntity.setBuildTime(buildTime);
		}
		String gemItem = (String) ioMessage.getInputParse("gemItem");
		if (StringUtils.isNotBlank(gemItem)) {
			JSONObject json = JSON.parseObject(gemItem);
			Map<String, Integer> temp = new HashMap<String, Integer>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				temp.put(key, Integer.parseInt(value));
			}
			legionMemberEntity.setGemItem(temp);
		}
		String gemTime = (String) ioMessage.getInputParse("gemTime");
		if (StringUtils.isNotBlank(gemTime)) {
			legionMemberEntity.setGemTime(gemTime);
		}
		dao.save(legionMemberEntity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
