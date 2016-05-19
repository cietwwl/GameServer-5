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
import com.mi.game.module.talisman.dao.TalismanMapDAO;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;

public class TalismanMapEntityManager extends BaseEntityManager<TalismanMapEntity> {
	public TalismanMapEntityManager() {
		this.dao = TalismanMapDAO.getInstance();
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
		TalismanMapEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String talismanMap = (String) ioMessage.getInputParse("talismanMap");
		String maxTalismanNum = (String) ioMessage.getInputParse("maxTalismanNum");
		if(StringUtils.isNotEmpty(maxTalismanNum)){
			entity.setMaxTalismanNum(Integer.parseInt(maxTalismanNum));
		}
		if(StringUtils.isNotEmpty(talismanMap)){
			JSONObject json = JSON.parseObject(talismanMap);
			Map<String, TalismanEntity> temp = new HashMap<String, TalismanEntity>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				TalismanEntity tailsman = JSON.parseObject(value, TalismanEntity.class);
				temp.put(key, tailsman);
			}
			entity.setTalismanMap(temp);
		}
		
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
