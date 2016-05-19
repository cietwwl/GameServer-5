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
import com.mi.game.module.dungeon.dao.DungeonActMapEntityDAO;
import com.mi.game.module.dungeon.pojo.DungeonAct;
import com.mi.game.module.dungeon.pojo.DungeonActMapEntity;

public class DungeonActMapEntityManager extends BaseEntityManager<DungeonActMapEntity> {

	public DungeonActMapEntityManager() {
		this.dao = DungeonActMapEntityDAO.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String playerID = (String) ioMessage.getInputParse("playerID");
		DungeonActMapEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String dungeMap = (String) ioMessage.getInputParse("dungeMap");
		if (StringUtils.isNotBlank(dungeMap)) {
			JSONObject json = JSON.parseObject(dungeMap);
			Map<Integer, DungeonAct> temp = new HashMap<Integer, DungeonAct>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				DungeonAct dungeonAct = JSON.parseObject(value, DungeonAct.class);
				temp.put(Integer.parseInt(key), dungeonAct);
			}
			entity.setDungeMap(temp);
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
