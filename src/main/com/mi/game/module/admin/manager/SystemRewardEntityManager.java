package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.dao.SystemRewardEntityDAO;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.reward.pojo.SystemRewardEntity;

public class SystemRewardEntityManager extends BaseEntityManager<SystemRewardEntity> {

	public SystemRewardEntityManager() {
		this.dao = SystemRewardEntityDAO.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		String rewardKey = (String) ioMessage.getInputParse("rewardKey");
		SystemRewardEntity entity = rewardModule.getSystemRewardEntity2(rewardKey);

//		String rewardID = (String) ioMessage.getInputParse("rewardID");
//		if (entity != null && StringUtils.isNotEmpty(rewardID) && "-1".equals(rewardID)) {
//			protocol.put("code", 0);
//			protocol.put("result", ResponseResult.NULL);
//			ioMessage.setOutputResult(protocol);
//			return;
//		}

		if (entity == null) {
			entity = new SystemRewardEntity();
			entity.setRewardID(rewardModule.getSystemRewardID() + "");
			entity.setCreateTime(System.currentTimeMillis());
			entity.setRewardKey(rewardKey);
		}
		String desc = (String) ioMessage.getInputParse("desc");
		if (StringUtils.isNotEmpty(desc)) {
			entity.setDesc(desc);
		}
		String goodsListString = (String) ioMessage.getInputParse("goodsList");
		List<GoodsBean> goodsList = null;
		if (StringUtils.isNotEmpty(goodsListString)) {
			goodsList = JSON.parseArray(goodsListString, GoodsBean.class);
			entity.setGoodsList(goodsList);
		}
		rewardModule.saveSystemRewardEntity(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String rewardKey = (String) ioMessage.getInputParse("rewardKey");
		String rewardID = (String) ioMessage.getInputParse("rewardID");
		if (StringUtils.isNotEmpty(rewardKey)) {
			queryInfo.addQueryCondition("rewardKey", rewardKey);
		}
		if (StringUtils.isNotEmpty(rewardID)) {
			queryInfo.addQueryCondition("rewardID", rewardID);
		}
		List<? extends BaseEntity> queryList = this.dao.queryList(queryInfo);
		return queryList;
	}

}
