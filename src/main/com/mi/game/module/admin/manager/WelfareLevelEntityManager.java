package com.mi.game.module.admin.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.welfare.dao.WelfareLevelEntityDao;
import com.mi.game.module.welfare.pojo.WelfareLevelEntity;

public class WelfareLevelEntityManager extends BaseEntityManager<WelfareLevelEntity> {
	public WelfareLevelEntityManager() {
		this.dao = WelfareLevelEntityDao.getInstance();
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
		WelfareLevelEntity entity = dao.getEntity(playerID);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String rewardTime = (String) ioMessage.getInputParse("rewardTime");
		if (StringUtils.isNotBlank(rewardTime)) {
			entity.setRewardTime(rewardTime);
		}
		String rewardList = (String) ioMessage.getInputParse("rewardList");
		if (StringUtils.isNotBlank(rewardList)) {
			JSONArray temp = JSON.parseArray(rewardList);
			List<Integer> tempList = new ArrayList<Integer>();
			for (int i = 0; i < temp.size(); i++) {
				tempList.add(temp.getInteger(i));
			}
			entity.setRewardList(tempList);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
