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
import com.mi.game.module.bag.dao.BagEntityDAO;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.bag.pojo.BagItem;

public class BagEntityManager extends BaseEntityManager<BagEntity> {
	public BagEntityManager() {
		this.dao = BagEntityDAO.getInstance();
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
		BagEntity bagEntity = dao.getEntity(playerID);
		if (bagEntity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String maxBagSellNum = (String) ioMessage.getInputParse("maxBagSellNum");
		String bagList = (String) ioMessage.getInputParse("bagList");
		if (StringUtils.isNotBlank(maxBagSellNum)) {
			bagEntity.setMaxBagSellNum(Integer.parseInt(maxBagSellNum));
		}
		if (StringUtils.isNotBlank(bagList)) {
			JSONObject json = JSON.parseObject(bagList);
			Map<Integer, BagItem> temp = new HashMap<Integer, BagItem>();
			Set<Entry<String, Object>> set = json.entrySet();
			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				String value = entry.getValue().toString();
				BagItem bagItem = JSON.parseObject(value, BagItem.class);
				temp.put(Integer.parseInt(key), bagItem);
			}
			bagEntity.setBagList(temp);
		}
		dao.save(bagEntity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
