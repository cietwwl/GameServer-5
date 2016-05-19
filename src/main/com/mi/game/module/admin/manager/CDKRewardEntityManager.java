package com.mi.game.module.admin.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.cdk.CDKModule;
import com.mi.game.module.cdk.dao.CDKRewardEntityDao;
import com.mi.game.module.cdk.pojo.CDKRewardEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class CDKRewardEntityManager extends BaseEntityManager<CDKRewardEntity> {

	public CDKRewardEntityManager() {
		this.dao = CDKRewardEntityDao.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		CDKModule cdkModule = ModuleManager.getModule(ModuleNames.CDKModule, CDKModule.class);
		String rewardID = (String) ioMessage.getInputParse("rewardID");
		CDKRewardEntity entity = dao.getEntity(rewardID);
		if (entity == null) {
			entity = new CDKRewardEntity();
			entity.setRewardID(cdkModule.cdkRewardID());
		}
		String desc = (String) ioMessage.getInputParse("desc");
		if (StringUtils.isNotEmpty(desc)) {
			entity.setDesc(desc);
		}
		String items = (String) ioMessage.getInputParse("items");
		if (StringUtils.isNotEmpty(items)) {
			JSONArray jsonArray = JSON.parseArray(items);
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			for (int i = 0; i < jsonArray.size(); i++) {
				goodsList.add(JSON.parseObject(jsonArray.getString(i), GoodsBean.class));
			}
			entity.setItems(goodsList);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		queryInfo.setOrder("-rewardID");
		String rewardID = (String) ioMessage.getInputParse("rewardID");
		if (StringUtils.isNotEmpty(rewardID)) {
			queryInfo.addQueryCondition("rewardID", rewardID);
		}
		return dao.queryPage(queryInfo);
	}
}
