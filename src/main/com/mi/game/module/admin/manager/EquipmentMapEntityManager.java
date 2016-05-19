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
import com.mi.game.module.equipment.dao.EquipmentEntityDAO;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.equipment.pojo.EquipmentShard;

public class EquipmentMapEntityManager extends BaseEntityManager<EquipmentMapEntity> {
	public EquipmentMapEntityManager() {
		this.dao = EquipmentEntityDAO.getInstance();
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
		EquipmentMapEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String maxEquipNum = (String) ioMessage.getInputParse("maxEquipNum");
		String maxFragmentNum = (String) ioMessage.getInputParse("maxFragmentNum");
		String equipMap = (String) ioMessage.getInputParse("equipMap");
		String shardList = (String) ioMessage.getInputParse("shardList");
		if (StringUtils.isNotBlank(maxEquipNum)) {
			entity.setMaxEquipNum(Integer.parseInt(maxEquipNum));
		}
		if (StringUtils.isNotBlank(maxFragmentNum)) {
			entity.setMaxFragmentNum(Integer.parseInt(maxFragmentNum));
		}
		if (StringUtils.isNotBlank(equipMap)) {
			JSONObject json = JSON.parseObject(equipMap);
			Map<String, Equipment> temp = new HashMap<String, Equipment>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				Equipment equipment = JSON.parseObject(value, Equipment.class);
				temp.put(key, equipment);
			}
			entity.setEquipMap(temp);
		}
		if (StringUtils.isNotBlank(shardList)) {
			JSONObject json = JSON.parseObject(shardList);
			Map<Integer, EquipmentShard> temp = new HashMap<Integer, EquipmentShard>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				EquipmentShard equipmentShard = JSON.parseObject(value, EquipmentShard.class);
				temp.put(Integer.parseInt(key), equipmentShard);
			}
			entity.setShardList(temp);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
