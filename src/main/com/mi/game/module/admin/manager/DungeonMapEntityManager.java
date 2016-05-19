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
import com.mi.game.module.dungeon.dao.DungeonMapEntityDAO;
import com.mi.game.module.dungeon.pojo.Dungeon;
import com.mi.game.module.dungeon.pojo.DungeonMapEntity;

public class DungeonMapEntityManager extends BaseEntityManager<DungeonMapEntity> {

	public DungeonMapEntityManager() {
		this.dao = DungeonMapEntityDAO.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String playerID = (String) ioMessage.getInputParse("playerID");
		DungeonMapEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String dungeonMap = (String) ioMessage.getInputParse("dungeonMap");
		if (StringUtils.isNotBlank(dungeonMap)) {
			JSONObject json = JSON.parseObject(dungeonMap);
			Map<String, Dungeon> temp = new HashMap<String, Dungeon>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				Dungeon dungeon = JSON.parseObject(value, Dungeon.class);
				temp.put(key, dungeon);
			}
			entity.setDungeonMap(temp);
		}
		String lastContinuousTime = (String) ioMessage.getInputParse("lastContinuousTime");
		if (StringUtils.isNotBlank(lastContinuousTime)) {
			entity.setLastContinuousTime(Long.parseLong(lastContinuousTime));
		}
		String continuousPayNum= (String) ioMessage.getInputParse("continuousPayNum");
		if (StringUtils.isNotBlank(continuousPayNum)) {
			entity.setContinuousPayNum(Integer.parseInt(continuousPayNum));
		}
		String starNum = (String) ioMessage.getInputParse("starNum");
		if (StringUtils.isNotBlank(starNum)) {
			entity.setStarNum(Integer.parseInt(starNum));
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String playerID = (String) ioMessage.getInputParse("playerID");
		if (StringUtils.isNotBlank(playerID)) {
			queryInfo.addQueryCondition("playerID", playerID);
		}
		return dao.queryPage(queryInfo);
	}
}
