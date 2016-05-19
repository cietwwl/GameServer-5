package com.mi.game.module.admin.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.tower.dao.TowerEntityDAO;
import com.mi.game.module.tower.pojo.HideInfo;
import com.mi.game.module.tower.pojo.TowerEntity;

public class TowerEntityManager extends BaseEntityManager<TowerEntity> {
	public TowerEntityManager() {
		this.dao = TowerEntityDAO.getInstance();
	}

	public List<? extends TowerEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
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
		TowerEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String maxLevel = (String) ioMessage.getInputParse("maxLevel");
		if (StringUtils.isNotEmpty(maxLevel)) {
			entity.setMaxLevel(Integer.parseInt(maxLevel));
		}
		String nowLevel = (String) ioMessage.getInputParse("nowLevel");
		if (StringUtils.isNotEmpty(nowLevel)) {
			entity.setNowLevel(Integer.parseInt(nowLevel));
		}
		String passLevel = (String) ioMessage.getInputParse("passLevel");
		if (StringUtils.isNotEmpty(passLevel)) {
			entity.setPassLevel(Integer.parseInt(passLevel));
		}
		String heartNum = (String) ioMessage.getInputParse("heartNum");
		if (StringUtils.isNotEmpty(heartNum)) {
			entity.setHeartBuyNum(Integer.parseInt(heartNum));
		}
		String freeResetNum = (String) ioMessage.getInputParse("freeResetNum");
		if (StringUtils.isNotEmpty(freeResetNum)) {
			entity.setFreeResetNum(Integer.parseInt(freeResetNum));
		}
		String lastUpdateTime = (String) ioMessage.getInputParse("lastUpdateTime");
		if (StringUtils.isNotEmpty(lastUpdateTime)) {
			entity.setLastUpdateTime(Long.parseLong(lastUpdateTime));
		}
		String payResetNum = (String) ioMessage.getInputParse("payResetNum");
		if (StringUtils.isNotEmpty(payResetNum)) {
			entity.setPayResetNum(Integer.parseInt(payResetNum));
		}
		String maxPayResetNum = (String) ioMessage.getInputParse("maxPayResetNum");
		if (StringUtils.isNotEmpty(maxPayResetNum)) {
			entity.setMaxPayResetNum(Integer.parseInt(maxPayResetNum));
		}
		String overTime = (String) ioMessage.getInputParse("overTime");
		if (StringUtils.isNotEmpty(overTime)) {
			entity.setOverTime(Long.parseLong(overTime));
		}
		String lock = (String) ioMessage.getInputParse("lock");
		if (StringUtils.isNotEmpty(lock)) {
			entity.setLock(Boolean.parseBoolean(lock));
		}
		String clear = (String) ioMessage.getInputParse("clear");
		if (StringUtils.isNotEmpty(clear)) {
			entity.setClear(Boolean.parseBoolean(clear));
		}
		String heartBuyNum = (String) ioMessage.getInputParse("heartBuyNum");
		if (StringUtils.isNotEmpty(heartBuyNum)) {
			entity.setHeartBuyNum(Integer.parseInt(heartBuyNum));
		}
		String lastAddTowerTime = (String) ioMessage.getInputParse("lastAddTowerTime");
		if (StringUtils.isNotEmpty(lastAddTowerTime)) {
			entity.setLastAddTowerTime(Long.parseLong(lastAddTowerTime));
		}
		String hideCounter = (String) ioMessage.getInputParse("hideCounter");
		if (StringUtils.isNotEmpty(hideCounter)) {
			entity.setHideCounter(Long.parseLong(hideCounter));
		}
		String hideList = (String) ioMessage.getInputParse("hideList");
		if (StringUtils.isNotBlank(hideList)) {
			JSONArray json = JSON.parseArray(hideList);
			List<HideInfo> temp = new ArrayList<HideInfo>();
			for (int i = 0; i < json.size(); i++) {
				HideInfo hideInfo = JSON.parseObject(json.getString(i), HideInfo.class);
				temp.add(hideInfo);
			}
			entity.setHideList(temp);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
