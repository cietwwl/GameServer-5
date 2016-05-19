package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.cdk.CDKModule;
import com.mi.game.module.cdk.dao.CDKEntityDao;
import com.mi.game.module.cdk.pojo.CDKEntity;

public class CDKEntityManager extends BaseEntityManager<CDKEntity> {

	public CDKEntityManager() {
		this.dao = CDKEntityDao.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		CDKModule cdkModule = ModuleManager.getModule(ModuleNames.CDKModule, CDKModule.class);
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String cdk = (String) ioMessage.getInputParse("cdk");
		CDKEntity entity = cdkModule.getCDKEntity(cdk);
		String batch = (String) ioMessage.getInputParse("batch");
		String typeID = (String) ioMessage.getInputParse("typeID");
		String platFrom = (String) ioMessage.getInputParse("platFrom");
		String rewardID = (String) ioMessage.getInputParse("rewardID");
		String startTime = (String) ioMessage.getInputParse("startTime");
		String endTime = (String) ioMessage.getInputParse("endTime");
		if (entity == null) {
			entity = new CDKEntity();
			entity.setKey(cdkModule.CDKID());
			entity.setCreateTime(System.currentTimeMillis());
			entity.setUsed(0);
			if (StringUtils.isNotEmpty(cdk)) {
				entity.setCdk(cdk);
			}
		}
		if (StringUtils.isNotEmpty(startTime)) {
			entity.setStartTime(Long.parseLong(startTime));
		}
		if (StringUtils.isNotEmpty(endTime)) {
			entity.setEndTime(Long.parseLong(endTime));
		}
		if (StringUtils.isNotEmpty(batch)) {
			entity.setBatch(batch);
		}
		if (StringUtils.isNotEmpty(typeID)) {
			entity.setTypeID(typeID);
		}
		if (StringUtils.isNotEmpty(platFrom)) {
			entity.setPlatFrom(platFrom);
		}
		if (StringUtils.isNotEmpty(rewardID)) {
			entity.setRewardID(rewardID);
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String typeID = (String) ioMessage.getInputParse("typeID");
		String platFrom = (String) ioMessage.getInputParse("platFrom");
		String cdk = (String) ioMessage.getInputParse("cdk");
		String rewardID = (String) ioMessage.getInputParse("rewardID");
		String batch = (String) ioMessage.getInputParse("batch");
		if (StringUtils.isNotEmpty(typeID)) {
			queryInfo.addQueryCondition("typeID", typeID);
		}
		if (StringUtils.isNotEmpty(platFrom)) {
			queryInfo.addQueryCondition("platFrom", platFrom);
		}
		if (StringUtils.isNotEmpty(cdk)) {
			queryInfo.addQueryCondition("cdk", cdk);
		}
		if (StringUtils.isNotEmpty(rewardID)) {
			queryInfo.addQueryCondition("rewardID", rewardID);
		}
		if (StringUtils.isNotEmpty(batch)) {
			queryInfo.addQueryCondition("batch", batch);
		}
		return dao.queryPage(queryInfo);
	}
}
